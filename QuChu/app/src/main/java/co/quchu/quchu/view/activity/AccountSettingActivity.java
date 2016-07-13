package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.BuildConfig;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.AppLocationListener;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.dialog.ASUserPhotoDialogFg;
import co.quchu.quchu.dialog.CommonDialog;
import co.quchu.quchu.dialog.ConfirmDialogFg;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.dialog.ModiffPasswordDialog;
import co.quchu.quchu.dialog.QAvatarSettingDialogFg;
import co.quchu.quchu.gallery.CoreConfig;
import co.quchu.quchu.gallery.FrescoImageLoader;
import co.quchu.quchu.gallery.FunctionConfig;
import co.quchu.quchu.gallery.GalleryFinal;
import co.quchu.quchu.gallery.model.PhotoInfo;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.presenter.AccountSettingPresenter;
import co.quchu.quchu.presenter.UserLoginPresenter;
import co.quchu.quchu.thirdhelp.UserInfoHelper;
import co.quchu.quchu.utils.AppKey;
import co.quchu.quchu.utils.EventFlags;
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
    @Bind(R.id.loginTypeIcon)
    ImageView loginTypeIcon;
    @Bind(R.id.modiffPass)
    RelativeLayout modiffPass;
    @Bind(R.id.saveUserInfo)
    TextView mSaveUserInfo;
    @Bind(R.id.vDividerUserName)
    View vDividerUserName;
    @Bind(R.id.rlUserName)
    View rlUserName;


    private boolean mProfileModified = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        ButterKnife.bind(this);
        EnhancedToolbar toolbar = getEnhancedToolbar();
        toolbar.getTitleTv().setText("账号编辑");

        TextView rightTv = toolbar.getRightTv();
        rightTv.setText("退出登录");
        rightTv.setOnClickListener(this);
        userInfoBinding();

        if (null==AppContext.user || AppContext.user.isIsVisitors()){
            vDividerUserName.setVisibility(View.GONE);
            rlUserName.setVisibility(View.GONE);
        }else{
            vDividerUserName.setVisibility(View.VISIBLE);
            rlUserName.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    private void userInfoBinding() {

        UserInfoModel user = AppContext.user;
        accountSettingAvatarSdv.setImageURI(Uri.parse(AppContext.user.getPhoto()));
        nickname.setText(AppContext.user.getFullname());
        nickname.setSelection(user.getFullname().length());
        photoNumber.setText(AppContext.user.getUsername());
        accountSettingUserLocation.setText(AppContext.user.getLocation());
        if ("男".equals(user.getGender())) {
            radioGroup.check(R.id.man);
        } else {
            radioGroup.check(R.id.girl);
        }

        switch (SPUtils.getLoginType()) {
            case SPUtils.LOGIN_TYPE_WEIBO:
                loginTypeIcon.setImageResource(R.mipmap.ic_weibo);
                break;
            case SPUtils.LOGIN_TYPE_WEIXIN:
                loginTypeIcon.setImageResource(R.mipmap.ic_wechatpay);
                break;
            default:
                loginTypeIcon.setImageResource(R.mipmap.ic_phone);
        }
        if (user.isphone()) {
            modiffPass.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        boolean userNameChanged;
        userNameChanged = !nickname.getText().toString().equals(AppContext.user.getFullname());
        boolean userGenderChanged;

        String gender = radioGroup.getCheckedRadioButtonId() == R.id.man ? "男" : "女";

        userGenderChanged = !AppContext.user.getGender().equals(gender);


        if (mProfileModified || userNameChanged || userGenderChanged || !accountSettingUserLocation.getText().toString().equals(AppContext.user.getLocation())) {
            CommonDialog dialog = CommonDialog.newInstance("请先保存", "当前修改尚未保存,退出会导致资料丢失,是否保存?", "先保存", "取消");

            dialog.setListener(new CommonDialog.OnActionListener() {
                @Override
                public boolean dialogClick(int clickId) {
                    if (clickId != CommonDialog.CLICK_ID_ACTIVE) {
                        finish();
                    } else {
                        mSaveUserInfo.callOnClick();
                    }
                    return true;
                }
            });

            dialog.show(getSupportFragmentManager(), "");
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
            case R.id.bindAccound:
                final Intent intent = new Intent(this, BindActivity.class);
                startActivity(intent);
                break;
            case R.id.modiffPass:
                ModiffPasswordDialog dialog = ModiffPasswordDialog.newInstance();
                dialog.show(getSupportFragmentManager(), "");
                break;
            case R.id.toolbar_tv_right:
                final ConfirmDialogFg confirmDialog = ConfirmDialogFg.newInstance("确认退出?", "退出后将以游客模式登录");
                confirmDialog.setActionListener(new ConfirmDialogFg.OnActionListener() {
                    @Override
                    public void onClick(int index) {
                        if (index == ConfirmDialogFg.INDEX_OK) {
                            SPUtils.clearUserinfo(AppContext.mContext);
                            AppContext.user = null;
                            SPUtils.clearSpMap(AccountSettingActivity.this, AppKey.LOGIN_TYPE);

                            UserLoginPresenter.visitorRegiest(AccountSettingActivity.this, new UserLoginPresenter.UserNameUniqueListener() {
                                @Override
                                public void isUnique(JSONObject msg) {
                                    confirmDialog.dismiss();
                                    EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_USER_LOGOUT));
                                    finish();
                                }

                                @Override
                                public void notUnique(String msg) {

                                }
                            });
                        }
                    }
                });
                confirmDialog.show(getSupportFragmentManager(), "confirm");
                break;
            case R.id.location:
                accountSettingUserLocation.setText("定位中...");
                AppLocationListener.addLocationListener(new AppLocationListener.LocationListener() {
                    @Override
                    public void location(AMapLocation amapLocation) {
                        accountSettingUserLocation.setText(amapLocation.getCity());
                        AppContext.stopLocation();
                        AppLocationListener.removeListener(this);
                    }
                });
                AppContext.initLocation();
                break;
        }
    }

    //选中头像dialog 点击回调
    public ASUserPhotoDialogFg.UserPhotoOriginSelectedListener listener = new ASUserPhotoDialogFg.UserPhotoOriginSelectedListener() {

        @Override
        public void selectedAblum() {
            initGralley();
            int REQUEST_CODE_GALLERY = 0x01;
            GalleryFinal.openGallerySingle(AccountSettingActivity.this, REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);
        }

        @Override
        public void selectedQuPhtot() {
            QAvatarSettingDialogFg qAvatarDIalogFg = new QAvatarSettingDialogFg();
            qAvatarDIalogFg.init(AccountSettingPresenter.getQAvatar(), new QAvatarSettingDialogFg.OnItenSelected() {
                @Override
                public void itemSelected(int imageId) {
                    updateAvatar(imageId);
                }
            });
            qAvatarDIalogFg.show(getSupportFragmentManager(), "qAvatar");

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
        functionConfigBuilder.setMutiSelect(false);
        functionConfig = functionConfigBuilder.build();
        CoreConfig coreConfig = new CoreConfig.Builder(this, new FrescoImageLoader(this))
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
//        GalleryFinal.cleanCacheFile();
        GalleryFinal.setmCallback(null);
    }


    //保存修改信息
    public void saveUserChange() {
        newUserNickName = nickname.getText().toString().trim();

        if (newUserNickName.length() < 1 || newUserNickName.length() > 10) {
            Toast.makeText(this, "昵称必须为1-10位字符", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.containsEmoji(newUserNickName)) {
            Toast.makeText(this, "昵称不能使用表情", Toast.LENGTH_SHORT).show();
            return;
        }

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
                newUserNickName, photoUrl, radioGroup.getCheckedRadioButtonId() == R.id.man ? "男" : "女", accountSettingUserLocation.getText().toString(), new AccountSettingPresenter.UploadUserPhotoListener() {
                    @Override
                    public void onSuccess(String photoUrl) {
                        refreshUserInfo();
                        EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_USER_INFO_UPDATE));

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
                finish();
            }

            @Override
            public boolean onError(String error) {
                DialogUtil.dismissProgess();
                return false;
            }
        });
    }


    public void updateAvatar(String avatarUrl) {
        mProfileModified = true;

        if (!avatarUrl.startsWith("http")) {
            newUserPhoto = ImageUtils.saveImage2Sd(avatarUrl);
//            co.quchu.quchu.gallery.utils.ImageUtils.loadWithAppropriateSize(accountSettingAvatarSdv, Uri.fromFile(new File(newUserPhoto)));
//
//            ImageRequest request = ImageRequestBuilder.newBuilderWithSource( Uri.fromFile(new File(newUserPhoto)))
//                    .setResizeOptions(new ResizeOptions(accountSettingAvatarSdv.getWidth(),  accountSettingAvatarSdv.getHeight()))
//                    .build();
//            DraweeController controller = Fresco.newDraweeControllerBuilder()
//                    .setOldController(accountSettingAvatarSdv.getController())
//                    .setImageRequest(request)
//                    .build();
//            accountSettingAvatarSdv.setController(controller);

            LogUtils.e("头像路径" + newUserPhoto);
//            accountSettingAvatarSdv.setImageURI(Uri.EMPTY);
            accountSettingAvatarSdv.setImageURI(Uri.fromFile(new File(newUserPhoto)));

        } else {
            newUserPhoto = avatarUrl;
            accountSettingAvatarSdv.setImageURI(Uri.parse(newUserPhoto));
        }
    }

    Bitmap bitmaps = null;

    public void updateAvatar(int avatarId) {
        mProfileModified = true;
        bitmaps = BitmapFactory.decodeResource(getResources(), avatarId);
        accountSettingAvatarSdv.setImageURI(Uri.parse("res:///" + avatarId));

        LogUtils.json("bitmap ==null?=" + (bitmaps == null));
        AccountSettingPresenter.getQiNiuToken(AccountSettingActivity.this, bitmaps, new AccountSettingPresenter.UploadUserPhotoListener() {
            @Override
            public void onSuccess(String photoUrl) {
                newUserPhoto = "http://7xo7ey.com1.z0.glb.clouddn.com/" + photoUrl;
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

        super.onResume();
        MobclickAgent.onPageStart("edit");
    }


}
