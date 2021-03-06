package co.quchu.quchu.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.sina.weibo.sdk.utils.MD5;

import java.util.HashMap;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.presenter.UserLoginPresenter;
import co.quchu.quchu.thirdhelp.UserInfoHelper;
import co.quchu.quchu.utils.StringUtils;

/**
 * Created by no21 on 2016/5/12.
 * email:437943145@qq.com
 * desc :
 */
public class BindPhoneNumDialog extends DialogFragment implements ViewPager.OnPageChangeListener {

    @Bind(R.id.recyclerView)
    ViewPager viewPager;
    private static MyHandle handle;

    private String phoneNumber;
    private TextInputEditText editTextPassword;

    public static BindPhoneNumDialog newInstance() {
        return new BindPhoneNumDialog();
    }

    private String autoCode;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_bind_phone, null);
        ButterKnife.bind(this, view);
        builder.setView(view);
        viewPager.addOnPageChangeListener(this);
        viewPager.setAdapter(new MyPagerAdapter());

        return builder.create();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 2) {
            editTextPassword.requestFocus();
            editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT);
//            FocusEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.restartInput(editTextPassword);
            manager.showSoftInput(editTextPassword, InputMethodManager.SHOW_FORCED);
            manager.showSoftInputFromInputMethod(editTextPassword.getWindowToken(), 0);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_bind_phone_common, container, false);
            container.addView(view);
            setListener(view, position);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void setListener(final View view, int position) {

        ImageView close = (ImageView) view.findViewById(R.id.close);
        TextView title = (TextView) view.findViewById(R.id.title);
        final TextInputEditText editText = (TextInputEditText) view.findViewById(R.id.editText);
        Button common = (Button) view.findViewById(R.id.commonButton);
        final TextInputLayout inputLayout = (TextInputLayout) view.findViewById(R.id.inputLayout);
        Button autoCode = (Button) view.findViewById(R.id.reGetAotoCode);
        ImageView back = (ImageView) view.findViewById(R.id.action_back);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                dismiss();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);

            }
        });

        switch (position) {
            case 0:
                back.setVisibility(View.GONE);
                title.setText("请输入手机号(1/3)");
                inputLayout.setHint("手机号:");
//                FocusEditText.setInputType(InputType.TYPE_CLASS_PHONE);
                common.setText("获取验证码");
                autoCode.setVisibility(View.GONE);
                editText.setKeyListener(DigitsKeyListener.getInstance(getActivity().getString(R.string.filter_phone)));
                editText.setText(phoneNumber);
                editText.requestFocus();
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});

//                InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                manager.restartInput(editText);
//                manager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
//                manager.showSoftInputFromInputMethod(editText.getWindowToken(), 0);

                common.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getAutoCode(editText.getText().toString().trim(), inputLayout);
                    }
                });

                break;
            case 1:
                title.setText("请输入验证码(2/3)");
                inputLayout.setHint("验证码:");
                autoCode.setText("重新获取");
                autoCode.setEnabled(false);
                common.setText("确认");
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                handle = new MyHandle(autoCode);
                editText.setKeyListener(DigitsKeyListener.getInstance(getActivity().getString(R.string.filter_phone)));

                autoCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getAutoCode(phoneNumber, inputLayout);
                    }
                });
                common.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkAutoCode(editText.getText().toString().trim(), inputLayout);
                    }
                });
                break;
            case 2:
                editTextPassword = (TextInputEditText) view.findViewById(R.id.editText);
                title.setText("请创建登陆密码(3/3)");
                inputLayout.setHint("登陆密码:");
                common.setText("确认");
                editTextPassword.setKeyListener(DigitsKeyListener.getInstance(getActivity().getString(R.string.passFilter)));
                autoCode.setVisibility(View.GONE);
                common.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setPassword(editTextPassword.getText().toString(), inputLayout);
                    }
                });
                break;
        }
    }

    private void setPassword(String password, final TextInputLayout layout) {
        if (password.length() < 6 || password.length() > 12) {
            layout.setError("请输入6-12位密码");
            return;
        }
        String format = String.format(Locale.SIMPLIFIED_CHINESE, NetApi.ResertPsw, phoneNumber, MD5.hexdigest(password), autoCode);

//        HashMap<String, String> params = new HashMap<>();
//        params.put("phoneNumber", phoneNumber);
//        params.put("salt", MD5.hexdigest(password));

        GsonRequest<String> request = new GsonRequest<>(format, new ResponseListener<String>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                layout.setError(getString(R.string.network_error));
            }

            @Override
            public void onResponse(String response, boolean result, String errorCode, @Nullable String msg) {
                if (result) {
                    dismiss();
                    UserInfoModel user = AppContext.user;
                    user.setIsphone(true);
                    UserInfoHelper.saveUserInfo(user);
                    Toast.makeText(getActivity(), "绑定成功", Toast.LENGTH_SHORT).show();
                } else {
                    layout.setError(msg);
                }
            }
        });
        request.start(getActivity());
    }

    /**
     * 获取验证码
     */
    private void getAutoCode(String phoneNumber, final TextInputLayout inputLayout) {
        if (!StringUtils.isMobileNO(phoneNumber)) {
            inputLayout.setError("请输入正确的手机号");
            return;
        }
        this.phoneNumber = phoneNumber;
        UserLoginPresenter.getCaptcha(getActivity(), phoneNumber, "register", new ResponseListener<String>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                inputLayout.setError(getString(R.string.network_error));
            }

            @Override
            public void onResponse(String response, boolean result, String errorCode, @Nullable String msg) {
                if (result) {
                    time = 60;
                    viewPager.setCurrentItem(1);
                    if (handle != null) {
                        handle.removeMessages(0);
                        handle.sendEmptyMessage(0);
                    }
                } else {
                    inputLayout.setError("手机号已经被绑定");
                }
            }
        });
    }

    private void checkAutoCode(String code, final TextInputLayout layout) {
        HashMap<String, String> params = new HashMap<>();
        params.put("phoneNumber", phoneNumber);
        params.put("verifyCode", code);

        if (code.length() == 0) {
            layout.setError("验证码有误");
            return;
        } else {
            layout.setError(null);
        }
        autoCode = code;

        GsonRequest<String> request = new GsonRequest<>(NetApi.autoCodeIsCorrect, String.class, params, new ResponseListener<String>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                layout.setError(getString(R.string.network_error));
            }

            @Override
            public void onResponse(String response, boolean result, String errorCode, @Nullable String msg) {
                if (result) {
                    viewPager.setCurrentItem(2);

                } else {
                    layout.setError("验证码有误");
                }

            }
        });
        request.start(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handle != null) {
            handle.removeMessages(0);
        }
    }

    private static int time = 60;

    static class MyHandle extends Handler {
        Button autoCodeButton;

        public MyHandle(Button autoCodeButton) {
            this.autoCodeButton = autoCodeButton;
        }

        @Override
        public void handleMessage(Message msg) {

            if (autoCodeButton != null) {
                if (time > 0) {
                    autoCodeButton.setText("重新获取(" + --time + ")");
                    autoCodeButton.setEnabled(false);
                    handle.sendEmptyMessageDelayed(0, 1000);
                } else {
                    autoCodeButton.setText("获取验证码");
                    autoCodeButton.setEnabled(true);
                    time = 60;
                }
            }
        }
    }


}
