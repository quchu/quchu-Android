package co.quchu.quchu.thirdhelp;

import android.app.Activity;
import android.os.Bundle;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import org.json.JSONObject;

import java.text.SimpleDateFormat;

import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.StringUtils;

/**
 * WeiboHelper
 * User: Chenhs
 * Date: 2015-11-30
 */
public class WeiboHelper {
    /**
     * WeiboSDKDemo 程序的 APP_SECRET。
     * 请注意：请务必妥善保管好自己的 APP_SECRET，不要直接暴露在程序中，此处仅作为一个DEMO来演示。
     */
    private static final String WEIBO_DEMO_APP_SECRET = "4e47e691a516afad0fc490e05ff70ee5";
    /**
     * 通过 code 获取 Token 的 URL
     */
    private static final String OAUTH2_ACCESS_TOKEN_URL = "https://open.weibo.cn/oauth2/access_token";
    /**
     * 获取 Token 成功或失败的消息
     */
    private static final int MSG_FETCH_TOKEN_SUCCESS = 1;
    private static final int MSG_FETCH_TOKEN_FAILED = 2;
    /**
     * 微博 Web 授权接口类，提供登陆等功能
     */
    private WeiboAuth mWeiboAuth;
    /**
     * 获取到的 Code
     */
    private String mCode;
    /**
     * 获取到的 Token
     */
    private Oauth2AccessToken mAccessToken;
    private static final String APP_KEY = "1884585494";
    private static final String REDIRECT_URL = "http://www.paimeilv.com";
    private static final String USER_URL = "https://api.weibo.com/2/users/show.json";
    private static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    public static SsoHandler mSsoHandler;
    private Activity activity;
    private UserLoginListener listener;

    public WeiboHelper(Activity context, UserLoginListener listener) {
        this.activity = context;
        mWeiboAuth = new WeiboAuth(activity, APP_KEY, REDIRECT_URL, SCOPE);
        this.listener = listener;
    }


    public void weiboLogin(Activity context) {
        this.activity = context;
        mWeiboAuth = new WeiboAuth(activity, APP_KEY, REDIRECT_URL, SCOPE);
        mSsoHandler = new SsoHandler(activity, mWeiboAuth);
        mSsoHandler.authorize(new AuthListener());
    }

    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
            if (accessToken != null && accessToken.isSessionValid()) {
                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                        new java.util.Date(accessToken.getExpiresTime()));
                AccessTokenKeeper.writeAccessToken(activity, accessToken);
            }

            LogUtils.json(values.toString());
            if (null == values) {
                LogUtils.json("values is empty");
                return;
            }

            String _weibo_transaction = values.getString("_weibo_transaction");
            String access_token = values.getString("access_token");
            String uid = values.getString("uid");
          /*  if (TextUtils.isEmpty(_weibo_transaction) ||) {
                LogUtils.json("code is empty");
                return;
            }*/
            LogUtils.json("uid==" + uid);
            regiest2Server(access_token, uid);
            LogUtils.json("access_token==" + access_token);
            mCode = _weibo_transaction;
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onWeiboException(WeiboException e) {
        }
    }


    private void regiest2Server(String token, String uid) {
        NetService.get(activity, String.format(NetApi.WeiboLogin, token, uid, StringUtils.getMyUUID()), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                UserInfoHelper.saveUserInfo(response);
                listener.loginSuccess();

                LogUtils.json("skdf" + response.toString());
            }

            @Override
            public boolean onError(String error) {
                return false;
            }
        });
    }
}
