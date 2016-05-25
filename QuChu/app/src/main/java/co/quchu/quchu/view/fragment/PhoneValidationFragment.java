package co.quchu.quchu.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.presenter.UserLoginPresenter;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.widget.ErrorView;

/**
 * Created by Nico on 16/5/13.
 */
public class PhoneValidationFragment extends Fragment {

//    OnRequestVerifySMSListener mCallback;
//    public interface OnRequestVerifySMSListener {
//        void onRequest(int position);
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        // This makes sure that the container activity has implemented
//        // the callback interface. If not, it throws an exception
//        try {
//            mCallback = (OnRequestVerifySMSListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString() + " must implement OnHeadlineSelectedListener");
//        }
//    }


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
    @Bind(R.id.tvNext)
    TextView tvNext;
    @Bind(R.id.errorView)
    ErrorView errorView;
    private boolean mEmptyForum = false;
    private long mRequestTimeStamp = -1;
    private Timer mCountingTimer;

    public void updateButtonStatus(){
        if (null==etUsername){
            return;
        }
        String userName = null==etUsername.getText()?"":etUsername.getText().toString();
        ivIconClear.setVisibility(userName.length()>0?View.VISIBLE:View.INVISIBLE);
        if (!TextUtils.isEmpty(userName)){
            mEmptyForum = false;
            tvNext.setText(R.string.next);
            tvNext.setBackgroundColor(getResources().getColor(R.color.standard_color_yellow));
        }else{
            mEmptyForum = true;
            tvNext.setText(R.string.login);
            tvNext.setBackgroundColor(getResources().getColor(R.color.standard_color_black));
        }
    }

    private boolean verifyForm() {
        boolean status = false;
        String userName = etUsername.getText().toString();

        if(TextUtils.isEmpty(userName) ){
            tvNext.setText(R.string.promote_empty_username_or_password);
            tvNext.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }else if (!StringUtils.isMobileNO(userName)){
            tvNext.setText(R.string.promote_invalid_username);
            tvNext.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }else if(StringUtils.isMobileNO(userName) ){
            tvNext.setBackgroundColor(getResources().getColor(R.color.standard_color_yellow));
            tvNext.setText(R.string.login);
            status = true;
        }else{
            tvNext.setText(R.string.login);
            tvNext.setBackgroundColor(getResources().getColor(R.color.standard_color_black));
        }
        return status;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_validation_phone, container, false);
        ButterKnife.bind(this, view);

        mCountingTimer = new Timer();
        mCountingTimer.schedule(new TimerTask() {
            @Override
            public void run() {

            }
        },1000l);
        mCountingTimer.cancel();


        ivIconClear.setVisibility(View.INVISIBLE);
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateButtonStatus();
            }
        });
        etUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                updateButtonStatus();
            }
        });

        tvSendValidCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mEmptyForum &&verifyForm()){
                    getValidCode();
                }
            }
        });
        return view;
    }

    private void getValidCode(){
        errorView.showLoading();

        UserLoginPresenter.requestRegistrationVerifySms(getActivity(), etUsername.getText().toString(), new UserLoginPresenter.UserNameUniqueListener() {
            @Override
            public void isUnique(JSONObject msg) {
                errorView.hideView();
            }

            @Override
            public void notUnique(String msg) {
                tvNext.setText("用户名已被占用,请尝试其他账号");
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
