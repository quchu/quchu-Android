package co.quchu.quchu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import co.quchu.quchu.R;

/**
 * Created by Nikolai on 2016/5/23.
 */
public class BottomDialog extends Dialog {

    private TextView tvTitle,tvLeft,tvRight;
    private int mtitle;
    private int mValueLeft = 0;
    private int mValueRight = 0;

    private BottomDialog(Context context, int theme) {
        super(context, R.style.dialog_bottom);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.dg_pick_time);
        initialize();
        getWindow().getAttributes().windowAnimations = R.style.dialog_bottom;
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);
    }


    private void initialize() {

    }


}