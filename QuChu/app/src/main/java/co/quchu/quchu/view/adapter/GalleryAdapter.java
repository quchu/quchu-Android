package co.quchu.quchu.view.adapter;

/**
 * Created by Nico on 16/6/28.
 */

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

import co.quchu.quchu.R;
import co.quchu.quchu.model.ImageModel;
import co.quchu.quchu.view.activity.PhotoViewActivity;

/**
 * Created by Nico on 16/4/7.
 */
public class GalleryAdapter extends PagerAdapter {

    private List<ImageModel> mData;
    private OnGalleryItemClickListener mListener;


    public void setListener(OnGalleryItemClickListener mListener) {
        this.mListener = mListener;
    }

    public GalleryAdapter(List<ImageModel> pData) {
        this.mData = pData;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        final View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_sdv, container, false);
        container.addView(view);


        final ImageRequestBuilder imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(mData.get(position).getPath()));

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(((SimpleDraweeView) view).getController())
                .setControllerListener(new ControllerListener<ImageInfo>() {
                    @Override
                    public void onSubmit(String id, Object callerContext) { }

                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) { }

                    @Override
                    public void onIntermediateImageSet(String id, ImageInfo imageInfo) {

                        float fact = 1;
                        if (view.getWidth()>imageInfo.getWidth() && view.getHeight()>imageInfo.getHeight()){
                            if (view.getHeight() <view.getWidth()){
                                fact = view.getHeight()/imageInfo.getHeight();
                            }else{
                                fact = view.getWidth()/imageInfo.getWidth();
                            }
                        }else if(view.getHeight()>imageInfo.getHeight()){
                            fact = view.getHeight()/imageInfo.getHeight();
                        }else if(view.getWidth()>imageInfo.getWidth()){
                            fact = view.getWidth()/imageInfo.getWidth();
                        }
                        imageRequest.setResizeOptions(new ResizeOptions((int)(imageInfo.getWidth()*fact),(int)(imageInfo.getHeight()*fact)));
                    }

                    @Override
                    public void onIntermediateImageFailed(String id, Throwable throwable) {}

                    @Override
                    public void onFailure(String id, Throwable throwable) { }

                    @Override
                    public void onRelease(String id) {}
                })
                .setImageRequest(imageRequest.build())
                .build();
        ((SimpleDraweeView) view).setController(controller);
        ((SimpleDraweeView) view).setAspectRatio(1.5f);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null!=mListener){
                    mListener.onClick(position);
                }
            }
        });

        return view;
    }


    @Override
    public int getCount() {

        return null != mData ? mData.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    public interface OnGalleryItemClickListener{
        void onClick(int position);
    }

}