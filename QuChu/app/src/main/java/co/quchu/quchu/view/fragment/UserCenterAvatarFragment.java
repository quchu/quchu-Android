package co.quchu.quchu.view.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.model.UserCenterInfo;

/**
 * Created by mwb on 16/9/9.
 */
public class UserCenterAvatarFragment extends BaseFragment {

  private static String USER_MODEL_BUNDLE_KEY = "user_model_bundle_key";

  @Bind(R.id.user_avatar_img) SimpleDraweeView mUserAvatarImg;
  @Bind(R.id.user_gender_img) SimpleDraweeView mUserGenderImg;
  @Bind(R.id.user_name_tv) TextView mUserNameTv;
  @Bind(R.id.user_mark_img) ImageView mUserMarkImg;
  @Bind(R.id.user_mark_tv) TextView mUserMarkTv;
  @Bind(R.id.user_mark_layout) LinearLayout mUserMarkLayout;

  public static UserCenterAvatarFragment newInstance(UserCenterInfo userCenterInfo) {
    UserCenterAvatarFragment fragment = new UserCenterAvatarFragment();
    Bundle bundle = new Bundle();
    bundle.putSerializable(USER_MODEL_BUNDLE_KEY, userCenterInfo);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_user_center_avatar, container, false);
    ButterKnife.bind(this, view);

    UserCenterInfo userCenterInfo = (UserCenterInfo) getArguments().getSerializable(USER_MODEL_BUNDLE_KEY);
    initViews(userCenterInfo);

    return view;
  }

  private void initViews(UserCenterInfo userCenterInfo) {
    if (userCenterInfo == null) {
      return;
    }

    mUserAvatarImg.setImageURI(Uri.parse(userCenterInfo.getPhoto()));
    mUserNameTv.setText(userCenterInfo.getName());
    mUserGenderImg.setImageURI(Uri.parse(
        "res:///" + (userCenterInfo.getGender().equals("男") ? R.drawable.ic_male
            : R.drawable.ic_female)));

    String userMark = userCenterInfo.getMark();
    if (userMark != null) {
      mUserMarkLayout.setVisibility(View.VISIBLE);
      mUserMarkTv.setText(userMark);

      switch (userMark) {
        case "小食神":
          mUserMarkImg.setImageResource(R.drawable.ic_chihuo_ahsy);
          break;

        case "艺术家":
          mUserMarkImg.setImageResource(R.drawable.ic_wenyi_ahsy);
          break;

        case "外交官":
          mUserMarkImg.setImageResource(R.drawable.ic_shejiao_ahsy);
          break;

        case "时尚精":
          mUserMarkImg.setImageResource(R.drawable.ic_shishang_ahsy);
          break;

        case "大财阀":
          mUserMarkImg.setImageResource(R.drawable.ic_tuhao_ahsy);
          break;

        case "玩乐咖":
          mUserMarkImg.setImageResource(R.drawable.ic_haoqi_ahsy);
          break;
      }
    }
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
}
