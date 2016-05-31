package co.quchu.quchu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;

/**
 * Created by Nikolai on 2016/5/23.
 */
public class QuchuDetailsMoreDialog extends Dialog {

    public OnButtonClickListener mListener;
    public QuchuDetailsMoreDialog(Context context) {
        super(context, R.style.dialog_bottom);
    }


    @OnClick({R.id.tvCloseAll,R.id.tvPreOrder,R.id.tvShare,R.id.tvCancel})
    public void detailClick(View v) {
        if (null!=mListener){
            switch (v.getId()){
                case R.id.tvCloseAll:
                    mListener.onReturnClick();
                    break;
                case R.id.tvPreOrder:
                    mListener.onPreOrderClick();
                    break;
                case R.id.tvShare:
                    mListener.onShareClick();
                    break;
                case R.id.tvCancel:
                    break;
            }
            dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dg_bottom_quchu_detail_more);
        ButterKnife.bind(this);
        getWindow().getAttributes().windowAnimations = R.style.dialog_bottom;
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);
    }


    public void setOnButtonClickListener(OnButtonClickListener listener){
        mListener = listener;
    }

    public interface OnButtonClickListener{
        void onReturnClick();
        void onPreOrderClick();
        void onShareClick();
    }


}