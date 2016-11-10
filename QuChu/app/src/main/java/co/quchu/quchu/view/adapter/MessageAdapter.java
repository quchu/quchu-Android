package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.net.Uri;
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
import co.quchu.quchu.model.MessageModel;

/**
 * Created by mwb on 16/11/5.
 */
public class MessageAdapter extends AdapterBase<MessageModel.ResultBean, RecyclerView.ViewHolder> {

  private Context mContext;

  public MessageAdapter(Context context) {
    mContext = context;
  }

  @Override
  public void onBindView(RecyclerView.ViewHolder viewHolder, int position) {
    MessageModel.ResultBean model = data.get(position);
    MessageViewHolder holder = (MessageViewHolder) viewHolder;

    holder.mDateTv.setText(model.getTime());
    if (TextUtils.isEmpty(model.getTargetImageUrl())) {
      holder.mCoverImg.setVisibility(View.GONE);
      holder.mTextTv.setText(model.getContent());
    } else {
      holder.mCoverImg.setVisibility(View.VISIBLE);
      holder.mCoverImg.setImageURI(Uri.parse(model.getTargetImageUrl()));
      holder.mTextTv.setText(model.getContent());
    }

    holder.itemView.setTag(model);
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        MessageModel.ResultBean model = (MessageModel.ResultBean) v.getTag();
        if (model != null && mListener != null) {
          mListener.onItemClick(model);
        }
      }
    });
  }

  @Override
  public RecyclerView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
    return new MessageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_message, parent, false));
  }

  @Override
  protected int getFooterBackgroundColor() {
    return R.color.colorBackground;
  }

  public class MessageViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.item_message_date_tv) TextView mDateTv;
    @Bind(R.id.item_message_cover_img) SimpleDraweeView mCoverImg;
    @Bind(R.id.item_message_text_tv) TextView mTextTv;

    public MessageViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  private OnMessageListListener mListener;

  public void setOnMessageistListener(OnMessageListListener listener) {
    mListener = listener;
  }

  public interface OnMessageListListener {
    void onItemClick(MessageModel.ResultBean model);
  }
}
