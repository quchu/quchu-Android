package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import co.quchu.quchu.model.PostCardModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.view.activity.IFootprintActivity;

/**
 * Created by no21 on 2016/4/12.
 * email:437943145@qq.com
 * desc :
 */
public class MyFootprintPresenter {
    private Context context;
    private IFootprintActivity view;

    public MyFootprintPresenter(Context context, IFootprintActivity view) {
        this.context = context;
        this.view = view;
    }

    public void getMyFoiotrintList(int userId) {

        Map<String, String> params = new HashMap<>();
        params.put("userId", String.valueOf(userId));
        params.put("pageno", "1");

        GsonRequest<PostCardModel> request = new GsonRequest<>(NetApi.getUserCardList, PostCardModel.class, params, new ResponseListener<PostCardModel>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {

                view.initData(true, null);
            }

            @Override
            public void onResponse(PostCardModel response, boolean result, @Nullable String exception, @Nullable String msg) {
                view.initData(false, response);
            }
        });
        request.start(context, null);
    }

    public void getMoreMyFoiotrintList(int userId, int pageNo) {

        Map<String, String> params = new HashMap<>();
        params.put("userId", String.valueOf(userId));
        params.put("pageno", String.valueOf(pageNo));

        GsonRequest<PostCardModel> request = new GsonRequest<>(NetApi.getUserCardList, PostCardModel.class, params, new ResponseListener<PostCardModel>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {

                view.initData(true, null);
            }

            @Override
            public void onResponse(PostCardModel response, boolean result, @Nullable String exception, @Nullable String msg) {
                view.initData(false, response);
            }
        });
        request.start(context, null);
    }
}
