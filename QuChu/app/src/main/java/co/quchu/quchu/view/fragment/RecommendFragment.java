package co.quchu.quchu.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.presenter.RecommendPresenter;
import co.quchu.quchu.view.adapter.RecommendAdapter;
import co.quchu.quchu.widget.recyclerviewpager.RecyclerViewPager;

/**
 * RecommendFragment
 * User: Chenhs
 * Date: 2015-12-07
 * 推荐
 */
public class RecommendFragment extends Fragment {
    @Bind(R.id.f_recommend_rvp)
    RecyclerViewPager fRecommendRvp;
    @Bind(R.id.f_recommend_bottom_rl)
    RelativeLayout fRecommendBottomRl;
    private View view;
    private float viewStartY = 0f;
    private int viewHeight = 0;
public boolean isRunningAnimation=false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recommend_hvp, null);
        ButterKnife.bind(this, view);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        fRecommendRvp.setLayoutManager(layout);
        adapter = new RecommendAdapter(getActivity());
        fRecommendRvp.setAdapter(adapter);
        fRecommendRvp.setHasFixedSize(true);
        fRecommendBottomRl.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewHeight = fRecommendBottomRl.getHeight();
            }
        });
        fRecommendRvp.addOnScrollListener();
        fRecommendRvp.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() {
            @Override
            public void OnPageChanged(int oldPosition, int newPosition) {
                Log.d("test", "oldPosition:" + oldPosition + " newPosition:" + newPosition);
                if (newPosition <= 2) {
                    if (fRecommendBottomRl.getVisibility() == View.VISIBLE)
                        if (!isRunningAnimation)
                        RecommendPresenter.showBottomAnimation(RecommendFragment.this, fRecommendBottomRl, viewHeight, false);
                } else if (newPosition >= 3) {
                    if (fRecommendBottomRl.getVisibility() == View.INVISIBLE)
                        if (!isRunningAnimation)
                        RecommendPresenter.showBottomAnimation(RecommendFragment.this, fRecommendBottomRl, viewHeight, true);

                }
            }
        });

        fRecommendRvp.addOnLayoutChangeListener();
        RecommendPresenter.getRecommendList(getActivity(), "", new RecommendPresenter.GetRecommendListener() {
            @Override
            public void onSuccess(ArrayList<RecommendModel> arrayList) {
                adapter.changeDataSet(arrayList);
            }
        });
        return view;
    }

    private RecommendAdapter adapter;

    @OnClick(R.id.f_recommend_bottom_rl)
    public void bottomClick(View view) {
        fRecommendRvp.smoothScrollToPosition(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
