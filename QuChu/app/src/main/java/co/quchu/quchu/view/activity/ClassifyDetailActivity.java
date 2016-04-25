package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.facebook.cache.common.CacheKey;
import com.facebook.common.references.CloseableReference;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.presenter.RecommendPresenter;
import co.quchu.quchu.utils.ImageUtils;
import co.quchu.quchu.view.adapter.DiscoverDetailPagerAdapter;


public class ClassifyDetailActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    public static final String PARAMETER_TITLE = "title";
    @Bind(R.id.vpContent)
    ViewPager vpContent;
    @Bind(R.id.ivBackground)
    ImageView ivBackground;
    ArrayList<RecommendModel> mData = new ArrayList<>();
    DiscoverDetailPagerAdapter mAdapter;


    private int currentIndex = -1;
    private int currentBGIndex = -1;
    Bitmap mSourceBitmap;
    private int index;
    private final int MESSAGE_FLAG_DELAY_TRIGGER = 0x0001;
    private final int MESSAGE_FLAG_BLUR_RENDERING_FINISH = 0x0002;
    public static final String MESSAGE_KEY_BITMAP = "MESSAGE_KEY_BITMAP";
    private boolean mFragmentStoped;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify_detail);
        ButterKnife.bind(this);


        mAdapter = new DiscoverDetailPagerAdapter(mData, this, new DiscoverDetailPagerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                MobclickAgent.onEvent(ClassifyDetailActivity.this, "detail_subject_c");
                Intent intent = new Intent(ClassifyDetailActivity.this, QuchuDetailsActivity.class);
                intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_FROM, QuchuDetailsActivity.FROM_TYPE_SUBJECT);
                intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, mData.get(position).getPid());
                System.out.println(mData.get(position).getPid());
                startActivity(intent);
            }
        });
        vpContent.setOnPageChangeListener(this);
        vpContent.setAdapter(mAdapter);
        vpContent.setClipToPadding(false);
        vpContent.setPadding(80, 40, 80, 40);
        vpContent.setPageMargin(40);
        getDataSetFromServer();
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }


    public void getDataSetFromServer() {
        RecommendPresenter.getRecommendList(this, false, new RecommendPresenter.GetRecommendListener() {
            @Override
            public void onSuccess(ArrayList<RecommendModel> arrayList, int pageCount, int pageNum) {
                mData.clear();
                mData.addAll(arrayList);
                getSwipeBackLayout().setEnableGesture(false);
                mAdapter.notifyDataSetChanged();
                index = currentIndex = 0;
                mBlurEffectAnimationHandler.sendEmptyMessageDelayed(MESSAGE_FLAG_DELAY_TRIGGER, 300L);
            }

            @Override
            public void onError() {

            }
        });


    }

    @Override
    public void onStop() {
        mFragmentStoped = true;
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFragmentStoped = false;
    }

    private Handler mBlurEffectAnimationHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mFragmentStoped) {
                return;
            }
            switch (msg.what) {
                case MESSAGE_FLAG_BLUR_RENDERING_FINISH:
                    mSourceBitmap = msg.getData().getParcelable(MESSAGE_KEY_BITMAP);
                    if (null != mSourceBitmap && !mSourceBitmap.isRecycled()) {
                        executeSwitchAnimation(ImageUtils.doBlur(mSourceBitmap, ivBackground.getWidth() / 4, ivBackground.getHeight() / 4));
                    }
                    currentBGIndex = currentIndex;
                    break;

                case MESSAGE_FLAG_DELAY_TRIGGER:
                    if (-1 != currentIndex && index == currentIndex && currentBGIndex != currentIndex) {
                        String strUri = mData.get(currentIndex).getCover();
                        if (!TextUtils.isEmpty(strUri)) {
                            Uri imageUri = Uri.parse(strUri);
                            if (Fresco.getImagePipeline().isInBitmapMemoryCache(imageUri)) {
                                ImageRequest request = ImageRequestBuilder
                                        .newBuilderWithSource(imageUri)
                                        .setImageType(ImageRequest.ImageType.SMALL)
                                        .setPostprocessor(new Postprocessor() {
                                            @Override
                                            public CloseableReference<Bitmap> process(Bitmap sourceBitmap, PlatformBitmapFactory bitmapFactory) {
                                                if (null != sourceBitmap) {
                                                    Message msg = new Message();
                                                    msg.what = MESSAGE_FLAG_BLUR_RENDERING_FINISH;
                                                    Bundle bundle = new Bundle();
                                                    bundle.putParcelable(MESSAGE_KEY_BITMAP, sourceBitmap);
                                                    msg.setData(bundle);
                                                    mBlurEffectAnimationHandler.sendMessage(msg);
                                                }
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
                                Fresco.getImagePipeline().fetchImageFromBitmapCache(request, this);
                            }
                        }
                    }
                    break;
            }
        }
    };

    /**
     * 执行切换动画
     */
    private void executeSwitchAnimation(Bitmap bm) {
        if (null == bm || bm.isRecycled()) {
            return;
        }

        final Bitmap finalBm = bm;

        ivBackground.animate().alpha(.1f).setDuration(400).setInterpolator(new AccelerateInterpolator()).withEndAction(new Runnable() {
            @Override
            public void run() {
                if (ivBackground.getDrawable() instanceof ColorDrawable) {
                    ivBackground.setImageBitmap(null);
                } else {
                    if (!(ivBackground.getDrawable() instanceof ColorDrawable) && null != ivBackground.getDrawable() && null != ((BitmapDrawable) ivBackground.getDrawable()).getBitmap()) {
                        ((BitmapDrawable) ivBackground.getDrawable()).getBitmap().recycle();
                        ivBackground.setImageBitmap(null);
                        System.gc();
                    }
                }
                ivBackground.setImageBitmap(finalBm);
                ivBackground.animate().alpha(1).setDuration(400).setInterpolator(new DecelerateInterpolator()).start();
            }
        }).start();

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        MobclickAgent.onEvent(this, "discovery_c");
        if (position == 0) {
            getSwipeBackLayout().setEnableGesture(true);
        } else {
            getSwipeBackLayout().setEnableGesture(false);
        }
        currentIndex = position;
        index = position;
        mBlurEffectAnimationHandler.sendEmptyMessageDelayed(MESSAGE_FLAG_DELAY_TRIGGER, 300L);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        if (null != vpContent) {
            vpContent.removeOnPageChangeListener(this);
        }
        super.onDestroy();
    }
}