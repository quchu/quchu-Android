package co.quchu.quchu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import co.quchu.quchu.R;
import co.quchu.quchu.utils.AppUtil;
import co.quchu.quchu.utils.LogUtils;

/**
 * FlickrButtonGroup
 * User: Chenhs
 * Date: 2015-11-12
 * 前期：TabLayout item  点击选中时 textview改变颜色， 底部index 图片动画移动
 */
public class TextSubtabLayout extends RelativeLayout implements View.OnClickListener {
    private TextView widgetTextSubtabLeftNumTv;
    private TextView widgetTextSubtabLeftDesTv;
    private TextView widgetTextSubtabRightNumTv;
    private TextView widgetTextSubtabRightDesTv;
    private ImageView widgetTextIndexIv;
    private LinearLayout widgetTextSubtabRightLl, widgetTextSubtabLeftLl;
    private Context context;
    private ObjectAnimator objectAnimator;
    private static long ScaleDuration = 400;//动画时长
    private boolean isLeftSelected = true;
    private int mScreenWidth = 0;
    private AnimatorSet animatorSet;
    private View view;
    private TextSubtabSelectedListener listener;

    public TextSubtabLayout(Context context) {
        this(context, null);
    }

    public TextSubtabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private float leftX = -1f, rightX = -1f;

    public TextSubtabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.widget_text_subtab, this, true);
        initView();
        mScreenWidth = AppUtil.getScreenWidth(context);
        initData();
        initIndexView(0);

    }

    private void initView() {
        widgetTextSubtabLeftNumTv = (TextView) findViewById(R.id.widget_text_subtab_left_num_tv);
        widgetTextSubtabLeftDesTv = (TextView) findViewById(R.id.widget_text_subtab_left_des_tv);
        widgetTextSubtabRightNumTv = (TextView) findViewById(R.id.widget_text_subtab_right_num_tv);
        widgetTextSubtabRightDesTv = (TextView) findViewById(R.id.widget_text_subtab_right_des_tv);
        widgetTextIndexIv = (ImageView) findViewById(R.id.widget_text_index_iv);
        widgetTextSubtabLeftLl = (LinearLayout) findViewById(R.id.widget_text_subtab_left_ll);
        widgetTextSubtabRightLl = (LinearLayout) findViewById(R.id.widget_text_subtab_right_ll);
        widgetTextSubtabLeftLl.setOnClickListener(this);
        widgetTextSubtabRightLl.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.widget_text_subtab_left_ll:

                if (!isLeftSelected) {
                    isLeftSelected = true;
                    viewState();
                    if (null != listener) {
                        listener.onSelected(0);
                    }
                    LogUtils.json("widget_image_subtab_left_sdv");
                }
                break;
            case R.id.widget_text_subtab_right_ll:
                if (isLeftSelected) {
                    isLeftSelected = false;
                    viewState();
                    if (null != listener) {
                        listener.onSelected(1);
                    }
                    LogUtils.json("widget_image_subtab_right_sdv");
                }
                break;
        }
    }

    private void viewState() {
        if (isLeftSelected) {
            //选中左边
            buttomIndexAnimation(true);
        } else {
            //选中右边
            buttomIndexAnimation(false);
        }
    }

    /**
     * 底部标志移动动画
     *
     * @param isR2L true=动画右向左
     *              false =动画左向右
     */
    private void buttomIndexAnimation(final boolean isR2L) {

        if (isR2L) {
            objectAnimator = ObjectAnimator.ofFloat(widgetTextIndexIv, "translationX", rightX, leftX);
        } else {
            objectAnimator = ObjectAnimator.ofFloat(widgetTextIndexIv, "translationX", leftX, rightX);
        }
        if (objectAnimator != null) {

            objectAnimator.setDuration(ScaleDuration);
            objectAnimator.setInterpolator(new OvershootInterpolator());
            objectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    changeTextColor(isR2L ? 0 : 1);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            objectAnimator.start();
        }
    }


    /**
     * 初始化位置
     *
     * @param selectedNum 0=左边选中状态   1 =右边选中状态
     */
    public void initIndexView(int selectedNum) {
        if (selectedNum == 0) {
            widgetTextIndexIv.setX(leftX);
            isLeftSelected = true;
        } else {
            widgetTextIndexIv.setX(rightX);
            isLeftSelected = false;
        }
        changeTextColor(selectedNum);
    }

    private void changeTextColor(int selectedNum) {
        if (selectedNum == 0) {
            widgetTextSubtabLeftNumTv.setTextColor(getResources().getColor(R.color.gene_textcolor_yellow));
            widgetTextSubtabLeftDesTv.setTextColor(getResources().getColor(R.color.gene_textcolor_yellow));
            widgetTextSubtabRightDesTv.setTextColor(getResources().getColor(R.color.planet_text_color_white));
            widgetTextSubtabRightNumTv.setTextColor(getResources().getColor(R.color.planet_text_color_white));
        } else {
            widgetTextSubtabLeftNumTv.setTextColor(getResources().getColor(R.color.planet_text_color_white));
            widgetTextSubtabLeftDesTv.setTextColor(getResources().getColor(R.color.planet_text_color_white));
            widgetTextSubtabRightDesTv.setTextColor(getResources().getColor(R.color.gene_textcolor_yellow));
            widgetTextSubtabRightNumTv.setTextColor(getResources().getColor(R.color.gene_textcolor_yellow));
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //    widgetTextIndexIv
        leftX = ((mScreenWidth / 2) - widgetTextIndexIv.getWidth() / 2) / 2;
        rightX = leftX * 3;
    }


    public void setSelectedListener(TextSubtabSelectedListener listener) {
        this.listener = listener;
    }

    public interface TextSubtabSelectedListener {
        void onSelected(int selectedNum);
    }

    public void setRightNum(int  Num) {
        widgetTextSubtabRightNumTv.setText(String.valueOf(Num));
    }

    public void setLeftNum(int Num) {
        widgetTextSubtabLeftNumTv.setText(String.valueOf(Num));
    }

    public void setRightDes(String des) {
        widgetTextSubtabRightDesTv.setText(des);
    }

    public void setLeftDes(String des) {
        widgetTextSubtabLeftDesTv.setText(des);
    }
}
