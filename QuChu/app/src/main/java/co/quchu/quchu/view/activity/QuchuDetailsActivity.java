package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.analysis.GatherCollectModel;
import co.quchu.quchu.analysis.GatherViewModel;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.dialog.RatingQuchuDialog;
import co.quchu.quchu.dialog.ShareDialogFg;
import co.quchu.quchu.dialog.VisitorLoginDialogFg;
import co.quchu.quchu.dialog.WantToGoDialogFg;
import co.quchu.quchu.model.DetailModel;
import co.quchu.quchu.model.NearbyItemModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.model.SimpleQuchuDetailAnalysisModel;
import co.quchu.quchu.model.TagsModel;
import co.quchu.quchu.model.VisitedInfoModel;
import co.quchu.quchu.model.VisitedUsersModel;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.InterestingDetailPresenter;
import co.quchu.quchu.presenter.NearbyPresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.adapter.QuchuDetailsAdapter;
import co.quchu.quchu.widget.HidingScrollListener;


/**
 * InterestingDetailsActivity
 * User: Chenhs
 * Date: 2015-12-09
 * 趣处详情
 */
public class QuchuDetailsActivity extends BaseActivity {


    @Bind(R.id.detail_recyclerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.detail_bottom_group_ll)
    View detail_bottom_group_ll;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.ivFavorite)
    ImageView vFavorite;
    @Bind(R.id.ivShare)
    View vShare;

    private long mLastAnimated = -1;
    public static final String REQUEST_KEY_PID = "pid";
    public static final String REQUEST_KEY_FROM = "from";
    private long startViewTime = 0L;
    private int pId = 0;
    private boolean mLoadingMore = false;
    public DetailModel dModel = new DetailModel();
    private GatherViewModel gatherViewModel;
    private QuchuDetailsAdapter mQuchuDetailAdapter;
    private VisitedInfoModel mVisitedInfoModel;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            detailClick(v);
        }
    };
    boolean mIsRatingRunning = false;
    boolean mIsLoadMoreRunning = false;

    public static final String FROM_TYPE_HOME = "detail_home_t";//从智能推荐进来的
    public static final String FROM_TYPE_TAG = "detail_tag_t";//从其他类别进来的
    public static final String FROM_TYPE_SUBJECT = "detail_subject_t";//从发现进来的
    public static final String FROM_TYPE_RECOM = "detail_recommendation_t";//从详情页推荐进来的
    public static final String FROM_TYPE_PROFILE = "detail_profile_t";//从用户收藏进来的
    public static final String BUNDLE_KEY_DATA_MODEL = "BUNDLE_KEY_DATA_MODEL";
    private String from;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quchu_details);
        ButterKnife.bind(this);
        from = getIntent().getStringExtra(REQUEST_KEY_FROM);

        getEnhancedToolbar().getRightTv().setText(R.string.pre_order);
        getEnhancedToolbar().getRightTv().setTextColor(getResources().getColor(R.color.gene_textcolor_yellow));
        getEnhancedToolbar().getRightTv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != dModel && null != dModel.getNet() && !StringUtils.isEmpty(dModel.getNet()) ) {
                    MobclickAgent.onEvent(QuchuDetailsActivity.this, "reserve_c");
                    WebViewActivity.enterActivity(QuchuDetailsActivity.this, dModel.getNet(), dModel.getName());
                }else{
                    MobclickAgent.onEvent(QuchuDetailsActivity.this, "reserve_c");
                    WebViewActivity.enterActivity(QuchuDetailsActivity.this,"http://www.dianping.com",dModel.getName());
                }
            }
        });


        if (null!=savedInstanceState){
            dModel = (DetailModel) savedInstanceState.getSerializable(BUNDLE_KEY_DATA_MODEL);
        }

        initData();
        mQuchuDetailAdapter = new QuchuDetailsAdapter(this, dModel, mOnClickListener);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mQuchuDetailAdapter);

        startViewTime = System.currentTimeMillis();

        getVisitors();

        getVisitorsAnlysis();

        getRatingInfo();

    }

    private void getVisitorsAnlysis() {
        InterestingDetailPresenter.getVisitorAnalysis(getApplicationContext(), pId, new CommonListener<SimpleQuchuDetailAnalysisModel>() {
            @Override
            public void successListener(SimpleQuchuDetailAnalysisModel response) {
                if (null != response) {
                    mQuchuDetailAdapter.updateVisitorAnalysis(response);
                }
            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {}
        });
    }

    private void getVisitors() {
        InterestingDetailPresenter.getVisitedUsers(getApplicationContext(), pId, new CommonListener<VisitedUsersModel>() {
            @Override
            public void successListener(VisitedUsersModel response) {
                if (null != response) {
                    mQuchuDetailAdapter.updateVisitedUsers(response);
                }
            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {}
        });
    }

    private void getRatingInfo(){
        InterestingDetailPresenter.getVisitedInfo(getApplicationContext(), pId, new CommonListener<VisitedInfoModel>() {
            @Override
            public void successListener(VisitedInfoModel response) {
                if (null != response) {
                    mVisitedInfoModel = response;
                }
            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {}
        });
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putSerializable(BUNDLE_KEY_DATA_MODEL,dModel);
    }


    private void initData() {
        pId = getIntent().getIntExtra(REQUEST_KEY_PID, -1);
        if (-1 == pId) {
            Toast.makeText(this, "该趣处已不存在!", Toast.LENGTH_SHORT).show();
        } else {
            if (dModel==null||StringUtils.isEmpty(dModel.getName())){

                DialogUtil.showProgess(this, "数据加载中...");
                InterestingDetailPresenter.getInterestingData(this, pId, new InterestingDetailPresenter.getDetailDataListener() {
                    @Override
                    public void getDetailData(DetailModel model) {
                        bindingDetailData(model);
                        DialogUtil.dismissProgess();
                    }
                });
            }else{
                bindingDetailData(dModel);
            }

        }
        gatherViewModel = new GatherViewModel(pId + "");
    }


    private void bindingDetailData(DetailModel model) {
        dModel.copyFrom(model);
        if (null != dModel.getImglist() && dModel.getImglist().size() > 1) {
            getSwipeBackLayout().setEnableGesture(false);
        }
        mQuchuDetailAdapter.notifyDataSetChanged();
        mQuchuDetailAdapter.setLoadMoreListener(new QuchuDetailsAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mLoadingMore) {
                    return;
                }

                mLoadingMore = true;
                String s ="";
                if (dModel.getNearPlace().size()==1){
                    s += dModel.getNearPlace().get(0).getPlaceId();
                }else {
                    for (int i = 0; i < dModel.getNearPlace().size(); i++) {
                        s+= dModel.getNearPlace().get(i).getPlaceId();
                        s+= "|";
                    }
                    s = s.substring(0,s.length()-1);
                }

                loadMore(s, null, 1, dModel.getPid(), SPUtils.getCityId(), SPUtils.getLatitude(), SPUtils.getLongitude());

            }
        });

        mRecyclerView.addOnScrollListener(new HidingScrollListener() {

            @Override
            public void onHide() {
                if ((System.currentTimeMillis() - mLastAnimated) < 500) {
                    return;
                }
                detail_bottom_group_ll.animate()
                        .translationY(detail_bottom_group_ll.getHeight())
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .setDuration(500)
                        .start();
                appbar.animate()
                        .translationY(-appbar.getHeight())
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .setDuration(500).withStartAction(new Runnable() {
                    @Override
                    public void run() {
                        appbar.setVisibility(View.GONE);
                        detail_bottom_group_ll.setVisibility(View.GONE);
                        mLastAnimated = System.currentTimeMillis();
                    }
                }).start();
            }

            @Override
            public void onShow() {
                if ((System.currentTimeMillis() - mLastAnimated) < 555) {
                    return;
                }
                detail_bottom_group_ll.animate()
                        .translationY(0)
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .setDuration(500)
                        .start();
                appbar.animate()
                        .translationY(0)
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .setDuration(500).withStartAction(new Runnable() {
                    @Override
                    public void run() {
                        appbar.setVisibility(View.VISIBLE);
                        detail_bottom_group_ll.setVisibility(View.VISIBLE);
                        mLastAnimated = System.currentTimeMillis();
                    }
                })
                        .start();
            }
        });
        changeCollectState(dModel.isIsf());
    }


    private void changeCollectState(boolean isCollect) {
        dModel.setIsf(isCollect);
        vFavorite.setImageResource(isCollect?R.mipmap.ic_star_stroke:R.mipmap.ic_star_fill);
        mQuchuDetailAdapter.notifyDataSetChanged();
    }


    private void loadMore(String recommendPids, String cateIds, int isFirst, final int pid, int cid, double lat, double lon) {
        if (mIsLoadMoreRunning) return;
        mIsLoadMoreRunning = true;

        NearbyPresenter.getNearbyData(getApplicationContext(), recommendPids, cateIds, isFirst, pid, cid, lat, lon, 1, new NearbyPresenter.getNearbyDataListener() {
            @Override
            public void getNearbyData(List<NearbyItemModel> model, int pMaxPageNo) {
                Intent intent = new Intent(QuchuDetailsActivity.this, NearbyActivity.class);

                String pids = "";
                List<DetailModel.NearPlace> places = dModel.getNearPlace();

                if (places.size() == 1) {
                    pids += places.get(0).getPlaceId();
                } else {
                    for (int i = 0; i < places.size(); i++) {
                        pids += places.get(i).getPlaceId();
                        pids += "|";
                    }
                    pids = pids.substring(0, pids.length() - 1);
                }
                MobclickAgent.onEvent(QuchuDetailsActivity.this, "allrecommendation_c");
                intent.putExtra(NearbyActivity.BUNDLE_KEY_DATA, (Serializable) model);
                intent.putExtra(NearbyActivity.BUNDLE_KEY_PID, dModel.getPid());
                intent.putExtra(NearbyActivity.BUNDLE_KEY_RECOMMEND_PIDS, pids);
                mQuchuDetailAdapter.finishLoadMore();
                startActivity(intent);
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIsLoadMoreRunning = false;
                        mLoadingMore = false;
                    }
                }, 500);
            }
        });
    }


    private void ratingQuchu(List<TagsModel> selection, int score) {
        String strTags = "";
        if (selection.size() == 1) {
            strTags += selection.get(0).getTagId();
        } else {
            for (int i = 0; i < selection.size(); i++) {
                if (selection.get(i).isPraise()){
                    strTags += selection.get(i).getTagId();
                    strTags += "|";
                }
            }
            strTags = strTags.substring(0, strTags.length() - 1);
        }
        if (mIsRatingRunning) return;
        mIsRatingRunning = true;
        InterestingDetailPresenter.updateRatingInfo(getApplicationContext(), dModel.getPid(), score, strTags, new InterestingDetailPresenter.DetailDataListener() {
            @Override
            public void onSuccessCall(String str) {
                Toast.makeText(getApplicationContext(), "评价成功", Toast.LENGTH_LONG).show();
                mIsRatingRunning = false;
            }

            @Override
            public void onErrorCall(String str) {
                mIsRatingRunning = false;
            }
        });

    }

    @OnClick({R.id.ivShare,R.id.ivFavorite})
    public void detailClick(View v) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
        if (dModel != null) {
            switch (v.getId()) {

                case R.id.tvQuguo:

//                    if (AppContext.user.isIsVisitors()) {
//                        VisitorLoginDialogFg vDialog = VisitorLoginDialogFg.newInstance(VisitorLoginDialogFg.QBEEN);
//                        vDialog.show(getFragmentManager(), "visitor");
//                    } else

                    if(null!=mVisitedInfoModel){
                        RatingQuchuDialog tagsFilterDialog = RatingQuchuDialog.newInstance(mVisitedInfoModel.getScore(),mVisitedInfoModel.getResult());
                        tagsFilterDialog.show(getFragmentManager(),"");
                        tagsFilterDialog.setPickingListener(new RatingQuchuDialog.OnFinishPickingListener() {
                            @Override
                            public void onFinishPicking(List<TagsModel> selection, int score) {
                                if (null != selection) {
                                    ratingQuchu(selection, score);
                                }
                            }
                        });
                    }

                    break;
                case R.id.tvFootPrint:
                    Intent footPrintIntent = new Intent(QuchuDetailsActivity.this, FootPrintActivity.class);
                    footPrintIntent.putExtra(FootPrintActivity.BUNDLE_KEY_QUCHU_ID, dModel.getPid());
                    footPrintIntent.putExtra(FootPrintActivity.BUNDLE_KEY_QUCHU_NAME, dModel.getName());
                    startActivity(footPrintIntent);
                    break;
//                case R.id.detail_want_tv:
//                    //用户想去
//                    if (dModel.isIsf()) {
//                        // startActivity(new Intent(InterestingDetailsActivity.this, ReserveActivity.class).putExtra("PlaceUrl", dModel.getNet()));
//                        WebViewActivity.enterActivity(QuchuDetailsActivity.this,dModel.getNet(),dModel.getName());
//                    } else {
//                        WantToGoDialogFg lDialog = WantToGoDialogFg.newInstance();
//                        lDialog.show(getFragmentManager(), "blur_sample", new Want2GoClickImpl());
//                    }
//                    if (AppContext.gatherList == null)
//                        AppContext.gatherList = new ArrayList<>();
//                    if (AppContext.user != null && dModel != null)
//                        AppContext.gatherList.add(new GatherWantGoModel(AppContext.user.getUserId(), dModel.getPid()));
//                    break;
                case R.id.detail_button_add_postcard_rl:
                    Intent intent = new Intent();
                    intent.putExtra("pId", dModel.getPid());
                    //intent.putExtra("pName", dModel.getName());
                    intent.setClass(this, FootPrintActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ivFavorite:
                    //收藏
                    setFavorite();
                    break;
                case R.id.ivShare:
                    //分享
                    try {
                        ShareDialogFg shareDialogFg = ShareDialogFg.newInstance(dModel.getPid(), dModel.getName(), true);
                        shareDialogFg.show(getFragmentManager(), "share_dialog");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;

                case R.id.detail_store_address_ll:

                    if (!"台北".equals(SPUtils.getCityName())) {
                        MobclickAgent.onEvent(this, "map_c");

                        Intent mapIntent = new Intent(QuchuDetailsActivity.this, PlaceMapActivity.class);
                        mapIntent.putExtra("lat", dModel.getLatitude());
                        mapIntent.putExtra("lon", dModel.getLongitude());
                        mapIntent.putExtra("gdlon", dModel.gdLongitude);
                        mapIntent.putExtra("gdlat", dModel.gdLatitude);
                        mapIntent.putExtra("title", dModel.getName());
                        mapIntent.putExtra("placeAddress", dModel.getAddress());
                        startActivity(mapIntent);
                    } else {
                        Toast.makeText(QuchuDetailsActivity.this, "此趣处暂无导航信息!", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    /**
     * 收藏
     */
    private void setFavorite() {
        InterestingDetailPresenter.setDetailFavorite(this, pId, dModel.isIsf(), new InterestingDetailPresenter.DetailDataListener() {
            @Override
            public void onSuccessCall(String str) {
                dModel.setIsf(!dModel.isIsf());
                changeCollectState(dModel.isIsf());
                if (AppContext.selectedPlace != null) {
                    AppContext.selectedPlace.setIsf(dModel.isIsf());
                    AppContext.dCardListNeedUpdate = true;
                }
                if (dModel.isIsf()) {
                    Toast.makeText(QuchuDetailsActivity.this, "收藏成功!", Toast.LENGTH_SHORT).show();
                    if (AppContext.gatherList == null)
                        AppContext.gatherList = new ArrayList<>();
                    AppContext.gatherList.add(new GatherCollectModel(GatherCollectModel.collectPlace, dModel.getPid()));
                } else {
                    Toast.makeText(QuchuDetailsActivity.this, "取消收藏!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorCall(String str) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        if (gatherViewModel == null)
            gatherViewModel = new GatherViewModel(pId + "");
        gatherViewModel.setViewDuration((System.currentTimeMillis() - startViewTime) / 1000);
        if (AppContext.gatherList == null)
            AppContext.gatherList = new ArrayList<>();
        if (gatherViewModel != null)
            AppContext.gatherList.add(gatherViewModel);


        super.onDestroy();
    }

    public class Want2GoClickImpl implements WantToGoDialogFg.Wan2GoClickListener {

        @Override
        public void collectClick() {
            if (AppContext.user.isIsVisitors()) {
                VisitorLoginDialogFg vDialog = VisitorLoginDialogFg.newInstance(VisitorLoginDialogFg.QFAVORITE);
                vDialog.show(getFragmentManager(), "visitor");
            } else {
                if (dModel.isIsf()) {
                    Toast.makeText(QuchuDetailsActivity.this, "已经收藏成功了!", Toast.LENGTH_SHORT).show();
                } else {
                    setFavorite();
                }
            }
        }

        @Override
        public void reserveClick() {
            if (StringUtils.isEmpty(dModel.getNet())) {
                Toast.makeText(QuchuDetailsActivity.this, "还没找到去往你心里的路...", Toast.LENGTH_SHORT).show();
            } else {
                LogUtils.json("webview ==");
                WebViewActivity.enterActivity(QuchuDetailsActivity.this, dModel.getNet(), dModel.getName());
            }
        }
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart(from);
        super.onResume();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd(from);
        super.onPause();

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onMessageEvent(QuchuEventModel event) {

        if (event.getFlag() == EventFlags.EVENT_QUCHU_DETAIL_UPDATED && null != dModel) {
            if ((Integer) event.getContent() == dModel.getPid()) {
                dModel.setMyCardId((Integer) event.getContent());
            }
        }

        if (event.getFlag() == EventFlags.EVENT_QUCHU_RATING_UPDATE){
            getRatingInfo();
            getVisitors();
        }
    }
}
