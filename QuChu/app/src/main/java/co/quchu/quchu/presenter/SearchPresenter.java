package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.model.SearchCategoryBean;
import co.quchu.quchu.model.TagsModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.view.activity.SearchActivity;

/**
 * SearchPresenter
 * User: Chenhs
 * Date: 2015-12-10
 */
public class SearchPresenter {
    public static void searchFromService(Context context, String seachStr, int pageNum, int cityId, String categoryCode, String areaCode, String sortType, final SearchResultListener listener) {

        Map<String, String> object = new HashMap<>();
        object.put("value", seachStr);
        object.put("pageno", String.valueOf(pageNum));
        object.put("cityId", String.valueOf(cityId));

        object.put("tagId", categoryCode);
        object.put("sortType", sortType);
        object.put("circleId", areaCode);


        GsonRequest<String> request = new GsonRequest<>(NetApi.Seach, null, object, new ResponseListener<String>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                LogUtils.json("onError=" + error.toString());
                DialogUtil.dismissProgess();
                listener.errorNull();
            }

            @Override
            public void onResponse(String response, boolean result, String errorCode, @Nullable String msg) {
                try {
                    if (!TextUtils.isEmpty(response)) {

                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray array = jsonObject.getJSONArray("result");
                        if (array.length() > 0) {
                            ArrayList<RecommendModel> arrayList = new Gson().fromJson(array.toString(), new TypeToken<ArrayList<RecommendModel>>() {
                            }.getType());
                            int maxPageNo = jsonObject.getInt("pageCount");
                            listener.successResult(arrayList, maxPageNo);
                        } else {
                            listener.errorNull();
                        }
                        DialogUtil.dismissProgess();
                    } else {
                        listener.errorNull();
                    }
                } catch (JSONException e) {
                    DialogUtil.dismissProgess();
                    listener.errorNull();
                    e.printStackTrace();
                }
            }
        });
        request.start(context);

//        NetService.post(context, NetApi.Seach, object, new IRequestListener() {
//            @Override
//            public void onSuccess(JSONObject response) {
//                try {
//                    if (response != null) {
//                        JSONArray array = response.getJSONArray("result");
//                        if (array.length() > 0) {
//                            Gson gson = new Gson();
//                            ArrayList<RecommendModel> arrayList = new ArrayList<RecommendModel>();
//                            RecommendModel model;
//                            for (int i = 0; i < array.length(); i++) {
//                                model = gson.fromJson(array.getString(i), RecommendModel.class);
//                                arrayList.add(model);
//                            }
//                            int maxPageNo = response.getInt("pageCount");
//                            listener.successResult(arrayList, maxPageNo);
//                        } else {
//                            listener.errorNull();
//                        }
//                        DialogUtil.dismissProgess();
//                    } else {
//                        listener.errorNull();
//                    }
//                } catch (JSONException e) {
//                    DialogUtil.dismissProgess();
//                    listener.errorNull();
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public boolean onError(String error) {
//                LogUtils.json("onError=" + error.toString());
//                DialogUtil.dismissProgess();
//                listener.errorNull();
//                return false;
//            }
//        });
    }

    public interface SearchResultListener {
        void successResult(ArrayList<RecommendModel> arrayList, int maxPageNo);

        void errorNull();
    }


    public interface SearchTagsListener {
        void successResult(List<TagsModel> arrayList);

        void errorNull();
    }


    public static void getCategoryTag(final SearchActivity context) {
        GsonRequest<ArrayList<SearchCategoryBean>> request = new GsonRequest<>(NetApi.getCagegoryTag, new TypeToken<ArrayList<SearchCategoryBean>>() {
        }.getType(), new ResponseListener<ArrayList<SearchCategoryBean>>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(ArrayList<SearchCategoryBean> response, boolean result, String errorCode, @Nullable String msg) {
                context.initCategoryList(response);

            }
        });
        request.start(context);
    }

    public static void getAreaList(final SearchActivity context) {
        GsonRequest<ArrayList<SearchCategoryBean>> request = new GsonRequest<>(NetApi.getCagegoryTag, new TypeToken<ArrayList<SearchCategoryBean>>() {
        }.getType(), new ResponseListener<ArrayList<SearchCategoryBean>>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(ArrayList<SearchCategoryBean> response, boolean result, String errorCode, @Nullable String msg) {
                context.initCategoryList(response);

            }
        });
        request.start(context);
    }

    public static void getSearchTags(Context context, final SearchTagsListener listener) {

        GsonRequest<List<TagsModel>> request = new GsonRequest<>(Request.Method.GET, NetApi.getSearchTags, new TypeToken<List<TagsModel>>() {
        }.getType(), new ResponseListener<List<TagsModel>>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                listener.errorNull();
            }

            @Override
            public void onResponse(List<TagsModel> response, boolean result, @Nullable String exception, @Nullable String msg) {
                listener.successResult(response);
            }
        });
        request.start(context);

    }
}
