package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.DetailModel;
import co.quchu.quchu.model.NearbyItemModel;
import co.quchu.quchu.model.NearbyMapModel;
import co.quchu.quchu.model.PostCardModel;
import co.quchu.quchu.model.SimpleQuchuSearchResultModel;
import co.quchu.quchu.model.TagsModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.net.ResponseListener;

/**
 * InterestingDetailPresenter
 * User: Chenhs
 * Date: 2015-12-13
 */
public class NearbyPresenter {
    public static void getNearbyData(Context context, int cityId,String tags,double latitude,double longitude,int pageNo, final getNearbyDataListener listener) {
        NetService.get(context, String.format(NetApi.getNearby, tags,cityId,String.valueOf(latitude),String.valueOf(longitude),pageNo), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response != null && response.has("result") && response.has("pageCount")) {
                    int maxPageNo = -1;
                    Gson gson = new Gson();
                    List<NearbyItemModel> nearbyItemModels = null;
                    try {
                        maxPageNo = response.getInt("pageCount");
                        nearbyItemModels = gson.fromJson(response.getString("result"), new TypeToken<List<NearbyItemModel>>(){}.getType());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    listener.getNearbyData(nearbyItemModels,maxPageNo);
                }
            }

            @Override
            public boolean onError(String error) {
                DialogUtil.dismissProgess();
                return false;
            }
        });
    }

    public static void getMapNearbyData(Context context, int cityId,String name,double latitude,double longitude,final CommonListener<List<NearbyMapModel>> listener) {

        String url = String.format(NetApi.getMapNearby,name,cityId,String.valueOf(latitude),String.valueOf(longitude));
        GsonRequest<List<NearbyMapModel>> request = new GsonRequest<>(Request.Method.GET,url, new TypeToken<List<NearbyMapModel>>(){}.getType(), new ResponseListener<List<NearbyMapModel>>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                listener.errorListener(error, "", "");
            }

            @Override
            public void onResponse(List<NearbyMapModel> response, boolean result, @Nullable String exception, @Nullable String msg) {
                listener.successListener(response);
            }
        });
        request.start(context, null);

    }


    public static void getSearchResult(Context context, int cityId,String name,final CommonListener<List<SimpleQuchuSearchResultModel>> listener) {

        String url = String.format(NetApi.getSearchResult,cityId,name);

        GsonRequest<List<SimpleQuchuSearchResultModel>> request = new GsonRequest<>(Request.Method.GET,url, new TypeToken<List<SimpleQuchuSearchResultModel>>(){}.getType(), new ResponseListener<List<SimpleQuchuSearchResultModel>>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                listener.errorListener(error, "", "");
            }

            @Override
            public void onResponse(List<SimpleQuchuSearchResultModel> response, boolean result, @Nullable String exception, @Nullable String msg) {
                listener.successListener(response);
            }
        });
        request.start(context, null);

    }

    public interface getNearbyDataListener {
        void getNearbyData(List<NearbyItemModel> model,int i);
    }


}
