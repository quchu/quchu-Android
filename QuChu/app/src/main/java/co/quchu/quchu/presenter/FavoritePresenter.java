package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import co.quchu.quchu.R;
import co.quchu.quchu.model.FavoriteBean;
import co.quchu.quchu.model.FavoriteEssayBean;
import co.quchu.quchu.model.FindBean;
import co.quchu.quchu.model.QuchuModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.view.activity.FindPositionListActivity;

/**
 * Created by no21 on 2016/4/5.
 * email:437943145@qq.com
 * desc : 收藏的趣处和文章
 */
public class FavoritePresenter {

    private QuchuModel model;
    private Context context;

    public FavoritePresenter(Context context) {
        this.context = context;
        model = new QuchuModel(context);
    }

    //获取收藏
    public void getFavoriteData(final int pageNo, final PageLoadListener<FavoriteBean> view) {
        model.getFavoriteData(pageNo, new ResponseListener<FavoriteBean>() {

            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                view.netError(pageNo, "");
            }

            @Override
            public void onResponse(FavoriteBean response, boolean result, @Nullable String exception, @Nullable String msg) {
                if (response == null) {
                    view.nullData();
                } else if (pageNo == 1) {
                    view.initData(response);
                } else {
                    view.moreData(response);
                }
            }
        });
    }


    //获取发现
    public void getFindData(final int pageNo, final PageLoadListener<FindBean> view) {


        model.getFindData(pageNo, new ResponseListener<FindBean>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                view.netError(pageNo, null);
            }

            @Override
            public void onResponse(FindBean response, boolean result, @Nullable String exception, @Nullable String msg) {
                if (response == null) {
                    view.nullData();
                } else if (pageNo == 1) {
                    view.initData(response);
                } else {
                    view.moreData(response);
                }
            }
        });
    }

    //删除我发现的趣处
    public void deleteMyFindQuchu(int placeId, final FindBean.ResultEntity entity, final RecyclerView.ViewHolder holder, final FindPositionListActivity view) {
        String uri = String.format(Locale.SIMPLIFIED_CHINESE, NetApi.deletePlace, placeId);

        GsonRequest<String> request = new GsonRequest<>(uri, new ResponseListener<String>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                Toast.makeText(view, (R.string.network_error), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, boolean result, String errorCode, @Nullable String msg) {
                if (result) {
                    view.deleteSucceed(holder, entity);
                } else {
                    Toast.makeText(view, (R.string.network_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
        request.start(view);

    }

    public void getFavoriteEssay(int cityId, final int pageNo, final PageLoadListener<FavoriteEssayBean> view) {
        Map<String, String> params = new HashMap<>();
        params.put("cityId", String.valueOf(cityId));
        params.put("pagesNo", String.valueOf(pageNo));


        GsonRequest<FavoriteEssayBean> request = new GsonRequest<>(NetApi.getFavoriteEssay, FavoriteEssayBean.class, params, new ResponseListener<FavoriteEssayBean>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                view.netError(pageNo, null);
            }

            @Override
            public void onResponse(FavoriteEssayBean response, boolean result, String errorCode, @Nullable String msg) {
                if (response == null) {
                    view.nullData();
                } else if (pageNo == 1) {
                    view.initData(response);
                } else {
                    view.moreData(response);
                }
            }
        });
        request.start(context);
    }

    /**
     * 取消收藏
     */
    public void deleteFavoriteEssay(int cityId, final int pageNo, final PageLoadListener<FavoriteEssayBean> view) {
        Map<String, String> params = new HashMap<>();
        params.put("cityId", String.valueOf(cityId));
        params.put("pagesNo", String.valueOf(pageNo));


        GsonRequest<FavoriteEssayBean> request = new GsonRequest<>(NetApi.getFavoriteEssay, FavoriteEssayBean.class, params, new ResponseListener<FavoriteEssayBean>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                view.netError(pageNo, null);
            }

            @Override
            public void onResponse(FavoriteEssayBean response, boolean result, String errorCode, @Nullable String msg) {
                if (response == null) {
                    view.nullData();
                } else if (pageNo == 1) {
                    view.initData(response);
                } else {
                    view.moreData(response);
                }
            }
        });
        request.start(context);
    }
}
