package co.quchu.quchu.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.activity.UserLoginActivity;

/**
 * UserLoginMainFragment
 * User: Chenhs
 * Date: 2015-11-25
 */
public class UserLoginMainFragment extends Fragment {
    /*   @Bind(R.id.user_login_main_circle_iv)
       ImageView userLoginMainCircleIv;
       @Bind(R.id.user_login_main_cwv)
       CircleWaveView userLoginMainCwv;*/
    @Bind(R.id.user_login_main_weibo_tv)
    TextView userLoginMainWeiboTv;
    @Bind(R.id.user_login_main_weibo_ll)
    LinearLayout userLoginMainWeiboLl;
    @Bind(R.id.user_login_main_wechat_tv)
    TextView userLoginMainWechatTv;
    @Bind(R.id.user_login_main_wechat_ll)
    LinearLayout userLoginMainWechatLl;
    @Bind(R.id.user_login_main_phone_ll)
    LinearLayout userLoginMainPhoneLl;
    @Bind(R.id.user_login_bg_animator_iv)
    ImageView user_login_bg_animator_iv;
    @Bind(R.id.user_login_bg_animators_iv)
    ImageView user_login_bg_animators_iv;
    private View view;
    AnimatorSet animatorSet, animatorSets;
    private long aDuration = 2000L;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_login_main, null);
        ButterKnife.bind(this, view);
        StringUtils.alterTextColor(userLoginMainWeiboTv, 0, 4, R.color.white);
        StringUtils.alterTextColor(userLoginMainWechatTv, 0, 2, R.color.white);


        return view;
    }

    private int RepeatCounts = 0;

    private void userAnimation() {
        animatorSets = new AnimatorSet();
        ObjectAnimator rountAxs = ObjectAnimator.ofFloat(user_login_bg_animators_iv, "scaleX", 0.2f, 0.8f, 0.9f);
        ObjectAnimator rountAys = ObjectAnimator.ofFloat(user_login_bg_animators_iv, "scaleY", 0.2f, 0.8f, 0.9f);
        ObjectAnimator rountAalphas = ObjectAnimator.ofFloat(user_login_bg_animators_iv, "alpha", 0.4f, 0.8f, 0.4f, 0f);
        rountAxs.setRepeatCount(-1);
        rountAys.setRepeatCount(-1);
        rountAalphas.setRepeatCount(-1);
        rountAalphas.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                RepeatCounts++;
                if (RepeatCounts % 3 == 0)
                    LogUtils.json("onAnimationRepeat" + RepeatCounts);
            }
        });

        animatorSets.playTogether(rountAalphas, rountAxs, rountAys);
        animatorSets.setDuration(aDuration - 200);
        animatorSets.setInterpolator(new AccelerateDecelerateInterpolator());

        animatorSet = new AnimatorSet();
        ObjectAnimator rountAx = ObjectAnimator.ofFloat(user_login_bg_animator_iv, "scaleX", 0.2f, 0.8f, 0.9f);
        ObjectAnimator rountAy = ObjectAnimator.ofFloat(user_login_bg_animator_iv, "scaleY", 0.2f, 0.8f, 0.9f);
        ObjectAnimator rountAalpha = ObjectAnimator.ofFloat(user_login_bg_animator_iv, "alpha", 0.6f, 0.8f, 0.5f);

        animatorSet.playTogether(rountAalpha, rountAx, rountAy);
        animatorSet.setDuration(aDuration);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (user_login_bg_animators_iv != null)
                    user_login_bg_animators_iv.setVisibility(View.VISIBLE);
                animatorSets.start();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.user_login_main_phone_ll, R.id.user_login_main_wechat_ll, R.id.user_login_main_weibo_ll})
    public void onLoginClick(View view) {
        switch (view.getId()) {
            case R.id.user_login_main_phone_ll:
                ((UserLoginActivity) getActivity()).mobileNoLogin();
                break;
            case R.id.user_login_main_wechat_ll:
                LogUtils.json("user_login_main_wechat_ll");
                ((UserLoginActivity) getActivity()).weixinLogin();
                break;
            case R.id.user_login_main_weibo_ll:
                LogUtils.json("user_login_main_weibo_ll");
                ((UserLoginActivity) getActivity()).sinaLogin();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        userAnimation();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
