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
                    SPUtils.setUserToken(AppContext.mContext, userInfo.getString("token"));
                    SPUtils.setUserInfo(AppContext.mContext, userInfo.toString());
                    AppContext.user = new Gson().fromJson(userInfo.toString(), UserInfoModel.class);
                    LogUtils.json("user info save success ");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}