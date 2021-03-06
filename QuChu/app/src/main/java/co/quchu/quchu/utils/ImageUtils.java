package co.quchu.quchu.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

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


    public static Bitmap doBlur(Bitmap bitmap, int scaleToWith, int scaleToHeight) {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                        sourceBitmap = RenderScriptBlurHelper.doBlur(sourceBitmap,60,false,getActivity());
//                    }else{
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap = Bitmap.createScaledBitmap(bitmap, scaleToWith, scaleToHeight, false);
            bitmap = FastBlurHelper.doBlur(bitmap, 10, false);
            return bitmap;
//                    }
        }
        return null;
//        }
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
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm);
    }

    //存储进SD卡

    public static void saveFile(Bitmap bm, String fileName) throws Exception {
        File dirFile = new File(fileName);
        //检测图片是否存在
        if (dirFile.exists()) {
            dirFile.delete();  //删除原图片
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dirFile));
        //100表示不进行压缩，70表示压缩率为30%
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
    }

    public static String saveImage2Sd(String starPath) {
        String filePath = FileUtils.SDPATH + System.currentTimeMillis() + "userAvatar.jpg";
        Bitmap bitmaps = getimage(starPath);
        try {
            saveFile(bitmaps, filePath);
            bitmaps.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filePath;
    }

    public static void ShowImage(String uri, final SimpleDraweeView view) {

        if (null == uri) {
            return;
        }
        int width;
        int height;

        width = view.getWidth();
        height = view.getHeight();
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(uri))
                .setResizeOptions(new ResizeOptions(width == 0 ? 150 : width, height == 0 ? 150 : height))//图片目标大小
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(view.getController())
                .setImageRequest(imageRequest)
                .build();
        view.setController(controller);
    }
}
