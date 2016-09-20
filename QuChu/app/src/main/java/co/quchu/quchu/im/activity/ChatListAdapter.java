package co.quchu.quchu.im.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import io.rong.imkit.utils.AndroidEmoji;
import io.rong.imkit.utils.RongDateUtils;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

/**
 * Created by mwb on 16/8/29.
 */
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {

  private static final int ITEM_VIEW_TYPE_HEADER = 0;
  private static final int ITEM_VIEW_TYPE_ITEM = 1;

  private Context mContext;
  private List<Conversation> mConversations;

  public ChatListAdapter(Context context, List<Conversation> conversations) {
    super();

    this.mContext = context;
    this.mConversations = conversations;
  }

  @Override
  public int getItemViewType(int position) {
    return position == 0 ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
  }

  @Override
  public ChatListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ChatListViewHolder(
        LayoutInflater.from(mContext).inflate(R.layout.item_conversation_list, parent, false));
  }

  @Override
  public void onBindViewHolder(ChatListViewHolder holder, final int position) {
    if (position == 0) {
      holder.titleTv.setText("趣处小Q");
      holder.contentTv.setText("趣处小Q是你的生活助手");
      holder.timeTv.setVisibility(View.INVISIBLE);
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (mListener != null) {
            mListener.itemClick(null, position);
          }
        }
      });
      return;
    }

    Conversation conversation = mConversations.get(position - 1);
    MessageContent latestMessage = conversation.getLatestMessage();
    UserInfo userInfo = latestMessage.getUserInfo();
    String avatar = "";
    String name = "";
    if (userInfo != null) {
      avatar = userInfo.getPortraitUri().toString();
      name = userInfo.getName();
    }

    if (TextUtils.isEmpty(avatar)) {
      holder.avatarImg.getHierarchy().setPlaceholderImage(R.drawable.rc_default_portrait);
    } else {
      holder.avatarImg.setImageURI(avatar);
    }
    holder.titleTv.setText(name);
    if (!TextUtils.isEmpty(conversation.getDraft())) {
      //有草稿显示草稿内容
      holder.contentTv.setText("[草稿]" + AndroidEmoji.ensure(conversation.getDraft()));
    } else {
      if (latestMessage != null) {
        String content = "";
        try {
          JSONObject jsonObject = new JSONObject(new String(latestMessage.encode()));
          if (jsonObject.has("content")) {
            //富文字
            content = jsonObject.getString("content");

          } else if (jsonObject.has("duration")) {
            //语音
            content = "[语音]";
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
        holder.contentTv.setText(AndroidEmoji.ensure(content));
      } else {
        holder.contentTv.setText("");
      }
    }

    String time = RongDateUtils.getConversationFormatDate(conversation.getReceivedTime(), holder.unreadMessageTv.getContext());
//    holder.timeTv.setText(TimeUtils.formatData(conversation.getReceivedTime()));
    holder.timeTv.setText(time);
    holder.unreadMessageTv.setVisibility(conversation.getUnreadMessageCount() > 0 ? View.VISIBLE : View.INVISIBLE);

    holder.itemView.setTag(conversation);
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Conversation conversation = (Conversation) view.getTag();
        if (conversation != null && mListener != null) {
          mListener.itemClick(conversation, position);
        }
      }
    });

    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View view) {
        Conversation conversation = (Conversation) view.getTag();
        if (conversation != null && mListener != null) {
          mListener.itemLongClick(conversation);
        }
        return true;
      }
    });
  }

  @Override
  public int getItemCount() {
    return mConversations != null ? mConversations.size() + 1 : 1;
  }

  public class ChatListViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.item_conversation_avatar_img) SimpleDraweeView avatarImg;
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
    void itemClick(Conversation conversation, int position);

    void itemLongClick(Conversation conversation);
  }
}
