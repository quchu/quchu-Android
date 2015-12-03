package co.quchu.quchu.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import co.quchu.quchu.R;

/**
 * PopupWindowUtils
 * User: Chenhs
 * Date: 2015-12-03
 */
public class PopupWindowUtils {
    public static void initPopupWindow(final View v, final Activity act, Bitmap bg) {
     /*   InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);*/
        LayoutInflater inflater = LayoutInflater.from(v.getContext());
        // 引入窗口配置文件
        View view = inflater.inflate(R.layout.pop_menus_setting, null);
        ( view.findViewById(R.id.pop_root)).setBackground(new BitmapDrawable(bg));
        // 创建PopupWindow对象
        final PopupWindow pop = new PopupWindow(view,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, false);

        // 需要设置一下此参数，点击外边可消失
    /*    ColorDrawable cd = new ColorDrawable(0x000000);
        pop.setBackgroundDrawable(cd);
        WindowManager.LayoutParams lp = act.getWindow().getAttributes();
        lp.alpha = 0.4f;
        act.getWindow().setAttributes(lp);*/
        // 设置点击窗口外边窗口消失
        pop.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(true);
        pop.setAnimationStyle(R.style.mypopwindow_anim_style);
        pop.showAtLocation((View) v.getParent(), Gravity.CENTER, 0, 0);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            // 在dismiss中恢复透明度
            public void onDismiss() {
              /*  WindowManager.LayoutParams lp = act.getWindow().getAttributes();
                lp.alpha = 1f;
                act.getWindow().setAttributes(lp);*/
            }
        });
        ( view.findViewById(R.id.pop_root)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });
    }

}
