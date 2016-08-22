package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;

/**
 * Created by mwb on 16/8/22.
 */
public class SettingPresenter {

    /**
     * @param type  0 '是否推荐趣处', 1 '是否推荐文章', 2 '是否推荐趣星人', 3 '是否开启搭伙'
     * @param value 0 不推荐 1 推荐
     */
    public static void setUserMsg(Context context, String type, String value, final OnUserMsgListener listener) {

        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("value", value);

        GsonRequest<Object> request = new GsonRequest<>(NetApi.setUserMsg, Object.class, map, new ResponseListener() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                if (listener != null) {
                    listener.onError();
                }
            }

            @Override
            public void onResponse(Object response, boolean result, String errorCode, @Nullable String msg) {
                if (listener != null) {
                    listener.onSuccess();
                }
            }
        });
        request.start(context);
    }

    public interface OnUserMsgListener {
        void onSuccess();

        void onError();
    }
}
