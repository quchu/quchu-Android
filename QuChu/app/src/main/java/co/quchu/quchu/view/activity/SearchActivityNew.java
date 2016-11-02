package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.gallery.utils.Utils;
import co.quchu.quchu.model.ArticleKeyword;
import co.quchu.quchu.model.SearchCategoryBean;
import co.quchu.quchu.model.SearchKeywordModel;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.SearchHistoryPresenter;
import co.quchu.quchu.presenter.SearchPresenter;
import co.quchu.quchu.utils.ScreenUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.adapter.SearchAdapterNew;
import co.quchu.quchu.widget.ReboundRecyclerView;
import co.quchu.quchu.widget.SearchView;
import co.quchu.quchu.widget.TagCloudView;

/**
 * 搜索界面
 * <p>
 * Created by mwb on 16/10/18.
 */
public class SearchActivityNew extends BaseBehaviorActivity {

  @Bind(R.id.search_view) SearchView mSearchView;
  @Bind(R.id.tag_cloud_view) TagCloudView mTagCloudView;
  @Bind(R.id.history_recycler_view) RecyclerView mHistoryRv;
  @Bind(R.id.search_category_recycler_view) ReboundRecyclerView mCategoryRv;

  private SearchAdapterNew mSearchCategoryAdapter;
  private SearchAdapterNew mSearchHistoryAdapter;
  private InputMethodManager mInputMethodManager;
  private EditText mSearchInputEt;
  private List<SearchKeywordModel> mSearchHistoryList;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_new);
    ButterKnife.bind(this);

    initHistory();

    initSearchView();

    initCategory();

    getNetArticleKeyword();
  }

  /**
   * 获取文章关键字
   */
  private void getNetArticleKeyword() {
    SearchPresenter.getNetArticleKeyword(this, new CommonListener<List<ArticleKeyword>>() {
      @Override
      public void successListener(List<ArticleKeyword> response) {
        if (response != null && response.size() > 0) {
          initTags(response);
        }
      }

      @Override
      public void errorListener(VolleyError error, String exception, String msg) {

      }
    });
  }

  /**
   * 编辑框
   */
  private void initSearchView() {
    ImageView backBtn = mSearchView.getSearchBackBtn();
    TextView searchBtn = mSearchView.getSearchBtn();
    mSearchInputEt = mSearchView.getSearchInputEt();

    mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    mSearchInputEt.clearFocus();
    mSearchInputEt.setFocusable(false);
    mSearchInputEt.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        mSearchInputEt.setFocusable(true);
        mSearchInputEt.setFocusableInTouchMode(true);
        mSearchInputEt.requestFocus();
        if (mSearchHistoryList != null && mSearchHistoryList.size() > 0) {
          mCategoryRv.setVisibility(View.GONE);
          mHistoryRv.setVisibility(View.VISIBLE);
        }
        return false;
      }
    });

//    mSearchInputEt.setOnKeyListener(new View.OnKeyListener() {
//      @Override
//      public boolean onKey(View v, int keyCode, KeyEvent event) {
//        //修改回车键功能
//        if (keyCode == KeyEvent.KEYCODE_ENTER) {
//          doSearch();
//        }
//        return false;
//      }
//    });

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
        doSearch();
      }
    });
  }

  /**
   * 开始搜索
   */
  private void doSearch() {
    if (!NetUtil.isNetworkConnected(this)) {
      makeToast(R.string.network_error);
      return;
    }

    String inputStr = mSearchInputEt.getText().toString().trim();

    if (TextUtils.isEmpty(inputStr)) {
      makeToast("请输入搜索内容!");
      return;
    }

    if (StringUtils.containsEmoji(inputStr)) {
      makeToast("搜索内容不能含有表情字符!");
      return;
    }

    //存入数据库
    insertHistory(inputStr);

    SearchResultActivity.launch(SearchActivityNew.this, null, inputStr, -1);
  }

  /**
   * 搜索分类
   */
  private void initCategory() {
    mCategoryRv.setLayoutManager(new GridLayoutManager(this, 3));
    mSearchCategoryAdapter = new SearchAdapterNew();
    mCategoryRv.setAdapter(mSearchCategoryAdapter);
    mSearchCategoryAdapter.setOnSearchItemClickListener(onItemClickListener);

    final ImageView imageView = (ImageView) findViewById(R.id.search_ic_logo);
    ViewTreeObserver imageViewTreeObserver = imageView.getViewTreeObserver();
    if (imageViewTreeObserver.isAlive()) {
      imageViewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

        boolean isFirstIn = true;

        @Override
        public void onGlobalLayout() {
          if (isFirstIn) {
            int top = imageView.getTop();
            int height = imageView.getHeight();
            int screenHeight = ScreenUtils.getScreenHeight(SearchActivityNew.this);

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mCategoryRv.getLayoutParams();
            layoutParams.height = screenHeight - Utils.dip2px(SearchActivityNew.this, 56 + 15) - top - height - height / 2;
            mCategoryRv.setLayoutParams(layoutParams);

            isFirstIn = false;
          }
        }
      });
    }
