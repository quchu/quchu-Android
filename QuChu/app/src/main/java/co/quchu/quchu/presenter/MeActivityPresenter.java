package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.volley.VolleyError;

import co.quchu.quchu.model.MyGeneModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.view.activity.IMeActivity;

/**
 * Created by no21 on 2016/4/8.
 * email:437943145@qq.com
 * desc :
 */
public class MeActivityPresenter {
    private IMeActivity view;

    private Context context;

    public MeActivityPresenter(IMeActivity view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void getGene() {
        GsonRequest<MyGeneModel> request = new GsonRequest<>(NetApi.getUserGene, MyGeneModel.class, new ResponseListener<MyGeneModel>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(MyGeneModel response, boolean result, @Nullable String exception, @Nullable String msg) {
                view.initGene(response);
            }
        });
        request.start(context, null);
    }


}
