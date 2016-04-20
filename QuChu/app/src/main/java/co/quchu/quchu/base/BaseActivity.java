package co.quchu.quchu.base;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.squareup.leakcanary.RefWatcher;
import com.umeng.analytics.MobclickAgent;

import co.quchu.quchu.R;
import co.quchu.quchu.photoselected.PreviewImageActivity;
import co.quchu.quchu.widget.MoreButtonView;
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
public abstract class BaseActivity extends AppCompatActivity implements SwipeBackActivityBase, View.OnClickListener, MoreButtonView.MoreClicklistener {
    private SwipeBackActivityHelper mHelper;
    protected SwipeBackLayout mSwipeBackLayout;
    protected String TAG = getClass().getName();

    //不做任何动画处理
    protected final int TRANSITION_TYPE_NOTHING = 0;
    //activity向左移动进入
    protected final int TRANSITION_TYPE_LEFT = 1;
    //activity向下移动进入
    protected final int TRANSITION_TYPE_BOTTOM = 2;
    //
    protected final int TRANSITION_TYPE_ALPHA = 3;

    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //压栈
        ActManager.getAppManager().addActivity(this);

        switch (activitySetup()) {
            case TRANSITION_TYPE_NOTHING:
                break;
            case TRANSITION_TYPE_ALPHA:
                overridePendingTransition(R.anim.in_alpha, R.anim.out_alpha);
                break;
            case TRANSITION_TYPE_LEFT:
                overridePendingTransition(R.anim.in_push_right_to_left, R.anim.in_stable);
                mHelper = new SwipeBackActivityHelper(this);
                mHelper.onActivityCreate();
                mSwipeBackLayout = getSwipeBackLayout();
                mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
                mSwipeBackLayout.setEdgeSize(360);
                break;
            case TRANSITION_TYPE_BOTTOM:
                overridePendingTransition(R.anim.in_push_right_to_left, R.anim.in_stable);
                mHelper = new SwipeBackActivityHelper(this);
                mHelper.onActivityCreate();
                mSwipeBackLayout = getSwipeBackLayout();
                mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_BOTTOM);
                mSwipeBackLayout.setEdgeSize(360);
                break;
        }

    }

    /**
     * activity 切换的时候调用,默认不执行动画处理
     */
    protected abstract int activitySetup();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = AppContext.getRefWatcher(getApplicationContext());
        refWatcher.watch(this);

        ActManager.getAppManager().finishActivity(this);
    }

    @Override
    public void finish() {
        super.finish();

//        if (this instanceof MenusActivity) {
//            overridePendingTransition(R.anim.out_bottom_to_top,
//                    R.anim.out_bottom_to_top);
//        } else
        if (this instanceof PreviewImageActivity) {
            overridePendingTransition(R.anim.in_alpha,
                    R.anim.out_alpha);
        } else {
            overridePendingTransition(R.anim.in_stable,
                    R.anim.out_push_left_to_right);
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
//        if (this instanceof MenusActivity) {
//            overridePendingTransition(R.anim.in_top_to_bottom,
//                    R.anim.in_stable);
//        }
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
        this.findViewById(R.id.title_back_iv).setOnClickListener(this);
        MoreButtonView mvv = (MoreButtonView) this.findViewById(R.id.title_more_rl);
        mvv.setMoreClick(this);
    }

    public EnhancedToolbar getEnhancedToolbar() {
        return null == enhancedToolbar ? initToolbar() : enhancedToolbar;
    }

    private EnhancedToolbar enhancedToolbar;

    private EnhancedToolbar initToolbar() {
        if (null != findViewById(R.id.enhancedToolbar)) {
            enhancedToolbar = (EnhancedToolbar) findViewById(R.id.enhancedToolbar);
            enhancedToolbar.getLeftIv().setImageResource(R.mipmap.ic_back);
            enhancedToolbar.getLeftIv().setScaleType(ImageView.ScaleType.CENTER);
            enhancedToolbar.getLeftIv().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            enhancedToolbar.getTitleTv().setText(getTitle());
            setSupportActionBar(enhancedToolbar);
            return enhancedToolbar;
        } else {
            return null;
        }
    }

    public void enableRightButton() {
        if (null != this.findViewById(R.id.title_more_rl))
            this.findViewById(R.id.title_more_rl).setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back_iv:
                this.finish();

                break;
        }
    }

    @Override
    public void moreClick() {
//        ActManager.getAppManager().Back2MenusAct();
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
