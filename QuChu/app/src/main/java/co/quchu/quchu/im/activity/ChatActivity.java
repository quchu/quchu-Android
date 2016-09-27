package co.quchu.quchu.im.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.gallery.utils.Utils;
import co.quchu.quchu.im.IMPresenter;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.activity.SplashActivity;
import co.quchu.quchu.view.activity.UserCenterActivityNew;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.TypingMessage.TypingStatus;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * 聊天界面
 * <p/>
 * Created by mwb on 16/8/25.
 */
public class ChatActivity extends BaseBehaviorActivity {

  private int TITLE_DEFAULT = 0;
  //显示“对方正在输入”
  private int TITLE_TEXT_TYPING = 1;
  //显示"对方正在讲话"
  private int TITLE_VOICE_TYPING = 2;

  //私聊的目标id
  private String mTargetId;
  //刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
  private String mTargetIds;
  //显示的title
  private String mTitle;
  //会话类型
  private Conversation.ConversationType mConversationType;

  private TextView mTitleTv;
  private IMPresenter mImPresenter;
  private Handler mHandler;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat);

    Intent intent = getIntent();
    if (intent == null || intent.getData() == null) {
      LogUtils.e("------ChatActivity", "intent or intent data is null");
      return;
    }

    if (!NetUtil.isNetworkConnected(this)) {
      makeToast("无网络");
    }

    EnhancedToolbar toolbar = getEnhancedToolbar();
    mTitleTv = toolbar.getTitleTv();
    ImageView settingIv = toolbar.getRightIv();
    settingIv.setImageResource(R.mipmap.ic_gengduo);
    settingIv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
          showBehaviorDialog();
      }
    });

    mImPresenter = new IMPresenter();

    RongIM.setConversationBehaviorListener(conversationBehaviorListener);

    getIntentDate(intent);

    isPushMessage(intent);

    setTypingStatus();
  }

  /**
   * 会话界面操作监听
   */
  private RongIM.ConversationBehaviorListener conversationBehaviorListener =
      new RongIM.ConversationBehaviorListener() {

        @Override
        public boolean onUserPortraitClick(Context context,
                                           Conversation.ConversationType conversationType, UserInfo userInfo) {
          //当点击用户头像后执行
          Intent intent = new Intent(ChatActivity.this, UserCenterActivityNew.class);
          intent.putExtra(UserCenterActivityNew.REQUEST_KEY_USER_ID,
              Integer.valueOf(userInfo.getUserId()));
          startActivity(intent);
          return true;
        }

        @Override
        public boolean onUserPortraitLongClick(Context context,
                                               Conversation.ConversationType conversationType, UserInfo userInfo) {
          //当长按用户头像后执行
          return false;
        }

        @Override
        public boolean onMessageClick(Context context, View view,
                                      io.rong.imlib.model.Message message) {
          //当点击消息时执行
          return false;
        }

        @Override
        public boolean onMessageLinkClick(Context context, String s) {
          //当点击链接消息时执行
          return false;
        }

        @Override
        public boolean onMessageLongClick(Context context, View view,
                                          io.rong.imlib.model.Message message) {
          //当长按消息时执行
//          messageLongClick(message);
//          IMDialog dialog = new IMDialog(ChatActivity.this, message);
//          dialog.show();
          return false;
        }
      };

  /**
   * 消息长按
   */
  private void messageLongClick(final io.rong.imlib.model.Message message) {
    //聊天界面，如果是自己发送的消息，并且在有效时间之内可以撤回消息
    io.rong.imlib.model.Message.MessageDirection messageDirection = message.getMessageDirection();
    long sentTime = message.getSentTime();
    Calendar calendar = Calendar.getInstance();
    long currentTime = calendar.getTimeInMillis();
    if (messageDirection.getValue() == 1 && (currentTime - sentTime) < 0.5 * 30 * 1000) {
      new MaterialDialog.Builder(this)
          .items("复制消息", "删除消息", "撤回消息")
          .itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
              operateMessage(message, position);
            }
          }).show();

    } else {
      new MaterialDialog.Builder(this)
          .items("复制消息", "删除消息")
          .itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
              operateMessage(message, position);
            }
          }).show();
    }
  }

  private void operateMessage(io.rong.imlib.model.Message message, int position) {
    int[] messageIds = new int[]{Integer.valueOf(message.getMessageId())};
    String mMessageContent = "";
    try {
      JSONObject jsonObject = new JSONObject(new String(message.getContent().encode()));
      mMessageContent = jsonObject.getString("content");
    } catch (JSONException e) {
      e.printStackTrace();
    }
    switch (position) {
      case 0: //复制消息
        ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(mMessageContent);
        break;

      case 1://删除消息
        mImPresenter.deleteMessages(messageIds);
        break;

      case 2://撤回消息
        mImPresenter.recallMessage(message);
        break;
    }
  }

  /**
   * 用户输入状态
   */
  private void setTypingStatus() {
    mHandler = new Handler() {
      @Override
      public void handleMessage(Message msg) {
        if (msg.what == TITLE_DEFAULT) {
          mTitleTv.setText(mTitle);
        } else if (msg.what == TITLE_TEXT_TYPING) {
          mTitleTv.setText("对方正在输入");
        } else if (msg.what == TITLE_VOICE_TYPING) {
          mTitleTv.setText("对方正在讲话");
        }
      }
    };

    RongIMClient.setTypingStatusListener(new RongIMClient.TypingStatusListener() {
      @Override
      public void onTypingStatusChanged(Conversation.ConversationType type, String id,
                                        Collection<TypingStatus> typingStatusSet) {
        //当输入状态的会话类型和targetID与当前会话一致时，才需要显示
        if (type.equals(mConversationType) && id.equals(mTargetId)) {
          //count表示当前会话中正在输入的用户数量，目前只支持单聊，所以判断大于0就可以给予显示了
          int count = typingStatusSet.size();
          if (count > 0) {
            Iterator iterator = typingStatusSet.iterator();
            TypingStatus status = (TypingStatus) iterator.next();
            String objectName = status.getTypingContentType();

            MessageTag textTag = TextMessage.class.getAnnotation(MessageTag.class);
            MessageTag voiceTag = VoiceMessage.class.getAnnotation(MessageTag.class);
            //匹配对方正在输入的是文本消息还是语音消息
            if (objectName.equals(textTag.value())) {
              //显示“对方正在输入”
              mHandler.sendEmptyMessage(TITLE_TEXT_TYPING);
            } else if (objectName.equals(voiceTag.value())) {
              //显示"对方正在讲话"
              mHandler.sendEmptyMessage(TITLE_VOICE_TYPING);
            }
          } else {
            //当前会话没有用户正在输入，标题栏仍显示原来标题
            mHandler.sendEmptyMessage(TITLE_DEFAULT);
          }
        }
      }
    });
  }

  /**
   * 得到 融云会话页面传递的 Uri
   * rong://{应用包名}/conversation/[private|discussion|group]?targetId={目标Id}&[title={开启会话名称}]
   */
  private void getIntentDate(Intent intent) {
    mTargetId = intent.getData().getQueryParameter("targetId");
    mTargetIds = intent.getData().getQueryParameter("targetIds");
    mTitle = intent.getData().getQueryParameter("title");
    mTitleTv.setText(mTitle);

    //intent.getData().getLastPathSegment();获得当前会话类型
    mConversationType = Conversation.ConversationType
        .valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));
  }

  /**
   * 判断消息是否是 push 消息
   */
  private void isPushMessage(Intent intent) {
    if (intent.getData().getScheme().equals("rong") && intent.getData().getQueryParameter("isFromPush") != null) {
      //push消息
      if (intent.getData().getQueryParameter("isFromPush").equals("true")) {
        enterActivity();
      } else if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
        enterActivity();
      } else {
        enterFragment(mConversationType, mTargetId);
      }

    } else {
      if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
        //融云未连接
        enterActivity();
      } else {
        enterFragment(mConversationType, mTargetId);
      }
    }
  }

  /**
   * 应用处于后台且进程被杀死，进入应用主页
   */
  private void enterActivity() {
    SPUtils.setRongYunTargetId(mTargetId);
    SPUtils.setRongYunTitle(mTitle);
    Intent intent = new Intent(ChatActivity.this, SplashActivity.class);
    intent.putExtra(SplashActivity.INTENT_KEY_IM_CHAT, true);
    startActivity(intent);
    finish();
  }

  /**
   * 加载会话页面 ConversationFragment
   */
  private void enterFragment(Conversation.ConversationType conversationType, String targetId) {
    ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager()
        .findFragmentById(R.id.conversation_fragment);

    Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
        .appendPath("conversation").appendPath(conversationType.getName().toLowerCase())
        .appendQueryParameter("targetId", targetId).build();

    fragment.setUri(uri);
  }

  /**
   * 重连融云服务
   */
  private void reconnect() {
    mImPresenter.connect(SPUtils.getRongYunToken(), new IMPresenter.RongYunBehaviorListener() {
      @Override
      public void onSuccess(String msg) {
        enterFragment(mConversationType, mTargetId);
      }

      @Override
      public void onError() {

      }
    });
  }

  /**
   * 显示dialog
   * 举报、屏蔽
   */
  private void showBehaviorDialog() {
    FrameLayout viewGroup = new FrameLayout(this);

    View view = LayoutInflater.from(this).inflate(R.layout.dialog_message_behavior, null);

    //举报
    View reportView = view.findViewById(R.id.dialog_message_behavior_report);

    //屏蔽
    View shieldView = view.findViewById(R.id.dialog_message_behavior_shield);

    FrameLayout.LayoutParams lp =
        new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.RIGHT);
    Rect frame = new Rect();
    getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
    lp.topMargin = Utils.dip2px(this, 20) + frame.top;
    lp.rightMargin = Utils.dip2px(this, 12);
    viewGroup.addView(view, lp);

    final PopupWindow popWin = new PopupWindow(viewGroup, ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT, true);
    popWin.setBackgroundDrawable(new ColorDrawable(0));
    popWin.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);

    viewGroup.setFocusable(true);
    viewGroup.setFocusableInTouchMode(true);
    viewGroup.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        popWin.dismiss();
      }
    });

    viewGroup.setOnKeyListener(new View.OnKeyListener() {

      @Override
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
          popWin.dismiss();
          return true;
        }
        return false;
      }
    });

    //举报
    reportView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        popWin.dismiss();
        mImPresenter.getLatestMessages(mTargetId, 100, new IMPresenter.RongYunBehaviorListener() {
          @Override
          public void onSuccess(String msg) {
            mImPresenter.sendImReport(ChatActivity.this, mTargetId, msg, new CommonListener<Object>() {
              @Override
              public void successListener(Object response) {
                makeToast("您已经举报该用户");
              }

              @Override
              public void errorListener(VolleyError error, String exception, String msg) {
                makeToast(R.string.network_error);
              }
            });
          }

          @Override
          public void onError() {

          }
        });
      }
    });

    //屏蔽
    shieldView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        popWin.dismiss();

        new MaterialDialog.Builder(ChatActivity.this)
            .title("确定要屏蔽此用户吗？")
            .content("屏蔽该用户后，90天内您将不会再收到该用户的消息")
            .positiveText("确定")
            .negativeText("取消")
            .cancelable(false)
            .onPositive(new MaterialDialog.SingleButtonCallback() {
              @Override
              public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                addToBack();
              }
            })
            .show();
      }
    });
  }

  /**
   * 屏蔽用户
   */
  private void addToBack() {
    //加入黑名单
    mImPresenter.addToBlackList(mTargetId, new IMPresenter.RongYunBehaviorListener() {
      @Override
      public void onSuccess(String msg) {
        //本地历史中删除该聊天
        mImPresenter.removeConversation(mTargetId, new IMPresenter.RongYunBehaviorListener() {
          @Override
          public void onSuccess(String msg) {
            mImPresenter.shield(ChatActivity.this, mTargetId, new CommonListener<Object>() {
              @Override
              public void successListener(Object response) {
                makeToast("成功屏蔽该用户");
                finish();
              }

              @Override
              public void errorListener(VolleyError error, String exception, String msg) {
              }
            });
          }

          @Override
          public void onError() {
          }
        });
      }

      @Override
      public void onError() {
        makeToast("屏蔽失败");
      }
    });
  }

  @Override
  public ArrayMap<String, Object> getUserBehaviorArguments() {
    return null;
  }

  @Override
  public int getUserBehaviorPageId() {
    return 0;
  }

  @Override
  protected int activitySetup() {
    return TRANSITION_TYPE_LEFT;
  }

  @Override
  protected String getPageNameCN() {
    return null;
  }
}
