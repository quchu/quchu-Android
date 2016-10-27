package co.quchu.quchu.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.sina.weibo.sdk.utils.MD5;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;

/**
 * 修改密码
 * <p>
 * Created by mwb on 16/10/27.
 */
public class ChangePasswordActivity extends BaseBehaviorActivity implements View.OnFocusChangeListener, TextWatcher {

  @Bind(R.id.etOldPassword) EditText mEtOldPassword;
  @Bind(R.id.etNewPassword) EditText mEtNewPassword;
  @Bind(R.id.tvSubmit) TextView tvSubmit;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_change_password);
    ButterKnife.bind(this);

    EnhancedToolbar toolbar = getEnhancedToolbar();
    TextView textView = toolbar.getTitleTv();
    textView.setText("修改密码");

    mEtOldPassword.setOnFocusChangeListener(this);
    mEtNewPassword.setOnFocusChangeListener(this);
    mEtOldPassword.addTextChangedListener(this);
    mEtNewPassword.addTextChangedListener(this);
  }

  @OnClick(R.id.tvSubmit)
  public void onClick() {
    String newPassword =  mEtNewPassword.getText().toString().trim();
    String originPassword = mEtOldPassword.getText().toString().trim();

    if (TextUtils.isEmpty(originPassword)) {
      tvSubmit.setText("请输入当前密码");
      tvSubmit.setBackgroundColor(getResources().getColor(R.color.standard_color_red));
      return;
    }

    if (TextUtils.isEmpty(newPassword)) {
      tvSubmit.setText("请输入新密码");
      tvSubmit.setBackgroundColor(getResources().getColor(R.color.standard_color_red));
      return;
    }

    if (newPassword.length() < 6 || newPassword.length() > 12) {
      tvSubmit.setText("密码为6-12位字母或者数字");
      tvSubmit.setBackgroundColor(getResources().getColor(R.color.standard_color_red));
      return;
    }

    if (originPassword.equals(newPassword)) {
      tvSubmit.setText("新旧密码不能相同");
      tvSubmit.setBackgroundColor(getResources().getColor(R.color.standard_color_red));
      return;
    }

    HashMap<String, String> params = new HashMap<>();
    params.put("oldPassword", MD5.hexdigest(originPassword));
    params.put("newPassword", MD5.hexdigest(newPassword));
    final GsonRequest<String> request = new GsonRequest<>(NetApi.midiff_password, String.class, params, new ResponseListener<String>() {
      @Override
      public void onErrorResponse(@Nullable VolleyError error) {
        makeToast(R.string.network_error);
      }

      @Override
      public void onResponse(String response, boolean result, String errorCode, @Nullable String msg) {
        if (!result) {
          tvSubmit.setText(msg);
          tvSubmit.setBackgroundColor(getResources().getColor(R.color.standard_color_red));
        } else {
          makeToast("密码修改成功");
        }
      }
    });
    request.start(this);
  }

  @Override
  public void onFocusChange(View v, boolean hasFocus) {
    updateButtonStatus();
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {

  }

  @Override
  public void afterTextChanged(Editable s) {
    updateButtonStatus();
  }

  public void updateButtonStatus() {
    String newPassword =  mEtNewPassword.getText().toString().trim();
    String originPassword = mEtOldPassword.getText().toString().trim();

    if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(originPassword)) {
      tvSubmit.setText("提交");
      tvSubmit.setBackgroundColor(Color.parseColor("#dbdbdb"));
      return;
    }

    tvSubmit.setText("提交");
    tvSubmit.setBackgroundColor(getResources().getColor(R.color.standard_color_yellow));
  }

  @Override
  public ArrayMap<String, Object> getUserBehaviorArguments() {
    return null;
  }

  @Override
  public int getUserBehaviorPageId() {
    return 0;
  }

  @Override
  protected int activitySetup() {
    return TRANSITION_TYPE_LEFT;
  }

  @Override
  protected String getPageNameCN() {
    return null;
  }
}
