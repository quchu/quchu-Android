package co.quchu.quchu.presenter;



import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.HashMap;

import co.quchu.quchu.model.ArticleDetailModel;
import co.quchu.quchu.model.ArticleWithBannerModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.net.ResponseListener;

/**
 * Created by Nico on 16/6/2.
 */
public class ArticlePresenter {




    public static void getArticles(final Context context, int cityId, int pageNo, final CommonListener<ArticleWithBannerModel> listener) {


        HashMap<String,String> params = new HashMap<>();
        params.put("cityId",String.valueOf(cityId));
        params.put("pagesNo",String.valueOf(pageNo));

        NetService.get(context, NetApi.getArticles,params, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {

                ArticleWithBannerModel articleBannerModel;
                articleBannerModel = new Gson().fromJson(response.toString(), new TypeToken<ArticleWithBannerModel>() {}.getType());

                listener.successListener(articleBannerModel);
            }

            @Override
            public boolean onError(String error) {
                return false;
            }
        });
    }

    public static void addFavoriteArticle(Context context, int articleId, final CommonListener listener){
        HashMap<String,String> params = new HashMap<>();
        params.put("articleId",String.valueOf(articleId));
        NetService.get(context, NetApi.addFavoriteArticle, params, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                listener.successListener(null);
            }

            @Override
            public boolean onError(String error) {
                listener.errorListener(null,null,null);
                return false;
            }
        });
    }

    public static void delFavoriteArticle(Context context, int articleId, final CommonListener listener){
        HashMap<String,String> params = new HashMap<>();
        params.put("articleId",String.valueOf(articleId));
        NetService.get(context, NetApi.delFavoriteArticle, params, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                listener.successListener(null);
            }

            @Override
            public boolean onError(String error) {
                listener.errorListener(null,null,null);
                return false;
            }
        });
    }

    public static void getArticleById(final Context context, int cityId, int pageNo, String articleId,final CommonListener<ArticleDetailModel> listener) {


        HashMap<String,String> params = new HashMap<>();
        params.put("cityId",String.valueOf(cityId));
        params.put("pagesNo",String.valueOf(pageNo));
        params.put("articleId",String.valueOf(articleId));
        GsonRequest<ArticleDetailModel> request = new GsonRequest<ArticleDetailModel>(NetApi.getArticleById, ArticleDetailModel.class, params, new ResponseListener<ArticleDetailModel>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                listener.errorListener(error, "", "");
            }

            @Override
            public void onResponse(ArticleDetailModel response, boolean result, String errorCode, @Nullable String msg) {
                listener.successListener(response);
            }
        });
        request.start(context);

//        NetService.get(context, NetApi.getArticleById,params, new IRequestListener() {
//            @Override
//            public void onSuccess(JSONObject response) {
//
//                ArticleDetailModel articleDetailModel;
//                articleDetailModel = new Gson().fromJson(response.toString(), new TypeToken<ArticleDetailModel>() {}.getType());
//
//                listener.successListener(articleDetailModel);
//            }
//
//            @Override
//            public boolean onError(String error) {
//                listener.errorListener(null,error,null);
//                return false;
//            }
//        });
    }


}
