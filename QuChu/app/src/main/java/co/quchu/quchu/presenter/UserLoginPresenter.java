package co.quchu.quchu.presenter;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.thirdhelp.UserInfoHelper;
import co.quchu.quchu.thirdhelp.UserLoginListener;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.StringUtils;

/**
 * UserLoginPresenter
 * User: Chenhs
 * Date: 2015-11-26
 */
public class UserLoginPresenter {
    public static void decideMobileCanLogin(Context context, String mobileNo, final UserNameUniqueListener listener) {

        NetService.get(context, String.format(NetApi.IsUnique, mobileNo), new IRequestListener() {
            //    NetService.get(context, String.format(NetApi.IsUnique,mobileNo), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String msg = response.getString("msg");
                    if (msg.equals("error")) {
                        LogUtils.json("msg.equals(\"error\")" + "不唯一" + response);
                        listener.notUnique("");
                    } else {
                        LogUtils.json("msg.equals(\"error\")" + "唯一" + response);
                        listener.isUnique("");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean onError(String error) {
                return false;
            }
        });
    }

    public static String getCaptcha_regiest = "regiest";
    public static String getCaptcha_reset = "reset";

    /**
     * 获取验证码
     *
     * @param context  上下文环境
     * @param mobileNo 手机号码
     * @param type     获取用途—— 注册：regiest  重置：reset
     * @param listener 回调
     */
    public static void getCaptcha(Context context, String mobileNo, String type, final UserNameUniqueListener listener) {
        NetService.get(context, String.format(NetApi.GetCaptcha, mobileNo, type), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json(response.toString());
                try {
                    response.getString("result");
                    if (response.has("result") && !StringUtils.isEmpty(response.getString("result"))) {

                        listener.isUnique(response.getString("result"));
                    } else {
                        listener.notUnique("");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public boolean onError(String error) {
                listener.notUnique(error);
                return false;
            }
        });
    }

    public static void userLogin2Server(Context context, String mobileNum, String password, UserNameUniqueListener listener) {

    }

    /**
     * 用户注册
     *
     * @param context  上下文环境
     * @param phoneNo  手机号
     * @param password 密码
     * @param nickName 昵称
     * @param authCode 验证码
     * @param listener 回调
     */
    public static void userRegiest(Context context, String phoneNo, String password, String nickName, String authCode, UserNameUniqueListener listener) {
        NetService.post(context, String.format(NetApi.Regiester, phoneNo, password, authCode, StringUtils.getMyUUID(), nickName), null, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json(response.toString());
            }

            @Override
            public boolean onError(String error) {
                return false;
            }
        });
    }

    /**
     * 用户登录
     *
     * @param context  上下文环境
     * @param phoneNo  手机号
     * @param password 密码
     * @param listener 回调
     */
    public static void userLogin(Context context, String phoneNo, String password, final UserLoginListener listener) {
        NetService.post(context, String.format(NetApi.Mlogin, phoneNo, password, StringUtils.getMyUUID()), null, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json(response.toString());
                UserInfoHelper.saveUserInfo(response);
                listener.loginSuccess();
            }

            @Override
            public boolean onError(String error) {
                return false;
            }
        });
    }

    public static void resetPassword(Context context,String phoneNo,String password,String authCode , final UserNameUniqueListener listener) {
        NetService.post(context, String.format(NetApi.ResertPsw, phoneNo, password, authCode), null, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json(response.toString());
                listener.isUnique(response.toString());

            }

            @Override
            public boolean onError(String error) {
                return false;
            }
        });
    }

    public interface UserNameUniqueListener {
        /**
         * 用户名或昵称可用
         * 成功
         */
        void isUnique(String msg);

        /**
         * 用户名或昵称已经被占用
         * 失败
         *
         * @param msg
         */
        void notUnique(String msg);
    }


}
