package co.quchu.quchu.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.model.AIConversationModel;
import co.quchu.quchu.model.DetailModel;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.activity.QuchuDetailsActivity;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.List;

/**
 * Created by Nico on 16/8/26.
 */
public class AIConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  public static final int TYPE_QUESTION = 0x001;
  public static final int TYPE_ANSWER = 0x002;
  public static final int TYPE_OPTION = 0x003;
  public static final int TYPE_GALLERY_OPTION = 0x004;
  public static final int TYPE_NO_NETWORK = 0x005;
  public static final int TYPE_SPACE = 0x006;


  private Activity mAnchor;
  private List<AIConversationModel> mDataSet;
  private OnAnswerListener mOnAnswerListener;

  public void updateNoNetwork(boolean noNetWork) {
    if (noNetWork){

      if (mDataSet.size()>0){
        for (int i = 0; i < mDataSet.size(); i++) {
          if (mDataSet.get(i).getDataType()== AIConversationModel.EnumDataType.NO_NETWORK){
            mDataSet.remove(i);
          }
        }

      }
      AIConversationModel aiConversationModel = new AIConversationModel();
      aiConversationModel.setDataType(AIConversationModel.EnumDataType.NO_NETWORK);
      mDataSet.add(aiConversationModel);

    }else{
      for (int i = 0; i < mDataSet.size(); i++) {
        if (mDataSet.get(i).getDataType()== AIConversationModel.EnumDataType.NO_NETWORK){
          mDataSet.remove(i);
        }
      }

    }

    notifyDataSetChanged();

  }

  public interface OnAnswerListener {
    void onAnswer(String answer, String additionalShit);
  }

  public AIConversationAdapter(Activity context, List<AIConversationModel> data,
      OnAnswerListener onAnswerListener) {
    this.mAnchor = context;
    this.mDataSet = data;
    this.mOnAnswerListener = onAnswerListener;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    switch (viewType) {
      case TYPE_QUESTION:
        return new QuestionViewHolder(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_ai_conversation_question, parent, false));
      case TYPE_ANSWER:
        return new AnswerViewHolder(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_ai_conversation_answer, parent, false));
      case TYPE_OPTION:
        return new OptionViewHolder(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_ai_conversation_option, parent, false));
      case TYPE_GALLERY_OPTION:
        return new GalleryViewHolder(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_ai_conversation_gallery_option, parent, false));
      case TYPE_NO_NETWORK:
        return new NoNetworkViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ai_conversation_no_network, parent, false));
      default:
        return new QuchuDetailsAdapter.BlankViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_space,parent,false));


    }
  }

  @Override public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

    if (position>=mDataSet.size()){

    }else{

      AIConversationModel q = mDataSet.get(position);
      switch (getItemViewType(position)) {
        case TYPE_QUESTION:

          if (position == 0) {
            ((QuestionViewHolder) holder).vSpace.setVisibility(View.VISIBLE);
          } else {
            ((QuestionViewHolder) holder).vSpace.setVisibility(View.GONE);
          }

          ((QuestionViewHolder) holder).tvQuestion.setText(q.getAnswer());
          break;
        case TYPE_ANSWER:
          ((AnswerViewHolder) holder).tvAnswer.setText(q.getAnswer());
          ((AnswerViewHolder) holder).sdvAvatar.setImageURI(Uri.parse(AppContext.user.getPhoto()));
          break;
        case TYPE_OPTION:


          ((OptionViewHolder) holder).rvOption.setItemAnimator(new DefaultItemAnimator());
          TextOptionAdapter adapter = new TextOptionAdapter(q.getAnswerPramms(), q.getFlash());
          ((OptionViewHolder) holder).rvOption.setAdapter(adapter);
          ((OptionViewHolder) holder).rvOption.setLayoutManager(
              new LinearLayoutManager(mAnchor, LinearLayoutManager.VERTICAL, false));

          break;

        case TYPE_GALLERY_OPTION:
          if (null != q.getPlaceList() && q.getPlaceList().size() > 0) {
            ((GalleryViewHolder) holder).vpPlace.setAdapter(new PlaceVPAdapter(q.getPlaceList()));
            ((GalleryViewHolder) holder).vpPlace.setVisibility(View.VISIBLE);

            ((GalleryViewHolder) holder).vpPlace.setClipToPadding(false);
            ((GalleryViewHolder) holder).vpPlace.setPadding(40, 0, 40, 20);
            ((GalleryViewHolder) holder).vpPlace.setPageMargin(0);
            ((GalleryViewHolder) holder).vpPlace.setCurrentItem(q.getPlaceList().size());
          } else {
            ((GalleryViewHolder) holder).vpPlace.setVisibility(View.GONE);
          }

          break;
        case TYPE_NO_NETWORK:


          break;
      }
    }
  }

  @Override public int getItemCount() {

    int count = null!=mDataSet?mDataSet.size():0;
    return count+1;
  }

  @Override public int getItemViewType(int position) {

    if (position>=mDataSet.size()){
      return TYPE_SPACE;
    }else{
      switch (mDataSet.get(position).getDataType()) {
        case QUESTION:
          return TYPE_QUESTION;
        case ANSWER:
          return TYPE_ANSWER;
        case OPTION:
          return TYPE_OPTION;
        case GALLERY:
          return TYPE_GALLERY_OPTION;
        case NO_NETWORK:
          return TYPE_NO_NETWORK;
        default:
          return TYPE_SPACE;
      }
    }


  }

  public static class QuestionViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.tvQuestion) TextView tvQuestion;
    @Bind(R.id.vSpace) View vSpace;

    QuestionViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  public static class AnswerViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.tvAnswer) TextView tvAnswer;
    @Bind(R.id.sdvAvatar) SimpleDraweeView sdvAvatar;

    AnswerViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  public static class OptionViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.rvOption) RecyclerView rvOption;

    OptionViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  public static class GalleryViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.vpPlace) ViewPager vpPlace;

    GalleryViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  public static class NoNetworkViewHolder extends RecyclerView.ViewHolder {

    NoNetworkViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  private class TextOptionAdapter extends RecyclerView.Adapter<TextOptionViewHolder> {

    private List<String> options;
    private String additionalShit;

    public TextOptionAdapter(List<String> options, String additional) {
      this.options = options;
      this.additionalShit = additional;
    }

    @Override public TextOptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new TextOptionViewHolder(LayoutInflater.from(parent.getContext())
          .inflate(R.layout.item_ai_conversation_txt_opt, parent, false));
    }

    @Override public void onBindViewHolder(TextOptionViewHolder holder, int position) {
      String s = Character.toString ((char) (65+position));
      final String answer = String.valueOf(options.get(position));
      holder.tvOption.setText(s+": "+answer);
      holder.tvOption.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          mOnAnswerListener.onAnswer(answer, additionalShit);
        }
      });
    }

    @Override public int getItemCount() {
      return null != options ? options.size() : 0;
    }
  }

  public class TextOptionViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.tvOption) TextView tvOption;

    public TextOptionViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  public class PlaceVPAdapter extends PagerAdapter {

    List<DetailModel> mData;

    public PlaceVPAdapter(List<DetailModel> pData) {
      mData = pData;
    }

    @Override public int getCount() {
      return null != mData ? mData.size() : 0;
    }

    @Override public boolean isViewFromObject(View view, Object object) {
      return view == object;
    }

    @Override public Object instantiateItem(ViewGroup container, final int position) {
      View v = LayoutInflater.from(container.getContext())
          .inflate(R.layout.item_ai_conversation_place, container, false);


      DetailModel dataObj = mData.get(position);
      SimpleDraweeView sdv = (SimpleDraweeView) v.findViewById(R.id.history_cover_img);

      TextView tvTitle = (TextView) v.findViewById(R.id.history_title_tv);
      TextView tvSubTitle = (TextView) v.findViewById(R.id.history_subtitle_tv);
      TextView tvInfo = (TextView) v.findViewById(R.id.history_tag_tv);


      tvTitle.setText(dataObj.getName());
      tvSubTitle.setText(dataObj.getAreaCircleName());
      String tagsString = "";
      if (null != dataObj.getTags()) {
        for (int i = 0; i < dataObj.getTags().size(); i++) {
          tagsString += dataObj.getTags().get(i).getZh();
          tagsString += (i + 1) < dataObj.getTags().size() ? " | " : "";
        }
      }
      tvInfo.setText(tagsString);

      sdv.setImageURI(Uri.parse(dataObj.getCover()));
      v.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          Intent intent = new Intent(mAnchor, QuchuDetailsActivity.class);
          intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, mData.get(position).getPid());
          mAnchor.startActivity(intent);
        }
      });
      container.addView(v);
      return v;
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
      container.removeView((View) object);
    }
  }
}
