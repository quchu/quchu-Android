package co.quchu.quchu.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.utils.StringUtils;

/**
 * Created by Nico on 16/5/13.
 */
public class LoginByPhoneFragment extends Fragment {
    @Bind(R.id.ivIconUserName)
    ImageView ivIconUserName;
    @Bind(R.id.etUsername)
    EditText etUsername;
    @Bind(R.id.ivIconClear)
    ImageView ivIconClear;
    @Bind(R.id.rlUserNameField)
    RelativeLayout rlUserNameField;
    @Bind(R.id.ivIconPassword)
    ImageView ivIconPassword;
    @Bind(R.id.etPassword)
    EditText etPassword;
    @Bind(R.id.ivSwitchVisible)
    ImageView ivSwitchVisible;
    @Bind(R.id.tvLoginViaPhone)
    TextView tvLoginViaPhone;
    @Bind(R.id.rlPasswordField)
    RelativeLayout rlPasswordField;

    public static final String TAG = "LoginByPhoneFragment";
    public boolean mDisplayPassword = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_login, container, false);
        ButterKnife.bind(this, view);
        rlUserNameField.setTranslationY(200);
        rlPasswordField.setTranslationY(200);
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                ivIconClear.setVisibility(s.length()>0?View.VISIBLE:View.INVISIBLE);
                if (!StringUtils.isMobileNO(s.toString())){
                    tvLoginViaPhone.setText(R.string.promote_invalid_username);
                    tvLoginViaPhone.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }else{
                    restoreLoginButton();
                }
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!StringUtils.isGoodPassword(s.toString())){
                    tvLoginViaPhone.setText(R.string.promote_invalid_username);
                    tvLoginViaPhone.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }else{
                    restoreLoginButton();
                }
            }
        });




        ivIconClear.setVisibility(View.INVISIBLE);
        ivIconClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUsername.setText("");
            }
        });
        ivSwitchVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mDisplayPassword){
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else{
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                mDisplayPassword = !mDisplayPassword;
            }
        });

        //rlUserNameField.animate().translationY(200).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        //rlPasswordField.animate().translationY(200).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        return view;
    }

    private void restoreLoginButton() {
        tvLoginViaPhone.setText(R.string.login);
        tvLoginViaPhone.setBackgroundColor(getResources().getColor(R.color.standard_color_black));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
