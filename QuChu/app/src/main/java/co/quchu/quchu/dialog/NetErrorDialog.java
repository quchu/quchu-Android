//package co.quchu.quchu.dialog;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.widget.TextView;
//
//import co.quchu.quchu.R;
//
//
//public class NetErrorDialog extends Dialog {
//
//    private TextView tv_load;
//    private String mText;
//    private static NetErrorDialog dialog;
//
//
//    public NetErrorDialog(Context context, String text) {
//        super(context, R.style.loading_dialog);
//        mText = text;
//        init();
//        dialog = this;
//    }
//
//
//    private void init() {
//        setContentView(R.layout.dialog_net_error);
//        setCancelable(false);
//        tv_load = (TextView) findViewById(R.id.spread_map_tv);
//        tv_load.setText(mText);
//    }
//
//    public void setText(String text) {
//        mText = text;
//        tv_load.setText(mText);
//    }
//
//    @Override
//    public void dismiss() {
//        //dialog != null &&
//        if (dialog != null && isShowing()) {
//            super.dismiss();
//        }
//    }
//
//    public static void showProgess(Context activity) {
//        if (activity != null) {
//            dialog = new NetErrorDialog(activity, "请检查网络");
//
//            try {
//                if (!dialog.isShowing()) {
//                    dialog.show();
//                 /*   if (activity != null && dialog != null)
//                        handler.sendMessageDelayed(handler.obtainMessage(1), 5000);*/
//                }
//            } catch (Exception e) {
//                // TODO: handle exception
//                if (dialog != null) {
//                    dialog.dismiss();
//                    dialog = null;
//                }
//                e.printStackTrace();
//            }
//        }
//    }
//
//}
