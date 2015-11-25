package co.quchu.quchu.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.activity.UserLoginActivity;
import co.quchu.quchu.widget.CircleWaveView;

/**
 * UserLoginMainFragment
 * User: Chenhs
 * Date: 2015-11-25
 */
public class UserLoginMainFragment extends Fragment {
    @Bind(R.id.user_login_main_circle_iv)
    ImageView userLoginMainCircleIv;
    @Bind(R.id.user_login_main_cwv)
    CircleWaveView userLoginMainCwv;
    @Bind(R.id.user_login_main_weibo_tv)
    TextView userLoginMainWeiboTv;
    @Bind(R.id.user_login_main_weibo_ll)
    LinearLayout userLoginMainWeiboLl;
    @Bind(R.id.user_login_main_wechat_tv)
    TextView userLoginMainWechatTv;
    @Bind(R.id.user_login_main_wechat_ll)
    LinearLayout userLoginMainWechatLl;
    @Bind(R.id.user_login_main_phone_ll)
    LinearLayout userLoginMainPhoneLl;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_login_main, null);
        ButterKnife.bind(this, view);
        StringUtils.alterTextColor(userLoginMainWeiboTv, 0, 4, R.color.white);
        StringUtils.alterTextColor(userLoginMainWechatTv, 0, 2, R.color.white);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.user_login_main_phone_ll, R.id.user_login_main_wechat_ll, R.id.user_login_main_weibo_ll})
    public void onLoginClick(View view) {
        switch (view.getId()) {
            case R.id.user_login_main_phone_ll:
                ((UserLoginActivity) getActivity()).clickPhone();
                break;
            case R.id.user_login_main_wechat_ll:

                break;
            case R.id.user_login_main_weibo_ll:

                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
