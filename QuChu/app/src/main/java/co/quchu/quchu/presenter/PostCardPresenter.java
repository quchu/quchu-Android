package co.quchu.quchu.presenter;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONObject;

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

    public static void GetPostCardList(Context context, final MyPostCardListener listener) {
        NetService.get(context, NetApi.GetCardList, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json("CardList=" + response);
                if (response != null) {
                    Gson gson = new Gson();
                    PostCardModel model = gson.fromJson(response.toString(), PostCardModel.class);
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
}
