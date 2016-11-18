package co.quchu.quchu.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.ImageModel;
import co.quchu.quchu.view.adapter.MultiTouchViewPager;
import co.quchu.quchu.widget.CircleIndicator;
import me.relex.photodraweeview.OnPhotoTapListener;
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
  @Bind(R.id.tvIndicator) TextView mTvIndicator;
  @Bind(R.id.tvSource) TextView mTvSource;
  @Bind(R.id.ivShowSource) ImageView mIvShowSource;


  public static void enterActivity(Activity from, int position, List<ImageModel> imageSet) {
    Intent intent = new Intent(from, PhotoViewActivity.class);
    intent.putExtra(BUNDLE_KEY_PHOTO_LIST_INDEX, position);
    intent.putParcelableArrayListExtra(BUNDLE_KEY_PHOTO_LIST, (ArrayList<? extends Parcelable>) imageSet);
    from.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mPhotoList = getIntent().getParcelableArrayListExtra(BUNDLE_KEY_PHOTO_LIST);
    mIndex = getIntent().getIntExtra(BUNDLE_KEY_PHOTO_LIST_INDEX, -1);

    setContentView(R.layout.activity_photoview);
    ButterKnife.bind(this);

    CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
    MultiTouchViewPager viewPager = (MultiTouchViewPager) findViewById(R.id.view_pager);
    viewPager.setAdapter(new DraweePagerAdapter());
    indicator.setViewPager(viewPager);

    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      }

      @Override
      public void onPageSelected(int position) {
        mTvIndicator.setText((position + 1) + " of " + (mPhotoList.size()));
      }

      @Override
      public void onPageScrollStateChanged(int state) {
      }
    });

    if (mIndex != -1 && mIndex < mPhotoList.size()) {
      mTvIndicator.setText("1 of " + (mPhotoList.size()));
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

  @OnClick({R.id.ivShowSource, R.id.ivReturn})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.ivShowSource:
        mTvSource.setVisibility(View.VISIBLE);
        mIvShowSource.setVisibility(View.INVISIBLE);
        break;

      case R.id.ivReturn:
        onBackPressed();
        break;
    }
  }


  public class DraweePagerAdapter extends PagerAdapter {


    @Override
    public int getCount() {
      return null != mPhotoList ? mPhotoList.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
      return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
      container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int position) {
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

      photoDraweeView.setOnPhotoTapListener(new OnPhotoTapListener() {
        @Override
        public void onPhotoTap(View view, float x, float y) {
          PhotoViewActivity.this.finish();
        }
      });
      return photoDraweeView;
    }
  }

}
