package co.quchu.quchu.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;

/**
 * Created by no21 on 2016/4/7.
 * email:437943145@qq.com
 * desc :
 */
public class ScrollIndexView extends FrameLayout {
    @Bind(R.id.hour)
    ImageView hourView;
    @Bind(R.id.min)
    ImageView minView;

    float hourFirstTime;
    float minFirstTime;
    @Bind(R.id.time)
    TextView time;

    public ScrollIndexView(Context context) {
        super(context);
        init();
    }


    public ScrollIndexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScrollIndexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScrollIndexView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.layout_scroll_index, this);
        ButterKnife.bind(this, this);
    }


    public void startTimeAnamation(int hour, int min) {

        time.setText(hour + ":" + min);

        //时
        RotateAnimation hourAnimation = new RotateAnimation(
                hourFirstTime / 12 * 360, computeDegressHour(hour),
                RotateAnimation.RELATIVE_TO_SELF, .5f,
                RotateAnimation.RELATIVE_TO_SELF, .86f);

        hourAnimation.setDuration(600);
        hourAnimation.setFillAfter(true);
        hourAnimation.setInterpolator(new DecelerateInterpolator());
        //分
        min %= 60;

        RotateAnimation minAnimation = new RotateAnimation(
                min / 60 * 360, computeDegressMin(hour, min),
                RotateAnimation.RELATIVE_TO_SELF, .5f,
                RotateAnimation.RELATIVE_TO_SELF, .86f);

        minAnimation.setDuration(600);
        minAnimation.setFillAfter(true);
        minAnimation.setInterpolator(new DecelerateInterpolator());

        hourView.startAnimation(hourAnimation);
        minView.startAnimation(minAnimation);

        hourFirstTime = hour;
        minFirstTime = min;
    }

    private float computeDegressHour(float hour) {
        hour %= 12;
        float offset = 0;
        if (hour < hourFirstTime) {
            offset = 360;
        }
        return hour / 12 * 360 + offset;
    }

    private float computeDegressMin(float Targethour, float TargetMin) {
        TargetMin %= 60;
        Targethour %= 12;

        return TargetMin / 60 * 360 + (12 - Math.abs(hourFirstTime - Targethour)) * 360;
    }

}
