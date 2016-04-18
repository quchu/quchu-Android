package co.quchu.quchu.thirdhelp;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;

/**
 * UserInfoHelper
 * User: Chenhs
 * Date: 2015-11-30
 */
public class UserInfoHelper {

    public static void saveUserInfo(JSONObject userInfo) {
        if (userInfo != null) {
            if (userInfo.has("token")) {
                try {
                    SPUtils.clearUserinfo(AppContext.mContext);
                    SPUtils.setUserToken(AppContext.mContext, userInfo.getString("token"));
                    AppContext.token = userInfo.getString("token");
                    SPUtils.setUserInfo(AppContext.mContext, userInfo.toString());
                    AppContext.user = new Gson().fromJson(userInfo.toString(), UserInfoModel.class);
                    //    AppContext.gatherDataModel=new GatherDataModel();
                    LogUtils.json("user info save success ");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void saveUserInfo(UserInfoModel userInfo) {
        if (userInfo != null) {
            SPUtils.clearUserinfo(AppContext.mContext);
            String json = new Gson().toJson(userInfo);
            SPUtils.setUserInfo(AppContext.mContext, json);
            AppContext.user = userInfo;

        }
    }
}
