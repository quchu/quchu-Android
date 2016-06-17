package co.quchu.quchu.thirdhelp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;

/**
 * WechatHelper
 * User: Chenhs
 * Date: 2015-11-30
 */
public class WechatHelper {
    private static IWXAPI api;
    private static Context mActivity;
    public static final String WECHAT_APP_ID = "wx812a0a8cd108d233";
    private static final String APPSECRET = "b38180312951c88c3c24a5223e53daac";
    private static WechatHelper instance;

    private WechatHelper() {
    }

    /**
     * 他内部重新创建了实例  没静态不行啊  坑爹的
     */
    private UserLoginListener listener;


    public static WechatHelper getInstance(Context context) {
        mActivity = context.getApplicationContext();

        if (instance == null) {
            instance = new WechatHelper();
            api = WXAPIFactory.createWXAPI(mActivity, WECHAT_APP_ID,
                    false);
            api.registerApp(WECHAT_APP_ID);
        }
        return instance;

    }

    public IWXAPI getApi() {
        return api;
    }

    public void login(UserLoginListener listener) {
        isBind = false;
        this.listener = listener;
        if (!api.isWXAppInstalled()) {
            Toast.makeText(mActivity, "您还未安装微信", Toast.LENGTH_SHORT).show();
            return;
        }
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "quchu";
        api.sendReq(req);
    }

    private boolean isBind = false;

    public void bind(UserLoginListener listener) {
        isBind = true;
        this.listener = listener;
        if (!api.isWXAppInstalled()) {
            Toast.makeText(mActivity, "您还未安装微信", Toast.LENGTH_SHORT).show();
            return;
        }
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "quchu";
        api.sendReq(req);
    }


    public void actionFromWX(SendAuth.Resp resp) {

        int errCode = resp.errCode;
        switch (errCode) {
            case 0:// 同意授权
                LogUtils.json("同意授权openId=" + resp.openId + "///state=" + resp.state + "//code==" + resp.code + ";lang;" + resp.lang + "//country" + resp.country);
                getWechatToken(resp.code);
                break;
            case -2:// 取消授权
                Toast.makeText(AppContext.mContext, "取消授权", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private static final String wechatGetToken = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    private void getWechatToken(String codes) {
        NetService.get(mActivity, String.format(wechatGetToken, WECHAT_APP_ID, APPSECRET, codes), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json(response.toString());
                try {
                    String access_token = response.getString("access_token");
                    String openid = response.getString("openid");
                    if (!StringUtils.isEmpty(access_token) && !StringUtils.isEmpty(openid)) {
                        LogUtils.json("access_token != null");
                        if (isBind) {
                            listener.loginSuccess(2, access_token, openid);
                        } else {
                            LogUtils.json("注册");
                            regiestWechat2Server(access_token, openid);
                        }
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

    private void regiestWechat2Server(final String token, final String appId) {
        NetService.get(mActivity, String.format(NetApi.WechatLogin, token, appId, StringUtils.getMyUUID()), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json(response.toString());
                UserInfoHelper.saveUserInfo(response);
                if (null != listener) {
                    SPUtils.putLoginType(SPUtils.LOGIN_TYPE_WEIXIN);
                    MobclickAgent.onProfileSignIn("loginwechat_c", AppContext.user.getUserId() + "");
                    listener.loginSuccess(2, token, appId);
                }

            }

            @Override
            public boolean onError(String error) {
                return false;
            }
        });
    }

    public static void shareFriends(Activity mActivity, String shareUrl, String title,
                                    boolean isShare4Friends) {
        IWXAPI api = WXAPIFactory.createWXAPI(mActivity, WECHAT_APP_ID,
                false);
        api.registerApp(WECHAT_APP_ID);
        if (!api.isWXAppInstalled()) {
            Toast.makeText(mActivity, "您还未安装微信", Toast.LENGTH_SHORT).show();
            return;
        }

        WXMediaMessage msg;

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = shareUrl;
        msg = new WXMediaMessage(webpage);

        msg.title = title;
        msg.description = "一千个人，就有一千个趣处";

        msg.thumbData = bmpToByteArray(BitmapFactory.decodeResource(mActivity.getResources(),
                R.mipmap.ic_launcher), true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        if (isShare4Friends) {
            req.scene = SendMessageToWX.Req.WXSceneSession;
        } else {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        }
        api.sendReq(req);

    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


}
