package co.quchu.quchu.presenter;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.SPUtils;

/**
 * 查看服务器信息
 * */
public class VersionInfoPresenter {

    public static void getVersionInfo(Context context, IRequestListener listener) {
        NetService.get(context, NetApi.getVersionInfo, null, listener);
    }

    public static void getIfForceUpdate(final Context context){
        NetService.get(context, NetApi.checkIfForceUpdate, null, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {

                if (null!=response && response.has("result")){
                    try {
                        if (response.getBoolean("result")){
                            SPUtils.setForceUpdateIfNecessary(context,response.getBoolean("result"));
                            SPUtils.setForceUpdateReason(context,response.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public boolean onError(String error) {
                return false;
            }
        });
    }
}
