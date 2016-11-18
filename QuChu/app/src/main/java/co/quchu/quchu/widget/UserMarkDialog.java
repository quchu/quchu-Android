package co.quchu.quchu.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

    ImageView imageView = (ImageView) findViewById(R.id.userMarkImg);
    TextView userMarkTv = (TextView) findViewById(R.id.userMarkTv);
    userMarkTv.setText("恭喜你获得了" + mMark + "称号");

    switch (mMark) {
      case "小食神":
        imageView.setImageResource(R.mipmap.ic_chihuo_main);
        break;

      case "艺术家":
        imageView.setImageResource(R.mipmap.ic_wenyi_main);
        break;

      case "外交官":
        imageView.setImageResource(R.mipmap.ic_shejiao_main);
        break;

      case "时尚精":
        imageView.setImageResource(R.mipmap.ic_shishang_main);
        break;

      case "大财阀":
        imageView.setImageResource(R.mipmap.ic_tuhao_main);
        break;

      case "玩乐咖":
        imageView.setImageResource(R.mipmap.ic_haoqi_main);
        break;
    }

    findViewById(R.id.confirmBtn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dismiss();
      }
    });
  }
}
