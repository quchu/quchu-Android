package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;

import co.quchu.quchu.model.FavoriteBean;
import co.quchu.quchu.model.FindBean;
import co.quchu.quchu.model.QuchuModel;
import co.quchu.quchu.net.ResponseListener;

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
    public void getFavoriteData(int pageNo, final IFavoriteFragment view) {
        model.getFavoriteData(pageNo, new ResponseListener<FavoriteBean>() {

            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                view.showData(true, null);
            }

            @Override
            public void onResponse(FavoriteBean response, boolean result, @Nullable String exception, @Nullable String msg) {
                view.showData(false, response);
            }
        });
    }


    //获取收藏
    public void getFavoriteMoreData(int pageNo, final IFavoriteFragment view) {
        model.getFavoriteData(pageNo, new ResponseListener<FavoriteBean>() {

            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                view.showMoreData(true, null);
            }

            @Override
            public void onResponse(FavoriteBean response, boolean result, @Nullable String exception, @Nullable String msg) {
                view.showMoreData(false, response);
            }
        });
    }


    //获取发现
    public void getFindData(int pageNo, final IFindFragment view) {


        model.getFindData(pageNo, new ResponseListener<FindBean>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                view.showData(true, null);
            }

            @Override
            public void onResponse(FindBean response, boolean result, @Nullable String exception, @Nullable String msg) {
                view.showData(false, response);
            }
        });
    }

    //获取发现
    public void getFindMoreData(int pageNo, final IFindFragment view) {


        model.getFindData(pageNo, new ResponseListener<FindBean>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                view.showMoredata(true, null);
            }

            @Override
            public void onResponse(FindBean response, boolean result, @Nullable String exception, @Nullable String msg) {
                view.showMoredata(false, response);
            }
        });
    }

}
