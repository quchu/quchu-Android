package co.quchu.quchu.view.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.galleryfinal.CoreConfig;
import co.quchu.galleryfinal.FunctionConfig;
import co.quchu.galleryfinal.GalleryFinal;
import co.quchu.galleryfinal.model.PhotoInfo;
import co.quchu.quchu.BuildConfig;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.ASUserPhotoDialogFg;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.dialog.GenderSelectedDialogFg;
import co.quchu.quchu.dialog.LocationSettingDialogFg;
import co.quchu.quchu.dialog.QAvatarSettingDialogFg;
import co.quchu.quchu.model.CityModel;
import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.photoselected.FrescoImageLoader;
import co.quchu.quchu.presenter.AccountSettingPresenter;
import co.quchu.quchu.thirdhelp.UserInfoHelper;
import co.quchu.quchu.utils.AppKey;
import co.quchu.quchu.utils.ImageUtils;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;

/**
 * AccountSettingActivity
 * User: Chenhs
 * Date: 2015-12-04
 */
public class AccountSettingActivity extends BaseActivity {
    @Bind(R.id.title_content_tv)
    TextView titleContentTv;
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
    @Bind(R.id.account_setting_location_iv)
    ImageView accountSettingLocationIv;
    @Bind(R.id.account_setting_save_tv)
    TextView accountSettingSaveTv;

