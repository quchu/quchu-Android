package co.quchu.quchu.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.analysis.GatherCollectModel;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.dialog.ShareDialogFg;
import co.quchu.quchu.dialog.VisitorLoginDialogFg;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.presenter.InterestingDetailPresenter;
import co.quchu.quchu.presenter.RecommendPresenter;
import co.quchu.quchu.presenter.RecommentFragPresenter;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.view.activity.InterestingDetailsActivity;
import co.quchu.quchu.view.adapter.RecommendAdapter2;
import co.quchu.quchu.widget.recyclerviewpager.RecyclerViewPager;

/**
 * RecommendFragment
 * User: Chenhs
 * Date: 2015-12-07
 * 推荐
 */
public class RecommendFragment2 extends Fragment implements RecommendAdapter2.CardClickListener, IRecommendFragment, RecyclerViewPager.OnPageChangedListener {
    @Bind(R.id.f_recommend_rvp)
    RecyclerViewPager recyclerView;
    public List<RecommendModel> cardList;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.f_recommend_bimg_bottom)
    ImageView fRecommendBimgBottom;
    @Bind(R.id.f_recommend_bimg_top)
    ImageView fRecommendBimgTop;
    private boolean isLoading = false;
    private RecommendAdapter2 adapter;

    RecommentFragPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend_hvp_new, container, false);
        ButterKnife.bind(this, view);
        presenter = new RecommentFragPresenter(getContext(), this);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layout);
        adapter = new RecommendAdapter2(getActivity(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener();
        recyclerView.addOnPageChangedListener(this);
        recyclerView.addOnLayoutChangeListener();

        presenter.init();

        return view;
    }

    @Override
    public void OnPageChanged(int oldPosition, int newPosition) {
        LogUtils.json("newPosition=" + newPosition + "//oldPosition=" + oldPosition + "//cardList.size() - 1===" + (cardList.size() - 1));
        if (newPosition > oldPosition && cardList.size() > 3 && newPosition == cardList.size() - 1 && !isLoading) {
            loadMore("");
        }
    }


    private int pageCounts = 2, pageNums = 1;
    private int hasChangePosition = 0;

    @Override
    public void onCardLick(View view, int position) {

        switch (view.getId()) {
            case R.id.root_cv:
                AppContext.selectedPlace = cardList.get(position);
                hasChangePosition = position;
                Intent intent = new Intent(getActivity(), InterestingDetailsActivity.class);
                intent.putExtra("pPosition", position);
                intent.putExtra("pId", cardList.get(position).getPid());
                startActivity(intent);
                break;
            case R.id.item_recommend_card_collect_rl:
                setFavorite(position);
                break;
            case R.id.item_recommend_card_interest_rl:
                ShareDialogFg shareDialogFg = ShareDialogFg.newInstance(cardList.get(position).getPid(), cardList.get(position).getName(), true);
                shareDialogFg.show(getActivity().getFragmentManager(), "share_place");
                break;
        }
    }

    private void setFavorite(final int position) {
        if (AppContext.user.isIsVisitors()) {
            VisitorLoginDialogFg vDialog = VisitorLoginDialogFg.newInstance(VisitorLoginDialogFg.QFAVORITE);
            vDialog.show(getActivity().getFragmentManager(), "visitor");
        } else {
            InterestingDetailPresenter.setDetailFavorite(getActivity(), cardList.get(position).getPid(), cardList.get(position).isIsf(), new InterestingDetailPresenter.DetailDataListener() {
                @Override
                public void onSuccessCall(String str) {
                    cardList.get(position).setIsf(!cardList.get(position).isIsf());
                    adapter.notifyDataSetChanged();
                    if (cardList.get(position).isIsf()) {
                        Toast.makeText(getActivity(), "收藏成功!", Toast.LENGTH_SHORT).show();
                        AppContext.gatherList.add(new GatherCollectModel(GatherCollectModel.collectPlace, cardList.get(position).getPid()));
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
        cardList.set(hasChangePosition, AppContext.selectedPlace);
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    public void removeDataSet(int removeIndex) {
        if (adapter != null && removeIndex < cardList.size()) {
            LogUtils.json("removeDataSet==" + removeIndex);
            cardList.remove(removeIndex);
            adapter.notifyDataSetChanged();
        }
    }

    public void hint() {
        if (recyclerView != null)
            recyclerView.setVisibility(View.GONE);
    }

    public void show() {
        if (recyclerView != null)
            recyclerView.setVisibility(View.VISIBLE);
    }


    @Override
    public void initTab() {
        for (int i = 0; i < 10; i++)
            tabLayout.addTab(tabLayout.newTab().setText("兴趣1"));
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
// TODO: 2016/2/26  参数第一个tab
        startAnimation();
    }

    @Override
    public void initTabData(List<RecommendModel> arrayList, int pageCount, int pageNum) {
        cardList = arrayList;
        if (adapter == null)
            adapter = new RecommendAdapter2(getActivity(), RecommendFragment2.this);
        adapter.changeDataSet(cardList);
        pageCounts = pageCount;
        pageNums = pageNum;
        if (recyclerView != null && cardList.size() > 0)
            recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void loadMore(String type) {
        if (pageNums < pageCounts) {
            isLoading = true;
            pageNums++;
            RecommendPresenter.loadMoreRecommendList(getActivity(), true, pageNums, new RecommendPresenter.GetRecommendListener() {
                @Override
                public void onSuccess(ArrayList<RecommendModel> arrayList, int pageCount, int pageNum) {
                    LogUtils.json("pageNums==" + pageNums);
                    pageCounts = pageCount;
                    pageNums = pageNum;
                    if (arrayList != null && arrayList.size() > 0) {
                        cardList.addAll(arrayList);
                        adapter.notifyDataSetChanged();
                    }
                    isLoading = false;
                }

                @Override
                public void onError() {
                    isLoading = false;
                }
            });
        }
    }

    @Override
    public void tabChangeAnimaton() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }



    /**
     * animation start
     */
    private long animationDuration = 3 * 1000;

    private void startAnimation() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(fRecommendBimgTop, "alpha", 1f, 0.2f);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(fRecommendBimgBottom, "alpha", 0.2f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.setDuration(animationDuration);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.playTogether(objectAnimator, objectAnimator2);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                fRecommendBimgTop.setVisibility(View.INVISIBLE);
                presenter.initTabData(true);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animatorSet.start();
    }
    /**
     * animation end
     */
}
