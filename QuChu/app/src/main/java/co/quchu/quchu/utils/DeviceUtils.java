package co.quchu.quchu.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;

import co.quchu.quchu.R;

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

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }
}
