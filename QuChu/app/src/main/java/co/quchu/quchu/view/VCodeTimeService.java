package co.quchu.quchu.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class VCodeTimeService {

  private static final String LEFT_SECOND_KEY = "left_second";
  public static final String SEND_CODE_DATE_INTENT_KEY = "send_code_date_key";

  public static final Integer TYPE_REGISTER_VCODE = 1;//1 用户注册
  public static final Integer TYPE_FORGET_PASSWORD_VCODE = 2;//2 忘记密码

  private static final int VALIDATE_CODE_TIMEOUT = 60;
  private static final int TIME_TICKET = 1000;
  private Timer timer;

  private Calendar startCalendar;
  private Calendar endCalendar;
  private int leftSecond;

  /**
   * @return the leftSecond
   */
  public int getLeftSecond() {
    return leftSecond;
  }

  public void onCreate(Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      leftSecond = savedInstanceState.getInt(LEFT_SECOND_KEY);
      Date lastDate = new Date(savedInstanceState.getLong(SEND_CODE_DATE_INTENT_KEY));
      Date now = new Date();
      startCalendar = Calendar.getInstance();
      startCalendar.setTime(lastDate);

      endCalendar = Calendar.getInstance();
      endCalendar.setTime(now);
      long minSecond = endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis();
      if (minSecond > 0) {
        leftSecond = leftSecond - (int) (minSecond / 1000);
      } else {
        leftSecond = 0;
      }
    } else {
      leftSecond = VALIDATE_CODE_TIMEOUT;
    }
  }

  public void reset() {
    leftSecond = VALIDATE_CODE_TIMEOUT;

    if (onTimeListener != null) {
      onTimeListener.onReset();
    }
  }

  public void startTimer() {
    cancelTimer();

    timer = new Timer();
    timer.schedule(new TimerTask() {

      @Override
      public void run() {
        handler.sendEmptyMessage(0);
      }
    }, 0, TIME_TICKET);
  }

  private void cancelTimer() {
    if (timer != null) {
      timer.cancel();
      timer.purge();
      timer = null;
    }
  }

  private Handler handler = new Handler() {

    @Override
    public void handleMessage(Message msg) {

      leftSecond--;

      if (leftSecond <= 0) {
        cancelTimer();
        if (onTimeListener != null) {
          onTimeListener.onTimeOut();
        }

        return;
      }

      if (onTimeListener != null) {
        onTimeListener.timeRemaining(leftSecond);
      }

    }
  };

  private OnVCodeTimeListener onTimeListener;

  public void setOnTimeListener(OnVCodeTimeListener onTimeListener) {
    this.onTimeListener = onTimeListener;
  }

  public interface OnVCodeTimeListener {

    void onReset();

    void onTimeOut();

    void timeRemaining(int leftSecond);
  }
}
