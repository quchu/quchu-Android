package co.quchu.quchu.view.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.BaseBehaviorFragment;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.AreaBean;
import co.quchu.quchu.model.DetailModel;
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
import co.quchu.quchu.view.adapter.SearchChildCategoryAdapter;
import co.quchu.quchu.view.adapter.SearchPopWinBaseAdapter;
import co.quchu.quchu.view.adapter.SearchSortAdapter;
import co.quchu.quchu.widget.AreaView;
import co.quchu.quchu.widget.EndlessRecyclerOnScrollListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SearchFragment
 * User: Chenhs
 * Date: 2015-12-04
 */
public class SearchActivity extends BaseBehaviorActivity implements View.OnClickListener {

    @Override
    public ArrayMap<String, Object> getUserBehaviorArguments() {
        return null;
    }

    @Override
    public int getUserBehaviorPageId() {
        return 114;
    }

    @Override protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    @Override
    protected String getPageNameCN() {
        return getString(R.string.pname_search);
    }
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
    @Bind(R.id.search_line)
    View searchLine;
    @Bind(R.id.vCover)
    View vCover;

    private ArrayList<RecommendModel> resultList;
    private SearchAdapter resultAdapter;
    private int mMaxPageNo = -1;
    private int mCurrentPageNo = 1;
    private boolean mIsLoading = false;
    private PopupWindow popupWindow;


    private ImageView firstFilterIcon;

    private static final int SHOWING_POPUP_TYPE_CATEGORY = 1;
    private static final int SHOWING_POPUP_TYPE_AREA = 2;
    private static final int SHOWING_POPUP_TYPE_SORT = 3;

    private int currentShowingPopupType;
    private View popWinView;
    private RecyclerView categoryRecyclerView;
    private RecyclerView categoryRecyclerViewChild;
    private LinearLayout llCategories;
    private AreaView areaView;

    private String categoryCode = "", areaId = "", circleId = "", sortType = "";
    private String categoryName = "";
    private String circleName = "";
    private String mLastCategoryCode = "";
    private RecyclerView sortRecyclerView;
    private SearchCategoryAdapter filterCategoryAdapter;
    private SearchSortAdapter filterSortAdapter;
    private SearchChildCategoryAdapter childTagsAdapter;

    private boolean filterUserInput;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initPopupWindow();
        initEdittext();
        initData();
        SearchPresenter.getCategoryTag(this);
        SearchPresenter.getGroupTags(this);
        searchInputEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInputEt.setCursorVisible(true);
                filterUserInput = true;
            }
        });
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (popupWindow.isShowing()){
            popupWindow.dismiss();
        }
        ButterKnife.unbind(this);
    }




    private ImageView searchPopIndicator;

    private void initPopupWindow() {

        searchFilterLL1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {


            @Override
            public void onGlobalLayout() {
                searchFilterLL1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int[] location = new int[2];
                searchFilterLL1.getLocationOnScreen(location);

                int height = (int) (AppContext.Height - location[1] - searchFilterLL1.getHeight());

                popupWindow = new PopupWindow((int) AppContext.Width, height);
                popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
                popupWindow.setFocusable(false);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        if (firstFilterIcon != null) {
                            ObjectAnimator animator = ObjectAnimator.ofFloat(firstFilterIcon, "Rotation", 0);
                            animator.setDuration(400);
                            animator.start();
                        }
                        currentShowingPopupType = -1;
                        firstFilterIcon = null;
                    }
                });

                popWinView = View.inflate(getApplicationContext(), R.layout.layout_search_popupwindow, null);
                llCategories = (LinearLayout) popWinView.findViewById(R.id.llCategories);
                categoryRecyclerView = (RecyclerView) popWinView.findViewById(R.id.rvTags);
                categoryRecyclerViewChild = (RecyclerView) popWinView.findViewById(R.id.rvChildsTags);
                sortRecyclerView = (RecyclerView) popWinView.findViewById(R.id.search_pop_sort_rv);
                areaView = (AreaView) popWinView.findViewById(R.id.areaView);
                searchPopIndicator = (ImageView) popWinView.findViewById(R.id.search_pop_indicator);


                popWinView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        seachStr(false);

                        dismissDialog();
                    }
                });



                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext()){
                    @Override
                    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                        super.onLayoutChildren(recycler, state);
                        categoryRecyclerViewChild.getLayoutParams().height = categoryRecyclerView.getHeight();
                    }
                };





                categoryRecyclerView.setLayoutManager(linearLayoutManager);
                categoryRecyclerViewChild.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                sortRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                //商圈
                areaView.setAreaSelectedListener(new AreaView.OnAreaSelected() {
                    @Override
                    public void areaSelected(AreaBean areaBean, AreaBean.CircleListBean circleListBean) {
                        areaId = areaBean.getAreaId();
                        circleId = circleListBean.getCircleId();
                        circleName = circleListBean.getCircleName();
                        dismissDialog();
                        
                        searchFilterTV2.setText(TextUtils.isEmpty(circleListBean.getCircleId()) ? areaBean.getAreaName() : circleListBean.getCircleName());
                        seachStr(false);
                    }
                });
