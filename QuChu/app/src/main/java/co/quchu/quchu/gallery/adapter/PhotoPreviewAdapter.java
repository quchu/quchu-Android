//package co.quchu.quchu.gallery.adapter;
//
//import android.app.Activity;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.util.DisplayMetrics;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.facebook.drawee.view.SimpleDraweeView;
//
//import java.io.File;
//import java.util.List;
//
//import cn.finalteam.toolsfinal.DeviceUtils;
//import co.quchu.quchu.R;
//import co.quchu.quchu.gallery.model.PhotoInfo;
//import co.quchu.quchu.gallery.utils.ImageUtils;
//
///**
// * Desction:
// * Author:pengjianbo
// * Date:2015/12/29 0029 15:53
// */
//public class PhotoPreviewAdapter extends ViewHolderRecyclingPagerAdapter<PhotoPreviewAdapter.PreviewViewHolder, PhotoInfo> {
//
//    private Activity mActivity;
//    private DisplayMetrics mDisplayMetrics;
//
//    public PhotoPreviewAdapter(Activity activity, List<PhotoInfo> list) {
//        super(activity, list);
//        this.mActivity = activity;
//        this.mDisplayMetrics = DeviceUtils.getScreenPix(mActivity);
//    }
//
//    @Override
//    public PreviewViewHolder onCreateViewHolder(ViewGroup parent, int position) {
//        View view = getLayoutInflater().inflate(R.layout.gf_adapter_preview_viewpgaer_item, null);
//        return new PreviewViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(PreviewViewHolder holder, int position) {
//        PhotoInfo photoInfo = getDatas().get(position);
//        String path = "";
//        if (photoInfo != null) {
//            path = photoInfo.getPhotoPath();
//        }
//        holder.mImageView.setImageResource(R.drawable.ic_gf_default_photo);
//        Drawable defaultDrawable = mActivity.getResources().getDrawable(R.drawable.ic_gf_default_photo);
////        GalleryFinal.getCoreConfig().getImageLoader().displayImage(mActivity, path, holder.mImageView,
//// defaultDrawable, mDisplayMetrics.widthPixels/2, mDisplayMetrics.heightPixels/2);
//        ImageUtils.ShowImage(Uri.fromFile(new File(path)), holder.mImageView,
//                mDisplayMetrics.widthPixels / 2, mDisplayMetrics.heightPixels / 2);
//    }
//
//    static class PreviewViewHolder extends ViewHolderRecyclingPagerAdapter.ViewHolder {
//        SimpleDraweeView mImageView;
//
//        public PreviewViewHolder(View view) {
//            super(view);
//            mImageView = (SimpleDraweeView) view;
//        }
//    }
//}
