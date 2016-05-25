package co.quchu.quchu.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.presenter.UserLoginPresenter;
import co.quchu.quchu.widget.ErrorView;

/**
 * Created by Nico on 16/5/13.
 */
public class PhoneValidationFragment extends Fragment {

    public static final String TAG = "PhoneValidationFragment";
    @Bind(R.id.ivIconUserName)
    ImageView ivIconUserName;
    @Bind(R.id.etUsername)
    EditText etUsername;
    @Bind(R.id.ivIconClear)
    ImageView ivIconClear;
    @Bind(R.id.rlUserNameField)
    RelativeLayout rlUserNameField;
    @Bind(R.id.ivIconValidCode)
    ImageView ivIconValidCode;
    @Bind(R.id.etValidCode)
    EditText etValidCode;
    @Bind(R.id.tvSendValidCode)
    TextView tvSendValidCode;
    @Bind(R.id.rlValidCode)
    RelativeLayout rlValidCode;
    @Bind(R.id.tvLoginViaPhone)
    TextView tvLoginViaPhone;
    @Bind(R.id.errorView)
    ErrorView errorView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_validation_phone, container, false);
        ButterKnife.bind(this, view);
        tvSendValidCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //errorView.showLoading();
                getValidCode();
            }
        });
        return view;
    }

    private void getValidCode(){
        errorView.showLoading();

        UserLoginPresenter.getCaptcha(getActivity(), etUsername.getText().toString(), UserLoginPresenter.getCaptcha_regiest, new UserLoginPresenter.UserNameUniqueListener() {
            @Override
            public void isUnique(JSONObject msg) {
                errorView.hideView();
            }

            @Override
            public void notUnique(String msg) {
                errorView.hideView();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