//类别
                filterCategoryAdapter = new SearchCategoryAdapter();
                filterCategoryAdapter.displayDivider(false);
                childTagsAdapter = new SearchChildCategoryAdapter();

                categoryRecyclerView.setAdapter(filterCategoryAdapter);
                filterCategoryAdapter.setItemClickListener(new SearchPopWinBaseAdapter.OnItemClickListener<SearchCategoryBean>() {
                    @Override
                    public void itemClick(int position, SearchCategoryBean item) {

                        categoryCode = String.valueOf(item.getTagId());
                        categoryName = String.valueOf(item.getZh());
                        searchFilterTV1.setText(item.getZh());
                        childTagsAdapter.setData(item.getDatas());
                        if (item.getTagId()==-1){
                            categoryCode = "";
                        }
                        mLastCategoryCode = categoryCode;
                    }
                });

                childTagsAdapter.setItemClickListener(new SearchPopWinBaseAdapter.OnItemClickListener<DetailModel.TagsEntity>() {
                    @Override
                    public void itemClick(int position, DetailModel.TagsEntity item) {

                        if (position==0){
                            dismissDialog();
                            seachStr(false);
                        }else{
                            categoryCode = String.valueOf(item.getTagId());
                            categoryName = String.valueOf(item.getZh());
                            dismissDialog();
                            seachStr(false);
                        }
                        mLastCategoryCode = categoryCode;
                    }
                });

                categoryRecyclerViewChild.setAdapter(childTagsAdapter);
