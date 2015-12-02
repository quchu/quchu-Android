package co.quchu.quchu.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import co.quchu.quchu.MainActivity;
import co.quchu.quchu.R;
import co.quchu.quchu.view.activity.MenusActivity;
import co.quchu.quchu.view.activity.PlanetActivity;
import co.quchu.quchu.view.activity.UserLoginActivity;
import co.quchu.quchu.widget.swipbacklayout.SwipeBackActivityBase;
import co.quchu.quchu.widget.swipbacklayout.SwipeBackActivityHelper;
import co.quchu.quchu.widget.swipbacklayout.SwipeBackLayout;
import co.quchu.quchu.widget.swipbacklayout.Utils;

/**
 * BaseActivity
 * User: Chenhs
 * Date: 2015-10-19
 * activity 基类
 */
public class BaseActivity extends AppCompatActivity implements SwipeBackActivityBase {
    private SwipeBackActivityHelper mHelper;
    protected SwipeBackLayout mSwipeBackLayout;

    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
        if (this instanceof MenusActivity) {
            overridePendingTransition(R.anim.in_top_to_bottom,
                    R.anim.in_stable);
        } else {
            overridePendingTransition(R.anim.in_push_right_to_left,
                    R.anim.in_stable);
        }
        //压栈
        ActManager.getAppManager().addActivity(this);

        mSwipeBackLayout = getSwipeBackLayout();
        if (this instanceof MainActivity || this instanceof UserLoginActivity ||this instanceof PlanetActivity) {
            mSwipeBackLayout.setEnableGesture(false);
        } else if (this instanceof MenusActivity) {
            mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_BOTTOM);
            mSwipeBackLayout.setEdgeSize(360);
        } else {
            mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
            mSwipeBackLayout.setEdgeSize(360);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 结束Activity&从堆栈中移除
        if (this instanceof PlanetActivity){
            ActManager.getAppManager().AppExit();
        }else {

        ActManager.getAppManager().finishActivity(this);
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (this instanceof MenusActivity) {
            overridePendingTransition(R.anim.out_bottom_to_top,
                    R.anim.out_bottom_to_top);
        }
  /*      overridePendingTransition(R.anim.in_stable,
                R.anim.out_push_left_to_right);*/
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }


}
