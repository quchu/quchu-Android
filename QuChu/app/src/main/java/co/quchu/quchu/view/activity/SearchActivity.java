package co.quchu.quchu.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.SearchPresenter;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.adapter.SearchAdapter;
import co.quchu.quchu.view.adapter.SearchFilterSortAdapter;
import co.quchu.quchu.widget.EndlessRecyclerOnScrollListener;

/**
 * SearchActivity
 * User: Chenhs
 * Date: 2015-12-04
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.search_input_et)
    EditText searchInputEt;
    @Bind(R.id.search_button_rl)
    RelativeLayout searchButtonRl;
    //    @Bind(R.id.search_title_bar_ll)
//    LinearLayout searchTitleBarLl;
    //    @Bind(R.id.search_history_rv)
//    RecyclerView searchHistoryRv;
//    @Bind(R.id.search_history_clear_rl)
//    RelativeLayout searchHistoryClearRl;
//    @Bind(R.id.search_history_fl)
//    LinearLayout searchHistoryFl;
    @Bind(R.id.search_result_rv)
    RecyclerView searchResultRv;
//    @Bind(R.id.search_result_fl)
//    FrameLayout searchResultFl;
//    @Bind(R.id.search_tag1)
//    TextView tvTag1;
//    @Bind(R.id.search_tag2)
//    TextView tvTag2;
//    @Bind(R.id.search_tag3)
//    TextView tvTag3;
//    @Bind(R.id.llTags)
//    LinearLayout llTags;

    //    SearchModel searchModel;
    //    @Bind(R.id.search_history_hint_fl)
//    TextView searchHistoryHintFl;
    @Bind(R.id.actionCategory)
    TextView actionCategory;
    @Bind(R.id.actionSort)
    TextView actionSort;
    private ArrayList<RecommendModel> resultList;
    private SearchAdapter resultAdapter;
    //    private SearchHistoryAdapter historyAdapter;
//    LinearLayoutManager lm;
    private int mMaxPageNo = -1;
    private int mCurrentPageNo = 1;
    private boolean mIsLoading = false;
    private PopupWindow categoryWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initEdittext();
        initData();
    }


    @Override
    protected void onResume() {
        MobclickAgent.onPageStart("search");
        super.onResume();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd("search");
        super.onPause();
        searchInputEt.requestFocus();

        searchInputEt.postDelayed(new Runnable() {

            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(searchInputEt, 0);
            }
        }, 200);
    }


    private void showPopupWindow(boolean isCategory) {
        if (categoryWindow != null && categoryWindow.isShowing()) {
            categoryWindow.dismiss();
            return;
        }

        View inflate = View.inflate(this, R.layout.layout_search_popupwindow, null);
        categoryWindow = new PopupWindow(inflate, (int) AppContext.Width, (int) (AppContext.Height - actionCategory.getBottom()));
        categoryWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        RecyclerView categoryRecyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);
        ViewGroup.LayoutParams params = categoryRecyclerView.getLayoutParams();
        params.width = (int) AppContext.Width;
        params.height = (int) (AppContext.Height - actionCategory.getBottom());
        categoryRecyclerView.setLayoutParams(params);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        SearchFilterSortAdapter adapter = new SearchFilterSortAdapter();
        categoryRecyclerView.setAdapter(adapter);

        categoryWindow.showAsDropDown(actionCategory);
    }

    private void seachStr(String str, final boolean loadMore) {
        System.out.println("mIsLoading " + mIsLoading);
        if (mIsLoading) return;
        if (loadMore && mCurrentPageNo >= mMaxPageNo && mMaxPageNo != -1) return;
        if (!loadMore) {
            resultList.clear();
            mCurrentPageNo = 1;
        } else if (mCurrentPageNo < mMaxPageNo) {
            mCurrentPageNo += 1;
        }
        mIsLoading = true;
        if (NetUtil.isNetworkConnected(this))
            DialogUtil.showProgess(this, R.string.loading_dialog_text);

        //统计搜索关键字
        Map<String, String> p = new HashMap<>();
        p.put("search_keyword", str);
        MobclickAgent.onEvent(this, "search_type", p);


        SearchPresenter.searchFromService(this, str, mCurrentPageNo, SPUtils.getCityId(), new SearchPresenter.SearchResultListener() {
            @Override
            public void successResult(ArrayList<RecommendModel> arrayList, int maxPageNo) {

                if (arrayList != null && arrayList.size() > 0) {
                    if (mMaxPageNo == -1) {
                        mMaxPageNo = maxPageNo;
                    }
                    searchResultRv.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                    resultAdapter.setCategory(false);
                    resultList.addAll(arrayList);

                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(searchButtonRl.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    resultAdapter.changeDataSet(resultList);

                    searchResultRv.addOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager) searchResultRv.getLayoutManager()) {
                        @Override
                        public void onLoadMore(int current_page) {
                            seachStr(searchInputEt.getText().toString(), true);
                        }
                    });

                } else {
                    Toast.makeText(SearchActivity.this, "没有搜索到内容!", Toast.LENGTH_SHORT).show();
                }
                mIsLoading = false;
                DialogUtil.dismissProgess();
            }

            @Override
            public void errorNull() {
                //数据为空
                DialogUtil.dismissProgess();
                Toast.makeText(SearchActivity.this, "没有搜索到内容!", Toast.LENGTH_SHORT).show();
                mIsLoading = false;
            }
        });
    }

    private void initEdittext() {
        searchInputEt.setOnKeyListener(new View.OnKeyListener() {//输入完后按键盘上的搜索键【回车键改为了搜索键】

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {//修改回车键功能
                    if (NetUtil.isNetworkConnected(getApplicationContext())) {
                        if (StringUtils.isEmpty(searchInputEt.getText().toString())) {
                            Toast.makeText(SearchActivity.this, "请输入搜索内容!", Toast.LENGTH_SHORT).show();
                            searchInputEt.setFocusable(true);
                        } else {
                            if (StringUtils.containsEmoji(searchInputEt.getText().toString())) {
                                Toast.makeText(SearchActivity.this, getResources().getString(R.string.search_content_has_emoji), Toast.LENGTH_SHORT).show();
                            } else {
                                seachStr(searchInputEt.getText().toString(), false);
                            }
                        }
                    } else {
                        Toast.makeText(SearchActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                return false;
            }
        });
    }


    private void initData() {

        searchButtonRl.setOnClickListener(this);
        actionCategory.setOnClickListener(this);
        actionSort.setOnClickListener(this);

        resultAdapter = new SearchAdapter();
        searchResultRv.setLayoutManager(new GridLayoutManager(this, 3));
        searchResultRv.setAdapter(resultAdapter);


        resultList = new ArrayList<>();

        resultAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(SearchActivity.this, QuchuDetailsActivity.class);
                intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, resultList.get(position).getPid());
                startActivity(intent);
            }
        });


    }

    @Override
    public void onClick(View v) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
        switch (v.getId()) {
            case R.id.search_button_rl:
                this.finish();
                break;
            case R.id.actionCategory:
                showPopupWindow(true);
                break;
            case R.id.actionSort:
                showPopupWindow(false);
                break;
        }
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }


//    private void initHistory() {
//        searchModel = SearchHistoryUtil.getSearchHistory();
////        if (searchModel != null && searchModel.getSearchList().size() > 0) {
////            showHistory();
////        } else {
////            showNoneHistory();
////        }
//    }
//    /**
//     * 添加历史记录
//     */
//    private void addHistory() {
//        if (searchModel == null)
//            searchModel = new SearchModel();
//        //   LogUtils.json("model=" + searchModel.toString());
//        searchModel.addSearchHistory(searchInputEt.getText().toString());
////        if (historyAdapter != null)
////            historyAdapter.updateData(searchModel);
//        //  LogUtils.json("model=" + searchModel.toString());
//
//    }

