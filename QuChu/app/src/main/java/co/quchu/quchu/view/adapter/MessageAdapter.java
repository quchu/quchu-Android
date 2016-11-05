package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
  public void onBindView(RecyclerView.ViewHolder holder, int position) {
    MessageModel.ResultBean model = data.get(position);
  }

  @Override
  public RecyclerView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
    return new MessageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_message, parent, false));
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
}
