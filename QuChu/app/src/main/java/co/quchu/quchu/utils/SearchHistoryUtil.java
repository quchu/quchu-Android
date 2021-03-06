package co.quchu.quchu.utils;

import com.google.gson.Gson;

import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.model.SearchModel;

/**
 * SearchHistoryUtil
 * User: Chenhs
 * Date: 2015-12-10
 */
public class SearchHistoryUtil {
    public static void saveSearchHistory(SearchModel model) {


        if (model != null) {
            LogUtils.json("search_Model=" + model.toString());
            while (model.getSearchList().size()>5){
                model.getSearchList().remove(0);
            }
            SPUtils.putValueToSPMap(AppContext.mContext, AppKey.SEARCHHISTORY, new Gson().toJson(model));
        }
    }

    public static SearchModel getSearchHistory() {

        if (StringUtils.isEmpty(SPUtils.getValueFromSPMap(AppContext.mContext, AppKey.SEARCHHISTORY, ""))) {
            return null;
        } else {
            SearchModel searchModel = new Gson().fromJson(SPUtils.getValueFromSPMap(AppContext.mContext, AppKey.SEARCHHISTORY, ""), SearchModel.class);
            while (searchModel.getSearchList().size()>5){
                searchModel.getSearchList().remove(0);
            }
            return searchModel;
        }
    }


}
