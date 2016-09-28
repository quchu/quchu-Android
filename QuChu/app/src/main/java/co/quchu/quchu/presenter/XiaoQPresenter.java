package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import co.quchu.quchu.model.SysMessage;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;

/**
 * Created by mwb on 16/9/20.
 */
public class XiaoQPresenter {

  private final Context mContext;

  public XiaoQPresenter(Context context) {
    mContext = context;
  }

  /**
   * 获取所有消息
   */
  public void getMessage(final int pageNo, final CommonListener<List<SysMessage>> listener) {
    GsonRequest<List<SysMessage>> request = new GsonRequest<List<SysMessage>>(NetApi.getQMsg, new TypeToken<List<SysMessage>>() {
    }.getType(), new ResponseListener<List<SysMessage>>() {
      @Override
      public void onErrorResponse(@Nullable VolleyError error) {
        if (listener != null) {
          listener.errorListener(error, "", "");
        }
      }

      @Override
      public void onResponse(List<SysMessage> response, boolean result, String errorCode, @Nullable String msg) {
        if (listener != null && response != null) {
          listener.successListener(response);
        }
      }
    });
    request.start(mContext);
  }

  /**
   * 获取未读消息数
   */
  public void getUnreadMsgCount(final CommonListener<Integer> listener) {
    GsonRequest<String> request = new GsonRequest<>(NetApi.notReadMassage, new ResponseListener<String>() {

      @Override
      public void onErrorResponse(@Nullable VolleyError error) {
      }

      @Override
      public void onResponse(String response, boolean result, @Nullable String exception, @Nullable String msg) {
        try {
          JSONObject jsonObject = new JSONObject(response);

          int count = jsonObject.getInt("msgCount");
          if (count > 0) {
            listener.successListener(count);
          }

        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });
    request.start(mContext);
  }
}
