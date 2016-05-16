package co.quchu.quchu.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_login, container, false);
        ButterKnife.bind(this, view);
        rlUserNameField.setTranslationY(200);
        rlPasswordField.setTranslationY(200);
        //rlUserNameField.animate().translationY(200).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        //rlPasswordField.animate().translationY(200).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
