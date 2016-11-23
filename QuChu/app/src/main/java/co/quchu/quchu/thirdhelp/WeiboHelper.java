package co.quchu.quchu.thirdhelp;

import android.app.Activity;
import android.content.Context;
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
import co.quchu.quchu.im.IMPresenter;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.utils.AppUtil;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;

/**
 * WeiboHelper
 * User: Chenhs
 * Date: 2015-11-30
 */
public class WeiboHelper {
    private static final String APP_KEY = "1884585494";
    private static final String REDIRECT_URL = "http://sit.quchu.co";

    private static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
    private Context context;
    private static WeiboHelper helper;
    private AuthInfo mAuthInfo;

    private WeiboHelper(Context context) {
        this.context = context.getApplicationContext();
        mAuthInfo = new AuthInfo(context, APP_KEY, REDIRECT_URL, SCOPE);
    }

    public static WeiboHelper getInstance(Context context) {
        if (helper == null) {
            synchronized (WeiboHelper.class) {
                if (helper == null) {
                    helper = new WeiboHelper(context);
                }
            }
        }
        return helper;
    }

    public AuthInfo getmAuthInfo() {
        return mAuthInfo;
    }

    public void weiboLogin(SsoHandler ssoHandler, final UserLoginListener listener, final boolean isLogin) {
        if (!NetUtil.isNetworkConnected(context)) {
            Toast.makeText(context, (R.string.network_error), Toast.LENGTH_SHORT).show();
            return;
        }

        //退出已经登录的融云账号
        new IMPresenter().logout();

        ssoHandler.authorize(new WeiboAuthListener() {
            @Override
            public void onComplete(Bundle bundle) {
                Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(bundle);
                if (accessToken.isSessionValid()) {
                    AccessTokenKeeper.writeAccessToken(context, accessToken);
                    String access_token = accessToken.getToken();
                    String uid = accessToken.getUid();
                    LogUtils.e("微博授权成功");
                    if (isLogin) {
                        regiest2Server(access_token, uid, listener);
                    } else {
                        listener.loginSuccess(3, access_token, uid);
                    }
                } else {
                    LogUtils.e("微博key错误:" + bundle.getString("code"));
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                LogUtils.e("微博登陆异常");
                e.printStackTrace();
            }

            @Override
            public void onCancel() {
                LogUtils.e("微博登陆取消");

            }
        });
    }


    private void regiest2Server(final String token, final String uid, final UserLoginListener listener) {
        NetService.get(context, String.format(NetApi.WeiboLogin, token, uid, StringUtils.getMyUUID()), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                UserInfoHelper.saveUserInfo(response);
                SPUtils.putLoginType(SPUtils.LOGIN_TYPE_WEIBO);
                listener.loginSuccess(3, token, uid);
                LogUtils.e("微博注册成功");
            }

            @Override
            public boolean onError(String error) {
                LogUtils.e("微博注册失败");
                return false;
            }
        });
    }

    public void share2Weibo(final Activity activity, String shareUrl, String shareTitle, Bitmap bitmap) {
        if (!AppUtil.isAppInstall(activity, "com.sina.weibo")) {
            Toast.makeText(activity, "请检查是否已安装微博客户端!", Toast.LENGTH_SHORT).show();
            return;
        }
        IWeiboShareAPI mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(activity, APP_KEY);
        mWeiboShareAPI.registerApp();
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();//初始化微博的分享消息
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = shareTitle;
        mediaObject.description = "点亮你的生活半径";

        if (bitmap == null || bitmap.isRecycled())
            bitmap = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher);
        // 设置 Bitmap 类型的图片到视频对象里  设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。


        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.WEBP, 85, os);
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
