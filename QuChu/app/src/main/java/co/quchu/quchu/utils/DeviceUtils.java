package co.quchu.quchu.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by admin on 2016/3/7.
 */
public class DeviceUtils {

    public static void makeCall(Context context, String strPhoneNum) {
        if (null!=strPhoneNum && !StringUtils.isEmpty(strPhoneNum)) {
            Uri uri = Uri.parse("tel:" + strPhoneNum);
            Intent it = new Intent(Intent.ACTION_DIAL, uri);
            context.startActivity(it);
        }
    }
}
