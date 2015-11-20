package co.quchu.quchu.presenter;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    public static void getAlbum(Context context) {
        LogUtils.json("FlickrPresenter  getAlbum  start ");
        NetService.get(context, String.format(NetApi.GetAlbum, NetApi.DEBUG_TOKEN), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json("onSuccess");
                LogUtils.json(response.toString());
                Gson gson = new Gson();
                FlickrModel model = gson.fromJson(response.toString(), FlickrModel.class);
                LogUtils.json("isNull" + (model != null));
                ArrayList<String>photoList=new ArrayList<String>();
                try {
                    JSONArray array = response.getJSONArray("result");
                    for (int i = 0; i < array.length(); i++) {
                        photoList.add(array.getJSONObject(i).getString("path"));
                    }
                        model.getImgs().setPhoto(photoList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (model != null) {
                    for (int i = 0; i < model.getImgs().getPhotos().size(); i++) {
                        LogUtils.json(i + "==position===" + model.getImgs().getPhotos().get(i));
                    }
                }
            }

            @Override
            public boolean onError(String error) {
                LogUtils.e(error);
                return false;
            }
        });
    }

    public interface AtmosphereListener {
        void onSuccess(ArrayList arrayList);
    }
}
