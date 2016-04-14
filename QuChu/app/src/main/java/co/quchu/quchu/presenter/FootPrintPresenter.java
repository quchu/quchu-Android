package co.quchu.quchu.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.FootprintModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.view.activity.IFootprintActivity;

/**
 * Created by no21 on 2016/4/7.
 * email:437943145@qq.com
 * desc :
 */
public class FootPrintPresenter {
    private Context context;

    private IFootprintActivity view;

    public FootPrintPresenter(Context context, IFootprintActivity view) {
        this.context = context;
        this.view = view;
    }

    public static void getFootprint(Context context, int pId,int pageNo, final GetFootprintDataListener listener) {
        NetService.get(context, String.format(NetApi.getFootprint, pId,pageNo), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response != null && response.has("result") && response.has("pageCount")) {
                    int maxPageNo = -1;
                    Gson gson = new Gson();
                    List<FootprintModel> footprintModelList = null;
                    try {
                        maxPageNo = response.getInt("pageCount");
                        footprintModelList = gson.fromJson(response.getString("result"), new TypeToken<List<FootprintModel>>(){}.getType());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    listener.getFootprint(footprintModelList,maxPageNo);
                }
            }

            @Override
            public boolean onError(String error) {
                DialogUtil.dismissProgess();
                return false;
            }
        });
    }


    public interface GetFootprintDataListener {
        void getFootprint(List<FootprintModel> model, int i);
    }

}
