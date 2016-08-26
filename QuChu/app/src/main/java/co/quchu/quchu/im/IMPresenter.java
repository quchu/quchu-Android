package co.quchu.quchu.im;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;

import java.util.List;

import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.im.model.RongImModel;
import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;

/**
 * 融云im聊天
 *
 * Created by mwb on 16/8/24.
 */
public class IMPresenter {

  private static String TAG = "IMPresenter";

  //融云测试
  public static String userId = "161";
  public static String userId1 = "216";

  /**
   * 获取im token
   */
  public static void getToken(Context context) {
    GsonRequest<RongImModel> request =
        new GsonRequest<RongImModel>(NetApi.getRongYunToken, RongImModel.class,
            new ResponseListener<RongImModel>() {
              @Override public void onErrorResponse(@Nullable VolleyError error) {
                LogUtils.e(TAG, "onErrorResponse()");
              }

              @Override
              public void onResponse(RongImModel response, boolean result, String errorCode,
                  @Nullable String msg) {
                if (response != null) {
                  LogUtils.e(TAG, "onResponse() token = " + response.getRongYunToken());

                  //连接融云服务器
                  connectIMService(response.getRongYunToken(), null);

                  //保存融云token到本地
                  SPUtils.setRongYunToken(response.getRongYunToken());
                }
              }
            });
    request.start(context);
  }

  /**
   * 建立与融云服务器的连接
   */
  public static void connectIMService(final String token, final RongYunConnectListener listener) {

    RongIM.connect(token, new RongIMClient.ConnectCallback() {

      /**
       * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
       */
      @Override public void onTokenIncorrect() {
        getToken(AppContext.mContext);
        LogUtils.e(TAG, "onTokenIncorrect()");
      }

      /**
       * 连接融云成功
       * @param userid 当前 token
       */
      @Override public void onSuccess(String userid) {
        LogUtils.e(TAG, "onSuccess()-----" + "userid = " + userid + ", token = " + token);
        if (listener != null) {
          listener.connectSuccess();
        }

        //指定当前用户信息
        if (AppContext.user != null) {
          UserInfoModel user = AppContext.user;
          int userId = user.getUserId();
          String fullName = user.getFullname();
          String userName = user.getUsername();
          String avatar = user.getPhoto();

          LogUtils.e(TAG,
              "userId = " + userid + ", fullName = " + fullName + ", userName = " + userName
                  + ", avatar = " + avatar);

          // 是指当前用户信息
          UserInfo userInfo = new UserInfo(String.valueOf(userId), fullName, Uri.parse(avatar));
          RongIM.getInstance().setCurrentUserInfo(userInfo);

          //设置消息携带用户信息
          RongIM.getInstance().setMessageAttachedUserInfo(true);
        }
      }

      /**
       * 连接融云失败
       * @param errorCode 错误码，可到官网 查看错误码对应的注释
       */
      @Override public void onError(RongIMClient.ErrorCode errorCode) {
        LogUtils.e("IMPresenter", "onError()-----" + "errorCode = " + errorCode);
      }
    });
  }

  /**
   * 发送文本消息
   *
   * @param targetId    目标id
   * @param content     发送的内容
   * @param pushContent 当下发 push 消息时，在通知栏里会显示这个字段。
   *                    如果发送的是自定义消息，该字段必须填写，否则无法收到 push 消息。
   *                    如果发送 sdk 中默认的消息类型，例如 RC:TxtMsg, RC:VcMsg, RC:ImgMsg，则不需要填写，默认已经指定。
   * @param pushData    push附加信息。如果设置该字段，用户在收到 push 消息时，能通过
   *                    {@link io.rong.push.notification.PushNotificationMessage#getPushData()}
   *                    方法获取。
   */
  public static void sendTextMessage(String targetId, String content, String pushContent,
      String pushData) {
    TextMessage myTextMessage = TextMessage.obtain(content);

    Message message =
        Message.obtain(targetId, Conversation.ConversationType.PRIVATE, myTextMessage);

    if (RongIM.getInstance() != null) {
      RongIM.getInstance()
          .sendMessage(message, pushContent, pushData, new IRongCallback.ISendMessageCallback() {
            @Override public void onAttached(Message message) {
              //消息本地数据库存储成功的回调
              LogUtils.e("IMPresenter", "message send onAttached()");
            }

            @Override public void onSuccess(Message message) {
              //消息通过网络发送成功的回调
              LogUtils.e("IMPresenter", "message send onSuccess()");
            }

            @Override public void onError(Message message, RongIMClient.ErrorCode errorCode) {
              //消息发送失败的回调
              LogUtils.e("IMPresenter", "message send onError()");
            }
          });
    }
  }

  /**
   * 获取未读消息数
   */
  public static void getUnreadCount(RongIM.OnReceiveUnreadCountChangedListener listener) {
    if (RongIM.getInstance() != null) {
      RongIM.getInstance()
          .setOnReceiveUnreadCountChangedListener(listener, Conversation.ConversationType.PRIVATE);
    }
  }

  /**
   * 置顶会话
   */
  public static void setConversationToTop(String tagetId, boolean isTop) {
    if (RongIM.getInstance() != null) {
      RongIM.getInstance()
          .setConversationToTop(Conversation.ConversationType.PRIVATE, tagetId, isTop,
              new RongIMClient.ResultCallback<Boolean>() {
                @Override public void onSuccess(Boolean aBoolean) {
                }

                @Override public void onError(RongIMClient.ErrorCode errorCode) {
                }
              });
    }
  }

  /**
   * 移除会话
   */
  public static void removeConversation(String targetId) {
    if (RongIM.getInstance() != null) {
      RongIM.getInstance().removeConversation(Conversation.ConversationType.PRIVATE, targetId,
          new RongIMClient.ResultCallback<Boolean>() {
            @Override public void onSuccess(Boolean aBoolean) {

            }

            @Override public void onError(RongIMClient.ErrorCode errorCode) {

            }
          });
    }
  }

  /**
   * 删除消息
   */
  public static void deleteMessages(int[] messageIds) {
    if (RongIM.getInstance() != null) {
      RongIM.getInstance().deleteMessages(messageIds, new RongIMClient.ResultCallback<Boolean>() {
        @Override public void onSuccess(Boolean aBoolean) {

        }

        @Override public void onError(RongIMClient.ErrorCode errorCode) {

        }
      });
    }
  }

  /**
   * 加入黑名单
   */
  public static void addToBlackList(String targetId) {
    if (RongIM.getInstance() != null) {
      RongIM.getInstance().addToBlacklist(targetId, new RongIMClient.OperationCallback() {
        @Override public void onSuccess() {

        }

        @Override public void onError(RongIMClient.ErrorCode errorCode) {

        }
      });
    }
  }

  /**
   * 获取指定用户的消息
   */
  public static void get(String targetId, int count) {
    if (RongIM.getInstance() != null) {
      RongIM.getInstance().getLatestMessages(Conversation.ConversationType.PRIVATE, targetId, count,
          new RongIMClient.ResultCallback<List<Message>>() {
            @Override public void onSuccess(List<Message> messages) {

            }

            @Override public void onError(RongIMClient.ErrorCode errorCode) {

            }
          });
    }
  }

  public interface RongYunConnectListener {
    void connectSuccess();
  }
}
