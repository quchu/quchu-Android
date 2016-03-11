package co.quchu.quchu.base;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

import co.quchu.quchu.R;
import co.quchu.quchu.photo.previewimage.PreviewImage;
import co.quchu.quchu.view.activity.MenusActivity;
import co.quchu.quchu.view.activity.PlaceMapActivity;
import co.quchu.quchu.view.activity.PostcarDetailActivity;
import co.quchu.quchu.view.activity.RecommendActivity;
import co.quchu.quchu.view.activity.ReserveActivity;
import co.quchu.quchu.view.activity.SplashActivity;
import co.quchu.quchu.view.activity.UserLoginActivity;
import co.quchu.quchu.widget.MoreButtonView;
import co.quchu.quchu.widget.swipbacklayout.SwipeBackActivityBase;
import co.quchu.quchu.widget.swipbacklayout.SwipeBackActivityHelper;
import co.quchu.quchu.widget.swipbacklayout.SwipeBackLayout;
import co.quchu.quchu.widget.swipbacklayout.Utils;

//import com.squareup.leakcanary.RefWatcher;

/**
 * BaseActivity
 * User: Chenhs
 * Date: 2015-10-19
 * activity 基类
 */
public class BaseActivity extends AppCompatActivity implements SwipeBackActivityBase, View.OnClickListener, MoreButtonView.MoreClicklistener {
    private SwipeBackActivityHelper mHelper;
    protected SwipeBackLayout mSwipeBackLayout;
    protected String TAG = getClass().getName();


    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!(this instanceof PostcarDetailActivity)) {
            if (this instanceof PreviewImage) {
                overridePendingTransition(R.anim.in_alpha,
                        R.anim.out_alpha);
            } else {
                overridePendingTransition(R.anim.in_push_right_to_left,
                        R.anim.in_stable);
            }
        }
        //压栈
        ActManager.getAppManager().addActivity(this);


        if (this instanceof UserLoginActivity || this instanceof RecommendActivity || this instanceof SplashActivity || this instanceof PostcarDetailActivity
                || this instanceof PreviewImage || this instanceof ReserveActivity || this instanceof PlaceMapActivity) {
//            mSwipeBackLayout.setEnableGesture(false);
        } else if (this instanceof MenusActivity) {
            mHelper = new SwipeBackActivityHelper(this);
            mHelper.onActivityCreate();
            mSwipeBackLayout = getSwipeBackLayout();

            mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_BOTTOM);
            mSwipeBackLayout.setEdgeSize(360);
        } else {
            mHelper = new SwipeBackActivityHelper(this);
            mHelper.onActivityCreate();
            mSwipeBackLayout = getSwipeBackLayout();

            mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
            mSwipeBackLayout.setEdgeSize(360);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActManager.getAppManager().finishActivity(this);
    }

    @Override
    public void finish() {
        super.finish();

        if (!(this instanceof PostcarDetailActivity)) {

            if (this instanceof MenusActivity) {
                overridePendingTransition(R.anim.out_bottom_to_top,
                        R.anim.out_bottom_to_top);
            } else if (this instanceof PreviewImage) {
                overridePendingTransition(R.anim.in_alpha,
                        R.anim.out_alpha);
            } else {
                overridePendingTransition(R.anim.in_stable,
                        R.anim.out_push_left_to_right);
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this instanceof MenusActivity) {
            overridePendingTransition(R.anim.in_top_to_bottom,
                    R.anim.in_stable);
        }
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mHelper != null)
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

    /**
     * title bar 控制  start
     */
    public void initTitleBar() {
        this.findViewById(R.id.title_back_rl).setOnClickListener(this);
        MoreButtonView mvv = (MoreButtonView) this.findViewById(R.id.title_more_rl);
        mvv.setMoreClick(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back_rl:
                this.finish();
                break;
        }
    }

    @Override
    public void moreClick() {
        //   this.startActivity(new Intent(this, MenusActivity.class));
        ActManager.getAppManager().Back2MenusAct();
    }

    /**
     * title bar 控制  end
     */

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }


}