//    ViewTreeObserver viewTreeObserver = mCategoryRv.getViewTreeObserver();
//    if (viewTreeObserver.isAlive()) {
//      viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//
//        boolean isFirstIn = true;
//
//        @Override
//        public void onGlobalLayout() {
//          if (isFirstIn) {
//            float y = imageView.getY();
//            int top = imageView.getTop();
//
//            Log.e("---mwb", "top = " + top);
//
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mCategoryRv.getLayoutParams();
//            layoutParams.height = top;
//            mCategoryRv.setLayoutParams(layoutParams);
//
//            isFirstIn = false;
//          }
//        }
//      });
//    }

    querySearchCategory();
  }

  /**
   * 搜索历史记录
   */
  private void initHistory() {
    mHistoryRv.setLayoutManager(new LinearLayoutManager(this));
    mSearchHistoryAdapter = new SearchAdapterNew();
    mHistoryRv.setAdapter(mSearchHistoryAdapter);
    mSearchHistoryAdapter.setOnSearchItemClickListener(onItemClickListener);

    queryHistory();
  }

  private SearchAdapterNew.OnSearchItemClickListener onItemClickListener = new SearchAdapterNew.OnSearchItemClickListener() {
    @Override
    public void onClick(int position, Parcelable bean, int itemType) {
      if (itemType == SearchAdapterNew.ITEM_TYPE_CATEGORY) {

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
        SearchCategoryBean categoryBean = (SearchCategoryBean) bean;
        if (categoryBean != null) {

          SearchResultActivity.launch(SearchActivityNew.this, categoryBean, "", position + 1);
        }

      } else if (itemType == SearchAdapterNew.ITEM_TYPE_HISTORY) {
        SearchKeywordModel keywordModel = (SearchKeywordModel) bean;
        if (keywordModel != null) {
          setInputEditText(keywordModel.getKeyword());
        }
      }
    }

    @Override
    public void onDelete(Parcelable bean, int itemType) {
      if (itemType == SearchAdapterNew.ITEM_TYPE_HISTORY) {
        SearchKeywordModel keywordModel = (SearchKeywordModel) bean;
        deleteHistory(keywordModel);
      }
    }
  };

  /**
   * 设置输入框的内容
   */
  private void setInputEditText(String input) {
    mSearchInputEt.setText("");
    mSearchInputEt.setText(input);
    mSearchInputEt.setSelection(input.length());
  }

  /**
   * 获取历史记录
   */
  private void queryHistory() {
    mSearchHistoryList = SearchHistoryPresenter.getHistoryKeywords(this);
    mSearchHistoryAdapter.setHistoryList(mSearchHistoryList);
  }

  private void insertHistory(String history) {
    if (!TextUtils.isEmpty(history)) {
      SearchHistoryPresenter.insertKeyword(this, history);
    }
  }

  private void deleteHistory(SearchKeywordModel keywordModel) {
    if (keywordModel == null) {
      return;
    }

    if (mSearchHistoryList.contains(keywordModel)) {
      mSearchHistoryList.remove(keywordModel);
      mSearchHistoryAdapter.setHistoryList(mSearchHistoryList);
    }

    SearchHistoryPresenter.deleteIfExisted(SearchActivityNew.this, keywordModel.getKeyword());
  }

  /**
   * 获取搜索分类
   */
  private void querySearchCategory() {
    SearchPresenter.getCategoryTag(this, new CommonListener<ArrayList<SearchCategoryBean>>() {
      @Override
      public void successListener(ArrayList<SearchCategoryBean> response) {
        mSearchCategoryAdapter.setCategoryList(response);
      }

      @Override
      public void errorListener(VolleyError error, String exception, String msg) {
      }
    });
  }

  /**
   * 搜索标签
   */
  private void initTags(final List<ArticleKeyword> articleKeywords) {
    final List<String> tags = new ArrayList<>();
    for (ArticleKeyword keyword : articleKeywords) {
      tags.add(keyword.getKeyword());
    }
    mTagCloudView.setTags(tags);
    mTagCloudView.setOnTagClickListener(new TagCloudView.OnTagClickListener() {
      @Override
      public void onTagClick(int position) {
        ArticleKeyword keyword = articleKeywords.get(position);
        ArticleDetailActivity.enterActivity(SearchActivityNew.this, String.valueOf(keyword.getNetArticleId()), keyword.getKeyword(), "");
      }
    });
  }

  @Override
  public void onBackPressed() {
    if (mHistoryRv.getVisibility() == View.VISIBLE) {
      mCategoryRv.setVisibility(View.VISIBLE);
      mHistoryRv.setVisibility(View.GONE);
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

  /**
   * 隐藏软键盘
   */
  protected void hideSoftware() {
//    InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
      if (getCurrentFocus() != null)
        mInputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
  }

  protected void hideSoftware(EditText editText) {
//    InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
  }

  /**
   * 显示软键盘
   */
  protected void showSoftWare(final EditText editText) {
//    final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    editText.postDelayed(new Runnable() {
      @Override
      public void run() {
        mInputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
      }
    }, 200);
  }
}
