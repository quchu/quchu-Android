package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.ToastManager;

/**
 * Created by mwb on 16/8/22.
 */
public class SettingPresenter {

  //是否推荐趣处
  public static String SETTING_TYPE_QUCHU = "0";
  //是否推荐文章
  public static String SETTING_TYPE_NEWS = "1";
  //是否推荐趣星人
  public static String SETTING_TYPE_QUCHU_USER = "2";
  //是否开启搭伙
  public static String SETTING_TYPE_DAHUO = "3";

  /**
   * @param type      0 '是否推荐趣处', 1 '是否推荐文章', 2 '是否推荐趣星人', 3 '是否开启搭伙'
   * @param isChecked false - 0 不推荐; true - 1 推荐
   */
  public static void setUserMsg(final Context context, final String type, final boolean isChecked) {

    Map<String, String> map = new HashMap<>();
    map.put("type", type);
    if (isChecked) {
      map.put("value", "1");
    } else {
      map.put("value", "0");
    }

    GsonRequest<Object> request =
        new GsonRequest<>(NetApi.setUserMsg, Object.class, map, new ResponseListener() {
          @Override public void onErrorResponse(@Nullable VolleyError error) {
            ToastManager.getInstance(context).show("设置失败");
          }

          @Override public void onResponse(Object response, boolean result, String errorCode,
              @Nullable String msg) {
            if (type.equals(SETTING_TYPE_DAHUO)) {
              //搭伙
              SPUtils.setDahuoSwitch(isChecked);

            } else if (type.equals(SETTING_TYPE_NEWS)) {
              //推荐文章
              SPUtils.setNewsSwitch(isChecked);

            } else if (type.equals(SETTING_TYPE_QUCHU)) {
              //推荐趣处
              SPUtils.setQuchuSwitch(isChecked);

            } else if (type.equals(SETTING_TYPE_QUCHU_USER)) {
              //推荐趣星人
              SPUtils.setQuchuUserSwitch(isChecked);
            }

            ToastManager.getInstance(context).show("设置成功");
          }
        });
    request.start(context);
  }

  public interface OnUserMsgListener {
    void onSuccess();

    void onError();
  }
}
