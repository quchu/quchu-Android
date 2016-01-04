package co.quchu.quchu.photo.previewimage;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
import co.quchu.quchu.utils.StringUtils;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;

public class PreviewImage extends BaseActivity implements OnPageChangeListener {

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

    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private SamplePagerAdapter pagerAdapter;
    private final Spring mSpring = SpringSystem
            .create()
            .createSpring()
            .addListener(new ExampleSpringListener());
    private float moveheight;
    private int type;
    private RelativeLayout MainView;
    //原图高
    private float y_img_h;
    protected float to_x = 0;
    protected float to_y = 0;
    private float tx;
    private float ty;
    private float img_w;
    private float img_h;
    private float size, size_h;
    protected ImageBDInfo bdInfo;

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
        getValue();
    }


    public void Listener() {
        // TODO Auto-generated method stub
        viewpager.setOnPageChangeListener(this);
    }

    PostCardImageListModel imageInfo;

    public void InData() {
        // TODO Auto-generated method stub
        index = getIntent().getIntExtra("index", 0);
        type = getIntent().getIntExtra("type", 0);
        PostCardItemModel model = (PostCardItemModel) getIntent().getSerializableExtra("data");
        ImgList = (ArrayList<PostCardImageListModel>) model.getImglist();
        imageInfo = ImgList.get(index);
        bdInfo = (ImageBDInfo) getIntent().getSerializableExtra("bdinfo");
        pagerAdapter = new SamplePagerAdapter();
        viewpager.setAdapter(pagerAdapter);
        viewpager.setCurrentItem(index);
        if (type == 1) {
            moveheight = StringUtils.dip2px(70);
        } else if (type == 2) {
            moveheight = (AppContext.Width - columnsNum * StringUtils.dip2px(2)) / columnsNum;
        } else if (type == 3) {
            moveheight = (AppContext.Width - StringUtils.dip2px(80) - StringUtils.dip2px(2)) / 3;
        }
        previewCreaterAvatarSdv.setImageURI(Uri.parse(model.getAutorPhoto()));
        previewCreaterAvatarSdv.setAspectRatio(1f);
        previewCreaterNameTv.setText(model.getAutor());
        previewCraeteTimeTv.setText(model.getTime().substring(0, 10));
        setIsfState(ImgList.get(index).isf());
        if (SPUtils.getBooleanFromSPMap(this, AppKey.IS_POSTCARD_IMAGES_GUIDE, false)){
            if (index ==ImgList.size()-1){

                userguideImageFirstIndexFl.setVisibility(View.GONE);
                userguideImageLastindexFl.setVisibility(View.VISIBLE);
            }else {
                userguideImageFirstIndexFl.setVisibility(View.VISIBLE);
                userguideImageFirstIndexFl.setVisibility(View.GONE);
            }
        }
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
        ImageLoaders.setsendimg(info.getPath(), showimg);
        if (type == 1) {
            int move_index = arg0 - index;
            to_y = move_index * moveheight;
        } else if (type == 2) {
            int a = index / columnsNum;
            int b = index % columnsNum;
            int a1 = arg0 / columnsNum;
            int b1 = arg0 % columnsNum;
            to_y = (a1 - a) * moveheight + (a1 - a) * StringUtils.dip2px(2);
            to_x = (b1 - b) * moveheight + (b1 - b) * StringUtils.dip2px(2);
        } else if (type == 3) {
            int a = index / 3;
            int b = index % 3;
            int a1 = arg0 / 3;
            int b1 = arg0 % 3;
            to_y = (a1 - a) * moveheight + (a1 - a) * StringUtils.dip2px(1);
            to_x = (b1 - b) * moveheight + (b1 - b) * StringUtils.dip2px(1);
        }
        setIsfState(info.isf());
        if (SPUtils.getBooleanFromSPMap(PreviewImage.this, AppKey.IS_POSTCARD_IMAGES_GUIDE, false)) {
            if (arg0 == ImgList.size() - 1) {
                userguideImageFirstIndexFl.setVisibility(View.GONE);
                userguideImageLastindexFl.setVisibility(View.VISIBLE);
            } else {
                userguideImageFirstIndexFl.setVisibility(View.VISIBLE);
                userguideImageLastindexFl.setVisibility(View.GONE);
            }

        }
    }

    class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return ImgList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            String path = ImgList.get(position).getPath();
            ImageLoader.getInstance().displayImage(path, photoView, options,
                    animateFirstListener);
            // Now just add PhotoView to ViewPager and return it
            photoView.setOnViewTapListener(new OnViewTapListener() {

                @Override
                public void onViewTap(View arg0, float arg1, float arg2) {
                    SPUtils.putBooleanToSPMap(PreviewImage.this, AppKey.IS_POSTCARD_IMAGES_GUIDE, false);
                    userguideImageFirstIndexFl.setVisibility(View.GONE);
                    userguideImageLastindexFl.setVisibility(View.GONE);
                    viewpager.setVisibility(View.GONE);
                    showimg.setVisibility(View.VISIBLE);
                    setShowimage();
//                    finish();
                }
            });
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

    private class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {

        final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                imageView.setImageBitmap(loadedImage);
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
//					FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            viewpager.setVisibility(View.GONE);
            showimg.setVisibility(View.VISIBLE);
            setShowimage();
        }
        return true;
    }

    protected void EndSoring() {
        viewpager.setVisibility(View.VISIBLE);
        showimg.setVisibility(View.GONE);
    }

    protected void EndMove() {
        finish();
    }

    protected ImageView showimg;

    protected void getValue() {
        showimg = new ImageView(this);
        showimg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoaders.setsendimg(imageInfo.getPath(), showimg);
        img_w = bdInfo.width;
        img_h = bdInfo.height;
        size = AppContext.Width / img_w;
        // Wait for layout.
        y_img_h = imageInfo.getHeight() * AppContext.Width / imageInfo.getWidth();
        size_h = y_img_h / img_h;
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams((int) bdInfo.width, (int) bdInfo.height);
        showimg.setLayoutParams(p);
        p.setMargins((int) bdInfo.x, (int) bdInfo.y, (int) (AppContext.Width - (bdInfo.x + bdInfo.width)), (int) (AppContext.Height - (bdInfo.y + bdInfo.height)));
        MainView.addView(showimg);
        showimg.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                setShowimage();
            }
        }, 300);

    }

    protected void setShowimage() {
        if (mSpring.getEndValue() == 0) {
            mSpring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(170, 5));
            tx = AppContext.Width / 2 - (bdInfo.x + img_w / 2);
            ty = AppContext.Height / 2 - (bdInfo.y + img_h / 2);
            MoveView();
            return;
        }
        mSpring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(1, 5));
        mSpring.setEndValue(0);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //execute the task
                MoveBackView();
            }
        }, 300);

    }

    private void MoveView() {

        ObjectAnimator.ofFloat(MainView, "alpha", 0.6f).setDuration(2).start();
        MainView.setVisibility(View.VISIBLE);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(showimg, "translationX", tx).setDuration(200),
                ObjectAnimator.ofFloat(showimg, "translationY", ty).setDuration(200),
                ObjectAnimator.ofFloat(MainView, "alpha", 1).setDuration(200)
        );
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                showimg.setScaleType(ImageView.ScaleType.FIT_XY);
                mSpring.setEndValue(1);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        set.start();
    }

    private void MoveBackView() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(showimg, "translationX", to_x).setDuration(200),
                ObjectAnimator.ofFloat(showimg, "translationY", to_y).setDuration(200)
        );
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                showimg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                EndMove();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        set.start();
    }

    private int showingIndex = 0;

    private class ExampleSpringListener implements SpringListener {

        @Override
        public void onSpringUpdate(Spring spring) {
            double CurrentValue = spring.getCurrentValue();
            float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(CurrentValue, 0, 1, 1, size);
            float mapy = (float) SpringUtil.mapValueFromRangeToRange(CurrentValue, 0, 1, 1, size_h);
            showimg.setScaleX(mappedValue);
            showimg.setScaleY(mapy);
            if (CurrentValue == 1) {
                EndSoring();
            }
        }

        @Override
        public void onSpringAtRest(Spring spring) {

        }

        @Override
        public void onSpringActivate(Spring spring) {

        }

        @Override
        public void onSpringEndStateChange(Spring spring) {

        }
    }

    private void setIsfState(boolean isfState) {
        previewCollectIv.setImageDrawable(getResources().getDrawable(isfState ? R.drawable.ic_detail_collect : R.drawable.ic_detail_uncollect));
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
                    Toast.makeText(PreviewImage.this, "取消收藏!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PreviewImage.this, "收藏成功!", Toast.LENGTH_SHORT).show();
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