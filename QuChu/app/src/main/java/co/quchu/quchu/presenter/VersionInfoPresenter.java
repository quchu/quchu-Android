package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;

import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.utils.SPUtils;

/**
 * 查看服务器信息
 */
public class VersionInfoPresenter {

    public static void getVersionInfo(Context context, IRequestListener listener) {
        NetService.get(context, NetApi.getVersionInfo, null, listener);
    }

    public static void getIfForceUpdate(final Context context) {
        GsonRequest<String> request = new GsonRequest<>(NetApi.checkIfForceUpdate, null, new ResponseListener<String>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
            }

            @Override
            public void onResponse(String response, boolean result, String errorCode, @Nullable String msg) {
                if (result) {
                    SPUtils.setForceUpdateIfNecessary(context, true);
                    SPUtils.setForceUpdateReason(context, msg);
                    SPUtils.setForceUpdateUrl(context,response);
                }
            }
        });
        request.start(context, null);

    }
}
