package co.quchu.quchu.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;

import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.PagerModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.model.SceneDetailModel;
import co.quchu.quchu.model.SceneModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.EventFlags;

/**
 * Created by Nico on 16/7/5.
 */
public class ScenePresenter {



    public static void getAllScene(Context context, int cityId,int pageNo, final CommonListener<PagerModel<SceneModel>> listener) {

        HashMap<String,String> params = new HashMap<>();
        params.put("cityId",String.valueOf(cityId));
        params.put("pageNo",String.valueOf(pageNo));
        NetService.get(context, NetApi.getAllScene, params,new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                PagerModel<SceneModel> sceneModels = new Gson().fromJson(response.toString(), new TypeToken<PagerModel<SceneModel>>() {}.getType());
                listener.successListener(sceneModels);
            }

            @Override
            public boolean onError(String error) {
                listener.errorListener(null,error,error);
                return false;
            }
        });
    }

    public static void getMyScene(Context context, int cityId,int pageNo, final CommonListener<PagerModel<SceneModel>> listener) {

        HashMap<String,String> params = new HashMap<>();
        params.put("cityId",String.valueOf(cityId));
        params.put("pageNo",String.valueOf(pageNo));
        NetService.get(context, NetApi.getMyScene, params,new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                PagerModel<SceneModel> sceneModels = new Gson().fromJson(response.toString(), new TypeToken<PagerModel<SceneModel>>() {}.getType());
                listener.successListener(sceneModels);
            }

            @Override
            public boolean onError(String error) {
                listener.errorListener(null,error,error);
                return false;
            }
        });
    }

    public static void addFavoriteScene(Context context, final int sceneId, final CommonListener listener){
        HashMap<String,String> params = new HashMap<>();
        params.put("sceneId",String.valueOf(sceneId));
        NetService.get(context, NetApi.addFavoriteScene, params,new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                listener.successListener(response);
                EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_SCENE_FAVORITE,sceneId));
            }

            @Override
            public boolean onError(String error) {
                listener.errorListener(null,error,error);
                return false;
            }
        });
    }

    public static void delFavoriteScene(Context context, final int sceneId, final CommonListener listener){
        HashMap<String,String> params = new HashMap<>();
        params.put("sceneId",String.valueOf(sceneId));
        NetService.get(context, NetApi.delFavoriteScene, params,new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                listener.successListener(response);
                EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_SCENE_CANCEL_FAVORITE,sceneId));
            }

            @Override
            public boolean onError(String error) {
                listener.errorListener(null,error,error);
                return false;
            }
        });
    }

    public static void getSceneDetail(Context context,int sceneId, int cityId,int pageNo,String lat,String lon,int []placeIds, final CommonListener<SceneDetailModel> listener) {

        StringBuffer urlGenerator = new StringBuffer(NetApi.getSceneDetail);

        urlGenerator
                .append("?cityId=").append(cityId)
                .append("&pagesNo=").append(pageNo)
                .append("&latitude=").append(lat)
                .append("&longitude=").append(lon)
                .append("&sceneId=").append(sceneId);


        if (null!=placeIds && pageNo>1){
            for (int i = 0; i < placeIds.length; i++) {
                urlGenerator.append("&placeIds=").append(placeIds[i]);
            }
        }

        NetService.get(context, urlGenerator.toString(),new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {

                SceneDetailModel sceneDetailModel = new Gson().fromJson(response.toString(), SceneDetailModel.class);
                listener.successListener(sceneDetailModel);
            }

            @Override
            public boolean onError(String error) {
                listener.errorListener(null,error,error);
                return false;
            }
        });
    }


}
