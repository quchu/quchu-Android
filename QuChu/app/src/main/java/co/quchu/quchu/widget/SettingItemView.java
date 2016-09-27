package co.quchu.quchu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.widget.swithbutton.SwitchButton;

/**
 * 设置ItemView
 * <p/>
 * Created by mwb on 16/8/19.
 */
public class SettingItemView extends LinearLayout {

  @Bind(R.id.item_title_tv) TextView titleTv;
  @Bind(R.id.item_switch_btn) SwitchButton switchBtn;
  @Bind(R.id.item_right_arrow_img) ImageView rightArrowImg;

  public SettingItemView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    LayoutInflater.from(context).inflate(R.layout.view_user_item, this);
    ButterKnife.bind(this);

    if (attrs != null) {
      TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingItemView);
      String text = typedArray.getString(R.styleable.SettingItemView_stText);
      int textColor = typedArray.getColor(R.styleable.SettingItemView_stTextColor, -1);
      boolean isShowSwitchBtn =
          typedArray.getBoolean(R.styleable.SettingItemView_stShowSwitchBtn, false);
      typedArray.recycle();

      if (!TextUtils.isEmpty(text)) {
        titleTv.setText(text);
      }

      if (textColor != -1) {
        titleTv.setTextColor(textColor);
      }

      if (isShowSwitchBtn) {
        switchBtn.setVisibility(VISIBLE);
        rightArrowImg.setVisibility(GONE);
      } else {
        switchBtn.setVisibility(GONE);
        rightArrowImg.setVisibility(VISIBLE);
      }
    }
  }

  public void setSwitchChecked(boolean checked, final SwitchChangedListener listener) {
    switchBtn.setChecked(checked);

    switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (listener != null) {
          listener.onSwitch(b);
        }
      }
    });
  }

  public SwitchButton getSwitchButton() {
    return switchBtn;
  }

  public interface SwitchChangedListener {
    void onSwitch(boolean isChecked);
  }
}
