package co.quchu.quchu.base;

import android.support.v4.app.Fragment;

//import com.squareup.leakcanary.RefWatcher;

/**
 * Created by admin on 2016/3/2.
 */
public class BaseFragment extends Fragment {
    @Override public void onDestroy() {
        super.onDestroy();
//        RefWatcher refWatcher = AppContext.getRefWatcher(getActivity());
//        refWatcher.watch(this);
    }
}
