package co.quchu.quchu.widget;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.widget.textcounter.CounterView;

/**
 * FlickrButtonGroup
 * User: Chenhs
 * Date: 2015-11-12
 * 前期：TabLayout item  点击选中时 textview改变颜色， 底部index 图片动画移动
 * 后期：考虑从外部父布局传入 滑动数值， item的textView 根据滑动数值 渐变位置，
 * imageview 根据数值 改变透明度，（1f- TabLayout.Height*2/3)
 * 透明度算法：1f减去alpha
 * alpha=    滚动数值/（TabLayout.Height*2/3）
 */
public class ImageSubtabLayout extends RelativeLayout {
    @Bind(R.id.widget_image_subtab_left_sdv)
    SimpleDraweeView widgetImageSubtabLeftSdv;
    @Bind(R.id.widget_image_subtab_left_num_tv)
    CounterView widgetImageSubtabLeftNumTv;
    @Bind(R.id.widget_image_subtab_left_des_tv)
    TextView widgetImageSubtabLeftDesTv;
    @Bind(R.id.widget_image_subtab_right_sdv)
    SimpleDraweeView widgetImageSubtabRightSdv;
    @Bind(R.id.widget_image_subtab_right_num_tv)
    CounterView widgetImageSubtabRightNumTv;
    @Bind(R.id.widget_image_subtab_right_des_tv)
    TextView widgetImageSubtabRightDesTv;
    @Bind(R.id.widget_animation_index_iv)
    ImageView widgetAnimationIndexIv;
    /* @Bind(R.id.widget_image_subtab_hint_left_num_tv)
     TextView widgetImageSubtabHintLeftNumTv;
     @Bind(R.id.widget_image_subtab_hint_left_des_tv)
     TextView widgetImageSubtabHintLeftDesTv;
     @Bind(R.id.widget_image_subtab_hint_right_num_tv)
     TextView widgetImageSubtabHintRightNumTv;
     @Bind(R.id.widget_image_subtab_hint_right_des_tv)
     TextView widgetImageSubtabHintRightDesTv;*/
    private Context context;
    private ObjectAnimator objectAnimator;
    private AnimatorSet animatorSet;
    private static long ScaleDuration = 400;//动画时长
    private boolean isLeftSelected = true;
    private ImageSubtabSelectedListener listener;

    public ImageSubtabLayout(Context context) {
        this(context, null);
    }

    public ImageSubtabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private float leftX = -1f, rightX = -1f;

    public ImageSubtabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.widget_image_subtab, this, true);
        ButterKnife.bind(this);
        widgetImageSubtabRightSdv.setImageURI(Uri.parse("res://" + context.getPackageName() + "/" + R.mipmap.ic_image_empty));
        widgetImageSubtabLeftSdv.setImageURI(Uri.parse("res://" + context.getPackageName() + "/" + R.mipmap.ic_image_empty));
        initData();
    }


    @OnClick({R.id.widget_image_subtab_left_ll, R.id.widget_image_subtab_right_ll})
    public void ViewClick(View view) {
        switch (view.getId()) {
            case R.id.widget_image_subtab_left_ll:

                if (!isLeftSelected) {
                    isLeftSelected = true;
                    buttomIndexAnimation(true);
                    if (null != listener) {
                        listener.onSelected(0);
                    }
                    LogUtils.json("widget_image_subtab_left_sdv");
                }
                break;
            case R.id.widget_image_subtab_right_ll:
                if (isLeftSelected) {
                    isLeftSelected = false;
                    buttomIndexAnimation(false);
                    if (null != listener) {
                        listener.onSelected(1);
                    }
                    LogUtils.json("widget_image_subtab_right_sdv");
                }
                break;
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
            objectAnimator = ObjectAnimator.ofFloat(widgetAnimationIndexIv, "translationX", rightX, leftX);
        } else {
            objectAnimator = ObjectAnimator.ofFloat(widgetAnimationIndexIv, "translationX", leftX, rightX);
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

    private float widgetIndexWidth = -1f;


    public void initIndexView(int selectedNum) {
        if (selectedNum == 0) {
            widgetAnimationIndexIv.setX(leftX);
            isLeftSelected = true;
        } else {
            widgetAnimationIndexIv.setX(rightX);
            isLeftSelected = false;
        }

        changeTextColor(selectedNum);
    }

    private void changeTextColor(int selectedNum) {
        if (selectedNum == 0) {
            //选中左边
            widgetImageSubtabLeftNumTv.setTextColor(getResources().getColor(R.color.gene_textcolor_yellow));
            widgetImageSubtabLeftDesTv.setTextColor(getResources().getColor(R.color.gene_textcolor_yellow));
            widgetImageSubtabRightDesTv.setTextColor(getResources().getColor(R.color.text_color_white));
            widgetImageSubtabRightNumTv.setTextColor(getResources().getColor(R.color.text_color_white));
        } else {
            //选中右边
            widgetImageSubtabLeftNumTv.setTextColor(getResources().getColor(R.color.text_color_white));
            widgetImageSubtabLeftDesTv.setTextColor(getResources().getColor(R.color.text_color_white));
            widgetImageSubtabRightDesTv.setTextColor(getResources().getColor(R.color.gene_textcolor_yellow));
            widgetImageSubtabRightNumTv.setTextColor(getResources().getColor(R.color.gene_textcolor_yellow));
        }
    }

    private void initData() {
        //    widgetTextIndexIv
        leftX = ((AppContext.Width / 2) - widgetAnimationIndexIv.getWidth() / 2) / 2;
        rightX = leftX * 3;
        widgetAnimationIndexIv.setX(leftX);
        initIndexView(0);
        LogUtils.json("mScreenWidth==" + leftX);
    }


    public void setSelectedListener(ImageSubtabSelectedListener listener) {
        this.listener = listener;
    }


    public interface ImageSubtabSelectedListener {
        /**
         * 顶部按钮选中回调
         *
         * @param selectedNum 0=选中左边   1=选中右边
         */
        void onSelected(int selectedNum);
    }

    public void setWidgetRightImage(String url) {
        widgetImageSubtabRightSdv.setImageURI(Uri.parse(url));
        widgetImageSubtabRightSdv.setAspectRatio(1f);
    }

    public void setWidgetLeftImage(String url) {
        widgetImageSubtabLeftSdv.setImageURI(Uri.parse(url));
        widgetImageSubtabLeftSdv.setAspectRatio(1f);
    }

    public void setWidgetRightNum(int Num) {
        widgetImageSubtabRightNumTv.setEndValue(Num);
        widgetImageSubtabLeftNumTv.setIncrement(1f);
        widgetImageSubtabRightNumTv.setTimeInterval(12); // the time interval (ms) at which the text changes
        widgetImageSubtabRightNumTv.start();
    }

    public void setWidgetLeftNum(int Num) {
        widgetImageSubtabLeftNumTv.setEndValue(Num);
        widgetImageSubtabLeftNumTv.setIncrement(1f);
        widgetImageSubtabLeftNumTv.setTimeInterval(12); // the time interval (ms) at which the text changes
        widgetImageSubtabLeftNumTv.start();
    }

    public void setWidgetRightDes(String des) {
        widgetImageSubtabRightDesTv.setText(des);
    }

    public void setWidgetLeftDes(String des) {
        widgetImageSubtabLeftDesTv.setText(des);
    }
}
