package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.VisitorLoginDialogFg;
import co.quchu.quchu.model.UserCenterInfo;
import co.quchu.quchu.presenter.UserCenterPresenter;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.widget.pull2zoomview.PullToZoomScrollViewEx;

/**
 * UserCenterActivity
 * User: Chenhs
 * Date: 2016-02-23
 */
public class UserCenterActivity extends BaseActivity implements View.OnClickListener {
    TextView userCenterFollowingTv;
    TextView userCenterFollowedTv;
    TextView userCenterPnumTv;
    TextView userCenterFavoritenumTv;
    SimpleDraweeView ivZoom;
    SimpleDraweeView userCenterUserIconSdv;
    TextView userCenterUserNicknameTv;
    TextView userCenterDescTv;
    TextView userCenterFoucsableTv;
    private PullToZoomScrollViewEx scrollView;
    private int userId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
        initTitleBar();
        ((TextView) findViewById(R.id.title_content_tv)).setText(getTitle());
        loadViewForCode();
        userId = getIntent().getIntExtra("USERID", 0);
        scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);
        initView();
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) ((0.618F * mScreenHeight) - getResources().getDimension(R.dimen.title_bar_heigh)));
        scrollView.setHeaderLayoutParams(localObject);
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    private void initView() {
        ivZoom = (SimpleDraweeView) scrollView.getZoomView().findViewById(R.id.iv_zoom);
        userCenterUserIconSdv = (SimpleDraweeView) scrollView.getHeaderView().findViewById(R.id.user_center_user_icon_sdv);
        userCenterFoucsableTv = (TextView) scrollView.getHeaderView().findViewById(R.id.user_center_foucsable_tv);
        userCenterUserNicknameTv = (TextView) scrollView.getHeaderView().findViewById(R.id.user_center_nickname_tv);
        userCenterDescTv = (TextView) scrollView.getHeaderView().findViewById(R.id.user_center_desc_tv);
        userCenterFollowingTv = (TextView) scrollView.getRootView().findViewById(R.id.user_center_following_tv);
        userCenterFollowedTv = (TextView) scrollView.getRootView().findViewById(R.id.user_center_followed_tv);
        userCenterPnumTv = (TextView) scrollView.getRootView().findViewById(R.id.user_center_pnum_tv);
        userCenterFavoritenumTv = (TextView) scrollView.getRootView().findViewById(R.id.user_center_favoritenum_tv);
        initData();
    }

    private UserCenterInfo userInfo;

    private void initData() {
        UserCenterPresenter.getUserCenterInfo(this, userId, new UserCenterPresenter.UserCenterInfoCallBack() {
            @Override
            public void onSuccess(UserCenterInfo userCenterInfo) {
                userInfo = userCenterInfo;
                userInfo.userId = userId;
                if (userInfo != null)
                    bindView();
            }

            @Override
            public void onError() {
                //   Toast.makeText(UserCenterActivity.this, "数据获取异常请稍后重试！", Toast.LENGTH_SHORT).show();
                //     UserCenterActivity.this.finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_center_favorite_ll:
                startActivity(new Intent(UserCenterActivity.this, FavoritePlaceActivity.class).putExtra("userId", userInfo.userId));
                break;
            case R.id.user_center_postcard_ll:
                startActivity(new Intent(UserCenterActivity.this, UserPostCardActivity.class).putExtra("userId", userInfo.userId));
                break;
            case R.id.user_center_following_tv:
                startActivity(new Intent(UserCenterActivity.this, FollowingActivity.class).putExtra("UserId", userInfo.userId).putExtra("FollowType", FollowingActivity.TAFOLLOWING));
                break;
            case R.id.user_center_followed_tv:
                startActivity(new Intent(UserCenterActivity.this, FollowingActivity.class).putExtra("UserId", userInfo.userId).putExtra("FollowType", FollowingActivity.TAFOLLOWERS));

                break;
            case R.id.user_center_foucsable_tv:
                followSomebody();
                break;

        }
        super.onClick(v);
    }

    private void bindView() {
       /* if (!StringUtils.isEmpty(userInfo.getBackImg()))
            ivZoom.setImageURI(Uri.parse(userInfo.getBackImg()));*/

        userCenterUserIconSdv.setImageURI(Uri.parse(userInfo.getPhoto()));
        updateIsFollow();
        userCenterPnumTv.setText(userInfo.getCardNum() + "");
        userCenterFavoritenumTv.setText(userInfo.getFovPlaceNum() + "");
        userCenterUserNicknameTv.setText(userInfo.getName() + "");
        if (null != userInfo.getLocation() && StringUtils.isEmpty(userInfo.getLocation().toString())) {
            userCenterDescTv.setText(String.format(getResources().getString(R.string.usercenter_desc_text, userInfo.getGender(), userInfo.getLocation())));
        } else {
            userCenterDescTv.setText(userInfo.getGender());
        }
        userCenterFollowedTv.setText(String.format(getResources().getString(R.string.usercenter_follow_text), userInfo.getFollowNum()));
        userCenterFollowingTv.setText(String.format(getResources().getString(R.string.usercenter_host_text), userInfo.getHostNum()));
        scrollView.getRootView().findViewById(R.id.user_center_postcard_ll).setOnClickListener(this);
        scrollView.getRootView().findViewById(R.id.user_center_favorite_ll).setOnClickListener(this);
        userCenterFollowingTv.setOnClickListener(this);
        userCenterFollowedTv.setOnClickListener(this);
        userCenterFoucsableTv.setOnClickListener(this);

    }

    private void loadViewForCode() {
        PullToZoomScrollViewEx scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);
        View headView = LayoutInflater.from(this).inflate(R.layout.usercenter_head_view, null, false);
        View zoomView = LayoutInflater.from(this).inflate(R.layout.usercenter_zoom_view, null, false);
        View contentView = LayoutInflater.from(this).inflate(R.layout.usercenter_content_view, null, false);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);
    }

    private void updateIsFollow() {
        if (userInfo.isIsFollow()) {
            userCenterFoucsableTv.setText(getResources().getString(R.string.usercenter_foucsed_text));
            userCenterFoucsableTv.setTextColor(getResources().getColor(R.color.black));
            userCenterFoucsableTv.setBackground(getResources().getDrawable(R.drawable.shape_usercenter_foucsed));

        } else {
            userCenterFoucsableTv.setText(getResources().getString(R.string.usercenter_foucs_text));
            userCenterFoucsableTv.setTextColor(getResources().getColor(R.color.gene_textcolor_yellow));
            userCenterFoucsableTv.setBackground(getResources().getDrawable(R.drawable.shape_usercenter_unfoucs));
        }
    }

    private void followSomebody() {
        if (AppContext.user.isIsVisitors()) {
            VisitorLoginDialogFg fg = VisitorLoginDialogFg.newInstance(VisitorLoginDialogFg.QFOCUS);
            fg.show(getFragmentManager(), "QFocus");
        } else {
            UserCenterPresenter.followSbd(this, userInfo.isIsFollow(), userInfo.userId, new UserCenterPresenter.UserCenterInfoCallBack() {
                @Override
                public void onSuccess(UserCenterInfo userCenterInfo) {
                    userInfo.setIsFollow(!userInfo.isIsFollow());
                    updateIsFollow();
                }

                @Override
                public void onError() {

                }
            });
        }
    }
}