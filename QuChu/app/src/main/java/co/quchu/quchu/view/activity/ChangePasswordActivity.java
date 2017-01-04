package co.quchu.quchu.view.activity;

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
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.utils.SoftInputUtils;

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

    tvSubmit.setEnabled(false);
    tvSubmit.setSelected(false);

    mEtOldPassword.setOnFocusChangeListener(this);
    mEtNewPassword.setOnFocusChangeListener(this);
    mEtOldPassword.addTextChangedListener(this);
    mEtNewPassword.addTextChangedListener(this);
  }

  @OnClick({R.id.tvSubmit, R.id.userBackgroundLayout})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tvSubmit:
        submitClick();
        break;

      case R.id.userBackgroundLayout:
        SoftInputUtils.hideSoftInput(this);
        break;
    }
  }

  /**
   * 提交
   */
  private void submitClick() {
    DialogUtil.showProgress(this, "正在提交", false);

    String newPassword = mEtNewPassword.getText().toString().trim();
    String originPassword = mEtOldPassword.getText().toString().trim();

    if (!isValid(newPassword, originPassword)) {
      DialogUtil.dismissProgress();
      return;
    }

    HashMap<String, String> params = new HashMap<>();
    params.put("oldPassword", MD5.hexdigest(originPassword));
    params.put("newPassword", MD5.hexdigest(newPassword));
    final GsonRequest<String> request = new GsonRequest<>(NetApi.midiff_password, String.class, params, new ResponseListener<String>() {
      @Override
      public void onErrorResponse(@Nullable VolleyError error) {
        DialogUtil.dismissProgress();
        makeToast(R.string.network_error);
      }

      @Override
      public void onResponse(String response, boolean result, String errorCode, @Nullable String msg) {
        if (!result) {
          tvSubmit.setText(msg);
          tvSubmit.setEnabled(false);
          tvSubmit.setSelected(true);
        } else {
          makeToast("密码修改成功");
          finish();
        }
        DialogUtil.dismissProgress();
      }
    });
    request.start(this);
  }

  private boolean isValid(String newPassword, String originPassword) {
    if (TextUtils.isEmpty(originPassword)) {
      tvSubmit.setText("请输入当前密码");
      tvSubmit.setEnabled(false);
      tvSubmit.setSelected(true);
      return false;
    }

    if (TextUtils.isEmpty(newPassword)) {
      tvSubmit.setText("请输入新密码");
      tvSubmit.setEnabled(false);
      tvSubmit.setSelected(true);
      return false;
    }

    if (newPassword.length() < 6 || newPassword.length() > 12) {
      tvSubmit.setText("密码为6-12位字母或者数字");
      tvSubmit.setEnabled(false);
      tvSubmit.setSelected(true);
      return false;
    }

    if (originPassword.equals(newPassword)) {
      tvSubmit.setText("新旧密码不能相同");
      tvSubmit.setEnabled(false);
      tvSubmit.setSelected(true);
      return false;
    }
    return true;
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
    String newPassword = mEtNewPassword.getText().toString().trim();
    String originPassword = mEtOldPassword.getText().toString().trim();

    if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(originPassword)) {
      tvSubmit.setText("提交");
      tvSubmit.setEnabled(false);
      tvSubmit.setSelected(false);
      return;
    }

    tvSubmit.setText("提交");
    tvSubmit.setEnabled(true);
    tvSubmit.setSelected(true);
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
