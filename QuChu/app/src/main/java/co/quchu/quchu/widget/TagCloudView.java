package co.quchu.quchu.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import co.quchu.quchu.R;

/**
 * Created by NeXT on 15-7-29.
 */
public class TagCloudView extends ViewGroup {

    private static final String TAG = TagCloudView.class.getSimpleName();
    private static final int TYPE_TEXT_NORMAL = 1;
    private List<String> tags;

    private LayoutInflater mInflater;
    private OnTagClickListener onTagClickListener;

    private int sizeWidth;
    private int sizeHeight;

    private float mTagSize;
    private int mTagColor;
    private int mBackground;
    private int mViewBorder;
    private int mTagBorderHor;
    private int mTagBorderVer;

    private int mTagResId;
    private boolean mSingleLine;
    private boolean mCanTagClick;


    private static final int DEFAULT_TEXT_COLOR = Color.WHITE;
    private static final int DEFAULT_TEXT_SIZE = 14;
    private static final int DEFAULT_TEXT_BACKGROUND = R.drawable.tag_background;
    private static final int DEFAULT_VIEW_BORDER = 0;
    private static final int DEFAULT_TEXT_BORDER_HORIZONTAL = 8;
    private static final int DEFAULT_TEXT_BORDER_VERTICAL = 5;

    private static final int DEFAULT_TAG_RESID = R.layout.item_tag;
    /*    private static final int DEFAULT_RIGHT_IMAGE = R.mipmap.arrow_right;*/
    private static final boolean DEFAULT_SINGLE_LINE = false;
    private static final boolean DEFAULT_SHOW_RIGHT_IMAGE = false;
    private static final boolean DEFAULT_SHOW_END_TEXT = true;
    private static final boolean DEFAULT_CAN_TAG_CLICK = true;
    private int mItemVerticalMargin = -1;

    public TagCloudView(Context context) {
        this(context, null);
    }

