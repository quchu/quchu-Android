/*
package co.quchu.quchu.photo.previewimage;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
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
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.FlickrModel;
import co.quchu.quchu.model.PostCardImageListModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.AppKey;
import co.quchu.quchu.utils.SPUtils;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;


*/
/**
 * 相册 图片 查看
 *//*

public class PreviewAlbumImage extends BaseActivity implements OnPageChangeListener {

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
    private int index = 0;
    private ViewPager viewpager;
    private ArrayList<PostCardImageListModel> ImgList;
    private SamplePagerAdapter pagerAdapter;
    private RelativeLayout MainView;


    private int columnsNum = 4;//gridView 列数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browseimage);
        ButterKnife.bind(this);
        MainView = (RelativeLayout) findViewById(R.id.MainView);
        viewpager = (HackyViewPager) findViewById(R.id.bi_viewpager);
        Listener();
        InData();
    }


    public void Listener() {
        // TODO Auto-generated method stub
        viewpager.setOnPageChangeListener(this);
    }

    PostCardImageListModel imageInfo;

    public void InData() {
        // TODO Auto-generated method stub

        index = getIntent().getIntExtra("index", 0);
        FlickrModel.ImgsEntity model = (FlickrModel.ImgsEntity) getIntent().getSerializableExtra("data");
        ImgList = (ArrayList<PostCardImageListModel>) model.getResult();
        imageInfo = ImgList.get(index);
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
        previewCreaterAvatarSdv.setImageURI(Uri.parse(imageInfo.getAutorPhoto()));
        previewCreaterAvatarSdv.setAspectRatio(1f);
        previewCreaterNameTv.setText(imageInfo.getAutor());
        previewCraeteTimeTv.setText(imageInfo.getTime().substring(0, 10));
        setIsfState(imageInfo.isIsf());

    }


    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPageSelected(int arg0) {
        // TODO Auto-generated method stub
        if (showimg == null) {
            return;
        }
        PostCardImageListModel info = ImgList.get(arg0);
        showimg.setImageURI(Uri.parse(info.getPath()));

        setIsfState(imageInfo.isIsf());

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
            Fresco.getImagePipeline().fetchImageFromBitmapCache(request, PreviewAlbumImage.this);


            // Now just add PhotoView to ViewPager and return it
            photoView.setOnViewTapListener(new OnViewTapListener() {

                @Override
                public void onViewTap(View arg0, float arg1, float arg2) {
                    PreviewAlbumImage.this.finish();
                }
            });
            if (position == 0 && SPUtils.getBooleanFromSPMap(PreviewAlbumImage.this, AppKey.IS_POSTCARD_IMAGES_GUIDE, false)) {
                userguideImageFirstIndexFl.setVisibility(View.VISIBLE);
            }
            container.addView(photoView, LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);

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


    protected SimpleDraweeView showimg;


    private int showingIndex = 0;


    private void setIsfState(boolean isfState) {
        previewCollectIv.setImageDrawable(getResources().getDrawable(isfState ? R.drawable.ic_detail_collect : R.drawable.ic_detail_uncollect));
    }


    */
/**
     *
     *//*

    @OnClick(R.id.preview_collect_rl)
    public void setCollectClick(View view) {
        String favoUrl = "";
        if (ImgList.get(showingIndex).isIsf()) {
            favoUrl = String.format(NetApi.userDelFavorite, ImgList.get(showingIndex).getImagId(), NetApi.FavTypeImg);
        } else {
            favoUrl = String.format(NetApi.userFavorite, ImgList.get(showingIndex).getImagId(), NetApi.FavTypeImg);
        }
        NetService.get(this, favoUrl, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                if (ImgList.get(showingIndex).isIsf()) {
                    Toast.makeText(PreviewAlbumImage.this, "取消收藏!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PreviewAlbumImage.this, "收藏成功!", Toast.LENGTH_SHORT).show();
                }
                ImgList.get(showingIndex).setIsf(!ImgList.get(showingIndex).isIsf());
                setIsfState(ImgList.get(showingIndex).isIsf());
            }

            @Override
            public boolean onError(String error) {

                return false;
            }
        });
    }
}*/
