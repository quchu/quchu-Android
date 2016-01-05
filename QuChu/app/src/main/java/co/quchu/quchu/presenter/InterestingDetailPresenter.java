package co.quchu.quchu.presenter;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONObject;

import co.quchu.quchu.model.DetailModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;

/**
 * InterestingDetailPresenter
 * User: Chenhs
 * Date: 2015-12-13
 */
public class InterestingDetailPresenter {
    public static void getInterestingData(Context context, int pId, final getDetailDataListener listener) {
        NetService.get(context, String.format(NetApi.getDetail, pId), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response != null) {
                    Gson gson = new Gson();
                    DetailModel detailModel = gson.fromJson(response.toString(), DetailModel.class);
                    listener.getDetailData(detailModel);
                }
            }

            @Override
            public boolean onError(String error) {
                return false;
            }
        });
    }

    public interface getDetailDataListener {
        void getDetailData(DetailModel model);
    }

    public static void getUserOutPlace(Context context, int pId, boolean isout, final DetailDataListener listener) {
        String urlStr = "";
        if (isout) {
            urlStr = String.format(NetApi.delUserOutPlace, pId);
        } else {
            urlStr = String.format(NetApi.getUserOutPlace, pId);
        }

        NetService.post(context, urlStr, null, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                listener.onSuccessCall("");
            }

            @Override
            public boolean onError(String error) {
                listener.onErrorCall(error);
                return false;
            }
        });
    }

    public static void setDetailFavorite(Context context, int pId, boolean isFavorite, final DetailDataListener listener) {
        String favoUrl = "";
        if (isFavorite) {
            favoUrl = String.format(NetApi.userDelFavorite, pId, NetApi.FavTypePlace);
        } else {
            favoUrl = String.format(NetApi.userFavorite, pId, NetApi.FavTypePlace);
        }
        NetService.get(context, favoUrl, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                listener.onSuccessCall("");
            }

            @Override
            public boolean onError(String error) {
                listener.onErrorCall("");
                return false;
            }
        });
    }


    public interface DetailDataListener {
        void onSuccessCall(String str);

        void onErrorCall(String str);
    }
}
