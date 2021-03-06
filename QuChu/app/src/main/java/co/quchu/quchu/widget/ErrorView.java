package co.quchu.quchu.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.quchu.quchu.R;

/**
 * Created by linqipeng on 2016/3/9 10:18
 * email:437943145@qq.com
 * desc:加载错误的页面
 */
public class ErrorView extends FrameLayout {

    TextView massageView;
    TextView actionButtton;
    ImageView loadingView;
    LinearLayout refreshLayout;
    private AnimationDrawable drawable;

    public ErrorView(Context context) {
        super(context);
        init(context);
    }

    public ErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.empty_view, this);
        setVisibility(GONE);
        massageView = (TextView) findViewById(R.id.massage);
        actionButtton = (TextView) findViewById(R.id.action_buttton);
        loadingView = (ImageView) findViewById(R.id.loadingView);
        drawable = (AnimationDrawable) loadingView.getDrawable();
        refreshLayout = (LinearLayout) findViewById(R.id.refreshLayout);
    }

    public void showView(String massage, String actionString, OnClickListener actionListener) {
        setVisibility(VISIBLE);
        refreshLayout.setVisibility(VISIBLE);
        loadingView.setVisibility(INVISIBLE);
        massageView.setText(massage);
        actionButtton.setText(actionString);
        actionButtton.setOnClickListener(actionListener);
    }

    public void showLoading() {
        setVisibility(VISIBLE);
        refreshLayout.setVisibility(INVISIBLE);
        loadingView.setVisibility(VISIBLE);
        drawable.start();
    }

    public void showViewDefault(OnClickListener actionListener) {
        setVisibility(VISIBLE);
        refreshLayout.setVisibility(VISIBLE);
        loadingView.setVisibility(INVISIBLE);
        massageView.setText(R.string.network_error);
        actionButtton.setText(R.string.click_to_retry);
        actionButtton.setOnClickListener(actionListener);
    }

    public void hideView() {
        refreshLayout.setVisibility(GONE);
        loadingView.setVisibility(GONE);
        setVisibility(GONE);
        actionButtton.setOnClickListener(null);
        drawable.stop();
    }



    public void tryHideView() {
        if (null!=refreshLayout){
            refreshLayout.setVisibility(GONE);
        }

        if (null!=loadingView){
            loadingView.setVisibility(GONE);
        }

        setVisibility(GONE);
        actionButtton.setOnClickListener(null);
        drawable.stop();
    }
}
