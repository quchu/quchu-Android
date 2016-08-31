package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.FeedbackModel;

/**
 * 用户反馈详情适配器
 * <p/>
 * Created by mwb on 16/8/29.
 */
public class FeedbackDetailAdapter
    extends AdapterBase<FeedbackModel.MsgListBean, FeedbackDetailAdapter.FeedbackDetailViewHolder> {

  private Context mContext;
  private String mIAvatar;
  private String mYAvatar;

  public FeedbackDetailAdapter(Context context) {
    mContext = context;
  }

  @Override
  public void onBindView(FeedbackDetailViewHolder holder, int position) {
    if (data == null || data.size() == 0) {
      return;
    }

    FeedbackModel.MsgListBean msgListBean = data.get(position);
    if (msgListBean.getType().equals("0")) {
      //自己发出的消息
      holder.contentTv.setText(msgListBean.getContent());
      holder.contentTv.setBackgroundResource(R.drawable.rc_ic_bubble_right);
      holder.leftAvatarImg.setVisibility(View.INVISIBLE);
      holder.rightAvatarImg.setVisibility(View.VISIBLE);
      if (TextUtils.isEmpty(mIAvatar)) {
        holder.rightAvatarImg.setImageURI("");
      } else {
        holder.rightAvatarImg.setImageURI(mIAvatar);
      }

    } else {
      holder.contentTv.setText(msgListBean.getContent());
      holder.contentTv.setBackgroundResource(R.drawable.rc_ic_bubble_left);
      holder.rightAvatarImg.setVisibility(View.INVISIBLE);
      holder.leftAvatarImg.setVisibility(View.VISIBLE);
      if (TextUtils.isEmpty(mYAvatar)) {
        holder.leftAvatarImg.setImageURI("");
      } else {
        holder.leftAvatarImg.setImageURI(mIAvatar);
      }
    }
  }

  @Override
  public RecyclerView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
    return new FeedbackDetailViewHolder(
        LayoutInflater.from(mContext).inflate(R.layout.item_feedback_detail, parent, false));
  }

  public void setAvatar(String iAvatar, String yAvatar) {
    mIAvatar = iAvatar;
    mYAvatar = yAvatar;
  }

  public class FeedbackDetailViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.feedback_content_tv)
    TextView contentTv;
    @Bind(R.id.feedback_left_avatar_img)
    SimpleDraweeView leftAvatarImg;
    @Bind(R.id.feedback_right_avatar_img)
    SimpleDraweeView rightAvatarImg;

    public FeedbackDetailViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
