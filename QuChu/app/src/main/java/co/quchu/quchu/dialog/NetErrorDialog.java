package co.quchu.quchu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import co.quchu.quchu.R;


public class NetErrorDialog extends Dialog {

    private TextView tv_load;
    private String mText;
    private static NetErrorDialog dialog;
    /* static Handler handler = new Handler() {
         public void handleMessage(Message msg) {
             switch (msg.what) {
                 case 1:
                     if (dialog != null && dialog.isShowing()) {
                         dialog.dismiss();
                         dialog = null;
                     }
                     break;
             }
         }
     };*/
    private Context mContext;

    public NetErrorDialog(Context context, String text) {
        super(context, R.style.loading_dialog);
        // TODO Auto-generated constructor stub
        mText = text;
        mContext = context;
        init();
        dialog = this;
    }


    private void init() {
        setContentView(R.layout.dialog_net_error);
        setCancelable(false);
        tv_load = (TextView) findViewById(R.id.spread_map_tv);
        tv_load.setText(mText);
    }

    public void setText(String text) {
        mText = text;
        tv_load.setText(mText);
    }

    @Override
    public void dismiss() {
        //dialog != null &&
        if (isShowing()) {
            super.dismiss();
        }
    }

    public static void showProgess(Context activity) {
        if (activity != null) {
            if (dialog == null) {
                dialog = new NetErrorDialog(activity, "请检查网络");
            }
            try {
                if (!dialog.isShowing()) {
                    dialog.show();
                 /*   if (activity != null && dialog != null)
                        handler.sendMessageDelayed(handler.obtainMessage(1), 5000);*/
                }
            } catch (Exception e) {
                // TODO: handle exception
                dialog.dismiss();
                dialog = null;
                e.printStackTrace();
            }
        }
    }

}
