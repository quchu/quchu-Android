package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.dialog.QuchuDetailsMoreDialog;
import co.quchu.quchu.dialog.RatingQuchuDialog;
import co.quchu.quchu.model.DetailModel;
import co.quchu.quchu.model.ImageModel;
import co.quchu.quchu.model.NearbyItemModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.model.SimpleQuchuDetailAnalysisModel;
import co.quchu.quchu.model.TagsModel;
import co.quchu.quchu.model.VisitedInfoModel;
import co.quchu.quchu.model.VisitedUsersModel;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.InterestingDetailPresenter;
import co.quchu.quchu.presenter.NearbyPresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.adapter.GalleryAdapter;
import co.quchu.quchu.view.adapter.QuchuDetailsAdapter;
import co.quchu.quchu.widget.SimpleIndicatorView;


/**
 * InterestingDetailsActivity
 * User: Chenhs
 * Date: 2015-12-09
 * 趣处详情
 */
public class QuchuDetailsActivity extends BaseBehaviorActivity implements AppBarLayout.OnOffsetChangedListener {


    @Bind(R.id.detail_recyclerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.detail_bottom_group_ll)
    View detail_bottom_group_ll;
    @Bind(R.id.appbar)
    AppBarLayout appbar;


    @Bind(R.id.ivFavorite)
    ImageView ivFavorite;
    @Bind(R.id.tvFootprintCount)
    TextView tvFootprintCount;

    @Bind(R.id.vpGallery)
    ViewPager vpGallery;

    @Bind(R.id.siv)
    SimpleIndicatorView siv;

    ImageView vFakeReturnButton;

