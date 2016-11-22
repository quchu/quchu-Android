package co.quchu.quchu.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.AreaBean;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.model.SearchCategoryBean;
import co.quchu.quchu.model.SearchSortBean;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.SearchPresenter;
import co.quchu.quchu.utils.SoftInputUtils;
import co.quchu.quchu.view.adapter.SearchAdapterNew;
import co.quchu.quchu.widget.DropDownMenu.MDropBean;
import co.quchu.quchu.widget.DropDownMenu.MDropDownMenu;
import co.quchu.quchu.widget.SearchView;

/**
 * 搜索结果展示
 * <p>
 * Created by mwb on 16/10/20.
 */
public class SearchResultActivity extends BaseBehaviorActivity {

  private static final String INTENT_KEY_SEARCH_CATEGORY_BEAN = "intent_key_search_category_bean";
  private static final String INTENT_KEY_SEARCH_INPUT = "intent_key_search_input";
  private static final String INTENT_KEY_CATEGORY_POSITION = "intent_key_category_position";

  @Bind(R.id.search_view) SearchView mSearchView;
  @Bind(R.id.recycler_view) RecyclerView mSearchResultRv;
  @Bind(R.id.search_no_data_tv) TextView mSearchNoDataTv;
  @Bind(R.id.search_refresh_layout) SwipeRefreshLayout mSearchRefreshLayout;
  @Bind(R.id.search_drop_down_menu) MDropDownMenu mDropDownMenu;

  private SearchAdapterNew mSearchAdapter;

  private int mCurrentPageNo = 1;
  private boolean mIsLoading = false;
  private boolean mHasMoreData;//有更多数据
  private boolean mIsPullDownRefresh;//下拉刷新

  private List<SearchCategoryBean> mCategoryList;
  private List<AreaBean> mAreaList;
  private List<SearchSortBean> mSortList;

  private String mCategoryZh = "", mInputStr = "", mTagId = "", mAreaId = "", mCircleId = "", mSortType = "";
  private int mCategoryPosition = 0;

  public static void launch(Activity activity, SearchCategoryBean categoryBean, String input, int position) {
    Intent intent = new Intent(activity, SearchResultActivity.class);
    intent.putExtra(INTENT_KEY_SEARCH_CATEGORY_BEAN, categoryBean);
    intent.putExtra(INTENT_KEY_SEARCH_INPUT, input);
    intent.putExtra(INTENT_KEY_CATEGORY_POSITION, position);
    activity.startActivity(intent);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_result_new);
    ButterKnife.bind(this);

    mInputStr = getIntent().getStringExtra(INTENT_KEY_SEARCH_INPUT);
    mCategoryPosition = getIntent().getIntExtra(INTENT_KEY_CATEGORY_POSITION, -1);
    SearchCategoryBean categoryBean = getIntent().getParcelableExtra(INTENT_KEY_SEARCH_CATEGORY_BEAN);
    if (categoryBean != null) {
      mTagId = String.valueOf(categoryBean.getTagId());
      mCategoryZh = categoryBean.getZh();
    }

    initRefreshLayout();

    initDropDownMenu();

    initSearchView();

    initRecyclerView();

