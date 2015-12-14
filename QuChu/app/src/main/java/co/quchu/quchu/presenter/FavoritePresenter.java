package co.quchu.quchu.presenter;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONObject;

import co.quchu.quchu.model.FavoriteModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.LogUtils;

/**
 * FavoritePresenter
 * User: Chenhs
 * Date: 2015-12-14
 * 我的收藏
 */
public class FavoritePresenter {
    public static void getFavoriteData(Context mContext) {
        NetService.get(mContext, NetApi.getFavorite, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json("Favorite==" + response);
                Gson gson = new Gson();
                FavoriteModel model = gson.fromJson(response.toString(), FavoriteModel.class);
                if (model != null) {
                    LogUtils.json("favorite model=" + model.getCard().getCount() + "///place=" + model.getPlace().getCount());
                }
            }

            @Override
            public boolean onError(String error) {
                return false;
            }
        });
    }
}
