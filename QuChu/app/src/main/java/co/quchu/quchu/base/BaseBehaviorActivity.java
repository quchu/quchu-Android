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

    private String getStrUserBehavior(ArrayMap<String, Object> dataSet) {
        if (null== dataSet){
            return "";
        }
        String strArguments = "";
        StringWriter sWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(sWriter);

        try {
            writer.beginObject();
            for (String key : dataSet.keySet()) {

                if (dataSet.get(key) instanceof Integer){
                    writer.name(key).value((Integer) dataSet.get(key));
                }else if(dataSet.get(key) instanceof Double){
                    writer.name(key).value((Double) dataSet.get(key));
                }else if(dataSet.get(key) instanceof Boolean){
                    writer.name(key).value((Boolean) dataSet.get(key));
                }else if(dataSet.get(key) instanceof Long){
                    writer.name(key).value((Long) dataSet.get(key));
                }else{
                    writer.name(key).value((String) dataSet.get(key));
                }

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

    public abstract ArrayMap<String, Object> getUserBehaviorArguments();

    public abstract int getUserBehaviorPageId();

}
