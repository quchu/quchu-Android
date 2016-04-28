package co.quchu.quchu.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

import co.quchu.quchu.utils.LogUtils;

/**
 * Created by no21 on 2016/4/18.
 * email:437943145@qq.com
 * desc :
 */
public class DampView extends ViewGroup implements NestedScrollingParent {


    private int ORIENTATION = 0;
    public static final int ORIENTATION_VERTICAL = 0;
    public static final int ORIENTATION_HORIZONTAL = 1;


    private RefreshView refreshView;
    private View headView;
    private View footView;


    //刷新中
    private boolean refreshing;
    //头部可见
    private boolean headViewVisiliby;
    //下拉刷新的手势的临界距离
    private float refreshDistance;


    private int childCount;
    private actionListener listener;

    public DampView(Context context) {
        super(context);
    }

    public DampView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DampView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DampView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        LogUtils.e("子View个数:" + childCount);
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();

        if (childCount < 1) {
            return;
        }
        int left;
        int top;
        int childWidth;
        int childHeight;

        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view == headView) {
                top = -view.getMeasuredHeight();
                childHeight = view.getMeasuredHeight();
            } else {
                top = getPaddingTop();
                childHeight = height - getPaddingTop() - getPaddingBottom();
            }
            left = getPaddingLeft();
            childWidth = width - getPaddingRight() - getPaddingLeft();

            view.layout(left, top, left + childWidth, top + childHeight);
        }

        refreshDistance = headView.getMeasuredHeight() * refreshView.getHeadPullRatio();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        childCount = getChildCount();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(sizeWidth, sizeHeight);

//        for (int i = 0; i < childCount; i++) {
//            View view = getChildAt(i);
//            if (view != footView && view != footView) {//scrollView
//                view.measure(MeasureSpec.makeMeasureSpec(
//                        getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
//                        MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
//                        getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY));
//            } else {
//                measureChild(view, widthMeasureSpec, heightMeasureSpec);
//            }
//        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setRefreshView(null);
        setListener(null);
        // TODO: 2016/4/26  测试
    }

    public void setRefreshView(RefreshView refreshView) {
        this.refreshView = new RefreshView();
        this.headView = LayoutInflater.from(getContext()).inflate(this.refreshView.getHeadViewId(), this, false);
        addView(headView, 0);
    }

    public void setListener(actionListener listener) {
        this.listener = new actionListener();
    }

    public void stopRefreshing() {
        refreshing = false;
        pullRelease(true);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }


    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (dyUnconsumed < 0) {//下拉
            headViewVisiliby = true;
            //刷新中关闭阻尼效果
            float offset = refreshing ? (float) dyUnconsumed / 2 : (float) dyUnconsumed / 3;
            onPull(offset);
        } else {
            headViewVisiliby = false;
        }
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (dy > 0 && headViewPushable) {//scrollView向上滑动
            consumed[1] = dy;
            onPull(dy);
        }
    }

    //HeadView是否可以向上移动
    boolean headViewPushable = false;

    /**
     * 下拉
     *
     * @param unconsumed 下拉的距离
     */
    private void onPull(float unconsumed) {

        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view != footView) {
                // TODO: 2016/4/26  方向
                float translation = getTranslation(view) - unconsumed;

                if (translation < 0) {
                    translation = 0;
                    headViewPushable = false;
                } else {
                    if (translation > headView.getHeight()) {
                        translation = headView.getHeight();
                    }
                    headViewPushable = true;
                }

                if (view == headView) {
                    int progress = (int) (Math.abs(translation / headView.getHeight()) * 100);
                    refreshView.headViewProgress(progress);
                }

                view.setTranslationY(translation);
            }
        }
    }


    /**
     * 下拉释放
     */
    private void pullRelease(boolean closeRefresh) {
        headViewVisiliby = false;
        AnimatorSet set = new AnimatorSet();
        set.setDuration(250);
        set.setInterpolator(new DecelerateInterpolator());

        List<Animator> animators = new ArrayList<>(3);
        float translation;
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view == footView) {
                continue;
            }
            translation = getTranslation(view);
            ObjectAnimator animator;
            //下拉距离超过临界值
            if (translation > refreshDistance && !closeRefresh) {
                if (!refreshing) {
                    refreshing = true;
                    listener.onRefresh();
                }
                animator = ObjectAnimator.ofFloat(view, "translationY", translation, refreshDistance);
            } else {
                animator = ObjectAnimator.ofFloat(view, "translationY", translation, 0);
            }
            if (view == headView) {
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float values = (float) animation.getAnimatedValue();
                        refreshView.headViewProgress((int) (Math.abs(values / headView.getHeight()) * 100));
                    }
                });
            }
            animators.add(animator);
        }
        set.playTogether(animators);
        set.start();
    }


    private float getTranslation(View view) {
        if (ORIENTATION == ORIENTATION_VERTICAL) {
            return view.getTranslationY();
        }
        return view.getTranslationX();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                if (headViewVisiliby) {
                    pullRelease(false);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        LogUtils.e("filing:" + velocityY);
        if (velocityY < 0 && !consumed && refreshing) {
            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                if (view != footView) {
                    view.setTranslationY(refreshDistance);
                }
            }

        }

        return true;
    }


    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
    }


    public void onStopNestedScroll(View target) {
    }

    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }


    public int getNestedScrollAxes() {
        return ViewCompat.SCROLL_AXIS_VERTICAL;
    }


    public class actionListener {
        void onRefresh() {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {

                    try {
                        Thread.sleep(9000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    stopRefreshing();

                }
            }.execute();
        }

        void onLoadMore() {
        }
    }

}
