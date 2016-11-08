package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import co.quchu.quchu.model.MessageModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;

/**
 * MessageCenterPresenter
 * User: Chenhs
 * Date: 2016-01-11
 */
public class MessagePresenter {

  /**
   * 动态
   */
  public static void getMessageList(Context mContext, final int pageNo, final PageLoadListener<MessageModel> listener) {
    String uri = NetApi.getMessageList + "?pageno=" + pageNo;

    GsonRequest<MessageModel> request = new GsonRequest<>(uri, MessageModel.class, new ResponseListener<MessageModel>() {
      @Override
      public void onErrorResponse(@Nullable VolleyError error) {
        listener.netError(pageNo, "");
      }

      @Override
      public void onResponse(MessageModel response, boolean result, @Nullable String exception, @Nullable String msg) {
        if (response == null) {
          listener.nullData();
        } else if (pageNo == 1) {
          listener.initData(response);
        } else {
          listener.moreData(response);
        }
      }
    });
    request.start(mContext, null);
  }

  /**
   * 系统消息
   */
  public static void getSysMessageList(Context context, final int pageNo, final PageLoadListener<MessageModel> listener) {
    String uri = NetApi.getSysMessageList + "?pageno=" + pageNo;

    GsonRequest<MessageModel> request = new GsonRequest<>(uri, MessageModel.class, new ResponseListener<MessageModel>() {
      @Override
      public void onErrorResponse(@Nullable VolleyError error) {
        listener.netError(pageNo, "");
      }

      @Override
      public void onResponse(MessageModel response, boolean result, @Nullable String exception, @Nullable String msg) {
        if (response == null) {
          listener.nullData();
        } else if (pageNo == 1) {
          listener.initData(response);
        } else {
          listener.moreData(response);
        }
      }
    });
    request.start(context);
  }

  /**
   * 未读消息数
   */
  public static void getUnreadMessageCount(Context context, final OnMessagePresenterListener listener) {
    GsonRequest<String> request = new GsonRequest<>(NetApi.notReadMessage, new ResponseListener<String>() {

      @Override
      public void onErrorResponse(@Nullable VolleyError error) {
      }

      @Override
      public void onResponse(String response, boolean result, @Nullable String exception, @Nullable String msg) {
        try {
          JSONObject jsonObject = new JSONObject(response);

          int msgCount = 0;
          int qCount = 0;
          int feedMsgCount = 0;

          if (jsonObject.has("msgCount")) {
            msgCount = jsonObject.getInt("msgCount");
          }

          if (jsonObject.has("qmsgCount")) {
            qCount = jsonObject.getInt("qmsgCount");
          }

          if (jsonObject.has("feedmsgCount")) {
            feedMsgCount = jsonObject.getInt("feedmsgCount");
          }

          if (msgCount > 0) {
            SPUtils.setHasPushMsg(true);
          }

          if (listener != null) {
            listener.onUnreadMessageCount(msgCount, feedMsgCount);
          }

        } catch (JSONException e) {
          e.printStackTrace();
        }

      }
    });
    request.start(context);
  }

  public static void followMessageCenterFriends(Context mContext, int friendsId, boolean isFollowing, final MessageGetDataListener listener) {
    String urlStr = "";
    if (isFollowing) {
      urlStr = String.format(NetApi.delFollowFriends, friendsId);
    } else {
      urlStr = String.format(NetApi.followFriends, friendsId);
    }
    NetService.get(mContext, urlStr, new IRequestListener() {
      @Override
      public void onSuccess(JSONObject response) {
        LogUtils.json("message follow==" + response);
        listener.onSuccess(null);
      }

      @Override
      public boolean onError(String error) {
        listener.onError();
        return false;
      }
    });
  }

  public interface OnMessagePresenterListener {
    void onUnreadMessageCount(int msgCount, int feedbackMsgCount);
  }

  public interface MessageGetDataListener {
    void onSuccess(MessageModel arrayList);

    void onError();
  }
}
