package co.quchu.quchu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import co.quchu.quchu.R;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.StringUtils;

/**
 * FlickrButtonGroup
 * User: Chenhs
 * Date: 2015-11-12
 * 控件为三个button 左中右
 * 点击选中 左右两个button时 View动画移动到选中位置
 * 例如：当前选中左边，当点击右边时 view移动到右边，右边button的background切换为选中是的资源
 * 点击选中 中间按钮 则中间imageview 由大变小动画 切换选中图片而后由小变大
 * 动画效果：位移动画 、伸缩动画
 */
public class FlickrButtonGroup extends RelativeLayout implements View.OnClickListener {
    Context context;
    View widgetSwitchSelectedView;
    ImageButton widgetSwitchHotBtn;
    ImageButton widgetSwitchNewBtn;
    ImageView widgetSwitchCenterIv;
    RelativeLayout widget_switch_root_rl;
    /**
     * 左边按钮选中状态
     * true==选中  false=未选中
     */
    private boolean isHotSelected = true;
    /**
     * 右边按钮选中状态
     */
    private boolean isNewSelected = false;
    /**
     * 中间按钮处于大图状态
     */
    private boolean isCenterSelected = true;
    private FlickrOnSelectedistener listener;

    public FlickrButtonGroup(Context context) {
        this(context, null);
    }

    public FlickrButtonGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private float newBtnX = -1f, hotBtnX = -1f;

