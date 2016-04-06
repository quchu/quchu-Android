package co.quchu.quchu.thirdhelp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sina.weibo.sdk.utils.Utility;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import co.quchu.quchu.R;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.AppUtil;
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

    private static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    public static SsoHandler mSsoHandler;
    private Activity activity;
    private UserLoginListener listener;
    AuthInfo mAuthInfo;

    public WeiboHelper(Activity context, UserLoginListener listener) {
        this.activity = context;
        mAuthInfo = new AuthInfo(activity, APP_KEY, REDIRECT_URL, SCOPE);
        this.listener = listener;

    }


    public void weiboLogin(Activity context) {
        this.activity = context;
        mAuthInfo = new AuthInfo(activity, APP_KEY, REDIRECT_URL, SCOPE);
        mSsoHandler = new SsoHandler(activity, mAuthInfo);
        mSsoHandler.authorize(new AuthListener());
    }

    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
            if (accessToken != null && accessToken.isSessionValid()) {
                AccessTokenKeeper.writeAccessToken(activity, accessToken);
            }

            LogUtils.json(values.toString());

            String _weibo_transaction = values.getString("_weibo_transaction");
            String access_token = values.getString("access_token");
            String uid = values.getString("uid");

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


    private void regiest2Server(final String token, final String uid) {
        NetService.get(activity, String.format(NetApi.WeiboLogin, token, uid, StringUtils.getMyUUID()), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                UserInfoHelper.saveUserInfo(response);
                listener.loginSuccess(3,token,uid);

                LogUtils.json("skdf" + response.toString());


            }

            @Override
            public boolean onError(String error) {
                return false;
            }
        });
    }

    public static void share2Weibo(final Activity activity, String shareUrl, String shareTitle) {
        if (!AppUtil.isAppInstall("com.sina.weibo")) {
            Toast.makeText(activity, "请检查是否已安装微博客户端!", Toast.LENGTH_SHORT).show();
            return;
        }
        IWeiboShareAPI mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(activity, APP_KEY);
        mWeiboShareAPI.registerApp();
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();//初始化微博的分享消息
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = shareTitle;
        mediaObject.description = "←点我\n (*^O^*)";
        // mediaObject.description = "←点我\n &#040;&#042;&#094;O&#094;&#042;&#041;";
        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher);
        // 设置 Bitmap 类型的图片到视频对象里  设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。


        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, os);
            System.out.println("kkkkkkk    size  " + os.toByteArray().length);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("Weibo.BaseMediaObject", "put thumb failed");
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = shareUrl;
        mediaObject.defaultText = shareTitle;
        weiboMessage.mediaObject = mediaObject;

        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        mWeiboShareAPI.sendRequest(activity, request);


    }

}
