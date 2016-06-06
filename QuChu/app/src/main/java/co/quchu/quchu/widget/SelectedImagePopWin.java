package co.quchu.quchu.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import java.util.List;

import co.quchu.quchu.BuildConfig;
import co.quchu.quchu.gallery.CoreConfig;
import co.quchu.quchu.gallery.FunctionConfig;
import co.quchu.quchu.gallery.GalleryFinal;
import co.quchu.quchu.gallery.model.PhotoInfo;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.gallery.FrescoImageLoader;

/**
 * Created by linqipeng on 2016/3/15 16:12
 * email:437943145@qq.com
 * desc:
 */
public class SelectedImagePopWin extends PopupWindow {
    private List<PhotoInfo> photo;
    private int maxSize;

    public SelectedImagePopWin(Context mContext, View parent, List<PhotoInfo> photo, int maxSize, final GalleryFinal.OnHanlderResultCallback listener) {
        this.photo = photo;
        this.maxSize = maxSize;
        View view = View
                .inflate(mContext, R.layout.item_popupwindows, null);

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        update();

        Button bt1 = (Button) view
                .findViewById(R.id.item_popupwindows_camera);
        Button bt2 = (Button) view
                .findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view
                .findViewById(R.id.item_popupwindows_cancel);
        initGralley();
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                functionConfig.setMutiSelect(false);
                GalleryFinal.openCamera(1, functionConfig, listener);
                dismiss();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GalleryFinal.openGalleryMuti(1, functionConfig, listener);
                dismiss();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    private FunctionConfig functionConfig;

    private void initGralley() {
        FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
        functionConfigBuilder.setMutiSelectMaxSize(maxSize);
        functionConfigBuilder.setEnableEdit(false);
        functionConfigBuilder.setEnableCrop(true);
        functionConfigBuilder.setEnablePreview(true);
        functionConfigBuilder.setForceCrop(false);//启动强制裁剪功能,一进入编辑页面就开启图片裁剪，不需要用户手动点击裁剪，此功能只针对单选操作
        functionConfigBuilder.setForceCropEdit(true);
        functionConfigBuilder.setFilter(photo);
        functionConfigBuilder.setRotateReplaceSource(true);
        functionConfigBuilder.setSelected(photo);//添加过滤集合
        functionConfig = functionConfigBuilder.build();
        CoreConfig coreConfig = new CoreConfig.Builder(AppContext.mContext, new FrescoImageLoader(AppContext.mContext), null)
                .setDebug(BuildConfig.DEBUG)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(null)
                .setNoAnimcation(true)
                .build();
        GalleryFinal.init(coreConfig);
    }
}
