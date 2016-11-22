package co.quchu.quchu.widget;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import java.io.File;

/**
 * Fresco SimpleDraweeView 封装
 * <p>
 * Created by mwb on 16/11/22.
 */
public class SmartImageView extends SimpleDraweeView {

  private ImageLoadingListener mImageLoadingListener;
  private ControllerListener<ImageInfo> mListener;

  public SmartImageView(Context context, GenericDraweeHierarchy hierarchy) {
    super(context, hierarchy);
    init();
  }

  public SmartImageView(Context context) {
    super(context);
    init();
  }

  public SmartImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public SmartImageView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  public SmartImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init();
  }

  private void init() {
    mListener = new BaseControllerListener<ImageInfo>() {

      @Override
      public void onSubmit(String id, Object callerContext) {
        if (mImageLoadingListener != null) {
          mImageLoadingListener.onStart();
        }
      }

      @Override
      public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
        //图片加载成功
        if (mImageLoadingListener != null) {
          mImageLoadingListener.onSuccess(imageInfo);
        }
      }

      @Override
      public void onFailure(String id, Throwable throwable) {
        //图片加载失败
        if (mImageLoadingListener != null) {
          mImageLoadingListener.onFailure();
        }
      }

      @Override
      public void onRelease(String id) {
        if (mImageLoadingListener != null) {
          mImageLoadingListener.onRelease();
        }
      }
    };
  }

  @Override
  public void setImageURI(Uri uri, @Nullable Object callerContext) {
    PipelineDraweeControllerBuilder controllerBuilder = Fresco.newDraweeControllerBuilder();
    controllerBuilder.setUri(uri);
    controllerBuilder.setCallerContext(callerContext);
    controllerBuilder.setOldController(getController());
    controllerBuilder.setControllerListener(mListener);
    setController(controllerBuilder.build());
  }

  /**
   * 设置占位图
   */
  public void setPlaceholderImage(int resId) {
    getHierarchy().setPlaceholderImage(resId);
  }

  public void setPlaceholderImage(Drawable resId) {
    getHierarchy().setPlaceholderImage(resId);
  }

  /**
   * 设置背景图
   */
  public void setBackgroundImage(Drawable drawable) {
    getHierarchy().setBackgroundImage(drawable);
  }

  /**
   * 设置叠加图
   */
  public void setOverlayImage(Drawable drawable) {
    getHierarchy().setOverlayImage(drawable);
  }

  /**
   * 加载资源图片
   */
  public void setImageUrl(int drawableId) {
//    Uri uri = new Uri.Builder().scheme("res").path(String.valueOf(drawableId)).build();
    setImageURI(Uri.parse("res:///" + drawableId));
  }

  /**
   * 加载网络图片
   */
  public void setImageUrl(String url) {
    if (checkUrl(url)) {
      setImageURI(Uri.parse(url));
    } else {
      setImageURI(Uri.parse(""));
    }
  }

  /**
   * 设置圆角
   */
  public void setCornersRadius(float cornersRadius) {
    RoundingParams roundingParams = RoundingParams.fromCornersRadius(cornersRadius);
    getHierarchy().setRoundingParams(roundingParams);
  }

  /**
   * 加载SD卡图片
   */
  public void setImageUrlSD(String url) {
    if (!TextUtils.isEmpty(url)) {
      setImageURI(Uri.fromFile(new File(url)));
    } else {
      setImageURI(Uri.parse(""));
    }
  }

  /**
   * 判断Url是否可用
   */
  public static boolean checkUrl(String url) {
    if (!TextUtils.isEmpty(url)) {
      Uri uri = Uri.parse(url);
      if (UriUtil.isNetworkUri(uri)) {
        return true;
      } else if (UriUtil.isLocalFileUri(uri)) {
        return true;
      } else if (UriUtil.isLocalContentUri(uri)) {
        return true;
      } else if (UriUtil.isLocalAssetUri(uri)) {
        return true;
      } else return UriUtil.isLocalResourceUri(uri);
    }
    return false;
  }

  /**
   * 加载监听
   */
  public interface ImageLoadingListener {

    void onStart();

    void onSuccess(ImageInfo imageInfo);

    void onFailure();

    void onRelease();
  }

  public void setImageLoadingListener(ImageLoadingListener mImageLoadingListener) {
    this.mImageLoadingListener = mImageLoadingListener;
  }

}
