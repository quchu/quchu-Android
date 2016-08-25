package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;
import co.quchu.quchu.model.ArticleDetailModel;
import co.quchu.quchu.model.FeedbackModel;
import co.quchu.quchu.model.HangoutUserModel;
import co.quchu.quchu.model.PagerModel;
import co.quchu.quchu.model.SceneModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.net.ResponseListener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nico on 16/8/22.
 */
public class HangoutPresenter {

  public static void getHangoutUsers(Context context,
      final CommonListener<List<HangoutUserModel>> listener) {

    GsonRequest<List<HangoutUserModel>> request = new GsonRequest<>(NetApi.getHangoutUsers, new TypeToken<List<HangoutUserModel>>() {
    }.getType(), new ResponseListener<List<HangoutUserModel>>() {
      @Override
      public void onErrorResponse(@Nullable VolleyError error) {
        if (listener != null) {
          listener.errorListener(error, "", "");
        }
      }

      @Override
      public void onResponse(List<HangoutUserModel> response, boolean result, String errorCode, @Nullable String msg) {
        if (response != null && listener != null) {
          listener.successListener(response);
        }
      }
    });
    request.start(context);

  }

  public static void inviteUser(Context context, int userId, int pId,
      final CommonListener<String> listener) {

    HashMap<String, String> params = new HashMap<>();
    params.put("userId", String.valueOf(userId));
    params.put("placeId", String.valueOf(pId));
    NetService.get(context, NetApi.sendInvite, params, new IRequestListener() {
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
