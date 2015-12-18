package co.quchu.quchu.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.io.ByteArrayOutputStream;

/**
 * ImageUtils
 * User: Chenhs
 * Date: 2015-12-18
 */
public class ImageUtils {
    public static byte[] Bitmap2Bytes(Bitmap bmp, int types) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (null == bmp) {
            LogUtils.json("bitmap =null =================");
        } else {
            bmp.compress(Bitmap.CompressFormat.JPEG, types, baos);
        }
        return baos.toByteArray();
    }
    public static byte[] getBitmapBytes(Bitmap bitmap, boolean paramBoolean) {
        Bitmap localBitmap = Bitmap.createBitmap(80, 80, Bitmap.Config.RGB_565);
        Canvas localCanvas = new Canvas(localBitmap);
        int i;
        int j;
        if (bitmap.getHeight() > bitmap.getWidth()) {
            i = bitmap.getWidth();
            j = bitmap.getWidth();
        } else {
            i = bitmap.getHeight();
            j = bitmap.getHeight();
        }
        while (true) {
            localCanvas.drawBitmap(bitmap, new Rect(0, 0, i, j), new Rect(0, 0,80, 80), null);
            if (paramBoolean)
                bitmap.recycle();

            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 80, localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                return arrayOfByte;
            } catch (Exception e) {

            }
            i = bitmap.getHeight();
            j = bitmap.getHeight();
        }
    }
}
