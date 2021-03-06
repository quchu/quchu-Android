package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.quchu.quchu.model.PostCardItemModel;
import co.quchu.quchu.model.PostCardModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.utils.LogUtils;

/**
 * PostCardPresenter
 * User: Chenhs
 * Date: 2015-11-12
 */
public class PostCardPresenter {


    public static void GetPostCardList(Context context, boolean isFavoritePostCard, final MyPostCardListener listener) {
        String netUrl;
        if (isFavoritePostCard) {
            netUrl = String.format(NetApi.getFavoriteList, 1, NetApi.FavTypeCard);
        } else {
            netUrl = NetApi.GetCardList;
        }
        NetService.get(context, netUrl, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json("CardList=" + response);
                if (response != null) {
                    if (!response.has("msg") && !response.has("data")) {
                        Gson gson = new Gson();
                        PostCardModel model = gson.fromJson(response.toString(), PostCardModel.class);
                        listener.onSuccess(model);
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

    /**
     * Get postcard info via pid
     *
     * @param context
     * @param pId
     * @param listener
     */
    public static void getPostCardByPid(Context context, int pId, final MyPostCardItemListener listener) {
        NetService.get(context, String.format(NetApi.getPlaceUserCard, pId), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json("CardList=" + response);
                if (response != null) {
                    if (!response.has("msg") && !response.has("data")) {
                        Gson gson = new Gson();
                        PostCardItemModel model = gson.fromJson(response.toString(), PostCardItemModel.class);
                        listener.onSuccess(model);
                    } else {
                        listener.onError(null);
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

    public static void sacePostCard(Context context, int pId, int cScore, String comment, String imageStr, int cid, final MyPostCardListener listener) {
//服务器接受不到图片列表
        //  "?card.pid=%1$d&card.score=%2$d&card.comment=%3$s&card.image=%4$s";
//        String saveUrl = "";
//        String strCid = cid > 0 ? String.valueOf(cid) : "";
//        if (StringUtils.isEmpty(comment) && StringUtils.isEmpty(imageStr)) {
//            saveUrl = String.format("?card.pid=%1$d&card.score=%2$d&card.cardId=%3$s", pId, cScore, strCid);
//        } else if (!StringUtils.isEmpty(comment) && StringUtils.isEmpty(imageStr)) {
//            saveUrl = String.format("?card.pid=%1$d&card.score=%2$d&card.comment=%3$s&card.cardId=%4$s", pId, cScore, comment, strCid);
//        } else if (StringUtils.isEmpty(comment) && !StringUtils.isEmpty(imageStr)) {
//            saveUrl = String.format("?card.pid=%1$d&card.score=%2$d&card.cardId=%3$s", pId, cScore, strCid);
//        } else {
//            saveUrl = String.format("?card.pid=%1$d&card.score=%2$d&card.comment=%3&card.cardId=%4$s", pId, cScore, comment, strCid);
//        }
//        JSONObject jsonObject = new JSONObject();
//
//        try {
//            jsonObject.put("card.image", imageStr);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        NetService.post(context, NetApi.saveOrUpdateCard + saveUrl, jsonObject, new IRequestListener() {
//            @Override
//            public void onSuccess(JSONObject response) {
//                listener.onSuccess(null);
//            }
//
//            @Override
//            public boolean onError(String error) {
//                listener.onError("");
//                return false;
//            }
//        });
        Map<String, String> params = new HashMap<>();
        params.put("card.pid", String.valueOf(pId));
        params.put("card.score", String.valueOf(cScore));
        params.put("card.cardId", cid > 0 ? String.valueOf(cid) : "");
        params.put("card.image", imageStr);
        params.put("card.comment", comment);
        GsonRequest<Object> request = new GsonRequest<>(NetApi.saveOrUpdateCard, Object.class, params, new ResponseListener<Object>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                listener.onError("");
            }

            @Override
            public void onResponse(Object response, boolean result, String errorCode, @Nullable String msg) {
                if (result) {
                    listener.onSuccess(null);
                } else
                    listener.onError("");
            }
        });
        request.start(context, null);

    }

    public interface MyPostCardListener {
        void onSuccess(PostCardModel model);

        void onError(String error);
    }


    public interface MyPostCardItemListener {
        void onSuccess(PostCardItemModel model);

        void onError(String error);
    }

    /**
     * 点赞、取消点赞功能
     *
     * @param isPraise   当前状态是否已经点赞 true 已经点赞 则进行取消点赞
     * @param typeIsCard 点赞或取消点赞 类型  true==当前操作类型为明信片
     * @param praiseId   id  操作对象id
     */
    public static void setPraise(Context mContext, boolean isPraise, boolean typeIsCard, int praiseId, final MyPostCardListener listener) {
        String urlStr;
        if (isPraise) {
            if (typeIsCard) {
                urlStr = String.format(NetApi.delPraise, praiseId, "card");
            } else {
                urlStr = String.format(NetApi.delPraise, praiseId, "image");
            }
        } else {
            if (typeIsCard) {
                urlStr = String.format(NetApi.doPraise, praiseId, "card");
            } else {
                urlStr = String.format(NetApi.doPraise, praiseId, "image");
            }
        }
        NetService.get(mContext, urlStr, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                if (listener != null)
                    listener.onSuccess(null);
            }

            @Override
            public boolean onError(String error) {
                if (listener != null)
                    listener.onError("");
                return false;
            }
        });
    }
}
