package co.quchu.quchu.widget.textcounter;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import co.quchu.quchu.R;
import co.quchu.quchu.utils.LogUtils;

/**
 * PullMenusView
 * User: Chenhs
 * Date: 2015-12-02
 */
public class PullMenusView extends RelativeLayout implements View.OnClickListener {
    private SimpleDraweeView widgetMenusAvatarSdv;
    private RelativeLayout widgetMenusAvatarRl;
    private ImageView widgetMenusMessageIv;
    private ImageView widgetMenusSettingIv;
    private ImageView widgetMenusHomeIv;
    private Context context;
    private long animatotDuration = 500;
    private boolean isNeedShowAnimation = false;//是否需要动画

    public PullMenusView(Context context) {
        this(context, null);
    }

    public PullMenusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullMenusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.widget_pull_menus_view, this, true);
        initView();
    }

    private float avatarX = 0f, avatarY = 0f, homeX = 0f, homeY = 0f,
            messageX = 0f, messageY = 0f, settingX = 0f, settingY = 0f;

    private void initView() {
        widgetMenusAvatarSdv = (SimpleDraweeView) findViewById(R.id.widget_menus_avatar_sdv);
        widgetMenusAvatarRl = (RelativeLayout) findViewById(R.id.widget_menus_avatar_rl);
        widgetMenusMessageIv = (ImageView) findViewById(R.id.widget_menus_message_iv);
        widgetMenusSettingIv = (ImageView) findViewById(R.id.widget_menus_setting_iv);
        widgetMenusHomeIv = (ImageView) findViewById(R.id.widget_menus_home_iv);
        widgetMenusAvatarSdv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                avatarX = widgetMenusAvatarRl.getX() + widgetMenusAvatarRl.getWidth();
                avatarY = widgetMenusAvatarRl.getY() + widgetMenusAvatarRl.getHeight();

                messageX = widgetMenusMessageIv.getX();
                messageY = widgetMenusMessageIv.getY();
                homeX = widgetMenusHomeIv.getX();
                homeY = widgetMenusHomeIv.getY();
                settingX = widgetMenusSettingIv.getX();
                settingY = widgetMenusSettingIv.getY();
                postInvalidate();
                LogUtils.json("avatarX=" + avatarX + "//avatarY" + avatarY + "//messageX=" + messageX + "//messY=" + messageY + "//homeX=" + homeX + "//homeY==" + homeY + "settingX=" + settingX + "..settingY=" + settingY);
            }
        });
        widgetMenusAvatarRl.setOnClickListener(this);
        widgetMenusMessageIv.setOnClickListener(this);
        widgetMenusSettingIv.setOnClickListener(this);
        widgetMenusHomeIv.setOnClickListener(this);
    }

    public void setAvatar(String avatarUrl) {
        widgetMenusAvatarSdv.setImageURI(Uri.parse(avatarUrl));
        ControllerListener controllerListener = new BaseControllerListener() {
            @Override
            public void onFinalImageSet(String id, @Nullable Object imageInfo, @Nullable Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null) {
                    return;
                }
                if (isNeedShowAnimation) {
                    showItemAnimation();
                }
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable Object imageInfo) {
                super.onIntermediateImageSet(id, imageInfo);
            }
        };


        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setControllerListener(controllerListener)
                .setUri(Uri.parse(avatarUrl))
                .build();
        widgetMenusAvatarSdv.setController(controller);
    }


    private void showItemAnimation() {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator aMessageX = ObjectAnimator.ofFloat(widgetMenusMessageIv, "translationX", avatarX, messageX - avatarX);
        ObjectAnimator aMessageY = ObjectAnimator.ofFloat(widgetMenusMessageIv, "translationY", avatarY, messageY - avatarY);
        ObjectAnimator aHomeY = ObjectAnimator.ofFloat(widgetMenusHomeIv, "translationX", avatarX, homeX - avatarX);
        ObjectAnimator aHomeX = ObjectAnimator.ofFloat(widgetMenusHomeIv, "translationY", avatarY, homeY - avatarY);
        ObjectAnimator aSettingX = ObjectAnimator.ofFloat(widgetMenusSettingIv, "translationX", avatarX, settingX - avatarX);
        ObjectAnimator aSettingY = ObjectAnimator.ofFloat(widgetMenusSettingIv, "translationY", avatarY, settingY - avatarY);
        animatorSet.playTogether(aMessageX, aMessageY, aHomeX, aHomeY, aSettingX, aSettingY);
        animatorSet.setDuration(animatotDuration);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
           /*     widgetMenusMessageIv.setX(avatarX);
                widgetMenusMessageIv.setY(avatarY);
                widgetMenusHomeIv.setX(avatarX);
                widgetMenusHomeIv.setY(avatarY);
           */
                widgetMenusMessageIv.setVisibility(View.VISIBLE);
                widgetMenusHomeIv.setVisibility(View.VISIBLE);
                widgetMenusSettingIv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animatorSet.start();
    }

    public static final int ClickAvatar = 0x00;
    public static final int ClickMessage = 0x01;
    public static final int ClickSetting = 0x02;
    public static final int ClickHome = 0x03;

    @Override
    public void onClick(View v) {

        if (listener != null) {
            switch (v.getId()) {
                case R.id.widget_menus_avatar_rl:
                    listener.onItemClick(ClickAvatar);
                    break;
                case R.id.widget_menus_message_iv:
                    listener.onItemClick(ClickMessage);
                    break;
                case R.id.widget_menus_setting_iv:
                    listener.onItemClick(ClickSetting);
                    break;
                case R.id.widget_menus_home_iv:
                    listener.onItemClick(ClickHome);
                    break;

            }
        }
    }

    private PullMenusClickListener listener;

    public void setItemClickListener(PullMenusClickListener listener) {
        this.listener = listener;
    }

    public interface PullMenusClickListener {
        void onItemClick(int itemID);
    }


}
