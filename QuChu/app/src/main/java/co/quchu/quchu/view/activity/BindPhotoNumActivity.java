package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;

/**
 * Created by no21 on 2016/4/19.
 * email:437943145@qq.com
 * desc : 绑定手机号
 */
public class BindPhotoNumActivity extends BaseActivity {
    @Bind(R.id.photo_number)
    EditText photoNumber;
    @Bind(R.id.getAuthCode)
    TextView getAuthCode;
    @Bind(R.id.account_setting_realpwd_hint)
    TextView accountSettingRealpwdHint;
    @Bind(R.id.authCode)
    EditText authCode;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.bindPhotoNumber)
    TextView bindPhotoNumber;

    private static final int AUTH_CODE = 1;
    @Bind(R.id.title_back_iv)
    ImageView titleBackIv;
    @Bind(R.id.title_content_tv)
    TextView titleContentTv;

    private MyHandle handle;
    private static final String mAuthDesc = "秒后重新获取";

    private int mAuthCounter;
    //点击获取验证码后保存

    private String photoNumberCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bind_photo_number);
        ButterKnife.bind(this);
        ininListener();
        titleContentTv.setText("绑定手机号");
    }

    private void ininListener() {

        getAuthCode.setOnClickListener(this);
        bindPhotoNumber.setOnClickListener(this);
        titleBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getAuthCode:
                getAuthCode(photoNumber.getText().toString());
                break;
            case R.id.bindPhotoNumber:
                bindPhotoNumber(authCode.getText().toString().trim(), password.getText().toString().trim());
                break;
        }
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }


    /**
     * 获取验证码
     */
    public void getAuthCode(String PhotoNumber) {

        if (PhotoNumber.trim().length() < 11) {
            Toast.makeText(this, "手机号不合法", Toast.LENGTH_SHORT).show();
            return;
        }
        photoNumberCache = PhotoNumber;
        mAuthCounter = 60;
        handle = new MyHandle();
        getAuthCode.setText("60秒后重新获取");
        getAuthCode.setEnabled(false);
        handle.sendEmptyMessageDelayed(AUTH_CODE, 1000);
        String uri = String.format(NetApi.GetCaptcha, PhotoNumber, "register");
        GsonRequest<Object> request = new GsonRequest<>(uri, Object.class, new ResponseListener<Object>() {
            @Override
            public void onErrorResponse(@Nullable VolleyError error) {
                Toast.makeText(BindPhotoNumActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                handle.removeMessages(AUTH_CODE);
                getAuthCode.setText("再次获取验证码");
                getAuthCode.setEnabled(true);
            }

            @Override
            public void onResponse(Object response, boolean result, @Nullable String exception, @Nullable String msg) {

            }
        });
        request.start(this, null);
    }

    class MyHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == AUTH_CODE) {
                getAuthCode.setText(--mAuthCounter + mAuthDesc);
                if (mAuthCounter < 1) {
                    getAuthCode.setText("再次获取验证码");
                    getAuthCode.setEnabled(true);
                } else {
                    handle.sendEmptyMessageDelayed(AUTH_CODE, 1000);
                }
            }
        }
    }

    public void bindPhotoNumber(String authCode, String password) {
        if (TextUtils.isEmpty(photoNumberCache) || authCode.trim().length() < 4) {
            Toast.makeText(this, "请先获取验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.trim().length() >= 6) {
            Map<String, String> params = new HashMap<>();
            params.put("phone", photoNumberCache);
            params.put("captcha", authCode);
            params.put("password", password);

            GsonRequest<Object> request = new GsonRequest<>(Request.Method.GET, NetApi.bindPhoneNumber, params, Object.class, new ResponseListener<Object>() {
                @Override
                public void onErrorResponse(@Nullable VolleyError error) {
                    Toast.makeText(BindPhotoNumActivity.this, "绑定失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(Object response, boolean result, @Nullable String exception, @Nullable String msg) {
                    if (result) {
                        Toast.makeText(BindPhotoNumActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(BindPhotoNumActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            request.start(BindPhotoNumActivity.this, null);
        } else {
            Toast.makeText(BindPhotoNumActivity.this, "密码长度必须大于六位", Toast.LENGTH_SHORT).show();
        }
    }
}
