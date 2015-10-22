package co.quchu.quchu.widget.planetanimations;

import com.nineoldandroids.animation.Animator;

/**
 * Created by Administrator on 2015/8/17.
 */
public class AnimationOnceListener implements Animator.AnimatorListener{

    private boolean mCanceled;

    public void onAnimationStart(Animator animation) {
        mCanceled = false;

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (!mCanceled) {
//                    animation.start();
        }

    }

    @Override
    public void onAnimationCancel(Animator animation) {
        mCanceled = true;
    }
}
