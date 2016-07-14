package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Locale;

import co.quchu.quchu.model.UserCenterInfo;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.net.ResponseListener;

/**
 * UserCenterPresenter
 * User: Chenhs
 * Date: 2016-02-24
 */
public class UserCenterPresenter {

    public static void getUserCenterInfo(Context context, int userId, final UserCenterInfoCallBack callBack) {

        GsonRequest<UserCenterInfo> request = new GsonRequest<>(String.format(Locale.SIMPLIFIED_CHINESE, NetApi.getUserInfo, userId), UserCenterInfo.class, new ResponseListener<UserCenterInfo>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                callBack.onError();
            }

            @Override
            public void onResponse(UserCenterInfo response, boolean result, String errorCode, @Nullable String msg) {
                callBack.onSuccess(response);
            }
        });
        request.start(context);
//
//        NetService.get(context, String.format(NetApi.getUserInfo, userId), new IRequestListener() {
//            @Override
//            public void onSuccess(JSONObject response) {
//                LogUtils.e("UserInfo=" + response);
//                try {
//                    Gson gson = new Gson();
//                    UserCenterInfo userCenterInfo = gson.fromJson(response.toString(), UserCenterInfo.class);
//                    if (response.has("backImg") && !StringUtils.isEmpty(response.getString("backImg")) && !"null".equals(response.getString("backImg"))) {
//                        JSONObject jsonObject = response.getJSONObject("backImg");
//                        if (jsonObject.has("path") && !StringUtils.isEmpty(jsonObject.getString("path")))
//                            userCenterInfo.setBackImg(jsonObject.getString("path"));
//                    }
//
//
//                } catch (Exception e) {
//                    LogUtils.e(e.toString());
//                    callBack.onError();
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public boolean onError(String error) {
//
//                return false;
//            }
//        });
    }

    public interface UserCenterInfoCallBack {
        void onSuccess(UserCenterInfo userCenterInfo);

        void onError();
    }

    public static void followSbd(Context context, boolean isFollowed, int userId, final UserCenterInfoCallBack callBack) {
        String url = "";
        if (isFollowed) {
            url = String.format(NetApi.delFollowFriends, userId);
        } else {
            url = String.format(NetApi.followFriends, userId);
        }
        NetService.get(context, url, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                callBack.onSuccess(null);
            }

            @Override
            public boolean onError(String error) {
                callBack.onError();
                return false;
            }
        });
    }
}