    public TagCloudView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagCloudView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mInflater = LayoutInflater.from(context);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TagCloudView,
                defStyleAttr,
                defStyleAttr
        );

        mTagSize = a.getDimension(R.styleable.TagCloudView_tcvTextSize, DEFAULT_TEXT_SIZE);
        mTagColor = a.getColor(R.styleable.TagCloudView_tcvTextColor, DEFAULT_TEXT_COLOR);
        mBackground = a.getResourceId(R.styleable.TagCloudView_tcvBackground, DEFAULT_TEXT_BACKGROUND);
        mViewBorder = a.getDimensionPixelSize(R.styleable.TagCloudView_tcvBorder, DEFAULT_VIEW_BORDER);
        mTagBorderHor = a.getDimensionPixelSize(
                R.styleable.TagCloudView_tcvItemBorderHorizontal, DEFAULT_TEXT_BORDER_HORIZONTAL);
        mTagBorderVer = a.getDimensionPixelSize(
                R.styleable.TagCloudView_tcvItemBorderVertical, DEFAULT_TEXT_BORDER_VERTICAL);
        mCanTagClick = a.getBoolean(R.styleable.TagCloudView_tcvCanTagClick, DEFAULT_CAN_TAG_CLICK);

        mSingleLine = a.getBoolean(R.styleable.TagCloudView_tcvSingleLine, DEFAULT_SINGLE_LINE);

        mTagResId = a.getResourceId(R.styleable.TagCloudView_tcvTagResId, DEFAULT_TAG_RESID);

        a.recycle();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return (!mCanTagClick && mSingleLine) || super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        if (mSingleLine) {
            //当前行总宽度
            int width = getWidth() - getPaddingRight() - getPaddingLeft();
            //当前行布局完成的宽度
            int currentWidth = 0;
            int baseLeft = getPaddingLeft();
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                //如果当前tag没有超过 父view的宽度
                if ((currentWidth + childView.getMeasuredWidth() + mTagBorderHor) < width) {
                    int left = baseLeft;
                    int top = getPaddingTop() + getPaddingTop();
                    int bottom = top + childView.getMeasuredHeight();
                    int right = left + childView.getMeasuredWidth();

                    childView.layout(left, top, right, bottom);
                    baseLeft = left + childView.getMeasuredWidth() + mTagBorderHor;
                    currentWidth += childView.getMeasuredWidth() + mTagBorderHor;
                } else {
                    //tag位置超过当前行最大宽度
                    childView.setVisibility(GONE);
                }
            }
        }
    }

    /**
     * 计算 ChildView 宽高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 计算 ViewGroup 上级容器为其推荐的宽高
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        //计算 childView 宽高
        measureChildren(widthMeasureSpec, heightMeasureSpec);


        int totalWidth = 0;
        int totalHeight = mTagBorderVer;

        if (mSingleLine) {
            totalHeight = getSingleTotalHeight(sizeWidth, sizeHeight);
        } else {
            totalHeight = getMultiTotalHeight(totalWidth, totalHeight);
        }

        /**
         * 高度根据设置改变
         * 如果为 MATCH_PARENT 则充满父窗体，否则根据内容自定义高度
         */
        setMeasuredDimension(
                sizeWidth,
                (heightMode == MeasureSpec.EXACTLY ? sizeHeight : totalHeight));

    }


    /**
     * 为 singleLine 模式布局，并计算视图高度
     *
     * @param totalWidth
     * @param totalHeight
     * @return
     */
    private int getSingleTotalHeight(int totalWidth, int totalHeight) {
        int childCount = getChildCount();
//        int maxWidth = getMeasuredWidth();
        int maxHeight = getPaddingBottom() + getPaddingTop();

        int currentWidth = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if ((currentWidth += childView.getMeasuredWidth()) < totalWidth) {
                maxHeight = Math.max(maxHeight, childView.getMeasuredHeight());
            } else {
                return maxHeight;
            }
        }
        return maxHeight;
    }

    /**
     * 为 multiLine 模式布局，并计算视图高度
     *
     * @param totalWidth
     * @param totalHeight
     * @return
     */
    private int getMultiTotalHeight(int totalWidth, int totalHeight) {
        int childWidth;
        int childHeight;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();

            totalWidth += childWidth + mViewBorder;

            if (i == 0) {
                totalHeight = childHeight + mViewBorder;
            }
            // + marginLeft 保证最右侧与 ViewGroup 右边距有边界
            if (totalWidth + mTagBorderHor + mViewBorder > sizeWidth) {
                totalWidth = mViewBorder;
                totalHeight += childHeight + mTagBorderVer;
                child.layout(
                        totalWidth + mTagBorderHor,
                        totalHeight - childHeight,
                        totalWidth + childWidth + mTagBorderHor,
                        totalHeight);
                totalWidth += childWidth;
            } else {
                child.layout(
                        totalWidth - childWidth + mTagBorderHor,
                        totalHeight - childHeight,
                        totalWidth + mTagBorderHor,
                        totalHeight);
            }
        }
        return totalHeight + mViewBorder;
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return super.generateLayoutParams(attrs);
    }

    public void setItemBackgound(int resId){
        mItemVerticalMargin = resId;
    }

    public void setTags(List<String> tagList) {
        this.tags = tagList;
        this.removeAllViews();
        if (tags != null && tags.size() > 0) {
            for (int i = 0; i < tags.size(); i++) {
                TextView tagView = (TextView) mInflater.inflate(mTagResId, null);
                if (mTagResId == DEFAULT_TAG_RESID) {
                    tagView.setBackgroundResource(mBackground);
                    tagView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTagSize);
                    tagView.setTextColor(mTagColor);

                }
                LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                tagView.setLayoutParams(layoutParams);
                tagView.setText(tags.get(i));
                tagView.setTag(TYPE_TEXT_NORMAL);
                final int finalI = i;
                tagView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onTagClickListener != null) {
                            onTagClickListener.onTagClick(finalI);
                        }
                    }
                });
                addView(tagView);
            }
        }
        postInvalidate();
    }

    public void singleLine(boolean mSingleLine) {
        this.mSingleLine = mSingleLine;
        this.setTags(tags);
//        requestLayout();
    }

    public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
        this.onTagClickListener = onTagClickListener;
    }

    public interface OnTagClickListener {
        void onTagClick(int position);
    }

}
