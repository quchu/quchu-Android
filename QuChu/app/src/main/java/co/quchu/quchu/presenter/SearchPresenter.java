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
import java.util.Locale;
import java.util.Map;

import co.quchu.quchu.R;
import co.quchu.quchu.model.AreaBean;
import co.quchu.quchu.model.ArticleKeyword;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.model.SearchCategoryBean;
import co.quchu.quchu.model.SearchSortBean;
import co.quchu.quchu.model.TagsModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.utils.SPUtils;

/**
 * SearchPresenter
 * User: Chenhs
 * Date: 2015-12-10
 */
public class SearchPresenter {

  public static void getNetArticleKeyword(Context context, final CommonListener<List<ArticleKeyword>> listener) {
    Map<String, String> map = new HashMap<>();
    map.put("cityId", String.valueOf(SPUtils.getCityId()));

    GsonRequest<List<ArticleKeyword>> request = new GsonRequest<>(NetApi.getNetArticleKeyword, new TypeToken<List<ArticleKeyword>>() {
    }.getType(), map, new ResponseListener<List<ArticleKeyword>>() {
      @Override
      public void onErrorResponse(@Nullable VolleyError error) {
        if (listener != null) {
          listener.errorListener(error, "", "");
        }
      }

      @Override
      public void onResponse(List<ArticleKeyword> response, boolean result, String errorCode, @Nullable String msg) {
        if (listener != null) {
          listener.successListener(response);
        }
      }
    });
    request.start(context);
  }

  public static void searchFromService(Context context, final int pageNum, String value, String tagId
      , String areaId, String circleId, String sortType, final SearchResultListener listener) {

    Map<String, String> map = new HashMap<>();
    map.put("value", value);
    map.put("pagesNo", String.valueOf(pageNum));
    map.put("cityId", String.valueOf(SPUtils.getCityId()));
    map.put("tagId", tagId);
    map.put("sortType", sortType);
    map.put("areaId", areaId);
    map.put("circleId", circleId);
    map.put("longitude", String.valueOf(SPUtils.getLongitude()));
    map.put("latitude", String.valueOf(SPUtils.getLatitude()));

    GsonRequest<String> request = new GsonRequest<>(NetApi.Seach, null, map, new ResponseListener<String>() {
      @Override
      public void onErrorResponse(@Nullable VolleyError error) {
        listener.onError();
      }

      @Override
      public void onResponse(String response, boolean result, String errorCode, @Nullable String msg) {
        try {
          if (!TextUtils.isEmpty(response)) {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray array = jsonObject.getJSONArray("result");
            int pageCount = jsonObject.getInt("pageCount");
            int pageNo = jsonObject.getInt("pagesNo");
            int resultCount = jsonObject.getInt("resultCount");

            if (array.length() > 0) {
              List<RecommendModel> arrayList = new Gson().fromJson(array.toString(), new TypeToken<ArrayList<RecommendModel>>() {
              }.getType());

              listener.onSuccess(arrayList, pageNo, pageCount, resultCount);
            }

          } else {
            listener.onError();
          }

        } catch (JSONException e) {
          listener.onError();
          e.printStackTrace();
        }
      }
    });
    request.start(context);
  }

//  public static void searchFromService(Context context, int pageNum, String searchStr, String categoryCode, String areaId, String circleId, String sortType, final SearchResultListener listener) {
//
//    Map<String, String> object = new HashMap<>();
//    object.put("value", searchStr);
//    object.put("pagesNo", String.valueOf(pageNum));
//    object.put("cityId", String.valueOf(SPUtils.getCityId()));
//    object.put("tagId", categoryCode);
//    object.put("sortType", sortType);
//    object.put("areaId", areaId);
//    object.put("circleId", circleId);
//    object.put("longitude", String.valueOf(SPUtils.getLongitude()));
//    object.put("latitude", String.valueOf(SPUtils.getLatitude()));
//
//
//    GsonRequest<String> request = new GsonRequest<>(NetApi.Seach, null, object, new ResponseListener<String>() {
//      @Override
//      public void onErrorResponse(@Nullable VolleyError error) {
//        LogUtils.json("onError=" + error.toString());
//        DialogUtil.dismissProgess();
//        listener.errorNull();
//      }
//
//      @Override
//      public void onResponse(String response, boolean result, String errorCode, @Nullable String msg) {
//        try {
//          if (!TextUtils.isEmpty(response)) {
//
//            JSONObject jsonObject = new JSONObject(response);
//
//            JSONArray array = jsonObject.getJSONArray("result");
//            if (array.length() > 0) {
//              List<RecommendModel> arrayList = new Gson().fromJson(array.toString(), new TypeToken<ArrayList<RecommendModel>>() {
//              }.getType());
//              int maxPageNo = jsonObject.getInt("pageCount");
//              listener.successResult(arrayList, maxPageNo);
//            } else {
//              listener.errorNull();
//            }
//            DialogUtil.dismissProgess();
//          } else {
//            listener.errorNull();
//          }
//        } catch (JSONException e) {
//          DialogUtil.dismissProgess();
//          listener.errorNull();
//          e.printStackTrace();
//        }
//      }
//    });
//    request.start(context);
//  }

