package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.galleryfinal.CoreConfig;
import co.quchu.galleryfinal.FunctionConfig;
import co.quchu.galleryfinal.GalleryFinal;
import co.quchu.galleryfinal.model.PhotoInfo;
import co.quchu.quchu.BuildConfig;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.ASUserPhotoDialogFg;
import co.quchu.quchu.dialog.MenuSettingDialogFg;
import co.quchu.quchu.dialog.QAvatarSettingDialogFg;
import co.quchu.quchu.dialog.VisitorLoginDialogFg;
import co.quchu.quchu.model.MyGeneModel;
import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.photoselected.FrescoImageLoader;
import co.quchu.quchu.presenter.AccountSettingPresenter;
import co.quchu.quchu.presenter.MeActivityPresenter;
import co.quchu.quchu.utils.ImageUtils;
import co.quchu.quchu.widget.RoundProgressView;

public class MeActivity extends BaseActivity implements IMeActivity, ASUserPhotoDialogFg.UserPhotoOriginSelectedListener, GalleryFinal.OnHanlderResultCallback {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.setting)
    ImageView setting;
    @Bind(R.id.bg_simpleDraweeView)
    SimpleDraweeView bgSimpleDraweeView;
    @Bind(R.id.headImage)
    SimpleDraweeView headImage;

    @Bind(R.id.quchu)
    LinearLayout quchu;
    @Bind(R.id.footPrint)
    LinearLayout footPrint;
    @Bind(R.id.friend)
    LinearLayout friend;
    @Bind(R.id.massage)
    LinearLayout massage;

    @Bind(R.id.rpv1)
    RoundProgressView rpv1;
    @Bind(R.id.rpv2)
    RoundProgressView rpv2;
    @Bind(R.id.rpv3)
    RoundProgressView rpv3;
    @Bind(R.id.rpv4)
    RoundProgressView rpv4;
    @Bind(R.id.findPosition)
    CardView findPosition;
    @Bind(R.id.tvLabel1)
    TextView tvLabel1;
    @Bind(R.id.tvLabel2)
    TextView tvLabel2;
    @Bind(R.id.tvLabel3)
    TextView tvLabel3;
    @Bind(R.id.tvLabel4)
    TextView tvLabel4;
    @Bind(R.id.tvUserNickName)
    TextView tvUserNickName;

    @Bind(R.id.name)
    TextView name;


    private MeActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        ButterKnife.bind(this);
        presenter = new MeActivityPresenter(this, this);
        initListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {

        if (AppContext.user != null) {
            headImage.setImageURI(Uri.parse(AppContext.user.getPhoto()));
            name.setText(AppContext.user.getFullname());
        }
        bgSimpleDraweeView.setImageURI(Uri.parse("res:///" + R.mipmap.bg_user));

        presenter.getGene();
    }

    private void initListener() {
        back.setOnClickListener(this);
        setting.setOnClickListener(this);
        headImage.setOnClickListener(this);
        quchu.setOnClickListener(this);
        footPrint.setOnClickListener(this);
        friend.setOnClickListener(this);
        massage.setOnClickListener(this);
        rpv1.setOnClickListener(this);
        rpv2.setOnClickListener(this);
        rpv3.setOnClickListener(this);
        rpv4.setOnClickListener(this);
        findPosition.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.setting://设置
                MenuSettingDialogFg.newInstance().show(getFragmentManager(), "menu_setting");
                break;
            case R.id.headImage:
                UserInfoModel user = AppContext.user;
                if (user.isIsVisitors() && (!user.isIsweixin() && !user.isIsweibo())) {
                    //游客
                    VisitorLoginDialogFg dialogFg = VisitorLoginDialogFg.newInstance(VisitorLoginDialogFg.QACCOUNTSETTING);
                    dialogFg.show(getFragmentManager(), "");
                } else {
                    intent = new Intent(this, AccountSettingActivity.class);
                    startActivity(intent);
                }

                break;
//            case R.id.changeHeadImage:
//                ASUserPhotoDialogFg photoDialogFg = ASUserPhotoDialogFg.newInstance();
//                photoDialogFg.setOnOriginListener(this);
//                photoDialogFg.show(getFragmentManager(), "photo");
//                break;
            case R.id.quchu://趣处
                intent = new Intent(this, QuchuActivity.class);
                startActivity(intent);
                break;
            case R.id.footPrint://脚印
                intent = new Intent(this, MyFootprintActivity.class);
                intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_ID, AppContext.user.getUserId());
                intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_AGE, AppContext.user.getAge());
                intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_FOOTER_COUND, AppContext.user.getCardCount());
                intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_PHOTO, AppContext.user.getPhoto());
                intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_FOOTER_TITLE, "我的脚印");
                startActivity(intent);
                break;
            case R.id.friend://趣友圈
                intent = new Intent(this, QuFriendsActivity.class);
                startActivity(intent);
                break;
            case R.id.massage://消息中心
                intent = new Intent(this, MessageCenterActivity.class);
                startActivity(intent);
                break;
            case R.id.findPosition://发现新去处
                intent = new Intent(this, FindPositionActivity.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    //趣头像
    private ArrayList<Integer> imageList;

    @Override
    public void selectedCamare() {
        initGralley();
        int REQUEST_CODE_CAMERA = 0x02;
        GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, this);
    }

    @Override
    public void selectedAblum() {
        initGralley();
        int REQUEST_CODE_GALLERY = 0x01;
        GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, functionConfig, this);
    }

    @Override
    public void selectedQuPhtot() {
        if (imageList == null) {
            imageList = AccountSettingPresenter.getQAvatar();
        }
        QAvatarSettingDialogFg qAvatarDIalogFg = new QAvatarSettingDialogFg();
        qAvatarDIalogFg.init(imageList, new QAvatarSettingDialogFg.OnItenSelected() {
            @Override
            public void itemSelected(int imageId) {
                headImage.setImageURI(Uri.parse("res:///" + imageId));
                uploadHeadToService(imageId);
            }
        });
        qAvatarDIalogFg.show(getFragmentManager(), "qAvatar");
    }

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

    //照片选择成功
    @Override
    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
        if (resultList.size() > 0) {
            String path = resultList.get(0).getPhotoPath();
            if (!TextUtils.isEmpty(path)) {
//                headImage.setImageURI(Uri.fromFile(new File(path)));
                ImageUtils.ShowImage("file://" + path, headImage);
                uploadHeadToService(path);
            }
        }
    }

    @Override
    public void onHanlderFailure(int requestCode, String errorMsg) {
        Toast.makeText(this, "图片保存失败", Toast.LENGTH_SHORT).show();
    }


    private void uploadHeadToService(String path) {
        AccountSettingPresenter.getQiNiuToken(this, path, new AccountSettingPresenter.UploadUserPhotoListener() {
            @Override
            public void onSuccess(String photoUrl) {
                AccountSettingPresenter.postUserInfo2Server(MeActivity.this, AppContext.user.getUsername(), "http://7xo7ey.com1.z0.glb.clouddn.com/" + photoUrl,
                        AppContext.user.getGender(), "", "", "", new AccountSettingPresenter.UploadUserPhotoListener() {
                            @Override
                            public void onSuccess(String photoUrl) {
                            }

                            @Override
                            public void onError() {
                                Toast.makeText(MeActivity.this, "网络异常", Toast.LENGTH_SHORT).show();

                            }
                        });
            }

            @Override
            public void onError() {
                Toast.makeText(MeActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadHeadToService(int ImageId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), ImageId);
        AccountSettingPresenter.getQiNiuToken(this, bitmap, new AccountSettingPresenter.UploadUserPhotoListener() {
            @Override
            public void onSuccess(String photoUrl) {
                AccountSettingPresenter.postUserInfo2Server(MeActivity.this, AppContext.user.getUsername(), "http://7xo7ey.com1.z0.glb.clouddn.com/" + photoUrl,
                        AppContext.user.getGender(), "", "", "", new AccountSettingPresenter.UploadUserPhotoListener() {
                            @Override
                            public void onSuccess(String photoUrl) {
                            }

                            @Override
                            public void onError() {
                                Toast.makeText(MeActivity.this, "网络异常", Toast.LENGTH_SHORT).show();

                            }
                        });
            }

            @Override
            public void onError() {
                Toast.makeText(MeActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void initGene(MyGeneModel data) {
        List<MyGeneModel.GenesEntity> genes = data.getGenes();
        for (int i = 0; i < genes.size(); i++) {
            double progress = genes.get(i).getWeight();
            String label = genes.get(i).getZh();
            switch (i){
                case 0:
                    if (null!=genes.get(i).getMark()){
                        tvUserNickName.setText(genes.get(i).getMark());
                    }
                    rpv1.setProgress(progress);
                    tvLabel1.setText(label);
                    break;
                case 1:
                    rpv2.setProgress(progress);
                    tvLabel2.setText(label);
                    break;
                case 2:
                    rpv3.setProgress(progress);
                    tvLabel3.setText(label);
                    break;
                case 3:
                    rpv4.setProgress(progress);
                    tvLabel4.setText(label);
                    break;
            }
        }





    }
}
