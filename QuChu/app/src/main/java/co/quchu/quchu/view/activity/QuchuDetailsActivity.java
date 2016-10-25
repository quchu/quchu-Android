package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.DetailModel;
import co.quchu.quchu.model.HangoutUserModel;
import co.quchu.quchu.model.NearbyItemModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.model.SimpleQuchuDetailAnalysisModel;
import co.quchu.quchu.model.TagsModel;
import co.quchu.quchu.model.VisitedInfoModel;
import co.quchu.quchu.model.VisitedUsersModel;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.HangoutPresenter;
import co.quchu.quchu.presenter.InterestingDetailPresenter;
import co.quchu.quchu.presenter.NearbyPresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.adapter.QuchuDetailsAdapter;
import co.quchu.quchu.widget.ErrorView;

/**
 * InterestingDetailsActivity
 * User: Chenhs
 * Date: 2015-12-09
 * 趣处详情
 */
public class QuchuDetailsActivity extends BaseBehaviorActivity {

  @Override protected String getPageNameCN() {
    return getString(R.string.pname_quchu_details);
  }

  @Bind(R.id.detail_recyclerview) RecyclerView mRecyclerView;
  @Bind(R.id.ivShouCang) ImageView ivShouCang;
  @Bind(R.id.detail_bottom_group_ll) View detail_bottom_group_ll;
  @Bind(R.id.errorView) ErrorView errorView;

  public static final String REQUEST_KEY_PID = "pid";
  public static final String REQUEST_KEY_FROM = "from";
  private long startViewTime = 0L;
  private int pId = 0;
  private boolean mLoadingMore = false;
  public DetailModel dModel = new DetailModel();
  private QuchuDetailsAdapter mQuchuDetailAdapter;
  private VisitedInfoModel mVisitedInfoModel;
  private List<HangoutUserModel> mAvailableUsers;
  private View.OnClickListener mOnClickListener = new View.OnClickListener() {
    @Override public void onClick(View v) {
      detailClick(v);
    }
  };
  boolean mIsRatingRunning = false;
  boolean mIsLoadMoreRunning = false;
  private BitmapDescriptor mOverlayLocation;

