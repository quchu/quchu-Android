package co.quchu.quchu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import co.quchu.quchu.R;
import co.quchu.quchu.utils.StringUtils;

/**
 * User: Chenhs
 * Date: 2015-11-12
 * 控件为三个button 左中右
 * 点击选中 左右两个button时 View动画移动到选中位置
 * 例如：当前选中左边，当点击右边时 view移动到右边，右边button的background切换为选中是的资源
 * 点击选中 中间按钮 则中间imageview 由大变小动画 切换选中图片而后由小变大
 * 动画效果：位移动画 、伸缩动画
 */
public class RecommendTitleGroup extends RelativeLayout implements View.OnClickListener {
    Context context;
    View widgetSwitchSelectedView;
    TextView widgetSwitchHotBtn;
    TextView widgetSwitchNewBtn;
    RelativeLayout widget_switch_root_rl;
    View rootView;
    /**
     * 左边按钮选中状态
     * true==选中  false=未选中
     */
    private boolean isHotSelected = true;
    /**
     * 右边按钮选中状态
     */
    private boolean isNewSelected = false;

    private RecoSelectedistener listener;

    public RecommendTitleGroup(Context context) {
        this(context, null);
    }

    public RecommendTitleGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private float newBtnX = -1f, hotBtnX = -1f;

    public RecommendTitleGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = mInflater.inflate(R.layout.widget_recommend_switch, this, true);
        widgetSwitchHotBtn = (TextView) findViewById(R.id.widget_switch_hot_btn);
        widgetSwitchNewBtn = (TextView) findViewById(R.id.widget_switch_new_btn);
        widget_switch_root_rl = (RelativeLayout) findViewById(R.id.widget_switch_root_rl);
        setInitSelected(false);
        widgetSwitchHotBtn.setOnClickListener(this);
        widgetSwitchNewBtn.setOnClickListener(this);
     //   LogUtils.json("RecommendTitleGroup   创建  创建");
    }

    public void setSelectedListener(RecoSelectedistener listener) {
        this.listener = listener;
    }

    private boolean isInitSelectedRight;

    public void setViewVisibility(int visibility) {
        switch (visibility) {
            case View.VISIBLE:
                rootView.setVisibility(VISIBLE);
                break;
            case View.GONE:
                rootView.setVisibility(GONE);
                break;
            case INVISIBLE:
                rootView.setVisibility(INVISIBLE);
                break;
        }
    }

    /**
     * 设置初始选中状态
     *
     * @param isSelectedRight
     */
    public void setInitSelected(boolean isSelectedRight) {
        isInitSelectedRight = isSelectedRight;
        if (isSelectedRight) {
            widgetSwitchSelectedView = findViewById(R.id.widget_switch_selected_right_view);
            findViewById(R.id.widget_switch_selected_view).setVisibility(INVISIBLE);
            isHotSelected = false;
            isNewSelected = true;

        } else {
            widgetSwitchSelectedView = findViewById(R.id.widget_switch_selected_view);
            findViewById(R.id.widget_switch_selected_right_view).setVisibility(INVISIBLE);
            isNewSelected = false;
            isHotSelected = true;
        }
        animationEndChange(isHotSelected);
        widgetSwitchSelectedView.setVisibility(VISIBLE);
    }

    public void selectedLeft() {
        if (isHotSelected)
            return;
        setViewsClickable(false);
        selectedViewL2R(false);
        isNewSelected = false;
        isHotSelected = true;
        if (listener != null) {

            listener.onViewsClick(SelectedL);
        }
    }

    @Override
    public void onClick(View v) {
        if (isViewClickable) {
            switch (v.getId()) {
                case R.id.widget_switch_hot_btn:
                    selectedLeft();
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
        } else {
            //  Toast.makeText(context, "请先选择感兴趣的类型！", Toast.LENGTH_SHORT).show();
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
    public void selectedViewL2R(boolean isL2R) {
        if (newBtnX == -1f || hotBtnX == -1f) {
            newBtnX = widgetSwitchNewBtn.getX();
            hotBtnX = widgetSwitchHotBtn.getX();
            TranslationDuration = (long) (widget_switch_root_rl.getWidth() * 0.3f);
        }
      //  LogUtils.json("newBtnX==" + newBtnX + "////hotBtnX==" + hotBtnX);
        animatorSet = new AnimatorSet();
        if (isInitSelectedRight) {
            if (isL2R) {
                objectAnimator = ObjectAnimator.ofFloat(widgetSwitchSelectedView, "translationX", -newBtnX + StringUtils.dip2px(context, 3), hotBtnX - StringUtils.dip2px(context, 4));
            } else {
                objectAnimator = ObjectAnimator.ofFloat(widgetSwitchSelectedView, "translationX", hotBtnX - StringUtils.dip2px(context, 3), -newBtnX + StringUtils.dip2px(context, 3));
            }
        } else {
            if (isL2R) {
                objectAnimator = ObjectAnimator.ofFloat(widgetSwitchSelectedView, "translationX", hotBtnX, newBtnX - StringUtils.dip2px(context, 4));
            } else {
                objectAnimator = ObjectAnimator.ofFloat(widgetSwitchSelectedView, "translationX", newBtnX - StringUtils.dip2px(context, 3), hotBtnX - StringUtils.dip2px(context, 2));
            }
        }
        /*objectAnimator1 = ObjectAnimator.ofFloat(widgetSwitchSelectedView, "scaleX", 0.6f, 1.4f, 0.6f, 1.4f,
                Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 1f);*/
        objectAnimator2 = ObjectAnimator.ofFloat(widgetSwitchSelectedView, "scaleY", 1.0f, 0.6f, 1.0f);
        animatorSet.playTogether(objectAnimator, objectAnimator2);
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
        widgetSwitchHotBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
        widgetSwitchHotBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    private void animationEndChange(boolean isHotSelected) {
        if (isHotSelected) {
            widgetSwitchNewBtn.setTextColor(getResources().getColor(R.color.text_color_white));
            widgetSwitchHotBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            widgetSwitchNewBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
            widgetSwitchHotBtn.setTextColor(getResources().getColor(R.color.text_color_white));
        }
    }


    /**
     * 选择结果回调
     */
    public interface RecoSelectedistener {
        /**
         * @param flag SelectedR= 0 = 当前选中右边  SelectedL= 1= 当前选中左边
         */
        void onViewsClick(int flag);
    }

    public void setViewsClickable(boolean isClickable) {
        isViewClickable = isClickable;
       /* widgetSwitchHotBtn.setClickable(isClickable);
        widgetSwitchNewBtn.setClickable(isClickable);*/

    }

    private boolean isViewClickable = true;
    /**
     * 回调结果：
     * SelectedR== 当前选中右边
     * SelectedL== 当前选中左边
     * SelectedCT== 当前选中中间按钮true
     * SelectedCF== 选中中间按钮false
     */
    public static final int SelectedR = 0x00;
    public static final int SelectedL = 0x01;

}
