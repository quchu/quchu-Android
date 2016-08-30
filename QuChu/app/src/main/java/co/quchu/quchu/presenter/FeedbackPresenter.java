package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.quchu.quchu.model.FeedbackModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;

/**
 * 用户反馈接口请求
 * <p/>
 * Created by mwb on 16/8/23.
 */
public class FeedbackPresenter {

  /**
   * 获取用户反馈列表
   *
   * @param context
   * @param listener
   */
  public static void getFeedbackList(final Context context,
      final CommonListener<List<FeedbackModel>> listener) {

    GsonRequest<List<FeedbackModel>> request =
        new GsonRequest<>(NetApi.getFeedbackList, new TypeToken<List<FeedbackModel>>() {
        }.getType(), new ResponseListener<List<FeedbackModel>>() {
          @Override public void onErrorResponse(@Nullable VolleyError error) {
            if (listener != null) {
              listener.errorListener(error, "", "");
            }
          }

          @Override
          public void onResponse(List<FeedbackModel> response, boolean result, String errorCode,
              @Nullable String msg) {
            if (response != null && listener != null) {
              listener.successListener(response);
            }
          }
        });
    request.start(context);
  }

  /**
   * 提交用户反馈
   *
   * @param context
   * @param listener
   */
  public static void sendFeedback(Context context, String title, String value,
      final CommonListener listener) {
    Map<String, String> map = new HashMap<>();
    map.put("title", title);
    map.put("value", value);
    GsonRequest<Object> request =
        new GsonRequest<>(NetApi.sendFeedback, Object.class, map, new ResponseListener<Object>() {
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
   * 用户反馈详情列表
   */
  public static void getFeedMsgList(Context context, String feedbackId,
      final CommonListener<FeedbackModel> listener) {

    Map<String, String> map = new HashMap<>();
    map.put("feedbackId", feedbackId);

    GsonRequest<FeedbackModel> request =
        new GsonRequest<>(NetApi.getFeedMsgList, FeedbackModel.class, map,
            new ResponseListener<FeedbackModel>() {
              @Override public void onErrorResponse(@Nullable VolleyError error) {
                if (listener != null) {
                  listener.errorListener(error, "", "");
                }
              }

              @Override
              public void onResponse(FeedbackModel response, boolean result, String errorCode,
                  @Nullable String msg) {
                if (response != null && listener != null) {
                  listener.successListener(response);
                }
              }
            });
    request.start(context);
  }

  /**
   * 用户反馈详情发送反馈
   */
  public static void sendFeedMsg(Context context, String feedbackId, String content, final CommonListener listener) {
    Map<String, String> map = new HashMap<>();
    map.put("feedbackId", feedbackId);
    map.put("content", content);

    GsonRequest<Object> request = new GsonRequest<Object>(NetApi.sendFeedMsg, Object.class, map, new ResponseListener<Object>() {
      @Override public void onErrorResponse(@Nullable VolleyError error) {
        if (listener != null) {
          listener.errorListener(error, "", "");
        }
      }

      @Override public void onResponse(Object response, boolean result, String errorCode,
          @Nullable String msg) {
          if (response != null && listener != null) {
            listener.successListener(response);
          }
      }
    });
    request.start(context);
  }
}
