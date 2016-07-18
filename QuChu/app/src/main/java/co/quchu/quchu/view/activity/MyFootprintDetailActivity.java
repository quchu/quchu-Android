package co.quchu.quchu.view.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.model.FootprintModel;
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

public class MyFootprintDetailActivity extends BaseBehaviorActivity implements View.OnClickListener {


    @Bind(R.id.container)
    ViewPager viewPager;

    /**
     * 从消息中心跳转 隐藏编辑按钮
     */
    public static final String REQUEST_KEY_FROM_MESSAGE = "message";
    public static final String REQUEST_KEY_FOOTPRINT_ID = "id";
    /**
     * 从趣处详情或者个人脚印列表
     */
    public static final String REQUEST_KEY_ENTITY_LIST = "entityList";
    public static final String REQUEST_KEY_SELECTED_POSITION = "selectedPosition";

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
    @Bind(R.id.fooopDetailActionBack)
    ImageView fooopDetailActionBack;
    private PostCardItemModel model;
    private ArrayList<FootprintModel.Entity> mEntitys;


    @Override
    public ArrayMap<String, Object> getUserBehaviorArguments() {

        ArrayMap<String, Object> data = new ArrayMap<>();
        int footprintId =getIntent().getIntExtra(REQUEST_KEY_FOOTPRINT_ID, -1);
        int itemIndex = getIntent().getIntExtra(REQUEST_KEY_SELECTED_POSITION,-1);

        if (footprintId ==-1){
            ArrayList<Parcelable> s = getIntent().getParcelableArrayListExtra(REQUEST_KEY_ENTITY_LIST);
            if (s==null){
                return null;
            }
            if (itemIndex >=0 && itemIndex<s.size() && null!=s.get(itemIndex)){
                FootprintModel.Entity fid = (FootprintModel.Entity)s.get(itemIndex);
                data.put("footprintid", fid.cardId);
            }

        }else{
            data.put("footprintid", footprintId);
        }
        return data;
    }

    @Override
    public int getUserBehaviorPageId() {
        return 114;
    }

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
        initListener();
        Intent intent = getIntent();
//        model = intent.getParcelableExtra(REQUEST_KEY_IMAGE_LIST);
        int id = intent.getIntExtra(REQUEST_KEY_FOOTPRINT_ID, -1);
        int selectedPosition = intent.getIntExtra(REQUEST_KEY_SELECTED_POSITION, -1);
        mEntitys = intent.getParcelableArrayListExtra(REQUEST_KEY_ENTITY_LIST);
        if (mEntitys != null) {
            PagerAdapter mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), mEntitys);
            ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    FootprintModel.Entity entity = mEntitys.get(position);
                    headImage.setImageURI(Uri.parse(entity.head));
                    supportCount.setText(String.valueOf(entity.supportCount));//点赞数目

                    SpannableString string = new SpannableString(entity.name + ": " + entity.Comment);
                    string.setSpan(new ForegroundColorSpan(ContextCompat.getColor(MyFootprintDetailActivity.this,
                            R.color.colorPrimary)), 0, entity.name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    detail.setText(string);

                    if (entity.autoId != AppContext.user.getUserId() || getIntent().getBooleanExtra(REQUEST_KEY_FROM_MESSAGE, false)) {//如果不是自己的脚印
                        edit.setVisibility(View.GONE);
                    } else {
                        edit.setVisibility(View.VISIBLE);
                    }
                    if (!entity.isP) {                            //当前登录用户是否已经点赞
                        support.setImageResource(R.mipmap.ic_heart);
                    } else {
                        support.setImageResource(R.mipmap.ic_heart_yellow);
                    }

                    if (entity.PlcaeId == 0) {
                        actionContainer.setVisibility(View.INVISIBLE);
                    } else {
                        actionContainer.setVisibility(View.VISIBLE);
                    }


                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            };

            viewPager.addOnPageChangeListener(pageChangeListener);
            viewPager.setAdapter(mPagerAdapter);
            viewPager.setCurrentItem(selectedPosition);
            if (selectedPosition == 0) {
                pageChangeListener.onPageSelected(0);
            }

        }
