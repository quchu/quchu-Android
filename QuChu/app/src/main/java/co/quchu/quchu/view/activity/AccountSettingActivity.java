package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
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
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.dialog.ASUserPhotoDialogFg;
import co.quchu.quchu.dialog.ConfirmDialogFg;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.dialog.LocationSettingDialogFg;
import co.quchu.quchu.dialog.ModiffPasswordDialog;
import co.quchu.quchu.dialog.QAvatarSettingDialogFg;
import co.quchu.quchu.dialog.VisitorLoginDialogFg;
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
public class AccountSettingActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.headView)
    SimpleDraweeView accountSettingAvatarSdv;
    @Bind(R.id.nickname)
    EditText nickname;
    @Bind(R.id.photoNumber)
    TextView photoNumber;
    @Bind(R.id.location)
    TextView accountSettingUserLocation;
    @Bind(R.id.radioGroup)
    RadioGroup radioGroup;


    private ArrayList<Integer> imageList;
    private boolean mProfileModified = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        ButterKnife.bind(this);
        EnhancedToolbar toolbar = getEnhancedToolbar();
        toolbar.getTitleTv().setText("账号编辑");
        imageList = AccountSettingPresenter.getQAvatar();
        TextView rightTv = toolbar.getRightTv();
        rightTv.setText("退出登录");
        rightTv.setOnClickListener(this);
    }


    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    private void userInfoBinding() {

        if (AppContext.user == null) {
            if (!StringUtils.isEmpty(SPUtils.getUserInfo(this)))
                AppContext.user = new Gson().fromJson(SPUtils.getUserInfo(this), UserInfoModel.class);
        }

        if (AppContext.user != null) {
            UserInfoModel user = AppContext.user;
            accountSettingAvatarSdv.setImageURI(Uri.parse(AppContext.user.getPhoto()));
            nickname.setText(AppContext.user.getFullname());
            photoNumber.setText(AppContext.user.getUsername());
            newUserLocation = AppContext.user.getLocation();
            accountSettingUserLocation.setText(newUserLocation);
            if ("男".equals(user.getGender())) {
                radioGroup.check(R.id.man);
            } else {
                radioGroup.check(R.id.girl);
            }
        }
    }

    @Override
    public void onBackPressed() {
        boolean userNameChanged;
        userNameChanged = !nickname.getText().toString().equals(AppContext.user.getFullname());
        boolean userGenderChanged;

        String gender = radioGroup.getCheckedRadioButtonId() == R.id.man ? "男" : "女";

        userGenderChanged = !AppContext.user.getGender().equals(gender);


        boolean userLocationChanged;
        userLocationChanged = !StringUtils.isEmpty(accountSettingUserLocation.getText().toString()) && !AppContext.user.getLocation().equals(accountSettingUserLocation.getText().toString());
        if (mProfileModified || userNameChanged || userGenderChanged || userLocationChanged) {
            ConfirmDialogFg confirmDialogFg = ConfirmDialogFg.newInstance("提示", "当前修改尚未保存，退出会导致资料丢失，是否保存");
            confirmDialogFg.setActionListener(new ConfirmDialogFg.OnActionListener() {
                @Override
                public void onClick(int index) {
                    if (index == ConfirmDialogFg.INDEX_OK) {
                        AccountSettingActivity.this.finish();
                    }
                }
            });
            confirmDialogFg.show(getSupportFragmentManager(), "~");
        } else {
            super.onBackPressed();
        }
    }

    @OnClick({R.id.headView, R.id.editHeadImage
            , R.id.location, R.id.modiffPass, R.id.bindAccound, R.id.saveUserInfo})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editHeadImage:
                ASUserPhotoDialogFg photoDialogFg = ASUserPhotoDialogFg.newInstance();
                photoDialogFg.setOnOriginListener(listener);
                photoDialogFg.show(getSupportFragmentManager(), "photo");
                break;
            case R.id.saveUserInfo:
                saveUserChange();
                break;

            case R.id.location:
                LocationSettingDialogFg locationDIalogFg = LocationSettingDialogFg.newInstance();
                locationDIalogFg.show(getSupportFragmentManager(), "location");
                break;
            case R.id.bindAccound:
                final Intent intent = new Intent(this, BindActivity.class);
                startActivity(intent);
                break;
            case R.id.modiffPass:
                ModiffPasswordDialog dialog = ModiffPasswordDialog.newInstance();
                dialog.show(getSupportFragmentManager(), "");
                break;
            case R.id.toolbar_tv_right:
                ConfirmDialogFg confirmDialog = ConfirmDialogFg.newInstance("确认退出?", "退出后将以游客模式登录");
                confirmDialog.setActionListener(new ConfirmDialogFg.OnActionListener() {
                    @Override
                    public void onClick(int index) {
                        if (index == ConfirmDialogFg.INDEX_OK) {
                            VisitorLoginDialogFg vDialog = VisitorLoginDialogFg.newInstance(VisitorLoginDialogFg.QBEEN);
                            vDialog.show(getSupportFragmentManager(), "visitor");
                            SPUtils.clearUserinfo(AppContext.mContext);
                            AppContext.user = null;
                            Intent intent1 = new Intent(AccountSettingActivity.this, UserLoginActivity.class);
                            intent1.putExtra("IsVisitorLogin", true);
                            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent1);
                            SPUtils.clearSpMap(AccountSettingActivity.this, AppKey.LOGIN_TYPE);
                        }
                    }
                });
                confirmDialog.show(getSupportFragmentManager(), "confirm");
                break;
        }
    }

    //选中头像dialog 点击回调
    public ASUserPhotoDialogFg.UserPhotoOriginSelectedListener listener = new ASUserPhotoDialogFg.UserPhotoOriginSelectedListener() {
        @Override
        public void selectedCamare() {
            initGralley();
            int REQUEST_CODE_CAMERA = 0x02;
            GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, mOnHanlderResultCallback);
        }

        @Override
        public void selectedAblum() {
            initGralley();
            int REQUEST_CODE_GALLERY = 0x01;
            GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);
        }

        @Override
        public void selectedQuPhtot() {
            if (imageList != null) {

                QAvatarSettingDialogFg qAvatarDIalogFg = new QAvatarSettingDialogFg();
                qAvatarDIalogFg.init(imageList, new QAvatarSettingDialogFg.OnItenSelected() {
                    @Override
                    public void itemSelected(int imageId) {
                        updateAvatar(imageId);
                    }
                });
                qAvatarDIalogFg.show(getSupportFragmentManager(), "qAvatar");
            }
        }
    };
    private FunctionConfig functionConfig;

    private void initGralley() {
        FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
        functionConfigBuilder.setEnableEdit(false);
        functionConfigBuilder.setEnableCrop(true);
        functionConfigBuilder.setEnableRotate(true);
        functionConfigBuilder.setEnablePreview(false);
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
    private String newUserNickName = "";
    private String newUserLocation = "";

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        GalleryFinal.cleanCacheFile();
    }


    //保存修改信息
    public void saveUserChange() {


        newUserNickName = nickname.getText().toString().trim();

        if (newUserNickName.length() < 1 || newUserNickName.length() > 10) {
            Toast.makeText(this, "昵称必须为1-10位汉字或英文", Toast.LENGTH_SHORT).show();
            return;
        }

        newUserLocation = accountSettingUserLocation.getText().toString().trim();

        DialogUtil.showProgess(this, R.string.loading_dialog_text);
        if (!StringUtils.isEmpty(newUserPhoto) && !newUserPhoto.startsWith("http")) {
            AccountSettingPresenter.getQiNiuToken(AccountSettingActivity.this, newUserPhoto, new AccountSettingPresenter.UploadUserPhotoListener() {
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
        } else {
            putUserInfo(newUserPhoto);
        }
    }

    public void putUserInfo(String photoUrl) {
        AccountSettingPresenter.postUserInfo2Server(AccountSettingActivity.this,
                newUserNickName, photoUrl, radioGroup.getCheckedRadioButtonId() == R.id.man ? "男" : "女", newUserLocation, new AccountSettingPresenter.UploadUserPhotoListener() {
                    @Override
                    public void onSuccess(String photoUrl) {
                        refreshUserInfo();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(AccountSettingActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                        DialogUtil.dismissProgess();
                    }
                });
    }


    public void refreshUserInfo() {
        NetService.get(this, NetApi.getMyUserInfo, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                UserInfoHelper.saveUserInfo(response);
                Toast.makeText(AccountSettingActivity.this, "账户信息修改成功", Toast.LENGTH_SHORT).show();
                DialogUtil.dismissProgess();
                SPUtils.putBooleanToSPMap(AccountSettingActivity.this, AppKey.IS_MENU_NEED_REFRESH, true);
            }

            @Override
            public boolean onError(String error) {
                DialogUtil.dismissProgess();
                return false;
            }
        });
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
        mProfileModified = true;
        bitmaps = BitmapFactory.decodeResource(getResources(), avatarId);
        LogUtils.json("bitmap ==null?=" + (bitmaps == null));
        AccountSettingPresenter.getQiNiuToken(AccountSettingActivity.this, bitmaps, new AccountSettingPresenter.UploadUserPhotoListener() {
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
        MobclickAgent.onPageEnd("edit");
    }

    @Override
    protected void onResume() {
        userInfoBinding();
        super.onResume();
        MobclickAgent.onPageStart("edit");
    }


}
