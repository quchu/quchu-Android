package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.model.PostCardItemModel;
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

    public void getMyFoiotrintList() {

        Map<String, String> params = new HashMap<>();
        params.put("userId", AppContext.user.getUserId() + "");
        params.put("pageno", "1");
        Type type = new TypeToken<List<PostCardItemModel>>() {
        }.getType();
        GsonRequest<PostCardModel> request = new GsonRequest<>(NetApi.getUserCardList, PostCardModel.class, params, new ResponseListener<PostCardModel>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {

                view.initData(true, null);
            }

            @Override
            public void onResponse(PostCardModel response, boolean result, @Nullable String exception, @Nullable String msg) {
                view.initData(false, response.getResult());
            }
        });
        request.start(context, null);
    }

}
