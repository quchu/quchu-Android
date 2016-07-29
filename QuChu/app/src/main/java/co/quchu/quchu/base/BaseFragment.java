package co.quchu.quchu.base;

import android.support.v4.app.Fragment;
import android.view.View;

import com.squareup.leakcanary.RefWatcher;
import com.umeng.analytics.MobclickAgent;


import co.quchu.quchu.R;

/**
 * Created by admin on 2016/3/2.
 */
public abstract class BaseFragment extends Fragment {

    protected abstract String getPageNameCN();


    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = AppContext.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getPageNameCN());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getPageNameCN());
    }


}
