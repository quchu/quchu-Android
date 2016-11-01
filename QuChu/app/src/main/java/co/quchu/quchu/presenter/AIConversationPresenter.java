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
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
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

    public static void getAIQuestion(Context context, boolean startor,final CommonListener<AIConversationQuestionModel> listener){

        HashMap<String, String> params = new HashMap<>();
        params.put("type", startor?String.valueOf("01"):"0");
        NetService.get(context, NetApi.getAIQuestion, params, new IRequestListener() {
            @Override public void onSuccess(JSONObject response) {
                if (null!=response){
                    AIConversationQuestionModel s = new Gson().fromJson(response.toString(),AIConversationQuestionModel.class);
                    listener.successListener(s);
                }else{
                    System.out.println("null");
                }
            }

            @Override public boolean onError(String error) {
                listener.errorListener(null, null, null);
                return false;
            }
        });
    }


    public static void getAIAnswer(Context context, String question,String flash,final CommonListener<AIConversationAnswerModel> listener){

        HashMap<String, String> params = new HashMap<>();
        params.put("question", question);
        params.put("flash", flash);

        NetService.get(context, NetApi.getAIAnswer, params, new IRequestListener() {
            @Override public void onSuccess(JSONObject response) {
                if (null!=response){
                    AIConversationAnswerModel s = new Gson().fromJson(response.toString(),AIConversationAnswerModel.class);
                    listener.successListener(s);
                }else{
                    System.out.println("null");
                }
            }

            @Override public boolean onError(String error) {
                listener.errorListener(null, null, null);
                return false;
            }
        });
    }


}