    public static final String REQUEST_KEY_PID = "pid";
    public static final String REQUEST_KEY_FROM = "from";
    private long startViewTime = 0L;
    private int pId = 0;
    private boolean mLoadingMore = false;
    public DetailModel dModel = new DetailModel();
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
    public static final String FROM_TYPE_MAP = "map";//从智能推荐进来的
    public static final String FROM_TYPE_TAG = "detail_tag_t";//从其他类别进来的
    public static final String FROM_TYPE_SUBJECT = "detail_subject_t";//从发现进来的
    public static final String FROM_TYPE_RECOM = "detail_recommendation_t";//从详情页推荐进来的
    public static final String FROM_TYPE_PROFILE = "detail_profile_t";//从用户收藏进来的
    public static final String BUNDLE_KEY_DATA_MODEL = "BUNDLE_KEY_DATA_MODEL";
    private String from;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            overridePendingTransition(-1,-1);
        }

        setContentView(R.layout.activity_quchu_details);
        ButterKnife.bind(this);
        from = getIntent().getStringExtra(REQUEST_KEY_FROM);
        getEnhancedToolbar().getTitleTv().setText("");
        getEnhancedToolbar().getTitleTv().setAlpha(0f);
        getEnhancedToolbar().getLeftIv().setImageResource(R.mipmap.ic_forward);
        getEnhancedToolbar().getLeftIv().setRotation(180);


        if (null != savedInstanceState) {
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

    @Override
    public ArrayMap<String, Object> getUserBehaviorArguments() {

        ArrayMap<String,Object> data = new ArrayMap<>();
        data.put("pid",getIntent().getIntExtra(REQUEST_KEY_PID, -1));
        return data;
    }

    @Override
    public int getUserBehaviorPageId() {
        return 111;
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
            public void errorListener(VolleyError error, String exception, String msg) {
            }
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
            public void errorListener(VolleyError error, String exception, String msg) {
            }
        });
    }

    private void getRatingInfo() {
        InterestingDetailPresenter.getVisitedInfo(getApplicationContext(), pId, new CommonListener<VisitedInfoModel>() {
            @Override
            public void successListener(VisitedInfoModel response) {
                if (null != response) {
                    mVisitedInfoModel = response;
                }
            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {
            }
        });
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BUNDLE_KEY_DATA_MODEL, dModel);
    }


    private void initData() {
        pId = getIntent().getIntExtra(REQUEST_KEY_PID, -1);
        if (-1 == pId) {
            Toast.makeText(this, "该趣处已不存在!", Toast.LENGTH_SHORT).show();
        } else {
            if (dModel == null || StringUtils.isEmpty(dModel.getName())) {

                DialogUtil.showProgess(this, "数据加载中...");
                InterestingDetailPresenter.getInterestingData(this, pId, new InterestingDetailPresenter.getDetailDataListener() {
                    @Override
                    public void getDetailData(DetailModel model) {

                        bindingDetailData(model);
                        DialogUtil.dismissProgess();
                    }
                });
            } else {
                bindingDetailData(dModel);
            }

        }
    }


    private void bindingDetailData(DetailModel model) {

        dModel.copyFrom(model);
        getEnhancedToolbar().getTitleTv().setText(dModel.getName());
        if(null==mQuchuDetailAdapter){
            return;
        }
        if (null != dModel.getImglist()&&dModel.getImglist().size()>0) {
            List<ImageModel> imageSet = new ArrayList<>();
            for (int i = 0; i < dModel.getImglist().size() && i<=3; i++) {
                imageSet.add(dModel.getImglist().get(i).convert2ImageModel());
            }
            vpGallery.setAdapter(new GalleryAdapter(imageSet));
            siv.setIndicators(imageSet.size());
            siv.setVisibility(View.VISIBLE);
            vpGallery.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    siv.setCurrentIndex(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }else{
            siv.setVisibility(View.GONE);
        }

        mQuchuDetailAdapter.notifyDataSetChanged();
        mQuchuDetailAdapter.setLoadMoreListener(new QuchuDetailsAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if(!NetUtil.isNetworkConnected(getApplicationContext())){
                    Toast.makeText(getApplicationContext(),R.string.network_error,Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mLoadingMore) {
                    return;
                }

                mLoadingMore = true;
                String str = "";

                for (int i = 0; i < dModel.getNearPlace().size(); i++) {
                    str += dModel.getNearPlace().get(i).getPlaceId();
                    str += "|";
                }
                if (str.contains("|")) {
                    str = str.substring(0, str.length() - 1);
                }
                double la;
                double lo;
                try {
                    la = Double.valueOf(dModel.getLatitude());
                    lo = Double.valueOf(dModel.getLongitude());
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

                loadMore(str, null, 1, dModel.getPid(), SPUtils.getCityId(), la, lo);
            }
        });

        changeCollectState(dModel.isIsf());
    }


    private void changeCollectState(boolean isCollect) {
        dModel.setIsf(isCollect);
        ivFavorite.setImageResource(isCollect ? R.mipmap.ic_star : R.mipmap.ic_star_light);
        tvFootprintCount.setText(String.valueOf(dModel.getCardCount()));
        tvFootprintCount.setVisibility(dModel.getCardCount()>0?View.VISIBLE:View.INVISIBLE);
        mQuchuDetailAdapter.notifyDataSetChanged();
    }


    private void loadMore(final String recommendPids, final String cateIds, final int isFirst, final int pid, final int cid, final double lat, final double lon) {
        if (mIsLoadMoreRunning) return;
        mIsLoadMoreRunning = true;

        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
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
        }, 1000l);
    }


    private void ratingQuchu(final List<TagsModel> selection, final int score) {
        String strTags = "";
        if (selection.size() == 1) {
            strTags += selection.get(0).getTagId();
        }
        for (int i = 0; i < selection.size(); i++) {
            if (selection.get(i).isPraise()) {
                strTags += selection.get(i).getTagId();
                strTags += ",";
            }
        }
        if (strTags.contains(",")) {
            strTags = strTags.substring(0, strTags.length() - 1);
        }
        if (mIsRatingRunning) return;
        mIsRatingRunning = true;
        InterestingDetailPresenter.updateRatingInfo(getApplicationContext(), dModel.getPid(), score, strTags, new InterestingDetailPresenter.DetailDataListener() {
            @Override
            public void onSuccessCall(String str) {
                Toast.makeText(QuchuDetailsActivity.this, "评价成功", Toast.LENGTH_SHORT).show();
                mVisitedInfoModel.setScore(score);
                for (int i = 0; i < selection.size(); i++) {
                    mVisitedInfoModel.getResult().get(i).setPraise(selection.get(i).isPraise());
                }
                mIsRatingRunning = false;
                dModel.setIsout(true);
                getVisitors();
                getRatingInfo();
            }

            @Override
            public void onErrorCall(String str) {
                mIsRatingRunning = false;
            }
        });

    }


    @OnClick({R.id.ivFootprint, R.id.ivFavorite, R.id.ivShare, R.id.ivMore})
    public void detailClick(View v) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
        if (dModel != null) {
            switch (v.getId()) {

                case R.id.ivMore:

                    QuchuDetailsMoreDialog quchuDetailsMoreDialog = new QuchuDetailsMoreDialog(QuchuDetailsActivity.this);
                    quchuDetailsMoreDialog.setOnButtonClickListener(new QuchuDetailsMoreDialog.OnButtonClickListener() {
                        @Override
                        public void onReturnClick() {
                            EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_GOTO_HOME_PAGE));
                        }

                        @Override
                        public void onPreOrderClick() {
                            if (NetUtil.isNetworkConnected(getApplicationContext())) {
                                if (null != dModel && null != dModel.getNet() && !StringUtils.isEmpty(dModel.getNet())) {
                                    MobclickAgent.onEvent(QuchuDetailsActivity.this, "reserve_c");
                                    WebViewActivity.enterActivity(QuchuDetailsActivity.this, dModel.getNet(), dModel.getName(),false);
                                } else {
                                    MobclickAgent.onEvent(QuchuDetailsActivity.this, "reserve_c");
                                    WebViewActivity.enterActivity(QuchuDetailsActivity.this, "http://www.dianping.com", dModel.getName(),false);
                                }
                            } else {
                                Toast.makeText(QuchuDetailsActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onShareClick() {
                            startActivity(ShareQuchuActivity.getStartIntent(QuchuDetailsActivity.this, dModel.getCover(), dModel.getName(), dModel.getAddress(), dModel.getPid()));
                        }
                    });
                    quchuDetailsMoreDialog.show();
                    break;

                case R.id.ivShare:
                    RatingQuchuDialog tagsFilterDialog = RatingQuchuDialog.newInstance(mVisitedInfoModel.getUserCount(), mVisitedInfoModel.getScore(), mVisitedInfoModel.getResult());
                    tagsFilterDialog.show(getSupportFragmentManager(), "");
                    tagsFilterDialog.setPickingListener(new RatingQuchuDialog.OnFinishPickingListener() {
                        @Override
                        public void onFinishPicking(List<TagsModel> selection, int score) {
                            if (null != selection) {
                                ratingQuchu(selection, score);
                            }
                        }
                    });

                    break;
                case R.id.ivFootprint:
                    Intent footPrintIntent = new Intent(QuchuDetailsActivity.this, FootPrintActivity.class);
                    footPrintIntent.putExtra(FootPrintActivity.BUNDLE_KEY_QUCHU_ID, dModel.getPid());
                    footPrintIntent.putExtra(FootPrintActivity.BUNDLE_KEY_QUCHU_NAME, dModel.getName());
                    startActivity(footPrintIntent);
                    break;
                case R.id.ivFavorite:
                    //收藏
                    setFavorite();
                    break;
//                case R.id.ivShare:
//                    //分享
//                    try {
//                        ShareDialogFg shareDialogFg = ShareDialogFg.newInstance(dModel.getPid(), dModel.getName(), true);
//                        shareDialogFg.show(getSupportFragmentManager(), "share_dialog");
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//                    break;

                case R.id.detail_store_address_ll:

                    if (!NetUtil.isNetworkConnected(getApplicationContext())){
                        Toast.makeText(QuchuDetailsActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                    }else if(!dModel.isMap()){
                        Toast.makeText(QuchuDetailsActivity.this, "此趣处暂无导航信息!", Toast.LENGTH_SHORT).show();
                    }else {
                        EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_FINISH_MAP));
                        MobclickAgent.onEvent(this, "map_c");

                        Intent mapIntent = new Intent(QuchuDetailsActivity.this, PlaceMapActivity.class);
                        mapIntent.putExtra("pid",dModel.getPid());
                        mapIntent.putExtra("lat", dModel.getLatitude());
                        mapIntent.putExtra("lon", dModel.getLongitude());
                        mapIntent.putExtra("gdlon", dModel.gdLongitude);
                        mapIntent.putExtra("gdlat", dModel.gdLatitude);
                        mapIntent.putExtra("title", dModel.getName());
                        mapIntent.putExtra("entity",dModel.convert2NearbyMapItem());
                        mapIntent.putExtra("placeAddress", dModel.getAddress());
                        startActivity(mapIntent);
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
    public void onBackPressed() {
        EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_CANCLE_FAVORITE_QUCHU, dModel.isIsf(), pId));
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        MobclickAgent.onPageStart(from);
        appbar.addOnOffsetChangedListener(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd(from);
        appbar.removeOnOffsetChangedListener(this);
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

        switch (event.getFlag()) {
            case EventFlags.EVENT_FOOTPRINT_UPDATED:
                if (null != dModel && (Integer) event.getContent()[0] == dModel.getPid()) {
                    dModel.setMyCardId((Integer) event.getContent()[0]);
                }
                break;
            case EventFlags.EVENT_QUCHU_RATING_UPDATE:
                getRatingInfo();
                getVisitors();
                break;
            case EventFlags.EVENT_POST_CARD_ADDED:
                if ((Integer) event.getContent()[0] == dModel.getPid()) {
                    dModel.setCardCount(dModel.getCardCount() + 1);
                }
                break;
            case EventFlags.EVENT_POST_CARD_DELETED:
                if (((Integer) event.getContent()[1]) == dModel.getPid() && dModel.getCardCount() > 1) {
                    dModel.setCardCount(dModel.getCardCount() - 1);
                }
                break;
            case EventFlags.EVENT_GOTO_HOME_PAGE:
                finish();
                break;
        }

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (appbar.getTotalScrollRange()==0 || verticalOffset==0){
            return;
        }
        float alpha = Math.abs(Float.valueOf(verticalOffset))/appbar.getTotalScrollRange();
        getEnhancedToolbar().getTitleTv().setAlpha(alpha);
        int color = (int) (255-(alpha*255));
        getEnhancedToolbar().getLeftIv().setColorFilter(Color.argb(255,color,color,color), PorterDuff.Mode.MULTIPLY);


    }
}
