package co.quchu.quchu.im.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.im.IMPresenter;
import co.quchu.quchu.utils.SPUtils;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.TypingMessage.TypingStatus;
import io.rong.imlib.model.Conversation;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * 聊天界面
 *
 * Created by mwb on 16/8/25.
 */
public class ConversationActivity extends BaseBehaviorActivity {

  private String TITLE_DEFAULT = "聊天";
  //显示“对方正在输入”
  private String TITLE_TEXT_TYPING = "聊天";
  //显示"对方正在讲话"
  private String TITLE_VOICE_TYPING = "聊天";

  //私聊的目标id
  private String targetId;
  //刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
  private String targetIds;
  //会话类型
  private Conversation.ConversationType conversationType;

  private TextView titleTv;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_conversation);

    EnhancedToolbar toolbar = getEnhancedToolbar();
    titleTv = toolbar.getTitleTv();
    titleTv.setText("聊天");

    getIntentDate(getIntent());

    isPushMessage(getIntent());

    getTypingStatus();

    RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
      @Override public void onSuccess(List<Conversation> conversations) {

      }

      @Override public void onError(RongIMClient.ErrorCode errorCode) {

      }
    }, Conversation.ConversationType.PRIVATE);

    RongIM.getInstance().setReadReceiptConversationTypeList();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_conversation, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.action_setting) {
      makeToast("click");
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * 用户输入状态
   */
  private void getTypingStatus() {
    RongIMClient.setTypingStatusListener(new RongIMClient.TypingStatusListener() {
      @Override
      public void onTypingStatusChanged(Conversation.ConversationType type, String id, Collection<TypingStatus> typingStatusSet) {
        //当输入状态的会话类型和targetID与当前会话一致时，才需要显示
        if (type.equals(conversationType) && id.equals(targetId)) {
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
              //mHandler.sendEmptyMessage(TITLE_TEXT_TYPING);
              titleTv.setText(TITLE_TEXT_TYPING);
            } else if (objectName.equals(voiceTag.value())) {
              //显示"对方正在讲话"
              //mHandler.sendEmptyMessage(TITLE_VOICE_TYPING);
              titleTv.setText(TITLE_VOICE_TYPING);
            }
          } else {
            //当前会话没有用户正在输入，标题栏仍显示原来标题
            //mHandler.sendEmptyMessage(TITLE_DEFAULT);
            titleTv.setText(TITLE_DEFAULT);
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
    targetId = intent.getData().getQueryParameter("targetId");
    targetIds = intent.getData().getQueryParameter("targetIds");
    //intent.getData().getLastPathSegment();//获得当前会话类型
    conversationType = Conversation.ConversationType
        .valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));

    enterFragment(conversationType, targetId);
  }

  /**
   * 加载会话页面 ConversationFragment
   */
  private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

    ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager()
        .findFragmentById(R.id.conversation_fragment);

    Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
        .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
        .appendQueryParameter("targetId", mTargetId).build();

    fragment.setUri(uri);
  }

  /**
   * 判断消息是否是 push 消息
   */
  private void isPushMessage(Intent intent) {
    String token = SPUtils.getRongYunToken();

    //push或通知过来
    if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {
      //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
      if (intent.getData().getQueryParameter("push") != null && intent.getData()
          .getQueryParameter("push").equals("true")) {

        reconnect(token);
      } else {
        //程序切到后台，收到消息后点击进入,会执行这里
        if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {

          reconnect(token);
        } else {
          enterFragment(conversationType, targetId);
        }
      }
    }
  }

  /**
   * 重连融云服务
   */
  private void reconnect(String token) {
    IMPresenter.connectIMService(token, new IMPresenter.RongYunConnectListener() {
      @Override public void connectSuccess() {
        enterFragment(conversationType, targetId);
      }
    });
  }

  @Override public ArrayMap<String, Object> getUserBehaviorArguments() {
    return null;
  }

  @Override public int getUserBehaviorPageId() {
    return 0;
  }

  @Override protected int activitySetup() {
    return TRANSITION_TYPE_LEFT;
  }

  @Override protected String getPageNameCN() {
    return null;
  }
}
