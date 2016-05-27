package co.quchu.quchu.view.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.ShareDialogFg;
import co.quchu.quchu.model.ImageModel;
import co.quchu.quchu.model.PostCardItemModel;
import co.quchu.quchu.model.PostCardModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.presenter.MyFootprintPresenter;
import co.quchu.quchu.presenter.PostCardPresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.view.fragment.FootprintDetailFragment;

public class MyFootprintDetailActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.container)
    ViewPager viewPager;

    public static final String REQUEST_KEY_IMAGE_LIST = "model";

    public static final String REQUEST_KEY_FOOTPRINT_ID = "id";
    /**
     * 从消息中心跳转 隐藏编辑按钮
     */
    public static final String REQUEST_KEY_FROM_MESSAGE = "message";

    @Bind(R.id.headImage)
    SimpleDraweeView headImage;
    @Bind(R.id.detail)
    TextView detail;
    @Bind(R.id.container_bottom)
    RelativeLayout containerBottom;

    @Bind(R.id.support)
    ImageView support;
    @Bind(R.id.supportCount)
    TextView supportCount;
    @Bind(R.id.share)
    ImageView share;
    @Bind(R.id.edit)
    FrameLayout edit;
    @Bind(R.id.supportContainer)
    LinearLayout supportContainer;
    @Bind(R.id.actionContainer)
    LinearLayout actionContainer;
    private PostCardItemModel model;


    //底下文字背景处理
