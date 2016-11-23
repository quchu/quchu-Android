package co.quchu.quchu.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Animatable;
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
import co.quchu.quchu.view.activity.QuchuDetailsActivity;
import co.quchu.quchu.widget.CardsPagerTransformerBasic;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import java.util.List;

/**
 * Created by Nico on 16/8/26.
 */
public class AIConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  public static final int TYPE_QUESTION = 0x001;
  public static final int TYPE_ANSWER = 0x002;
  //public static final int TYPE_OPTION = 0x003;
  public static final int TYPE_GALLERY_OPTION = 0x004;
  public static final int TYPE_SPACE = 0x006;


  private Activity mAnchor;
  private List<AIConversationModel> mDataSet;
  private co.quchu.quchu.view.adapter.TextOptionAdapter.OnInteractiveClick mOnInteractiveListener;



  public AIConversationAdapter(Activity context, List<AIConversationModel> data,
      co.quchu.quchu.view.adapter.TextOptionAdapter.OnInteractiveClick onAnswerListener) {
    this.mAnchor = context;
    this.mDataSet = data;
    this.mOnInteractiveListener = onAnswerListener;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    switch (viewType) {
      case TYPE_QUESTION:
        return new QuestionViewHolder(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_ai_conversation_question, parent, false));
      case TYPE_ANSWER:
        return new AnswerViewHolder(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_ai_conversation_answer, parent, false));
      //case TYPE_OPTION:
      //  return new OptionViewHolder(LayoutInflater.from(parent.getContext())
      //      .inflate(R.layout.item_ai_conversation_option, parent, false));
      case TYPE_GALLERY_OPTION:
        return new GalleryViewHolder(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_ai_conversation_gallery_option, parent, false));
      //case TYPE_NO_NETWORK:
      //  return new NoNetworkViewHolder(LayoutInflater.from(parent.getContext())
      //      .inflate(R.layout.item_ai_conversation_no_network, parent, false));
      default:
        return new QuchuDetailsAdapter.BlankViewHolder(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_space,parent,false));


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

          if (null!=q.getAnswer() && q.getAnswer().startsWith("http")){
            ((QuestionViewHolder) holder).sdvImage.setVisibility(View.VISIBLE);
            ((QuestionViewHolder) holder).tvQuestion.setVisibility(View.GONE);

            Uri uri = Uri.parse(q.getAnswer());

            ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
              @Override
              public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null) {
                  return;
                }

                ViewGroup.LayoutParams lp = ((QuestionViewHolder) holder).sdvImage.getLayoutParams();
                lp.width = imageInfo.getWidth();
                lp.height = imageInfo.getHeight();
                ((QuestionViewHolder) holder).sdvImage.requestLayout();
                if (animatable != null) {
                  // app-specific logic to enable animation starting
                  animatable.start();
                }
              }
            };

            DraweeController draweeController =
                Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setControllerListener(controllerListener)
                    .setAutoPlayAnimations(true) // 设置加载图片完成后是否直接进行播放
                    .build();

            ((QuestionViewHolder) holder).sdvImage.setController(draweeController);
          }else{
            ((QuestionViewHolder) holder).sdvImage.setVisibility(View.GONE);
            ((QuestionViewHolder) holder).tvQuestion.setVisibility(View.VISIBLE);

          }

          Uri xiaoQLogoUri = new Uri.Builder()
              .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
              .path(String.valueOf(R.mipmap.ic_xiaoq_logo))
              .build();
          ((QuestionViewHolder) holder).tvQuestion.setText(q.getAnswer());
          ((QuestionViewHolder) holder).sdvAvatar.setImageURI(xiaoQLogoUri);
          ((QuestionViewHolder) holder).sdvAvatar.setVisibility(View.INVISIBLE);
          if (position>0){
            if (mDataSet.get(position-1).getDataType()!= AIConversationModel.EnumDataType.QUESTION){
              ((QuestionViewHolder) holder).sdvAvatar.setVisibility(View.VISIBLE);
            }
          }else if(position==0){
            ((QuestionViewHolder) holder).sdvAvatar.setVisibility(View.VISIBLE);
          }



          break;
        case TYPE_ANSWER:
          System.out.println("type answer");

          ((AnswerViewHolder) holder).tvAnswer.setText(q.getAnswer());
          if (AppContext.user.getGeneAvatar()!=-1){

            Uri userAvatar = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(AppContext.user.getGeneAvatar()))
                .build();
            ((AnswerViewHolder) holder).sdvAvatar.setImageURI(userAvatar);
          }else{
            ((AnswerViewHolder) holder).sdvAvatar.setImageURI(Uri.parse(AppContext.user.getPhoto()));
          }

          break;
        //case TYPE_OPTION:
        //
        //  holder.itemView.setVisibility(View.INVISIBLE);
        //  ((OptionViewHolder) holder).rvOption.setItemAnimator(new DefaultItemAnimator());
        //  TextOptionAdapter adapter = new TextOptionAdapter(q.getAnswerPramms(), q.getFlash());
        //  ((OptionViewHolder) holder).rvOption.setAdapter(adapter);
        //  ((OptionViewHolder) holder).rvOption.setLayoutManager(
        //      new LinearLayoutManager(mAnchor, LinearLayoutManager.VERTICAL, false));
        //
        //  break;

        case TYPE_GALLERY_OPTION:
          System.out.println("type gallery");

          if (null != q.getPlaceList() && q.getPlaceList().size() > 0) {
            ((GalleryViewHolder) holder).vpPlace.setAdapter(new PlaceVPAdapter(q.getPlaceList()));
            ((GalleryViewHolder) holder).vpPlace.setVisibility(View.VISIBLE);
            ((GalleryViewHolder) holder).vpPlace.setPageTransformer(false,new CardsPagerTransformerBasic(0,0,.9f,0));

            ((GalleryViewHolder) holder).vpPlace.setClipToPadding(false);
            ((GalleryViewHolder) holder).vpPlace.setPadding(80, 0, 80, 20);
            ((GalleryViewHolder) holder).vpPlace.setPageMargin(0);
          } else {
            ((GalleryViewHolder) holder).vpPlace.setVisibility(View.GONE);
          }

          break;
        //case TYPE_NO_NETWORK:
        //  ((NoNetworkViewHolder)holder).tvRetry.setOnClickListener(new View.OnClickListener() {
        //    @Override public void onClick(View v) {
        //      if (null!=mOnInteractiveListener){
        //        mOnInteractiveListener.onRetry();
        //      }
        //    }
        //  });
        //
        //  ((NoNetworkViewHolder) holder).tvSearch.setOnClickListener(new View.OnClickListener() {
        //    @Override public void onClick(View v) {
        //      if (null!=mOnInteractiveListener){
        //        mOnInteractiveListener.onSearch();
        //      }
        //    }
        //  });
        //
        //  break;
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
        //case OPTION:
        //  return TYPE_OPTION;
        case GALLERY:
          return TYPE_GALLERY_OPTION;
        //case NO_NETWORK:
        //  return TYPE_NO_NETWORK;
        default:
          return TYPE_SPACE;
      }
    }


  }

  public static class QuestionViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.tvQuestion) TextView tvQuestion;
    @Bind(R.id.vSpace) View vSpace;
    @Bind(R.id.sdvAvatar) SimpleDraweeView sdvAvatar;
    @Bind(R.id.sdvImage) SimpleDraweeView sdvImage;

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

    @Bind(R.id.tvRetry) TextView tvRetry;
    @Bind(R.id.tvSearch) TextView tvSearch;

    NoNetworkViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
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


      tvTitle.setText(dataObj.getDescribed());
      tvSubTitle.setText(dataObj.getName());
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
