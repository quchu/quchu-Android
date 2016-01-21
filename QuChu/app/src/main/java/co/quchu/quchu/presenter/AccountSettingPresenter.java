package co.quchu.quchu.presenter;

import android.content.Context;
import android.graphics.Bitmap;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONException;
import org.json.JSONObject;

import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.ImageUtils;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.StringUtils;

/**
 * AccountSettingPresenter
 * User: Chenhs
 * Date: 2016-01-21
 * 账户设置
 */
public class AccountSettingPresenter {

    public static void getQiNiuToken(Context mContext, final String filePath, final UploadUserPhotoListener listener) {
        NetService.get(mContext, NetApi.getQiniuToken, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json("qiniu token==" + response);

                if (response != null && response.has("token")) {
                    try {
                        String qiniuToken = response.getString("token");
                        UploadManager uploadManager = new UploadManager();
                        addImage2QiNiu(filePath, qiniuToken, uploadManager, listener);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public boolean onError(String error) {
                return false;
            }
        });
    }

    private static Bitmap uploadBitmap = null;

    private static void addImage2QiNiu(String filePath, String qiniuToken, UploadManager uploadManager, final UploadUserPhotoListener listener) {
        String defaulQiNiuFileName = "%d-%d.JPEG";
        uploadBitmap = ImageUtils.getimage(filePath);
        if (uploadBitmap != null)
            uploadManager.put(ImageUtils.Bitmap2Bytes(uploadBitmap, 90), String.format(defaulQiNiuFileName, AppContext.user.getUserId(), System.currentTimeMillis()), qiniuToken,
                    new UpCompletionHandler() {

                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
                            if (null != info) {
                                try {
                                    if (info.isOK()) {
                                        uploadBitmap.recycle();
                                        uploadBitmap = null;
                                        String url = response.getString("key");
                                        listener.onSuccess(url);
                                    } else {
                                        listener.onError();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    listener.onError();
                                }
                            }
                        }
                    }, new UploadOptions(null, null, false,
                            new UpProgressHandler() {
                                public void progress(String key, double percent) {
                                }
                            }, null));
    }

    public interface UploadUserPhotoListener {
        void onSuccess(String photoUrl);

        void onError();
    }

    /**
     * 保存用户信息
     *
     * @param mContext
     * @param userName
     * @param userPhoto
     * @param userGender
     * @param userLocation
     * @param userPw
     * @param userRePw
     * @param listener
     */
    public static void postUserInfo2Server(Context mContext, String userName, String userPhoto, String userGender, String userLocation, String userPw, String userRePw, final UploadUserPhotoListener listener) {
        String netUrl = NetApi.updateUser + "?user.name=" + userName + "&user.gander=" + ("男".equals(userGender) ? "M" : "W");
        if (!StringUtils.isEmpty(userPhoto))
            netUrl += "&user.photo=" + userPhoto;
        if (!StringUtils.isEmpty(userLocation))
            netUrl += "&user.location=" + userLocation;
        if (!StringUtils.isEmpty(userPw))
            netUrl += "&user.password=" + userPw + "&user.restpsw=" + userRePw;
        NetService.post(mContext, netUrl, null, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                listener.onSuccess("");
            }

            @Override
            public boolean onError(String error) {
                listener.onError();
                return false;
            }
        });
    }

}
