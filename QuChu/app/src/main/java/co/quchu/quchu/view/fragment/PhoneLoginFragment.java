package co.quchu.quchu.view.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.presenter.UserLoginPresenter;
import co.quchu.quchu.utils.StringUtils;

/**
 * UserLoginMainFragment
 * User: Chenhs
 * Date: 2015-11-25
 */
public class PhoneLoginFragment extends Fragment {
    @Bind(R.id.phone_login_pnum_et)
    EditText phoneLoginPnumEt;
    @Bind(R.id.phone_login_pnum_ll)
    LinearLayout phoneLoginPnumLl;
    @Bind(R.id.phone_login_password_et)
    EditText phoneLoginPasswordEt;
    @Bind(R.id.phone_login_password_ll)
    LinearLayout phoneLoginPasswordLl;
    @Bind(R.id.authcode_login_password_et)
    EditText authcodeLoginPasswordEt;
    @Bind(R.id.getauthcode_login_tv)
    TextView getauthcodeLoginTv;
    @Bind(R.id.getauthcode_login_rl)
    RelativeLayout getauthcodeLoginRl;
    @Bind(R.id.authcode_login_password_ll)
    LinearLayout authcodeLoginPasswordLl;
    @Bind(R.id.phone_login_enter_tv)
    TextView phoneLoginEnterTv;
    @Bind(R.id.phone_login_progress_iv)
    ImageView phoneLoginProgressIv;
    private View view;
    private AnimationDrawable animationDrawable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_login_phone, null);
        ButterKnife.bind(this, view);
        phoneLoginPnumEt.addTextChangedListener(new PhoneNumWatcher());

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 开始播放帧动画
     */
    private void startUserLoginProgress() {
        phoneLoginProgressIv.setImageResource(R.drawable.user_login_progress);
        animationDrawable = (AnimationDrawable) phoneLoginProgressIv.getDrawable();
        phoneLoginProgressIv.setVisibility(View.VISIBLE);
        animationDrawable.start();
    }

    private void stopUserLoginProgress() {
        if (phoneLoginProgressIv.getVisibility() == View.VISIBLE) {
            animationDrawable = (AnimationDrawable) phoneLoginProgressIv.getDrawable();
            animationDrawable.stop();
            phoneLoginProgressIv.setVisibility(View.GONE);
        }
    }


    private class PhoneNumWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (s.length() >= 11) {
                String phoneNo = s.toString().trim();
                if (phoneNo.length() >= 11) {
                    if (StringUtils.isMobileNO(phoneNo)) {
                        startUserLoginProgress();
                        UserLoginPresenter.decideMobileCanLogin(getActivity(),phoneNo);
                    } else {
                        Toast.makeText(getActivity(), "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                        stopUserLoginProgress();
                    }
                } else {
                    Toast.makeText(getActivity(), "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    stopUserLoginProgress();
                }
            }else {
                stopUserLoginProgress();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
