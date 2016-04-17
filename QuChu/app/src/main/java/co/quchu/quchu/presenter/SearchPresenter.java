package co.quchu.quchu.presenter;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;

/**
 * SearchPresenter
 * User: Chenhs
 * Date: 2015-12-10
 */
public class SearchPresenter {
    public static void searchFromService(Context context, String seachStr, int pageNum, final SearchResultListener listener) {

        System.out.println(String.format(NetApi.Seach, seachStr, pageNum));
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
}
