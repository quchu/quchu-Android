package co.quchu.quchu.view.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.widget.MoreButtonView;

/**
 * AccountSettingActivity
 * User: Chenhs
 * Date: 2015-12-04
 */
public class AccountSettingActivity extends BaseActivity {
    @Bind(R.id.title_back_iv)
    ImageView titleBackIv;
    @Bind(R.id.title_back_rl)
    RelativeLayout titleBackRl;
    @Bind(R.id.title_content_tv)
    TextView titleContentTv;
    @Bind(R.id.title_more_rl)
    MoreButtonView titleMoreRl;
    @Bind(R.id.account_setting_avatar_sdv)
    SimpleDraweeView accountSettingAvatarSdv;
    @Bind(R.id.account_setting_avatar_editer_tv)
    TextView accountSettingAvatarEditerTv;
    @Bind(R.id.account_setting_nickname_et)
    EditText accountSettingNicknameEt;
    @Bind(R.id.account_setting_gender_tv)
    TextView accountSettingGenderTv;
    @Bind(R.id.account_setting_phone_tv)
    TextView accountSettingPhoneTv;
    @Bind(R.id.account_setting_user_location)
    TextView accountSettingUserLocation;
    @Bind(R.id.account_setting_new_pwd_et)
    EditText accountSettingNewPwdEt;
    @Bind(R.id.account_setting_new_pwd_again_et)
    EditText accountSettingNewPwdAgainEt;
    @Bind(R.id.account_setting_bind_weibo_tv)
    TextView accountSettingBindWeiboTv;
    @Bind(R.id.account_setting_bind_wechat_tv)
    TextView accountSettingBindWechatTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        ButterKnife.bind(this);
        initTitleBar();
        titleContentTv.setText(getTitle());
        userInfoBinding();
    }

    private void userInfoBinding() {
        titleContentTv.setText(getTitle());
        if (AppContext.user == null) {
            if (!StringUtils.isEmpty(SPUtils.getUserInfo(this)))
                AppContext.user = new Gson().fromJson(SPUtils.getUserInfo(this), UserInfoModel.class);
        }

        if (AppContext.user != null) {
            accountSettingAvatarSdv.setImageURI(Uri.parse(AppContext.user.getPhoto()));
            accountSettingNicknameEt.setText(AppContext.user.getFullname());
            accountSettingPhoneTv.setText(AppContext.user.getUsername());
            accountSettingGenderTv.setText(AppContext.user.getGender());
        }
    }

    @OnClick({R.id.account_setting_avatar_editer_tv, R.id.account_setting_gender_tv})
    public void accountClick(View v) {
        switch (v.getId()) {
            case R.id.account_setting_avatar_editer_tv:
                break;

        }
    }
}
