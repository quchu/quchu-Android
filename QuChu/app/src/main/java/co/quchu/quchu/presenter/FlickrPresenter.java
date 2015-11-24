package co.quchu.quchu.presenter;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONObject;

import co.quchu.quchu.model.FlickrModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;

/**
 * FlickrPresenter
 * User: Chenhs
 * Date: 2015-11-20
 */
public class FlickrPresenter {

    public static void loadMoreAlbum(Context context, String ablumUrl, final FlickrListener listener) {
        LogUtils.json("FlickrPresenter  getAlbum  start ");
        NetService.get(context, ablumUrl, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json("onSuccess");
                LogUtils.json(response.toString());
                Gson gson = new Gson();
                FlickrModel model = gson.fromJson(response.toString(), FlickrModel.class);

                LogUtils.json("isNull" + (model == null));
                if (null != model) {
                    listener.onSuccess(model);
                } else {
                    listener.onError("没有更多数据了");
                }
            }

            @Override
            public boolean onError(String error) {
                LogUtils.e(error);
                listener.onError(error);
                return false;
            }
        });
    }

    public interface FlickrListener {
        void onSuccess(FlickrModel flickrModel);

        void onError(String error);
    }

    public class FlickrListenerImpl implements FlickrListener {

        @Override
        public void onSuccess(FlickrModel flickrModel) {

        }

        @Override
        public void onError(String error) {

        }
    }

    public static void getFavoriteAlbumHot(Context context, final FlickrListener listener) {
        LogUtils.json("FlickrPresenter  getAlbum  start ");
        NetService.get(context, String.format(NetApi.GetFavoriteAlbum, SPUtils.getUserToken(context), "hot", 0), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json("onSuccess");
                LogUtils.json(response.toString());
                Gson gson = new Gson();
                FlickrModel model = gson.fromJson(response.toString(), FlickrModel.class);

                LogUtils.json("isNull" + (model == null));
                if (null != model) {
                    listener.onSuccess(model);
                } else {
                    listener.onError("没有更多数据了");
                }
            }

            @Override
            public boolean onError(String error) {
                LogUtils.e(error);
                listener.onError(error);
                return false;
            }
        });
    }

    public static void getFlickrAlbum(Context context, String lastUrl, String albumType, int pageNum, final FlickrListener listener) {
        NetService.get(context, String.format(lastUrl, SPUtils.getUserToken(context), albumType, pageNum), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json("onSuccess");
                LogUtils.json(response.toString());
                Gson gson = new Gson();
                FlickrModel model = gson.fromJson(response.toString(), FlickrModel.class);

                LogUtils.json("isNull" + (model == null));
                if (null != model) {
                    listener.onSuccess(model);
                } else {
                    listener.onError("没有更多数据了");
                }
            }

            @Override
            public boolean onError(String error) {
                LogUtils.e(error);
                listener.onError(error);
                return false;
            }
        });
    }
}
