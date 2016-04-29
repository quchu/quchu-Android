package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.NearbyItemModel;
import co.quchu.quchu.model.NearbyMapModel;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.model.TagsModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.utils.LogUtils;

/**
 * SearchPresenter
 * User: Chenhs
 * Date: 2015-12-10
 */
public class SearchPresenter {
    public static void searchFromService(Context context, String seachStr, int pageNum,int cityId, final SearchResultListener listener) {

        System.out.println(String.format(NetApi.Seach, seachStr, pageNum,cityId));
        NetService.get(context, String.format(NetApi.Seach, seachStr, pageNum), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
         //           if (response.has("result") && !StringUtils.isEmpty(response.getString("result"))) {
                    if (response!=null) {
                        JSONArray array = response.getJSONArray("result");
                        if (array.length() > 0) {
                            Gson gson = new Gson();
                            ArrayList<RecommendModel> arrayList = new ArrayList<RecommendModel>();
                            RecommendModel model;
                            for (int i = 0; i < array.length(); i++) {
                                model = gson.fromJson(array.getString(i), RecommendModel.class);
                                arrayList.add(model);
                            }
                            int maxPageNo = response.getInt("pageCount");
                            listener.successResult(arrayList,maxPageNo);
                        }else {
                            listener.errorNull();
                        }
                        DialogUtil.dismissProgess();
                    }else {
                        listener.errorNull();
                    }
                } catch (JSONException e) {
                    DialogUtil.dismissProgess();
                    listener.errorNull();
                    e.printStackTrace();
                }
            }

            @Override
            public boolean onError(String error) {
                LogUtils.json("onError=" + error.toString());
                DialogUtil.dismissProgess();
                listener.errorNull();
                return false;
            }
        });
    }

    public interface SearchResultListener {
        void successResult(ArrayList<RecommendModel> arrayList,int maxPageNo);

        void errorNull();
    }


    public interface SearchTagsListener {
        void successResult(List<TagsModel> arrayList);

        void errorNull();
    }

    public static void getSearchTags(final SearchTagsListener listener){

        new GsonRequest<>(Request.Method.GET, NetApi.getSearchTags, new TypeToken<List<TagsModel>>() {}.getType(), new ResponseListener<List<TagsModel>>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                listener.errorNull();
            }

            @Override
            public void onResponse(List<TagsModel> response, boolean result, @Nullable String exception, @Nullable String msg) {
                listener.successResult(response);
            }
        });

    }
}
