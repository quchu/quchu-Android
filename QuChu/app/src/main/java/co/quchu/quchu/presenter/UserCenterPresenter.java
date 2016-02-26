package co.quchu.quchu.presenter;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONObject;

import co.quchu.quchu.model.UserCenterInfo;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.StringUtils;

/**
 * UserCenterPresenter
 * User: Chenhs
 * Date: 2016-02-24
 */
public class UserCenterPresenter {

    public static void getUserCenterInfo(Context context, int userId, final UserCenterInfoCallBack callBack) {
        NetService.get(context, String.format(NetApi.getUserInfo, userId), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.e("UserInfo=" + response);
                try {
                    Gson gson = new Gson();
                    UserCenterInfo userCenterInfo = gson.fromJson(response.toString(), UserCenterInfo.class);
                    if (response.has("backImg") && !StringUtils.isEmpty(response.getString("backImg")) && !"null".equals(response.getString("backImg"))) {
                        JSONObject jsonObject = response.getJSONObject("backImg");
                        if (jsonObject.has("path") && !StringUtils.isEmpty(jsonObject.getString("path")))
                            userCenterInfo.setBackImg(jsonObject.getString("path"));
                    }
                    callBack.onSuccess(userCenterInfo);

                } catch (Exception e) {
                    LogUtils.e(e.toString());
                    callBack.onError();
                    e.printStackTrace();
                }
            }

            @Override
            public boolean onError(String error) {
                callBack.onError();
                return false;
            }
        });
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
