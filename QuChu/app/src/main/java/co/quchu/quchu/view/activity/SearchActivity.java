package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.model.SearchModel;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.SearchPresenter;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.SearchHistoryUtil;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.adapter.SearchAdapter;
import co.quchu.quchu.view.adapter.SearchHistoryAdapter;
import co.quchu.quchu.widget.VerTextView;

/**
 * SearchActivity
 * User: Chenhs
 * Date: 2015-12-04
 */
public class SearchActivity extends BaseActivity implements SearchHistoryAdapter.SearchHItemClickListener {
    @Bind(R.id.search_input_et)
    EditText searchInputEt;
    @Bind(R.id.search_button_rl)
    RelativeLayout searchButtonRl;
    @Bind(R.id.search_title_bar_ll)
    LinearLayout searchTitleBarLl;
    @Bind(R.id.search_history_rv)
    RecyclerView searchHistoryRv;
    /*  InnerListView searchHistoryRv;*/
    @Bind(R.id.search_history_clear_rl)
    RelativeLayout searchHistoryClearRl;
    @Bind(R.id.search_history_fl)
    FrameLayout searchHistoryFl;
    @Bind(R.id.search_result_rv)
    RecyclerView searchResultRv;
    @Bind(R.id.search_result_fl)
    FrameLayout searchResultFl;
    @Bind(R.id.search_result_isnull_vtv)
    VerTextView searchResultIsnullVtv;

    SearchModel searchModel;
    @Bind(R.id.search_history_hint_fl)
    TextView searchHistoryHintFl;

