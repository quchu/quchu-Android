package co.quchu.quchu.view.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.PagerModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.model.SceneModel;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.ScenePresenter;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.ScreenUtils;
import co.quchu.quchu.view.activity.QuchuDetailsActivity;
import co.quchu.quchu.view.adapter.AllSceneAdapter;
import co.quchu.quchu.view.adapter.RecommendGridAdapter;
import co.quchu.quchu.widget.ErrorView;
import co.quchu.quchu.widget.RefreshLayout.HorizontalSwipeRefLayout;
import co.quchu.quchu.widget.SpacesItemDecoration;


/**
 * RecommendFragment
 * User: Chenhs
 * Date: 2015-12-07
 * 推荐
 */
public class RecommendFragment extends BaseFragment implements AllSceneAdapter.CardClickListener, ViewPager.OnPageChangeListener, ViewPager.PageTransformer {
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    //    @Bind(R.id.tabLayout)
//    TabLayout tabLayout;
    @Bind(R.id.refreshLayout)
    HorizontalSwipeRefLayout refreshLayout;
    @Bind(R.id.errorView)
    ErrorView errorView;
    @Bind(R.id.tvPageIndicatorCurrent)
    TextView tvPageIndicatorCurrent;
    @Bind(R.id.tvPageIndicatorSize)
    TextView TvPageIndicatorSize;
    @Bind(R.id.tvPageIndicatorLabel)
    TextView tvPageIndicatorLabel;

    @Bind(R.id.rgDisplayMode)
    RadioGroup radioGroup;
    @Bind(R.id.rvGrid)
    RecyclerView rvGrid;

    private boolean isLoading = false;
    private List<SceneModel> cardList = new ArrayList<>();
    private AllSceneAdapter adapter;
    private int currentIndex = 0;
    private int dataCount = -1;

    private String from = QuchuDetailsActivity.FROM_TYPE_HOME;
    public static final int ANIMATION_DURATION = 350;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recommend_hvp_new, container, false);
        ButterKnife.bind(this, view);
        viewpager.setClipToPadding(false);
        viewpager.setPadding(160, 0, 160, 0);
        viewpager.setPageMargin(80);
        viewpager.addOnPageChangeListener(this);
        adapter = new AllSceneAdapter(this, cardList, this);
        viewpager.setAdapter(adapter);
        rvGrid.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.quarter_margin), 2));

        rvGrid.setAdapter(new RecommendGridAdapter(cardList, null));
        rvGrid.setLayoutManager(new GridLayoutManager(getContext(), 2));
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "AGENCYFB.TTF");
        TvPageIndicatorSize.setTypeface(face);
        tvPageIndicatorLabel.setTypeface(face);
        tvPageIndicatorCurrent.setTypeface(face);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvPageIndicatorCurrent.setText(String.valueOf(position + 1));
                TvPageIndicatorSize.setText(String.valueOf(adapter.getCount()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        viewpager.setPageTransformer(true, this);not use please  see adapter
        refreshLayout.setColorSchemeResources(R.color.standard_color_yellow);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbFavorites:
                        viewpager.clearAnimation();
                        rvGrid.clearAnimation();
                        for (int i = rvGrid.getChildCount() - 1; i >= 0; i--) {
                            if (rvGrid.getChildAt(i) == null) {
                                return;
                            }
                            if (i == 0) {
                                rvGrid.getChildAt(i).animate()
                                        .translationY(150)
                                        .setDuration(ANIMATION_DURATION)
                                        .setInterpolator(new AccelerateDecelerateInterpolator())
                                        .setStartDelay((rvGrid.getChildCount() - i) * 30).withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        rvGrid.setVisibility(View.GONE);
                                    }
                                }).start();
                            } else {
                                rvGrid.getChildAt(i).animate()
                                        .translationY(150)
                                        .setDuration(ANIMATION_DURATION)
                                        .setInterpolator(new AccelerateDecelerateInterpolator())
                                        .setStartDelay((rvGrid.getChildCount() - i) * 30).start();
                            }

                        }
                        rvGrid.animate().alpha(0).setDuration(ANIMATION_DURATION).start();
                        rvGrid.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                viewpager.animate().translationX(0).alpha(1).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(ANIMATION_DURATION).start();
                            }
                        }, rvGrid.getChildCount() * 30);


