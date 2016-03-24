package co.quchu.quchu.photo.previewimage;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.cache.common.CacheKey;
import com.facebook.common.references.CloseableReference;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.PostCardImageListModel;
import co.quchu.quchu.model.PostCardItemModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.AppKey;
import co.quchu.quchu.utils.SPUtils;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

//import com.facebook.rebound.Spring;
//import com.facebook.rebound.SpringConfig;
//import com.facebook.rebound.SpringListener;
//import com.facebook.rebound.SpringSystem;
//import com.facebook.rebound.SpringUtil;

public class PreviewImageActivity extends BaseActivity implements OnPageChangeListener {

    @Bind(R.id.preview_creater_avatar_sdv)
    SimpleDraweeView previewCreaterAvatarSdv;
    @Bind(R.id.preview_creater_name_tv)
    TextView previewCreaterNameTv;
    @Bind(R.id.preview_craete_time_tv)
    TextView previewCraeteTimeTv;
    @Bind(R.id.preview_collect_iv)
    ImageView previewCollectIv;
    @Bind(R.id.preview_collect_rl)
    RelativeLayout previewCollectRl;
    @Bind(R.id.userguide_image_first_index_fl)
    FrameLayout userguideImageFirstIndexFl;
    @Bind(R.id.userguide_image_lastindex_fl)
    FrameLayout userguideImageLastindexFl;
    @Bind(R.id.bi_viewpager)
    HackyViewPager viewpager;
    @Bind(R.id.MainView)
    RelativeLayout MainView;

    private int index = 0;
    private ArrayList<PostCardImageListModel> ImgList;
    private SamplePagerAdapter pagerAdapter;
    private int type;
    protected ImageBDInfo bdInfo;
    private int showingIndex = 0;
    PostCardImageListModel imageInfo;
    PostCardItemModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browseimage);
        ButterKnife.bind(this);
        viewpager.setOnPageChangeListener(this);
        viewpager.setVisibility(View.VISIBLE);
        InData();
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_ALPHA;
    }


    public void InData() {

        index = getIntent().getIntExtra("index", 0);
        type = getIntent().getIntExtra("type", 0);
        model = (PostCardItemModel) getIntent().getSerializableExtra("data");
        bdInfo = (ImageBDInfo) getIntent().getSerializableExtra("bdinfo");
        ImgList = (ArrayList<PostCardImageListModel>) model.getImglist();
        imageInfo = ImgList.get(index);

        /**
         * Guide stuff
         */
        if (SPUtils.getBooleanFromSPMap(this, AppKey.IS_POSTCARD_IMAGES_GUIDE, false)) {
            if (index == ImgList.size() - 1) {
                userguideImageFirstIndexFl.setVisibility(View.GONE);
                userguideImageLastindexFl.setVisibility(View.VISIBLE);
            } else {
                userguideImageFirstIndexFl.setVisibility(View.VISIBLE);
                userguideImageFirstIndexFl.setVisibility(View.GONE);
            }
        }

        pagerAdapter = new SamplePagerAdapter();
        viewpager.setAdapter(pagerAdapter);
        viewpager.setCurrentItem(index);
        previewCreaterAvatarSdv.setImageURI(Uri.parse(model.getAutorPhoto()));
        previewCreaterAvatarSdv.setAspectRatio(1f);
        previewCreaterNameTv.setText(model.getAutor());
        previewCraeteTimeTv.setText(model.getTime().substring(0, 10));
        if (AppContext.user.getUserId() == model.getAutorId()) {
            previewCollectIv.setVisibility(View.GONE);
        } else {
            previewCollectIv.setVisibility(View.VISIBLE);
            setIsfState(ImgList.get(index).isf());
        }

    }


    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int arg0) {
        PostCardImageListModel info = ImgList.get(arg0);
        if (AppContext.user.getUserId() == model.getAutorId()) {
            previewCollectIv.setVisibility(View.GONE);
        } else {
            previewCollectIv.setVisibility(View.VISIBLE);
            setIsfState(info.isf());
        }
        if (SPUtils.getBooleanFromSPMap(PreviewImageActivity.this, AppKey.IS_POSTCARD_IMAGES_GUIDE, false)) {
            if (arg0 == ImgList.size() - 1) {
                userguideImageFirstIndexFl.setVisibility(View.GONE);
                userguideImageLastindexFl.setVisibility(View.VISIBLE);
            } else {
                userguideImageFirstIndexFl.setVisibility(View.VISIBLE);
                userguideImageLastindexFl.setVisibility(View.GONE);
            }
        }
        showingIndex = arg0;
    }

    class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return ImgList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {


            final PhotoView photoView = new PhotoView(container.getContext());
            String path = ImgList.get(position).getPath();

//            ImageLoader.getInstance().displayImage(path, photoView, options,
//                    animateFirstListener);

            ImageRequest request = ImageRequestBuilder
                    .newBuilderWithSource(Uri.parse(path))
                    .setImageType(ImageRequest.ImageType.SMALL)
                    .setPostprocessor(new Postprocessor() {
                        @Override
                        public CloseableReference<Bitmap> process(Bitmap sourceBitmap, PlatformBitmapFactory bitmapFactory) {
                            photoView.setImageBitmap(sourceBitmap);
                            return null;
                        }

                        @Override
                        public String getName() {
                            return null;
                        }

                        @Override
                        public CacheKey getPostprocessorCacheKey() {
                            return null;
                        }
                    })
                    .build();
            Fresco.getImagePipeline().fetchImageFromBitmapCache(request, PreviewImageActivity.this);


            // Now just add PhotoView to ViewPager and return it
            photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {

                @Override
                public void onViewTap(View arg0, float arg1, float arg2) {
                    SPUtils.putBooleanToSPMap(PreviewImageActivity.this, AppKey.IS_POSTCARD_IMAGES_GUIDE, false);
                    userguideImageFirstIndexFl.setVisibility(View.GONE);
                    userguideImageLastindexFl.setVisibility(View.GONE);
                    viewpager.setVisibility(View.GONE);
//                    finish();
                }
            });
            if (position == 0 && SPUtils.getBooleanFromSPMap(PreviewImageActivity.this, AppKey.IS_POSTCARD_IMAGES_GUIDE, false)) {
                userguideImageFirstIndexFl.setVisibility(View.VISIBLE);
            }
            container.addView(photoView, ViewPager.LayoutParams.MATCH_PARENT,
                    ViewPager.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }


    private void setIsfState(boolean isfState) {
        previewCollectIv.setImageDrawable(getResources().getDrawable(isfState ? R.mipmap.ic_detail_collect : R.mipmap.ic_detail_uncollect));
    }


    /**
     *
     */
    @OnClick(R.id.preview_collect_rl)
    public void setCollectClick(View view) {
        String favoUrl = "";
        if (ImgList.get(showingIndex).isf()) {
            favoUrl = String.format(NetApi.userDelFavorite, ImgList.get(showingIndex).getImgId(), NetApi.FavTypeImg);
        } else {
            favoUrl = String.format(NetApi.userFavorite, ImgList.get(showingIndex).getImgId(), NetApi.FavTypeImg);
        }
        NetService.get(this, favoUrl, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                if (ImgList.get(showingIndex).isf()) {
                    Toast.makeText(PreviewImageActivity.this, "取消收藏!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PreviewImageActivity.this, "收藏成功!", Toast.LENGTH_SHORT).show();
                }
                ImgList.get(showingIndex).setIsf(!ImgList.get(showingIndex).isf());
                setIsfState(ImgList.get(showingIndex).isf());
            }

            @Override
            public boolean onError(String error) {

                return false;
            }
        });
    }
}