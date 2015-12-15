package co.quchu.quchu.presenter;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONObject;

import co.quchu.quchu.model.PostCardModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.LogUtils;

/**
 * PostCardPresenter
 * User: Chenhs
 * Date: 2015-11-12
 */
public class PostCardPresenter {

    public static void GetPostCardList(Context context, final MyPostCardListener listener) {
        NetService.get(context, NetApi.GetCardList, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json("CardList=" + response);
                if (response!=null){
                    Gson gson = new Gson();
                    PostCardModel model =gson.fromJson(response.toString(), PostCardModel.class);
                    listener.onSuccess(model);
                }
            }

            @Override
            public boolean onError(String error) {
                listener.onError(error);
                return false;
            }
        });
    }

  public   interface MyPostCardListener {
        void onSuccess(PostCardModel model);

        void onError(String error);
    }
}
