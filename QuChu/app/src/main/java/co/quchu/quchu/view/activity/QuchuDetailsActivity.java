package co.quchu.quchu.view.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.widget.Toast;
import com.android.volley.VolleyError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.DetailModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.InterestingDetailPresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.fragment.CommentListFragment;
import co.quchu.quchu.view.fragment.QuchuDetailFragment;
import co.quchu.quchu.widget.ErrorView;

import static co.quchu.quchu.R.id.detail_bottom_group_ll;
import static co.quchu.quchu.R.id.errorView;
import static co.quchu.quchu.R.id.llFavorite;
import static co.quchu.quchu.R.id.tvFavorite;

/**
 * InterestingDetailsActivity
 * User: Chenhs
 * Date: 2015-12-09
 * 趣处详情
 */
public class QuchuDetailsActivity extends BaseBehaviorActivity {

  public static final String FROM_TYPE_HOME = "detail_home_t";//从智能推荐进来的
  public static final String FROM_TYPE_MAP = "map";//从智能推荐进来的
  public static final String FROM_TYPE_TAG = "detail_tag_t";//从其他类别进来的
  public static final String FROM_TYPE_SUBJECT = "detail_subject_t";//从发现进来的
  public static final String FROM_TYPE_RECOM = "detail_recommendation_t";//从详情页推荐进来的
  public static final String FROM_TYPE_PROFILE = "detail_profile_t";//从用户收藏进来的
  public static final String BUNDLE_KEY_DATA_MODEL = "BUNDLE_KEY_DATA_MODEL";
  private String from;

  public static final String REQUEST_KEY_PID = "pid";
  public static final String REQUEST_KEY_FROM = "from";
  private int pId = 0;
  public DetailModel dModel = new DetailModel();

  private QuchuDetailFragment mDetailFragment;
  private CommentListFragment mCommentListFragment;

  @Bind(R.id.quchu_detail_tab_layout) TabLayout mTabLayout;
  @Bind(R.id.quchu_detail_view_pager) ViewPager mViewPager;
  @Bind(R.id.ivPingJia) ImageView mIvPingJia;
  @Bind(R.id.ivShare) ImageView mIvShare;
  @Bind(R.id.ivPreOrder) ImageView mIvPreOrder;
  @Bind(tvFavorite) TextView mTvFavorite;
  @Bind(errorView) ErrorView mErrorView;
  @Bind(llFavorite) LinearLayout mLlFavorite;
  @Bind(detail_bottom_group_ll) LinearLayout mDetailBottomGroupLl;