//                        tvPageIndicator.animate()
//                                .withLayer()
//                                .alpha(1)
//                                .translationY(0)
//                                .setInterpolator(new AccelerateDecelerateInterpolator())
//                                .setDuration(ANIMATION_DURATION)
//                                .withStartAction(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        tvPageIndicator.setVisibility(View.VISIBLE);
//                                    }
//                                }).start();

                        break;
                    case R.id.rbAll:
                        viewpager.clearAnimation();
                        rvGrid.clearAnimation();
                        int edge = ScreenUtils.getScreenWidth(getActivity());

                        viewpager.animate().translationX(edge).alpha(0).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(ANIMATION_DURATION).start();

                        rvGrid.setVisibility(View.VISIBLE);

                        rvGrid.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < rvGrid.getChildCount(); i++) {
                                    rvGrid.getChildAt(i).setTranslationY(150);
                                    rvGrid.getChildAt(i).animate().translationY(0).setDuration(ANIMATION_DURATION).setInterpolator(new AccelerateDecelerateInterpolator()).setStartDelay(i * 30).start();

                                }
                            }
                        }, 10);
                        rvGrid.animate().alpha(1).setDuration(ANIMATION_DURATION).start();
//                        tvPageIndicator.animate()
//                                .alpha(0)
//                                .translationY(tvPageIndicator.getHeight())
//                                .setInterpolator(new AccelerateDecelerateInterpolator())
//                                .setDuration(ANIMATION_DURATION)
//                                .withEndAction(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        tvPageIndicator.setVisibility(View.GONE);
//                                    }
//                                })
//                                .start();
                        break;
                }
            }
        });
        refreshLayout.setOnRefreshListener(new HorizontalSwipeRefLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mThreadRunning){
                    return;
                }
                getData(false);
            }
        });
        view.setClickable(true);

        getData(false);
        return view;
    }


    private boolean mThreadRunning = false;

    public void getData(final boolean loadMore) {
        mThreadRunning = true;

        ScenePresenter.getAllScene(getContext(), SPUtils.getCityId(), 0, new CommonListener<PagerModel<SceneModel>>() {

            @Override
            public void successListener(PagerModel<SceneModel> response) {
                refreshLayout.setRefreshing(false);
                if (response != null && response.getResult()!=null) {
                    if (!loadMore){
                        cardList.clear();
                    }
                    cardList.addAll(response.getResult());
                    adapter.notifyDataSetChanged();

                    tvPageIndicatorCurrent.setText(String.valueOf(viewpager.getCurrentItem() + 1));
                    TvPageIndicatorSize.setText(String.valueOf(adapter.getCount()));
                }
                mThreadRunning = false;
            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {
                refreshLayout.setRefreshing(false);
                mThreadRunning = false;
            }
        });
    }


    @Override
    public void onCardLick(View view, int position) {

        if (from.equals(QuchuDetailsActivity.FROM_TYPE_HOME)) {
            MobclickAgent.onEvent(getContext(), "detail_home_c");
        } else {
            MobclickAgent.onEvent(getContext(), "detail_tag_c");
        }
        MobclickAgent.onEvent(getActivity(), "detail_c");
        Intent intent = new Intent(getActivity(), QuchuDetailsActivity.class);
        intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, cardList.get(viewpager.getCurrentItem()).getSceneId());
        intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_FROM, from);
        getActivity().startActivity(intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle();
        }

    }




    public void loadMore(boolean isError, List<SceneModel> arrayList, int pageCount, int pageNum) {
        isLoading = false;
        DialogUtil.dismissProgessDirectly();
        if (isError) {
            Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
        } else {
            if (arrayList != null && arrayList.size() > 0) {
                cardList.addAll(arrayList);
                adapter.notifyDataSetChanged();

                tvPageIndicatorCurrent.setText(String.valueOf(viewpager.getCurrentItem() + 1));
                TvPageIndicatorSize.setText(String.valueOf(adapter.getCount()));
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onResume() {
        MobclickAgent.onPageStart("h_recommendtion");
        super.onResume();
    }

    @Override
    public void onPause() {
        MobclickAgent.onPageEnd("h_recommendtion");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Subscribe
    public void onMessageEvent(QuchuEventModel event) {

       //TODO favorite udpate
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        LogUtils.e("childCound" + viewpager.getChildCount());
        MobclickAgent.onEvent(getContext(), "recommendation_c");
        if (cardList.size() < dataCount) {
            if (position == cardList.size() - 2 && !isLoading) {
                isLoading = true;

                //TODO Load More
                //presenter.loadMore(selectedTag, (cardList.size() / 10) + 1);
            } else if (isLoading) {
                DialogUtil.showProgess(getActivity(), R.string.loading_dialog_text);
            }
        }
        currentIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public static float MIN_SCALE = .9f;

    @Override
    public void transformPage(View page, float position) {
//        LogUtils.e("id: " + page + " position:" + position);
        if (position <= 1) {
            if (position < 0) {//滑出的页 0.0 ~ -1 *
                float scaleFactor = (1 - MIN_SCALE) * (0 - position);
                page.setScaleY(1 - scaleFactor);
            } else {//滑进的页 1 ~ 0.0 *
                float scaleFactor = (1 - MIN_SCALE) * (1 - position);
                page.setScaleY(MIN_SCALE + scaleFactor);
            }
        }

    }

    public ViewPager getViewpager() {
        return viewpager;
    }
}
