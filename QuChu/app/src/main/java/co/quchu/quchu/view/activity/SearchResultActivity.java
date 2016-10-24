package co.quchu.quchu.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.adapter.SearchAdapterNew;
import co.quchu.quchu.widget.DropDownMenu.DropContentView;
import co.quchu.quchu.widget.DropDownMenu.DropDownMenu;
import co.quchu.quchu.widget.EndlessRecyclerOnScrollListener;
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
  @Bind(R.id.drop_down_menu) DropDownMenu mDropDownMenu;
  @Bind(R.id.recycler_view) RecyclerView mSearchResultRv;
  @Bind(R.id.search_no_data_tv) TextView mSearchNoDataTv;

  private EditText mSearchInputEt;
  private SearchAdapterNew mSearchAdapter;

  private int mMaxPageNo = -1;
  private int mCurrentPageNo = 1;
  private boolean mIsLoading = false;

  private List<SearchCategoryBean> mCategoryList;
  private List<AreaBean> mAreaList;
  private List<SearchSortBean> mSortList;

  private String mInputStr = "", mCategoryCode = "", mAreaId = "", mCircleId = "", mSortType = "";
  private String mDefaultValue = "";
  private int mCategoryPosition;

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
    setContentView(R.layout.activity_search_result);
    ButterKnife.bind(this);

    mInputStr = getIntent().getStringExtra(INTENT_KEY_SEARCH_INPUT);
    mCategoryPosition = getIntent().getIntExtra(INTENT_KEY_CATEGORY_POSITION, -1);
    SearchCategoryBean categoryBean = getIntent().getParcelableExtra(INTENT_KEY_SEARCH_CATEGORY_BEAN);
    if (categoryBean != null) {
      mCategoryCode = String.valueOf(categoryBean.getTagId());
      String categoryZh = categoryBean.getZh();

      mInputStr = categoryZh;
    }

    initSearchView();

    initDropDownMenu();

    initRecyclerView();

    queryResult(false);
  }

  private void initRecyclerView() {
    mSearchResultRv.setLayoutManager(new LinearLayoutManager(this));
    mSearchAdapter = new SearchAdapterNew();
    mSearchAdapter.setOnSearchItemClickListener(onItemClickListener);
    mSearchResultRv.setAdapter(mSearchAdapter);
  }

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
    public void onDelete(Parcelable bean, int itemType) {

    }
  };

  private void initSearchView() {
    ImageView backBtn = mSearchView.getSearchBackBtn();
    TextView searchBtn = mSearchView.getSearchBtn();
    mSearchInputEt = mSearchView.getSearchInputEt();

    mSearchInputEt.clearFocus();
    mSearchInputEt.setFocusable(false);
    mSearchInputEt.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        mSearchInputEt.setFocusable(true);
        mSearchInputEt.setFocusableInTouchMode(true);
        mSearchInputEt.requestFocus();
        return false;
      }
    });

    mSearchInputEt.setOnKeyListener(new View.OnKeyListener() {
      @Override
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        //修改回车键功能
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
          doSearch(false);
        }
        return false;
      }
    });

    setInputEditText(mInputStr);

    //返回
    backBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    //搜索
    searchBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        doSearch(false);
      }
    });
  }

  /**
   * 设置输入框的内容
   */
  private void setInputEditText(String input) {
    mSearchInputEt.setText("");
    mSearchInputEt.setText(input);
    mSearchInputEt.setSelection(input.length());
  }

  /**
   * 搜索
   */
  private void doSearch(boolean loadMore) {
    String inputStr = mSearchInputEt.getText().toString().trim();

    if (TextUtils.isEmpty(inputStr)) {
      makeToast("请输入搜索内容!");
      return;
    }

    if (StringUtils.containsEmoji(mInputStr)) {
      makeToast("搜索内容不能含有表情字符!");
      return;
    }

    if (!inputStr.equals(mInputStr)) {
      mInputStr = inputStr;
    }

    queryResult(loadMore);
  }

  /**
   * 查询搜索结果
   */
  private void queryResult(boolean loadMore) {
    if (mIsLoading) return;

    if (loadMore && mCurrentPageNo >= mMaxPageNo && mMaxPageNo != -1) return;

    if (!loadMore) {
      mCurrentPageNo = 1;

    } else if (mCurrentPageNo < mMaxPageNo) {
      mCurrentPageNo += 1;
    }

    mIsLoading = true;
    if (NetUtil.isNetworkConnected(getApplicationContext())) {
      DialogUtil.showProgess(this, R.string.loading_dialog_text);
    }

    SearchPresenter.searchFromService(this, mCurrentPageNo, mInputStr, mCategoryCode, mAreaId, mCircleId, mSortType,
        new SearchPresenter.SearchResultListener() {
          @Override
          public void successResult(List<RecommendModel> arrayList, int maxPageNo) {
            mSearchResultRv.clearOnScrollListeners();

            if (arrayList != null && arrayList.size() > 0) {
              mSearchNoDataTv.setVisibility(View.GONE);

              if (mMaxPageNo == -1) {
                mMaxPageNo = maxPageNo;
              }

              mSearchAdapter.setResultList(arrayList);

              mSearchResultRv.addOnScrollListener(
                  new EndlessRecyclerOnScrollListener(mSearchResultRv.getLayoutManager()) {
                    @Override
                    public void onLoadMore(int current_page) {
                      queryResult(true);
                    }
                  });

            } else {
              //无数据
              mSearchNoDataTv.setVisibility(View.VISIBLE);
            }

            mIsLoading = false;
            DialogUtil.dismissProgess();
          }

          @Override
          public void errorNull() {
            mIsLoading = false;
            DialogUtil.dismissProgess();

            mSearchNoDataTv.setVisibility(View.VISIBLE);
          }
        });
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
        mDropDownMenu.setDropCategory(mCategoryPosition, mCategoryList);
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
        mDropDownMenu.setDropArea(mAreaList);
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
        mDropDownMenu.setDropSort(mSortList);
      }

      @Override
      public void errorListener(VolleyError error, String exception, String msg) {

      }
    });
  }

  private void initDropDownMenu() {
    List<String> tabs = new ArrayList<>();
    tabs.add("全部");
    tabs.add("全部商圈");
    tabs.add("智能排序");
    mDropDownMenu.addTab(tabs);

    mDropDownMenu.setOnDropTabClickListener(new DropDownMenu.OnDropTabClickListener() {
      @Override
      public void onTabSelected(int tabPosition) {
        switch (tabPosition) {
          case 0:
            if (mCategoryList != null && mCategoryList.size() > 0) {
              mDropDownMenu.setDropCategory(mCategoryPosition, mCategoryList);
            } else {
              queryGroupTags();
            }
            break;

          case 2:
            if (mAreaList != null && mAreaList.size() > 0) {
              mDropDownMenu.setDropArea(mAreaList);
            } else {
              queryAreaList();
            }
            break;

          case 4:
            if (mSortList != null && mSortList.size() > 0) {
              mDropDownMenu.setDropSort(mSortList);
            } else {
              querySortTypeList();
            }
            break;
        }
      }

      @Override
      public void onItemSelected(DropContentView.DropBean parent, DropContentView.DropBean child) {
        if (parent != null) {
          if (parent.getType() == DropContentView.DropBean.DATA_TYPE_CATEGORY) {
            if (parent.getText().substring(0, 2).equals("全部")) {
              mInputStr = "";
            } else {
              mInputStr = parent.getText();
            }
            mDropDownMenu.setTabText(parent.getText());
            mDefaultValue = parent.getText();

          } else if (parent.getType() == DropContentView.DropBean.DATA_TYPE_AREA) {
            if (parent.getText().substring(0, 2).equals("全部")) {
              mAreaId = "";
            } else {
              mAreaId = parent.getId();
            }
            mDefaultValue = parent.getText();

          } else if (parent.getType() == DropContentView.DropBean.DATA_TYPE_SORT) {
            mSortType = parent.getId();

            mDropDownMenu.closeMenu();
            queryResult(false);
          }

        } else if (child != null) {
          if (child.getType() == DropContentView.DropBean.DATA_TYPE_CATEGORY) {
            if (child.getText().substring(0, 2).equals("全部")) {
              mInputStr = mDefaultValue;
              mCategoryCode = "";
              setInputEditText(mDefaultValue);
            } else {
              setInputEditText(child.getText());
              mInputStr = child.getText();
              mCategoryCode = child.getId();
            }

          } else if (child.getType() == DropContentView.DropBean.DATA_TYPE_AREA) {
            if (child.getText().substring(0, 2).equals("全部")) {
              setInputEditText(mDefaultValue);
              mCircleId = "";

              mDropDownMenu.setTabText(mDefaultValue);

            } else {
              setInputEditText(child.getText());
              mCircleId = child.getId();
              mDropDownMenu.setTabText(child.getText());
            }
          }

          mDropDownMenu.closeMenu();
          queryResult(false);
        }
      }
    });
  }

  @Override
  public void onBackPressed() {
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
    return 0;
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
