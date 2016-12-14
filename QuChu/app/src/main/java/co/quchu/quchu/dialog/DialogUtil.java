package co.quchu.quchu.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.afollestad.materialdialogs.MaterialDialog;
import java.util.Timer;
import java.util.TimerTask;

public class DialogUtil {

    static MaterialDialog loadingDialog;

    static Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    dismissProgressDirectly();
                    break;
                case 2:
                    dismissProgress();
                    break;
            }
        }
    };

    public static void showProgress(Context activity, int resId) {
        if (activity != null) {

            if (loadingDialog!=null && loadingDialog.isShowing()){
                loadingDialog.setContent(resId);
            }else{
                loadingDialog = new MaterialDialog.Builder(activity)
                    .content(resId)
                    .progress(true, 0)
                    .show();
            }

            if(!((Activity) activity).isFinishing()) {
                loadingDialog.show();
            }

        }
    }

    public static void showProgress(Context activity, String msg) {
        if (activity != null) {

            if (loadingDialog!=null && loadingDialog.isShowing()){
                loadingDialog.setContent(msg);
            }else{
                loadingDialog = new MaterialDialog.Builder(activity)
                    .content(msg)
                    .progress(true, 0)
                    .show();
            }

            if(!((Activity) activity).isFinishing()) {
                loadingDialog.show();
            }

        }
    }

    public static void showProgress(Context activity, String msg, boolean isCancelable) {
        if (activity != null) {

            if (loadingDialog!=null && loadingDialog.isShowing()){
                loadingDialog.setContent(msg);
            }else{
                loadingDialog = new MaterialDialog.Builder(activity)
                    .content(msg)
                    .progress(true, 0)
                    .cancelable(isCancelable)
                    .show();
            }

            if(!((Activity) activity).isFinishing()) {
                loadingDialog.show();
            }

        }
    }

    public static void dismissProgressDirectly() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            try{
                loadingDialog.dismiss();
            }catch (Exception ex){

            }
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

    public static void dismissProgress() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        }, 500);
    }

    public static void dismissProgress(int resId) {
        dismissProgress();
    }

    public static void dismissProgress(String msg) {
        dismissProgress();
    }

    public static void dismissProgressWithSuccess(int resId) {
        dismissProgress();
    }

    public static void dismissProgressWithSuccess(String msg) {
        dismissProgress();
    }

    public static void dismissProgressWithError(int resId) {
        dismissProgress();
    }

    public static void dismissProgressWithError(String msg) {
        dismissProgress();
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
