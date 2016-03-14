package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.PostCardItemModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.view.fragment.PostCardDetailFg;
import co.quchu.quchu.view.fragment.PostCardPhotoFg;
import co.quchu.quchu.widget.AnimationViewPager.Rotate3dAnimation;

/**
 * PostCardFromImageActivity
 * User: Chenhs
 * Date: 2016-03-11
 * 明信片界面 （从相册跳转）
 */
public class PostCardFromImageActivity extends BaseActivity {


    @Bind(R.id.title_content_tv)
    TextView mTitleContentTv;
    @Bind(R.id.recommend_body_fl_one)
    FrameLayout recommendBodyFlOne;
    @Bind(R.id.recommend_body_fl_two)
    FrameLayout recommendBodyFlTwo;
    @Bind(R.id.animation_root)
    RelativeLayout animationRoot;
    private PostCardItemModel model;
    private PostCardPhotoFg photoFragment;
    private PostCardDetailFg detailFragment;
    private int indexView = 0;
    private int centerX;
    private int centerY;
    private int depthZ = 310;
    private int duration = 260;
    private boolean isOpen = true;
    private Rotate3dAnimation openAnimation, closeAnimation, rotateAnimationTwo, rotateAnimationOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postcard_from_image);
        ButterKnife.bind(this);
        initTitleBar();
        mTitleContentTv.setText(getTitle());
        initData();
    }


    public void flipOver() {
        centerX = animationRoot.getWidth() / 2;
        centerY = animationRoot.getHeight() / 2;
        if (openAnimation == null) {
            initOpenAnim();
            initCloseAnim();
        }

        if (openAnimation.hasStarted() && !openAnimation.hasEnded()) {
            return;
        }
        if (closeAnimation.hasStarted() && !closeAnimation.hasEnded()) {
            return;
        }

        if (isOpen) {
            recommendBodyFlOne.startAnimation(openAnimation);
        } else {
            recommendBodyFlTwo.startAnimation(closeAnimation);
        }

        isOpen = !isOpen;
    }


    /*
   * 初始化ViewPager
   */
    public void InitViewPager() {
        photoFragment = PostCardPhotoFg.newInstance(model);
        detailFragment = PostCardDetailFg.newInstance(model);
        getSupportFragmentManager().beginTransaction().add(R.id.recommend_body_fl_one, photoFragment).commitAllowingStateLoss();
        getSupportFragmentManager().beginTransaction().add(R.id.recommend_body_fl_two, detailFragment).commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


    private void initOpenAnim() {
        openAnimation = new Rotate3dAnimation(0, 90, centerX, centerY, depthZ, true);
        openAnimation.setDuration(duration);
        openAnimation.setFillAfter(true);
        openAnimation.setInterpolator(new DecelerateInterpolator());
        openAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                recommendBodyFlOne.setVisibility(View.GONE);
                recommendBodyFlTwo.setVisibility(View.VISIBLE);
                if (null == rotateAnimationTwo)
                    rotateAnimationTwo = new Rotate3dAnimation(270, 360, centerX, centerY, depthZ, false);
                rotateAnimationTwo.setDuration(duration);
                rotateAnimationTwo.setFillAfter(true);
                rotateAnimationTwo.setInterpolator(new AccelerateInterpolator());
                recommendBodyFlTwo.startAnimation(rotateAnimationTwo);
            }
        });
    }


    private void initCloseAnim() {
        closeAnimation = new Rotate3dAnimation(360, 270, centerX, centerY, depthZ, true);
        closeAnimation.setDuration(duration);
        closeAnimation.setFillAfter(true);
        closeAnimation.setInterpolator(new DecelerateInterpolator());
        closeAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                recommendBodyFlOne.setVisibility(View.VISIBLE);
                recommendBodyFlTwo.setVisibility(View.GONE);


                if (null == rotateAnimationOne)
                    rotateAnimationOne = new Rotate3dAnimation(90, 0, centerX, centerY, depthZ, false);
                rotateAnimationOne.setDuration(duration);
                rotateAnimationOne.setFillAfter(true);
                rotateAnimationOne.setInterpolator(new AccelerateInterpolator());
                recommendBodyFlOne.startAnimation(rotateAnimationOne);
            }
        });
    }


    private void initData() {
        int postCardId = getIntent().getIntExtra("postCardId", 0);
        GsonRequest<PostCardItemModel> request = new GsonRequest<>(String.format(NetApi.getCardDetail, postCardId), PostCardItemModel.class, new ResponseListener<PostCardItemModel>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
            }

            @Override
            public void onResponse(PostCardItemModel response, boolean result, @Nullable String exception, @Nullable String msg) {
                model = response;
                InitViewPager();
            }
        });
        request.start(this, null);
    }
}
