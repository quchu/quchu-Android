package co.quchu.quchu.im;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import io.rong.imlib.model.Message;

/**
 * im对话框
 * <p/>
 * Created by mwb on 16/8/29.
 */
public class IMDialog extends Dialog {

  @Bind(R.id.im_dialog_top_tv)
  TextView topTv;//置顶聊天、复制消息
  @Bind(R.id.im_dialog_delete_tv)
  TextView deleteTv;//删除聊天、删除消息
  @Bind(R.id.im_dialog_recall_tv)
  TextView recallTv;//撤回消息
  @Bind(R.id.im_dialog_resend_tv)
  TextView mResendTv;//重发消息

  private boolean mIsChatList;//聊天列表
  private boolean mIsTop;//聊天是否置顶
  private String mTargetId;
  private int[] messageIds;//消息id
  private String mMessageContent;//消息内容
  private Message mMessage;//消息实体

  private String topTitle;
  private String deleteTitle;
  private Message.SentStatus mSentStatus;

  /**
   * 聊天列表
   */
  public IMDialog(Context context, String targetId, boolean isTop) {
    super(context, R.style.loading_dialog);

    mIsChatList = true;
    mIsTop = isTop;
    mTargetId = targetId;

    topTitle = mIsTop ? "取消置顶" : "置顶聊天";
    deleteTitle = "删除聊天";
  }

  /**
   * 聊天界面
   */
  public IMDialog(Context context, Message message) {
    super(context, R.style.loading_dialog);

    mIsChatList = false;
    mMessage = message;
    messageIds = new int[]{Integer.valueOf(message.getMessageId())};
    mTargetId = message.getTargetId();

    //消息发送状态
    mSentStatus = message.getSentStatus();

    try {
      JSONObject jsonObject = new JSONObject(new String(message.getContent().encode()));
      mMessageContent = jsonObject.getString("content");
    } catch (JSONException e) {
      e.printStackTrace();
    }

    topTitle = "复制消息";
    deleteTitle = "删除消息";
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_im);
    ButterKnife.bind(this);

    topTv.setText(topTitle);
    deleteTv.setText(deleteTitle);

    //聊天列表，隐藏置顶聊天
    if (mIsChatList) {
      topTv.setVisibility(View.GONE);
    }

    //发送消息失败
    if (!mIsChatList && mSentStatus != null && mSentStatus.getValue() == 20) {
      mResendTv.setVisibility(View.VISIBLE);
    }

    //聊天界面，如果是自己发送的消息，并且在有效时间之内可以撤回消息
    if (!mIsChatList && mMessage != null) {
      Message.MessageDirection messageDirection = mMessage.getMessageDirection();
      long sentTime = mMessage.getSentTime();
      Calendar calendar = Calendar.getInstance();
      long currentTime = calendar.getTimeInMillis();
      if (messageDirection.getValue() == 1 && (currentTime - sentTime) < 0.5 * 30 * 1000) {
        recallTv.setVisibility(View.VISIBLE);
      }
    }
  }

  @OnClick({R.id.im_dialog_top_tv, R.id.im_dialog_delete_tv, R.id.im_dialog_recall_tv, R.id.im_dialog_resend_tv})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.im_dialog_top_tv:
        if (mIsChatList) {
          //聊天列表，置顶聊天、取消置顶聊天
          IMPresenter.setConversationToTop(mTargetId, !mIsTop);
        } else {
          //聊天界面，复制消息
          ClipboardManager cmb =
              (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
          cmb.setText(mMessageContent);
        }
        break;

      case R.id.im_dialog_delete_tv:
        if (mIsChatList) {
          //聊天列表，删除聊天
          IMPresenter.removeConversation(mTargetId, null);
        } else {
          //聊天界面，删除消息
          IMPresenter.deleteMessages(messageIds);
        }
        break;

      case R.id.im_dialog_recall_tv:
        //撤回消息
        IMPresenter.recallMessage(mMessage);
        break;

      case R.id.im_dialog_resend_tv:
        //重发消息
        IMPresenter.sendMessage(mTargetId, mMessageContent);
        break;
    }

    dismiss();
  }
}
