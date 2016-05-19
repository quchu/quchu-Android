package co.quchu.quchu.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
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
import co.quchu.quchu.dialog.ShareDialogFg;
import co.quchu.quchu.dialog.VisitorLoginDialogFg;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.model.TagsModel;
import co.quchu.quchu.presenter.InterestingDetailPresenter;
import co.quchu.quchu.presenter.RecommentFragPresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.view.activity.QuchuDetailsActivity;
import co.quchu.quchu.view.adapter.RecommendAdapter;
import co.quchu.quchu.widget.ErrorView;
import co.quchu.quchu.widget.RefreshLayout.HorizontalSwipeRefLayout;
import co.quchu.quchu.widget.recyclerviewpager.RecyclerViewPager;


/**
 * RecommendFragment
 * User: Chenhs
 * Date: 2015-12-07
 * 推荐
 */
public class RecommendFragment extends BaseFragment implements RecommendAdapter.CardClickListener, IRecommendFragment, RecyclerViewPager.OnPageChangedListener {
    @Bind(R.id.recyclerView)
    RecyclerViewPager recyclerView;
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
    private int currentIndex = -1;
    private int dataCount = -1;

    private String from = QuchuDetailsActivity.FROM_TYPE_HOME;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend_hvp_new, container, false);
        ButterKnife.bind(this, view);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layout);
        adapter = new RecommendAdapter(getActivity(), cardList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener();
        recyclerView.addOnPageChangedListener(this);
        presenter = new RecommentFragPresenter(getContext(), this);
        recyclerView.addOnLayoutChangeListener();
        refreshLayout.setColorSchemeResources(R.color.standard_color_yellow);
        initData();

        refreshLayout.setOnRefreshListener(new HorizontalSwipeRefLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNums = 1;
                presenter.initTabData(true, selectedTag);
            }
        });

        return view;
    }

    public void initData() {
        presenter.init();

    }

    @Override
    public void OnPageChanged(int oldPosition, int newPosition) {
        LogUtils.json("newPosition-> " + newPosition + " oldPosition-> " + oldPosition
                + " cardList.size()" + (cardList.size()) + " pageNums-> " + pageNums + " pageCounts-> " + pageCounts);

        MobclickAgent.onEvent(getContext(), "recommendation_c");
        if (newPosition > oldPosition) {
            MobclickAgent.onEvent(getContext(), "slideright_c");
        }
        if (newPosition > oldPosition && cardList.size() < dataCount) {
            if (newPosition == cardList.size() - 2 && !isLoading) {
                isLoading = true;
                presenter.loadMore(selectedTag, (cardList.size() / 10) + 1);
            } else if (isLoading) {
                DialogUtil.showProgess(getActivity(), R.string.loading_dialog_text);
            }
        }
        currentIndex = newPosition;
    }


    private int pageCounts, pageNums = 1;
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
                    startActivity(intent);
                }
                break;
            case R.id.item_recommend_card_collect_iv:
                //收藏
                setFavorite(position);
                break;
            case R.id.item_recommend_card_interest_iv:
                //分享
                ShareDialogFg shareDialogFg = ShareDialogFg.newInstance(cardList.get(position).getPid(), cardList.get(position).getName(), true);
                shareDialogFg.show(getActivity().getSupportFragmentManager(), "share_place");
                break;
        }
    }

    private void setFavorite(final int position) {
        if (AppContext.user.isIsVisitors()) {
            VisitorLoginDialogFg vDialog = VisitorLoginDialogFg.newInstance(VisitorLoginDialogFg.QFAVORITE);
            vDialog.show(getActivity().getSupportFragmentManager(), "visitor");
        } else {
            InterestingDetailPresenter.setDetailFavorite(getActivity(), cardList.get(position).getPid(), cardList.get(position).isIsf(), new InterestingDetailPresenter.DetailDataListener() {
                @Override
                public void onSuccessCall(String str) {
                    cardList.get(position).setIsf(!cardList.get(position).isIsf());
                    adapter.notifyDataSetChanged();
                    if (cardList.get(position).isIsf()) {
                        Toast.makeText(getActivity(), "收藏成功!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "取消收藏!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onErrorCall(String str) {

                }
            });
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
            recyclerView.setVisibility(View.GONE);
            return;
        }
        tabLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        errorView.himeView();

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
                //mBlurEffectAnimationHandler.sendEmptyMessageDelayed(MESSAGE_FLAG_DELAY_TRIGGER, 500L);
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
            recyclerView.setVisibility(View.GONE);
        } else {
            if (recyclerView.getVisibility() == View.GONE) {
                errorView.himeView();
                recyclerView.setVisibility(View.VISIBLE);
            }
            cardList.clear();
            cardList.addAll(arrayList);
            adapter.notifyDataSetChanged();
            pageCounts = pageCount;
            if (cardList.size() > 0)
                recyclerView.smoothScrollToPosition(0);

        }
    }

    @Override
    public void loadMore(boolean isError, List<RecommendModel> arrayList, int pageCount, int pageNum) {
        isLoading = false;
        DialogUtil.dismissProgessDirectly();
        if (isError) {
            Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
        } else {
            pageCounts = pageCount;
            pageNums = ++pageNum;
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
                adapter.notifyItemChanged(currentIndex);
            }
        }
    }
}
