package co.quchu.quchu.presenter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Toast;

import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.adapter.CommonItemClickListener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

import co.quchu.quchu.R;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.DetailModel;
import co.quchu.quchu.model.SimpleQuchuDetailAnalysisModel;
import co.quchu.quchu.model.VisitedInfoModel;
import co.quchu.quchu.model.VisitedUsersModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;

/**
 * InterestingDetailPresenter
 * User: Chenhs
 * Date: 2015-12-13
 */
public class InterestingDetailPresenter {
    public static void getInterestingData(final Context context, int pId, final CommonListener<DetailModel> listener) {

        NetService.get(context, String.format(NetApi.getDetail, pId), new IRequestListener() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        if (response != null) {
                            Gson gson = new Gson();
                            DetailModel detailModel = gson.fromJson(response.toString(), DetailModel.class);
                            if (detailModel != null)
                                listener.successListener(detailModel);
                            else {
                                listener.errorListener(null,null,null);
                            }
                        } else {
                            listener.errorListener(null,null,null);
                        }
                    }

                    @Override
                    public boolean onError(String error) {
                        listener.errorListener(null,null,null);
                        return false;
                    }
                }

        );
    }

    public interface getDetailDataListener {
        void getDetailData(DetailModel model);
    }

    public static void getUserOutPlace(Context context, int pId, boolean isout, final DetailDataListener listener) {
        String urlStr;
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
        String favoUrl;
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


    public static void getVisitedUsers(Context context, int cityId, final CommonListener<VisitedUsersModel> listener) {
        String url = String.format(NetApi.getVisitedUsers, cityId);
        NetService.get(context, url, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                VisitedUsersModel result = new Gson().fromJson(response.toString(), VisitedUsersModel.class);
                listener.successListener(result);
            }

            @Override
            public boolean onError(String error) {
                listener.errorListener(new VolleyError("error"), "", "");
                return false;
            }
        });
    }

    public static void getVisitedInfo(Context context, int pId, final CommonListener<VisitedInfoModel> pListener) {
        NetService.get(context, String.format(NetApi.getVisitedInfo, pId), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                VisitedInfoModel visitedInfoModel = new Gson().fromJson(response.toString(), VisitedInfoModel.class);
                pListener.successListener(visitedInfoModel);

            }

            @Override
            public boolean onError(String error) {
                pListener.errorListener(new VolleyError(error), "", "");
                return false;
            }
        });
    }


    public static void submitDetailRating(Activity context, String images, String tagIds,int pId,String content, int score, final CommonListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("images",images);
        params.put("content",String.valueOf(content));
        params.put("tagIds",tagIds);
        params.put("score",String.valueOf(score));
        params.put("placeId",String.valueOf(pId));
        System.out.println("!--!");
        new GsonRequest<>(NetApi.commitDetailRating, Object.class, params, new ResponseListener<Object>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                listener.errorListener(null,null,null);
                System.out.println("---!");
            }

            @Override
            public void onResponse(Object response, boolean isNull, String exception, @Nullable String msg) {
                listener.successListener(response);
                System.out.println("---!!!!!");
            }
        }).start(context);

    }


    public static void updateRatingInfo(Context context, int pId, int score, String tagIds, final DetailDataListener listener) {
        NetService.get(context, String.format(NetApi.updateRatingInfo, pId, tagIds, score), new IRequestListener() {
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


    public static void getVisitorAnalysis(Context context, int cityId, final CommonListener<SimpleQuchuDetailAnalysisModel> listener) {
        String url = String.format(NetApi.getVisitorAnalysis, cityId);
        NetService.get(context, url, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                SimpleQuchuDetailAnalysisModel result = new Gson().fromJson(response.toString(), SimpleQuchuDetailAnalysisModel.class);
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
