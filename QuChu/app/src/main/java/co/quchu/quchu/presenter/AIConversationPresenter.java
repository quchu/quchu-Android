package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;
import co.quchu.quchu.model.AIConversationModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;
import com.android.volley.VolleyError;
import java.util.HashMap;
import java.util.Map;

/**
 * RecommendPresenter
 * User: Chenhs
 * Date: 2015-12-08
 * 趣处推荐 逻辑类
 */
public class AIConversationPresenter {

    public static void startConversation(Context context, boolean startor,final CommonListener<AIConversationModel> listener){

        Map<String, String> map = new HashMap<>();
        map.put("type", startor?String.valueOf("01"):"03");
        GsonRequest<AIConversationModel> request =
            new GsonRequest<>(NetApi.getAIQuestion, AIConversationModel.class, map, new ResponseListener<AIConversationModel>() {
                @Override public void onErrorResponse(@Nullable VolleyError error) {
                    if (listener != null) {
                        listener.errorListener(error, "", "");
                    }
                }

                @Override public void onResponse(AIConversationModel response, boolean result, String errorCode,
                    @Nullable String msg) {
                    if (listener != null) {
                        listener.successListener(response);
                    }
                }
            });
        request.start(context);


    }


    public static void getNext(Context context, String question,String flash,final CommonListener<AIConversationModel> listener){



        Map<String, String> map = new HashMap<>();
        map.put("question", question);
        map.put("flash", flash);
        GsonRequest<AIConversationModel> request =
            new GsonRequest<>(NetApi.getAIAnswer, AIConversationModel.class, map, new ResponseListener<AIConversationModel>() {
                @Override public void onErrorResponse(@Nullable VolleyError error) {
                    if (listener != null) {
                        listener.errorListener(error, "", "");
                    }
                }

                @Override public void onResponse(AIConversationModel response, boolean result, String errorCode,
                    @Nullable String msg) {
                    if (listener != null) {
                        listener.successListener(response);
                    }
                }
            });
        request.start(context);


    }


}
