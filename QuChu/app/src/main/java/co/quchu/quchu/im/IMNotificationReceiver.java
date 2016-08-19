package co.quchu.quchu.im;

import android.content.Context;

import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * 接收融云推送消息
 *
 * Created by mwb on 16/8/19.
 */
public class IMNotificationReceiver extends PushMessageReceiver {

    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage pushNotificationMessage) {
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage pushNotificationMessage) {
        return false;
    }
}
