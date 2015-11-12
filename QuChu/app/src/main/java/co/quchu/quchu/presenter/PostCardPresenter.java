package co.quchu.quchu.presenter;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.quchu.quchu.model.PostCardModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;

/**
 * PostCardPresenter
 * User: Chenhs
 * Date: 2015-11-12
 */
public class PostCardPresenter {

    public static void GetPostCardList(Context context, final GetPostCardListener listener) {
        NetService.get(context, String.format(NetApi.GetCardList, NetApi.DEBUG_TOKEN), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response != null && response.has("result")) {
                    try {
                        JSONArray array = response.getJSONArray("result");
                        if (array.length() > 0) {
                            ArrayList<PostCardModel> postCardList = new ArrayList<PostCardModel>();
                            Gson gson = new Gson();
                            PostCardModel postCardModel;
                            for (int i = 0; i < array.length(); i++) {
                                postCardModel = gson.fromJson(array.getString(i), PostCardModel.class);
                                postCardList.add(postCardModel);
                            }
                            listener.onSuccess(postCardList);
                        } else {
                            listener.onError("  ");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onError("  ");
                    }
                }
            }

            @Override
            public boolean onError(String error) {
                listener.onError(error);
                return false;
            }
        });
    }

    interface GetPostCardListener {
        void onSuccess(ArrayList<PostCardModel> arrayList);

        void onError(String error);
    }
}
