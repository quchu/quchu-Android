package co.quchu.quchu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import co.quchu.quchu.R;

/**
 * MenusSettingDialog
 * User: Chenhs
 * Date: 2015-12-02
 */
public class MenusSettingDialog extends Dialog {

    public MenusSettingDialog(Context context) {
        this(context,"");
    }

    private TextView tv_load;
    private String mText;
    private static MenusSettingDialog dialog;

    public MenusSettingDialog(Context context, String text) {
        super(context, R.style.loading_dialog);
        // TODO Auto-generated constructor stub
        mText = text;
        init();
    }

    private void init() {
        setCancelable(false);
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
