package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
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
    public void onBindView(RecyclerView.ViewHolder viewHolder, int position) {
        FeedbackModel feedbackModel = data.get(position);

        FeedbackViewHolder holder = (FeedbackViewHolder) viewHolder;
        holder.createTimeTv.setText(feedbackModel.getCreateDate());
        holder.titleTv.setText(feedbackModel.getTitle());
        holder.contentTv.setText(feedbackModel.getValue());
        if (feedbackModel.getState().equals("1")) {
            holder.settleTv.setVisibility(View.VISIBLE);
            holder.unsettleTv.setVisibility(View.GONE);
        } else {
            holder.settleTv.setVisibility(View.GONE);
            holder.unsettleTv.setVisibility(View.VISIBLE);
        }

        //和pm聊天
        holder.chatLayout.setTag(feedbackModel);
        holder.chatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FeedbackModel feedbackModel = (FeedbackModel) view.getTag();
                if (feedbackModel != null && listener != null) {
                    listener.onItemClick(feedbackModel);
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
        return new FeedbackViewHolder(LayoutInflater.from(context).inflate(R.layout.item_feedback, parent, false));
    }

    public class FeedbackViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.feedback_item_create_time_tv)
        TextView createTimeTv;
        @Bind(R.id.feedback_item_title_tv)
        TextView titleTv;
        @Bind(R.id.feedback_item_content_tv)
        TextView contentTv;
        @Bind(R.id.feedback_item_settle_tv)
        TextView settleTv;
        @Bind(R.id.feedback_item_unsettle_tv)
        TextView unsettleTv;
        @Bind(R.id.feedback_item_chat_layout)
        RelativeLayout chatLayout;

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
        void onItemClick(FeedbackModel feedbackModel);
    }
}
