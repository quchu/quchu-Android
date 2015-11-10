package co.quchu.quchu.view.activity;

import android.os.Bundle;

import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.widget.CircleWaveView;

/**
 * SplashActivity
 * User: Chenhs
 * Date: 2015-11-10
 */
public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final CircleWaveView rippleBackground=(CircleWaveView)findViewById(R.id.content);
        rippleBackground.startRippleAnimation();
    }

}
