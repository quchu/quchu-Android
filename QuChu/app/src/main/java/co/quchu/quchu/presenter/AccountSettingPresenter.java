package co.quchu.quchu.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.sina.weibo.sdk.utils.MD5;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.utils.ImageUtils;
import co.quchu.quchu.utils.LogUtils;

/**
 * AccountSettingPresenter
 * User: Chenhs
 * Date: 2016-01-21
 * 账户设置
 * 8336f98e7571483f21b11c13cf603268 正式
 * ab3d26f0aa7f5fe92a6032a8837bbf2f debug
 */
public class AccountSettingPresenter {

    private Context context;
    private static final int AUTH_CODE = 1;

    private TextView authView;
    private MyHandle handle;
    private static final String mAuthDesc = "秒后重新获取";
    private int mAuthCounter;

    public AccountSettingPresenter(Context context) {
        this.context = context;
    }


    class MyHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == AUTH_CODE) {
                authView.setText(--mAuthCounter + mAuthDesc);
                if (mAuthCounter < 1) {
                    authView.setText("再次获取验证码");
                    authView.setEnabled(true);
                } else {
                    handle.sendEmptyMessageDelayed(AUTH_CODE, 1000);
                }
            }
        }
    }


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


    private static void addImage2QiNiu(String filePath, String qiniuToken, UploadManager uploadManager, final UploadUserPhotoListener listener) {

        String defaulQiNiuFileName = "%d-%d.JPEG";
        final Bitmap uploadBitmap = ImageUtils.getimage(filePath);
        if (uploadBitmap != null)
            uploadManager.put(ImageUtils.Bitmap2Bytes(uploadBitmap, 90), String.format(defaulQiNiuFileName, AppContext.user.getUserId(), System.currentTimeMillis()), qiniuToken,
                    new UpCompletionHandler() {

                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
                            if (null != info) {
                                try {
                                    if (info.isOK()) {
                                        uploadBitmap.recycle();
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
     */
    public static void postUserInfo2Server(final Context mContext, String userName, String userPhoto, String userGender, String userLocation, String userPw, String userRePw,
                                           final UploadUserPhotoListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("user.name", userName);
        params.put("user.gander", ("男".equals(userGender) ? "M" : "W"));
        params.put("user.location", userLocation);
        params.put("user.photo", userPhoto);
        params.put("user.password", TextUtils.isEmpty(userPw) ? "" : MD5.hexdigest(userPw));
        params.put("user.restpsw", TextUtils.isEmpty(userRePw) ? "" : MD5.hexdigest(userRePw));

        GsonRequest<Object> request = new GsonRequest<>(NetApi.updateUser, Object.class, params, new ResponseListener<Object>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
                listener.onError();
            }

            @Override
            public void onResponse(Object response, boolean result, String errorCode, @Nullable String msg) {
                if (result) {
                    listener.onSuccess("");
                } else {
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                    listener.onError();
                }
            }
        });
        request.start(mContext, null);
    }


    public static ArrayList<Integer> getQAvatar() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(R.mipmap.avatar_1);
        arrayList.add(R.mipmap.avatar_2);
        arrayList.add(R.mipmap.avatar_3);
        arrayList.add(R.mipmap.avatar_4);
        arrayList.add(R.mipmap.avatar_5);
        arrayList.add(R.mipmap.avatar_6);
        arrayList.add(R.mipmap.avatar_7);
        arrayList.add(R.mipmap.avatar_8);
        arrayList.add(R.mipmap.avatar_9);
        arrayList.add(R.mipmap.avatar_10);
        arrayList.add(R.mipmap.avatar_11);
        arrayList.add(R.mipmap.avatar_12);
        arrayList.add(R.mipmap.avatar_13);
        arrayList.add(R.mipmap.avatar_14);
        arrayList.add(R.mipmap.avatar_15);
        arrayList.add(R.mipmap.avatar_16);
        arrayList.add(R.mipmap.avatar_17);
        arrayList.add(R.mipmap.avatar_18);
        arrayList.add(R.mipmap.avatar_19);
        arrayList.add(R.mipmap.avatar_20);
        arrayList.add(R.mipmap.avatar_21);
        arrayList.add(R.mipmap.avatar_22);
        arrayList.add(R.mipmap.avatar_23);
        arrayList.add(R.mipmap.avatar_24);
        arrayList.add(R.mipmap.avatar_25);
        arrayList.add(R.mipmap.avatar_26);
        arrayList.add(R.mipmap.avatar_27);
        arrayList.add(R.mipmap.avatar_28);
        arrayList.add(R.mipmap.avatar_29);
        arrayList.add(R.mipmap.avatar_30);
        arrayList.add(R.mipmap.avatar_31);
        arrayList.add(R.mipmap.avatar_32);
        arrayList.add(R.mipmap.avatar_33);
        arrayList.add(R.mipmap.avatar_34);
        arrayList.add(R.mipmap.avatar_35);
        arrayList.add(R.mipmap.avatar_36);
        arrayList.add(R.mipmap.avatar_37);
        arrayList.add(R.mipmap.avatar_38);
        arrayList.add(R.mipmap.avatar_39);
        arrayList.add(R.mipmap.avatar_40);
        arrayList.add(R.mipmap.avatar_41);
        arrayList.add(R.mipmap.avatar_42);
        arrayList.add(R.mipmap.avatar_43);
        arrayList.add(R.mipmap.avatar_44);
        arrayList.add(R.mipmap.avatar_45);
        arrayList.add(R.mipmap.avatar_46);
        arrayList.add(R.mipmap.avatar_47);
        arrayList.add(R.mipmap.avatar_48);
        arrayList.add(R.mipmap.avatar_49);
        arrayList.add(R.mipmap.avatar_50);
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

//    private static Bitmap uploadBitmap = null;

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
