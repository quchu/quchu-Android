package co.quchu.quchu.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;

/**
 * Created by Nico on 16/5/13.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

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

    @OnClick({R.id.tvLoginViaPhone,R.id.tvCreateAccountViaPhone})
    public void onClick(View v) {
        mContainerId = mContainerId == -1? ((ViewGroup)getView().getParent()).getId():mContainerId;
        switch (v.getId()){
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
                break;
            case R.id.tvCreateAccountViaPhone:
                //getFragmentManager().beginTransaction().hide(this).commitAllowingStateLoss();
                getFragmentManager().beginTransaction().setCustomAnimations(
                        R.animator.card_flip_horizontal_right_in,
                        R.animator.card_flip_horizontal_left_out,
                        R.animator.card_flip_horizontal_left_in,
                        R.animator.card_flip_horizontal_right_out)
                        .replace(mContainerId,new RegistrationFragment())
                        .addToBackStack(TAG)
                        .commitAllowingStateLoss();
                getFragmentManager().executePendingTransactions();

                break;
        }
    }
}