    private ArrayList<Integer> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        ButterKnife.bind(this);
        initTitleBar();
        userInfoBinding();
        imageList = AccountSettingPresenter.getQAvatar();

    }

    private void userInfoBinding() {
        titleContentTv.setText(getTitle());
        if (AppContext.user == null) {
            if (!StringUtils.isEmpty(SPUtils.getUserInfo(this)))
                AppContext.user = new Gson().fromJson(SPUtils.getUserInfo(this), UserInfoModel.class);
        }

        if (AppContext.user != null) {
            accountSettingAvatarSdv.setImageURI(Uri.parse(AppContext.user.getPhoto()));
            accountSettingNicknameEt.setHint(AppContext.user.getFullname());
            accountSettingPhoneTv.setText(AppContext.user.getUsername());
            newUserGender = AppContext.user.getGender();
            accountSettingGenderTv.setText(newUserGender);
            //   SPUtils.getValueFromSPMap(this, AppKey.LOCATION_CITY)
            newUserLocation = AppContext.user.getLocation();
            accountSettingUserLocation.setText(newUserLocation);
        }

    }

    ArrayList<CityModel> genderList;

    @OnClick({R.id.account_setting_avatar_sdv, R.id.account_setting_avatar_editer_tv, R.id.account_setting_gender_tv
            , R.id.account_setting_save_tv, R.id.account_setting_user_location, R.id.account_setting_location_iv})
    public void accountClick(View v) {
        switch (v.getId()) {
            case R.id.account_setting_avatar_sdv:
            case R.id.account_setting_avatar_editer_tv:
                ASUserPhotoDialogFg photoDialogFg = ASUserPhotoDialogFg.newInstance();
                photoDialogFg.setOnOriginListener(listener);
                photoDialogFg.show(getFragmentManager(), "photo");
                break;
            case R.id.account_setting_save_tv:
                saveUserChange();
                break;
            case R.id.account_setting_gender_tv:
                genderList = new ArrayList<>();
                genderList.add(new CityModel("男", 0, "男".equals(newUserGender)));
                genderList.add(new CityModel("女", 0, "女".equals(newUserGender)));
                GenderSelectedDialogFg genderDialogFg = GenderSelectedDialogFg.newInstance(genderList);
                genderDialogFg.show(getFragmentManager(), "gender");
                break;
            case R.id.account_setting_user_location:
            case R.id.account_setting_location_iv:
                LocationSettingDialogFg locationDIalogFg = LocationSettingDialogFg.newInstance();
                locationDIalogFg.show(getFragmentManager(), "location");
                break;
        }
    }

    private final int REQUEST_CODE_GALLERY = 0x01;
    private final int REQUEST_CODE_CAMERA = 0x02;

    //选中头像dialog 点击回调
    public ASUserPhotoDialogFg.UserPhotoOriginSelectedListener listener = new ASUserPhotoDialogFg.UserPhotoOriginSelectedListener() {
        @Override
        public void selectedCamare() {
            initGralley();
            GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, mOnHanlderResultCallback);
        }

        @Override
        public void selectedAblum() {
            initGralley();
            GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);
        }

        @Override
        public void selectedQuPhtot() {
            if (imageList != null) {
                QAvatarSettingDialogFg qAvatarDIalogFg = QAvatarSettingDialogFg.newInstance(imageList);
                qAvatarDIalogFg.show(getFragmentManager(), "qAvatar");
            }
        }
    };
    private FunctionConfig functionConfig;

    private void initGralley() {
        FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
        functionConfigBuilder.setEnableEdit(false);
        functionConfigBuilder.setEnableCrop(true);
        functionConfigBuilder.setEnablePreview(true);
        functionConfigBuilder.setForceCrop(false);//启动强制裁剪功能,一进入编辑页面就开启图片裁剪，不需要用户手动点击裁剪，此功能只针对单选操作
        functionConfigBuilder.setForceCropEdit(true);
        functionConfigBuilder.setRotateReplaceSource(true);
        functionConfig = functionConfigBuilder.build();
        CoreConfig coreConfig = new CoreConfig.Builder(this, new FrescoImageLoader(this), null)
                .setDebug(BuildConfig.DEBUG)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(null)
                .setNoAnimcation(true)
                .build();
        GalleryFinal.init(coreConfig);
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null && resultList.size() > 0 && !StringUtils.isEmpty(resultList.get(0).getPhotoPath())) {
                updateAvatar(resultList.get(0).getPhotoPath());
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(AccountSettingActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    private String newUserPhoto = "";
    private String newUserGender = "";
    private String newUserNickName = "";
    private String newUserLocation = "";

    @Override
    public void onDestroy() {
        super.onDestroy();
        GalleryFinal.cleanCacheFile();
    }

    private String newUserPw = "", newUserPwAgain = "";

    //保存修改信息
    public void saveUserChange() {
        newUserPw = accountSettingNewPwdEt.getText().toString();
        newUserPwAgain = accountSettingNewPwdAgainEt.getText().toString();
        newUserNickName = StringUtils.isEmpty(accountSettingNicknameEt.getText().toString()) ? AppContext.user.getFullname() : accountSettingNicknameEt.getText().toString();
        newUserGender = accountSettingGenderTv.getText().toString();
        newUserLocation = accountSettingUserLocation.getText().toString();
        if ((StringUtils.isEmpty(newUserPw) && StringUtils.isEmpty(newUserPwAgain)) || (!StringUtils.isEmpty(newUserPw) && !StringUtils.isEmpty(newUserPwAgain) && newUserPwAgain.equals(newUserPw))) {
            if (newUserPw.length() < 6) {
                Toast.makeText(this, "密码长度必须大于6位", Toast.LENGTH_SHORT).show();
            }
            DialogUtil.showProgess(this, R.string.loading_dialog_text);
            if (!StringUtils.isEmpty(newUserPhoto) && !newUserPhoto.startsWith("http")) {
                AccountSettingPresenter.getQiNiuToken(this, newUserPhoto, new AccountSettingPresenter.UploadUserPhotoListener() {
                    @Override
                    public void onSuccess(String photoUrl) {
                        putUserInfo("http://7xo7ey.com1.z0.glb.clouddn.com/" + photoUrl);
                    }

                    @Override
                    public void onError() {
                        DialogUtil.dismissProgess();
                        Toast.makeText(AccountSettingActivity.this, "图片上传失败!", Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (!StringUtils.isEmpty(newUserPhoto) && newUserPhoto.startsWith("http")) {
                putUserInfo(newUserPhoto);
            } else {
                putUserInfo("");
            }

        } else {
            Toast.makeText(this, "请检查新密码与确认密码是否一致!", Toast.LENGTH_SHORT).show();
        }
    }

    public void putUserInfo(String photoUrl) {
        AccountSettingPresenter.postUserInfo2Server(AccountSettingActivity.this, newUserNickName, photoUrl, newUserGender, newUserLocation, newUserPw, newUserPwAgain, new AccountSettingPresenter.UploadUserPhotoListener() {
            @Override
            public void onSuccess(String photoUrl) {
                refreshUserInfo();
            }

            @Override
            public void onError() {
                Toast.makeText(AccountSettingActivity.this, "账户信息修改失败", Toast.LENGTH_SHORT).show();
                DialogUtil.dismissProgess();
            }
        });
    }


    public void refreshUserInfo() {
        NetService.get(this, NetApi.getMyUserInfo, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json("==" + response);
                UserInfoHelper.saveUserInfo(response);
                Toast.makeText(AccountSettingActivity.this, "账户信息修改成功", Toast.LENGTH_SHORT).show();
                DialogUtil.dismissProgess();
                SPUtils.putBooleanToSPMap(AccountSettingActivity.this, AppKey.IS_MENU_NEED_REFRESH, true);
            }

            @Override
            public boolean onError(String error) {
                Toast.makeText(AccountSettingActivity.this, "账户信息修改失败", Toast.LENGTH_SHORT).show();
                DialogUtil.dismissProgess();
                return false;
            }
        });
    }

    public void updateGender(String userGender) {
        newUserGender = userGender;
        accountSettingGenderTv.setText(newUserGender);
    }

    public void updateLocation(String locationDes) {
        newUserLocation = locationDes;
        accountSettingUserLocation.setText(newUserLocation);
    }

    public void updateAvatar(String avatarUrl) {
        if (!avatarUrl.startsWith("http")) {
            newUserPhoto = ImageUtils.saveImage2Sd(avatarUrl);
            accountSettingAvatarSdv.setImageURI(Uri.parse("file://" + newUserPhoto));
        } else {
            newUserPhoto = avatarUrl;
            accountSettingAvatarSdv.setImageURI(Uri.parse(newUserPhoto));
        }
    }

    Bitmap bitmaps = null;

    public void updateAvatar(int avatarId) {
        bitmaps = BitmapFactory.decodeResource(getResources(), avatarId);
        LogUtils.json("bitmap ==null?=" + (bitmaps == null));
        AccountSettingPresenter.getQiNiuToken(this, bitmaps, new AccountSettingPresenter.UploadUserPhotoListener() {
            @Override
            public void onSuccess(String photoUrl) {
                newUserPhoto = "http://7xo7ey.com1.z0.glb.clouddn.com/" + photoUrl;
                accountSettingAvatarSdv.setImageURI(Uri.parse(newUserPhoto));
                if (bitmaps != null) {
                    bitmaps.recycle();
                    bitmaps = null;
                }
            }

            @Override
            public void onError() {
                DialogUtil.dismissProgess();
                Toast.makeText(AccountSettingActivity.this, "图片上传失败!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("AccountSettingActivity");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("AccountSettingActivity");
        MobclickAgent.onResume(this);
    }
}
