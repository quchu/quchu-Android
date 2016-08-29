package co.quchu.quchu.im.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.utils.DateUtils;
import co.quchu.quchu.utils.LogUtils;
import io.rong.imkit.widget.AsyncImageView;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MentionedInfo;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

/**
 * Created by mwb on 16/8/29.
 */
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {

  private Context mContext;
  private List<Conversation> mConversations;

  public ChatListAdapter(Context context, List<Conversation> conversations) {
    super();

    this.mContext = context;
    this.mConversations = conversations;
  }

  @Override public ChatListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ChatListViewHolder(
        LayoutInflater.from(mContext).inflate(R.layout.item_conversation_list, parent, false));
  }

  @Override public void onBindViewHolder(ChatListViewHolder holder, int position) {
    Conversation conversation = mConversations.get(position);
    //草稿
    String draft = conversation.getDraft();
    //最后一条消息
    MessageContent latestMessage = conversation.getLatestMessage();
    JSONObject mentionInfoJson = latestMessage.getJsonMentionInfo();
    JSONObject userInfoJson = latestMessage.getJSONUserInfo();
    MentionedInfo mentionedInfo = latestMessage.getMentionedInfo();
    UserInfo userInfo = latestMessage.getUserInfo();
    //未读消息数
    int unreadMessageCount = conversation.getUnreadMessageCount();
    String receivedTime = DateUtils.dateTimeFormat(conversation.getReceivedTime());
    String sentTime = DateUtils.dateTimeFormat(conversation.getSentTime());

    holder.avatarImg.setAvatar(conversation.getPortraitUrl(), R.drawable.rc_default_portrait);
    holder.titleTv.setText(conversation.getTargetId());
    if (!TextUtils.isEmpty(draft)) {
      //有草稿显示草稿内容
      LogUtils.e("==========mwb", "draft = " + draft);
      holder.contentTv.setText("[草稿]" + draft);
    } else {
      holder.contentTv.setText("------------");
    }
    holder.timeTv.setText(DateUtils.dateTimeFormat(conversation.getReceivedTime()));
    holder.unreadMessageTv.setText("" + unreadMessageCount);
    holder.unreadMessageTv.setVisibility(unreadMessageCount > 0 ? View.VISIBLE : View.INVISIBLE);

    holder.itemView.setTag(conversation);
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Conversation conversation = (Conversation) view.getTag();
        if (conversation != null && mListener != null) {
          mListener.itemClick(conversation);
        }
      }
    });
  }

  @Override public int getItemCount() {
    return mConversations != null ? mConversations.size() : 0;
  }

  public class ChatListViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.item_conversation_avatar_img) AsyncImageView avatarImg;
    @Bind(R.id.item_conversation_unread_message_tv) TextView unreadMessageTv;
    @Bind(R.id.item_conversation_title_tv) TextView titleTv;
    @Bind(R.id.item_conversation_content_tv) TextView contentTv;
    @Bind(R.id.item_conversation_time_tv) TextView timeTv;
    @Bind(R.id.item_conversation) RelativeLayout itemView;

    public ChatListViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  private OnConversationItemClickListener mListener;

  public void setOnConversationItemClickListener(OnConversationItemClickListener listener) {
    mListener = listener;
  }

  public interface OnConversationItemClickListener {
    void itemClick(Conversation conversation);
  }
}
