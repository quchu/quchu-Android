package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.quchu.quchu.model.MessageModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.StringUtils;

/**
 * MessageCenterPresenter
 * User: Chenhs
 * Date: 2016-01-11
 */
public class MessageCenterPresenter {

    public static void getMessageList(Context mContext, final MessageGetDataListener listener) {
//        NetService.get(mContext, NetApi.getMessageList, new IRequestListener() {
//            @Override
//            public void onSuccess(JSONObject response) {
//                LogUtils.json("Message==" + response);
//                try {
//                    if (response.has("result") && !"null".equals(response.getString("result")) && !StringUtils.isEmpty(response.getString("result")) && response.getJSONArray("result").length() > 0) {
//                        JSONArray arrayList = response.getJSONArray("result");
//                        ArrayList<MessageModel> messageList = new ArrayList<MessageModel>();
//                        Gson gson = new Gson();
//                        for (int i = 0; i < arrayList.length(); i++) {
//                            MessageModel model = gson.fromJson(arrayList.getString(i), MessageModel.class);
//                            messageList.add(model);
//                        }
//                        listener.onSuccess(messageList);
//                    } else {
//                        listener.onError();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    listener.onError();
//                }
//            }
//
//            @Override
//            public boolean onError(String error) {
//                listener.onError();
//                return false;
//            }
//        });

        GsonRequest<List<MessageModel>> request = new GsonRequest<>(NetApi.getMessageList, new TypeToken<List<MessageModel>>() {
        }.getType(), new ResponseListener<List<MessageModel>>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                listener.onError();
            }

            @Override
            public void onResponse(List<MessageModel> response, boolean result, @Nullable String exception, @Nullable String msg) {
                listener.onSuccess(response);
            }
        });
        request.start(mContext, null);


    }

    public interface MessageGetDataListener {
        void onSuccess(List<MessageModel> arrayList);

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
