package co.quchu.quchu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import butterknife.ButterKnife;
import co.quchu.quchu.R;

/**
 * 用户反馈提交对话框
 * <p/>
 * Created by mwb on 16/8/23.
 */
public class FeedbackSuccessDialog extends Dialog {

  private final int TIME_OUT_TASK = 1;

  public FeedbackSuccessDialog(Context context) {
    super(context, R.style.loading_dialog);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_feedback_success);
    ButterKnife.bind(this);
  }

  @Override
  public void show() {
    startTimer();
    super.show();
  }

  @Override
  public void dismiss() {
    super.dismiss();
    cancelTimer();
  }

  private void startTimer() {
    cancelTimer();
    handler.sendEmptyMessageDelayed(TIME_OUT_TASK, 2000);
  }

  private void cancelTimer() {
    handler.removeMessages(TIME_OUT_TASK);
  }

  /**
   * 定时消失dialog
   */
  private Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      if (msg.what == TIME_OUT_TASK) {
        dismiss();
      }
    }
  };
}
