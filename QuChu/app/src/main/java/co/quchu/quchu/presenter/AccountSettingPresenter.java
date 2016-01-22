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

import java.util.ArrayList;

import co.quchu.quchu.R;
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

    //  private static Bitmap uploadBitmap = null;

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
        String netUrl = NetApi.updateUser + "?user.name=" + userName + "&user.gander=" + ("男".equals(userGender) ? "M" : "W") + "&user.location=" + userLocation;
        if (!StringUtils.isEmpty(userPhoto))
            netUrl += "&user.photo=" + userPhoto;
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


    public static ArrayList<Integer> getQAvatar() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(R.drawable.avatar_1);
        arrayList.add(R.drawable.avatar_2);
        arrayList.add(R.drawable.avatar_3);
        arrayList.add(R.drawable.avatar_4);
        arrayList.add(R.drawable.avatar_5);
        arrayList.add(R.drawable.avatar_6);
        arrayList.add(R.drawable.avatar_7);
        arrayList.add(R.drawable.avatar_8);
        arrayList.add(R.drawable.avatar_9);
        arrayList.add(R.drawable.avatar_10);
        arrayList.add(R.drawable.avatar_11);
        arrayList.add(R.drawable.avatar_12);
        arrayList.add(R.drawable.avatar_13);
        arrayList.add(R.drawable.avatar_14);
        arrayList.add(R.drawable.avatar_15);
        arrayList.add(R.drawable.avatar_16);
        arrayList.add(R.drawable.avatar_17);
        arrayList.add(R.drawable.avatar_18);
        arrayList.add(R.drawable.avatar_19);
        arrayList.add(R.drawable.avatar_20);
        arrayList.add(R.drawable.avatar_21);
        arrayList.add(R.drawable.avatar_22);
        arrayList.add(R.drawable.avatar_23);
        arrayList.add(R.drawable.avatar_24);
        arrayList.add(R.drawable.avatar_25);
        arrayList.add(R.drawable.avatar_26);
        arrayList.add(R.drawable.avatar_27);
        arrayList.add(R.drawable.avatar_28);
        arrayList.add(R.drawable.avatar_29);
        arrayList.add(R.drawable.avatar_30);
        arrayList.add(R.drawable.avatar_31);
        arrayList.add(R.drawable.avatar_32);
        arrayList.add(R.drawable.avatar_33);
        arrayList.add(R.drawable.avatar_34);
        arrayList.add(R.drawable.avatar_35);
        arrayList.add(R.drawable.avatar_36);
        arrayList.add(R.drawable.avatar_37);
        arrayList.add(R.drawable.avatar_38);
        arrayList.add(R.drawable.avatar_39);
        arrayList.add(R.drawable.avatar_40);
        arrayList.add(R.drawable.avatar_41);
        arrayList.add(R.drawable.avatar_42);
        arrayList.add(R.drawable.avatar_43);
        arrayList.add(R.drawable.avatar_44);
        arrayList.add(R.drawable.avatar_45);
        arrayList.add(R.drawable.avatar_46);
        arrayList.add(R.drawable.avatar_47);
        arrayList.add(R.drawable.avatar_48);
        arrayList.add(R.drawable.avatar_49);
        arrayList.add(R.drawable.avatar_50);
        return arrayList;
    }


    public static void getQiNiuToken(Context mContext, final Bitmap bitmapA, final UploadUserPhotoListener listener) {
        NetService.get(mContext, NetApi.getQiniuToken, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json("qiniu token==" + response);

                if (response != null && response.has("token")) {
                    try {
                        String qiniuToken = response.getString("token");
                        UploadManager uploadManager = new UploadManager();
                        LogUtils.json("qiniu token==" + response);
                        addImage2QiNiu(bitmapA, qiniuToken, uploadManager, listener);
                    } catch (JSONException e) {
                        LogUtils.json("qiniu token==" + e);
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

    private static void addImage2QiNiu(Bitmap bitmapA, String qiniuToken, UploadManager uploadManager, final UploadUserPhotoListener listener) {
        String defaulQiNiuFileName = "%d-%d.JPEG";
        LogUtils.json("qiniu token==" + defaulQiNiuFileName);
        if (bitmapA != null)
            LogUtils.json("qiniu token==bitmapA != null");
        uploadManager.put(ImageUtils.Bitmap2Bytes(bitmapA, 95), String.format(defaulQiNiuFileName, AppContext.user.getUserId(), System.currentTimeMillis()), qiniuToken,
                new UpCompletionHandler() {

                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        if (null != info) {
                            try {
                                if (response.has("key")) {
                                    String url = response.getString("key");
                            LogUtils.json("qiniu token==bitmapA " + response + "!= ResponseInfo===" + url);
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
}