    public FlickrButtonGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.widget_switch_buttons, this, true);
        widgetSwitchSelectedView = findViewById(R.id.widget_switch_selected_view);
        widgetSwitchHotBtn = (ImageButton) findViewById(R.id.widget_switch_hot_btn);
        widgetSwitchNewBtn = (ImageButton) findViewById(R.id.widget_switch_new_btn);
        widgetSwitchCenterIv = (ImageView) findViewById(R.id.widget_switch_center_iv);
        widget_switch_root_rl = (RelativeLayout) findViewById(R.id.widget_switch_root_rl);
        widgetSwitchCenterIv.setOnClickListener(this);
        widgetSwitchHotBtn.setOnClickListener(this);
        widgetSwitchNewBtn.setOnClickListener(this);


    }

    public void setSelectedListener(FlickrOnSelectedistener listener) {
        this.listener = listener;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.widget_switch_center_iv:
                setViewsClickable(false);
                animCenterShrink();
                break;
            case R.id.widget_switch_hot_btn:
                if (isHotSelected)
                    return;
                setViewsClickable(false);
                selectedViewL2R(false);
                isNewSelected = false;
                isHotSelected = true;
                if (listener != null) {

                    listener.onViewsClick(SelectedL);
                }
                break;
            case R.id.widget_switch_new_btn:
                if (isNewSelected)
                    return;
                setViewsClickable(false);
                selectedViewL2R(true);
                isHotSelected = false;
                isNewSelected = true;
                if (listener != null) {
                    listener.onViewsClick(SelectedR);
                }
                break;

        }
    }

    private ObjectAnimator objectAnimator, objectAnimator1, objectAnimator2;
    private AnimatorSet animatorSet;
    private static long TranslationDuration = -1;
    private static long ScaleDuration = 400;


    /**
     * 黄色色块移动动画，
     *
     * @param isL2R true 自左向右   false 自右向左
     */
    private void selectedViewL2R(boolean isL2R) {
        if (newBtnX == -1f || hotBtnX == -1f) {
            newBtnX = widgetSwitchNewBtn.getX();
            hotBtnX = widgetSwitchHotBtn.getX();
            TranslationDuration = (long) (widget_switch_root_rl.getWidth() * 0.5f);
        }
        LogUtils.json("newBtnX==" + newBtnX + "////hotBtnX==" + hotBtnX);
        animatorSet = new AnimatorSet();
        if (isL2R) {
            objectAnimator = ObjectAnimator.ofFloat(widgetSwitchSelectedView, "translationX", hotBtnX, newBtnX - StringUtils.dip2px(context, 4));
        } else {
            objectAnimator = ObjectAnimator.ofFloat(widgetSwitchSelectedView, "translationX", newBtnX, hotBtnX - StringUtils.dip2px(context, 2));
        }
        objectAnimator1 = ObjectAnimator.ofFloat(widgetSwitchSelectedView, "scaleX", 0.6f, 1.4f, 0.6f, 1.4f,
                Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 1f);
        objectAnimator2 = ObjectAnimator.ofFloat(widgetSwitchSelectedView, "scaleY", 1.0f, 0.6f, 1.0f);
        animatorSet.playTogether(objectAnimator, objectAnimator1, objectAnimator2);
        animatorSet.setDuration(TranslationDuration);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                animationStartChange(isHotSelected);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animationEndChange(isHotSelected);
                setViewsClickable(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    private void animationStartChange(boolean isHotSelected) {
        if (isHotSelected) {
            widgetSwitchHotBtn.setImageResource(R.drawable.ic_widget_hot);
        } else {
            widgetSwitchNewBtn.setImageResource(R.drawable.ic_widget_new);
        }
    }

    private void animationEndChange(boolean isHotSelected) {
        if (isHotSelected) {
            widgetSwitchHotBtn.setImageResource(R.drawable.ic_widget_hot_selected);
        } else {
            widgetSwitchNewBtn.setImageResource(R.drawable.ic_widget_new_selected);
        }
    }

    /**
     * 中间按钮缩小动画
     */
    private void animCenterShrink() {
        animatorSet = new AnimatorSet();
        objectAnimator1 = ObjectAnimator.ofFloat(widgetSwitchCenterIv, "scaleX", 1f, 0.9f, 0.8f, 0.4f);
        objectAnimator2 = ObjectAnimator.ofFloat(widgetSwitchCenterIv, "scaleY", 1f, 0.9f, 0.8f, 0.4f);
        animatorSet.playTogether(objectAnimator1, objectAnimator2);
        animatorSet.setDuration(ScaleDuration);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animCenterAmplify();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();
    }

    /**
     * 中间按扭扩大动画
     */
    private void animCenterAmplify() {
        animatorSet = new AnimatorSet();
        if (isCenterSelected) {
            widgetSwitchCenterIv.setImageResource(R.drawable.ic_widget_center_amplify);
            isCenterSelected = false;
        } else {
            widgetSwitchCenterIv.setImageResource(R.drawable.ic_widget_center_shrink);
            isCenterSelected = true;
        }
        objectAnimator1 = ObjectAnimator.ofFloat(widgetSwitchCenterIv, "scaleX", 0.4f, 0.8f, 0.9f, 1.0f);
        objectAnimator2 = ObjectAnimator.ofFloat(widgetSwitchCenterIv, "scaleY", 0.4f, 0.8f, 0.9f, 1.0f);
        animatorSet.playTogether(objectAnimator1, objectAnimator2);
        animatorSet.setDuration(ScaleDuration);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setViewsClickable(true);
                if (listener != null) {
                    if (isCenterSelected) {
                        listener.onViewsClick(SelectedCT);
                    } else {
                        listener.onViewsClick(SelectedCF);
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    /**
     * 选择结果回调
     */
    public interface FlickrOnSelectedistener {
        public void onViewsClick(int flag);
    }

    private void setViewsClickable(boolean isClickable) {
        widgetSwitchHotBtn.setClickable(isClickable);
        widgetSwitchNewBtn.setClickable(isClickable);
        widgetSwitchCenterIv.setClickable(isClickable);
    }

    /**
     * 回调结果：
     * SelectedR== 当前选中右边
     * SelectedL== 当前选中左边
     * SelectedCT== 当前选中中间按钮true
     * SelectedCF== 选中中间按钮false
     */
    public static final int SelectedR = 0x00;
    public static final int SelectedL = 0x01;
    public static final int SelectedCT = 0x02;
    public static final int SelectedCF = 0x03;
}