//排序
                filterSortAdapter = new SearchSortAdapter();
                sortRecyclerView.setAdapter(filterSortAdapter);
                filterSortAdapter.setItemClickListener(new SearchSortAdapter.OnItemClickListener<SearchSortBean>() {
                    @Override
                    public void itemClick(int position, SearchSortBean item) {
                        sortType = String.valueOf(item.getSortId());
                        dismissDialog();

                        searchFilterTV3.setText(item.getSortName());
                        seachStr(false);
                    }
                });

            }
        });


    }

    private void showPopupWindow(ImageView currentIcon, TextView currentTV, View contentView) {
        searchLine.setVisibility(View.VISIBLE);
        //箭头动画
        ObjectAnimator animator = ObjectAnimator.ofFloat(currentIcon, "Rotation", 180);
        animator.setDuration(400);
        animator.start();
        if (firstFilterIcon != null) {
            animator = ObjectAnimator.ofFloat(firstFilterIcon, "Rotation", 0);
            animator.setDuration(400);
            animator.start();
        }

        firstFilterIcon = currentIcon;

        popupWindow.setContentView(contentView);
        if (popupWindow.isShowing()) {
            popupWindow.update();
        } else {
            popupWindow.showAsDropDown(searchFilterContainer);
        }
        vCover.setVisibility(View.VISIBLE);
    }

    private View getPopWinView(int type) {
        float xOff = (AppContext.Width * 2 * type + AppContext.Width) / 6;
        ObjectAnimator animator = ObjectAnimator.ofFloat(searchPopIndicator, "TranslationX", xOff);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();

        switch (type) {
            case 0:
                if (categoryParentList == null) {
                    SearchPresenter.getCategoryTag(this);
                }
                llCategories.setVisibility(View.VISIBLE);
                areaView.setVisibility(View.INVISIBLE);
                sortRecyclerView.setVisibility(View.INVISIBLE);
                break;
            case 1:
                if (areaData == null) {
                    SearchPresenter.getAreaList(this);
                }
                areaView.setVisibility(View.VISIBLE);
                llCategories.setVisibility(View.INVISIBLE);
                sortRecyclerView.setVisibility(View.INVISIBLE);

                break;
            case 2:
                if (sortList == null) {
                    SearchPresenter.getSortTypeList(this);
                }
                areaView.setVisibility(View.INVISIBLE);
                llCategories.setVisibility(View.INVISIBLE);
                sortRecyclerView.setVisibility(View.VISIBLE);
                break;
        }
        return popWinView;
    }

    private List<SearchSortBean> sortList;
    private ArrayList<SearchCategoryBean> categoryBeanList;
    private List<SearchCategoryBean> categoryParentList;

    private List<AreaBean> areaData;

    public void setAreaData(List<AreaBean> areaData) {
        this.areaData = areaData;
        searchFilterTV2.setText("全部商圈");
        areaView.setDatas(areaData);
    }

    public void setSortList(List<SearchSortBean> sortList) {
        this.sortList = sortList;
        filterSortAdapter.setDatas(sortList);
        searchFilterTV3.setText(sortList.get(0).getSortName());
    }

    //获取大的分类
    public void initCategoryList(ArrayList<SearchCategoryBean> categoryParentList) {

        if (resultAdapter.isCategory()) {
            resultAdapter.setCategoryList(categoryParentList);
//        SearchPresenter.getTagByParentId(getActivity(), categoryParentList.get(0).getTagId());
        }

    }



    @Override
    public void onClick(View v) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
        switch (v.getId()) {
            case R.id.search_button_rl:
                searchInputEt.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                if (popupWindow != null && popupWindow.isShowing()) {
                    dismissDialog();

                }
                break;

            case R.id.searchFilterLL1:
                if (currentShowingPopupType == SHOWING_POPUP_TYPE_CATEGORY) {
                    if (mLastCategoryCode !=mLastCategoryCode){}{
                        seachStr(false);
                    }
                    dismissDialog();

                } else {
                    currentShowingPopupType = SHOWING_POPUP_TYPE_CATEGORY;
                    showPopupWindow(searchFilterIcon1, searchFilterTV1, getPopWinView(0));
                }
                break;
            case R.id.searchFilterLL2:
                if (currentShowingPopupType == SHOWING_POPUP_TYPE_AREA) {
                    dismissDialog();

                } else {
                    currentShowingPopupType = SHOWING_POPUP_TYPE_AREA;
                    showPopupWindow(searchFilterIcon2, searchFilterTV2, getPopWinView(1));
                }
                break;
            case R.id.searchFilterLL3:
                if (currentShowingPopupType == SHOWING_POPUP_TYPE_SORT) {
                    dismissDialog();

                } else {
                    currentShowingPopupType = SHOWING_POPUP_TYPE_SORT;
                    showPopupWindow(searchFilterIcon3, searchFilterTV3, getPopWinView(2));
                }
                break;
            case R.id.search_input_et:
                if (popupWindow != null && popupWindow.isShowing()) {
                    dismissDialog();
                }
                break;
        }
    }



    private void initData() {

        searchButtonRl.setOnClickListener(this);

        searchFilterLL1.setOnClickListener(this);
        searchFilterLL2.setOnClickListener(this);
        searchFilterLL3.setOnClickListener(this);
        searchInputEt.setOnClickListener(this);

        resultAdapter = new SearchAdapter();
        searchResultRv.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        searchResultRv.setAdapter(resultAdapter);
        resultList = new ArrayList<>();

        resultAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, Parcelable bean, int itemYype) {
                if (itemYype == SearchAdapter.ITEM_TYPE_RESULT) {


                    ArrayMap<String,Object> params = new ArrayMap<>();
                    params.put("趣处名称",((RecommendModel) bean).getName());
                    params.put("入口名称",getPageNameCN());
                    ZGEvent(params,"进入趣处详情页");


                    Intent intent = new Intent(getApplicationContext(), QuchuDetailsActivity.class);
                    intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, ((RecommendModel) bean).getPid());
                    startActivity(intent);
                } else {
                    switch (position){
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
                    categoryCode = String.valueOf(((SearchCategoryBean) bean).getTagId());
                    categoryName = String.valueOf(((SearchCategoryBean)bean).getZh());
                    categoryGroupAllString = "全部" + ((SearchCategoryBean) bean).getZh();
                    categoryGroupAllId = ((SearchCategoryBean) bean).getCode();
                    searchFilterTV1.setText(categoryGroupAllString);
                    //SearchPresenter.getTagByParentId(getApplicationContext(), ((SearchCategoryBean) bean).getTagId());
                    searchInputEt.setText(((SearchCategoryBean) bean).getZh());
                    filterUserInput=false;
                    seachStr(false);

                    searchInputEt.setSelection(searchInputEt.getText().toString().trim().length());
                    searchInputEt.setCursorVisible(false);
                }
            }
        });
    }

    private String categoryGroupAllString = "全部";
    private String categoryGroupAllId = "";


    private void seachStr(final boolean loadMore) {


        String str = "";
        if (filterUserInput)
            str = searchInputEt.getText().toString().trim();

        if (mIsLoading) return;

        if (loadMore && mCurrentPageNo >= mMaxPageNo && mMaxPageNo != -1) return;
        if (!loadMore) {
            resultList.clear();
            mCurrentPageNo = 1;
        } else if (mCurrentPageNo < mMaxPageNo) {
            mCurrentPageNo += 1;
        }
        mIsLoading = true;
        if (NetUtil.isNetworkConnected(getApplicationContext()))
            DialogUtil.showProgess(getApplicationContext(), R.string.loading_dialog_text);

        //统计搜索关键字
        Map<String, String> p = new HashMap<>();
        p.put("search_keyword", str);


        ArrayMap<String,Object> params = new ArrayMap<>();
        params.put("商圈名称",circleName);
        params.put("输入文本",str);
        params.put("分类名称",categoryName);
        ZGEvent(params,"搜索条件");
        SearchPresenter.searchFromService(getApplicationContext(), areaId, str, mCurrentPageNo, SPUtils.getCityId(), categoryCode, circleId, sortType, new SearchPresenter.SearchResultListener() {
            @Override
            public void successResult(ArrayList<RecommendModel> arrayList, int maxPageNo) {
                searchResultRv.clearOnScrollListeners();

                if (arrayList != null && arrayList.size() > 0) {
                    if (mMaxPageNo == -1) {
                        mMaxPageNo = maxPageNo;
                    }
//                    if (searchFilterContainer.getVisibility() == View.INVISIBLE) {
                    searchFilterContainer.setVisibility(View.VISIBLE);
//                        searchFilterContainer.animate().translationYBy(-searchFilterContainer.getHeight()).translationY(searchFilterContainer.getHeight()).setDuration(300).start();
//                    }


                    searchLine.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.bg_pager));

                    if (searchResultRv.getLayoutManager() instanceof GridLayoutManager)
                        searchResultRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    resultAdapter.setCategory(false);
                    resultList.addAll(arrayList);

                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(searchButtonRl.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    resultAdapter.changeDataSet(resultList);
                    searchResultRv.addOnScrollListener(
                            new EndlessRecyclerOnScrollListener((LinearLayoutManager) searchResultRv.getLayoutManager()) {
                                @Override
                                public void onLoadMore(int current_page) {
                                    seachStr(true);
                                }
                            });

                } else {
                    Toast.makeText(getApplicationContext(), "没有搜索到内容!", Toast.LENGTH_SHORT).show();
                }
                mIsLoading = false;
                DialogUtil.dismissProgess();


            }

            @Override
            public void errorNull() {
                //数据为空
                DialogUtil.dismissProgess();
                Toast.makeText(getApplicationContext(), "没有搜索到内容!", Toast.LENGTH_SHORT).show();
                mIsLoading = false;
                resultAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initEdittext() {

        searchInputEt.setOnKeyListener(new View.OnKeyListener() {//输入完后按键盘上的搜索键【回车键改为了搜索键】

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {//修改回车键功能
                    if (NetUtil.isNetworkConnected(getApplicationContext())) {
                        if (StringUtils.isEmpty(searchInputEt.getText().toString())) {
                            Toast.makeText(getApplicationContext(), "请输入搜索内容!", Toast.LENGTH_SHORT).show();
//                            searchInputEt.setFocusable(true);
                        } else {
                            if (StringUtils.containsEmoji(searchInputEt.getText().toString())) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.search_content_has_emoji), Toast.LENGTH_SHORT).show();
                            } else {
                                if (resultAdapter.isCategory()) {
                                    categoryCode = "";
                                }
                                //分类列表显示大的分类
                                if (filterCategoryAdapter.getItemCount() == 0) {
                                    filterCategoryAdapter.setDatas(categoryParentList);
                                }
                                seachStr(false);
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(searchResultRv.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                return false;
            }
        });
    }



    public void initGroupList(List<SearchCategoryBean> response) {
        this.categoryParentList = response;
        filterCategoryAdapter.setDatas(response);
        if (response.size()>0){
            childTagsAdapter.setData(response.get(0).getDatas());
        }


    }


    private void dismissDialog(){
        vCover.setVisibility(View.GONE);
        popupWindow.dismiss();
    }
}
