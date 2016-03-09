package co.quchu.quchu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
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
        massageView = (TextView) findViewById(R.id.massage);
        actionButtton = (TextView) findViewById(R.id.action_buttton);
    }

    public void showView(String massage, String actionString, OnClickListener actionListener) {
        setVisibility(VISIBLE);
        massageView.setText(massage);
        actionButtton.setText(actionString);
        actionButtton.setOnClickListener(actionListener);
    }

    public void showViewDefault(OnClickListener actionListener) {
        setVisibility(VISIBLE);
        massageView.setText("网络发送异常了~~");
        actionButtton.setText(" 刷新 ");
        actionButtton.setOnClickListener(actionListener);
    }

    public void himeView() {
        setVisibility(GONE);
        actionButtton.setOnClickListener(null);
    }

}
