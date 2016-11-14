package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.FeedbackModel;

/**
 * 用户反馈列表适配器
 * <p/>
 * Created by mwb on 16/8/23.
 */
public class FeedbackAdapter extends AdapterBase<FeedbackModel, RecyclerView.ViewHolder> {

  private Context context;

  public FeedbackAdapter(Context context) {
    this.context = context;
  }

  @Override
  public boolean getFeedback() {
    return true;
  }

  @Override
  public boolean hideFooter() {
    return true;
  }

  @Override
  public void onBindView(RecyclerView.ViewHolder viewHolder, int position) {
    FeedbackModel feedbackModel = data.get(position);

    final FeedbackViewHolder holder = (FeedbackViewHolder) viewHolder;
    holder.createTimeTv.setText(feedbackModel.getCreateDate());
    holder.titleTv.setText(feedbackModel.getTitle());
    holder.contentTv.setText(feedbackModel.getValue());
    holder.chatTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    holder.chatTv.getPaint().setAntiAlias(true);
    if (feedbackModel.getState().equals("1")) {
      holder.settleTv.setText("已解决");
      holder.settleTv.setBackground(context.getResources().getDrawable(R.drawable.shape_feedback_settle_btn));
    } else {
      holder.settleTv.setText("未解决");
      holder.settleTv.setBackground(context.getResources().getDrawable(R.drawable.shape_feedback_unsettle_btn));
    }

//    if (feedbackModel.getState().equals("2")) {
      holder.chatLayout.setVisibility(View.VISIBLE);
      holder.unReadTv.setVisibility(feedbackModel.getFeedmsgCount() > 0 ? View.VISIBLE : View.INVISIBLE);
//    } else {
//      holder.chatLayout.setVisibility(View.GONE);
//    }

    //和pm聊天
    holder.chatLayout.setTag(feedbackModel);
    holder.chatLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        FeedbackModel feedbackModel = (FeedbackModel) holder.chatLayout.getTag();
        if (feedbackModel != null && listener != null) {
          listener.onItemClick(feedbackModel, holder.unReadTv);
        }
      }
    });

    //长按删除
    holder.itemView.setTag(feedbackModel);
    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        FeedbackModel feedbackModel = (FeedbackModel) v.getTag();
        if (feedbackModel != null && listener != null) {
          listener.onItemLongClick(feedbackModel);
        }
        return true;
      }
    });
  }

  @Override
  public RecyclerView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
    return new FeedbackViewHolder(LayoutInflater.from(context).inflate(R.layout.item_feedback, parent, false));
  }

  public class FeedbackViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.feedback_item_create_time_tv) TextView createTimeTv;
    @Bind(R.id.feedback_item_title_tv) TextView titleTv;
    @Bind(R.id.feedback_item_content_tv) TextView contentTv;
    @Bind(R.id.feedback_item_settle_tv) TextView settleTv;
    @Bind(R.id.feedback_item_chat_tv) TextView chatTv;
    @Bind(R.id.feedback_item_unread_tv) TextView unReadTv;
    @Bind(R.id.feedback_item_chat_layout) LinearLayout chatLayout;

    public FeedbackViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  private OnFeedbackItemClickListener listener;

  public void setOnFeedbackItemClickListener(OnFeedbackItemClickListener listener) {
    this.listener = listener;
  }

  public interface OnFeedbackItemClickListener {
    void onItemClick(FeedbackModel feedbackModel, TextView textView);

    void onItemLongClick(FeedbackModel feedbackModel);
  }
}
