package co.quchu.quchu.presenter;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONObject;

import co.quchu.quchu.model.FlickrModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.LogUtils;

/**
 * FlickrPresenter
 * User: Chenhs
 * Date: 2015-11-20
 */
public class FlickrPresenter {

    public static void getImageAlbum(Context context , final FlickrListener listener) {
        LogUtils.json("FlickrPresenter  getAlbum  start ");
        NetService.get(context, String.format(NetApi.GetFavoriteAlbum, NetApi.DEBUG_TOKEN,"new",0), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json("onSuccess");
                LogUtils.json(response.toString());
                Gson gson = new Gson();
                FlickrModel model = gson.fromJson(response.toString(), FlickrModel.class);

                LogUtils.json("isNull" + (model == null));
                if (null!= model){
                    listener.onSuccess(model);
                    for (int i =0;i<model.getImgs().getResult().size();i++){
                        LogUtils.json(model.getImgs().getResult().get(i).getPath());
                    }
                }else{
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

    public static void getFavoriteAlbum(){

    }
}
