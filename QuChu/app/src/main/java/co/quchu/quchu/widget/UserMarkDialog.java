package co.quchu.quchu.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import co.quchu.quchu.R;

/**
 * 称号弹窗
 * <p/>
 * Created by mwb on 16/9/22.
 */
public class UserMarkDialog extends Dialog {

  private final String mMark;

  public UserMarkDialog(Context context, String mark) {
    super(context, R.style.loading_dialog);

    mMark = mark;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_user_mark);
    setCanceledOnTouchOutside(false);
    setCancelable(false);

    TextView userMarkTv = (TextView) findViewById(R.id.userMarkTv);
    userMarkTv.setText("恭喜你获得了" + mMark + "称号");

    findViewById(R.id.confirmBtn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dismiss();
      }
    });
  }
}
