//package co.quchu.quchu.widget;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.animation.AccelerateDecelerateInterpolator;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//
//import com.nineoldandroids.animation.Animator;
//import com.nineoldandroids.animation.ObjectAnimator;
//
//import co.quchu.quchu.R;
//import co.quchu.quchu.utils.KeyboardUtils;
//
//
///**
// * 环形进度条
// */
//public class MoreButtonView extends RelativeLayout implements View.OnClickListener, Animator.AnimatorListener {
//
//
//    private Context context;
//    private ImageView mTitleMore;
//
//    private OnClickListener listener;
//    private long animationDuration = 300;//动画时长
//    ObjectAnimator objectAnimator;
//    private int menuState = 0x00; //0x00 当前状态为+  0x01 当前状态为X
//    private boolean isNeedAnimation = true;
//
//    /**
//     * 初始化
//     *
//     * @param context
//     */
//    public MoreButtonView(Context context) {
//        this(context, null);
//    }
//
//    public MoreButtonView(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public MoreButtonView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        this.context = context;
//        LayoutInflater mInflater = (LayoutInflater) context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        mInflater.inflate(R.layout.title_more_button_view, this, true);
//        mTitleMore = (ImageView) findViewById(R.id.widget_title_more_iv);
//        mTitleMore.setOnClickListener(this);
//    }
//
//    @Override
//    public void setOnClickListener(OnClickListener l) {
//        super.setOnClickListener(l);
//        listener = l;
//    }
//
//
//    public void setImage(int imagedraw) {
//        mTitleMore.setImageDrawable(getResources().getDrawable(imagedraw));
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (KeyboardUtils.isFastDoubleClick())
//            return;
//        if (isNeedAnimation) {
//            if (listener != null)
//                listener.onClick(v);
//            onMenuOpenAnim();
//        } else {
//            if (moreClicklistener != null)
//                moreClicklistener.moreClick();
//        }
//
//    }
//
//
//    private void onMenuOpenAnim() {
//        objectAnimator = ObjectAnimator.ofFloat(mTitleMore, "rotation", 0f, 90);
//        objectAnimator.setDuration(animationDuration);
//        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
//        objectAnimator.addListener(this);
//        objectAnimator.start();
//    }
//
//    @Override
//    public void onAnimationStart(Animator animator) {
//
//    }
//
//    @Override
//    public void onAnimationEnd(Animator animator) {
//        if (moreClicklistener != null)
//            moreClicklistener.moreClick();
//    }
//
//    @Override
//    public void onAnimationCancel(Animator animator) {
//
//    }
//
//    @Override
//    public void onAnimationRepeat(Animator animator) {
//
//    }
//
//    private MoreClicklistener moreClicklistener;
//
//    public void setMoreClick(MoreClicklistener moreClicklistener) {
//        this.moreClicklistener = moreClicklistener;
//    }
//
//    public interface MoreClicklistener {
//            void moreClick();
//    }
//
//    public void isNeedAnimation(boolean isNeed) {
//        isNeedAnimation = isNeed;
//    }
//}
