package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;

import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.widget.CircleWaveView;

/**
 * SplashActivity
 * User: Chenhs
 * Date: 2015-11-10
 */
public class SplashActivity extends BaseActivity {
    private CircleWaveView rippleBackground;
    private ImageView circleIv;
    private ArrayList<Animator> animatorList;
    private AnimatorSet animatorSet;
    private int durationTime=2300;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
         rippleBackground=(CircleWaveView)findViewById(R.id.content);
        circleIv= (ImageView) findViewById(R.id.splash_circle_iv);
/*        rippleBackground.startRippleAnimation();*/
        initCircleAnimation();
        /*handler.sendMessageDelayed(handler.obtainMessage(0x00),3*1000);*/
    }





  public void   initCircleAnimation(){
      animatorList= new ArrayList<Animator>();
      animatorSet = new AnimatorSet();
      final ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(circleIv, "ScaleX", 0.3f, 1.0f);
      scaleXAnimator.setDuration(durationTime);
      animatorList.add(scaleXAnimator);
      final ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(circleIv, "ScaleY",0.3f, 1.0f);
      scaleYAnimator.setDuration(durationTime);
      animatorList.add(scaleYAnimator);
      final ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(circleIv, "Alpha", 0.3f, 1.0f);
      alphaAnimator.setDuration(durationTime);
      animatorList.add(alphaAnimator);

      animatorSet.playTogether(animatorList);
      animatorSet.setInterpolator(new DecelerateInterpolator());
      animatorSet.addListener(new Animator.AnimatorListener() {
          @Override
          public void onAnimationStart(Animator animation) {
              circleIv.setVisibility(View.VISIBLE);
              Message msgs = handler.obtainMessage(0x00);
                handler.sendMessageDelayed(msgs,600);

          }

          @Override
          public void onAnimationEnd(Animator animation) {

          }

          @Override
          public void onAnimationCancel(Animator animation) {

          }

          @Override
          public void onAnimationRepeat(Animator animation) {

          }
      });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x00:
                    rippleBackground.startRippleAnimation();
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (animatorSet !=null)
            animatorSet.start();
    }
}
