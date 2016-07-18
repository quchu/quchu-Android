package co.quchu.quchu.base;


import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.quchu.quchu.R;


/**
 * Created by Nico on 16/4/13.
 */
public class EnhancedToolbar extends Toolbar {

    public static final int TYPE_LEFT_TV = 0x1001;
    public static final int TYPE_RIGHT_TV = 0x1002;
    public static final int TYPE_LEFT_IV = 0x2001;
    public static final int TYPE_RIGHT_IV = 0x2002;
    public static final int TYPE_TITLE_TV = 0x3001;
    public static final int TYPE_CUSTOM_V = 0x4001;
    private RelativeLayout rlContent;

    public EnhancedToolbar(Context context) {
        super(context);
        initSelf();

    }

    public EnhancedToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSelf();

    }

    public EnhancedToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSelf();
    }


    protected void initSelf() {
        if (getChildCount() <= 0) {
            rlContent = new RelativeLayout(new ContextThemeWrapper(getContext(), R.style.ToolbarContainer));
            addView(rlContent, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            setContentInsetsRelative(0, 0);
        }
    }

    public void show(){
        this.setVisibility(View.VISIBLE);
    }

    public void hide(){
        this.setVisibility(View.GONE);
    }


    protected int getActionViewId(int type) {
        int childViewId = -1;
        switch (type) {
            case TYPE_LEFT_TV:
                childViewId = R.id.toolbar_tv_left;
                break;
            case TYPE_RIGHT_TV:
                childViewId = R.id.toolbar_tv_right;
                break;
            case TYPE_LEFT_IV:
                childViewId = R.id.toolbar_iv_left;
                break;
            case TYPE_RIGHT_IV:
                childViewId = R.id.toolbar_iv_right;
                break;
            case TYPE_TITLE_TV:
                childViewId = R.id.toolbar_tv_title;
                break;
            case TYPE_CUSTOM_V:
                childViewId = R.id.toolbar_v_custom;
                break;
        }
        return childViewId;
    }


    protected View getActionView(int type) {
        return findViewById(getActionViewId(type));
    }

    public TextView getLeftTv() {
        return null == findViewById(R.id.toolbar_tv_left) ? addLeftTv() : (TextView) findViewById(getActionViewId(TYPE_LEFT_TV));
    }

    public ImageView getLeftIv() {
        return null == findViewById(R.id.toolbar_iv_left) ? addLeftIv() : (ImageView) findViewById(getActionViewId(TYPE_LEFT_IV));
    }

    public TextView getRightTv() {
        return null == findViewById(R.id.toolbar_tv_right) ? addRightTv() : (TextView) findViewById(getActionViewId(TYPE_RIGHT_TV));
    }

    public ImageView getRightIv() {
        return null == findViewById(R.id.toolbar_iv_right) ? addRightIv() : (ImageView) findViewById(getActionViewId(TYPE_RIGHT_IV));
    }

    public TextView getTitleTv() {

        TextView v = null == findViewById(R.id.toolbar_tv_title) ? addTitleTv() : (TextView) findViewById(getActionViewId(TYPE_TITLE_TV));
        if (null!=v){
            v.setTypeface(null, Typeface.BOLD);
        }
        return v;
    }


    private TextView addLeftTv() {
        return (TextView) addActionView(TYPE_LEFT_TV);
    }

    private ImageView addLeftIv() {
        return (ImageView) addActionView(TYPE_LEFT_IV);
    }

    private TextView addRightTv() {
        return (TextView) addActionView(TYPE_RIGHT_TV);
    }

    private ImageView addRightIv() {
        return (ImageView) addActionView(TYPE_RIGHT_IV);
    }

    private TextView addTitleTv() {
        return (TextView) addActionView(TYPE_TITLE_TV);
    }

    public View addCustomView(View v, ViewGroup.LayoutParams layoutParams) {
        return addCustomChildView(v, layoutParams);
    }


    protected View addCustomChildView(View v, ViewGroup.LayoutParams layoutParams) {
        rlContent.addView(v, layoutParams);
        return v;
    }

    protected View addActionView(int type) {
        View v = null;
        int itemSize = getResources().getDimensionPixelSize(R.dimen.toolbar_item_size);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(itemSize, itemSize);

        switch (type) {
            case TYPE_LEFT_TV:
                v = new TextView(new ContextThemeWrapper(getContext(), R.style.ToolbarItem_ActionTextView));
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                lp.addRule(RelativeLayout.CENTER_VERTICAL);
                lp.width = LinearLayoutCompat.LayoutParams.WRAP_CONTENT;
                break;
            case TYPE_RIGHT_TV:
                v = new TextView(new ContextThemeWrapper(getContext(), R.style.ToolbarItem_ActionTextView));
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                lp.addRule(RelativeLayout.CENTER_VERTICAL);
                lp.setMargins(0,0,getResources().getDimensionPixelSize(R.dimen.half_margin),0);
                lp.width = LinearLayoutCompat.LayoutParams.WRAP_CONTENT;
                break;
            case TYPE_LEFT_IV:
                v = new ImageView(new ContextThemeWrapper(getContext(), R.style.ToolbarItem));
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                lp.addRule(RelativeLayout.CENTER_VERTICAL);
                ((ImageView) v).setScaleType(ImageView.ScaleType.CENTER);
                break;
            case TYPE_RIGHT_IV:
                v = new ImageView(new ContextThemeWrapper(getContext(), R.style.ToolbarItem));
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                lp.addRule(RelativeLayout.CENTER_VERTICAL);
                ((ImageView) v).setScaleType(ImageView.ScaleType.CENTER);
                break;
            case TYPE_TITLE_TV:
                v = new TextView(new ContextThemeWrapper(getContext(), R.style.ToolbarItem_TitleTextView));
                lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, itemSize);
                lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                break;

        }
        v.setId(getActionViewId(type));
        rlContent.addView(v, lp);
        return v;
    }


}
