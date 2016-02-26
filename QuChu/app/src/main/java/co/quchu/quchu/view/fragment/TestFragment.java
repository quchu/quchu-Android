package co.quchu.quchu.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;

/**
 * RecommendFragment
 * User: Chenhs
 * Date: 2015-12-07
 * 推荐
 */
public class TestFragment extends Fragment {

    @Bind(R.id.back_bottom)
    ImageView backBottom;
    @Bind(R.id.back_top)
    ImageView backTop;
    @Bind(R.id.fragment_flickr_gv)
    RelativeLayout fragmentFlickrGv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.testview, container, false);
        ButterKnife.bind(this, view);
        handler.sendMessageDelayed(handler.obtainMessage(1), 2000);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    long animationDuration = 1 * 1000;

    private void startAnimation() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(backTop, "alpha", 1f, 0.2f);
       ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(backBottom, "alpha", 0.2f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.setDuration(animationDuration);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.playTogether(objectAnimator, objectAnimator2);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                backTop.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
       animatorSet.start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            startAnimation();
        }
    };
}
