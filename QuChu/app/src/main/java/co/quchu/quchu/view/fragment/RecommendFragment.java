package co.quchu.quchu.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.analysis.GatherCollectModel;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.dialog.ShareDialogFg;
import co.quchu.quchu.dialog.VisitorLoginDialogFg;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.presenter.InterestingDetailPresenter;
import co.quchu.quchu.presenter.RecommendPresenter;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.view.activity.InterestingDetailsActivity;
import co.quchu.quchu.view.adapter.RecommendAdapter;
import co.quchu.quchu.widget.recyclerviewpager.RecyclerViewPager;

/**
 * RecommendFragment
 * User: Chenhs
 * Date: 2015-12-07
 * 推荐
 */
public class RecommendFragment extends BaseFragment implements RecommendAdapter.CardClickListener {
    @Bind(R.id.f_recommend_rvp)
    RecyclerViewPager fRecommendRvp;
    @Bind(R.id.f_recommend_bottom_rl)
    ImageView fRecommendBottomRl;
    private int viewHeight = 0;
    public boolean isRunningAnimation = false;
    public ArrayList<RecommendModel> cardList;
RelativeLayout itemback;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend_hvp, container, false);
        ButterKnife.bind(this, view);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        fRecommendRvp.setLayoutManager(layout);
        adapter = new RecommendAdapter(getActivity(), this);
        fRecommendRvp.setAdapter(adapter);
        fRecommendRvp.setHasFixedSize(true);
        fRecommendBottomRl.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                fRecommendBottomRl.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                viewHeight = fRecommendBottomRl.getHeight();
            }
        });
        fRecommendRvp.addOnScrollListener();
        fRecommendRvp.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() {
            @Override
            public void OnPageChanged(int oldPosition, int newPosition) {
                Log.d("test", "oldPosition:" + oldPosition + " newPosition:" + newPosition);
                if (newPosition <= 2) {
                    if (fRecommendBottomRl.getVisibility() == View.VISIBLE && !isRunningAnimation)
                        RecommendPresenter.showBottomAnimation(RecommendFragment.this, fRecommendBottomRl, viewHeight, false);
                } else if (newPosition >= 3) {
                    if (fRecommendBottomRl.getVisibility() == View.INVISIBLE && !isRunningAnimation)
                        RecommendPresenter.showBottomAnimation(RecommendFragment.this, fRecommendBottomRl, viewHeight, true);
                }
                LogUtils.json("newPosition=" + newPosition + "//oldPosition=" + oldPosition + "//cardList.size() - 1===" + (cardList.size() - 1));
                if (newPosition > oldPosition && cardList.size() > 3 && newPosition == cardList.size() - 1 && !isLoading) {
                    loadMoreDateSet();
                }
                indexPosition = newPosition;
            }
        });
//        fRecommendRvp.addOnLayoutChangeListener();
        changeDataSetFromServer();
        return view;
    }

    private boolean isLoading = false;
    private RecommendAdapter adapter;
    private int indexPosition = 0;

    @OnClick(R.id.f_recommend_bottom_rl)
    public void bottomClick(View view) {
        if (indexPosition > 5) {
            fRecommendRvp.scrollToPosition(0);
        } else {
            fRecommendRvp.smoothScrollToPosition(0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public void changeDataSetFromServer() {
        RecommendPresenter.getRecommendList(getActivity(), true, new RecommendPresenter.GetRecommendListener() {
            @Override
            public void onSuccess(ArrayList<RecommendModel> arrayList, int pageCount, int pageNum) {
                cardList = arrayList;
                if (adapter == null)
                    adapter = new RecommendAdapter(getActivity(), RecommendFragment.this);
                adapter.changeDataSet(cardList);
                pageCounts = pageCount;
                pageNums = pageNum;
                if (fRecommendRvp != null && cardList.size() > 0)
                    fRecommendRvp.smoothScrollToPosition(0);
                if (fRecommendBottomRl != null && fRecommendBottomRl.getVisibility() == View.VISIBLE)
                    RecommendPresenter.showBottomAnimation(RecommendFragment.this, fRecommendBottomRl, viewHeight, false);
            }

            @Override
            public void onError() {

            }
        });
    }

    public void loadMoreDateSet() {
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
                        //    adapter.loadMoreDataSet(arrayList);
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
            //adapter.changeDataSet(cardList);
            adapter.notifyDataSetChanged();
        }
    }

    public void hint() {
        if (fRecommendRvp != null)
            fRecommendRvp.setVisibility(View.GONE);
    }

    public void show() {
        if (fRecommendRvp != null)
            fRecommendRvp.setVisibility(View.VISIBLE);
    }

   /* @Override
    public void onResume() {
        super.onResume();
        if (((RecommendActivity) getActivity()).viewPagerIndex == 0) {
            LogUtils.json("RecommendFragment==onResume");
            if (AppContext.dCardListRemoveIndex != -1) {
                LogUtils.json("AppContext.dCardListRemoveIndex != -1   true");
                removeDataSet(AppContext.dCardListRemoveIndex);
                AppContext.dCardListRemoveIndex = -1;
                AppContext.dCardListNeedUpdate = false;
            } else {
                if (AppContext.dCardListNeedUpdate) {
                    updateDateSet();
                    AppContext.dCardListNeedUpdate = false;
                }
            }
        }
    }*/
}
