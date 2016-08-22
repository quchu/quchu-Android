package co.quchu.quchu.presenter;

import android.content.Context;
import co.quchu.quchu.model.ArticleDetailModel;
import co.quchu.quchu.model.HangoutUserModel;
import co.quchu.quchu.model.PagerModel;
import co.quchu.quchu.model.SceneModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;

/**
 * Created by Nico on 16/8/22.
 */
public class HangoutPresenter {

  public static void getMapNearbyData(Context context, final CommonListener<List<HangoutUserModel>> listener) {

    NetService.get(context, NetApi.getHangoutUsers, new HashMap<String, String>(),
        new IRequestListener() {
          @Override public void onSuccess(JSONObject response) {
            if (null!=response && !response.isNull("data")){
              List data = new Gson().fromJson(response.toString(), new TypeToken<List<HangoutUserModel>>() {}.getType());
              listener.successListener(data);
            }else{
              listener.successListener(null);
            }

          }

          @Override public boolean onError(String error) {
            listener.errorListener(null, null, null);
            return false;
          }
        });
  }

  public static void inviteUser(Context context,int userId,int pId, final CommonListener<String> listener) {

    HashMap<String,String> params = new HashMap<>();
    params.put("userId",String.valueOf(userId));
    params.put("placeId",String.valueOf(pId));
    NetService.get(context, NetApi.sendInvite, params,
        new IRequestListener() {
          @Override public void onSuccess(JSONObject response) {
            listener.successListener(null);
          }

          @Override public boolean onError(String error) {
            listener.errorListener(null, null, null);
            return false;
          }
        });
  }
}