  public static final String FROM_TYPE_HOME = "detail_home_t";//从智能推荐进来的
  public static final String FROM_TYPE_MAP = "map";//从智能推荐进来的
  public static final String FROM_TYPE_TAG = "detail_tag_t";//从其他类别进来的
  public static final String FROM_TYPE_SUBJECT = "detail_subject_t";//从发现进来的
  public static final String FROM_TYPE_RECOM = "detail_recommendation_t";//从详情页推荐进来的
  public static final String FROM_TYPE_PROFILE = "detail_profile_t";//从用户收藏进来的
  public static final String BUNDLE_KEY_DATA_MODEL = "BUNDLE_KEY_DATA_MODEL";
  private String from;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_quchu_details);
    ButterKnife.bind(this);
    from = getIntent().getStringExtra(REQUEST_KEY_FROM);
    getEnhancedToolbar().getTitleTv().setText("");

    if (null != savedInstanceState) {
      dModel = (DetailModel) savedInstanceState.getSerializable(BUNDLE_KEY_DATA_MODEL);
    }

    detail_bottom_group_ll.setVisibility(View.INVISIBLE);
    mRecyclerView.setVisibility(View.INVISIBLE);

    mQuchuDetailAdapter = new QuchuDetailsAdapter(this, dModel);
    mOverlayLocation = BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_pin_me);
    mRecyclerView.setLayoutManager(
        new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
    mRecyclerView.setAdapter(mQuchuDetailAdapter);

    startViewTime = System.currentTimeMillis();
    initData();
    getVisitors();
    getRatingInfo();
  }

  private void resetFavorite() {
    ivShouCang.setImageResource(
        dModel.isIsf() ? R.mipmap.ic_shoucang_yellow : R.mipmap.ic_shoucang_large);
  }

  @Override public ArrayMap<String, Object> getUserBehaviorArguments() {

    ArrayMap<String, Object> data = new ArrayMap<>();
    data.put("pid", getIntent().getIntExtra(REQUEST_KEY_PID, -1));
    return data;
  }

  @Override public int getUserBehaviorPageId() {
    return 104;
  }

  private void getVisitors() {
    InterestingDetailPresenter.getVisitedUsers(getApplicationContext(), pId,
        new CommonListener<VisitedUsersModel>() {
          @Override public void successListener(VisitedUsersModel response) {
            if (null != response) {
              mQuchuDetailAdapter.updateVisitedUsers();
            }
          }

          @Override public void errorListener(VolleyError error, String exception, String msg) {
          }
        });
  }

  private void getRatingInfo() {
    InterestingDetailPresenter.getVisitedInfo(getApplicationContext(), pId,
        new CommonListener<VisitedInfoModel>() {
          @Override public void successListener(VisitedInfoModel response) {
            if (null != response) {
              mVisitedInfoModel = response;
              mQuchuDetailAdapter.updateRatingInfo(mVisitedInfoModel);
            }
          }

          @Override public void errorListener(VolleyError error, String exception, String msg) {
          }
        });
  }

  @Override protected int activitySetup() {
    return TRANSITION_TYPE_LEFT;
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putSerializable(BUNDLE_KEY_DATA_MODEL, dModel);
  }

  private void initData() {
    pId = getIntent().getIntExtra(REQUEST_KEY_PID, -1);
    if (-1 == pId) {
      Toast.makeText(this, "该趣处已不存在!", Toast.LENGTH_SHORT).show();
    } else {

      if (dModel == null || StringUtils.isEmpty(dModel.getName())) {

        if (!NetUtil.isNetworkConnected(getApplicationContext())) {
          errorView.showViewDefault(new View.OnClickListener() {
            @Override public void onClick(View v) {
              getData();
            }
          });
        } else {
          getData();
        }
      } else {
        bindingDetailData(dModel);
      }
    }
  }

  private void getData() {

    DialogUtil.showProgess(this, "数据加载中...");
    InterestingDetailPresenter.getInterestingData(this, pId, new CommonListener<DetailModel>() {
      @Override public void successListener(DetailModel response) {
        DialogUtil.dismissProgess();
        bindingDetailData(response);
        errorView.hideView();
      }

      @Override public void errorListener(VolleyError error, String exception, String msg) {
        errorView.showViewDefault(new View.OnClickListener() {
          @Override public void onClick(View v) {
            DialogUtil.showProgess(QuchuDetailsActivity.this, "加载中");
            getData();
          }
        });
      }
    });
  }


  private void bindingDetailData(DetailModel model) {
    mRecyclerView.setVisibility(View.VISIBLE);
    detail_bottom_group_ll.setVisibility(View.VISIBLE);
    dModel.copyFrom(model);

    getEnhancedToolbar().getRightIv().setImageResource(R.mipmap.ic_home);
    getEnhancedToolbar().getRightIv().setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {

        UMEvent("back_c");
        ZGEvent("趣处名称", dModel.getName(), "返回首页");
        EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_GOTO_HOME_PAGE));
      }
    });
    if (null == mQuchuDetailAdapter) {
      return;
    }

    detail_bottom_group_ll.setVisibility(View.VISIBLE);
    mRecyclerView.setVisibility(View.VISIBLE);
    mQuchuDetailAdapter.notifyDataSetChanged();

    changeCollectState(dModel.isIsf());
    //initMap();
  }

  //private void initMap() {
  //  LatLng locationLatLnb =
  //      new LatLng(Double.valueOf(dModel.getLatitude()), Double.valueOf(dModel.getLongitude()));
  //  MapStatusUpdate status = MapStatusUpdateFactory.newLatLngZoom(locationLatLnb, 17f);
  //  mvMap.getMap().animateMapStatus(status);
  //  mvMap.getMap().getUiSettings().setAllGesturesEnabled(false);
  //  mvMap.showZoomControls(false);
  //  OverlayOptions optionMyLoc = new MarkerOptions().position(locationLatLnb)
  //      .anchor(.5f, .5f)
  //      .perspective(true)
  //      .draggable(false)
  //      .period(50)
  //      .icon(mOverlayLocation);
  //  mvMap.getMap().addOverlay(optionMyLoc);
  //
  //
  //  mvMap.postDelayed(new Runnable() {
  //    @Override public void run() {
  //      mvMap.getMap().snapshot(new BaiduMap.SnapshotReadyCallback() {
  //        @Override public void onSnapshotReady(Bitmap bitmap) {
  //          mQuchuDetailAdapter.updateMapBitmap(bitmap);
  //          DialogUtil.dismissProgess();
  //        }
  //      });
  //    }
  //  },2500);
  //
  //
  //
  //
  //}



  private void changeCollectState(boolean isCollect) {
    dModel.setIsf(isCollect);
    resetFavorite();
    mQuchuDetailAdapter.notifyDataSetChanged();
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
    InterestingDetailPresenter.updateRatingInfo(getApplicationContext(), dModel.getPid(), score,
        strTags, new InterestingDetailPresenter.DetailDataListener() {
          @Override public void onSuccessCall(String str) {
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

          @Override public void onErrorCall(String str) {
            mIsRatingRunning = false;
          }
        });
  }

  @OnClick({ R.id.ivShouCang, R.id.ivPreOrder, R.id.ivShare, R.id.ivPingJia })
  public void detailClick(View v) {
    if (!NetUtil.isNetworkConnected(this)) {
      Toast.makeText(QuchuDetailsActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
    }

    if (KeyboardUtils.isFastDoubleClick()) return;
    if (dModel != null) {
      switch (v.getId()) {
        case R.id.ivPreOrder:

          UMEvent("reserve_c");
          ZGEvent("趣处名称", dModel.getName(), "预定按钮");
          if (NetUtil.isNetworkConnected(getApplicationContext())) {
            if (null != dModel && null != dModel.getNet() && !StringUtils.isEmpty(
                dModel.getNet())) {
              WebViewActivity.enterActivity(QuchuDetailsActivity.this, dModel.getNet(),
                  dModel.getName(), false);
            } else {
              Toast.makeText(QuchuDetailsActivity.this, R.string.pre_order_not_supported,
                  Toast.LENGTH_SHORT).show();
            }
          } else {
            Toast.makeText(QuchuDetailsActivity.this, R.string.network_error, Toast.LENGTH_SHORT)
                .show();
          }
          //QuchuDetailsMoreDialog quchuDetailsMoreDialog = new QuchuDetailsMoreDialog(QuchuDetailsActivity.this);
          //quchuDetailsMoreDialog.setOnButtonClickListener(new QuchuDetailsMoreDialog.OnButtonClickListener() {
          //    @Override
          //    public void onReturnClick() {
          //
          //    }
          //
          //    @Override
          //    public void onPreOrderClick() {
          //
          //    }
          //
          //    @Override
          //    public void onShareClick() {
          //        UMEvent("share_c");
          //        ZGEvent("文章名称",dModel.getName(),"趣处分享");
          //        startActivity(ShareQuchuActivity.getStartIntent(QuchuDetailsActivity.this, dModel));
          //    }
          //});
          //quchuDetailsMoreDialog.show();
          break;

        case R.id.ivShare:
          UMEvent("share_c");
          ZGEvent("文章名称", dModel.getName(), "趣处分享");
          startActivity(ShareQuchuActivity.getStartIntent(QuchuDetailsActivity.this, dModel));

          break;
        case R.id.ivPingJia:

          if (null != mVisitedInfoModel) {
            UMEvent("comment_c");
            ZGEvent("趣处名称", dModel.getName(), "进入评价");
            AddFootprintActivity.enterActivity(QuchuDetailsActivity.this, mVisitedInfoModel,
                dModel.getPid(), getPageNameCN());
          }
          //RatingQuchuDialog tagsFilterDialog = RatingQuchuDialog.newInstance(mVisitedInfoModel.getUserCount(), mVisitedInfoModel.getScore(), mVisitedInfoModel.getResult());
          //tagsFilterDialog.show(getSupportFragmentManager(), "");
          //tagsFilterDialog.setPickingListener(new RatingQuchuDialog.OnFinishPickingListener() {
          //    @Override
          //    public void onFinishPicking(List<TagsModel> selection, int score) {
          //        if (null != selection) {
          //            ZGEvent("趣处名称",dModel.getName(),"提交评价");
          //            ratingQuchu(selection, score);
          //        }
          //    }
          //});
          break;
        case R.id.ivShouCang:
          //收藏
          UMEvent("like_c");
          ZGEvent("趣处名称", dModel.getName(), "收藏趣处");
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

        //case R.id.mvMap:
        //
        //  if (!NetUtil.isNetworkConnected(getApplicationContext())) {
        //    Toast.makeText(QuchuDetailsActivity.this, R.string.network_error, Toast.LENGTH_SHORT)
        //        .show();
        //  } else if (!dModel.isMap()) {
        //    Toast.makeText(QuchuDetailsActivity.this, "此趣处暂无导航信息!", Toast.LENGTH_SHORT).show();
        //  } else {
        //    EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_FINISH_MAP));
        //
        //    UMEvent("map_c");
        //    Intent mapIntent = new Intent(QuchuDetailsActivity.this, PlaceMapActivity.class);
        //    mapIntent.putExtra("pid", dModel.getPid());
        //    mapIntent.putExtra("lat", dModel.getLatitude());
        //    mapIntent.putExtra("lon", dModel.getLongitude());
        //    mapIntent.putExtra("gdlon", dModel.gdLongitude);
        //    mapIntent.putExtra("gdlat", dModel.gdLatitude);
        //    mapIntent.putExtra("title", dModel.getName());
        //    mapIntent.putExtra("entity", dModel.convert2NearbyMapItem());
        //    mapIntent.putExtra("placeAddress", dModel.getAddress());
        //    startActivity(mapIntent);
        //  }
        //
        //  break;
      }
    }
  }

  /**
   * 收藏
   */
  private void setFavorite() {
    InterestingDetailPresenter.setDetailFavorite(this, pId, dModel.isIsf(),
        new InterestingDetailPresenter.DetailDataListener() {
          @Override public void onSuccessCall(String str) {
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

            if (!TextUtils.isEmpty(from) && from.equals(FROM_TYPE_PROFILE)) {
              //从用户收藏列表进来,通知列表刷新
              setResult(RESULT_OK);
            }
          }

          @Override public void onErrorCall(String str) {

          }
        });
  }

  @Override public void onBackPressed() {
    super.onBackPressed();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
  }

  @Override public void onStart() {
    super.onStart();
    EventBus.getDefault().register(this);
  }

  @Override public void onStop() {
    EventBus.getDefault().unregister(this);
    super.onStop();
  }

  @Subscribe public void onMessageEvent(QuchuEventModel event) {

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
      case EventFlags.EVENT_CANCLE_FAVORITE_QUCHU:
        break;
      //case EventFlags.EVENT_POST_CARD_ADDED:
      //    if ((Integer) event.getContent()[0] == dModel.getPid()) {
      //        dModel.setCardCount(dModel.getCardCount() + 1);
      //    }
      //    break;
      //case EventFlags.EVENT_POST_CARD_DELETED:
      //    if (((Integer) event.getContent()[1]) == dModel.getPid() && dModel.getCardCount() > 1) {
      //        dModel.setCardCount(dModel.getCardCount() - 1);
      //    }
      //    break;
      case EventFlags.EVENT_GOTO_HOME_PAGE:
        finish();
        break;
    }
  }

  //    @Override
  //    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
  //        if (appbar.getTotalScrollRange()==0 || verticalOffset==0){
  //            return;
  //        }
  //        float alpha = Math.abs(Float.valueOf(verticalOffset))/appbar.getTotalScrollRange();
  //        getEnhancedToolbar().getTitleTv().setAlpha(alpha);
  //        int color = (int) (255-(alpha*255));
  //        getEnhancedToolbar().getLeftIv().setColorFilter(Color.argb(255,color,color,color), PorterDuff.Mode.MULTIPLY);
  //
  //
  //    }
}