    private SearchHistoryAdapter historyAdapter;
    LinearLayoutManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initHistory();
        initEdittext();
        initData();
    }


    private void showNoneResultFrame() {
        searchResultIsnullVtv.setVisibility(View.VISIBLE);
        searchResultIsnullVtv.setFontSize(60);             // 设定字体尺寸
        searchResultIsnullVtv.setIsOpenUnderLine(false);     // 设定开启下划线
        /*searchResultIsnullVtv.setUnderLineColor(Color.RED); // 设定下划线颜色
        searchResultIsnullVtv.setUnderLineWidth(3);         // 设定下划线宽度*/
        searchResultIsnullVtv.setUnderLineSpacing(30);      // 设定下划线到字的间距
        searchResultIsnullVtv.setTextStartAlign(VerTextView.RIGHT); // 从右侧或左侧开始排版
        searchResultIsnullVtv.setTextColor(getResources().getColor(R.color.load_progress_gray));           // 设定字体颜色
        searchResultIsnullVtv.setText("啦啦：啦啊 \n呼呼 \n哈");
    }

    private void initHistory() {

        searchModel = SearchHistoryUtil.getSearchHistory();
      /*  if (searchModel == null) {
            searchModel = new SearchModel();
            ArrayList<SearchModel.SearchListEntity> hlist = new ArrayList<SearchModel.SearchListEntity>();
            SearchModel.SearchListEntity entity1;
            for (int i = 0; i < 10; i++) {
                entity1 = new SearchModel.SearchListEntity();
                entity1.setSerachStr("来拉" + i);
                hlist.add(entity1);
            }

            searchModel.setSearchList(hlist);
        }*/
        if (searchModel != null && searchModel.getSearchList().size() > 0) {
            showHistory();
        } else {
            showNoneHistory();
        }
    }


    @OnClick({R.id.search_button_rl, R.id.search_history_clear_rl})
    public void buttonClick(View view) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
        switch (view.getId()) {
            case R.id.search_button_rl:
                this.finish();
                break;
            case R.id.search_history_clear_rl:
                searchModel.removeAllSearchHistory();
                historyAdapter.notifyDataSetChanged();
                showNoneHistory();
                break;
        }
    }

    private void seachStr(String str, int pageNo) {
        if (NetUtil.isNetworkConnected(this))
            DialogUtil.showProgess(this, R.string.loading_dialog_text);
        SearchPresenter.searchFromService(this, str, pageNo, new SearchPresenter.SearchResultListener() {
            @Override
            public void successResult(ArrayList<RecommendModel> arrayList) {
                if (arrayList != null && arrayList.size() > 0) {
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    SearchActivity.this
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    showSearchResult();
                    resultList = arrayList;
                    resultAdapter.changeDataSet(resultList);
                } else {
                  /*  resultList = null;
                    resultAdapter.changeDataSet(resultList);*/
                    Toast.makeText(SearchActivity.this, "没有搜索到内容!", Toast.LENGTH_SHORT).show();
                    updateHistory();
                }
                DialogUtil.dismissProgess();
            }

            @Override
            public void errorNull() {
                //数据为空
                DialogUtil.dismissProgess();
             /*   resultList = null;
                resultAdapter.changeDataSet(resultList);*/
                Toast.makeText(SearchActivity.this, "没有搜索到内容!", Toast.LENGTH_SHORT).show();
                updateHistory();
            }
        });
    }

    private void initEdittext() {
        searchInputEt.setOnKeyListener(new View.OnKeyListener() {//输入完后按键盘上的搜索键【回车键改为了搜索键】

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {//修改回车键功能
                    if (StringUtils.isEmpty(searchInputEt.getText().toString())) {
                        Toast.makeText(SearchActivity.this, "请输入搜索内容!", Toast.LENGTH_SHORT).show();
                        searchInputEt.setFocusable(true);
                    } else {
                        if (StringUtils.containsEmoji(searchInputEt.getText().toString())) {
                            Toast.makeText(SearchActivity.this, getResources().getString(R.string.search_content_has_emoji), Toast.LENGTH_SHORT).show();
                        } else {
                            addHistory();
                            // 先隐藏键盘
                            seachStr(searchInputEt.getText().toString(), 1);
                        }
                    }
                }
                return false;
            }
        });
        searchInputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            /*    if (s.length() == 0) {
                    initHistory();
                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 添加历史记录
     */
    private void addHistory() {
        if (searchModel == null)
            searchModel = new SearchModel();
        //   LogUtils.json("model=" + searchModel.toString());
        searchModel.addSearchHistory(searchInputEt.getText().toString());
        if (historyAdapter != null)
            historyAdapter.updateData(searchModel);
        //  LogUtils.json("model=" + searchModel.toString());

    }

    @Override
    protected void onDestroy() {
        SearchHistoryUtil.saveSearchHistory(searchModel);
        super.onDestroy();
    }

    /**
     * 显示无历史记录状态
     */
    private void showNoneHistory() {
        searchHistoryFl.setVisibility(View.VISIBLE);
        searchHistoryHintFl.setText("尚无搜索历史");
        searchHistoryClearRl.setVisibility(View.GONE);
        searchHistoryRv.setVisibility(View.GONE);
        searchResultFl.setVisibility(View.GONE);
    }


    private void showHistory() {
        searchHistoryFl.setVisibility(View.VISIBLE);
        searchHistoryRv.setVisibility(View.VISIBLE);
        searchHistoryClearRl.setVisibility(View.VISIBLE);
        searchResultFl.setVisibility(View.GONE);
        searchHistoryHintFl.setText("历史记录");
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        searchHistoryRv.setLayoutManager(mLayoutManager);
        searchHistoryRv.setItemAnimator(new DefaultItemAnimator());
        historyAdapter = new SearchHistoryAdapter(this, searchModel, this);
        searchHistoryRv.setAdapter(historyAdapter);
        searchHistoryRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });
        lm = (LinearLayoutManager) searchHistoryRv.getLayoutManager();

    }

    private void updateHistory() {
        searchHistoryFl.setVisibility(View.VISIBLE);
        searchHistoryRv.setVisibility(View.VISIBLE);
        searchHistoryClearRl.setVisibility(View.VISIBLE);
        searchResultFl.setVisibility(View.GONE);
        searchHistoryHintFl.setText("历史记录");
        SearchHistoryUtil.saveSearchHistory(searchModel);
        searchModel = SearchHistoryUtil.getSearchHistory();
        if (searchModel != null) {
            if (historyAdapter == null)
                historyAdapter = new SearchHistoryAdapter(this, searchModel, this);
            historyAdapter.updateData(searchModel);
        }
    }

    public void showSearchResult() {
        searchHistoryFl.setVisibility(View.GONE);
        searchHistoryRv.setVisibility(View.GONE);
        searchHistoryClearRl.setVisibility(View.GONE);
        searchResultFl.setVisibility(View.VISIBLE);

    }

    private ArrayList<RecommendModel> resultList;
    private SearchAdapter resultAdapter;

    private void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        searchResultRv.setLayoutManager(layoutManager);
        resultList = new ArrayList<RecommendModel>();
        resultAdapter = new SearchAdapter(this);
        searchResultRv.setAdapter(resultAdapter);
    }

    @Override
    public void itemTVClick(int position) {
        searchInputEt.setText(searchModel.getSearchList().get(position).getSerachStr());
    }

    @Override
    public void itemIVClick(int position) {
        if (searchModel.removeSearchHistory(position))
            showNoneHistory();
        historyAdapter.notifyItemRemoved(position);
    }

    @Override
    public void itemRemoveAllClick() {
        searchModel.removeAllSearchHistory();
        historyAdapter.notifyDataSetChanged();
        showNoneHistory();
    }
}
