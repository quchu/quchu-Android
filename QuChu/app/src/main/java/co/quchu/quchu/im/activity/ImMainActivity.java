package co.quchu.quchu.im.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.android.volley.VolleyError;

import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.im.IMPresenter;
import co.quchu.quchu.im.model.RongToken;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.activity.MessageActivity;
import co.quchu.quchu.view.activity.SplashActivity;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

/**
 * IM相关操作
 * <p/>
 * Created by mwb on 16/9/13.
 */
public class ImMainActivity extends BaseBehaviorActivity {

  private String TAG = "ImMainActivity";

  private IMPresenter mImPresenter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //im推送跳转
    boolean isChat = getIntent().getBooleanExtra(SplashActivity.INTENT_KEY_IM_CHAT, false);
    boolean isChatList = getIntent().getBooleanExtra(SplashActivity.INTENT_KEY_IM_CHAT_LIST, false);
    if (isChat) {
      startChat();
    } else if (isChatList) {
      startChatList();
    }

    setReceiveMessageListener();

    setConnectionStatusListener();

    String token = SPUtils.getRongYunToken();
    mImPresenter = new IMPresenter();
    if (TextUtils.isEmpty(token)) {
      getToken();
    } else {
//      connect(token);
      mImPresenter.connect(token, new IMPresenter.RongYunBehaviorListener() {
        @Override
        public void onSuccess(String msg) {
          onConnectImSuccess();
        }

        @Override
        public void onError() {

        }
      });
    }
  }

  /**
   * 开启聊天界面
   */
  public void startChat() {
    if (RongIM.getInstance() != null) {
      RongIM.getInstance().startPrivateChat(this, SPUtils.getRongYunTargetId(), SPUtils.getRongYunTitle());
    }
  }

  /**
   * 开启聊天列表
   */
  public void startChatList() {
    startActivity(MessageActivity.class);
  }

  /**
   * 接收消息监听
   * 在 connect 前调用
   */
  private void setReceiveMessageListener() {
    RongIMClient.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
      @Override
      public boolean onReceived(Message message, int i) {
        LogUtils.e(TAG, "message receive--------i = " + i);
        return false;
      }
    });
  }

  /**
   * 获取融云Token
   */
  private void getToken() {
    mImPresenter.getToken(this, new CommonListener<RongToken>() {
      @Override
      public void successListener(RongToken response) {
        if (response == null) {
          return;
        }

        LogUtils.e(TAG, "getToken()-----获取融云Token成功");

        //保存Token到本地
        SPUtils.setRongYunToken(response.getRongYunToken());

        mImPresenter.connect(response.getRongYunToken(), new IMPresenter.RongYunBehaviorListener() {
          @Override
          public void onSuccess(String msg) {
            onConnectImSuccess();
          }

          @Override
          public void onError() {

          }
        });
      }

      @Override
      public void errorListener(VolleyError error, String exception, String msg) {
        LogUtils.e(TAG, "getToken()-----获取融云Token失败");
      }
    });
  }

  /**
   * 连接融云服务
   */
  private void connect(String token) {
    LogUtils.e(TAG, "connect()-------token = " + token);

    RongIMClient.connect(token, new RongIMClient.ConnectCallback() {
      @Override
      public void onTokenIncorrect() {
        //Token过期
        LogUtils.e(TAG, "onTokenIncorrect()");
        ImMainActivity.this.getToken();
      }

      @Override
      public void onSuccess(String userId) {
        LogUtils.e(TAG, "onSuccess()------userId = " + userId);
        onConnectImSuccess();
      }

      @Override
      public void onError(RongIMClient.ErrorCode errorCode) {
        LogUtils.e(TAG, "onError()------errorCode = " + errorCode);
      }
    });
  }

  /**
   * 连接融云服务成功
   */
  protected void onConnectImSuccess() {
    LogUtils.e(TAG, "onConnectImSuccess()");
  }

  /**
   * 连接状态监听
   * 在 init 后进行调用
   */
  private void setConnectionStatusListener() {
    RongIMClient.setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
      @Override
      public void onChanged(ConnectionStatus connectionStatus) {
        switch (connectionStatus) {
          case CONNECTED://连接成功。
            LogUtils.e(TAG, "ConnectionStatusListener-----CONNECTED");
            break;

          case DISCONNECTED://断开连接。
            LogUtils.e(TAG, "ConnectionStatusListener-----DISCONNECTED");
            break;

          case CONNECTING://连接中。
            LogUtils.e(TAG, "ConnectionStatusListener-----CONNECTING");
            break;

          case NETWORK_UNAVAILABLE://网络不可用。
            LogUtils.e(TAG, "ConnectionStatusListener-----NETWORK_UNAVAILABLE");
            break;

          case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
            LogUtils.e(TAG, "ConnectionStatusListener-----KICKED_OFFLINE_BY_OTHER_CLIENT");
            break;

          case TOKEN_INCORRECT://Token不正确
            LogUtils.e(TAG, "ConnectionStatusListener-----TOKEN_INCORRECT");
            break;

          case SERVER_INVALID://服务器异常或者无法连接
            LogUtils.e(TAG, "ConnectionStatusListener-----SERVER_INVALID");
            break;
        }
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
    return 0;
  }

  @Override
  protected String getPageNameCN() {
    return null;
  }
}
