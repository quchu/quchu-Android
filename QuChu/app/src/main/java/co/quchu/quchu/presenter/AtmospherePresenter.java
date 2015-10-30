package co.quchu.quchu.presenter;

import android.app.Activity;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.quchu.quchu.model.AtmosphereItemModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.LogUtils;

/**
 * AtmospherePresenter
 * User: Chenhs
 * Date: 2015-10-30
 * 氛围Presenter
 */
public class AtmospherePresenter {

    public static void getAtmData(Activity activity, final AtmosphereListener listener){
        NetService.get(activity, NetApi.GetCityList, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json(response.toString());
                Gson gson =  new Gson();
                AtmosphereItemModel aim;
                if (response.has("result")){
                    ArrayList<AtmosphereItemModel> list = new ArrayList<AtmosphereItemModel>();
                    try {
                        JSONArray jsonArry= response.getJSONArray("result");
                      for (int i = 0;i<jsonArry.length();i++){
                          aim = gson.fromJson(jsonArry.getString(i), AtmosphereItemModel.class);
                          list.add(aim);
                      }
                        listener.onSuccess(list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public boolean onError(String error) {
                LogUtils.e(error);
                return false;
            }
        });
    }

   public interface AtmosphereListener{
        void onSuccess(ArrayList<AtmosphereItemModel> arrayList);
    }

}
