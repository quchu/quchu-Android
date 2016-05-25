package co.quchu.quchu.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.thirdhelp.UserLoginListener;
import co.quchu.quchu.thirdhelp.WechatHelper;
import co.quchu.quchu.thirdhelp.WeiboHelper;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.view.activity.LoginActivity;
import co.quchu.quchu.view.activity.RecommendActivity;


/**
 * Created by Nico on 16/5/13.
 */
public class LoginFragment extends Fragment implements View.OnClickListener, UserLoginListener {

    public static final String TAG = "LoginFragment";
    @Bind(R.id.llAuthorizationViaWeibo)
    LinearLayout llAuthorizationViaWeibo;
    @Bind(R.id.llAuthorizationViaMm)
    LinearLayout llAuthorizationViaMm;
    @Bind(R.id.thirdLoginContainer)
    LinearLayout thirdLoginContainer;
    @Bind(R.id.hellWord)
    TextView hellWord;
    @Bind(R.id.tvLoginViaPhone)
    TextView tvLoginViaPhone;
    @Bind(R.id.tvCreateAccountViaPhone)
    TextView tvCreateAccountViaPhone;
    @Bind(R.id.tvForgottenPassword)
    TextView tvForgottenPassword;
    private int mContainerId = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }



    public void sinaLogin() {
        WeiboHelper instance = WeiboHelper.getInstance(getActivity());
        ((LoginActivity)getActivity()).handler = new SsoHandler(getActivity(), instance.getmAuthInfo());
        instance.weiboLogin(((LoginActivity)getActivity()).handler , this, true);
    }

    public void weixinLogin() {
        WechatHelper.getInstance(getActivity()).login(this);
    }


    @OnClick({R.id.tvForgottenPassword,R.id.tvLoginViaPhone,R.id.tvCreateAccountViaPhone,R.id.llAuthorizationViaMm,R.id.llAuthorizationViaWeibo})
    public void onClick(View v) {
        mContainerId = mContainerId == -1? ((ViewGroup)getView().getParent()).getId():mContainerId;
        switch (v.getId()){
            case R.id.tvForgottenPassword:
                getFragmentManager().beginTransaction().setCustomAnimations(
                        R.animator.card_flip_horizontal_right_in,
                        R.animator.card_flip_horizontal_left_out,
                        R.animator.card_flip_horizontal_left_in,
                        R.animator.card_flip_horizontal_right_out)
                        .replace(mContainerId,new PhoneValidationFragment())
                        .addToBackStack(TAG)
                        .commitAllowingStateLoss();
                getFragmentManager().executePendingTransactions();
                ((BaseActivity)getActivity()).getEnhancedToolbar().show();
                break;
            case R.id.llAuthorizationViaMm:
                weixinLogin();
                break;
            case R.id.llAuthorizationViaWeibo:
                sinaLogin();
                break;
            case R.id.tvLoginViaPhone:
                //getFragmentManager().beginTransaction().hide(this).commitAllowingStateLoss();

                getFragmentManager().beginTransaction().setCustomAnimations(
                        R.animator.card_flip_horizontal_right_in,
                        R.animator.card_flip_horizontal_left_out,
                        R.animator.card_flip_horizontal_left_in,
                        R.animator.card_flip_horizontal_right_out)
                        .replace(mContainerId,new LoginByPhoneFragment())
                        .addToBackStack(TAG)
                        .commitAllowingStateLoss();
                getFragmentManager().executePendingTransactions();
                ((BaseActivity)getActivity()).getEnhancedToolbar().show();
                break;
            case R.id.tvCreateAccountViaPhone:
                //getFragmentManager().beginTransaction().hide(this).commitAllowingStateLoss();
                getFragmentManager().beginTransaction().setCustomAnimations(
                        R.animator.card_flip_horizontal_right_in,
                        R.animator.card_flip_horizontal_left_out,
                        R.animator.card_flip_horizontal_left_in,
                        R.animator.card_flip_horizontal_right_out)
                        .replace(mContainerId,new PhoneValidationFragment())
                        .addToBackStack(TAG)
                        .commitAllowingStateLoss();
                getFragmentManager().executePendingTransactions();
                ((BaseActivity)getActivity()).getEnhancedToolbar().show();

                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity)getActivity()).getEnhancedToolbar().hide();
    }

    @Override
    public void loginSuccess(int type, String token, String appId) {
        startActivity(new Intent(getActivity(), RecommendActivity.class));
        EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_USER_LOGIN_SUCCESS));
    }

    @Override
    public void loginFail(String message) {

    }

}
