package co.quchu.quchu.utils;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by mwb on 16/8/19.
 */
public class ToastManager {

    private static ToastManager sInstance;

    private Toast sToastLast;
    private int sResIdLast;
    private String sStringLast;
    private long sShowTimeLast;

    private Context mContext;
    private final Handler mHandler;

    public static ToastManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ToastManager();
        }
        sInstance.mContext = context;
        return sInstance;
    }

    private ToastManager() {
        mHandler = new Handler();
    }

    private Runnable mRunnable = new Runnable() {
        public void run() {
            if (mContext != null) {
                sToastLast = Toast.makeText(mContext, sResIdLast, Toast.LENGTH_SHORT);
                sToastLast.show();
                sShowTimeLast = System.currentTimeMillis();
            }
        }
    };

    private Runnable mRunnableString = new Runnable() {
        @Override
        public void run() {
            if (mContext != null) {
                sToastLast = Toast.makeText(mContext, sStringLast, Toast.LENGTH_SHORT);
                sToastLast.show();
                sShowTimeLast = System.currentTimeMillis();
            }
        }
    };

    public void show(int nResId) {
        if (nResId == sResIdLast) {
            if (System.currentTimeMillis() - sShowTimeLast < 2000) {
                return;
            }
        }
        if (sToastLast != null) {
            sToastLast.cancel();
        }

        sResIdLast = nResId;
        mHandler.removeCallbacks(mRunnable);
        mHandler.post(mRunnable);
    }

    public void show(final String strText) {
        if (TextUtils.isEmpty(strText)) {
            return;
        }
        if (strText.equals(sStringLast)) {
            if (System.currentTimeMillis() - sShowTimeLast < 2000) {
                return;
            }
        }

        if (sToastLast != null) {
            sToastLast.cancel();
        }

        sStringLast = strText;
        mHandler.removeCallbacks(mRunnableString);
        mHandler.post(mRunnableString);
    }
}
