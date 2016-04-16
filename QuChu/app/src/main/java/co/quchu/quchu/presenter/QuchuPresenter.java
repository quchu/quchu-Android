package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;

import co.quchu.quchu.model.FavoriteBean;
import co.quchu.quchu.model.FindBean;
import co.quchu.quchu.model.QuchuModel;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.view.PageLoadListener;

/**
 * Created by no21 on 2016/4/5.
 * email:437943145@qq.com
 * desc : 获取发现和收藏
 */
public class QuchuPresenter {

    private QuchuModel model;

    public QuchuPresenter(Context context) {
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
                if (result) {
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
                if (result) {
                    view.nullData();
                } else if (pageNo == 1) {
                    view.initData(response);
                } else {
                    view.moreData(response);
                }
            }
        });
    }


}
