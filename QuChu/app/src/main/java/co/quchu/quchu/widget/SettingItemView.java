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
import co.quchu.quchu.presenter.SettingPresenter;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.ToastManager;
import co.quchu.quchu.widget.swithbutton.SwitchButton;

/**
 * 设置ItemView
 * <p/>
 * Created by mwb on 16/8/19.
 */
public class SettingItemView extends LinearLayout {

    @Bind(R.id.item_indicator_img)
    ImageView indicatorImg;
    @Bind(R.id.item_title_tv)
    TextView titleTv;
    @Bind(R.id.item_switch_btn)
    SwitchButton switchBtn;
    @Bind(R.id.item_right_arrow_img)
    ImageView rightArrowImg;

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
            int imgResId = typedArray.getResourceId(R.styleable.SettingItemView_stImage, -1);
            int textColor = typedArray.getColor(R.styleable.SettingItemView_stTextColor, -1);
            boolean isShowSwitchBtn = typedArray.getBoolean(R.styleable.SettingItemView_stShowSwitchBtn, false);
            typedArray.recycle();

            if (!TextUtils.isEmpty(text)) {
                titleTv.setText(text);
            }

            if (imgResId != -1) {
                indicatorImg.setVisibility(VISIBLE);
                indicatorImg.setBackgroundResource(imgResId);
            } else {
                indicatorImg.setVisibility(GONE);
            }

            if (textColor != -1) {
                titleTv.setTextColor(textColor);
            }

            if (isShowSwitchBtn) {
                switchBtn.setVisibility(VISIBLE);
                rightArrowImg.setVisibility(GONE);

                switchBtn.setChecked(SPUtils.getDahuoSwitch());

                switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        setSwitchChecked(b);
                    }
                });

            } else {
                switchBtn.setVisibility(GONE);
                rightArrowImg.setVisibility(VISIBLE);
            }
        }
    }

    public void setSwitchChecked(final boolean checked) {
        String value = checked ? "1" : "0";
        SettingPresenter.setUserMsg(getContext(), "3", value, new SettingPresenter.OnUserMsgListener() {
            @Override
            public void onSuccess() {
                switchBtn.setChecked(checked);
                SPUtils.setDahuoSwitch(true);
            }

            @Override
            public void onError() {
                ToastManager.getInstance(getContext()).show("设置失败");
                SPUtils.setDahuoSwitch(false);
            }
        });
    }
}
