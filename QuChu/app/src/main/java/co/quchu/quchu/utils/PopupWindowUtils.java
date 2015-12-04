package co.quchu.quchu.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import co.quchu.quchu.R;
import co.quchu.quchu.view.activity.AccountSettingActivity;
import co.quchu.quchu.view.activity.FeedbackActivity;

/**
 * PopupWindowUtils
 * User: Chenhs
 * Date: 2015-12-03
 */
public class PopupWindowUtils {
    public static void initPopupWindow(final View v, final Activity act) {
        LayoutInflater inflater = LayoutInflater.from(v.getContext());
        // 引入窗口配置文件
        View view = inflater.inflate(R.layout.pop_menus_setting, null);
        //  ( view.findViewById(R.id.pop_root)).setBackground(act.getResources().getDrawable(R.drawable.bb));
        //( view.findViewById(R.id.pop_root)).setBackground(new BitmapDrawable(bg));
        // 创建PopupWindow对象
        final PopupWindow pop = new PopupWindow(view,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, false);
        pop.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(true);
        pop.setAnimationStyle(R.style.mypopwindow_anim_style);
        pop.showAtLocation((View) v.getParent(), Gravity.CENTER, 0, 0);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            // 在dismiss中恢复透明度
            public void onDismiss() {
            }
        });
        (view.findViewById(R.id.pop_root)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });
        (view.findViewById(R.id.pop_menus_setting_tv)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act.startActivity(new Intent(act, AccountSettingActivity.class));
                pop.dismiss();
            }
        });
        (view.findViewById(R.id.pop_menus_setting_feedback_tv)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                act.startActivity(new Intent(act, FeedbackActivity.class));
            }
        });

        (view.findViewById(R.id.pop_menus_setting_aboutus_tv)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });
    }

}
