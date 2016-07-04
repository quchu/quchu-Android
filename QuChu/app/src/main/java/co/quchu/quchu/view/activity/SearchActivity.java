package co.quchu.quchu.view.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.AreaBean;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.model.SearchCategoryBean;
import co.quchu.quchu.model.SearchSortBean;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.SearchPresenter;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.adapter.SearchAdapter;
import co.quchu.quchu.view.adapter.SearchCategoryAdapter;
import co.quchu.quchu.view.adapter.SearchPopWinBaseAdapter;
import co.quchu.quchu.view.adapter.SearchSortAdapter;
import co.quchu.quchu.widget.AreaView;
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
    TextView searchButtonRl;

    @Bind(R.id.search_result_rv)
    RecyclerView searchResultRv;
    @Bind(R.id.searchFilterTV1)
    TextView searchFilterTV1;
    @Bind(R.id.searchFilterTV2)
    TextView searchFilterTV2;

    @Bind(R.id.searchFilterLL1)
    LinearLayout searchFilterLL1;
    @Bind(R.id.searchFilterLL2)
    LinearLayout searchFilterLL2;
    @Bind(R.id.searchFilterTV3)
    TextView searchFilterTV3;
    @Bind(R.id.searchFilterLL3)
    LinearLayout searchFilterLL3;

    @Bind(R.id.searchFilterIcon1)
    ImageView searchFilterIcon1;
    @Bind(R.id.searchFilterIcon2)
    ImageView searchFilterIcon2;
    @Bind(R.id.searchFilterIcon3)
    ImageView searchFilterIcon3;
    @Bind(R.id.searchFilterContainer)
    LinearLayout searchFilterContainer;
    @Bind(R.id.search_back)
    ImageView searchBack;


    private ArrayList<RecommendModel> resultList;
    private SearchAdapter resultAdapter;
    private int mMaxPageNo = -1;
    private int mCurrentPageNo = 1;
    private boolean mIsLoading = false;
    private PopupWindow popupWindow;


    private ImageView firstFilterIcon;
    private TextView firstFilterTV;

    private static final int SHOWING_POPUP_TYPE_CATEGORY = 1;
    private static final int SHOWING_POPUP_TYPE_AREA = 2;
    private static final int SHOWING_POPUP_TYPE_SORT = 3;

    private int currentShowingPopupType;
    private View popWinView;
    private RecyclerView categoryRecyclerView;
    private AreaView areaView;
    private List<AreaBean> areaData;

    private String categoryCode = "", areaCode = "", sortType = "";
    private RecyclerView sortRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initEdittext();
        initData();
        SearchPresenter.getCategoryTag(this);
        SearchPresenter.getAreaList(this);
        SearchPresenter.getSortTypeList(this);
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

    private void initPopupWindow() {


        popupWindow = new PopupWindow((int) AppContext.Width, (int) (AppContext.Height - searchFilterLL1.getBottom()));
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (firstFilterIcon != null) {
                    ObjectAnimator animator = ObjectAnimator.ofFloat(firstFilterIcon, "Rotation", 0);
                    animator.setDuration(400);
                    animator.start();
                }
                if (firstFilterTV != null) {
                    firstFilterTV.setTextColor(ContextCompat.getColor(SearchActivity.this, R.color.standard_color_h2_dark));
                }
                firstFilterTV = null;
                currentShowingPopupType = -1;
                firstFilterIcon = null;
            }
        });
    }

    private void showPopupWindow(ImageView currentIcon, TextView currentTV, View contentView) {
        if (popupWindow == null) initPopupWindow();
        //箭头动画
        ObjectAnimator animator = ObjectAnimator.ofFloat(currentIcon, "Rotation", -180);
        animator.setDuration(400);
        animator.start();
        if (firstFilterIcon != null) {
            animator = ObjectAnimator.ofFloat(firstFilterIcon, "Rotation", 0);
            animator.setDuration(400);
            animator.start();
        }
        currentTV.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        if (firstFilterTV != null) {
            firstFilterTV.setTextColor(ContextCompat.getColor(this, R.color.standard_color_h2_dark));
        }

        firstFilterTV = currentTV;
        firstFilterIcon = currentIcon;

        popupWindow.setContentView(contentView);
        if (popupWindow.isShowing()) {
            popupWindow.update();
        } else {
            popupWindow.showAsDropDown(searchFilterContainer);
        }
    }

    private View getPopWinView(int type) {
        if (popWinView == null) {
            popWinView = View.inflate(this, R.layout.layout_search_popupwindow, null);
            categoryRecyclerView = (RecyclerView) popWinView.findViewById(R.id.recyclerView);
            sortRecyclerView = (RecyclerView) popWinView.findViewById(R.id.search_pop_sort_rv);
            areaView = (AreaView) popWinView.findViewById(R.id.areaView);

            ViewGroup.LayoutParams params = categoryRecyclerView.getLayoutParams();
            params.width = (int) AppContext.Width;
            params.height = (int) (AppContext.Height - searchFilterLL1.getBottom());

            categoryRecyclerView.setLayoutParams(params);
            areaView.setLayoutParams(params);
            sortRecyclerView.setLayoutParams(params);

            areaView.setAreaSelectedListener(new AreaView.OnAreaSelected() {
                @Override
                public void areaSelected(AreaBean areaBean, AreaBean.CircleListBean circleListBean) {
                    areaCode = String.valueOf(circleListBean.getCircleId());
                    popupWindow.dismiss();

                    searchFilterTV2.setText(circleListBean.getCircleName());

                    seachStr(false);
                }
            });
        }
        switch (type) {
            case 0:
                categoryRecyclerView.setVisibility(View.VISIBLE);
                areaView.setVisibility(View.INVISIBLE);
                sortRecyclerView.setVisibility(View.INVISIBLE);

                categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                sortRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                if (categoryRecyclerView.getAdapter() == null) {
                    SearchCategoryAdapter categoryAdapter = new SearchCategoryAdapter(categoryBeanList);
                    categoryAdapter.setItemClickListener(new SearchPopWinBaseAdapter.OnItemClickListener<SearchCategoryBean>() {
                        @Override
                        public void itemClick(int position, SearchCategoryBean item) {
                            categoryCode = String.valueOf(item.getTagId());
                            popupWindow.dismiss();
                            searchFilterTV1.setText(item.getZh());
                            seachStr(false);
                        }
                    });
                    categoryRecyclerView.setAdapter(categoryAdapter);
                }
                break;
            case 1:
                areaView.setVisibility(View.VISIBLE);
                categoryRecyclerView.setVisibility(View.INVISIBLE);
                sortRecyclerView.setVisibility(View.INVISIBLE);

                areaView.setDatas(areaData);
                break;
            case 2:
                areaView.setVisibility(View.INVISIBLE);
                categoryRecyclerView.setVisibility(View.INVISIBLE);
                sortRecyclerView.setVisibility(View.VISIBLE);
                if (sortRecyclerView.getAdapter() == null) {
                    SearchSortAdapter sortAdapter = new SearchSortAdapter(sortList);
                    sortAdapter.setItemClickListener(new SearchSortAdapter.OnItemClickListener<SearchSortBean>() {
                        @Override
                        public void itemClick(int position, SearchSortBean item) {
                            sortType = String.valueOf(item.getSortId());
                            popupWindow.dismiss();
                            searchFilterTV3.setText(item.getSortName());
                            seachStr(false);
                        }
                    });
                    sortRecyclerView.setAdapter(sortAdapter);
                }
                break;
        }
        return popWinView;
    }

    List<SearchSortBean> sortList;

    public void setAreaData(List<AreaBean> areaData) {
        this.areaData = areaData;
        searchFilterTV2.setText("全部商圈");
    }

    public void setSortList(List<SearchSortBean> sortList) {
        this.sortList = sortList;
        searchFilterTV3.setText(sortList.get(0).getSortName());
    }

    @Override
    public void onClick(View v) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
        switch (v.getId()) {
            case R.id.search_button_rl:
                searchInputEt.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                break;
            case R.id.search_back:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                finish();
            case R.id.searchFilterLL1:
                if (currentShowingPopupType == SHOWING_POPUP_TYPE_CATEGORY) {
                    popupWindow.dismiss();
                } else {
                    currentShowingPopupType = SHOWING_POPUP_TYPE_CATEGORY;
                    showPopupWindow(searchFilterIcon1, searchFilterTV1, getPopWinView(0));
                }
                break;
            case R.id.searchFilterLL2:
                if (currentShowingPopupType == SHOWING_POPUP_TYPE_AREA) {
                    popupWindow.dismiss();
                } else {
                    currentShowingPopupType = SHOWING_POPUP_TYPE_AREA;
                    showPopupWindow(searchFilterIcon2, searchFilterTV2, getPopWinView(1));
                }
                break;
            case R.id.searchFilterLL3:
                if (currentShowingPopupType == SHOWING_POPUP_TYPE_SORT) {
                    popupWindow.dismiss();
                } else {
                    currentShowingPopupType = SHOWING_POPUP_TYPE_SORT;
                    showPopupWindow(searchFilterIcon3, searchFilterTV3, getPopWinView(2));
                }
                break;
            case R.id.search_input_et:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                break;
        }
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    private void initData() {

        searchButtonRl.setOnClickListener(this);

        searchFilterLL1.setOnClickListener(this);
        searchFilterLL2.setOnClickListener(this);
        searchFilterLL3.setOnClickListener(this);
        searchInputEt.setOnClickListener(this);
        searchBack.setOnClickListener(this);

        resultAdapter = new SearchAdapter();
        searchResultRv.setLayoutManager(new GridLayoutManager(this, 3));
        searchResultRv.setAdapter(resultAdapter);
        resultList = new ArrayList<>();

        resultAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, Parcelable bean, int itemYype) {
                if (itemYype == SearchAdapter.ITEM_TYPE_RESULT) {
                    Intent intent = new Intent(SearchActivity.this, QuchuDetailsActivity.class);
                    intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, ((RecommendModel) bean).getPid());
                    startActivity(intent);
                } else {
                    categoryCode = String.valueOf(((SearchCategoryBean) bean).getTagId());
                    searchFilterTV1.setText("全部" + ((SearchCategoryBean) bean).getZh());

                    seachStr(false);
                }
            }
        });
    }

    private ArrayList<SearchCategoryBean> categoryBeanList;

    public void initCategoryList(ArrayList<SearchCategoryBean> categoryBeanList) {
        this.categoryBeanList = categoryBeanList;
        resultAdapter.setCategory(true);
        resultAdapter.setCategoryList(categoryBeanList);
    }

    private void seachStr(final boolean loadMore) {

        String str = searchInputEt.getText().toString().trim();

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


        SearchPresenter.searchFromService(this, str, mCurrentPageNo, SPUtils.getCityId(), categoryCode, areaCode, sortType, new SearchPresenter.SearchResultListener() {
            @Override
            public void successResult(ArrayList<RecommendModel> arrayList, int maxPageNo) {

                if (arrayList != null && arrayList.size() > 0) {
                    if (mMaxPageNo == -1) {
                        mMaxPageNo = maxPageNo;
                    }
                    searchFilterContainer.setVisibility(View.VISIBLE);

                    // TODO: 2016/6/29
                    searchFilterTV1.setText("全部美食");
                    searchFilterTV2.setText("全部商圈");
                    searchFilterTV3.setText("智能排序");

                    searchResultRv.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                    resultAdapter.setCategory(false);
                    resultList.addAll(arrayList);

                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(searchButtonRl.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    resultAdapter.changeDataSet(resultList);
                    searchResultRv.addOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager) searchResultRv.getLayoutManager()) {
                        @Override
                        public void onLoadMore(int current_page) {
                            seachStr(true);
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
                                seachStr(false);
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

    @Override
    public void onBackPressed() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else
            super.onBackPressed();
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
