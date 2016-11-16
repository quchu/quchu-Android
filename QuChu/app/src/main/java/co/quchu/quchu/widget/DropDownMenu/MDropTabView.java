package co.quchu.quchu.widget.DropDownMenu;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.quchu.quchu.R;

/**
 * Created by mwb on 16/10/20.
 */
public class MDropTabView extends RelativeLayout {

  private TextView mTextView;
  private ImageView mImageView;

  public MDropTabView(Context context) {
    this(context, null);
  }

  public MDropTabView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public MDropTabView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context) {
    setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    LinearLayout linearLayout = new LinearLayout(context);
    linearLayout.setPadding(dpToPx(5), 0, dpToPx(5), 0);
    LayoutParams layoutParams = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
    linearLayout.setLayoutParams(layoutParams);

    mTextView = new TextView(context);
    mTextView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    mTextView.setSingleLine();
    mTextView.setEllipsize(TextUtils.TruncateAt.END);
    mTextView.setGravity(Gravity.CENTER);
    mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
    mTextView.setTextColor(getContext().getResources().getColor(R.color.standard_color_h2_dark));
    mTextView.setText("默认");
    linearLayout.addView(mTextView, 0);

    mImageView = new ImageView(context);
    mImageView.setPadding(dpToPx(2), 0, 0, 0);
    mImageView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    mImageView.setImageResource(R.mipmap.ic_down);
    linearLayout.addView(mImageView, 1);

    addView(linearLayout, 0);

    View dividerView = new View(getContext());
    LayoutParams lp = new LayoutParams(dpToPx(0.5f), RelativeLayout.LayoutParams.MATCH_PARENT);
    lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    dividerView.setLayoutParams(lp);
    dividerView.setBackgroundColor(getResources().getColor(R.color.colorDivider));

    addView(dividerView, 1);
  }

  public TextView getTextView() {
    return mTextView;
  }

  public ImageView getImageView() {
    return mImageView;
  }

  private int dpToPx(float value) {
    DisplayMetrics dm = getResources().getDisplayMetrics();
    return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
  }
}
