package co.quchu.quchu.presenter;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.quchu.quchu.model.MessageModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.StringUtils;

/**
 * MessageCenterPresenter
 * User: Chenhs
 * Date: 2016-01-11
 */
public class MessageCenterPresenter {

    public static void getMessageList(Context mContext, final MessageGetDataListener listener) {
        NetService.get(mContext, NetApi.getMessageList, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json("Message==" + response);
                try {
                    if (response.has("result") && !"null".equals(response.getString("result")) && !StringUtils.isEmpty(response.getString("result")) && response.getJSONArray("result").length() > 0) {
                        JSONArray arrayList = response.getJSONArray("result");
                        ArrayList<MessageModel> messageList = new ArrayList<MessageModel>();
                        Gson gson = new Gson();
                        for (int i = 0; i < arrayList.length(); i++) {
                            MessageModel model = gson.fromJson(arrayList.getString(i), MessageModel.class);
                            messageList.add(model);
                        }
                        listener.onSuccess(messageList);
                    } else {
                        listener.onError();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onError();
                }
            }

            @Override
            public boolean onError(String error) {
                listener.onError();
                return false;
            }
        });
    }

    public interface MessageGetDataListener {
        void onSuccess(ArrayList<MessageModel> arrayList);

        void onError();
    }
}
