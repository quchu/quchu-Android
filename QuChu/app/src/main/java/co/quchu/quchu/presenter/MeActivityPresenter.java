package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import co.quchu.quchu.model.MyGeneModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.utils.SPUtils;

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
//                Toast.makeText(context, (R.string.network_error), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(MyGeneModel response, boolean result, @Nullable String exception, @Nullable String msg) {
                listener.successListener(response);
            }
        });
        request.start(context);
    }

    public void getUnreadMessageCound(final OnUnreadMessageCountListener listener) {
        GsonRequest<String> request = new GsonRequest<>(NetApi.notReadMessage, new ResponseListener<String>() {

            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
            }

            @Override
            public void onResponse(String response, boolean result, @Nullable String exception, @Nullable String msg) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int msgCount = 0;
                    int qCount = 0;
                    int feedMsgCount = 0;

                    if (jsonObject.has("msgCount")) {
                        msgCount = jsonObject.getInt("msgCount");
                    }

                    if (jsonObject.has("qmsgCount")) {
                        qCount = jsonObject.getInt("qmsgCount");
                    }

                    if (jsonObject.has("feedmsgCount")) {
                        feedMsgCount = jsonObject.getInt("feedmsgCount");
                    }

                    if (msgCount > 0 ) {
                        SPUtils.setHasPushMsg(true);
                    }

                    if (listener != null) {
                        listener.onUnreadMessageCount(msgCount, feedMsgCount);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        request.start(context);
    }

    public interface OnUnreadMessageCountListener {
        void onUnreadMessageCount(int msgCount, int feedbackMsgCount);
    }

}
