package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import co.quchu.quchu.model.QuChuHistoryModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.utils.SPUtils;

/**
 * Created by mwb on 16/11/5.
 */
public class QuChuHistoryPresenter {

  public static void getHistory(Context context, String placeIds, final int pagesNo, final CommonListener<QuChuHistoryModel> listener) {
    Map<String, String> map = new HashMap<>();
    if (!TextUtils.isEmpty(placeIds)) {
      map.put("placeIds", placeIds);
    }
    map.put("longitude", String.valueOf(SPUtils.getLongitude()));
    map.put("latitude", String.valueOf(SPUtils.getLatitude()));
    map.put("cityId", String.valueOf(SPUtils.getCityId()));
    map.put("pagesNo", String.valueOf(pagesNo));

    GsonRequest<QuChuHistoryModel> request = new GsonRequest<QuChuHistoryModel>(NetApi.getHistoryPlace, QuChuHistoryModel.class, map, new ResponseListener<QuChuHistoryModel>() {
      @Override
      public void onErrorResponse(@Nullable VolleyError error) {
        listener.errorListener(error, "", "");
      }

      @Override
      public void onResponse(QuChuHistoryModel response, boolean result, String errorCode, @Nullable String msg) {
        listener.successListener(response);
      }
    });
    request.start(context);
  }
}
