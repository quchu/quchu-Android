package co.quchu.quchu.model;

import android.content.Context;

import java.util.Locale;

import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;

/**
 * Created by no21 on 2016/4/5.
 * email:437943145@qq.com
 * desc :
 */
public class QuchuModel {
    private Context context;

    public QuchuModel(Context context) {
        this.context = context;
    }

    //获取收藏
    public void getFavoriteData(int pageNo, ResponseListener<FavoriteBean> listener) {
        String uri = String.format(Locale.CHINA, NetApi.getUsercenterFavoriteList, AppContext.user.getUserId(), pageNo);

        GsonRequest<FavoriteBean> request = new GsonRequest<>(uri, FavoriteBean.class, listener);
        request.start(context, null);
    }

    //获取发现
    public void getFindData(int pageNo, ResponseListener<FindBean> listener) {
        String uri = String.format(Locale.CHINA,NetApi.getProposalPlaceList, pageNo);
        GsonRequest<FindBean> request = new GsonRequest<>(uri, FindBean.class, listener);
        request.start(context, null);
    }


}
