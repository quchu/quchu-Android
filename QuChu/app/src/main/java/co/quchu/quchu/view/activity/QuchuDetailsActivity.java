package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.StringTokenizer;

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
import co.quchu.quchu.dialog.VisitorLoginDialogFg;
import co.quchu.quchu.dialog.WantToGoDialogFg;
import co.quchu.quchu.model.DetailModel;
import co.quchu.quchu.presenter.InterestingDetailPresenter;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.adapter.DetailListViewAdapter;
import co.quchu.quchu.view.adapter.QuchuDetailAdapter;
import co.quchu.quchu.widget.HorizontalNumProgressBar;
import co.quchu.quchu.widget.InnerListView;
import co.quchu.quchu.widget.OutSideScrollView;
import co.quchu.quchu.widget.TagCloudView;
import co.quchu.quchu.widget.ratingbar.ProperRatingBar;


/**
 * InterestingDetailsActivity
 * User: Chenhs
 * Date: 2015-12-09
 * 趣处详情
 */
public class QuchuDetailsActivity extends BaseActivity {


    @Bind(R.id.detail_been_tv)
    TextView detailBeenTv;
    @Bind(R.id.detail_button_group_out_ll)
    LinearLayout detailButtonGroupOutLl;    //悬浮操作条
    @Bind(R.id.detail_button_collect_out_iv)
    ImageView detailButtonCollectOutIv;
    @Bind(R.id.title_content_tv)
    TextView titleContentTv;
    @Bind(R.id.detail_recyclerview)
    RecyclerView mRecyclerView;
    private int pId, pPosition = 0;
    public DetailModel dModel = new DetailModel();
    private GatherViewModel gatherViewModel;
    private long startViewTime = 0L;
    private QuchuDetailAdapter mQuchuDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quchu_details);
        ButterKnife.bind(this);
        initTitleBar();
        initData();
        mQuchuDetailAdapter = new QuchuDetailAdapter(getApplicationContext(), dModel, new QuchuDetailAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v) {
                detailClick(v);
                Log.d("Click",String.valueOf(v.getId()));
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setAdapter(mQuchuDetailAdapter);
        startViewTime = System.currentTimeMillis();
    }

    private void initData() {
        pId = getIntent().getIntExtra("pId", -1);
        pPosition = getIntent().getIntExtra("pPosition", 0);
        if (-1 == pId) {
            Toast.makeText(this, "该趣处已不存在!", Toast.LENGTH_SHORT).show();
        } else {
            DialogUtil.showProgess(this, "数据加载中...");
            InterestingDetailPresenter.getInterestingData(this, pId, new InterestingDetailPresenter.getDetailDataListener() {
                @Override
                public void getDetailData(DetailModel model) {
                    dModel.copyFrom(model);
                    mQuchuDetailAdapter.notifyDataSetChanged();
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
        DetailListViewAdapter dlvAdapter = new DetailListViewAdapter(this, dModel.getImglist());
        //TODO DETAIL IMAGE LIST
        //detailImageListIlv.setAdapter(dlvAdapter);

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
        if (isCollect) {
            detailButtonCollectOutIv.setImageResource(R.drawable.ic_detail_collect);
        } else {
            detailButtonCollectOutIv.setImageResource(R.drawable.ic_detail_uncollect);
        }
    }


    public void detailClick(View v) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
        if (dModel != null) {
            switch (v.getId()) {
               /* case R.id.detail_store_phone_ll:
                    callPhone();
                    break;*/
                case R.id.detail_been_tv:
                    userBeen();//用户去过
                    break;
                case R.id.detail_want_tv:
                    //用户想去
                    if (dModel.isIsf()) {
                        // startActivity(new Intent(InterestingDetailsActivity.this, ReserveActivity.class).putExtra("PlaceUrl", dModel.getNet()));
                        Uri uri = Uri.parse(dModel.getNet());
                        Intent it = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(it);
                    } else {
                        WantToGoDialogFg lDialog = WantToGoDialogFg.newInstance();
                        lDialog.show(getFragmentManager(), "blur_sample", new Want2GoClickImpl());
                    }
                    if (AppContext.gatherList == null)
                        AppContext.gatherList = new ArrayList<>();
                    if (AppContext.user != null && dModel != null)
                        AppContext.gatherList.add(new GatherWantGoModel(AppContext.user.getUserId(), dModel.getPid()));
                    break;
                case R.id.detail_button_add_postcard_out_rl:
                case R.id.detail_button_add_postcard_rl:
                    //添加明信片
                    Intent intent = new Intent();
                    intent.putExtra("pId", dModel.getPid());
                    intent.putExtra("pName", dModel.getName());
                    intent.setClass(this, PlacePostCardActivity.class);
                    startActivity(intent);
                    break;
                case R.id.detail_button_collect_out_rl:
                case R.id.detail_button_collect_rl:
                    //收藏
                    setFavorite();
                    break;
                case R.id.detail_button_share_out_rl:
                case R.id.detail_button_share_rl:
                    //分享
                    ShareDialogFg shareDialogFg = ShareDialogFg.newInstance(dModel.getPid(), dModel.getName(), true);
                    shareDialogFg.show(getFragmentManager(), "share_dialog");
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
        InterestingDetailPresenter.getUserOutPlace(this, pId, dModel.isIsout(), new InterestingDetailPresenter.DetailDataListener() {
            @Override
            public void onSuccessCall(String str) {
                dModel.setIsout(!dModel.isIsout());
                changeBottomBeenBg(dModel.isIsout());
            }

            @Override
            public void onErrorCall(String str) {

            }
        });
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
        if (dModel != null && dModel.isIsout()) {
            AppContext.dCardListRemoveIndex = pPosition;
        }

        LogUtils.json(" AppContext.dCardListRemoveIndex = pPosition;==" + AppContext.dCardListRemoveIndex);
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
                Uri uri = Uri.parse(dModel.getNet());
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        }
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart("PlaceDetailActivity");
        MobclickAgent.onResume(this);

        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd("PlaceDetailActivity");
        MobclickAgent.onPause(this);
    }
}
