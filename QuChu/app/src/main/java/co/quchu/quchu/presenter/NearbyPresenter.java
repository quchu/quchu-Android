package co.quchu.quchu.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.DetailModel;
import co.quchu.quchu.model.NearbyItemModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;

/**
 * InterestingDetailPresenter
 * User: Chenhs
 * Date: 2015-12-13
 */
public class NearbyPresenter {
    public static void getNearbyData(Context context, int cityId,String tags,double latitude,double longitude,int pageNo, final getNearbyDataListener listener) {
        NetService.get(context, String.format(NetApi.getNearby, tags,cityId,String.valueOf(latitude),String.valueOf(longitude),pageNo), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response != null && response.has("result") && response.has("pageCount")) {
                    int maxPageNo = -1;
                    Gson gson = new Gson();
                    List<NearbyItemModel> nearbyItemModels = null;
                    try {
                        maxPageNo = response.getInt("pageCount");
                        nearbyItemModels = gson.fromJson(response.getString("result"), new TypeToken<List<NearbyItemModel>>(){}.getType());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    listener.getNearbyData(nearbyItemModels,maxPageNo);
                }
            }

            @Override
            public boolean onError(String error) {
                DialogUtil.dismissProgess();
                return false;
            }
        });
    }

    public interface getNearbyDataListener {
        void getNearbyData(List<NearbyItemModel> model,int i);
    }

}
