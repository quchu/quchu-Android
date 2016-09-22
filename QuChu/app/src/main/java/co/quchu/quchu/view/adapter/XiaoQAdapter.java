package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.SysMessage;

/**
 * Created by mwb on 16/9/20.
 */
public class XiaoQAdapter extends AdapterBase<SysMessage, XiaoQAdapter.XiaoQViewHolder> {

  private final Context mContext;

  public XiaoQAdapter(Context context) {
    mContext = context;
  }

  @Override
  public void onBindView(XiaoQViewHolder holder, int position) {
    SysMessage message = data.get(position);
    switch (message.getType()) {
      case SysMessage.TYPE_QUCHU_DETAIL:
        holder.mPlaceLayout.setVisibility(View.VISIBLE);
        holder.mUserLayout.setVisibility(View.GONE);

        if (TextUtils.isEmpty(message.getImage())) {
          holder.mPlaceImg.setImageURI("");
        } else {
          holder.mPlaceImg.setImageURI(message.getImage());
        }
        holder.mPlaceTv.setText(message.getName());
        holder.mPlaceOriginTv.setVisibility(View.VISIBLE);
        break;

      case SysMessage.TYPE_USER:
        holder.mPlaceLayout.setVisibility(View.GONE);
        holder.mUserLayout.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(message.getImage())) {
          holder.mUserAvatarImg.setImageURI("");
        } else {
          holder.mUserAvatarImg.setImageURI(message.getImage());
        }
        holder.mUserNameTv.setText(message.getName());
        break;

      case SysMessage.TYPE_ARTICLE_DETAIL:
        holder.mPlaceLayout.setVisibility(View.VISIBLE);
        holder.mUserLayout.setVisibility(View.GONE);

        if (TextUtils.isEmpty(message.getImage())) {
          holder.mPlaceImg.setImageURI("");
        } else {
          holder.mPlaceImg.setImageURI(message.getImage());
        }
        holder.mPlaceTv.setText(message.getName());
        holder.mPlaceOriginTv.setVisibility(View.GONE);
        break;
    }

    holder.mItemView.setTag(message);
    holder.mItemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        SysMessage message = (SysMessage) view.getTag();
        if (message != null && mListener != null) {
          mListener.onItemClick(message);
        }
      }
    });
  }

  @Override
  public RecyclerView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
    return new XiaoQViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_xiaoq, parent, false));
  }

  public class XiaoQViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.avatarImg) SimpleDraweeView mAvatarImg;
    @Bind(R.id.placeTv) TextView mPlaceTv;
    @Bind(R.id.placeImg) SimpleDraweeView mPlaceImg;
    @Bind(R.id.placeOriginTv) TextView mPlaceOriginTv;
    @Bind(R.id.placeLayout) LinearLayout mPlaceLayout;
    @Bind(R.id.userAvatarImg) SimpleDraweeView mUserAvatarImg;
    @Bind(R.id.userNameTv) TextView mUserNameTv;
    @Bind(R.id.userLayout) RelativeLayout mUserLayout;
    @Bind(R.id.itemView) RelativeLayout mItemView;

    public XiaoQViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  private OnMessageItemClickListener mListener;

  public void setOnMessageItemClickListener(OnMessageItemClickListener listener) {
    mListener = listener;
  }

  public interface OnMessageItemClickListener {
    void onItemClick(SysMessage message);
  }
}
