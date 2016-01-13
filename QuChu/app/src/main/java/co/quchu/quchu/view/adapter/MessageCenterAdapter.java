package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.MessageModel;
import co.quchu.quchu.presenter.MessageCenterPresenter;
import co.quchu.quchu.utils.DateUtils;

/**
 * MessageCenterAdapter
 * User: Chenhs
 * Date: 2016-01-12
 */
public class MessageCenterAdapter extends RecyclerView.Adapter<MessageCenterAdapter.MessageCenterItemHolder> {

    private Context mContext;
    private ArrayList<MessageModel> messageModelArrayList;


    public MessageCenterAdapter(Context mContext, ArrayList<MessageModel> messageList) {
        this.mContext = mContext;
        this.messageModelArrayList = messageList;
    }

    @Override
    public MessageCenterItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MessageCenterItemHolder holder = new MessageCenterItemHolder(LayoutInflater.from(mContext).inflate(R.layout.item_message_center_follow, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MessageCenterItemHolder holder, int position) {
        MessageModel model = messageModelArrayList.get(position);
        holder.itemMessageFromAvator.setImageURI(Uri.parse(model.getFormPhoto()));
        holder.itemMessageDesTv.setText(model.getContent());
        holder.itemMessageUserNameTv.setText(model.getForm());
        holder.itemMessageAddTimeTv.setText(DateUtils.getTimeRange(model.getTime(), mContext));
        switch (model.getType()) {
            case "follow":
                holder.itemMessageFromRl.setVisibility(View.VISIBLE);
                holder.itemMessageFrom.setVisibility(View.GONE);
                holder.itemMessageFollowTv.setVisibility(View.VISIBLE);
                if ("yes".equals(model.getCome())) {
                    holder.itemMessageFollowTv.setTextColor(mContext.getResources().getColor(R.color.black));
                    holder.itemMessageFollowTv.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_message_follow_full_bg));
                    holder.itemMessageFollowTv.setText("已关注");
                } else {
                    holder.itemMessageFollowTv.setTextColor(mContext.getResources().getColor(R.color.gene_textcolor_yellow));
                    holder.itemMessageFollowTv.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_message_follow_bg));
                    holder.itemMessageFollowTv.setText("关注");
                }
                break;

            case "img":
                holder.itemMessageFromRl.setVisibility(View.VISIBLE);
                holder.itemMessageFrom.setVisibility(View.VISIBLE);
                holder.itemMessageFollowTv.setVisibility(View.GONE);
                if (model.getCome().startsWith("http"))
                    holder.itemMessageFrom.setImageURI(Uri.parse(model.getCome()));
                break;
            case "words":
                holder.itemMessageFromRl.setVisibility(View.GONE);
                break;
        }
    }

    public void changeDateSet(ArrayList<MessageModel> arrayList) {
        this.messageModelArrayList = arrayList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (messageModelArrayList == null)
            return 0;
        else
            return messageModelArrayList.size();
    }

    public class MessageCenterItemHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_message_from_avator)
        SimpleDraweeView itemMessageFromAvator;
        @Bind(R.id.item_message_from)
        SimpleDraweeView itemMessageFrom;
        @Bind(R.id.item_message_follow_tv)
        TextView itemMessageFollowTv;
        @Bind(R.id.item_message_from_rl)
        RelativeLayout itemMessageFromRl;
        @Bind(R.id.item_message_user_name_tv)
        TextView itemMessageUserNameTv;
        @Bind(R.id.item_message_add_time_tv)
        TextView itemMessageAddTimeTv;
        @Bind(R.id.item_message_des_tv)
        TextView itemMessageDesTv;

        public MessageCenterItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.item_message_follow_tv})
        public void messageItemClick(View view) {
            switch (view.getId()) {
                case R.id.item_message_follow_tv:
                    DialogUtil.showProgess(mContext, R.string.loading_dialog_text);
                    MessageCenterPresenter.followMessageCenterFriends(mContext, messageModelArrayList.get(getPosition()).getFormId(), "yes".equals(messageModelArrayList.get(getPosition()).getCome()), new MessageCenterPresenter.MessageGetDataListener() {
                        @Override
                        public void onSuccess(ArrayList<MessageModel> arrayList) {
                            if ("yes".equals(messageModelArrayList.get(getPosition()).getCome())) {
                                messageModelArrayList.get(getPosition()).setCome("no");
                            } else {
                                messageModelArrayList.get(getPosition()).setCome("yes");
                            }
                            notifyDataSetChanged();
                            DialogUtil.dismissProgess();
                        }

                        @Override
                        public void onError() {
                            DialogUtil.dismissProgess();
                        }
                    });
                    break;
            }
        }
    }
}
