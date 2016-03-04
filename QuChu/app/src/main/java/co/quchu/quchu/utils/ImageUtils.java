package co.quchu.quchu.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import co.quchu.quchu.blurdialogfragment.FastBlurHelper;

/**
 * ImageUtils
 * User: Chenhs
 * Date: 2015-12-18
 */
public class ImageUtils {

    public static Bitmap setSaturation(Bitmap bmp, float fact){
        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.setSaturation(fact);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));
        Canvas canvas = new Canvas(bmp);
        canvas.drawBitmap(bmp, 0, 0, paint);
        return bmp;
    }

    public static Bitmap doBlur(Bitmap bitmap,int scaleToWith,int scaleToHeight){
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                        sourceBitmap = RenderScriptBlurHelper.doBlur(sourceBitmap,60,false,getActivity());
//                    }else{
            bitmap = Bitmap.createScaledBitmap(bitmap, scaleToWith, scaleToHeight, false);
            bitmap = FastBlurHelper.doBlur(bitmap, 10, false);
//                    }
//        }
        return bitmap;
    }

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
            localCanvas.drawBitmap(bitmap, new Rect(0, 0, i, j), new Rect(0, 0, 80, 80), null);
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

    public static Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    //存储进SD卡

    public static void saveFile(Bitmap bm, String fileName) throws Exception {
        File dirFile = new File(fileName);
        //检测图片是否存在
        if (dirFile.exists()) {
            dirFile.delete();  //删除原图片
        }
        File myCaptureFile = new File(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        //100表示不进行压缩，70表示压缩率为30%
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
    }

    public static String saveImage2Sd(String starPath) {
        String filePath = starPath;
        Bitmap bitmaps = getimage(starPath);
        try {
            saveFile(bitmaps, FileUtils.SDPATH + "userAvatar.jpg");
            bitmaps.recycle();
            bitmaps = null;
            filePath = FileUtils.SDPATH + "userAvatar.jpg";
        } catch (Exception e) {

            e.printStackTrace();
        }

        return filePath;
    }
}
