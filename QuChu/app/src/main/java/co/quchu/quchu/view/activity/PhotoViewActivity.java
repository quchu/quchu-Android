package co.quchu.quchu.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import butterknife.Bind;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;

import java.util.ArrayList;
import java.util.List;

import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.ImageModel;
import co.quchu.quchu.view.adapter.MultiTouchViewPager;
import co.quchu.quchu.widget.CircleIndicator;
import me.relex.photodraweeview.PhotoDraweeView;

/**
 * Created by Nico on 16/8/9.
 */
public class PhotoViewActivity extends BaseActivity {

    private List<ImageModel> mPhotoList;
    private int mIndex = -1;
    private static String BUNDLE_KEY_PHOTO_LIST = "BUNDLE_KEY_PHOTO_LIST";
    private static String BUNDLE_KEY_PHOTO_LIST_INDEX = "BUNDLE_KEY_PHOTO_LIST_INDEX";
    @Bind(R.id.ivReturn) ImageView mIvReturn;


    public static void enterActivity(Activity from,int position, List<ImageModel> imageSet) {
        Intent intent = new Intent(from,PhotoViewActivity.class);
        intent.putExtra(BUNDLE_KEY_PHOTO_LIST_INDEX,position);
        intent.putParcelableArrayListExtra(BUNDLE_KEY_PHOTO_LIST, (ArrayList<? extends Parcelable>) imageSet);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPhotoList = getIntent().getParcelableArrayListExtra(BUNDLE_KEY_PHOTO_LIST);
        mIndex = getIntent().getIntExtra(BUNDLE_KEY_PHOTO_LIST_INDEX,-1);

        setContentView(R.layout.activity_photoview);


        mIvReturn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                onBackPressed();
            }
        });
//        ((Toolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(
//                new View.OnClickListener() {
//                    @Override public void onClick(View v) {
//                        onBackPressed();
//                    }
//                });

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        MultiTouchViewPager viewPager = (MultiTouchViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new DraweePagerAdapter());
        indicator.setViewPager(viewPager);

        if (mIndex!=-1 && mIndex< mPhotoList.size()){
            viewPager.setCurrentItem(mIndex);
        }
    }




    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    @Override
    protected String getPageNameCN() {
        return null;
    }


    public class DraweePagerAdapter extends PagerAdapter {



        @Override public int getCount() {
            return null!=mPhotoList? mPhotoList.size() : 0;
        }

        @Override public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override public Object instantiateItem(ViewGroup viewGroup, int position) {
            final PhotoDraweeView photoDraweeView = new PhotoDraweeView(viewGroup.getContext());
            PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
            controller.setUri(Uri.parse(mPhotoList.get(position).getPath()));
            controller.setOldController(photoDraweeView.getController());
            controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                    super.onFinalImageSet(id, imageInfo, animatable);
                    if (imageInfo == null) {
                        return;
                    }
                    photoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
                }
            });
            photoDraweeView.setController(controller.build());
            photoDraweeView.setScale(0);

            try {
                viewGroup.addView(photoDraweeView, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
            } catch (Exception e) {
                e.printStackTrace();
            }

            photoDraweeView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    PhotoViewActivity.this.finish();
                }
            });
            return photoDraweeView;
        }
    }
}
