package co.quchu.quchu.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.utils.ImageUtils;

/**
 * Created by linqipeng on 2016/3/15 17:21
 * email:437943145@qq.com
 * desc: 压缩图片上传, 对于传入的图片地址是网络地址的不压缩直接原样返回
 */
public class ImageUpload {
    private Context context;
    private UploadResponseListener listener;
    private List<String> path;
    private File cacheDir;
    private String qiniuToken;

    public ImageUpload(Context context, List<String> path, UploadResponseListener listener) {
        this.context = context;
        this.path = path;
        this.listener = listener;
        cacheDir = context.getCacheDir();
        getToken(context);
    }

    int faild;
    int succeed;
    StringBuffer buffer = new StringBuffer();

    /**
     * @param files 压缩后的图片
     * @param token 骑牛token
     */
    private void upLoad(List<String> files, String token) {
        // 重用 uploadManager。一般地，只需要创建一个 uploadManager 对象
        final int totalSize = files.size();
        UploadManager uploadManager = new UploadManager();
        String defaulQiNiuFileName = "%d-%d.JPEG";
        for (int i = 0, s = files.size(); i < s; i++) {
            String item = files.get(i);
            if (item.contains("http://")) {
                buffer.append(Uri.parse(item).getPath());
                buffer.append("|");
                succeed++;
                continue;
            }
            String key = String.format(defaulQiNiuFileName, AppContext.user.getUserId(), System.currentTimeMillis());
            uploadManager.put(item, key, token,
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject res) {
                            if (info.isOK()) {
                                succeed++;
                                buffer.append(key);
                                buffer.append("|");
                            } else {
                                faild++;
                            }
                            if (succeed + faild == totalSize) {
                                listener.finish(buffer.toString());
                            }

                        }
                    }, null);
        }
    }


    /**
     * 压缩图片
     */
    class MyAsyncTask extends AsyncTask<Void, Void, List<String>> {
        List<String> path;

        public MyAsyncTask(List<String> path) {
            this.path = path;
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            List<String> result = new ArrayList<>();
            for (int i = 0, s = path.size(); i < s; i++) {
                String item = path.get(i);
                try {
                    if (item.contains("http://")) {
                        result.add(item);
                        continue;
                    }
                    Bitmap bm = ImageUtils.getimage(item);
                    File file = new File(cacheDir, getRondom());
                    ImageUtils.saveFile(bm, file.getAbsolutePath());
                    result.add(file.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            upLoad(strings, qiniuToken);
        }
    }

    public interface UploadResponseListener {
        void finish(String result);

        void error();
    }

    public void getToken(Context context) {
        NetService.get(context, NetApi.getQiniuToken, new IRequestListener() {


            @Override
            public void onSuccess(JSONObject response) {
                if (response != null && response.has("token")) {
                    try {
                        qiniuToken = response.getString("token");
                        new MyAsyncTask(path).execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.error();
                    }
                } else {
                    listener.error();
                }
            }

            @Override
            public boolean onError(String error) {
                listener.error();
                return true;
            }
        });
    }

    public String getRondom() {
        SimpleDateFormat format = new SimpleDateFormat("yyMMdd-HHmmssSSS", Locale.CHINA);
        return format.format(new Date()) + ".jpg";
    }

}
