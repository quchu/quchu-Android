package co.quchu.quchu.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import co.quchu.quchu.presenter.UserBehaviorPresentor;


/**
 * Created by Nico on 16/6/2.
 */
public abstract class BaseBehaviorActivity extends BaseActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserBehaviorPresentor.insertBehavior(getApplicationContext(), getUserBehaviorPageId(), "shutdown", "", System.currentTimeMillis());
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserBehaviorPresentor.insertBehavior(getApplicationContext(), getUserBehaviorPageId(), "enter", getStrUserBehavior(getUserBehaviorArguments()), System.currentTimeMillis());
    }

    private String getStrUserBehavior(ArrayMap<String, String> dataSet) {
        if (null== dataSet){
            return "";
        }
        String strArguments = "";
        StringWriter sWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(sWriter);

        try {
            writer.beginObject();
            for (String key : dataSet.keySet()) {
                writer.name(key).value(dataSet.get(key));
            }
            writer.endObject();
            writer.close();
            strArguments = sWriter.toString();
            sWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return strArguments;
    }

    public abstract ArrayMap<String, String> getUserBehaviorArguments();

    public abstract int getUserBehaviorPageId();

}
