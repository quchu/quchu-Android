package co.quchu.galleryfinal.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Desction:为了兼容fresco框架而自定义的ImageView
 * Author:pengjianbo
 * Date:2015/12/24 0024 20:14
 */
public class GFImageView extends ImageView {

    private OnImageViewListener mOnImageViewListener;

    public GFImageView(Context context) {
        super(context);
    }

    public GFImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GFImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnImageViewListener(OnImageViewListener listener) {
        mOnImageViewListener = listener;
    }

    public static interface OnImageViewListener {
        public void onDetach();
        public void onAttach();
        public boolean verifyDrawable(Drawable dr) ;
    }

    @Override
    protected void onDetachedFromWindow() {
        if(mOnImageViewListener != null) {
            mOnImageViewListener.onDetach();
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        if(mOnImageViewListener != null) {
            mOnImageViewListener.onAttach();
        }
        super.onAttachedToWindow();
    }

    @Override
    protected boolean verifyDrawable(Drawable dr) {
        if(mOnImageViewListener != null) {
            if (mOnImageViewListener.verifyDrawable(dr)) {
                return true;
            }
        }
        return super.verifyDrawable(dr);
    }

    @Override
    public void onStartTemporaryDetach() {
        super.onStartTemporaryDetach();
        if(mOnImageViewListener != null) {
            mOnImageViewListener.onDetach();
        }
    }

    @Override
    public void onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach();
        if(mOnImageViewListener != null) {
            mOnImageViewListener.onAttach();
        }
    }
}
