package co.quchu.quchu.view.activityPackage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;

import java.util.ArrayList;
import java.util.List;

import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.widget.planetanimations.Interpolator.BezierInterpolatorData;
import co.quchu.quchu.widget.planetanimations.Interpolator.BezierInterpolators;
import co.quchu.quchu.widget.planetanimations.Interpolator.InterpolatorInterface;
import co.quchu.quchu.widget.planetanimations.Interpolator.MyInterpolator;
import co.quchu.quchu.widget.planetanimations.MovePath;
import co.quchu.quchu.widget.planetanimations.MyAnimation;

/**
 * PlanetActivity
 * User: Chenhs
 * Date: 2015-10-21
 * 我的趣星球
 */
public class PlanetActivity extends BaseActivity {
    ImageView imageView4, planet_avatar_icon, imageView2, imageView3, imageView1, imageView5;
    int heigh = 0;
    int AnimationDuration = 10 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet);
        imageView4 = (ImageView) findViewById(R.id.imageView4);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView5 = (ImageView) findViewById(R.id.imageView5);
        planet_avatar_icon = (ImageView) findViewById(R.id.planet_avatar_icon);
        planet_avatar_icon.getViewTreeObserver();
        ViewTreeObserver vto = planet_avatar_icon.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                heigh = planet_avatar_icon.getHeight();
                planet_avatar_icon.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                myHandler.sendMessageDelayed(myHandler.obtainMessage(0), 3000);
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlanetActivity.this, "imageVIew2 is click", Toast.LENGTH_SHORT).show();
                if (animatorSet!=null){
                    animatorSet.cancel();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        startAnimation();
    }

    AnimatorSet animatorSet;
    Animator.AnimatorListener aal;

    private void startAnimation() {
        final MovePath movePath = new MovePath();
        List animationList = new ArrayList();
        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeigh = dm.heightPixels;
        MyInterpolator mi = new MyInterpolator(new InterpolatorInterface() {
            @Override
            public float setMy(float param) {
                float y = new BezierInterpolatorData().bezierDataWithoutRate(new float[]{0, 1}, new int[]{0}, param);
                return y;
            }
        });
        LogUtils.e((-(screenWidth / 2 - heigh / 2)+10)+ "Math.abs(screenWidth / 4 - heigh*3/4)....iVY=" + imageView2.getY() + "////" + heigh + "screenWidth=" + screenWidth + "/////screenHeigh" + screenHeigh);
        List lis4t = movePath.getCircleData(imageView4, new float[]{0, heigh * 3 / 4});
        List list2 = movePath.getCircleData(imageView2, new float[]{-(screenWidth / 2 - heigh / 2)+40,(heigh / 4)+20});
        List list3 = movePath.getCircleData(imageView3, new float[]{-heigh * 2 / 3, -heigh / 2});
        List list1 = movePath.getCircleData(imageView1, new float[]{20, -heigh});
        List list5 = movePath.getCircleData(imageView5, new float[]{Math.abs((screenWidth / 2 - heigh / 2)-20),0});
        MyAnimation moveAnimation = new MyAnimation();
        animationList.add(moveAnimation.setTranslation(imageView4, (List) lis4t.get(0), (List) lis4t.get(1), AnimationDuration));
        animationList.add(moveAnimation.setTranslation(imageView2, (List) list2.get(0), (List) list2.get(1), AnimationDuration));
        animationList.add(moveAnimation.setTranslation(imageView3, (List) list3.get(0), (List) list3.get(1), AnimationDuration));
        animationList.add(moveAnimation.setTranslation(imageView1, (List) list1.get(0), (List) list1.get(1), AnimationDuration));
        animationList.add(moveAnimation.setTranslation(imageView5, (List) list5.get(0), (List) list5.get(1), AnimationDuration));

        animatorSet = moveAnimation.playTogether(animationList);
        animatorSet.setDuration(AnimationDuration);
        aal = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                animation.addListener(aal);
                LogUtils.json("onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animation.addListener(animation.getListeners().get(0));
                animation.start();
                LogUtils.json("onAnimationEnd");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                LogUtils.json("onAnimationCancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                LogUtils.json("onAnimationRepeat");
            }
        };
//        animatorSet.addListener(aal);
//        animatorSet.setInterpolator(mi);
        animatorSet.setInterpolator( new BezierInterpolators(0.1f, 0.1f,0.1f, 0.1f));
//        AccelerateDecelerateInterpolator adl = new AccelerateDecelerateInterpolator();
        animatorSet.start();

    }

    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    startAnimation();
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
