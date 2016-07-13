package co.quchu.quchu.thirdhelp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

/**
 * QQHelper
 * User: Chenhs
 * Date: 2015-12-25
 */
public class QQHelper {
    public static void share2QQ(final Activity mContext, Tencent mTencent, String shareUrl, String shareTitle) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, shareTitle);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "趣处 - 一千个人，就有一千个趣处");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareUrl);
/*        if (StringUtils.isEmpty(imageUrl)) {*/

            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://7vzrp0.com5.z0.glb.clouddn.com/ic_quchu_logo.png");
      /*  } else {
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
        }*/
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "趣处");
        //  params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, "其他附加功能");
        if (mTencent != null)
            mTencent.shareToQQ(mContext, params, new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    Toast.makeText(mContext, "QQ分享成功!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(UiError uiError) {
                    Toast.makeText(mContext, "QQ分享出错!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel() {
                    Toast.makeText(mContext, "QQ分享取消!", Toast.LENGTH_SHORT).show();
                }
            });
    }

    public static void shareToQzone(final Activity mContext, Tencent mTencent, String shareUrl, String shareTitle) {
//分享类型
        Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, shareTitle);//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "一千个人，就有一千个趣处");//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, shareUrl);//必填
        params.putString(QzoneShare.SHARE_TO_QQ_APP_NAME, "趣处");//必填
        ArrayList<String> imageList = new ArrayList<>();
        imageList.add("http://7vzrp0.com5.z0.glb.clouddn.com/ic_quchu_logo.png");
        params.putStringArrayList(QQShare.SHARE_TO_QQ_IMAGE_URL, imageList);
        //      params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, "图片链接ArrayList");
    /*    if (StringUtils.isEmpty(imageUrl)) {


        } else {
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
        }*/
        if (mTencent != null)
            mTencent.shareToQzone(mContext, params, new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    Toast.makeText(mContext, "QQ空间分享成功!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(UiError uiError) {
                    Toast.makeText(mContext, "QQ空间分享出错!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel() {
                    Toast.makeText(mContext, "QQ空间分享取消!", Toast.LENGTH_SHORT).show();
                }
            });
    }

}
