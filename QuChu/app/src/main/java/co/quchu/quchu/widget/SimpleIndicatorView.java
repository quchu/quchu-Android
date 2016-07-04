package co.quchu.quchu.widget;


import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by Nico on 16/7/1.
 */
public class SimpleIndicatorView extends View {

    ValueAnimator mAnimator;

    public SimpleIndicatorView(Context context) {
        super(context);
        init();
    }

    public SimpleIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SimpleIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mAnimator = ValueAnimator.ofFloat(0, 1);
        mPaintIndicators = new Paint();
        mPaintIndicators.setStrokeWidth(2);
        mPaintIndicators.setColor(Color.parseColor("#eaeaea"));
        mPaintIndicators.setStyle(Paint.Style.FILL);
        mPaintIndicators.setAntiAlias(true);
    }

    int mIndicators = 4;
    int mSelectIndex = 0;
    int mLastIndex = -1;
    float mAnimateProgress = 1;


    Paint mPaintIndicators;
    public void setIndicators(int indicators){
        mIndicators = indicators;
    }

    public void setCurrentIndex(int i) {
        if (i==mSelectIndex){
            return;
        }
        mLastIndex = mSelectIndex;
        mSelectIndex = i;

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,1);
        valueAnimator.setDuration(250);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimateProgress = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float weight = (mIndicators * 2)-1;
        float width = getWidth()/weight;
        float radius = width/2;
        float padding = (getWidth()-(weight*radius*2))/2;

        for (int i = 0; i < weight; i++) {
            if ((i+1)%2!=0){
                int index = i/2;
                float zoomLevel = .75f;
                if (index==mSelectIndex){
                    zoomLevel = 1f;
                }
                float x =(width*i)+radius+padding;
                if (index == mLastIndex){
                    zoomLevel = 1-(mAnimateProgress * .25f);
                }else if(index == mSelectIndex){
                    zoomLevel = (mAnimateProgress * .25f)+.75f;
                }

                canvas.drawCircle(x,getHeight()/2,radius*zoomLevel,mPaintIndicators);


            }
        }

    }


}

