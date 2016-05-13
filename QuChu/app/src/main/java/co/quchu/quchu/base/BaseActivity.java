package co.quchu.quchu.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.squareup.leakcanary.RefWatcher;
import com.umeng.analytics.MobclickAgent;

import co.quchu.quchu.R;


/**
 * BaseActivity
 * User: Chenhs
 * Date: 2015-10-19
 * activity 基类
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG = getClass().getName();

    //不做任何动画处理
    protected final int TRANSITION_TYPE_NOTHING = 0;
    //activity向左移动进入
    protected final int TRANSITION_TYPE_LEFT = 1;
    //activity向下移动进入
    protected final int TRANSITION_TYPE_BOTTOM = 2;
    //
    protected final int TRANSITION_TYPE_ALPHA = 3;
    protected final int TRANSITION_TYPE_TOP = 4;

    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //压栈
        ActManager.getAppManager().addActivity(this);
//        getWindow().setStatusBarColor(getColor(R.color.colorPrimaryDark));
//        switch (activitySetup()) {
//            case TRANSITION_TYPE_NOTHING:
//                overridePendingTransition(R.anim.in_push_right_to_left, R.anim.out_push_letf_to_right);
//                break;
//            case TRANSITION_TYPE_ALPHA:
//                overridePendingTransition(R.anim.in_alpha, R.anim.out_alpha);
//                break;
//            case TRANSITION_TYPE_LEFT:
//                overridePendingTransition(R.anim.in_push_right_to_left, R.anim.out_push_letf_to_right);
//                break;
//            case TRANSITION_TYPE_BOTTOM:
//                overridePendingTransition(R.anim.in_top_to_bottom, R.anim.out_bottom_to_top);
//                break;
//            case TRANSITION_TYPE_TOP:
//                overridePendingTransition(R.anim.in_bottom_to_top, R.anim.out_top_to_bottom);
//                break;
//        }

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

    //
    @Override
    public void finish() {
        super.finish();
//        switch (activitySetup()) {
//            case TRANSITION_TYPE_NOTHING:
//                break;
//            case TRANSITION_TYPE_ALPHA:
//                overridePendingTransition(R.anim.in_alpha, R.anim.out_alpha);
//                break;
//            case TRANSITION_TYPE_LEFT:
//                overridePendingTransition(R.anim.in_push_right_to_left, R.anim.out_push_letf_to_right);
//                break;
//            case TRANSITION_TYPE_BOTTOM:
//                overridePendingTransition(R.anim.in_top_to_bottom, R.anim.out_bottom_to_top);
//                break;
//            case TRANSITION_TYPE_TOP:
//                overridePendingTransition(R.anim.in_bottom_to_top, R.anim.out_top_to_bottom);
//                break;
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
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
}
