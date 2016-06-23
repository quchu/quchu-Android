package co.quchu.quchu.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.model.TagsModel;
import co.quchu.quchu.presenter.RecommentFragPresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.ScreenUtils;
import co.quchu.quchu.view.activity.QuchuDetailsActivity;
import co.quchu.quchu.view.activity.SearchActivity;
import co.quchu.quchu.view.adapter.RecommendAdapter;
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
public class RecommendFragment extends BaseFragment implements RecommendAdapter.CardClickListener, IRecommendFragment, ViewPager.OnPageChangeListener, ViewPager.PageTransformer {
    @Bind(R.id.viewpager)
    ViewPager viewpager;
//    @Bind(R.id.tabLayout)
//    TabLayout tabLayout;
    @Bind(R.id.refreshLayout)
    HorizontalSwipeRefLayout refreshLayout;
    @Bind(R.id.errorView)
    ErrorView errorView;
    @Bind(R.id.tvPageIndicator)
    TextView tvPageIndicator;
    @Bind(R.id.rgDisplayMode)
    RadioGroup radioGroup;
    @Bind(R.id.rvGrid)
    RecyclerView rvGrid;

    private boolean isLoading = false;
    public List<RecommendModel> cardList = new ArrayList<>();
    private RecommendAdapter adapter;
    private RecommentFragPresenter presenter;
    private int currentIndex = 0;
    private int dataCount = -1;

