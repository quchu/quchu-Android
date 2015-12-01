package co.quchu.quchu.thirdhelp;

import android.app.Activity;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.StringUtils;

/**
 * WechatHelper
 * User: Chenhs
 * Date: 2015-11-30
 */
public class WechatHelper {
    private IWXAPI api;
    private Activity mActivity;
    public static final String WECHAT_APP_ID = "wx812a0a8cd108d233";
    private static final String APPSECRET = "b38180312951c88c3c24a5223e53daac";

    public Activity getmActivity() {
        return mActivity;
    }

    private static UserLoginListener listener;

    public WechatHelper(Activity activity, UserLoginListener listener) {
        mActivity = activity;
        api = WXAPIFactory.createWXAPI(mActivity, WECHAT_APP_ID,
                false);
        api.registerApp(WECHAT_APP_ID);
        this.listener = listener;
    }
    public WechatHelper(Activity activity) {
        mActivity = activity;
        api = WXAPIFactory.createWXAPI(mActivity, WECHAT_APP_ID,
                false);
        api.registerApp(WECHAT_APP_ID);
    }
    public IWXAPI getApi() {
        return api;
    }

    public void login() {
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
                LogUtils.json("openId=" + resp.openId + "///state=" + resp.state + "//code==" + resp.code + ";lang;" + resp.lang + "//country" + resp.country);
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
                        regiestWechat2Server(access_token, openid);
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

    private void regiestWechat2Server(String token, String appId) {
        NetService.get(mActivity, String.format(NetApi.WechatLogin, token, appId, StringUtils.getMyUUID()), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json(response.toString());
                UserInfoHelper.saveUserInfo(response);
                if (null != listener)
                    listener.loginSuccess();
            }

            @Override
            public boolean onError(String error) {
                return false;
            }
        });
    }





    /* public void share2WeChat(String id,
                             String description, Bitmap bitmap) {
        share2WeChat(id, description, bitmap, false);
    }


    public void shareFriends(String id,
                             String description, Bitmap bitmap, boolean isShare4Friends) {
        if (!api.isWXAppInstalled()) {
            Toast.makeText(mActivity, "您还未安装微信", Toast.LENGTH_SHORT).show();
            return;
        }
        WXMediaMessage msg;
        if (StringUtils.isEmpty(id)) {
            WXImageObject imageObject = new WXImageObject();
            imageObject.imageData = ImageUtils.Bitmap2Bytes(bitmap, 90);
            msg = new WXMediaMessage(imageObject);
        } else {
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = String.format(NetApi.MY_TOPIC_SHARE, id);
            msg = new WXMediaMessage(webpage);
            if (isShare4Friends) {
                msg.description = "←戳\n" +
                        "(*^◎^*)";
            }
            msg.title = description;
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(mActivity.getResources(),
                        R.drawable.ic_share_logo);
//                msg.thumbData = Util.bmpToByteArray(bitmap, true);
            }
//            else{
//                msg.thumbData = Util.bmpToByteArray(bitmap, true);
//            }
            msg.thumbData = Util.bmpToByteArray(bitmap, true);

        }

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


    public void shareFriends(String id,
                             String description, byte[] bytes, boolean isShare4Friends) {
        if (!api.isWXAppInstalled()) {
            Toast.makeText(mActivity, "您还未安装微信", Toast.LENGTH_SHORT).show();
            return;
        }
        WXMediaMessage msg;
        if (StringUtils.isEmpty(id)) {
            WXImageObject imageObject = new WXImageObject();
            imageObject.imageData = bytes;
            msg = new WXMediaMessage(imageObject);
        } else {
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = String.format(NetApi.MY_TOPIC_SHARE, id);
            msg = new WXMediaMessage(webpage);
            if (isShare4Friends) {
                msg.description = "←戳\n" +
                        "(*^◎^*)";
            }
            msg.title = description;
            if (bytes == null) {
                bytes = Util.bmpToByteArray(BitmapFactory.decodeResource(mActivity.getResources(),
                        R.drawable.ic_share_logo), true);
            }
            msg.thumbData = bytes;

        }

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
    /* public void share2WeChat(String id,
                             String description, Bitmap bitmap) {
        share2WeChat(id, description, bitmap, false);
    }


    public void shareFriends(String id,
                             String description, Bitmap bitmap, boolean isShare4Friends) {
        if (!api.isWXAppInstalled()) {
            Toast.makeText(mActivity, "您还未安装微信", Toast.LENGTH_SHORT).show();
            return;
        }
        WXMediaMessage msg;
        if (StringUtils.isEmpty(id)) {
            WXImageObject imageObject = new WXImageObject();
            imageObject.imageData = ImageUtils.Bitmap2Bytes(bitmap, 90);
            msg = new WXMediaMessage(imageObject);
        } else {
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = String.format(NetApi.MY_TOPIC_SHARE, id);
            msg = new WXMediaMessage(webpage);
            if (isShare4Friends) {
                msg.description = "←戳\n" +
                        "(*^◎^*)";
            }
            msg.title = description;
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(mActivity.getResources(),
                        R.drawable.ic_share_logo);
//                msg.thumbData = Util.bmpToByteArray(bitmap, true);
            }
//            else{
//                msg.thumbData = Util.bmpToByteArray(bitmap, true);
//            }
            msg.thumbData = Util.bmpToByteArray(bitmap, true);

        }

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


    public void shareFriends(String id,
                             String description, byte[] bytes, boolean isShare4Friends) {
        if (!api.isWXAppInstalled()) {
            Toast.makeText(mActivity, "您还未安装微信", Toast.LENGTH_SHORT).show();
            return;
        }
        WXMediaMessage msg;
        if (StringUtils.isEmpty(id)) {
            WXImageObject imageObject = new WXImageObject();
            imageObject.imageData = bytes;
            msg = new WXMediaMessage(imageObject);
        } else {
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = String.format(NetApi.MY_TOPIC_SHARE, id);
            msg = new WXMediaMessage(webpage);
            if (isShare4Friends) {
                msg.description = "←戳\n" +
                        "(*^◎^*)";
            }
            msg.title = description;
            if (bytes == null) {
                bytes = Util.bmpToByteArray(BitmapFactory.decodeResource(mActivity.getResources(),
                        R.drawable.ic_share_logo), true);
            }
            msg.thumbData = bytes;

        }

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

*/
}
