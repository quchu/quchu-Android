package co.quchu.quchu.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.CommentModel;
import co.quchu.quchu.model.DetailModel;
import co.quchu.quchu.model.PagerModel;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.CommentsPresenter;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.view.adapter.CommentAdapter;
import co.quchu.quchu.widget.EndlessRecyclerOnScrollListener;
import co.quchu.quchu.widget.ErrorView;

/**
 * Created by Nico on 16/7/11.
 */
public class CommentListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

  @Override
  protected String getPageNameCN() {
    return getString(R.string.pname_comment_list);
  }

  public static final String BUNDLE_KEY_PLACE_ID = "BUNDLE_KEY_PLACE_ID";
  public static final String BUNDLE_KEY_AVG_RATING = "BUNDLE_KEY_AVG_RATING";
  public static final String BUNDLE_KEY_RATING_COUNT = "BUNDLE_KEY_RATING_COUNT";
  public static final String BUNDLE_KEY_BIZ_LIST = "BUNDLE_KEY_BIZ_LIST";
  public static final String BUNDLE_KEY_TAG_LIST = "BUNDLE_KEY_TAG_LIST";
  public static final String BUNDLE_KEY_RECENT_RATING = "BUNDLE_KEY_RECENT_RATING";

  @Bind(R.id.rv) RecyclerView rv;
  @Bind(R.id.swipeRefreshLayout) SwipeRefreshLayout mSwipeRefreshLayout;
  @Bind(R.id.errorView) ErrorView errorView;

  private float mAvgRating = 0;
  private float mRecentRating = 0;
  private int mRatingCount = 0;
  private List<DetailModel.BizInfoModel> mBizList;
  private List<CommentModel> mDataSet;
  private List<String> mTagsList;

  private int sceneId;
  private int mMaxPageNo = -1;
  private int mPageNo = 1;
  private EndlessRecyclerOnScrollListener mLoadingListener;

  private List<CommentModel> mSceneList = new ArrayList<>();
  private CommentAdapter mAdapter;
  private boolean mActivityStop = false;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.activity_simple_recyclerview, null, false);
    v.findViewById(R.id.appbar).setVisibility(View.GONE);
    ButterKnife.bind(this, v);

    Bundle bundle = getArguments();
    if (bundle == null) {
      return v;
    }

    sceneId = bundle.getInt(BUNDLE_KEY_PLACE_ID, -1);
    mAvgRating = bundle.getFloat(BUNDLE_KEY_AVG_RATING, 0);
    mRatingCount = bundle.getInt(BUNDLE_KEY_RATING_COUNT, 0);
    mBizList = (List<DetailModel.BizInfoModel>) bundle.getSerializable(BUNDLE_KEY_BIZ_LIST);
    mTagsList = (List<String>) bundle.getSerializable(BUNDLE_KEY_TAG_LIST);


    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
    rv.setLayoutManager(mLayoutManager);
    if (!NetUtil.isNetworkConnected(getActivity()) && mAdapter == null) {
      errorView.showViewDefault(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          DialogUtil.showProgess(getActivity(), "加载中");
          getData(true, false);
        }
      });
    }
    mSwipeRefreshLayout.setOnRefreshListener(this);


    mLoadingListener = new EndlessRecyclerOnScrollListener(rv.getLayoutManager()) {
      @Override
      public void onLoadMore(int current_page) {
        getData(false, true);
      }
    };
    rv.addOnScrollListener(mLoadingListener);


    errorView.hideView();


    getData();
    return v;
  }


  public void getData() {
    DialogUtil.showProgess(getActivity(), R.string.loading_dialog_text);
    getData(true, false);
  }


  private void getData(final boolean firstLoad, final boolean loadMore) {
    if (firstLoad) {
      mSceneList.clear();
      mMaxPageNo = -1;
      mPageNo = 1;
    }

    if (mMaxPageNo != -1 && mPageNo >= mMaxPageNo && loadMore) {
      mAdapter.showPageEnd(true);
      return;
    }

    if (loadMore) {
      mPageNo += 1;
    }

    if (mActivityStop) {
      return;
    }
    DialogUtil.showProgess(getActivity(), R.string.loading_dialog_text);

    CommentsPresenter.getComments(getActivity(), sceneId, mPageNo, new CommonListener<PagerModel<CommentModel>>() {
      @Override
      public void successListener(PagerModel<CommentModel> response) {
        DialogUtil.dismissProgessDirectly();

        if (mMaxPageNo == -1) {
          mMaxPageNo = response.getPageCount();
        }
        if (null != mAdapter && !loadMore) {
          mAdapter.showPageEnd(false);
        }
        mSceneList.addAll(response.getResult());

        if (firstLoad) {
          mAdapter = new CommentAdapter(getActivity(), mSceneList, mRatingCount, mAvgRating,mRecentRating , mBizList, mTagsList);
          rv.setAdapter(mAdapter);
        } else {
          mAdapter.notifyDataSetChanged();
        }
        mSwipeRefreshLayout.setRefreshing(false);
        mLoadingListener.loadingComplete();


      }

      @Override
      public void errorListener(VolleyError error, String exception, String msg) {
        DialogUtil.dismissProgessDirectly();
        if (!loadMore) {
          errorView.showViewDefault(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              DialogUtil.showProgess(getActivity(), "加载中");
              getData(true, false);
            }
          });
        }
        if (mSwipeRefreshLayout.isRefreshing()) {
          mSwipeRefreshLayout.setRefreshing(false);
        }
        mLoadingListener.loadingComplete();

      }
    });
  }


  @Override
  public void onStart() {
    super.onStart();
    mActivityStop = false;
  }

  @Override
  public void onStop() {
    mActivityStop = true;
    super.onStop();
  }

  @Override
  public void onRefresh() {
    if (NetUtil.isNetworkConnected(getActivity())) {
      getData();
    } else {
      Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
      mSwipeRefreshLayout.setRefreshing(false);
    }
  }
}
