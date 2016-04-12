package co.quchu.quchu.view.activity;

import android.animation.ObjectAnimator;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.PostCardImageListModel;
import co.quchu.quchu.model.PostCardItemModel;
import co.quchu.quchu.view.fragment.FootprintDetailFragment;

public class MyFootprintDetailActivity extends BaseActivity {


    @Bind(R.id.title_back_iv)
    ImageView titleBackIv;
    @Bind(R.id.title_content_tv)
    TextView titleContentTv;
    @Bind(R.id.container)
    ViewPager viewPager;

    public static final String REQUEST_KEY_MODEL = "model";
    @Bind(R.id.headImage)
    SimpleDraweeView headImage;
    @Bind(R.id.detail)
    TextView detail;
    @Bind(R.id.container_bottom)
    RelativeLayout containerBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_footprint_detail);
        ButterKnife.bind(this);
        initListener();
        initData();
    }

    private void initData() {
        PostCardItemModel bean = getIntent().getParcelableExtra(REQUEST_KEY_MODEL);
        if (bean != null) {
            titleContentTv.setText(bean.getTime());
            headImage.setImageURI(Uri.parse(bean.getAutorPhoto()));

            StringBuilder text1 = new StringBuilder();
            text1.append(bean.getAutor());
            text1.append(":");
            int index1 = text1.length();
            text1.append(bean.getComment());
            text1.append("。- 在 ");
            int index2 = text1.length();
            text1.append(bean.getPlcaeAddress());
            int index3 = text1.length();

            SpannableStringBuilder builder = new SpannableStringBuilder(text1.toString());
            builder.setSpan(new ForegroundColorSpan(Color.argb(255, 255, 255, 255)), 0, index1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            builder.setSpan(new ForegroundColorSpan(Color.parseColor("#838181")), index1, index2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            builder.setSpan(new ForegroundColorSpan(Color.argb(255, 255, 255, 255)), index2, index3, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            detail.setText(builder);

            List<FootprintDetailFragment> fragments = new ArrayList<>();
            for (PostCardImageListModel item : bean.getImglist()) {
                Bundle bund = new Bundle();
                bund.putParcelable(FootprintDetailFragment.REQUEST_KEY_IMAGE_ENTITY, item);
                FootprintDetailFragment fragment = new FootprintDetailFragment();
                fragment.setArguments(bund);
                fragments.add(fragment);
            }
            PagerAdapter mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
            viewPager.setAdapter(mPagerAdapter);
        }
    }

    private void initListener() {
        titleBackIv.setOnClickListener(this);
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
                        if (event.getX() - x < 100 && event.getY() - y < 100) {
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
        private List<FootprintDetailFragment> fragments;

        public PagerAdapter(FragmentManager fm, List<FootprintDetailFragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments == null ? 0 : fragments.size();
        }

    }
}
