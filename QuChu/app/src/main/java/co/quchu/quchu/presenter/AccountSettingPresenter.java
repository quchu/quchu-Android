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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
    public static void postUserInfo2Server(final Context mContext, String userName, String userPhoto, String userGender, String userLocation,
                                           final UploadUserPhotoListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("user.name", userName);
        if (userGender.equals("男")||userGender.equals("女")){
            params.put("user.gander", ("男".equals(userGender) ? "M" : "W"));
        }else{
            params.put("user.gander", "A");
        }
        if (userLocation != null) {
            params.put("user.location", userLocation);
        }
        if (!TextUtils.isEmpty(userPhoto)) {
            params.put("user.photo", userPhoto);
        }
//        params.put("user.password", TextUtils.isEmpty(userPw) ? "" : MD5.hexdigest(userPw));
//        params.put("user.restpsw", TextUtils.isEmpty(userRePw) ? "" : MD5.hexdigest(userRePw));

        GsonRequest<Object> request = new GsonRequest<>(NetApi.updateUser, Object.class, params, new ResponseListener<Object>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
//                Toast.makeText(mContext, (R.string.network_error), Toast.LENGTH_SHORT).show();
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
