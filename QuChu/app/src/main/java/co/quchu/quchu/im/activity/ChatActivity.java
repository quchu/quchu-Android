package co.quchu.quchu.im.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.dialog.CommonDialog;
import co.quchu.quchu.gallery.utils.Utils;
import co.quchu.quchu.im.IMDialog;
import co.quchu.quchu.im.IMPresenter;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.activity.ArticleDetailActivity;
import co.quchu.quchu.view.activity.QuchuDetailsActivity;
import co.quchu.quchu.view.activity.SettingXioaQActivity;
import co.quchu.quchu.view.activity.SplashActivity;
import co.quchu.quchu.view.activity.UserCenterActivity;
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

  private TextView titleTv;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat);

    EnhancedToolbar toolbar = getEnhancedToolbar();
    titleTv = toolbar.getTitleTv();
    ImageView settingIv = toolbar.getRightIv();
    settingIv.setImageResource(R.mipmap.ic_shezhi);
    settingIv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (mTargetId.equals(IMPresenter.xiaoqId)) {
          //跳转小Q设置
          startActivity(SettingXioaQActivity.class);
        } else {
          //普通聊天设置
          showBehaviorDialog();
        }
      }
    });

    if (getIntent() == null || getIntent().getData() == null) {
      LogUtils.e("------mwb", "intent null");
      return;
    }

    getIntentDate(getIntent());

    isPushMessage(getIntent());

    getTypingStatus();

    RongIM.setConversationBehaviorListener(conversationBehaviorListener);
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
          Intent intent = new Intent(ChatActivity.this, UserCenterActivity.class);
          intent.putExtra(UserCenterActivity.REQUEST_KEY_USER_ID,
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
          return customClickMessage(message);
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
          IMDialog dialog = new IMDialog(ChatActivity.this, message);
          dialog.show();
          return true;
        }
      };

  /**
   * 自定义消息点击事件
   * 0-趣处详情；1-用户；2-趣处文章
   */
  private boolean customClickMessage(io.rong.imlib.model.Message message) {
    try {
      JSONObject jsonObject = new JSONObject(new String(message.getContent().encode()));
      if (jsonObject.has("content")) {
        String content = jsonObject.getString("content");
      }

      if (jsonObject.has("extra")) {
        String extra = jsonObject.getString("extra");
        JSONObject extraObject = new JSONObject(extra);
        String id = "";
        String type = "";
        if (extraObject.has("id")) {
          id = extraObject.getString("id");
        }
        if (extraObject.has("type")) {
          type = extraObject.getString("type");
        }

        if (TextUtils.isEmpty(id)) {
          return false;
        }

        Intent intent = null;
        if (type.equals(IMPresenter.JUMP_TYPE_QUCHU_DETAIL)) {
          //趣处详情
          intent = new Intent(ChatActivity.this, QuchuDetailsActivity.class);
          intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, id);
          startActivity(intent);

        } else if (type.equals(IMPresenter.JUMP_TYPE_USER)) {
          //用户
          intent = new Intent(ChatActivity.this, UserCenterActivity.class);
          intent.putExtra(UserCenterActivity.REQUEST_KEY_USER_ID, id);
          startActivity(intent);

        } else if (type.equals(IMPresenter.JUMP_TYPE_ARTICLE_DETAIL)) {
          //文章详情
          ArticleDetailActivity.enterActivity(ChatActivity.this, id, "文章详情", "小Q聊天界面");
        }

        return true;

      } else {
        return false;
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return false;
  }

  /**
   * 用户输入状态
   */
  private void getTypingStatus() {
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
              handler.sendEmptyMessage(TITLE_TEXT_TYPING);
            } else if (objectName.equals(voiceTag.value())) {
              //显示"对方正在讲话"
              handler.sendEmptyMessage(TITLE_VOICE_TYPING);
            }
          } else {
            //当前会话没有用户正在输入，标题栏仍显示原来标题
            handler.sendEmptyMessage(TITLE_DEFAULT);
          }
        }
      }
    });
  }

  private Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      if (msg.what == TITLE_DEFAULT) {
        titleTv.setText(mTitle);
      } else if (msg.what == TITLE_TEXT_TYPING) {
        titleTv.setText("对方正在输入");
      } else if (msg.what == TITLE_VOICE_TYPING) {
        titleTv.setText("对方正在讲话");
      }
    }
  };

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);

    LogUtils.e("ChatActivity", "onNewIntent()");
  }

  /**
   * 得到 融云会话页面传递的 Uri
   * rong://{应用包名}/conversation/[private|discussion|group]?targetId={目标Id}&[title={开启会话名称}]
   */
  private void getIntentDate(Intent intent) {
    LogUtils.e("------mwb", "getIntentDate");
    mTargetId = intent.getData().getQueryParameter("targetId");
    mTargetIds = intent.getData().getQueryParameter("targetIds");
    mTitle = intent.getData().getQueryParameter("title");
    titleTv.setText(mTitle);

    //intent.getData().getLastPathSegment();获得当前会话类型
    mConversationType = Conversation.ConversationType
        .valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));

    enterFragment(mConversationType, mTargetId);
  }

  /**
   * 判断消息是否是 push 消息
   */
  private void isPushMessage(Intent intent) {
    LogUtils.e("------mwb", "isPushMessage");
    String token = SPUtils.getRongYunToken();

    if (intent.getData().getScheme().equals("rong")
        && intent.getData().getQueryParameter("push") != null) {
      //push消息

      //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
      if (intent.getData().getQueryParameter("push").equals("true")) {

        enterActivity();
      }

    } else {
      //程序切到后台，收到消息后点击进入,会执行这里
      if (!RongIMClient.getInstance().getCurrentConnectionStatus()
          .equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {

        enterActivity();
      }

//      enterFragment(mConversationType, mTargetId);
      reconnect(token);
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
  private void reconnect(String token) {
    IMPresenter.connectIMService(token, new IMPresenter.RongYunBehaviorListener() {
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
    lp.rightMargin = Utils.dip2px(this, 10);
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
        IMPresenter.getLatestMessages(mTargetId, 50, new IMPresenter.RongYunBehaviorListener() {
          @Override
          public void onSuccess(String msg) {
            IMPresenter.sendImReport(ChatActivity.this, mTargetId, msg, new CommonListener<Object>() {
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

        CommonDialog commonDialog =
            CommonDialog.newInstance("确定要屏蔽此用户吗？", "屏蔽该用户后，90天内您将不会再收到该用户的消息", "确定", "取消");
        commonDialog.setListener(new CommonDialog.OnActionListener() {
          @Override
          public boolean dialogClick(int clickId) {
            if (clickId == CommonDialog.CLICK_ID_ACTIVE) {
              addToBack();
            }
            return true;
          }
        });
        commonDialog.show(getSupportFragmentManager(), "");
      }
    });
  }

  /**
   * 屏蔽用户
   */
  private void addToBack() {
    //加入黑名单
    IMPresenter.addToBlackList(mTargetId, new IMPresenter.RongYunBehaviorListener() {
      @Override
      public void onSuccess(String msg) {
        //本地历史中删除该聊天
        IMPresenter.removeConversation(mTargetId, new IMPresenter.RongYunBehaviorListener() {
          @Override
          public void onSuccess(String msg) {
            IMPresenter.shield(ChatActivity.this, mTargetId, new CommonListener<Object>() {
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
