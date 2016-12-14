package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import co.quchu.quchu.model.PagerModel;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.NearbyItemModel;
import co.quchu.quchu.model.NearbyMapModel;
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

    public static final int TYPE_AREA = 1;
    public static final int TYPE_CIRCLE = 0;


    public static void getQuchuListViaTagId(Context context,int type,int tagId,int cityId,String lat,String lon,final CommonListener<PagerModel> listener){
        HashMap<String,String> params = new HashMap<>();
        String url;
        if (type==-1){
            url = String.format(NetApi.getQuchuListViaTagId,tagId, cityId, lat, lon);
        }else{
            if (type==TYPE_CIRCLE){
                params.put("areaId",String.valueOf(tagId));
                url = NetApi.getPlaceByAreaId;
            }else{
                params.put("circleId",String.valueOf(tagId));
                url = NetApi.getPlaceByCircleId;
            }
            params.put("latitude",lat);
            params.put("longitude",lon);
        }
        NetService.get(context, url, params,new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                PagerModel<NearbyItemModel> sceneModels = new Gson().fromJson(response.toString(), new TypeToken<PagerModel<NearbyItemModel>>() {}.getType());
                listener.successListener(sceneModels);
            }

            @Override
            public boolean onError(String error) {
                DialogUtil.dismissProgress();
                return false;
            }
        });

    }



    public static void getNearbyData(Context context, String recommendPlaceIds, String categoryTagIds, int isFirst, int placeId, int cityId, double latitude, double longitude, int pageNo, final getNearbyDataListener listener) {
        String url = String.format(NetApi.getNearby, cityId, String.valueOf(latitude), String.valueOf(longitude), pageNo, recommendPlaceIds, categoryTagIds, isFirst, placeId);
        NetService.post(context, url, null,new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response != null && response.has("result") && response.has("pageCount")) {
                    int maxPageNo = -1;
                    Gson gson = new Gson();
                    List<NearbyItemModel> nearbyItemModels = null;
                    try {
                        maxPageNo = response.getInt("pageCount");
                        nearbyItemModels = gson.fromJson(response.getString("result"), new TypeToken<List<NearbyItemModel>>() {
                        }.getType());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    listener.getNearbyData(nearbyItemModels, maxPageNo);
                }
            }

            @Override
            public boolean onError(String error) {
                DialogUtil.dismissProgress();
                return false;
            }
        });
    }

    public static void getMapNearbyData(Context context, int cityId, String name, double latitude, double longitude, final CommonListener<List<NearbyMapModel>> listener) {

        String url = String.format(NetApi.getMapNearby, name, cityId, String.valueOf(latitude), String.valueOf(longitude));
        GsonRequest<List<NearbyMapModel>> request = new GsonRequest<>(Request.Method.GET, url, new TypeToken<List<NearbyMapModel>>() {
        }.getType(), new ResponseListener<List<NearbyMapModel>>() {
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

    public static void getFilterData(Context context, final CommonListener<List<TagsModel>> listener) {

        GsonRequest<List<TagsModel>> request = new GsonRequest<>(Request.Method.GET, NetApi.getFilterTags, new TypeToken<List<TagsModel>>() {
        }.getType(), new ResponseListener<List<TagsModel>>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                listener.errorListener(error, "", "");
            }

            @Override
            public void onResponse(List<TagsModel> response, boolean result, @Nullable String exception, @Nullable String msg) {
                listener.successListener(response);
            }
        });
        request.start(context, null);

    }


    public static void getSearchResult(Context context, int cityId, String name, final CommonListener<List<SimpleQuchuSearchResultModel>> listener) {

        String url = String.format(Locale.CHINA, NetApi.getSearchResult, cityId, name);

        GsonRequest<List<SimpleQuchuSearchResultModel>> request = new GsonRequest<>(Request.Method.GET, url, new TypeToken<List<SimpleQuchuSearchResultModel>>() {
        }.getType(), new ResponseListener<List<SimpleQuchuSearchResultModel>>() {
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
        void getNearbyData(List<NearbyItemModel> model, int i);
    }


    public interface GenericListener {
        void onCallBack(Object o);
    }


}