    private String from = QuchuDetailsActivity.FROM_TYPE_HOME;
    private boolean mAnimationRunning = false;
    public static final int ANIMATION_DURATION = 350;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recommend_hvp_new, container, false);
        ButterKnife.bind(this, view);
        viewpager.setClipToPadding(false);
        viewpager.setPadding(60, 0, 60, 0);
        viewpager.setPageMargin(20);
        viewpager.addOnPageChangeListener(this);
        adapter = new RecommendAdapter(this, cardList, this);
        viewpager.setAdapter(adapter);
        rvGrid.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.quarter_margin), 2));

        rvGrid.setAdapter(new RecommendGridAdapter(cardList,null));
        rvGrid.setLayoutManager(new GridLayoutManager(getContext(),2));
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvPageIndicator.setText((position+1) +" of "+ adapter.getCount());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        viewpager.setPageTransformer(true, this);not use please  see adapter
        presenter = new RecommentFragPresenter(getContext(), this);
        refreshLayout.setColorSchemeResources(R.color.standard_color_yellow);
        initData();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rbFavorites:
                        viewpager.clearAnimation();
                        rvGrid.clearAnimation();
                        for (int i = rvGrid.getChildCount()-1; i >=0; i--) {
                            if (rvGrid.getChildAt(i)==null){
                                return;
                            }
                            rvGrid.getChildAt(i).clearAnimation();
                            if (i==0){
                                rvGrid.getChildAt(i).animate().translationY(150).alpha(0).setDuration(ANIMATION_DURATION).setInterpolator(new AccelerateDecelerateInterpolator()).setStartDelay((rvGrid.getChildCount()-i)*30).withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        rvGrid.setVisibility(View.GONE);
                                    }
                                }).start();
                            }else{
                                rvGrid.getChildAt(i).animate().translationY(150).alpha(0).setDuration(ANIMATION_DURATION).setInterpolator(new AccelerateDecelerateInterpolator()).setStartDelay((rvGrid.getChildCount()-i)*30).start();
                            }

                        }
                        rvGrid.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                viewpager.animate().translationX(0).alpha(1).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(ANIMATION_DURATION).start();
                            }
                        },rvGrid.getChildCount()*30);


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
                                    rvGrid.getChildAt(i).animate().translationY(0).alpha(1).setDuration(ANIMATION_DURATION).setInterpolator(new AccelerateDecelerateInterpolator()).setStartDelay(i*30).start();

                                }
                            }
                        },0);


                        break;
                }
            }
        });
        refreshLayout.setOnRefreshListener(new HorizontalSwipeRefLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.initTabData(true, selectedTag);
            }
        });
        view.setClickable(true);
        return view;
    }


    public void initData() {
        presenter.init();
    }

    private int hasChangePosition = 0;

    @Override
    public void onCardLick(View view, int position) {

        AppContext.selectedPlace = cardList.get(viewpager.getCurrentItem());
        hasChangePosition = viewpager.getCurrentItem();
        if (from.equals(QuchuDetailsActivity.FROM_TYPE_HOME)) {
            MobclickAgent.onEvent(getContext(), "detail_home_c");
        } else {
            MobclickAgent.onEvent(getContext(), "detail_tag_c");
        }
        MobclickAgent.onEvent(getActivity(), "detail_c");
        Intent intent = new Intent(getActivity(), QuchuDetailsActivity.class);
        intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, cardList.get(viewpager.getCurrentItem()).getPid());
        intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_FROM, from);
        getActivity().startActivity(intent);

    }


    public void updateDateSet() {
        if (null != cardList && cardList.size() > hasChangePosition) {
            cardList.set(hasChangePosition, AppContext.selectedPlace);
            if (adapter != null)
                adapter.notifyDataSetChanged();

        }
    }


    private List<TagsModel> tagList;

    @Override
    public void initTab(boolean isError, List<TagsModel> list) {

        if (isError) {
            errorView.showViewDefault(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.init();
                }
            });
            viewpager.setVisibility(View.GONE);
            return;
        }
        viewpager.setVisibility(View.VISIBLE);
        errorView.hideView();

        tagList = list;
        if (tagList.size() > 0) {
            selectedTag = tagList.get(0).getEn();
            presenter.initTabData(false, selectedTag);
        }
    }


    private String selectedTag = "";

    @Override
    public void initTabData(boolean isError, List<RecommendModel> arrayList, int pageCount, int pageNum, int rowCount) {

        dataCount = rowCount > 0 ? rowCount : -1;

        if (null == refreshLayout) {
            return;
        }

        refreshLayout.setRefreshing(false);
        if (isError) {
            errorView.showViewDefault(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.initTabData(false, selectedTag);
                }
            });
            viewpager.setVisibility(View.GONE);
        } else {
            if (viewpager.getVisibility() == View.GONE) {
                errorView.hideView();
                viewpager.setVisibility(View.VISIBLE);
            }
            cardList.clear();
            cardList.addAll(arrayList);
            adapter.notifyDataSetChanged();
            tvPageIndicator.setText((viewpager.getCurrentItem()+1) +" of "+ adapter.getCount());

            if (cardList.size() > 0)
                viewpager.setCurrentItem(0);

        }
    }

    @Override
    public void loadMore(boolean isError, List<RecommendModel> arrayList, int pageCount, int pageNum) {
        isLoading = false;
        DialogUtil.dismissProgessDirectly();
        if (isError) {
            Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
        } else {
            if (arrayList != null && arrayList.size() > 0) {
                cardList.addAll(arrayList);
                adapter.notifyDataSetChanged();
                tvPageIndicator.setText((viewpager.getCurrentItem()+1) +" of "+ adapter.getCount());
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

        if (event.getFlag() == EventFlags.EVENT_FOOTPRINT_UPDATED) {
            if ((Integer) event.getContent()[0] == cardList.get(currentIndex).getPid()) {
                cardList.get(currentIndex).isout = true;
                adapter.notifyDataSetChanged();
            }
        }else if(event.getFlag() == EventFlags.EVENT_LOCATION_UPDATED){
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        LogUtils.e("childCound" + viewpager.getChildCount());
        MobclickAgent.onEvent(getContext(), "recommendation_c");
//        if (newPosition > oldPosition) {
//            MobclickAgent.onEvent(getContext(), "slideright_c");
//        }
        if (cardList.size() < dataCount) {
            if (position == cardList.size() - 2 && !isLoading) {
                isLoading = true;
                presenter.loadMore(selectedTag, (cardList.size() / 10) + 1);
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
