package co.quchu.quchu.im;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.im.model.RongImModel;
import co.quchu.quchu.model.UserCenterInfo;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.UserCenterPresenter;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.ToastManager;
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

  //用户信息，名称，头像
  private static UserInfo userInfo;
  private static String name;
  private static String avatar;

  /**
   * 举报
   */
  public static void sendImReport(Context context, String targetId, String content, final CommonListener<Object> listener) {
    Map<String, String> map = new HashMap<>();
    map.put("toUserId", targetId);
    map.put("content", content);

    GsonRequest<Object> request = new GsonRequest<Object>(NetApi.sendImReport, Object.class, map, new ResponseListener<Object>() {
      @Override public void onErrorResponse(@Nullable VolleyError error) {
        if (listener != null) {
          listener.errorListener(error, "", "");
        }
      }

      @Override public void onResponse(Object response, boolean result, String errorCode,
          @Nullable String msg) {
        if (listener != null) {
          listener.successListener(response);
        }
      }
    });
    request.start(context);
  }

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
        LogUtils.e(TAG, "connectIMService() ---- onSuccess()-----" + "userid = " + userid + ", token = " + token);
        if (listener != null) {
          listener.onSuccess("");
        }

        //指定用户信息
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
          @Override public UserInfo getUserInfo(String userId) {

            LogUtils.e(TAG, "getUserInfo() ===== userId = " + userId);

            return findUserById(userId);
          }
        }, true);
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
   * 在App服务器查询用户信息，返回给融云显示
   */
  private static UserInfo findUserById(final String userId) {
    UserCenterPresenter.getUserCenterInfo(AppContext.mContext, Integer.valueOf(userId),
        new UserCenterPresenter.UserCenterInfoCallBack() {
          @Override public void onSuccess(UserCenterInfo userCenterInfo) {
            if (userCenterInfo != null) {
              name = userCenterInfo.getName();
              avatar = userCenterInfo.getPhoto();

              userInfo = new UserInfo(userId, name, Uri.parse(avatar));

              //刷新用户缓存用户
              if (RongIM.getInstance() != null) {
                LogUtils.e(TAG, "UserInfo : " + ", userId = " + userId + ", name = " + name + ", avatar = " + avatar);
                RongIM.getInstance().refreshUserInfoCache(new UserInfo(userId, name, Uri.parse(
                    avatar)));
              }
            }
          }

          @Override public void onError() {
            LogUtils.e(TAG, "UserCenterInfo-----onError()");
          }
        });

    return userInfo;
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
  public static void setConversationToTop(String targetId, boolean isTop) {
    if (RongIM.getInstance() != null) {
      RongIM.getInstance()
          .setConversationToTop(Conversation.ConversationType.PRIVATE, targetId, isTop,
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
   * 撤回消息
   */
  public static void recallMessage(Message message) {
    if (RongIM.getInstance() != null) {
      RongIM.getInstance().recallMessage(message);
    }
  }

  /**
   * 加入黑名单
   */
  public static void addToBlackList(String targetId) {
    if (RongIM.getInstance() != null) {
      RongIM.getInstance().addToBlacklist(targetId, new RongIMClient.OperationCallback() {
        @Override public void onSuccess() {
          ToastManager.getInstance(AppContext.mContext).show("您将不会再收到该用户的消息！");
        }

        @Override public void onError(RongIMClient.ErrorCode errorCode) {

        }
      });
    }
  }

  /**
   * 获取本地聊天列表
   */
  public static void getConversationList(
      RongIMClient.ResultCallback<List<Conversation>> resultCallback) {
    if (RongIM.getInstance() != null) {
      RongIM.getInstance()
          .getConversationList(resultCallback, Conversation.ConversationType.PRIVATE);
    }
  }

  /**
   * 获取指定用户的消息
   */
  public static void getLatestMessages(final String targetId, int count, final RongYunConnectListener listener) {
    if (RongIM.getInstance() != null) {
      RongIM.getInstance().getLatestMessages(Conversation.ConversationType.PRIVATE, targetId, count,
          new RongIMClient.ResultCallback<List<Message>>() {
            @Override public void onSuccess(List<Message> messages) {
              StringBuffer sb = new StringBuffer();
              for (Message message : messages) {
                try {
                  JSONObject jsonObject = new JSONObject(new String(message.getContent().encode()));
                  String content = jsonObject.getString("content");
                  sb.append(content);
                  sb.append(",");
                  if (listener != null) {
                    listener.onSuccess(sb.toString());
                  }
                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }
            }

            @Override public void onError(RongIMClient.ErrorCode errorCode) {

            }
          });
    }
  }

  public interface RongYunConnectListener {
    void onSuccess(String msg);
  }
}