//    @Override
//    protected void onDestroy() {
//        SearchHistoryUtil.saveSearchHistory(searchModel);
//        super.onDestroy();
//    }

//    /**
//     * 显示无历史记录状态
//     */
//    private void showNoneHistory() {
//        searchHistoryFl.setVisibility(View.VISIBLE);
//        searchHistoryHintFl.setText("尚无搜索历史");
//        //searchHistoryClearRl.setVisibility(View.GONE);
//        searchHistoryRv.setVisibility(View.GONE);
//        searchResultFl.setVisibility(View.GONE);
//    }

//
//    private void showHistory() {
//        searchHistoryFl.setVisibility(View.VISIBLE);
//        searchHistoryRv.setVisibility(View.VISIBLE);
//        //searchHistoryClearRl.setVisibility(View.VISIBLE);
//        searchResultFl.setVisibility(View.GONE);
//        searchHistoryHintFl.setText("历史记录");
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
//        searchHistoryRv.setLayoutManager(mLayoutManager);
//        searchHistoryRv.setItemAnimator(new DefaultItemAnimator());
//        historyAdapter = new SearchHistoryAdapter(this, searchModel, this);
//        searchHistoryRv.setAdapter(historyAdapter);
//        searchHistoryRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//            }
//        });
//        lm = (LinearLayoutManager) searchHistoryRv.getLayoutManager();
//
//    }
//
//    private void updateHistory() {
//        searchHistoryFl.setVisibility(View.VISIBLE);
//        searchHistoryRv.setVisibility(View.VISIBLE);
//        //searchHistoryClearRl.setVisibility(View.VISIBLE);
//        searchResultFl.setVisibility(View.GONE);
//        searchHistoryHintFl.setText("历史记录");
//        SearchHistoryUtil.saveSearchHistory(searchModel);
//        searchModel = SearchHistoryUtil.getSearchHistory();
//        if (searchModel != null) {
//            if (historyAdapter == null)
//                historyAdapter = new SearchHistoryAdapter(this, searchModel, this);
//            historyAdapter.updateData(searchModel);
//        }
//    }

//    public void showSearchResult() {
//        searchHistoryFl.setVisibility(View.GONE);
//        searchHistoryRv.setVisibility(View.GONE);
////        searchHistoryClearRl.setVisibility(View.GONE);
//        searchResultFl.setVisibility(View.VISIBLE);
//
//    }


//    @Override
//    public void itemTVClick(int position) {
//        searchInputEt.setText(searchModel.getSearchList().get(position).getSerachStr());
//        seachStr(searchInputEt.getText().toString(), false);
//    }
//
//    @Override
//    public void itemIVClick(int position) {
////        if (searchModel.removeSearchHistory(position))
////            showNoneHistory();
////            historyAdapter.notifyItemRemoved(position);
//    }
//
//    @Override
//    public void itemRemoveAllClick() {
//        searchModel.removeAllSearchHistory();
////        historyAdapter.notifyDataSetChanged();
////        showNoneHistory();
//    }
}