  public interface SearchResultListener {

    void onSuccess(List<RecommendModel> data, int pageNo, int pageCount, int resultCount);

    void onError();
  }


  public interface SearchTagsListener {
    void successResult(List<TagsModel> arrayList);

    void errorNull();
  }


  public static void getCategoryTag(final Context context, final CommonListener<ArrayList<SearchCategoryBean>> listener) {
    GsonRequest<ArrayList<SearchCategoryBean>> request = new GsonRequest<>(NetApi.getCagegoryTag, new TypeToken<ArrayList<SearchCategoryBean>>() {
    }.getType(), new ResponseListener<ArrayList<SearchCategoryBean>>() {
      @Override
      public void onErrorResponse(@Nullable VolleyError error) {
        listener.errorListener(null, null, null);
      }

      @Override
      public void onResponse(ArrayList<SearchCategoryBean> response, boolean result, String errorCode, @Nullable String msg) {
        listener.successListener(response);

      }
    });
    request.start(context);
  }

  public static void getAreaList(final Context context, final CommonListener<ArrayList<AreaBean>> listener) {
    GsonRequest<ArrayList<AreaBean>> request = new GsonRequest<>(String.format(Locale.SIMPLIFIED_CHINESE, NetApi.getAreaList, SPUtils.getCityId()), new TypeToken<ArrayList<AreaBean>>() {
    }.getType(), new ResponseListener<ArrayList<AreaBean>>() {
      @Override
      public void onErrorResponse(@Nullable VolleyError error) {
        Toast.makeText(context, (R.string.network_error), Toast.LENGTH_SHORT).show();
        if (listener != null) {
          listener.errorListener(error, "", "");
        }
      }

      @Override
      public void onResponse(ArrayList<AreaBean> response, boolean result, String errorCode, @Nullable String msg) {
        if (listener != null) {
          listener.successListener(response);
        }
      }
    });
    request.start(context);
  }

  public static void getSortTypeList(final Context context, final CommonListener<ArrayList<SearchSortBean>> listener) {
    GsonRequest<ArrayList<SearchSortBean>> request = new GsonRequest<>(NetApi.getSortList, new TypeToken<ArrayList<SearchSortBean>>() {
    }.getType(), new ResponseListener<ArrayList<SearchSortBean>>() {
      @Override
      public void onErrorResponse(@Nullable VolleyError error) {
        Toast.makeText(context, (R.string.network_error), Toast.LENGTH_SHORT).show();
        if (listener != null) {
          listener.errorListener(error, "", "");
        }
      }

      @Override
      public void onResponse(ArrayList<SearchSortBean> response, boolean result, String errorCode, @Nullable String msg) {
        if (listener != null) {
          listener.successListener(response);
        }
      }
    });
    request.start(context);
  }


  public static void getGroupTags(final Context context, final CommonListener<List<SearchCategoryBean>> listener) {
    GsonRequest<List<SearchCategoryBean>> request = new GsonRequest<>(NetApi.getGroupTags, new TypeToken<List<SearchCategoryBean>>() {
    }.getType(), new ResponseListener<List<SearchCategoryBean>>() {
      @Override
      public void onErrorResponse(@Nullable VolleyError error) {
        Toast.makeText(context, (R.string.network_error), Toast.LENGTH_SHORT).show();
        if (listener != null) {
          listener.errorListener(error, "", "");
        }
      }

      @Override
      public void onResponse(List<SearchCategoryBean> response, boolean result, String errorCode, @Nullable String msg) {
        if (listener != null) {
          listener.successListener(response);
        }

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
