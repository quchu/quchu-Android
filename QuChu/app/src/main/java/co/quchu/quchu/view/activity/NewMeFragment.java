package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.im.MessageActivity;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.model.UserCenterInfo;
import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.MeActivityPresenter;
import co.quchu.quchu.presenter.UserCenterPresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.widget.CircleIndicator;

/**
 * 我的 TAB
 * <p/>
 * Created by mwb on 16/8/22.
 */
public class NewMeFragment extends BaseFragment {

    @Bind(R.id.me_viewpager)
    ViewPager viewpager;
    @Bind(R.id.me_indicator)
    CircleIndicator indicator;
    @Bind(R.id.friend_layout)
    LinearLayout friendLayout;
    @Bind(R.id.quchu_layout)
    LinearLayout quchuLayout;
    @Bind(R.id.unReadMassage)
    TextView unReadMassage;
    @Bind(R.id.massage_layout)
    RelativeLayout massageLayout;
    @Bind(R.id.feedback_layout)
    LinearLayout feedbackLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);

        viewpager.setAdapter(new MyViewPagerAdapter(getActivity().getSupportFragmentManager()));
        indicator.setViewPager(viewpager);

        getUnreadMessage();
        getUserCenterInfo();

        return view;
    }

    /**
     * 获取用户信息
     */
    private void getUserCenterInfo() {
        if (AppContext.user == null) {
            return;
        }

        int userId = AppContext.user.getUserId();

        UserCenterPresenter.getUserCenterInfo(getActivity(), userId, new UserCenterPresenter.UserCenterInfoCallBack() {
            @Override
            public void onSuccess(UserCenterInfo userCenterInfo) {
                if (userCenterInfo != null) {
                    String mark = userCenterInfo.getMark();
                    SPUtils.setUserMark(mark);
                }
            }

            @Override
            public void onError() {
            }
        });
    }

    /**
     * 获取未读消息
     */
    private void getUnreadMessage() {
        new MeActivityPresenter(getActivity()).getUnreadMassageCound(new CommonListener<Integer>() {
            @Override
            public void successListener(Integer response) {
                notReadMassage(response);
            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {
            }
        });
    }

    public void notReadMassage(int cound) {
        unReadMassage.setText(String.valueOf(cound));
        unReadMassage.setVisibility(View.VISIBLE);
    }

    @Override
    protected String getPageNameCN() {
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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
            case EventFlags.EVENT_DEVICE_NETWORK_AVAILABLE:
                getUnreadMessage();
                break;
        }
    }

    @OnClick({R.id.friend_layout, R.id.quchu_layout, R.id.massage_layout, R.id.feedback_layout})
    public void onClick(View view) {
        UserInfoModel user = AppContext.user;
        switch (view.getId()) {
            case R.id.friend_layout:
                //趣友圈
                UMEvent("community_c");
                if (user.isIsVisitors()) {
                    ((BaseActivity) getActivity()).showLoginDialog();
                } else {
                    startActivity(QuFriendsActivity.class);
                }
                break;

            case R.id.quchu_layout:
                //收藏
                UMEvent("collection_c");
                startActivity(FavoriteActivity.class);
                break;

            case R.id.massage_layout:
                //消息
                UMEvent("message_c");
                startActivity(MessageActivity.class);
                unReadMassage.setVisibility(View.INVISIBLE);
                break;

            case R.id.feedback_layout:
                //意见与反馈
                startActivity(FeedbackActivity.class);
                break;
        }
    }

    private class MyViewPagerAdapter extends FragmentPagerAdapter {

        private final MeAvatarFragment avatarFragment;
        private final MeGenFragment genFragment;

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);

            avatarFragment = new MeAvatarFragment();
            genFragment = new MeGenFragment();
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return avatarFragment;
            }
            return genFragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}