//        else if (model != null) {
//            initData();
//        }
        else if (id != -1) {
            MyFootprintPresenter presenter = new MyFootprintPresenter(this);

            presenter.getFootprintDetail(id, new ResponseListener<PostCardItemModel>() {
                @Override
                public void onErrorResponse(@Nullable VolleyError error) {
                    Toast.makeText(MyFootprintDetailActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
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


    private void initData() {
        headImage.setImageURI(Uri.parse(model.getAutorPhoto()));
        supportCount.setText(String.valueOf(model.getPraiseNum()));//点赞数目
//        detail.setText(model.getAutor() + ": " + model.getComment());

        SpannableString string = new SpannableString(model.getAutor() + ": " + model.getComment());
        string.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimary)), 0, model.getAutor().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        detail.setText(string);

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
    }

    private void initListener() {
        supportContainer.setOnClickListener(this);
        edit.setOnClickListener(this);
        share.setOnClickListener(this);
        headImage.setOnClickListener(this);
        fooopDetailActionBack.setOnClickListener(this);
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

    private void setSupportCount(int cardId) {
        for (FootprintModel.Entity item : mEntitys) {
            if (item.cardId == cardId) {
                if (item.isP) {
                    item.isP = false;
                    item.supportCount--;
                } else {
                    item.isP = true;
                    item.supportCount++;
                }
            }
        }
        FootprintModel.Entity currentEntity = mEntitys.get(viewPager.getCurrentItem());
        if (!currentEntity.isP) {                            //当前登录用户是否已经点赞
            support.setImageResource(R.mipmap.ic_heart);
        } else {
            support.setImageResource(R.mipmap.ic_heart_yellow);
        }
        supportCount.setText(String.valueOf(currentEntity.supportCount));//点赞数目
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.supportContainer://点赞
                if (model != null) {
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
                            Toast.makeText(MyFootprintDetailActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (mEntitys != null) {
                    v.setEnabled(false);
                    final FootprintModel.Entity entity = mEntitys.get(viewPager.getCurrentItem());
                    PostCardPresenter.setPraise(this, entity.isP, true, entity.cardId, new PostCardPresenter.MyPostCardListener() {
                        @Override
                        public void onSuccess(PostCardModel modeffl) {
                            setSupportCount(entity.cardId);
                            v.setEnabled(true);


                        }

                        @Override
                        public void onError(String error) {
                            v.setEnabled(true);
                            Toast.makeText(MyFootprintDetailActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                break;
            case R.id.share://分享
                Intent intent1 = new Intent(this, SharePreviewActivity.class);
                if (model != null) {

                    intent1.putExtra(SharePreviewActivity.REQUEST_KEY_PLACE_NAME, model.getPlcaeName());
                    intent1.putExtra(SharePreviewActivity.REQUEST_KEY_FOOTPRINT_ID, model.getCardId());
                    intent1.putExtra(SharePreviewActivity.REQUEST_KEY_COMMENT, model.getComment());
                    intent1.putExtra(SharePreviewActivity.REQUEST_KEY_IMAGE_ID, model.getImglist().get(viewPager.getCurrentItem()).getImgId());
                    intent1.putExtra(SharePreviewActivity.REQUEST_KEY_USER_NAME, model.getAutor());
                    intent1.putExtra(SharePreviewActivity.REQUEST_KEY_HEAD_IMAGE, model.getAutorPhoto());
                    intent1.putExtra(SharePreviewActivity.REQUEST_KEY_COVER, model.getImglist().get(viewPager.getCurrentItem()));
                } else {
                    FootprintModel.Entity entity = mEntitys.get(viewPager.getCurrentItem());
                    intent1.putExtra(SharePreviewActivity.REQUEST_KEY_PLACE_NAME, entity.PlcaeName);
                    intent1.putExtra(SharePreviewActivity.REQUEST_KEY_FOOTPRINT_ID, entity.cardId);
                    intent1.putExtra(SharePreviewActivity.REQUEST_KEY_COMMENT, entity.Comment);
                    intent1.putExtra(SharePreviewActivity.REQUEST_KEY_USER_NAME, entity.name);
                    intent1.putExtra(SharePreviewActivity.REQUEST_KEY_HEAD_IMAGE, entity.head);
                    intent1.putExtra(SharePreviewActivity.REQUEST_KEY_IMAGE_ID, entity.image.getImgId());

//                    ArrayList<ImageModel> data = new ArrayList<>();
//                    for (FootprintModel.Entity item : mEntitys) {
//                        if (item.cardId == entity.cardId) {
//                            data.add(item.image);
//                        }
//                    }
                    intent1.putExtra(SharePreviewActivity.REQUEST_KEY_COVER, entity.image);
                }
                startActivity(intent1);

                break;
            case R.id.edit://编辑
                //获取一个脚印
                Intent intent = new Intent(this, AddFootprintActivity.class);
                if (model != null) {
                    intent.putExtra(AddFootprintActivity.REQUEST_KEY_ENTITY, model);
                    intent.putExtra(AddFootprintActivity.REQUEST_KEY_NAME, model.getPlcaeName());
                    intent.putExtra(AddFootprintActivity.REQUEST_KEY_ID, model.getPlaceId());
                    intent.putExtra(AddFootprintActivity.REQUEST_KEY_IS_EDIT, true);
                    startActivity(intent);
                } else if (mEntitys != null) {
                    FootprintModel.Entity entity = mEntitys.get(viewPager.getCurrentItem());
                    PostCardItemModel model = new PostCardItemModel();
                    model.setPlaceName(entity.PlcaeName);
                    model.setPlaceId(entity.PlcaeId);
                    model.setCardId(entity.cardId);
                    model.setComment(entity.Comment);
                    for (FootprintModel.Entity item : mEntitys) {
                        if (item.cardId == entity.cardId) {
                            model.addImageModel(item.image);
                        }
                    }
                    intent.putExtra(AddFootprintActivity.REQUEST_KEY_ENTITY, model);
                    intent.putExtra(AddFootprintActivity.REQUEST_KEY_IS_EDIT, true);
                    intent.putExtra(AddFootprintActivity.REQUEST_KEY_NAME, model.getPlcaeName());
                    intent.putExtra(AddFootprintActivity.REQUEST_KEY_ID, model.getPlaceId());
                    startActivity(intent);
                }
                if (!EventBus.getDefault().isRegistered(this))
                    EventBus.getDefault().register(this);
                break;
            case R.id.headImage:
                int userID = 0;

                if (model != null) {
                    userID = model.getAutorId();
                } else if (mEntitys != null) {
                    userID = mEntitys.get(viewPager.getCurrentItem()).autoId;
                }
                if (userID == 0) {
                    return;
                }
                Intent intent2 = new Intent(this, UserCenterActivity.class);
                intent2.putExtra(UserCenterActivity.REQUEST_KEY_USER_ID, userID);
                startActivity(intent2);
                break;
            case R.id.fooopDetailActionBack:
                finish();
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
        private ArrayList<FootprintModel.Entity> entitys;

        public PagerAdapter(FragmentManager fm, List<ImageModel> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        public PagerAdapter(FragmentManager fm, ArrayList<FootprintModel.Entity> entitys) {
            super(fm);
            this.entitys = entitys;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bund = new Bundle();

            if (entitys == null) {
                bund.putParcelable(FootprintDetailFragment.REQUEST_KEY_IMAGE_ENTITY, fragments.get(position));
            } else {
                bund.putParcelable(FootprintDetailFragment.REQUEST_KEY_IMAGE_ENTITY, entitys.get(position).image);
            }
            FootprintDetailFragment fragment = new FootprintDetailFragment();
            fragment.setArguments(bund);
            return fragment;
        }

        @Override
        public int getCount() {
            return fragments == null ? entitys.size() : fragments.size();
        }

    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Subscribe
    public void onMessageEvent(QuchuEventModel model) {
        if (model.getFlag() == EventFlags.EVENT_POST_CARD_DELETED || model.getFlag() == EventFlags.EVENT_FOOTPRINT_UPDATED) {
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
