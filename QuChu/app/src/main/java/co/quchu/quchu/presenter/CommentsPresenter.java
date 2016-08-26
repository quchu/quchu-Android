package co.quchu.quchu.presenter;

import android.content.Context;
import co.quchu.quchu.model.CommentModel;
import co.quchu.quchu.model.PagerModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import org.json.JSONObject;

public class CommentsPresenter {

    public static void getComments(Context context, long pid,int pageNo, final CommonListener<PagerModel<CommentModel>> listener) {

        HashMap<String,String> params = new HashMap<>();
        params.put("placeId",String.valueOf(pid));
        params.put("pagesNo",String.valueOf(pageNo));
        NetService.get(context, NetApi.getDetailComments, params,new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                PagerModel<CommentModel> sceneModels = new Gson().fromJson(response.toString(), new TypeToken<PagerModel<CommentModel>>() {}.getType());
                listener.successListener(sceneModels);
            }

            @Override
            public boolean onError(String error) {
                listener.errorListener(null,error,error);
                return false;
            }
        });
    }

}
