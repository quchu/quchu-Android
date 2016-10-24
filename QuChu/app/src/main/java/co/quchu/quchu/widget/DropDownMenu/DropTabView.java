package co.quchu.quchu.widget.DropDownMenu;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.quchu.quchu.R;

/**
 * Created by mwb on 16/10/20.
 */
public class DropTabView extends LinearLayout {

  public DropTabView(Context context) {
    super(context);
    init(context);
  }

  public DropTabView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public DropTabView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context) {
    setOrientation(HORIZONTAL);
    setGravity(Gravity.CENTER);

    TextView textView = new TextView(context);
    textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
    textView.setSingleLine();
    textView.setEllipsize(TextUtils.TruncateAt.END);
    textView.setGravity(Gravity.CENTER);
    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
    textView.setTextColor(getContext().getResources().getColor(R.color.standard_color_h2_dark));
    textView.setText("默认");
    addView(textView, 0);

    ImageView imageView = new ImageView(context);
    ViewGroup.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    imageView.setPadding(dip2px(getContext(), 2), 0, 0, 0);
    imageView.setLayoutParams(layoutParams);
    imageView.setImageResource(R.mipmap.ic_down);
    addView(imageView, 1);
  }

  public static int dip2px(Context context, float dpValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }
}
