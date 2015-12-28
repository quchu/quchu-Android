package co.quchu.quchu.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Pattern;

import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;

/**
 * StringUtils
 * User: Chenhs
 * Date: 2015-10-19
 * 字符串工具类
 */
public class StringUtils {

    public static boolean isFile(String path) {

        return new File(path).isFile();
    }

    public static String getRealPath() {
        String sdDir = null;
        if (Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED)) {
            sdDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Quchu/";//获取跟目录
            File src = new File(sdDir);
            if (!src.exists()) {
                src.mkdirs();
            }
            return sdDir;
        } else {
            File src = new File("/sdcard/Quchu/");
            if (!src.exists()) {
                src.mkdirs();
            }
            return src.getAbsolutePath();
        }
    }

    public static String getRealPath(String fileName) {
        return getRealPath() + fileName;
    }

    /**
     * json 本地保存
     *
     * @param response
     */
    public static void SaveLoca(String response) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File file_name = null;
            try {
                file_name = new File(getRealPath() + "Quchu.txt");
                if (!file_name.exists()) {
                    File dir = new File(file_name.getParent());
                    dir.mkdirs();
                    file_name.createNewFile();
                }
            } catch (final IOException e1) {
                e1.printStackTrace();
            }
            try {
                final OutputStream os = new FileOutputStream(file_name);
                os.write(response.getBytes());
                os.close();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
//        LogUtils.json(scale+" display");
        return (int) (dpValue * scale + 0.5f);
    }

    public static int pt2sp(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().xdpi;
        final float scd = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (dpValue * scale * (1.0f / (72 * scd)));
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取设备uuid
     *
     * @return
     */
    public static String getMyUUID() {
        String MyUUID = SPUtils.getValueFromSPMap(AppContext.mContext,
                AppKey.UUID);
        if (StringUtils.isEmpty(MyUUID)) {
            TelephonyManager tm = (TelephonyManager) AppContext.mContext
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String tmDevice, tmSerial, tmPhone, androidId;
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = ""
                    + android.provider.Settings.Secure.getString(
                    AppContext.mContext.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
            UUID deviceUuid = new UUID(androidId.hashCode(),
                    ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            String uniqueId = deviceUuid.toString();
            SPUtils.putValueToSPMap(AppContext.mContext, AppKey.UUID, uniqueId);
            return uniqueId;
        } else {
            return MyUUID;
        }
    }

    /**
     * 判断字符串是否为空
     *
     * @param input
     * @return
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 文字高亮处理
     *
     * @param view       Textview
     * @param startIndex 起始位置
     * @param endIndex   结束位置
     */
    public static void setTextHighlighting(TextView view, int startIndex, int endIndex) {
        SpannableStringBuilder builder = new SpannableStringBuilder(view.getText().toString());
        ForegroundColorSpan redSpan = new ForegroundColorSpan(view.getResources().getColor(R.color.planet_progress_yellow));
        builder.setSpan(redSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(builder);
    }

    /**
     * 修改部分文字颜色
     *
     * @param view       textview
     * @param startIndex 起始位置
     * @param endIndex   需改变的文字字数  结束位置
     * @param mColor     颜色id
     */
    public static void alterTextColor(TextView view, int startIndex, int endIndex, int mColor) {
        SpannableStringBuilder builder = new SpannableStringBuilder(view.getText().toString());
        ForegroundColorSpan redSpan = new ForegroundColorSpan(view.getResources().getColor(mColor));
        builder.setSpan(redSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(builder);
    }

    /**
     * 文字加粗高亮
     *
     * @param view
     * @param startIndex
     * @param endIndex
     * @param mColor
     */
    public static void alterBoldTextColor(TextView view, int startIndex, int endIndex, int mColor) {
        SpannableStringBuilder builder = new SpannableStringBuilder(view.getText().toString());
        ForegroundColorSpan redSpan = new ForegroundColorSpan(view.getResources().getColor(mColor));
        builder.setSpan(redSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(builder);
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
        移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
        联通：130、131、132、152、155、156、185、186
        电信：133、153、180、189、（1349卫通）
        总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
        "/`((1[3,5,8][0-9])|(14[5,7])|(17[0,6,7,8]))\\d{8}$/

        ^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$
        */
        String telRegex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        //  String telRegex = "`((1[3,5,8][0-9])|(14[5,7])|(17[0,6,7,8]))\\\\d{8}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        //  String telRegex = "((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return Pattern.matches(telRegex, mobiles);
    }

    /**
     * 字符串 以， 切割 返回string List
     *
     * @param str 待切割字符串
     * @return ArrayList
     */
    public static ArrayList<String> convertStrToArray(String str) {
        String[] strArray = null;
        strArray = str.split(","); //拆分字符为"," ,然后把结果交给数组strArray
        ArrayList<String> nearByList = new ArrayList<String>();
        for (int i = 0; i < strArray.length; i++) {
            nearByList.add(strArray[i]);
        }
        return nearByList;
    }


}
