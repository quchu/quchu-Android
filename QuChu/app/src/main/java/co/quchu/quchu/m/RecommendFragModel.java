package co.quchu.quchu.m;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;

import co.quchu.quchu.model.RecommendModelNew;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.utils.AppKey;
import co.quchu.quchu.utils.SPUtils;

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
    public void getTab() {

    }

    @Override
    public void getTabData(final boolean isDefaultData, final CommonListener<RecommendModelNew> listener) {
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
    public void loadMore(boolean isDefaultData, int pageNumber, final CommonListener<RecommendModelNew> listener) {
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
