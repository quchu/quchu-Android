package co.quchu.quchu.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

public class DialogUtil {

    static LoadingDialog loadingDialog;

    static Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    dismissProgessDirectly();
                    break;
                case 2:
                    dismissProgess();
                    break;
            }
        }
    };

    public static void showProgess(Context activity, int resId) {
        if (activity != null) {
            String msg = activity.getString(resId);
            if (loadingDialog == null) {
                loadingDialog = new LoadingDialog(activity, msg);
            } else {
                loadingDialog.setText(msg);
            }
            loadingDialog.show();
        }
    }

    public static void showProgess(Context activity, String msg) {
        if (activity != null) {
            if (loadingDialog == null) {
                loadingDialog = new LoadingDialog(activity, msg);
            } else {
                loadingDialog.setText(msg);
            }
            if (!loadingDialog.isShowing()) {
                loadingDialog.show();
             //   handler.sendMessageDelayed(handler.obtainMessage(2), 30 * 1000);
            }
        }
    }

    public static void dismissProgessDirectly() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
        loadingDialog = null;
    }

    public static boolean isDialogShowing() {
        if (loadingDialog != null) {
            return loadingDialog.isShowing();
        } else {
            return false;
        }


    }

    public static void dismissProgess() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        }, 500);
    }

    public static void dismissProgess(int resId) {
        dismissProgess();
    }

    public static void dismissProgess(String msg) {
        dismissProgess();
    }

    public static void dismissProgessWithSuccess(int resId) {
        dismissProgess();
    }

    public static void dismissProgessWithSuccess(String msg) {
        dismissProgess();
    }

    public static void dismissProgessWithError(int resId) {
        dismissProgess();
    }

    public static void dismissProgessWithError(String msg) {
        dismissProgess();
    }
//	
//	public static void showCancleDialog(final Context context) {
//		// TODO Auto-generated method stub
//		final Dialog dialog = new Dialog(context,R.style.MyDialog);
//		dialog.setContentView(R.layout.dialog_cancle_post);
//		dialog.show();
//		dialog.findViewById(R.id.tv_dialog_cancle).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
//		
//		dialog.findViewById(R.id.tv_dialog_yes).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				dialog.dismiss();
//				context.finish();
//				
//			}
//		});
//		int width = ( (WindowManager)context.getSystemService(context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
//  		LayoutParams layoutParams = dialog.getWindow().getAttributes();
//		layoutParams.width=(int) (width*0.7);
//  		dialog.getWindow().setAttributes(layoutParams);
//  		dialog.setCanceledOnTouchOutside(false);
//	}
}
