package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.model.PostCardItemModel;
import co.quchu.quchu.model.PostCardModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;

/**
 * Created by no21 on 2016/4/12.
 * email:437943145@qq.com
 * desc :脚印相关
 */
public class MyFootprintPresenter {
    private Context context;

    public MyFootprintPresenter(Context context) {
        this.context = context;
    }

    public void getMyFoiotrintList(int userId, final int pageNo, final PageLoadListener<PostCardModel> view) {

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

    public void getFootprintDetail(long footprintId, final ResponseListener<PostCardItemModel> listener) {
        Map<String, String> params = new HashMap<>();
        params.put("cardId", String.valueOf(footprintId));

        GsonRequest<PostCardItemModel> request = new GsonRequest<>(NetApi.footpritDetail, PostCardItemModel.class, params, listener);
        request.start(context, null);


    }

}
