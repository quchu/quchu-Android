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
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.analysis.GatherCollectModel;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.dialog.ShareDialogFg;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.presenter.InterestingDetailPresenter;
import co.quchu.quchu.presenter.RecommendPresenter;
import co.quchu.quchu.utils.AppKey;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.activity.InterestingDetailsActivity;
import co.quchu.quchu.view.adapter.RecommendAdapter;
import co.quchu.quchu.widget.recyclerviewpager.RecyclerViewPager;

/**
 * RecommendFragment
 * User: Chenhs
 * Date: 2015-12-07
 * 推荐
 */
public class DefaultRecommendFragment extends Fragment implements RecommendAdapter.CardClickListener {
    @Bind(R.id.f_recommend_rvp)
    RecyclerViewPager dfRecommendRvp;
    @Bind(R.id.f_recommend_bottom_rl)
    ImageView dfRecommendBottomRl;
    private View view;
    private float viewStartY = 0f;
    private int dViewHeight = 0;
    public boolean isRunningAnimation = false;
    public ArrayList<RecommendModel> dCardList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recommend_hvp, null);
        ButterKnife.bind(this, view);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        dfRecommendRvp.setLayoutManager(layout);
        adapter = new RecommendAdapter(getActivity(), this);
        dfRecommendRvp.setAdapter(adapter);
        dfRecommendRvp.setHasFixedSize(true);
        dfRecommendBottomRl.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                dViewHeight = dfRecommendBottomRl.getHeight();
            }
        });
        dfRecommendRvp.addOnScrollListener();
        dfRecommendRvp.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() {
            @Override
            public void OnPageChanged(int oldPosition, int newPosition) {
                Log.d("test", "oldPosition:" + oldPosition + " newPosition:" + newPosition);
                if (newPosition <= 2) {
                    if (dfRecommendBottomRl.getVisibility() == View.VISIBLE)
                        if (!isRunningAnimation)
                            RecommendPresenter.showBottomAnimation(DefaultRecommendFragment.this, dfRecommendBottomRl, dViewHeight, false);
                } else if (newPosition >= 3) {
                    if (dfRecommendBottomRl.getVisibility() == View.INVISIBLE)
                        if (!isRunningAnimation)
                            RecommendPresenter.showBottomAnimation(DefaultRecommendFragment.this, dfRecommendBottomRl, dViewHeight, true);
                }
                if (newPosition > oldPosition && dCardList.size() > 3 && newPosition == dCardList.size() - 1 && !isLoading) {
                    loadMoreDateSet();
                }
            }
        });
        dfRecommendRvp.addOnLayoutChangeListener();
        if (!StringUtils.isEmpty(SPUtils.getValueFromSPMap(getActivity(), AppKey.USERSELECTEDCLASSIFY, ""))) {
            changeDataSetFromServer();
        }
        if (AppContext.dCardList == null)
            AppContext.dCardList = new ArrayList<>();
        return view;
    }

    private boolean isLoading = false;
    private RecommendAdapter adapter;

    @OnClick(R.id.f_recommend_bottom_rl)
    public void bottomClick(View view) {
        dfRecommendRvp.smoothScrollToPosition(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public void changeDataSetFromServer() {
        RecommendPresenter.getRecommendList(getActivity(), false, new RecommendPresenter.GetRecommendListener() {
            @Override
            public void onSuccess(ArrayList<RecommendModel> arrayList, int pageCount, int pageNum) {
                pageCounts = pageCount;
                pageNums = pageNum;
                dCardList = arrayList;
                adapter.changeDataSet(dCardList);
                dfRecommendRvp.smoothScrollToPosition(0);
                if (dfRecommendBottomRl.getVisibility() == View.VISIBLE)
                    RecommendPresenter.showBottomAnimation(DefaultRecommendFragment.this, dfRecommendBottomRl, dViewHeight, false);
            }
        });
    }

    private Intent intent;

    @Override
    public void onCardLick(View view, int position) {
        switch (view.getId()) {
            case R.id.root_cv:
                AppContext.dCardList = dCardList;
                intent = new Intent(getActivity(), InterestingDetailsActivity.class);
                intent.putExtra("pId", dCardList.get(position).getPid());
                intent.putExtra("pPosition", position);
                getActivity().startActivity(intent);
                break;
            case R.id.item_recommend_card_collect_rl:
                setFavorite(position);
                break;
            case R.id.item_recommend_card_interest_rl:
                ShareDialogFg shareDialogFg = ShareDialogFg.newInstance(dCardList.get(position).getPid(), dCardList.get(position).getName(), true);
                shareDialogFg.show(getActivity().getFragmentManager(), "share_place");
                break;
        }
    }

    private void setFavorite(final int position) {
        InterestingDetailPresenter.setDetailFavorite(getActivity(), dCardList.get(position).getPid(), dCardList.get(position).isIsf(), new InterestingDetailPresenter.DetailDataListener() {
            @Override
            public void onSuccessCall(String str) {
                dCardList.get(position).setIsf(!dCardList.get(position).isIsf());
                adapter.notifyDataSetChanged();
                if (dCardList.get(position).isIsf()) {
                    Toast.makeText(getActivity(), "收藏成功!", Toast.LENGTH_SHORT).show();
                    AppContext.gatherList.add(new GatherCollectModel(GatherCollectModel.collectPlace, dCardList.get(position).getPid()));
                } else {
                    Toast.makeText(getActivity(), "取消收藏!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorCall(String str) {

            }
        });
    }

    private int pageCounts = 2, pageNums = 1;

    public void loadMoreDateSet() {
        if (pageNums < pageCounts) {
            isLoading = true;
            pageNums++;
            RecommendPresenter.loadMoreRecommendList(getActivity(), false, pageNums, new RecommendPresenter.GetRecommendListener() {
                @Override
                public void onSuccess(ArrayList<RecommendModel> arrayList, int pageCount, int pageNum) {
                    LogUtils.json("pageNums==" + pageNums);
                    pageCounts = pageCount;
                    pageNums = pageNum;
                    if (arrayList != null && arrayList.size() > 0) {
                        //    adapter.loadMoreDataSet(arrayList);
                        dCardList.addAll(arrayList);
                        adapter.notifyDataSetChanged();
                    }
                    isLoading = false;
                }
            });
        }
    }

    public void updateDateSet() {
        dCardList = AppContext.dCardList;
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    public void removeDataSet(int removeIndex) {
        LogUtils.json("removeDataSet==" + removeIndex);
        if (adapter != null && removeIndex < dCardList.size()) {
            dCardList.remove(removeIndex);
            //  adapter.changeDataSet(dCardList);
            adapter.notifyDataSetChanged();
        }
    }
}