    queryResult(false);
  }

  @Override
  protected void onResume() {
    super.onResume();

    mSearchView.queryHistory();
  }

  @Override
  protected void onStop() {
    mSearchView.setInputEditText("");

    super.onStop();
  }

  private void initRefreshLayout() {
    mSearchRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        mIsPullDownRefresh = true;
        queryResult(false);
      }
    });
  }

  private void initRecyclerView() {
    mSearchResultRv.setLayoutManager(new LinearLayoutManager(this));
    mSearchAdapter = new SearchAdapterNew();
    mSearchResultRv.setAdapter(mSearchAdapter);
    mSearchAdapter.setOnSearchItemClickListener(onItemClickListener);
    mSearchResultRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        //判断是否在顶部 false-顶部
        boolean canScrollDown = recyclerView.canScrollVertically(-1);
        //判断是否在底部 false-底部
        boolean canScrollUp = recyclerView.canScrollVertically(1);

        if (!canScrollDown) {
          //已经在顶部,可以下拉刷新
          mSearchRefreshLayout.setEnabled(true);
        } else {
          //不能下拉刷新
          mSearchRefreshLayout.setEnabled(false);
        }

        if (!canScrollUp && mHasMoreData) {
          //已经在底部,可以上拉加载更多
          queryResult(true);
        }
      }
    });

    mSearchResultRv.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        SoftInputUtils.hideSoftInput(SearchResultActivity.this);

        mSearchView.hideHistory();
        return false;
      }
    });
  }

  /**
   * 结果列表点击监听
   */
  private SearchAdapterNew.OnSearchItemClickListener onItemClickListener = new SearchAdapterNew.OnSearchItemClickListener() {
    @Override
    public void onClick(int position, Parcelable bean, int itemType) {
      if (itemType == SearchAdapterNew.ITEM_TYPE_RESULT) {
        RecommendModel model = (RecommendModel) bean;

        ArrayMap<String, Object> params = new ArrayMap<>();
        params.put("趣处名称", model.getName());
        params.put("入口名称", getPageNameCN());
        ZGEvent(params, "进入趣处详情页");

        Intent intent = new Intent(getApplicationContext(), QuchuDetailsActivity.class);
        intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, model.getPid());
        startActivity(intent);
      }
    }

    @Override
    public void onClickHistory(String keyword) {
      //空实现
    }

    @Override
    public void onDeleteHistory(String keyword) {
      //空实现
    }
  };

  private void initSearchView() {
    mSearchView.setOnSearchViewClickListener(new SearchView.OnSearchViewClickListener() {
      @Override
      public void onClickBack() {
        onBackPressed();
      }

      @Override
      public void onTouchEditText() {
        if (mDropDownMenu != null && mDropDownMenu.isShowing()) {
          mDropDownMenu.closeMenu();
        }

        mSearchView.showHistory();
      }

      @Override
      public void onClickSearch(String inputStr) {
        resetSearch();

        doSearch(false, inputStr);
      }

      @Override
      public void onClickHistory(String history) {
        resetSearch();

        doSearch(false, history);
      }
    });

    setInputEditText(TextUtils.isEmpty(mCategoryZh) ? mInputStr : mCategoryZh);
  }

  private void resetSearch() {
    mTagId = "";
    mAreaId = "";
    mCircleId = "";
    mSortType = "";
    mDropDownMenu.reset();
  }

  /**
   * 设置输入框的内容
   */
  private void setInputEditText(String input) {
    mSearchView.setInputEditText(input);
  }

  /**
   * 搜索
   */
  private void doSearch(boolean isLoadMore, String inputStr) {
    if (!inputStr.equals(mInputStr)) {
      mInputStr = inputStr;
    }

    if (mDropDownMenu != null && mDropDownMenu.isShowing()) {
      mDropDownMenu.closeMenu();
    }

    queryResult(isLoadMore);
  }

  /**
   * 查询搜索结果
   */
  private void queryResult(final boolean isLoadMore) {
    if (!NetUtil.isNetworkConnected(this)) {
      makeToast(R.string.network_error);
      return;
    }

    if (mIsLoading) return;

    if (!mIsPullDownRefresh && NetUtil.isNetworkConnected(this)) {
      DialogUtil.showProgess(this, R.string.loading_dialog_text);
      mIsPullDownRefresh = false;
    }
    mIsLoading = true;

    if (isLoadMore) {
      mCurrentPageNo += 1;
    } else {
      mCurrentPageNo = 1;
    }

    SearchPresenter.searchFromService(this, mCurrentPageNo, mInputStr, mTagId, mAreaId, mCircleId, mSortType, new SearchPresenter.SearchResultListener() {

      @Override
      public void onSuccess(List<RecommendModel> data, int pageNo, int pageCount, int resultCount) {
        mIsLoading = false;
        mSearchNoDataTv.setVisibility(View.GONE);
        DialogUtil.dismissProgess();

        if (mSearchRefreshLayout.isRefreshing()) {
          mSearchRefreshLayout.setRefreshing(false);
        }

        //判断是否存在分页
        if (pageNo < pageCount) {
          mHasMoreData = true;
        } else {
          mHasMoreData = false;
        }

        if (pageNo == 1) {
          mSearchAdapter.initResultList(data);
        } else {
          mSearchAdapter.addMoreResultList(data);
        }

        mSearchAdapter.setHasMoreData(mHasMoreData);

        if (!isLoadMore) {
          mSearchResultRv.smoothScrollToPosition(0);
        }

        validateData(data);
      }

      @Override
      public void onError() {
        mIsLoading = false;
        DialogUtil.dismissProgess();

        if (mSearchRefreshLayout.isRefreshing()) {
          mSearchRefreshLayout.setRefreshing(false);
        }

        validateData(null);
      }
    });
  }

  /**
   * 检查服务器是否有返回数据
   */
  private void validateData(List<RecommendModel> data) {
    if (data == null || (data != null && data.size() == 0)) {
      mSearchNoDataTv.setVisibility(View.VISIBLE);
      mSearchResultRv.setVisibility(View.GONE);
      mSearchRefreshLayout.setEnabled(false);

    } else {
      mSearchNoDataTv.setVisibility(View.GONE);
      mSearchResultRv.setVisibility(View.VISIBLE);
      mSearchRefreshLayout.setEnabled(true);
    }
  }

  /**
   * 查询分组
   */
  private void queryGroupTags() {
    SearchPresenter.getGroupTags(this, new CommonListener<List<SearchCategoryBean>>() {
      @Override
      public void successListener(List<SearchCategoryBean> response) {
        if (response == null || response.size() == 0) {
          return;
        }

        mCategoryList = response;
      }

      @Override
      public void errorListener(VolleyError error, String exception, String msg) {

      }
    });
  }

  /**
   * 查询地区
   */
  private void queryAreaList() {
    SearchPresenter.getAreaList(this, new CommonListener<ArrayList<AreaBean>>() {
      @Override
      public void successListener(ArrayList<AreaBean> response) {
        if (response == null || response.size() == 0) {
          return;
        }

        mAreaList = response;
      }

      @Override
      public void errorListener(VolleyError error, String exception, String msg) {

      }
    });
  }

  /**
   * 查询排序
   */
  private void querySortTypeList() {
    SearchPresenter.getSortTypeList(this, new CommonListener<ArrayList<SearchSortBean>>() {
      @Override
      public void successListener(ArrayList<SearchSortBean> response) {
        if (response == null || response.size() == 0) {
          return;
        }

        mSortList = response;
      }

      @Override
      public void errorListener(VolleyError error, String exception, String msg) {

      }
    });
  }

  private void initDropDownMenu() {
    if (!TextUtils.isEmpty(mCategoryZh)) {
      mDropDownMenu.setTabText(0, mCategoryZh);
    }

    queryGroupTags();
    queryAreaList();
    querySortTypeList();

    mDropDownMenu.setOnDropDownMenuClickListener(new MDropDownMenu.OnDropDownMenuClickListener() {
      @Override
      public void onTabSelected(int tabIndex) {
        switch (tabIndex) {
          case 0:
            mDropDownMenu.setDropCategory(mCategoryPosition, mCategoryList);
            break;

          case 1:
            mDropDownMenu.setDropArea(mAreaList);
            break;

          case 2:
            mDropDownMenu.setDropSort(mSortList);
            break;
        }
      }

      @Override
      public void onItemSelected(MDropBean parent, MDropBean child) {
        setSearchParams(parent, child);
      }
    });
  }

  /**
   * 设置搜索参数
   *
   * @param parent selected parent
   * @param child  selected child
   */
  private void setSearchParams(MDropBean parent, MDropBean child) {
    if (parent != null) {
      if (parent.getType() == MDropBean.DATA_TYPE_CATEGORY) {
        mInputStr = "";

      } else if (parent.getType() == MDropBean.DATA_TYPE_AREA) {
        if (parent.getText().substring(0, 2).equals("全部")) {
          mAreaId = "";
        } else {
          mAreaId = parent.getId();
        }

      } else if (parent.getType() == MDropBean.DATA_TYPE_SORT) {
        mSortType = parent.getId();
        mDropDownMenu.setTabText(parent.getText());
        mDropDownMenu.closeMenu();
        queryResult(false);
      }

    } else if (child != null) {
      if (child.getType() == MDropBean.DATA_TYPE_CATEGORY) {

        mInputStr = "";

        String selectedParentValue = mDropDownMenu.getSelectedParentValue(MDropBean.DATA_TYPE_CATEGORY);
        if (child.getText().substring(0, 2).equals("全部")) {
          String selectedParentId = mDropDownMenu.getSelectedParentId(MDropBean.DATA_TYPE_CATEGORY);
          mTagId = selectedParentId;
          setInputEditText(selectedParentValue);
          mDropDownMenu.setTabText(selectedParentValue);
        } else {
          mTagId = child.getId();
          setInputEditText(child.getText());
        }
        mDropDownMenu.setTabText(0, selectedParentValue);

      } else if (child.getType() == MDropBean.DATA_TYPE_AREA) {

        if (child.getText().substring(0, 2).equals("全部")) {
          String selectedParentValue = mDropDownMenu.getSelectedParentValue(MDropBean.DATA_TYPE_AREA);
          String selectedParentId = mDropDownMenu.getSelectedParentId(MDropBean.DATA_TYPE_AREA);
          mAreaId = selectedParentId;
          mCircleId = "";
          mDropDownMenu.setTabText(selectedParentValue);
          setInputEditText(selectedParentValue);

        } else {
          mCircleId = child.getId();
          mDropDownMenu.setTabText(child.getText());
          setInputEditText(child.getText());
        }
      }

      mDropDownMenu.closeMenu();
      queryResult(false);
    }
  }

  @Override
  public void onBackPressed() {
    SoftInputUtils.hideSoftInput(this);

    if (mSearchView.isShowHistory()) {
      mSearchView.hideHistory();
      return;
    }

    if (mDropDownMenu.isShowing()) {
      mDropDownMenu.closeMenu();
      return;
    }

    super.onBackPressed();
  }

  @Override
  public ArrayMap<String, Object> getUserBehaviorArguments() {
    return null;
  }

  @Override
  public int getUserBehaviorPageId() {
    return 114;
  }

  @Override
  protected int activitySetup() {
    return TRANSITION_TYPE_LEFT;
  }

  @Override
  protected String getPageNameCN() {
    return null;
  }
}