//        ImagePipeline pipeline = Fresco.getImagePipeline();
//        Uri uri = Uri.parse(entity.image.getPath());
//        if (pipeline.isInBitmapMemoryCache(uri)) {
//            ImageRequest request = ImageRequestBuilder
//                    .newBuilderWithSource(uri)
//                    .build();
//
//            DataSource<CloseableReference<CloseableImage>> source = pipeline.fetchImageFromBitmapCache(request, this);
//            CloseableReference<CloseableImage> result = null;
//            try {
//                result = source.getResult();
//                if (result != null) {
//                    CloseableImage image = result.get();
//                    if (image instanceof CloseableStaticBitmap) {
//                        Bitmap bitmap = ((CloseableStaticBitmap) image).getUnderlyingBitmap();
//                        int viewPagerHeight = viewPager.getHeight();
//                        int viewPagerWidth = viewPager.getWidth();
//                        //获取缩放后图片的高度
//                        int bitmaphHeight = bitmap.getHeight();
//                        int offset = (viewPagerHeight - bitmaphHeight) / 2;
//                        int containerHeight = StringUtils.dip2px(this, 80);
//                        if (offset < containerHeight) {
//                            Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, bitmap.getHeight() - (containerHeight - offset), bitmap.getWidth(), containerHeight);
//                            containerBottom.setBackground(new BitmapDrawable(bitmap1));
//                        }
//                    }
//                }
//
//            } finally {
//                source.close();
//                CloseableReference.closeSafely(result);
//            }
//        }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_footprint_detail);
        ButterKnife.bind(this);
        getEnhancedToolbar().getTitleTv().setText("");
        initListener();
        Intent intent = getIntent();
        model = intent.getParcelableExtra(REQUEST_KEY_IMAGE_LIST);
        int id = intent.getIntExtra(REQUEST_KEY_FOOTPRINT_ID, -1);
        if (model != null) {
            initData();
        } else if (id != -1) {
            MyFootprintPresenter presenter = new MyFootprintPresenter(this);

            presenter.getFootprintDetail(id, new ResponseListener<PostCardItemModel>() {
                @Override
                public void onErrorResponse(@Nullable VolleyError error) {
                    Toast.makeText(MyFootprintDetailActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(PostCardItemModel response, boolean result, String errorCode, @Nullable String msg) {
                    if (response == null) {
                        Toast.makeText(MyFootprintDetailActivity.this, "脚印不存在", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    model = response;
                    initData();
                }
            });
        }
    }

    //图片进来需要移动到的位置
    int pageInintPosition;

    private void initData() {
        headImage.setImageURI(Uri.parse(model.getAutorPhoto()));
        supportCount.setText(String.valueOf(model.getPraiseNum()));//点赞数目
        detail.setText(model.getComment());

        if (model.getPlaceId() == 0) {
            actionContainer.setVisibility(View.INVISIBLE);
        } else {
            actionContainer.setVisibility(View.VISIBLE);
        }

        if (model.getAutorId() != AppContext.user.getUserId() || getIntent().getBooleanExtra(REQUEST_KEY_FROM_MESSAGE, false)) {//如果不是自己的脚印
            edit.setVisibility(View.GONE);
        } else {
            edit.setVisibility(View.VISIBLE);
        }

        if (!model.isIsp()) {                            //当前登录用户是否已经点赞
            support.setImageResource(R.mipmap.ic_heart);
        } else {
            support.setImageResource(R.mipmap.ic_heart_yellow);
        }


        PagerAdapter mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), model.getImglist());
        viewPager.setAdapter(mPagerAdapter);

        viewPager.setCurrentItem(pageInintPosition);

    }

    private void initListener() {
        supportContainer.setOnClickListener(this);
        edit.setOnClickListener(this);
        share.setOnClickListener(this);
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            float x;
            float y;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = event.getX();
                        y = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (Math.abs(event.getX() - x) < 100 && Math.abs(event.getY() - y) < 100) {
                            animation();
                        }

                        break;
                }
                return false;
            }
        });
    }


    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.supportContainer://点赞
                v.setEnabled(false);
                PostCardPresenter.setPraise(this, model.isIsp(), true, model.getCardId(), new PostCardPresenter.MyPostCardListener() {
                    @Override
                    public void onSuccess(PostCardModel modeffl) {
                        if (model.isIsp()) {
                            LogUtils.e("取消点赞成功");
                            model.setIsp(false);
                            model.setPraiseNum(model.getPraiseNum() - 1);
                            supportCount.setText(String.valueOf(model.getPraiseNum()));//点赞数目
                            support.setImageResource(R.mipmap.ic_heart);
                        } else {
                            model.setIsp(true);
                            LogUtils.e("点赞成功");
                            model.setPraiseNum(model.getPraiseNum() + 1);
                            supportCount.setText(String.valueOf(model.getPraiseNum()));//点赞数目
                            support.setImageResource(R.mipmap.ic_heart_yellow);
                        }
                        v.setEnabled(true);
                    }

                    @Override
                    public void onError(String error) {
                        v.setEnabled(true);
                        Toast.makeText(MyFootprintDetailActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            case R.id.share://分享
                ShareDialogFg shareDialogFg = ShareDialogFg.newInstance(model.getCardId(), model.getPlcaeName(), false);
                shareDialogFg.show(getSupportFragmentManager(), "share_postcard");
                break;
            case R.id.edit://编辑
                //获取一个脚印
                Intent intent = new Intent(this, AddFootprintActivity.class);
                intent.putExtra(AddFootprintActivity.REQUEST_KEY_ENTITY, model);
                intent.putExtra(AddFootprintActivity.REQUEST_KEY_NAME, model.getPlcaeName());
                intent.putExtra(AddFootprintActivity.REQUEST_KEY_ID, model.getPlaceId());
                intent.putExtra(AddFootprintActivity.REQUEST_KEY_IS_EDIT, true);
                startActivity(intent);
                break;
        }
    }


    private boolean isShowing = true;

    private void animation() {
        if (isShowing) {
            isShowing = false;
            ObjectAnimator animator = ObjectAnimator.ofFloat(containerBottom, "translationY", 0, containerBottom.getHeight() + 150);
            animator.setDuration(600);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.start();
        } else {
            isShowing = true;
            ObjectAnimator animator = ObjectAnimator.ofFloat(containerBottom, "translationY", containerBottom.getHeight() + 150, 0);
            animator.setDuration(600);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.start();
        }
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }


    public class PagerAdapter extends FragmentPagerAdapter {
        private List<ImageModel> fragments;

        public PagerAdapter(FragmentManager fm, List<ImageModel> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bund = new Bundle();
            bund.putParcelable(FootprintDetailFragment.REQUEST_KEY_IMAGE_ENTITY, fragments.get(position));
            FootprintDetailFragment fragment = new FootprintDetailFragment();
            fragment.setArguments(bund);
            return fragment;
        }

        @Override
        public int getCount() {
            return fragments == null ? 0 : fragments.size();
        }

    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        super.onStop();
    }

    @Subscribe
    public void onMessageEvent(QuchuEventModel model) {
        if (model.getFlag() == EventFlags.EVENT_POST_CARD_DELETED) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
