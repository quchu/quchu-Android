package co.quchu.quchu.base;

import android.support.v4.app.Fragment;
import android.view.View;

import com.squareup.leakcanary.RefWatcher;
import com.umeng.analytics.MobclickAgent;

import co.quchu.quchu.R;
import co.quchu.quchu.widget.EmptyView;

/**
 * Created by admin on 2016/3/2.
 */
public class BaseFragment extends Fragment {
    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = AppContext.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getContext());
    }

    private EmptyView emptyView;
    public EmptyView getEmptyView(){
        return emptyView;
    }

    public EmptyView initEmptyView(View v) {

        if (null!=v &&null != v.findViewById(R.id.rlEmptyView)) {
            emptyView = new EmptyView();
            emptyView.init(v.findViewById(R.id.rlEmptyView));
            return emptyView;
        } else {
            return null;
        }
    }
}
