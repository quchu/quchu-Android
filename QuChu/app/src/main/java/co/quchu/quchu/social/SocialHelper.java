package co.quchu.quchu.social;

import android.app.Activity;
import android.graphics.Bitmap;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.json.JSONObject;

import java.util.Map;

import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.UserInfoHelper;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.utils.ToastManager;

/**
 * 社会化(第三方登录、分享)
 * <p>
 * Created by mwb on 2016/12/21.
 */
public class SocialHelper {

  private static String TAG = "SocialHelper";

  private static String SHARE_CONTENT = "点亮你的生活半径。";
  private static String SHARE_TITLE = "趣处";
  private static String APP_DOWNLOAD_URL = "http://www.quchu.co/shareApp/";

  /**
   * 分享
   */
  public static void share(Activity activity, SHARE_MEDIA platform) {
    if (!isInstall(activity, platform)) {
      return;
    }

    UMImage image = new UMImage(activity, R.drawable.ic_launcher);

    new ShareAction(activity).setPlatform(platform)
        .withMedia(image)
        .withTargetUrl(APP_DOWNLOAD_URL)
        .withTitle(SHARE_TITLE)
        .withText(SHARE_CONTENT)
        .setCallback(new UMShareListener() {
          @Override
          public void onResult(SHARE_MEDIA share_media) {
            ToastManager.getInstance(AppContext.mContext).show("分享成功");
            LogUtils.e(TAG, "onResult: " + share_media);
          }

          @Override
          public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            ToastManager.getInstance(AppContext.mContext).show("分享失败");
            LogUtils.e(TAG, "onError: " + share_media + ", " + throwable.getMessage());
          }

          @Override
          public void onCancel(SHARE_MEDIA share_media) {
            ToastManager.getInstance(AppContext.mContext).show("分享取消");
            LogUtils.e(TAG, "onCancel: " + share_media);
          }
        })
        .share();
  }

  public static void share(Activity activity, SHARE_MEDIA platform, String title, String url, Bitmap bitmap) {
    if (!isInstall(activity, platform)) {
      return;
    }

    UMImage image;

    ShareAction action = new ShareAction(activity);

    if (bitmap == null) {
      image = new UMImage(activity, R.drawable.ic_launcher);

    } else {
      image = new UMImage(activity, bitmap);
    }

    action.setPlatform(platform)
        .withTitle(title)
        .withTargetUrl(url)
        .withMedia(image)
        .withText(SHARE_CONTENT);

    action.setCallback(new UMShareListener() {
      @Override
      public void onResult(SHARE_MEDIA share_media) {
        ToastManager.getInstance(AppContext.mContext).show("分享成功");
        LogUtils.e(TAG, "onResult: " + share_media);
      }

      @Override
      public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        ToastManager.getInstance(AppContext.mContext).show("分享失败");
        LogUtils.e(TAG, "onError: " + share_media + ", " + throwable.getMessage());
      }

      @Override
      public void onCancel(SHARE_MEDIA share_media) {
        ToastManager.getInstance(AppContext.mContext).show("分享取消");
        LogUtils.e(TAG, "onCancel: " + share_media);
      }
    }).share();
  }

  /**
   * 授权并且获取用户信息
   *
   * @param isLogin true - 第三方账号登录; false - 绑定第三方账号
   */
  public static void getPlatformInfo(final Activity activity, final SHARE_MEDIA platform, final boolean isLogin, final UserLoginListener listener) {
    if (!isInstall(activity, platform)) {
      return;
    }

    final int loginType;
    if (platform == SHARE_MEDIA.SINA) {
      loginType = 3;
    } else {
      loginType = 2;
    }

    UMShareAPI.get(activity)
        .getPlatformInfo(activity, platform, new UMAuthListener() {
          @Override
          public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            String accessToken = "";
            String id = "";
            if (share_media == SHARE_MEDIA.SINA) {
              accessToken = map.get("access_token");
              id = map.get("uid");

            } else if (share_media == SHARE_MEDIA.WEIXIN) {
              accessToken = map.get("access_token");
              id = map.get("openid");
            }

            String info = "";
            for (String key : map.keySet()) {
              info = info + key + " : " + map.get(key) + "\n";
            }
            LogUtils.e(TAG, "getPlatformInfo() " + "onComplete: " + "share_media = " + share_media
                + ", accessToken = " + accessToken + ", id = " + id + ", info = " + info);

            if (isLogin) {
              register2Server(activity, loginType, accessToken, id, listener);
            } else {
              listener.loginSuccess(loginType, accessToken, id);
            }
          }

          @Override
          public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            listener.loginFail("");
            if (isLogin) {
              ToastManager.getInstance(AppContext.mContext).show(R.string.login_failure);
            } else {
              ToastManager.getInstance(AppContext.mContext).show("绑定失败");
            }
            LogUtils.e(TAG, "getPlatformInfo() " + "onError: " + "share_media = " + share_media + ", message = " + throwable.getMessage());
          }

          @Override
          public void onCancel(SHARE_MEDIA share_media, int i) {
            if (isLogin) {
              ToastManager.getInstance(AppContext.mContext).show(R.string.login_cancel);
            } else {
              ToastManager.getInstance(AppContext.mContext).show("绑定取消");
            }
            LogUtils.e(TAG, "getPlatformInfo() " + "onCancel: " + "share_media = " + share_media);
          }
        });
  }

  /**
   * 微博、微信登录
   */
  private static void register2Server(Activity activity, final int loginType, final String token, final String id, final UserLoginListener listener) {
    String url;
    if (loginType == 3) {
      url = NetApi.WeiboLogin;
    } else {
      url = NetApi.WechatLogin;
    }

    NetService.get(activity, String.format(url, token, id, StringUtils.getMyUUID()), new IRequestListener() {
      @Override
      public void onSuccess(JSONObject response) {
        UserInfoHelper.saveUserInfo(response);
        if (loginType == 3) {
          SPUtils.putLoginType(SPUtils.LOGIN_TYPE_WEIBO);
        } else {
          SPUtils.putLoginType(SPUtils.LOGIN_TYPE_WEIXIN);
        }

        listener.loginSuccess(loginType, token, id);
        ToastManager.getInstance(AppContext.mContext).show(R.string.login_success);
        LogUtils.e(TAG, "注册成功" + ", loginType = " + loginType);
      }

      @Override
      public boolean onError(String error) {
        ToastManager.getInstance(AppContext.mContext).show(R.string.login_failure);
        listener.loginFail("");
        LogUtils.e(TAG, "注册失败" + ", loginType = " + loginType);
        return false;
      }
    });
  }

  public static boolean isInstall(Activity activity, SHARE_MEDIA platform) {
    boolean isInstall = UMShareAPI.get(activity).isInstall(activity, platform);
    if (!isInstall) {
      switch (platform) {
        case QQ:
          ToastManager.getInstance(AppContext.mContext).show("请先安装QQ客户端");
          return false;

        case WEIXIN:
        case WEIXIN_CIRCLE:
          ToastManager.getInstance(AppContext.mContext).show("请先安装微信客户端");
          return false;
      }
    }

    return true;
  }
}
