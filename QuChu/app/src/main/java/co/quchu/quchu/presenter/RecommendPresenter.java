package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.CityEntity;
import co.quchu.quchu.model.CityModel;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.utils.AppKey;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;

/**
 * RecommendPresenter
 * User: Chenhs
 * Date: 2015-12-08
 * 趣处推荐 逻辑类
 */
public class RecommendPresenter {


    public static void getRecommendList(final Context context, boolean isDefaultData, final GetRecommendListener listener) {
        DialogUtil.showProgess(context, "数据加载中...");
        final String urlStr;
        if (isDefaultData) {
            urlStr = String.format(NetApi.getDefaultPlaceList,
                    SPUtils.getCityId(), SPUtils.getLatitude(), SPUtils.getLongitude(), 1
            );
        } else {
            urlStr = String.format(NetApi.getPlaceList, SPUtils.getCityId(),
                    SPUtils.getValueFromSPMap(context, AppKey.USERSELECTEDCLASSIFY, ""), SPUtils.getLatitude(), SPUtils.getLongitude(), 1
            );
        }
        DialogUtil.showProgess(context,"加载中!!");

        NetService.get(context, urlStr, new IRequestListener() {


            @Override
            public void onSuccess(JSONObject response) {
                int pageCount = 2, pageNum = 1;

                LogUtils.json("getPlaceList==" + response.toString());
                try {
                    if (!response.has("msg") && response.has("result")) {
                        if (response.has("pageCount"))
                            pageCount = response.getInt("pageCount");
                        if (response.has("pagesNo"))
                            pageNum = response.getInt("pagesNo");
                        JSONArray array = response.getJSONArray("result");
                        if (array.length() > 0) {
                            Gson gson = new Gson();
                            ArrayList<RecommendModel> arrayList = new ArrayList<>();
                            RecommendModel model;
                            for (int i = 0; i < array.length(); i++) {
                                model = gson.fromJson(array.getString(i), RecommendModel.class);
                                arrayList.add(model);
                            }
                            listener.onSuccess(arrayList, pageCount, pageNum);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                DialogUtil.dismissProgess();
            }

            @Override
            public boolean onError(String error) {
                DialogUtil.dismissProgess();
                return false;
            }
        });
    }

    /**
     * 加载更多
     *
     * @param context       c
     * @param isDefaultData s
     * @param listener      s
     */
    public static void loadMoreRecommendList(final Context context, boolean isDefaultData, int pageNumber, final GetRecommendListener listener) {
        //    DialogUtil.showProgess(context, "数据加载中...");
        String urlStr = "";
        if (isDefaultData) {
            urlStr = String.format(NetApi.getDefaultPlaceList,
                    SPUtils.getCityId(), SPUtils.getLatitude(), SPUtils.getLongitude(), pageNumber
            );
        } else {
            urlStr = String.format(NetApi.getPlaceList, SPUtils.getCityId(),
                    SPUtils.getValueFromSPMap(context, AppKey.USERSELECTEDCLASSIFY, ""), SPUtils.getLatitude(), SPUtils.getLongitude(), pageNumber
            );
        }
        NetService.get(context, urlStr, new IRequestListener() {

            @Override
            public void onSuccess(JSONObject response) {
                int pageCount = 2, pageNum = 1;

                LogUtils.json("getPlaceList==" + response.toString());
                try {
                    if (response.has("result") && !StringUtils.isEmpty(response.getString("result"))) {
                        if (response.has("pageCount"))
                            pageCount = response.getInt("pageCount");
                        if (response.has("pagesNo"))
                            pageNum = response.getInt("pagesNo");


                        LogUtils.json("pageCount==" + pageCount + "///pageNum==" + pageNum);
                        JSONArray array = response.getJSONArray("result");
                        if (array.length() > 0) {
                            Gson gson = new Gson();
                            ArrayList<RecommendModel> arrayList = new ArrayList<>();
                            RecommendModel model;
                            for (int i = 0; i < array.length(); i++) {
                                model = gson.fromJson(array.getString(i), RecommendModel.class);
                                arrayList.add(model);
                            }
                            listener.onSuccess(arrayList, pageCount, pageNum);

                        } else {
                            listener.onError();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onError();
                }
            }

            @Override
            public boolean onError(String error) {
                listener.onError();
                return false;
            }
        });
    }


    public interface GetRecommendListener {
        void onSuccess(ArrayList<RecommendModel> arrayList, int pageCount, int pageNum);

        void onError();
    }


    public static void getCityList(Context context, final CityListListener listener) {
        GsonRequest<CityEntity> request = new GsonRequest<>(NetApi.GetCityList, CityEntity.class, new ResponseListener<CityEntity>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {

            }

            @Override
            public void onResponse(CityEntity response, boolean result, @Nullable String exception, @Nullable String msg) {
                SPUtils.setCityId(response.getDefaultX().getCid());
                SPUtils.setCityName(response.getDefaultX().getCvalue());
                ArrayList<CityModel> list = response.getPage().getResult();
                for (CityModel item : list) {
                    if (item.getCid() == response.getDefaultX().getCid()) {
                        item.setIsSelected(true);
                        break;
                    }
                }
                listener.hasCityList(list);
            }
        });
        request.start(context, null);
    }

    public interface CityListListener {
        void hasCityList(ArrayList<CityModel> list);
    }
}
