package co.quchu.quchu.view.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.view.SimpleDraweeView;

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
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.dialog.ASUserPhotoDialogFg;
import co.quchu.quchu.dialog.DialogUtil;
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
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.AccountSettingPresenter;
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
public class AccountSettingActivity extends BaseBehaviorActivity implements View.OnClickListener {

  @Bind(R.id.avatarImg)
  SimpleDraweeView avatarImg;
  @Bind(R.id.nickname)
  EditText nickname;
  @Bind(R.id.radioGroup)
  RadioGroup radioGroup;

  private boolean mProfileModified = false;
  private String mLocationStr;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_account_setting);
    ButterKnife.bind(this);

    initToolbar();

    initUserInfo();
  }

  private void initToolbar() {
    EnhancedToolbar toolbar = getEnhancedToolbar();
    toolbar.getTitleTv().setText("修改个人资料");

    TextView saveUserInfoTv = toolbar.getRightTv();
    saveUserInfoTv.setText("保存");
    saveUserInfoTv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        saveUserChange();
      }
    });
  }

  /**
   * 初始化用户信息
   */
  private void initUserInfo() {
    UserInfoModel user = AppContext.user;
    if (user == null) {
      return;
    }

    mLocationStr = user.getLocation();

    avatarImg.setImageURI(Uri.parse(user.getPhoto()));
    nickname.setText(user.getFullname());
    nickname.setSelection(user.getFullname().length());
    nickname.setCursorVisible(false);
    nickname.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        nickname.setCursorVisible(true);
      }
    });

    nickname.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().length() == 14) {
          makeToast("昵称最多可以输入14个字符");
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });

    if ("未知".equals(user.getGender())) {
      radioGroup.clearCheck();
    } else {
      if ("男".equals(user.getGender())) {
        radioGroup.check(R.id.male);
      } else {
        radioGroup.check(R.id.female);
      }
    }
  }

  @Override
  public void onBackPressed() {
    boolean userNameChanged;
    userNameChanged = !nickname.getText().toString().equals(AppContext.user.getFullname());
    boolean userGenderChanged;

    String gender = radioGroup.getCheckedRadioButtonId() == R.id.male ? "男" : "女";

    userGenderChanged = !AppContext.user.getGender().equals(gender);

    if (mProfileModified || userNameChanged || userGenderChanged) {
      new MaterialDialog.Builder(AccountSettingActivity.this)
          .content("当前修改尚未保存,退出会导致资料丢失,是否保存?")
          .positiveText("先保存")
          .negativeText(R.string.cancel)
          .cancelable(false)
          .onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
              saveUserChange();
            }
          })
          .onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
              AccountSettingActivity.this.finish();
            }
          })
          .show();
    } else {
      super.onBackPressed();
    }
  }

  @OnClick({R.id.editHeadImage})
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.editHeadImage://编辑头像
        ASUserPhotoDialogFg photoDialogFg = ASUserPhotoDialogFg.newInstance();
        photoDialogFg.setOnOriginListener(listener);
        photoDialogFg.show(getSupportFragmentManager(), "photo");
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
    if (!NetUtil.isNetworkConnected(this)) {
      makeToast(R.string.network_error);
      return;
    }

    newUserNickName = nickname.getText().toString().trim();

    if (newUserNickName.length() < 1 || newUserNickName.length() > 16) {
      Toast.makeText(this, "昵称必须为1-16位字符", Toast.LENGTH_SHORT).show();
      return;
    }
    //if (StringUtils.containsEmoji(newUserNickName)) {
    //    Toast.makeText(this, "昵称不能使用表情", Toast.LENGTH_SHORT).show();
    //    return;
    //}

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
        newUserNickName, photoUrl, radioGroup.getCheckedRadioButtonId() == R.id.male ? "男" : "女", mLocationStr, new AccountSettingPresenter.UploadUserPhotoListener() {
          @Override
          public void onSuccess(String photoUrl) {
            refreshUserInfo();
          }

          @Override
          public void onError() {
            Toast.makeText(AccountSettingActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
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
        EventBus.getDefault().post(new QuchuEventModel(EventFlags.EVENT_USER_INFO_UPDATE));
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
      LogUtils.e("头像路径" + newUserPhoto);
      avatarImg.setImageURI(Uri.fromFile(new File(newUserPhoto)));

    } else {
      newUserPhoto = avatarUrl;
      avatarImg.setImageURI(Uri.parse(newUserPhoto));
    }
  }

  Bitmap bitmaps = null;

  public void updateAvatar(int avatarId) {
    mProfileModified = true;
    bitmaps = BitmapFactory.decodeResource(getResources(), avatarId);
    avatarImg.setImageURI(Uri.parse("res:///" + avatarId));

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
  protected String getPageNameCN() {
    return getString(R.string.pname_account_setting);
  }

  @Override
  public ArrayMap<String, Object> getUserBehaviorArguments() {
    return null;
  }

  @Override
  public int getUserBehaviorPageId() {
    return 120;
  }

  @Override
  protected int activitySetup() {
    return TRANSITION_TYPE_LEFT;
  }
}
