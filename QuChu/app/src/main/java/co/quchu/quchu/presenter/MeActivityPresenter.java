package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import co.quchu.quchu.model.MyGeneModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;

/**
 * Created by no21 on 2016/4/8.
 * email:437943145@qq.com
 * desc :
 */
public class MeActivityPresenter {

    private Context context;

    public MeActivityPresenter( Context context) {
        this.context = context;
    }

    public void getGene(final CommonListener<MyGeneModel> listener) {
        GsonRequest<MyGeneModel> request = new GsonRequest<>(NetApi.getUserGene, MyGeneModel.class, new ResponseListener<MyGeneModel>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(MyGeneModel response, boolean result, @Nullable String exception, @Nullable String msg) {
                listener.successListener(response);
            }
        });
        request.start(context);
    }

    public void getUnreadMassageCound(final CommonListener<Integer> listener) {
        GsonRequest<String> request = new GsonRequest<>(NetApi.notReadMassage, new ResponseListener<String>() {

            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
            }

            @Override
            public void onResponse(String response, boolean result, @Nullable String exception, @Nullable String msg) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int count = jsonObject.getInt("msgCount");
                    if (count > 0) {
                        listener.successListener(count);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        request.start(context);
    }

}
