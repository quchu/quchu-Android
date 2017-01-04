package co.quchu.quchu.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.model.SceneInfoModel;
import co.quchu.quchu.model.SearchCategoryBean;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.RecommendPresenter;
import co.quchu.quchu.presenter.SearchPresenter;
import co.quchu.quchu.utils.SoftInputUtils;
import co.quchu.quchu.view.adapter.SearchAdapter;
import co.quchu.quchu.widget.SearchView;

/**
 * 搜索界面
 * <p>
 * Created by mwb on 16/10/18.
 */
public class SearchActivity extends BaseBehaviorActivity {

  @Bind(R.id.search_view) SearchView mSearchView;
  @Bind(R.id.search_category_grid_view) RecyclerView mCategoryGridView;
  @Bind(R.id.tag_refresh_btn) TextView mTagRefreshBtn;
  @Bind(R.id.tags_recycler_view) RecyclerView mTagRecyclerView;

  private static String INTENT_KEY_ALL_SCENE_LIST = "intent_key_all_scene_list";

  private SearchAdapter mCategoryAdapter;

  private ArrayList<SearchCategoryBean> mSearchCategoryList = new ArrayList<>();
  private List<SceneInfoModel> mAllSceneList;
  private SearchAdapter mTagAdapter;

  public static void launch(Activity activity, List<SceneInfoModel> allSceneList) {
    Intent intent = new Intent(activity, SearchActivity.class);
    intent.putExtra(INTENT_KEY_ALL_SCENE_LIST, (Serializable) allSceneList);
    activity.startActivity(intent);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);
    ButterKnife.bind(this);

    mAllSceneList = (List<SceneInfoModel>) getIntent().getSerializableExtra(INTENT_KEY_ALL_SCENE_LIST);

    initTagView();

    if (mAllSceneList == null) {
      getSceneList();
    }

    fillTags();

    initSearchView();

    initCategory();

//    getNetArticleKeyword();
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

  /**
   * 获得场景列表
   */
  private void getSceneList() {
    RecommendPresenter.getSceneList(this, new CommonListener<List<SceneInfoModel>>() {
      @Override
      public void successListener(List<SceneInfoModel> response) {
        if (null != mAllSceneList) {
          mAllSceneList.clear();
          mAllSceneList.addAll(response);
        } else {
          mAllSceneList = new ArrayList<>();
          mAllSceneList.addAll(response);
        }

        fillTags();
      }

      @Override
      public void errorListener(VolleyError error, String exception, String msg) {
      }
    });
  }

  private void initTagView() {
    GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
    mTagRecyclerView.setLayoutManager(layoutManager);
    mTagAdapter = new SearchAdapter();
    mTagRecyclerView.setAdapter(mTagAdapter);
  }

  private void fillTags() {
    mTagAdapter.setTags(mAllSceneList);
    if (mAllSceneList != null && mAllSceneList.size() > 0) {
      mTagRefreshBtn.setVisibility(View.VISIBLE);
    } else {
      mTagRefreshBtn.setVisibility(View.GONE);
    }
  }

  /**
   * 对场景列表随机排序
   */
  private void shuffleTagList() {
    if (mAllSceneList != null && mAllSceneList.size() > 3) {
      Collections.shuffle(mAllSceneList);
    }

    fillTags();
  }

  /**
   * 编辑框
   */
  private void initSearchView() {
    mSearchView.setOnSearchViewClickListener(new SearchView.OnSearchViewClickListener() {
      @Override
      public void onClickBack() {
        onBackPressed();
      }

      @Override
      public void onTouchEditText() {
        mSearchView.showHistory();
      }

      @Override
      public void onClickSearch(String inputStr) {
        doSearch(inputStr);
      }

      @Override
      public void onClickHistory(String history) {
        doSearch(history);
      }
    });
  }

  /**
   * 开始搜索
   */
  private void doSearch(String inputStr) {
    if (!NetUtil.isNetworkConnected(this)) {
      makeToast(R.string.network_error);
      return;
    }

    SearchResultActivity.launch(SearchActivity.this, null, inputStr, -1);
  }

  /**
   * 搜索分类
   */
  private void initCategory() {
    mCategoryAdapter = new SearchAdapter();
    mCategoryGridView.setAdapter(mCategoryAdapter);
    mCategoryGridView.setLayoutManager(new GridLayoutManager(this, 3));
    mCategoryAdapter.setOnSearchItemClickListener(new SearchAdapter.OnSearchItemClickListener() {
      @Override
      public void onClick(int position, Parcelable bean, int itemType) {
        if (mSearchCategoryList == null) {
          return;
        }

        if (mSearchView.isShowHistory()) {
          mSearchView.hideHistory();
        }

        SoftInputUtils.hideSoftInput(SearchActivity.this);

        SearchCategoryBean categoryBean = mSearchCategoryList.get(position);
        if (categoryBean == null) {
          return;
        }

        //统计
        switch (position) {
          case 0:
            UMEvent("food_c");
            break;
          case 1:
            UMEvent("hotel_c");
            break;
          case 2:
            UMEvent("entertainment_c");
            break;
          case 3:
            UMEvent("relaxation_c");
            break;
          case 4:
            UMEvent("shopping_c");
            break;
          case 5:
            UMEvent("event_c");
            break;
        }

        //启动搜索结果界面
        SearchResultActivity.launch(SearchActivity.this, categoryBean, "", position + 1);
      }

      @Override
      public void onClickHistory(String keyword) {

      }

      @Override
      public void onDeleteHistory(String keyword) {

      }
    });

    mCategoryGridView.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (mSearchView.isShowHistory()) {
          mSearchView.hideHistory();
        }
        return false;
      }
    });

    querySearchCategory();
  }

  /**
   * 获取搜索分类
   */
  private void querySearchCategory() {
    SearchPresenter.getCategoryTag(this, new CommonListener<ArrayList<SearchCategoryBean>>() {
      @Override
      public void successListener(ArrayList<SearchCategoryBean> response) {

        mSearchCategoryList.clear();
        mSearchCategoryList.addAll(response);
        mCategoryAdapter.setCategoryList(mSearchCategoryList);
      }

      @Override
      public void errorListener(VolleyError error, String exception, String msg) {
      }
    });
  }

  @Override
  public void onBackPressed() {
    SoftInputUtils.hideSoftInput(this);

    if (mSearchView.isShowHistory()) {
      mSearchView.hideHistory();
      return;
    }

    super.onBackPressed();
  }

  @OnClick(R.id.tag_refresh_btn)
  public void onClick() {
    shuffleTagList();
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
