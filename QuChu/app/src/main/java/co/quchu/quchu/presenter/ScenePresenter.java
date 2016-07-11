package co.quchu.quchu.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.HashMap;

import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.PagerModel;
import co.quchu.quchu.model.SceneModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;

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
                DialogUtil.dismissProgess();
                listener.errorListener(null,error,error);
                return false;
            }
        });
    }


//
//        getMyScene
//        getAllScene
//        addFavoriteScene
//        delFavoriteScene
//        getSceneDetail
}
