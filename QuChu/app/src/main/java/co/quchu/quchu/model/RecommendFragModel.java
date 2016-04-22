package co.quchu.quchu.model;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;

/**
 * Created by linqipeng on 2016/2/26 11:18
 * email:437943145@qq.com
 * desc:
 */
public class RecommendFragModel implements IRecommendFragModel {
    private Context context;

    public RecommendFragModel(Context context) {
        this.context = context;
    }

    @Override
    public void getTab(final CommonListener<List<TagsModel>> listener) {
        String uri = String.format(NetApi.getCategoryTags, SPUtils.getCityId());

        GsonRequest<List<TagsModel>> request = new GsonRequest<>(Request.Method.GET, uri, new TypeToken<List<TagsModel>>() {}.getType(), new ResponseListener<List<TagsModel>>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                listener.errorListener(error, "", "");
            }

            @Override
            public void onResponse(@Nullable List<TagsModel> response, boolean result, @Nullable String exception, @Nullable String msg) {
                listener.successListener(response);
            }
        });

        request.start(context, null);
    }

    @Override
    public void getTabData(String isDefaultData, final CommonListener<RecommendModelNew> listener) {
        String urlStr;
        urlStr = String.format(NetApi.getPlaceList, SPUtils.getCityId(), isDefaultData,
                SPUtils.getLatitude(), SPUtils.getLongitude(), 1
        );
        LogUtils.json(urlStr);

        GsonRequest<RecommendModelNew> request = new GsonRequest<>(urlStr, RecommendModelNew.class, new ResponseListener<RecommendModelNew>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                listener.errorListener(error, null, null);
            }

            @Override
            public void onResponse(RecommendModelNew response, boolean result, @Nullable String exception, @Nullable String msg) {
                listener.successListener(response);
            }
        });
        request.start(context, null);
    }

    @Override
    public void loadMore(String type, int pageNumber, final CommonListener<RecommendModelNew> listener) {
        String urlStr = String.format(NetApi.getPlaceList, SPUtils.getCityId(), type,
                SPUtils.getLatitude(), SPUtils.getLongitude(), pageNumber);
        LogUtils.json(urlStr);
        GsonRequest<RecommendModelNew> request = new GsonRequest<>(urlStr, RecommendModelNew.class, new ResponseListener<RecommendModelNew>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                listener.errorListener(error, null, null);
            }

            @Override
            public void onResponse(RecommendModelNew response, boolean result, @Nullable String exception, @Nullable String msg) {
                listener.successListener(response);
            }
        });
        request.start(context, null);
    }
}
