package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;

import co.quchu.quchu.model.QuchuBean;
import co.quchu.quchu.model.QuchuModel;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.view.activity.QuchuActivity;

/**
 * Created by no21 on 2016/4/5.
 * email:437943145@qq.com
 * desc : 获取发现和收藏
 */
public class QuchuPresenter {

    private Context context;
    private QuchuActivity view;
    private QuchuModel model;

    public QuchuPresenter(Context context, QuchuActivity view) {
        this.context = context;
        this.view = view;
        model = new QuchuModel(context);
    }

    //获取收藏
    public void getFavoriteData(int pageNo) {
        model.getFavoriteData(pageNo, new ResponseListener<QuchuBean>() {

            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                view.showFavorite(true, null);
            }

            @Override
            public void onResponse(QuchuBean response, boolean result, @Nullable String exception, @Nullable String msg) {
                view.showFavorite(false, response);
            }
        });
    }

    //获取发现
    public void getFindData(int pageNo) {

    }


}
