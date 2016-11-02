package co.quchu.quchu.presenter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import co.quchu.quchu.base.AppLocationListener;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.AIConversationAnswerModel;
import co.quchu.quchu.model.AIConversationQuestionModel;
import co.quchu.quchu.model.CityEntity;
import co.quchu.quchu.model.CityModel;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.utils.AppKey;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.widget.PolygonProgressView;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * RecommendPresenter
 * User: Chenhs
 * Date: 2015-12-08
 * 趣处推荐 逻辑类
 */
public class AIConversationPresenter {

    public static void postAIQuestion(Context context, boolean startor,final CommonListener<AIConversationQuestionModel> listener){

        Map<String, String> map = new HashMap<>();
        map.put("type", startor?String.valueOf("01"):"0");
        GsonRequest<AIConversationQuestionModel> request =
            new GsonRequest<>(NetApi.getAIQuestion, AIConversationQuestionModel.class, map, new ResponseListener<AIConversationQuestionModel>() {
                @Override public void onErrorResponse(@Nullable VolleyError error) {
                    if (listener != null) {
                        listener.errorListener(error, "", "");
                    }
                }

                @Override public void onResponse(AIConversationQuestionModel response, boolean result, String errorCode,
                    @Nullable String msg) {
                    if (listener != null) {
                        listener.successListener(response);
                    }
                }
            });
        request.start(context);


    }


    public static void postAIAnswer(Context context, String question,String flash,final CommonListener<AIConversationAnswerModel> listener){



        Map<String, String> map = new HashMap<>();
        map.put("question", question);
        map.put("flash", flash);
        GsonRequest<AIConversationAnswerModel> request =
            new GsonRequest<>(NetApi.getAIAnswer, AIConversationAnswerModel.class, map, new ResponseListener<AIConversationAnswerModel>() {
                @Override public void onErrorResponse(@Nullable VolleyError error) {
                    if (listener != null) {
                        listener.errorListener(error, "", "");
                    }
                }

                @Override public void onResponse(AIConversationAnswerModel response, boolean result, String errorCode,
                    @Nullable String msg) {
                    if (listener != null) {
                        listener.successListener(response);
                    }
                }
            });
        request.start(context);


    }


}
