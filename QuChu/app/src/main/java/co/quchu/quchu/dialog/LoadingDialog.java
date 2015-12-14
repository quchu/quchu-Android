package co.quchu.quchu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import co.quchu.quchu.R;


public class LoadingDialog extends Dialog {

    private TextView tv_load;
    private String mText;
    private ImageView loading_iv;
    private AnimationDrawable animationDrawable;

    public LoadingDialog(Context context, String text) {
        super(context, R.style.loading_dialog);
        // TODO Auto-generated constructor stub
        mText = text;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_net_loading);
        setCancelable(false);
        loading_iv = (ImageView) findViewById(R.id.loading_iv);
        loading_iv.setImageResource(R.drawable.user_login_progress);
        animationDrawable = (AnimationDrawable) loading_iv.getDrawable();
        loading_iv.setVisibility(View.VISIBLE);
        animationDrawable.start();
        tv_load = (TextView) findViewById(R.id.tv_loading);
        tv_load.setText(mText);
    }

    public void setText(String text) {
        mText = text;
        tv_load.setText(mText);
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
    }
}
