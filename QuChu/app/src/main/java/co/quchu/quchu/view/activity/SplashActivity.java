package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

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
        handler.sendMessageDelayed(handler.obtainMessage(0x00),3*1000);
    }



    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0x00)
            {
                startActivity(new Intent(SplashActivity.this,PlanetActivity.class));
                SplashActivity.this.finish();
            }

            super.handleMessage(msg);
        }
    };
}
