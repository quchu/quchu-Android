package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import co.quchu.quchu.model.ImageModel;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.DetailModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.model.VisitedInfoModel;
import co.quchu.quchu.model.VisitedUsersModel;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.InterestingDetailPresenter;
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
  private int pId = 0;
  public DetailModel dModel = new DetailModel();
  private QuchuDetailsAdapter mQuchuDetailAdapter;
  private VisitedInfoModel mVisitedInfoModel;
  private CommentListFragment mCommentListFragment;

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
    mRecyclerView.setLayoutManager(
        new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
    mRecyclerView.setAdapter(mQuchuDetailAdapter);

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

      mCommentListFragment = new CommentListFragment();
      Bundle bundle = new Bundle();
      bundle.putInt(CommentListFragment.BUNDLE_KEY_PLACE_ID,pId);
      mCommentListFragment.setArguments(bundle);
      getSupportFragmentManager().beginTransaction().add(R.id.flContainer,mCommentListFragment).commitAllowingStateLoss();

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

    if (null != dModel && null != dModel.getImglist() && dModel.getImglist().size() > 0) {

      List<ImageModel> galleryImgs = new ArrayList<>();
      for (int i = 0; i < dModel.getImglist().size(); i++) {
        galleryImgs.add(dModel.getImglist().get(i).convert2ImageModel());
      }

      mQuchuDetailAdapter.updateGallery(galleryImgs);
    }

    changeCollectState(dModel.isIsf());
    mQuchuDetailAdapter.notifyDataSetChanged();

    mRecyclerView.postDelayed(new Runnable() {
      @Override public void run() {
        mQuchuDetailAdapter.notifyItemChanged(0);
      }
    }, 1000);
  }

  private void changeCollectState(boolean isCollect) {
    dModel.setIsf(isCollect);
    resetFavorite();
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

          break;
        case R.id.ivShouCang:
          //收藏
          UMEvent("like_c");
          ZGEvent("趣处名称", dModel.getName(), "收藏趣处");
          setFavorite();
          break;
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
      case EventFlags.EVENT_GOTO_HOME_PAGE:
        finish();
        break;
    }
  }
}
