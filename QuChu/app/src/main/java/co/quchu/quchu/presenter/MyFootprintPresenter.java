package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.model.PostCardModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;

/**
 * Created by no21 on 2016/4/12.
 * email:437943145@qq.com
 * desc :
 */
public class MyFootprintPresenter {
    private Context context;
    private PageLoadListener<PostCardModel> view;

    public MyFootprintPresenter(Context context, PageLoadListener<PostCardModel> view) {
        this.context = context;
        this.view = view;
    }

    public void getMyFoiotrintList(int userId, final int pageNo) {

        Map<String, String> params = new HashMap<>();
        params.put("pageno", String.valueOf(pageNo));
        params.put("userId", String.valueOf(userId));

        String uri = userId == AppContext.user.getUserId() ? NetApi.GetCardList : NetApi.getUserCardList;
        GsonRequest<PostCardModel> request = new GsonRequest<>(uri, PostCardModel.class, params, new ResponseListener<PostCardModel>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {

                view.netError(pageNo, null);
            }

            @Override
            public void onResponse(PostCardModel response, boolean result, @Nullable String exception, @Nullable String msg) {
                if (response == null) {
                    view.nullData();
                } else if (pageNo == 1) {
                    view.initData(response);
                } else {
                    view.moreData(response);
                }


            }
        });
        request.start(context, null);
    }
}
