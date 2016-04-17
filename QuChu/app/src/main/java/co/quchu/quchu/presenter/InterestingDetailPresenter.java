package co.quchu.quchu.presenter;

import android.content.Context;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.DetailModel;
import co.quchu.quchu.model.SimpleQuchuDetailAnalysisModel;
import co.quchu.quchu.model.SimpleUserModel;
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
                DialogUtil.dismissProgess();
                return false;
            }
        });
    }

    public interface getDetailDataListener {
        void getDetailData(DetailModel model);
    }

    public static void getUserOutPlace(Context context, int pId, boolean isout, final DetailDataListener listener) {
        String urlStr ;
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
        String favoUrl ;
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


    public static void getVisitedUsers(Context context, int cityId,final CommonListener<List<SimpleUserModel>> listener) {
        String url = String.format(NetApi.getVisitedUsers,cityId);
        NetService.get(context, url, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    List<SimpleUserModel> result = new Gson().fromJson(response.getString("result"),new TypeToken<List<SimpleUserModel>>(){}.getType());
                    listener.successListener(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.errorListener(new VolleyError("error"), e.getMessage(), "");
                }
            }

            @Override
            public boolean onError(String error) {
                listener.errorListener(new VolleyError("error"), "", "");
                return false;
            }
        });
    }

    public static void getVisitorAnalysis(Context context, int cityId, final CommonListener<SimpleQuchuDetailAnalysisModel> listener) {
        String url = String.format(NetApi.getVisitorAnalysis,cityId);
        NetService.get(context, url, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                SimpleQuchuDetailAnalysisModel result = new Gson().fromJson(response.toString(),SimpleQuchuDetailAnalysisModel.class);
                listener.successListener(result);
            }

            @Override
            public boolean onError(String error) {
                listener.errorListener(new VolleyError("error"), "", "");
                return false;
            }
        });
    }

}
