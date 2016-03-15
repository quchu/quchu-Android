package co.quchu.quchu.net;

import android.content.Context;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import co.quchu.quchu.utils.ImageUtils;
import co.quchu.quchu.utils.LogUtils;

/**
 * Created by linqipeng on 2016/3/15 17:21
 * email:437943145@qq.com
 * desc:
 */
public class ImageUpload {

    private String[] paths;

    public ImageUpload(String[] paths) {
        this.paths = paths;
    }


    public void upLoad(String[] path, String token) {
        // 重用 uploadManager。一般地，只需要创建一个 uploadManager 对象
        UploadManager uploadManager = new UploadManager();
        for (String item : path) {
            uploadManager.put(item, null, token,
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject res) {

                        }
                    }, null);
        }
    }

    /**
     * 压缩上传
     */
    public void upLoadCompress(String[] path, String token) {
        // 重用 uploadManager。一般地，只需要创建一个 uploadManager 对象


        UploadManager uploadManager = new UploadManager();
        for (String item : path) {
            try {
                ImageUtils.saveFile(ImageUtils.getimage(item), "");
            } catch (Exception e) {
                e.printStackTrace();
            }

            uploadManager.put(item, null, token,
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject res) {

                        }
                    }, null);
        }
    }

    public interface UploadResponse {
        void finish();

        void error();
    }

    public void getToken(Context context) {
        NetService.get(context, NetApi.getQiniuToken, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json("qiniu token==" + response);

                if (response != null && response.has("token")) {
                    try {
                        String qiniuToken = response.getString("token");

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
}
