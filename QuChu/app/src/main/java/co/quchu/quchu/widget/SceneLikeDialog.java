package co.quchu.quchu.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;

/**
 * Created by mwb on 16/9/21.
 */
public class SceneLikeDialog extends Dialog {

  private final int TIME_OUT_TASK = 1;

  private boolean mIsLiked;
  private int mSuccessCode;

  @Bind(R.id.contentImg) ImageView mContentImg;
  @Bind(R.id.contentTv) TextView mContentTv;

  public SceneLikeDialog(Context context, boolean isLiked, int i) {
    super(context, R.style.loading_dialog);

    mIsLiked = isLiked;
    mSuccessCode = i;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_scene_like);
    ButterKnife.bind(this);

    if (mIsLiked) {
      if (mSuccessCode == 0) {
        mContentImg.setImageResource(R.mipmap.ic_launcher);
        mContentTv.setText("设为常用成功");
      } else {
        mContentImg.setImageResource(R.mipmap.ic_launcher);
        mContentTv.setText("设为常用失败");
      }
    } else {
      if (mSuccessCode == 0) {
        mContentImg.setImageResource(R.mipmap.ic_launcher);
        mContentTv.setText("移除常用成功");
      } else {
        mContentImg.setImageResource(R.mipmap.ic_launcher);
        mContentTv.setText("移除常用失败");
      }
    }
  }

  @Override
  public void show() {
    super.show();

    startTimer();
  }

  @Override
  public void dismiss() {
    cancelTimer();
    super.dismiss();
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
    @Override
    public void handleMessage(Message msg) {
      if (msg.what == TIME_OUT_TASK) {
        dismiss();
      }
    }
  };
}
