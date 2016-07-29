package co.quchu.quchu.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import co.quchu.quchu.presenter.UserBehaviorPresentor;


/**
 * Created by Nico on 16/6/2.
 */
public abstract class BaseBehaviorFragment extends BaseFragment {


    @Override
    public void onDestroy() {
        super.onDestroy();
        UserBehaviorPresentor.insertBehavior(getContext(), getUserBehaviorPageId(), "finish", "", System.currentTimeMillis());
    }


    @Override
    public void onPause() {
        super.onPause();
        UserBehaviorPresentor.insertBehavior(getContext(), getUserBehaviorPageId(), "pause", "", System.currentTimeMillis());

    }

    @Override
    public void onResume() {
        super.onResume();
        UserBehaviorPresentor.insertBehavior(getContext(), getUserBehaviorPageId(), "resume", "", System.currentTimeMillis());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        UserBehaviorPresentor.insertBehavior(getContext(), getUserBehaviorPageId(), "enter", getStrUserBehavior(getUserBehaviorArguments()), System.currentTimeMillis());
        return super.onCreateView(inflater, container, savedInstanceState);
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
