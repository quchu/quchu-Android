package co.quchu.quchu.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;

/**
 * Created by no21 on 2016/5/11.
 * email:437943145@qq.com
 * desc :通用的,对话框
 */
public class CommonDialog extends DialogFragment implements View.OnClickListener {

  private final int TIME_OUT_TASK = 1;

  public static final int CLICK_ID_PASSIVE = 1;
  public static final int CLICK_ID_ACTIVE = 2;
  public static final int CLICK_ID_SUBBUTTON = 3;
  @Bind(R.id.title) TextView title;
  @Bind(R.id.searchFilterContainer) TextView searchFilterContainer;
  @Bind(R.id.passive) Button passive;
  @Bind(R.id.active) Button active;
  @Bind(R.id.common_dialog_btn_layout) LinearLayout commonDialogBtnLayout;
  @Bind(R.id.subButton) Button subButton;

  private OnActionListener listener;
  //隐藏按钮
  private boolean hideButton;

  private static final String KEY_TITLE = "title";
  private static final String KEY_SUBTITLE = "subTitle";
  private static final String KEY_ACTIVE = "sactive";
  private static final String KEY_PASSIVE = "passive";
  private static final String KEY_SUBBUTTON = "subButton";
  private static final String KEY_HIDE_BUTTON = "showButton";

  public static CommonDialog newInstance(@NonNull String title, @NonNull String subTitle,
      @NonNull boolean hideButton) {
    Bundle bundle = new Bundle();
    bundle.putString(KEY_TITLE, title);
    bundle.putString(KEY_SUBTITLE, subTitle);
    bundle.putBoolean(KEY_HIDE_BUTTON, hideButton);
    CommonDialog dialog = new CommonDialog();
    dialog.setArguments(bundle);
    return dialog;
  }

  public static CommonDialog newInstance(@NonNull String title, @NonNull String subTitle,
      @NonNull String activeName, @Nullable String passiveName, @Nullable String sunButton) {
    Bundle bundle = new Bundle();
    bundle.putString(KEY_TITLE, title);
    bundle.putString(KEY_SUBTITLE, subTitle);
    bundle.putString(KEY_ACTIVE, activeName);
    bundle.putString(KEY_PASSIVE, passiveName);
    bundle.putString(KEY_SUBBUTTON, sunButton);
    CommonDialog dialog = new CommonDialog();
    dialog.setArguments(bundle);
    return dialog;
  }

  public static CommonDialog newInstance(@NonNull String title, @NonNull String subTitle,
      @NonNull String activeName, @Nullable String passiveName) {
    Bundle bundle = new Bundle();
    bundle.putString(KEY_TITLE, title);
    bundle.putString(KEY_SUBTITLE, subTitle);
    bundle.putString(KEY_ACTIVE, activeName);
    bundle.putString(KEY_PASSIVE, passiveName);
    CommonDialog dialog = new CommonDialog();
    dialog.setArguments(bundle);
    return dialog;
  }

  @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    AppCompatDialog dialog =
        new AppCompatDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
    View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_common, null);
    ButterKnife.bind(this, view);
    dialog.setContentView(view);
    Bundle bundle = getArguments();

    title.setText(bundle.getString(KEY_TITLE));
    searchFilterContainer.setText(bundle.getString(KEY_SUBTITLE));
    active.setText(bundle.getString(KEY_ACTIVE));

    hideButton = bundle.getBoolean(KEY_HIDE_BUTTON, false);
    if (hideButton) {
      commonDialogBtnLayout.setVisibility(View.GONE);
      startTimer();
    } else {
      commonDialogBtnLayout.setVisibility(View.VISIBLE);
    }

    if (TextUtils.isEmpty(bundle.getString(KEY_PASSIVE))) {
      passive.setVisibility(View.GONE);
    } else {
      passive.setText(bundle.getString(KEY_PASSIVE));
      passive.setOnClickListener(this);
    }

    if (TextUtils.isEmpty(bundle.getString(KEY_SUBBUTTON))) {
      subButton.setVisibility(View.GONE);
    } else {
      subButton.setText(bundle.getString(KEY_SUBBUTTON));
      subButton.setOnClickListener(this);
    }
    active.setOnClickListener(this);

    view.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (isCancelable()) {
          dismiss();
        }
      }
    });

    return dialog;
  }

  public void setListener(OnActionListener listener) {
    this.listener = listener;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  private void startTimer() {
    cancelTimer();
    handler.sendEmptyMessageDelayed(TIME_OUT_TASK, 1000);
  }

  private void cancelTimer() {
    handler.removeMessages(TIME_OUT_TASK);
  }

  /**
   * 定时消失dialog
   */
  private Handler handler = new Handler() {
    @Override public void handleMessage(Message msg) {
      if (msg.what == TIME_OUT_TASK) {
        dismiss();
      }
    }
  };

  @Override public void onClick(View v) {
    if (listener == null) {
      cancelTimer();
      dismiss();
      return;
    }

    boolean result = true;

    switch (v.getId()) {
      case R.id.passive:
        result = listener.dialogClick(CLICK_ID_PASSIVE);
        break;
      case R.id.active:
        result = listener.dialogClick(CLICK_ID_ACTIVE);
        break;
      case R.id.subButton:
        result = listener.dialogClick(CLICK_ID_SUBBUTTON);
    }
    if (result) {
      cancelTimer();
      dismiss();
    }
  }

  public interface OnActionListener {

    /**
     * @param clickId 按钮类型
     * @return true 父类处理关闭事件
     */
    boolean dialogClick(int clickId);
  }
}
