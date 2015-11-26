package co.quchu.quchu.presenter;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.LogUtils;

/**
 * UserLoginPresenter
 * User: Chenhs
 * Date: 2015-11-26
 */
public class UserLoginPresenter {
    public static void decideMobileCanLogin(Context context,String mobileNo){
        JSONObject _obj = new JSONObject();
        try {
            _obj.put("value",mobileNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (_obj==null){
            LogUtils.json("_obj is null");
        }
        NetService.get(context, String.format(NetApi.IsUnique,mobileNo), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json(response.toString());
            }

            @Override
            public boolean onError(String error) {
                return false;
            }
        });
    }


}
