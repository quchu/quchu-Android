package co.quchu.quchu.net;

import org.json.JSONObject;

import co.quchu.quchu.utils.LogUtils;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * CallbackImpl
 * User: Chenhs
 * Date: 2015-10-29
 */
public abstract class CallbackImpl implements Callback<JSONObject> {
    @Override
    public void onResponse(Response<JSONObject> response, Retrofit retrofit) {
        if (response != null && response.code() == 200) {
            if (response.body() != null) {
                try {
                    LogUtils.netLog("Header==" + response.headers().toString());
//                    if (response.body().has("result") && response.body().getBoolean("result")) {
                 JSONObject body=   response.body();
                   if (body.has("result"))
                        onSuccess(response.body());
//                    } else {
//                        if (response.body().has("msg")) {
//                            onError(response.body().getString("msg"));
//                        }
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.netLog(e);
                }
            }
//            onError();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        LogUtils.netLog(t);
    }

    public abstract void onSuccess(JSONObject response);

    public abstract void onError(String errorMsg);


}
