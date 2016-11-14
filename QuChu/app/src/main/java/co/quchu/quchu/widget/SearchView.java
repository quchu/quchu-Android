package co.quchu.quchu.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.model.SearchKeywordModel;
import co.quchu.quchu.presenter.SearchHistoryPresenter;
import co.quchu.quchu.utils.SoftInputUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.utils.ToastManager;
import co.quchu.quchu.view.adapter.SearchAdapterNew;

/**
 * Created by mwb on 16/10/20.
 */
public class SearchView extends LinearLayout {

  @Bind(R.id.search_back_btn) ImageView mSearchBackBtn;
  @Bind(R.id.search_input_et) EditText mSearchInputEt;
  @Bind(R.id.search_btn) TextView mSearchBtn;
  @Bind(R.id.search_history_rv) RecyclerView mSearchHistoryRv;

  private SearchAdapterNew mSearchHistoryAdapter;
  private List<String> mSearchHistoryList = new ArrayList<>();
  private String finalInputStr = "";

  public SearchView(Context context, AttributeSet attrs) {
    super(context, attrs);

    LayoutInflater.from(context).inflate(R.layout.view_search, this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);

    mSearchInputEt.clearFocus();
    mSearchInputEt.setFocusable(false);
    mSearchInputEt.setOnTouchListener(new OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        mSearchInputEt.setFocusable(true);
        mSearchInputEt.setFocusableInTouchMode(true);
        mSearchInputEt.requestFocus();

        if (mListener != null) {
          mListener.onTouchEditText();
        }
        return false;
      }
    });

    mSearchInputEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
          clickSearch();
          return true;
        }
        return false;
      }
    });

    initHistory();
  }

  public EditText getSearchInputEt() {
    return mSearchInputEt;
  }

  public TextView getSearchBtn() {
    return mSearchBtn;
  }

  public ImageView getSearchBackBtn() {
    return mSearchBackBtn;
  }

  /**
   * 设置输入框的内容
   */
  public void setInputEditText(String input) {
    mSearchInputEt.setText("");
    mSearchInputEt.setText(input);
    mSearchInputEt.setSelection(input.length());
  }

  public void showHistory() {
    if (mSearchHistoryList != null && mSearchHistoryList.size() > 0) {
      mSearchHistoryRv.setVisibility(VISIBLE);
    }
  }

  public void hideHistory() {
    if (mSearchHistoryRv.getVisibility() == VISIBLE) {
      mSearchHistoryRv.setVisibility(GONE);
    }
  }

  public boolean isShowHistory() {
    return mSearchHistoryRv.getVisibility() == VISIBLE;
  }

  /**
   * 初始化搜索历史列表
   */
  private void initHistory() {
    mSearchHistoryRv.setLayoutManager(new LinearLayoutManager(getContext()));
    mSearchHistoryAdapter = new SearchAdapterNew();
    mSearchHistoryRv.setAdapter(mSearchHistoryAdapter);
    mSearchHistoryAdapter.setOnSearchItemClickListener(new SearchAdapterNew.OnSearchItemClickListener() {

      @Override
      public void onClick(int position, Parcelable bean, int itemType) {
      }

      @Override
      public void onClickHistory(String keyword) {
        setInputEditText(keyword);

        hideHistory();

        SoftInputUtils.hideSoftInput((Activity) getContext());

        if (mListener != null) {
          mListener.onClickHistory(keyword);
        }
      }

      @Override
      public void onDeleteHistory(String keyword) {
        deleteHistory(keyword);
      }
    });

    queryHistory();
  }

  /**
   * 查询历史记录
   */
  private void queryHistory() {
    List<SearchKeywordModel> keywords = SearchHistoryPresenter.getHistoryKeywords(getContext());
    if (keywords != null && keywords.size() > 0) {
      mSearchHistoryList.clear();
      for (SearchKeywordModel keyword : keywords) {
        mSearchHistoryList.add(keyword.getKeyword());
      }
    }

    mSearchHistoryAdapter.setHistoryList(mSearchHistoryList);
    mSearchHistoryAdapter.notifyDataSetChanged();
  }

  /**
   * 插入历史记录(单条)
   */
  public void insertHistory(String keyword) {
    if (TextUtils.isEmpty(keyword)) {
      return;
    }

    if (mSearchHistoryList.contains(keyword)) {
      return;
    }

    mSearchHistoryList.add(0, keyword);
    mSearchHistoryAdapter.notifyDataSetChanged();

    SearchHistoryPresenter.insertKeyword(getContext(), keyword);
  }

  /**
   * 删除历史记录(单条)
   */
  public void deleteHistory(String keyword) {
    if (TextUtils.isEmpty(keyword)) {
      return;
    }

    if (mSearchHistoryList.contains(keyword)) {
      mSearchHistoryList.remove(keyword);
      mSearchHistoryAdapter.notifyDataSetChanged();
    }

    if (mSearchHistoryList.size() == 0) {
      mSearchHistoryRv.setVisibility(GONE);
    }

    SearchHistoryPresenter.deleteIfExisted(getContext(), keyword);
  }

  @OnClick({R.id.search_back_btn, R.id.search_input_et, R.id.search_btn})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.search_back_btn:
        if (mListener != null) {
          mListener.onClickBack();
        }
        break;

      case R.id.search_input_et:
        break;

      case R.id.search_btn:
        clickSearch();
        break;
    }
  }

  private void clickSearch() {
    String inputStr = mSearchInputEt.getText().toString().trim();

    if (TextUtils.isEmpty(inputStr)) {
      ToastManager.getInstance(getContext()).show("请输入搜索内容!");
      return;
    }

    if (StringUtils.containsEmoji(inputStr)) {
      ToastManager.getInstance(getContext()).show("搜索内容不能含有表情字符!");
      return;
    }

    if (isShowHistory()) {
      hideHistory();
    }

    insertHistory(inputStr);

    if (mListener != null) {
      mListener.onClickSearch(inputStr);
    }
  }

  private OnSearchViewClickListener mListener;

  public void setOnSearchViewClickListener(OnSearchViewClickListener listener) {
    mListener = listener;
  }

  public interface OnSearchViewClickListener {
    void onClickBack();

    void onTouchEditText();

    void onClickSearch(String inputStr);

    void onClickHistory(String history);
  }
}