  @Override public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    onRestart();
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_quchu_details);
    ButterKnife.bind(this);

    from = getIntent().getStringExtra(REQUEST_KEY_FROM);

    initToolbar();

    if (null != savedInstanceState) {
      dModel = (DetailModel) savedInstanceState.getSerializable(BUNDLE_KEY_DATA_MODEL);
      bindingDetailData(dModel);
    } else {
      initData();
    }

    getVisitors();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putSerializable(BUNDLE_KEY_DATA_MODEL, dModel);
  }

  private void initToolbar() {
    getEnhancedToolbar().getTitleTv().setText("");
    getEnhancedToolbar().getRightIv().setImageResource(R.drawable.ic_home);
    getEnhancedToolbar().getRightIv().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        UMEvent("back_c");
        ZGEvent("趣处名称", dModel.getName(), "返回首页");
        EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_GOTO_HOME_PAGE));
      }
    });
  }

  private void initViewPager() {
    QuchuDetailViewPagerAdapter viewPagerAdapter = new QuchuDetailViewPagerAdapter(getSupportFragmentManager());
    mViewPager.setAdapter(viewPagerAdapter);

    mTabLayout.setupWithViewPager(mViewPager);
  }

  private void initData() {
    pId = getIntent().getIntExtra(REQUEST_KEY_PID, -1);
    if (-1 == pId) {
      makeToast("该趣处已不存在!");
    } else {

      if (dModel == null || StringUtils.isEmpty(dModel.getName())) {

        if (!NetUtil.isNetworkConnected(getApplicationContext())) {
          mErrorView.showViewDefault(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    DialogUtil.showProgress(this, "数据加载中...");
    InterestingDetailPresenter.getInterestingData(this, pId, new CommonListener<DetailModel>() {
      @Override
      public void successListener(DetailModel response) {
        DialogUtil.dismissProgress();
        bindingDetailData(response);

        mErrorView.hideView();
      }

      @Override
      public void errorListener(VolleyError error, String exception, String msg) {
        mErrorView.showViewDefault(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            DialogUtil.showProgress(QuchuDetailsActivity.this, "加载中");
            getData();
          }
        });
      }
    });
  }

  private void bindingDetailData(DetailModel model) {
    mDetailBottomGroupLl.setVisibility(View.VISIBLE);
    dModel.copyFrom(model);

    changeCollectState(dModel.isIsf());

    mDetailFragment = new QuchuDetailFragment();
    Bundle detailBundle = new Bundle();
    detailBundle.putSerializable(QuchuDetailFragment.BUNDLE_KEY_DETAIL_MODEL, dModel);
    mDetailFragment.setArguments(detailBundle);

    mCommentListFragment = new CommentListFragment();
    Bundle commentBundle = new Bundle();
    commentBundle.putInt(CommentListFragment.BUNDLE_KEY_PLACE_ID, pId);
    commentBundle.putInt(CommentListFragment.BUNDLE_KEY_RATING_COUNT, dModel.getPlaceReviewCount());
    commentBundle.putFloat(CommentListFragment.BUNDLE_KEY_AVG_RATING, dModel.getSuggest());
    commentBundle.putFloat(CommentListFragment.BUNDLE_KEY_RECENT_RATING, dModel.getRecentSuggest());
    commentBundle.putSerializable(CommentListFragment.BUNDLE_KEY_BIZ_LIST,
        (Serializable) dModel.getReviewGroupList());
    commentBundle.putSerializable(CommentListFragment.BUNDLE_KEY_TAG_LIST,
        (Serializable) dModel.getReviewTagList());
    mCommentListFragment.setArguments(commentBundle);

    initViewPager();
  }

  private void changeCollectState(boolean isCollect) {
    dModel.setIsf(isCollect);
    resetFavorite();
  }

  private void resetFavorite() {
    mLlFavorite.setBackgroundResource(!dModel.isIsf() ? R.color.colorChristmasDark : R.color.colorHint);
    mTvFavorite.setTextColor(!dModel.isIsf() ? getResources().getColor(R.color.standard_color_white) : getResources().getColor(R.color.standard_color_white));
    mTvFavorite.setText(dModel.isIsf() ? R.string.cancel_favorite : R.string.favorite);
  }

  private void getVisitors() {
    //InterestingDetailPresenter.getVisitedUsers(getApplicationContext(), pId,
    //    new CommonListener<VisitedUsersModel>() {
    //      @Override
    //      public void successListener(VisitedUsersModel response) {
    //        if (null != response) {
    //
    //        }
    //      }
    //
    //      @Override
    //      public void errorListener(VolleyError error, String exception, String msg) {}
    //    });
  }

  @OnClick({llFavorite, R.id.ivPreOrder, R.id.ivShare, R.id.ivPingJia})
  public void onClick(View v) {
    if (!NetUtil.isNetworkConnected(this)) {
      makeToast(R.string.network_error);
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
              makeToast(R.string.pre_order_not_supported);
            }
          } else {
            makeToast(R.string.network_error);
          }

          break;

        case R.id.ivShare:
          UMEvent("share_c");
          ZGEvent("文章名称", dModel.getName(), "趣处分享");
          startActivity(ShareQuchuActivity.getStartIntent(QuchuDetailsActivity.this, dModel));

          break;
        case R.id.ivPingJia:

          UMEvent("comment_c");
          ZGEvent("趣处名称", dModel.getName(), "进入评价");
          AddFootprintActivity.enterActivity(QuchuDetailsActivity.this, null,
              dModel.getPid(), getPageNameCN());

          break;
        case llFavorite:
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
          @Override
          public void onSuccessCall(String str) {
            dModel.setIsf(!dModel.isIsf());
            changeCollectState(dModel.isIsf());
            if (AppContext.selectedPlace != null) {
              AppContext.selectedPlace.setIsf(dModel.isIsf());
              AppContext.dCardListNeedUpdate = true;
            }
            if (dModel.isIsf()) {
              makeToast("收藏成功!");
            } else {
              makeToast("取消收藏!");
            }

            if (!TextUtils.isEmpty(from) && from.equals(FROM_TYPE_PROFILE)) {
              //从用户收藏列表进来,通知列表刷新
              setResult(RESULT_OK);
            }
          }

          @Override
          public void onErrorCall(String str) {

          }
        });
  }

  @Override
  public ArrayMap<String, Object> getUserBehaviorArguments() {
    return null;
  }

  @Override
  public int getUserBehaviorPageId() {
    return 104;
  }

  @Override
  protected String getPageNameCN() {
    return "";
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
        getVisitors();
        break;
      case EventFlags.EVENT_CANCLE_FAVORITE_QUCHU:
        break;
      case EventFlags.EVENT_GOTO_HOME_PAGE:
        startActivity(RecommendActivity.class);
        break;
    }
  }

  /**
   * ViewPager 适配器
   */
  private class QuchuDetailViewPagerAdapter extends FragmentPagerAdapter {

    public QuchuDetailViewPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      if (position == 0) {
        return mDetailFragment;
      }
      return mCommentListFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      if (position == 0) {
        return "趣处";
      }
      return "评论";
    }

    @Override
    public int getCount() {
      return 2;
    }
  }
}
