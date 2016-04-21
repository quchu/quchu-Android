package co.quchu.quchu.view.activity;

import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.base.EnhancedToolbar;

/**
 * AboutUsActivity
 * User: Chenhs
 * Date: 2016-01-12
 */
public class AboutUsActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        EnhancedToolbar toolbar = getEnhancedToolbar();
        toolbar.getTitleTv().setText(getTitle());

    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("AboutUsActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("AboutUsActivity");
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
