package co.quchu.quchu.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.model.TagsModel;
import co.quchu.quchu.presenter.RecommentFragPresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.view.activity.QuchuDetailsActivity;
import co.quchu.quchu.view.adapter.RecommendAdapter;
import co.quchu.quchu.widget.ErrorView;
import co.quchu.quchu.widget.RefreshLayout.HorizontalSwipeRefLayout;


/**
 * RecommendFragment
 * User: Chenhs
 * Date: 2015-12-07
 * 推荐
 */
public class RecommendFragment extends BaseFragment implements RecommendAdapter.CardClickListener, IRecommendFragment, ViewPager.OnPageChangeListener, ViewPager.PageTransformer {
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.refreshLayout)
    HorizontalSwipeRefLayout refreshLayout;
    @Bind(R.id.errorView)
    ErrorView errorView;

    private boolean isLoading = false;
    public List<RecommendModel> cardList = new ArrayList<>();
    private RecommendAdapter adapter;
    private RecommentFragPresenter presenter;
    private int currentIndex = 0;
    private int dataCount = -1;

    private String from = QuchuDetailsActivity.FROM_TYPE_HOME;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend_hvp_new, container, false);
        ButterKnife.bind(this, view);
        viewpager.setClipToPadding(false);
        viewpager.setPadding(80, 40, 80, 40);
        viewpager.setPageMargin(40);
        viewpager.addOnPageChangeListener(this);
        adapter = new RecommendAdapter(getActivity(), cardList, this);
        viewpager.setAdapter(adapter);
        viewpager.setPageTransformer(false, this);
        presenter = new RecommentFragPresenter(getContext(), this);
        refreshLayout.setColorSchemeResources(R.color.standard_color_yellow);
        initData();

        refreshLayout.setOnRefreshListener(new HorizontalSwipeRefLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.initTabData(true, selectedTag);
            }
        });
        return view;
    }

    public void initData() {
        presenter.init();

    }

    private int hasChangePosition = 0;

    @Override
    public void onCardLick(View view, int position) {

        switch (view.getId()) {
            case R.id.root_cv:
                AppContext.selectedPlace = cardList.get(position);
                hasChangePosition = position;
                if (!KeyboardUtils.isFastDoubleClick()) {
                    if (from.equals(QuchuDetailsActivity.FROM_TYPE_HOME)) {
                        MobclickAgent.onEvent(getContext(), "detail_home_c");
                    } else {
                        MobclickAgent.onEvent(getContext(), "detail_tag_c");
                    }

                    MobclickAgent.onEvent(getActivity(), "detail_c");
                    Intent intent = new Intent(getActivity(), QuchuDetailsActivity.class);
                    intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, cardList.get(position).getPid());
                    intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_FROM, from);
                    getActivity().startActivity(intent);
                }
                break;

        }
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
            tabLayout.setVisibility(View.GONE);
            viewpager.setVisibility(View.GONE);
            return;
        }
        tabLayout.setVisibility(View.VISIBLE);
        viewpager.setVisibility(View.VISIBLE);
        errorView.hideView();

        tagList = list;
        if (tabLayout.getTabCount() > 0) {
            tabLayout.removeAllTabs();
        }

        for (int i = 0; i < list.size(); i++) {
            TextView textView = (TextView) View.inflate(getActivity(), R.layout.text_view, null);
            textView.setText(list.get(i).getZh());
            if (i == 0) {
                textView.setTextSize(15);
            } else {
                textView.setTextSize(13);
            }
            tabLayout.addTab(tabLayout.newTab().setCustomView(textView));
        }
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    from = QuchuDetailsActivity.FROM_TYPE_HOME;
                } else {
                    from = QuchuDetailsActivity.FROM_TYPE_TAG;
                }


                TextView view = (TextView) tab.getCustomView();
                if (view != null) {
                    view.setTextSize(15);
                }
                MobclickAgent.onEvent(getContext(), "tag_c");
                selectedTag = tagList.get(tab.getPosition()).getEn();
                LogUtils.json("selectedTag=" + selectedTag);
                presenter.initTabData(false, selectedTag);
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView view = (TextView) tab.getCustomView();
                if (view != null) {
                    view.setTextSize(13);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
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

        if (event.getFlag() == EventFlags.EVENT_QUCHU_DETAIL_UPDATED) {
            if ((Integer) event.getContent()[0] == cardList.get(currentIndex).getPid()) {
                cardList.get(currentIndex).isout = true;
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        LogUtils.e("childCound" + viewpager.getChildCount());
    }

    @Override
    public void onPageSelected(int position) {
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
        LogUtils.e("id: " + page + " position:" + position);
        if (position <= 1) {
            if (position < 0) {//滑出的页 0.0 ~ -1 *
                float scaleFactor = (1 - MIN_SCALE) * (0 - position);
//                page.setScaleX(1 - scaleFactor);
                page.setScaleY(1 - scaleFactor);
            } else if (currentIndex != 0) {//滑进的页 1 ~ 0.0 *
                float scaleFactor = (1 - MIN_SCALE) * (1 - position);
//                page.setScaleX(MIN_SCALE + scaleFactor);
                page.setScaleY(MIN_SCALE + scaleFactor);
            }
        }

    }
}
