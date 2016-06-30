package co.quchu.quchu.presenter;



import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import co.quchu.quchu.model.ArticleModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;

/**
 * Created by Nico on 16/6/2.
 */
public class ArticlePresenter {




    public static void getArticles(final Context context, int cityId, int pageNo, final CommonListener<List<ArticleModel>> listener) {


        HashMap<String,String> params = new HashMap<>();
        params.put("cityId",String.valueOf(cityId));
        params.put("pagesNo",String.valueOf(pageNo));

        NetService.get(context, NetApi.getArticles,params, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {

                int maxPageNo = -1;
                List<ArticleModel> articleModels = null;
                if (response != null && response.has("result") && response.has("pageCount")) {
                    Gson gson = new Gson();
                    try {
                        maxPageNo = response.getInt("pageCount");
                        articleModels = gson.fromJson(response.getString("result"), new TypeToken<List<ArticleModel>>() {}.getType());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                listener.successListener(articleModels);
            }

            @Override
            public boolean onError(String error) {
                return false;
            }
        });
    }


    public static void getArticleById(final Context context, int cityId, int pageNo, int articleId) {


        HashMap<String,String> params = new HashMap<>();
        params.put("cityId",String.valueOf(cityId));
        params.put("pagesNo",String.valueOf(pageNo));
        params.put("articleId",String.valueOf(articleId));
        NetService.get(context, NetApi.getArticleById,params, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
            }

            @Override
            public boolean onError(String error) {
                return false;
            }
        });
    }


}
