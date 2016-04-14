package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.analysis.GatherCollectModel;
import co.quchu.quchu.analysis.GatherViewModel;
import co.quchu.quchu.analysis.GatherWantGoModel;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.dialog.ShareDialogFg;
import co.quchu.quchu.dialog.TagsFilterDialog;
import co.quchu.quchu.dialog.VisitorLoginDialogFg;
import co.quchu.quchu.dialog.WantToGoDialogFg;
import co.quchu.quchu.model.DetailModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.model.TagsModel;
import co.quchu.quchu.presenter.InterestingDetailPresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.adapter.QuchuDetailsAdapter;
import co.quchu.quchu.widget.EndlessRecyclerOnScrollListener;


/**
 * InterestingDetailsActivity
 * User: Chenhs
 * Date: 2015-12-09
 * 趣处详情
 */
public class QuchuDetailsActivity extends BaseActivity {


    @Bind(R.id.detail_been_tv)
    TextView detailBeenTv;
    @Bind(R.id.detail_recyclerview)
    RecyclerView mRecyclerView;

    public static final String REQUEST_KEY_PID = "pid";

    private boolean mLoadingMore = false;
    private int pId = 0;
    public DetailModel dModel = new DetailModel();
    private GatherViewModel gatherViewModel;
    private long startViewTime = 0L;
    private QuchuDetailsAdapter mQuchuDetailAdapter;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            detailClick(v);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quchu_details);
        ButterKnife.bind(this);
        getEnhancedToolbar().getRightTv().setText(R.string.pre_order);
        getEnhancedToolbar().getRightTv().setTextColor(getResources().getColor(R.color.gene_textcolor_yellow));
        getEnhancedToolbar().getRightTv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null!=dModel&&null!=dModel.getNet()&&null!=dModel.getNet()){
                    WebViewActivity.enterActivity(QuchuDetailsActivity.this,dModel.getNet(),dModel.getName());
                }
            }
        });

        initData();
        mQuchuDetailAdapter = new QuchuDetailsAdapter(this, dModel, mOnClickListener);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mQuchuDetailAdapter);

        startViewTime = System.currentTimeMillis();
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    private void initData() {
        pId = getIntent().getIntExtra(REQUEST_KEY_PID, -1);
        if (-1 == pId) {
            Toast.makeText(this, "该趣处已不存在!", Toast.LENGTH_SHORT).show();
        } else {
            DialogUtil.showProgess(this, "数据加载中...");
            InterestingDetailPresenter.getInterestingData(this, pId, new InterestingDetailPresenter.getDetailDataListener() {
                @Override
                public void getDetailData(DetailModel model) {
                    dModel.copyFrom(model);
                    mQuchuDetailAdapter.notifyDataSetChanged();
                    mQuchuDetailAdapter.setLoadMoreListener(new QuchuDetailsAdapter.OnLoadMoreListener() {
                        @Override
                        public void onLoadMore() {
                            if (mLoadingMore){
                                return;
                            }
                            mLoadingMore = true;
                            //TODO load nearby stuff
                            mRecyclerView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(QuchuDetailsActivity.this,NearbyActivity.class));

                                }
                            },1500l);

                        }
                    });
//                    mRecyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager)mRecyclerView.getLayoutManager()) {
//                        @Override
//                        public void onLoadMore(int current_page) {
//                            ImageView ivLoadMore = new ImageView(getApplicationContext());
//                            ivLoadMore.setImageResource(R.drawable.ic_refresh);
//
//                            mRecyclerView.addView(ivLoadMore,mRecyclerView.getChildCount());
//                            ivLoadMore.animate().rotation(36000).setDuration(360).start();
//                            //startActivity(new Intent(QuchuDetailsActivity.this,NearbyActivity.class));
//                        }
//                    });
                    bindingDetailData();
                    DialogUtil.dismissProgess();
                }
            });
        }
        gatherViewModel = new GatherViewModel(pId + "");
    }

    private void bindingDetailData() {
        changeBottomBeenBg(dModel.isIsout());
        changeCollectState(dModel.isIsf());

    }


    private void changeBottomBeenBg(boolean isOut) {
        if (isOut) {
            detailBeenTv.setTextColor(getResources().getColor(R.color.black));
            detailBeenTv.setBackgroundResource(R.drawable.shape_detail_bottom_full_bg);
        } else {
            detailBeenTv.setTextColor(getResources().getColor(R.color.gene_textcolor_yellow));
            detailBeenTv.setBackgroundResource(R.drawable.shape_detail_bottom_bg);
        }
    }


    private void changeCollectState(boolean isCollect) {
        dModel.setIsf(isCollect);
        mQuchuDetailAdapter.notifyDataSetChanged();
    }


    @OnClick({R.id.detail_want_tv, R.id.detail_been_tv})
    public void detailClick(View v) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
        if (dModel != null) {
            switch (v.getId()) {
                case R.id.tvQuguo:
                            ArrayList<TagsModel> data = new ArrayList<>();
                            for (int i = 0; i < 19; i++) {
                                TagsModel tag = new TagsModel();
                                tag.setCode(String.valueOf(i));
                                tag.setEn("EN"+i);
                                tag.setZh("标签"+ (i%7==0?"凑数":""));
                                tag.setTagId(i*1000);
                                data.add(tag);
                            }
                            ArrayList<Boolean> selection = new ArrayList<>();
                            selection.add(false);
                            selection.add(true);
                            selection.add(false);
                            selection.add(true);
                            selection.add(false);
                            TagsFilterDialog tagsFilterDialog = TagsFilterDialog.newInstance(data,selection);
                            tagsFilterDialog.show(getFragmentManager(),"");
                    break;
                case R.id.tvFootPrint:
                    startActivity(new Intent(QuchuDetailsActivity.this,FootPrintActivity.class));
                    break;
               /* case R.id.detail_store_phone_ll:
                    callPhone();
                    break;*/
                case R.id.detail_been_tv:

                    if (AppContext.user.isIsVisitors()) {
                        VisitorLoginDialogFg vDialog = VisitorLoginDialogFg.newInstance(VisitorLoginDialogFg.QBEEN);
                        vDialog.show(getFragmentManager(), "visitor");
                    } else {
                        userBeen();//用户去过
                    }

                    break;
                case R.id.detail_want_tv:
                    //用户想去
                    if (dModel.isIsf()) {
                        // startActivity(new Intent(InterestingDetailsActivity.this, ReserveActivity.class).putExtra("PlaceUrl", dModel.getNet()));
                        WebViewActivity.enterActivity(QuchuDetailsActivity.this,dModel.getNet(),dModel.getName());
                    } else {
                        WantToGoDialogFg lDialog = WantToGoDialogFg.newInstance();
                        lDialog.show(getFragmentManager(), "blur_sample", new Want2GoClickImpl());
                    }
                    if (AppContext.gatherList == null)
                        AppContext.gatherList = new ArrayList<>();
                    if (AppContext.user != null && dModel != null)
                        AppContext.gatherList.add(new GatherWantGoModel(AppContext.user.getUserId(), dModel.getPid()));
                    break;
                case R.id.detail_button_add_postcard_rl:

                    //添加明信片
                    Intent intent = new Intent();
                    intent.putExtra("pId", dModel.getPid());
                    //intent.putExtra("pName", dModel.getName());
                    intent.setClass(this, FootPrintActivity.class);
                    startActivity(intent);
                    break;
                case R.id.detail_button_collect_rl:
                    //收藏
                    setFavorite();
                    break;
                case R.id.detail_button_share_rl:
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
     * 是否去过
     */
    private void userBeen() {

        Intent intent = new Intent(this, AddPostCardActivity.class);
        intent.putExtra("pName", dModel.getName());
        intent.putExtra("pId", dModel.getPid());
        if (dModel.isIsout()) {

            //Edit post card
        } else {

            //Create new one
        }
        startActivity(intent);
//        InterestingDetailPresenter.getUserOutPlace(this, pId, dModel.isIsout(), new InterestingDetailPresenter.DetailDataListener() {
//            @Override
//            public void onSuccessCall(String str) {
//                dModel.setIsout(!dModel.isIsout());
//                changeBottomBeenBg(dModel.isIsout());
//            }
//
//            @Override
//            public void onErrorCall(String str) {
//
//            }
//        });
    }

    /**
     * 收藏
     */
    private void setFavorite() {
        if (AppContext.user.isIsVisitors()) {
            VisitorLoginDialogFg vDialog = VisitorLoginDialogFg.newInstance(VisitorLoginDialogFg.QFAVORITE);
            vDialog.show(getFragmentManager(), "visitor");
        } else {
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
                WebViewActivity.enterActivity(QuchuDetailsActivity.this,dModel.getNet(),dModel.getName());
            }
        }
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart("PlaceDetailActivity");

        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd("PlaceDetailActivity");
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
                changeBottomBeenBg(true);
            }
        }
    }
}
