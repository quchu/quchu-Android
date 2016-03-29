package co.quchu.quchu.presenter;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONObject;

import co.quchu.quchu.model.PostCardItemModel;
import co.quchu.quchu.model.PostCardModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.StringUtils;

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

    public static void sacePostCard(Context context, int pId, int cScore, String comment, String imageStr, final MyPostCardListener listener) {
        //  "?card.pid=%1$d&card.score=%2$d&card.comment=%3$s&card.image=%4$s";
        String saveUrl = "";
        if (StringUtils.isEmpty(comment) && StringUtils.isEmpty(imageStr)) {
            saveUrl = String.format("?card.pid=%1$d&card.score=%2$d", pId, cScore);
        } else if (!StringUtils.isEmpty(comment) && StringUtils.isEmpty(imageStr)) {
            saveUrl = String.format("?card.pid=%1$d&card.score=%2$d&card.comment=%3$s", pId, cScore, comment);
        } else if (StringUtils.isEmpty(comment) && !StringUtils.isEmpty(imageStr)) {
            saveUrl = String.format("?card.pid=%1$d&card.score=%2$d&card.image=%3$s", pId, cScore, imageStr);
        } else {
            saveUrl = String.format("?card.pid=%1$d&card.score=%2$d&card.comment=%3$s&card.image=%4$s", pId, cScore, comment, imageStr);
        }
        NetService.post(context, NetApi.saveOrUpdateCard + saveUrl, null, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                listener.onSuccess(null);
            }

            @Override
            public boolean onError(String error) {
                listener.onError("");
                return false;
            }
        });
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
                listener.onSuccess(null);
            }

            @Override
            public boolean onError(String error) {
                listener.onError("");
                return false;
            }
        });
    }
}
