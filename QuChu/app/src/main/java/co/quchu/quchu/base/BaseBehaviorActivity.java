package co.quchu.quchu.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringWriter;
import co.quchu.quchu.presenter.UserBehaviorPresentor;


/**
 * Created by Nico on 16/6/2.
 */
public abstract class BaseBehaviorActivity extends BaseActivity {


    @Override
    public void finish() {
        super.finish();
        UserBehaviorPresentor.insertBehavior(getApplicationContext(), getUserBehaviorPageId(), "finish", "", System.currentTimeMillis());
    }

    @Override
    protected void onPause() {
        super.onPause();
        UserBehaviorPresentor.insertBehavior(getApplicationContext(), getUserBehaviorPageId(), "pause", "", System.currentTimeMillis());
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserBehaviorPresentor.insertBehavior(getApplicationContext(), getUserBehaviorPageId(), "resume", "", System.currentTimeMillis());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserBehaviorPresentor.insertBehavior(getApplicationContext(), getUserBehaviorPageId(), "enter", getStrUserBehavior(getUserBehaviorArguments()), System.currentTimeMillis());
    }




    private String getStrUserBehavior(ArrayMap<String, Object> dataSet) {
        if (null== dataSet){
            return "";
        }
        JSONObject jsonObject = new JSONObject();
        for (String key : dataSet.keySet()) {
            try {
                jsonObject.put(key,dataSet.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return jsonObject.toString();
    }

    public abstract ArrayMap<String, Object> getUserBehaviorArguments();

    public abstract int getUserBehaviorPageId();

}
