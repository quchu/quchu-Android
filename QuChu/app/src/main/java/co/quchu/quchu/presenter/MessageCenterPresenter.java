package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import co.quchu.quchu.model.MessageModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.view.PageLoadListener;

/**
 * MessageCenterPresenter
 * User: Chenhs
 * Date: 2016-01-11
 */
public class MessageCenterPresenter {

    public static void getMessageList(Context mContext, final int pageNo, final PageLoadListener<MessageModel> listener) {


        GsonRequest<MessageModel> request = new GsonRequest<>(NetApi.getMessageList, MessageModel.class, new ResponseListener<MessageModel>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                listener.netError(pageNo, "");
            }

            @Override
            public void onResponse(MessageModel response, boolean result, @Nullable String exception, @Nullable String msg) {
                if (result) {
                    listener.nullData();
                } else if (pageNo == 1) {
                    listener.initData(response);
                } else {
                    listener.moreData(response);
                }
            }
        });
        request.start(mContext, null);


    }



    public interface MessageGetDataListener {
        void onSuccess(MessageModel arrayList);

        void onError();
    }

    public static void followMessageCenterFriends(Context mContext, int friendsId, boolean isFollowing, final MessageGetDataListener listener) {
        String urlStr = "";
        if (isFollowing) {
            urlStr = String.format(NetApi.delFollowFriends, friendsId);
        } else {
            urlStr = String.format(NetApi.followFriends, friendsId);
        }
        NetService.get(mContext, urlStr, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json("message follow==" + response);
                listener.onSuccess(null);
            }

            @Override
            public boolean onError(String error) {
                listener.onError();
                return false;
            }
        });
    }
}
