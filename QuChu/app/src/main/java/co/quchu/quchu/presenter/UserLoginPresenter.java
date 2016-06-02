package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.sina.weibo.sdk.utils.MD5;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.net.ResponseListener;
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
                        listener.isUnique(null);
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

                        listener.isUnique(response.getJSONObject("result"));
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

    /**
     * 获取验证码
     *
     * @param context  上下文环境
     * @param mobileNo 手机号码
     * @param listener 回调
     */
    public static void requestVerifySms(Context context, String mobileNo,String type, final UserNameUniqueListener listener) {
        NetService.get(context, String.format(NetApi.GetCaptcha, mobileNo, type), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json(response.toString());
                try {
                    if (response.has("result")&&response.getString("result").equals("0")){
                        listener.isUnique(null);
                    }else{
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

    /**
     * 获取验证码
     *
     * @param context  上下文环境
     * @param mobileNo 手机号码
     * @param type     获取用途—— 注册：regiest  重置：reset
     * @param listener 回调
     */
    public static void getCaptcha(Context context, String mobileNo, String type, final ResponseListener<String> listener) {
        new GsonRequest<>(String.format(NetApi.GetCaptcha, mobileNo, type), null, listener).start(context);
    }

    /**
     * 游客转正式用户
     *
     * @param context  上下文环境
     * @param phoneNo  手机号
     * @param password 密码
     * @param nickName 昵称
     * @param id       原先用户的id
     * @param authCode 验证码
     * @param listener 回调
     */
    public static void userRegiest(final Context context, int id, String phoneNo, String password, String nickName, String authCode, final UserNameUniqueListener listener) {
//        String regiestUrl = String.format(NetApi.Regiester, phoneNo, password, authCode, StringUtils.getMyUUID(), nickName);
//        if (AppContext.user != null) {
//            regiestUrl += "&userId=" + AppContext.user.getUserId();
//        }
//        NetService.post(context, regiestUrl, null, new IRequestListener() {
//            @Override
//            public void onSuccess(JSONObject response) {
//                LogUtils.json(response.toString());
//                listener.isUnique(response);
//            }
//
//            @Override
//            public boolean onError(String error) {
//                return false;
//            }
//        });

        Map<String, String> params = new HashMap<>();
        params.put("username", phoneNo);
        params.put("password", MD5.hexdigest(password));
        params.put("captcha", authCode);
        params.put("regType", "tel");
        params.put("equip", StringUtils.getMyUUID());
        params.put("userId", String.valueOf(id));
        params.put("fullname", nickName);

        GsonRequest<UserInfoModel> request = new GsonRequest<>(NetApi.register, UserInfoModel.class, params, new ResponseListener<UserInfoModel>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(UserInfoModel response, boolean result, String errorCode, @Nullable String msg) {
                if (result) {
                    UserInfoHelper.saveUserInfo(response);
                    LogUtils.json(response.toString());
                    listener.isUnique(null);
                } else {
                    Toast.makeText(context, "验证码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        request.start(context, null);

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
        NetService.post(context, String.format(NetApi.Mlogin, phoneNo, MD5.hexdigest(password), StringUtils.getMyUUID()), null, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                UserInfoHelper.saveUserInfo(response);
                listener.loginSuccess(1, null, null);
            }

            @Override
            public boolean onError(String error) {
                listener.loginFail(error);
                return false;
            }
        });
    }

    public static void resetPassword(Context context, String phoneNo, String password, String authCode, final UserNameUniqueListener listener) {
        NetService.post(context, String.format(NetApi.ResertPsw, phoneNo, MD5.hexdigest(password), authCode), null, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json(response.toString());
                listener.isUnique(response);

            }

            @Override
            public boolean onError(String error) {
                return false;
            }
        });
    }


    /**
     * 验证验证码
     * @param context
     * @param phoneNo
     * @param code
     * @param listener
     */
    public static void verifyNext(Context context,String phoneNo,String code,final CommonListener listener){

        Map<String, String> params = new HashMap<>();
        params.put("phoneNumber", phoneNo);
        params.put("verifyCode", code);
        GsonRequest<String> request = new GsonRequest<>(NetApi.autoCodeIsCorrect, String.class, params, new ResponseListener<String>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                listener.errorListener(error,"","");
            }

            @Override
            public void onResponse(String response, boolean result, String errorCode, @Nullable String msg) {
                if (result) {
                    listener.successListener(null);
                } else {
                    listener.errorListener(null,"","");
                }
            }
        });
        request.start(context, null);
    }




    public interface UserNameUniqueListener {
        /**
         * 用户名或昵称可用
         * 成功
         */
        void isUnique(JSONObject msg);

        /**
         * 用户名或昵称已经被占用
         * 失败
         *
         * @param msg
         */
        void notUnique(String msg);
    }

    /**
     * 游客注册
     */
    public static void visitorRegiest(Context context, final UserNameUniqueListener listener) {
        NetService.post(context, String.format(NetApi.visitorRegiester, StringUtils.getMyUUID()), null, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json("visitorRegiest=" + response.toString());
                UserInfoHelper.saveUserInfo(response);
                if (null != listener)
                    listener.isUnique(response);
            }

            @Override
            public boolean onError(String error) {
                return false;
            }
        });
    }

}
