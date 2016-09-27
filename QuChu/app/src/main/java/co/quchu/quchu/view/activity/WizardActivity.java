package co.quchu.quchu.view.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;

/**
 * Created by Nico on 16/9/20.
 */
public class WizardActivity extends BaseActivity {

  @Bind(R.id.ivFemale) ImageView ivFemale;
  @Bind(R.id.ivMale) ImageView ivMale;
  @Bind(R.id.ivLogo) ImageView ivLogo;
  @Bind(R.id.tvTitle) TextView tvTitle;
  @Bind(R.id.vBoldDivider) View vBoldDivider;
  @Bind(R.id.tvSubTitle) TextView tvSubTitle;
  @Bind(R.id.llTextArea) LinearLayout llTextArea;
  @Bind(R.id.tvNext) TextView tvNext;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_wizard);
    ButterKnife.bind(this);

    tvNext.setAlpha(0);
    ivFemale.setAlpha(0f);
    ivMale.setAlpha(0f);
    ivLogo.getViewTreeObserver()
        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override public void onGlobalLayout() {
            ivLogo.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            animateLogo();
          }
        });
  }

  private void animateLogo() {
    ivLogo.setAlpha(0f);
    llTextArea.setAlpha(0);
    //ivLogo.setTranslationY(-ivLogo.getTop());

    ObjectAnimator scaleX = ObjectAnimator.ofFloat(ivLogo, "scaleX", .5f,1);
    ObjectAnimator scaleY = ObjectAnimator.ofFloat(ivLogo, "scaleY", .5f,1);
    scaleX.setInterpolator(new OvershootInterpolator(1.5f));
    scaleY.setInterpolator(new OvershootInterpolator(1.5f));
    ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(ivLogo, "alpha", 0, 1);
    alphaAnimator.setDuration(500);
    alphaAnimator.setInterpolator(new LinearInterpolator());
    final AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.playTogether(scaleX, scaleY, alphaAnimator);
    animatorSet.setDuration(1000);
    animatorSet.setStartDelay(100);
    animatorSet.start();
    animateText(true);
    ivLogo.postDelayed(new Runnable() {
      @Override public void run() {
        animateText(false);
      }
    }, 2500);

    ivLogo.postDelayed(new Runnable() {
      @Override public void run() {
        animateLast();
        animateText(true);
      }
    }, 3500);
  }

  private void animateLast() {

    tvTitle.setText("搭伙功能");
    tvSubTitle.setText("现在你可以约和你有相同基因的用\n户一起去High啦");
    ivFemale.setTranslationX(-400);
    ivMale.setTranslationX(400);
    ObjectAnimator alphaFemale = ObjectAnimator.ofFloat(ivFemale, "alpha", 0, 1);
    ObjectAnimator alphaMale = ObjectAnimator.ofFloat(ivMale, "alpha", 0, 1);
    ObjectAnimator alphaNext = ObjectAnimator.ofFloat(tvNext, "alpha", 0, 1);
    alphaNext.setStartDelay(500);
    ObjectAnimator translationXFemale = ObjectAnimator.ofFloat(ivFemale, "translationX", -400, 0);
    ObjectAnimator translationXMale = ObjectAnimator.ofFloat(ivMale, "translationX", 400, 0);
    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.playTogether(alphaFemale, alphaMale, translationXFemale, translationXMale,alphaNext);
    animatorSet.setDuration(1000);
    animatorSet.start();

    ObjectAnimator floatingAnimation = ObjectAnimator.ofFloat(tvNext,"translationY",20);
    floatingAnimation.setRepeatCount(ValueAnimator.INFINITE);
    floatingAnimation.setRepeatMode(ValueAnimator.REVERSE);
    floatingAnimation.setInterpolator(new DecelerateInterpolator());
    floatingAnimation.setDuration(1500);
    floatingAnimation.setStartDelay(500);
    floatingAnimation.start();

    tvNext.postDelayed(new Runnable() {
      @Override
      public void run() {
        startActivity(new Intent(WizardActivity.this, RecommendActivity.class));
        finish();
      }
    }, 3000);

//    tvNext.setOnClickListener(new View.OnClickListener() {
//      @Override public void onClick(View view) {
//        startActivity(new Intent(WizardActivity.this, RecommendActivity.class));
//        tvNext.postDelayed(new Runnable() {
//          @Override public void run() {
//            finish();
//          }
//        },300);
//      }
//    });
  }

  private void animateText(boolean inOrOut) {

    tvTitle.setTranslationX(inOrOut ? 200 : 0);
    vBoldDivider.setTranslationX(inOrOut ? 250 : 0);
    tvSubTitle.setTranslationX(inOrOut ? 300 : 0);

    ObjectAnimator translationXTitle =
        ObjectAnimator.ofFloat(tvTitle, "translationX", inOrOut ? tvTitle.getTranslationX() : 0,
            inOrOut ? 0 : -200);
    translationXTitle.setStartDelay(100);
    ObjectAnimator translationXDivider = ObjectAnimator.ofFloat(vBoldDivider, "translationX",
        inOrOut ? vBoldDivider.getTranslationX() : 0, inOrOut ? 0 : -250);
    translationXDivider.setStartDelay(200);
    ObjectAnimator translationXSubTitle = ObjectAnimator.ofFloat(tvSubTitle, "translationX",
        inOrOut ? tvSubTitle.getTranslationX() : 0, inOrOut ? 0 : -300);
    translationXSubTitle.setStartDelay(300);
    ObjectAnimator textAreaAlpha =
        ObjectAnimator.ofFloat(llTextArea, "alpha", inOrOut ? 0 : 1, inOrOut ? 1 : 0);
    textAreaAlpha.setDuration(500);

    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.playTogether(translationXTitle, translationXSubTitle, translationXDivider,
        textAreaAlpha);
    animatorSet.setDuration(500);
    animatorSet.start();
    animatorSet.setStartDelay(500);
    if (!inOrOut){

      ObjectAnimator alphaLogo = ObjectAnimator.ofFloat(ivLogo, "alpha", 1, 0);
      alphaLogo.setDuration(500);
      alphaLogo.start();
      alphaLogo.setStartDelay(500);
    }


  }

  @Override protected int activitySetup() {
    return TRANSITION_TYPE_LEFT;
  }

  @Override protected String getPageNameCN() {
    return null;
  }
}
