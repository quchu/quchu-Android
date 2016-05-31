package co.quchu.quchu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.toolsfinal.DeviceUtils;
import co.quchu.quchu.R;
import co.quchu.quchu.utils.AppUtil;

/**
 * Created by Nico on 16/5/31.
 */
public class BottomListDialog extends Dialog {

    public String[] mItems;

    @Bind(R.id.llContainer)
    LinearLayout mLlContainer;
    @Bind(R.id.tvCancel)
    TextView tvCancel;

    public BottomListDialog(Context context, String[] items) {
        super(context, R.style.dialog_bottom);
        mItems = items;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dg_bottom_list);
        ButterKnife.bind(this);
        getWindow().getAttributes().windowAnimations = R.style.dialog_bottom;
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);

        if (null==mItems){
            return;
        }


        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getContext().getResources().getDimensionPixelSize(R.dimen.toolbar_item_size));
        for (int i = 0; i < mItems.length; i++) {
            final String strItem = mItems[i];
            TextView textView = new TextView(getContext());
            int margin = (int) getContext().getResources().getDimension(R.dimen.base_margin);
            lp.setMargins(0,0,0,margin);
            textView.setLayoutParams(lp);
            textView.setText(strItem);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP ,24);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(getContext().getResources().getColor(R.color.standard_color_white));
            textView.setBackgroundColor(getContext().getResources().getColor(R.color.standard_color_h1_dark));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUtil.makeCall(getContext(),strItem);
                }
            });
            mLlContainer.addView(textView);
        }
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
