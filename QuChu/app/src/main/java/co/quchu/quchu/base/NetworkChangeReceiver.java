package co.quchu.quchu.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.utils.EventFlags;

/**
 * Created by Nico on 16/7/13.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(final Context context, final Intent intent) {
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable() || mobile.isAvailable()) {
            EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_DEVICE_NETWORK_AVAILABLE));
        }else{
            EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_DEVICE_NETWORK_UNAVAILABLE));
        }
    }

}
