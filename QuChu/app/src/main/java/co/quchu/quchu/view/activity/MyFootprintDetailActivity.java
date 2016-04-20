package co.quchu.quchu.view.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
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
import co.quchu.quchu.presenter.PostCardPresenter;
import co.quchu.quchu.view.fragment.FootprintDetailFragment;

public class MyFootprintDetailActivity extends BaseActivity implements ViewPager.OnPageChangeListener {


    @Bind(R.id.title_back_iv)
    ImageView titleBackIv;
    @Bind(R.id.title_content_tv)
    TextView titleContentTv;
    @Bind(R.id.container)
    ViewPager viewPager;

    public static final String REQUEST_KEY_MODEL = "modelList";
    public static final String REQUEST_KEY_POSITION = "clickPosition";
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
    ImageView edit;
    @Bind(R.id.supportContainer)
    LinearLayout supportContainer;
    private List<PostCardItemModel> beanList;
    private int selectedPosition;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        selectedPosition = position;
        Entity entity = data.get(position);

        titleContentTv.setText(entity.time);
        headImage.setImageURI(Uri.parse(entity.head));
//
        if (entity.autoId != AppContext.user.getUserId()) {//如果不是自己的脚印
            edit.setVisibility(View.GONE);
        } else {
            edit.setVisibility(View.VISIBLE);
        }
        if (!entity.isP) {//当前登录用户是否已经点赞
            support.setImageResource(R.drawable.ic_light_like);
        } else {
            support.setImageResource(R.drawable.ic_light_like_fill);
        }
        supportCount.setText(String.valueOf(entity.supportCount));//点赞数目
        detail.setText(entity.builder);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    static class Entity {
        public ImageModel image;//大图
        public String head;//头像
        public SpannableStringBuilder builder;//显示的文字
        public int autoId;//发布人ID
        public boolean isP;//当前登录用户是否点赞
        public int supportCount;//点赞数
        public String time;//时间
        public int cardId;
        public String PlcaeName;
        public int PlcaeId;
        public String Comment;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_footprint_detail);
        ButterKnife.bind(this);
        initListener();
        initData();
    }

    List<Entity> data;
    //图片进来需要移动到的位置
    int pageInintPosition;

    private void initData() {
        beanList = getIntent().getParcelableArrayListExtra(REQUEST_KEY_MODEL);
        int position = getIntent().getIntExtra(REQUEST_KEY_POSITION, 0);
        if (null == beanList || beanList.size() == 0) {
            return;
        }
        data = new ArrayList<>();
        for (int i = 0, s = beanList.size(); i < s; i++) {
            PostCardItemModel bean = beanList.get(i);
            Entity entity = null;
            if (i == position) {
                pageInintPosition = data.size();
            }
            for (ImageModel item : bean.getImglist()) {
                entity = new Entity();
                entity.autoId = bean.getAutorId();
                entity.head = bean.getAutorPhoto();
                entity.isP = bean.isIsp();
                entity.supportCount = bean.getPraiseNum();
                entity.time = bean.getTime();
                entity.image = item;
                entity.cardId = bean.getCardId();
                entity.PlcaeName = bean.getPlcaeName();
                entity.PlcaeId = bean.getPlaceId();
                entity.Comment = bean.getComment();


                StringBuilder text1 = new StringBuilder();
                text1.append(bean.getAutor());
                text1.append(":");
                int index1 = text1.length();
                text1.append(bean.getComment());
                text1.append("\n");
                text1.append("- 在 ");
                int index2 = text1.length();
                text1.append(bean.getPlcaeName());
                int index3 = text1.length();

                SpannableStringBuilder builder = new SpannableStringBuilder(text1.toString());
                builder.setSpan(new ForegroundColorSpan(Color.argb(255, 255, 255, 255)), 0, index1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                builder.setSpan(new ForegroundColorSpan(Color.parseColor("#838181")), index1, index2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                builder.setSpan(new ForegroundColorSpan(Color.argb(255, 255, 255, 255)), index2, index3, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

                entity.builder = builder;
            }
            if (entity != null)
                data.add(entity);
        }
        PagerAdapter mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), data);
        viewPager.setAdapter(mPagerAdapter);
        viewPager.addOnPageChangeListener(this);

        if (data.size() > pageInintPosition) {
            viewPager.setCurrentItem(pageInintPosition);
        } else {
            onPageSelected(0);
        }

    }

    private void initListener() {
        titleBackIv.setOnClickListener(this);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back_iv:
                finish();
                break;
            case R.id.supportContainer://点赞
                final Entity entity = data.get(selectedPosition);

                PostCardPresenter.setPraise(this, entity.isP, true, entity.cardId, new PostCardPresenter.MyPostCardListener() {
                    @Override
                    public void onSuccess(PostCardModel model) {
                        if (entity.isP) {
                            entity.isP = false;
                            support.setImageResource(R.drawable.ic_light_like);
                        } else {
                            entity.isP = true;
                            support.setImageResource(R.drawable.ic_light_like_fill);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(MyFootprintDetailActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            case R.id.share://分享
                final Entity en = data.get(selectedPosition);
                ShareDialogFg shareDialogFg = ShareDialogFg.newInstance(en.cardId, en.PlcaeName, false);
                shareDialogFg.show(getFragmentManager(), "share_postcard");
                break;
            case R.id.edit://编辑
                //获取一个脚印
                Intent intent = new Intent(this, AddFootprintActivity.class);
                PostCardItemModel bean = new PostCardItemModel();
                int cardId = data.get(selectedPosition).cardId;

                for (Entity item : data) {
                    if (item.cardId == cardId) {
                        bean.addImageModel(item.image);
                        bean.setPlaceId(item.PlcaeId);
                        bean.setPlcaeName(item.PlcaeName);
                        bean.setComment(item.Comment);
                    }
                }
                intent.putExtra(AddFootprintActivity.REQUEST_KEY_ENTITY, bean);
                intent.putExtra(AddFootprintActivity.REQUEST_KEY_NAME, bean.getPlcaeName());
                intent.putExtra(AddFootprintActivity.REQUEST_KEY_ID, bean.getPlaceId());
                startActivity(intent);
                break;
        }
    }

    private boolean isShowing = true;

    private void animation() {
        if (isShowing) {
            isShowing = false;
            ObjectAnimator animator = ObjectAnimator.ofFloat(containerBottom, "translationY", 0, containerBottom.getHeight());
            animator.setDuration(600);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.start();
        } else {
            isShowing = true;
            ObjectAnimator animator = ObjectAnimator.ofFloat(containerBottom, "translationY", containerBottom.getHeight(), 0);
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
        private List<Entity> fragments;

        public PagerAdapter(FragmentManager fm, List<Entity> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bund = new Bundle();
            bund.putParcelable(FootprintDetailFragment.REQUEST_KEY_IMAGE_ENTITY, fragments.get(position).image);
            FootprintDetailFragment fragment = new FootprintDetailFragment();
            fragment.setArguments(bund);

            return fragment;
        }

        @Override
        public int getCount() {
            return fragments == null ? 0 : fragments.size();
        }

    }
}
