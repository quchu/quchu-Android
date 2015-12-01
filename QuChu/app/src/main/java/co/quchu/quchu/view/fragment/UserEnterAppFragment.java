package co.quchu.quchu.view.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.view.activity.UserLoginActivity;
import co.quchu.quchu.widget.CircleWaveView;

/**
 * UserEnterAppFragment
 * User: Chenhs
 * Date: 2015-11-30
 */
public class UserEnterAppFragment extends Fragment {

    @Bind(R.id.user_enter_app_circle_iv)
    SimpleDraweeView userEnterAppCircleIv;
    @Bind(R.id.user_enter_app_cwv)
    CircleWaveView userEnterAppCwv;
    @Bind(R.id.user_enter_app_btn)
    Button userEnterAppBtn;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_enter_app, null);
        ButterKnife.bind(this, view);

        if (null != AppContext.user) {
            userEnterAppCircleIv.setImageURI(Uri.parse(AppContext.user.getPhoto()));
            LogUtils.json(AppContext.user.getPhoto()+"      imageurl");
        }
        return view;
    }

    @OnClick(R.id.user_enter_app_btn)
    public void enterApp(View view) {
        ((UserLoginActivity) getActivity()).enterApp();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
