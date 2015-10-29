package co.quchu.quchu.net;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * NetListener
 * User: Chenhs
 * Date: 2015-10-29
 */
public interface NetListener  extends Callback<JSONObject>{
    @Override
    void onResponse(Response<JSONObject> response, Retrofit retrofit);

    @Override
    void onFailure(Throwable t);
}
