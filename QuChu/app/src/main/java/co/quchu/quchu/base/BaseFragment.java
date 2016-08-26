package co.quchu.quchu.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.leakcanary.RefWatcher;
import com.umeng.analytics.MobclickAgent;
import com.zhuge.analysis.stat.ZhugeSDK;

import org.json.JSONException;
import org.json.JSONObject;

import co.quchu.quchu.utils.ToastManager;

/**
 * Created by admin on 2016/3/2.
 */
public abstract class BaseFragment extends Fragment {

    private ToastManager toastManager;

    protected abstract String getPageNameCN();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        toastManager = ToastManager.getInstance(getActivity());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = AppContext.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null!=getPageNameCN()){
            MobclickAgent.onPageStart(getPageNameCN());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null!=getPageNameCN()){
            MobclickAgent.onPageEnd(getPageNameCN());
        }
    }

    protected void makeToast(int resId) {
        if (toastManager != null) {
            toastManager.show(resId);
        }
    }

    protected void makeToast(String str) {
        if (toastManager != null) {
            toastManager.show(str);
        }
    }

    protected void startActivity(Class<? extends BaseActivity> clz) {
        startActivity(new Intent(getActivity(), clz));
    }

    protected void UMEvent(String strEventName){
        MobclickAgent.onEvent(getContext(),strEventName);
    }


    protected void ZGEvent(ArrayMap<String, Object> params, String eventName) {

        JSONObject jsonObject = new JSONObject();
        try {
            for (String key : params.keySet()) {
                jsonObject.put(key, params.get(key));
            }
            jsonObject.put("时间", System.currentTimeMillis());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ZhugeSDK.getInstance().track(getActivity(), eventName, jsonObject);
    }
}
