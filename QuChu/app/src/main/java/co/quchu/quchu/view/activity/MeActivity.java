package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.MenuSettingDialogFg;
import co.quchu.quchu.dialog.VisitorLoginDialogFg;
import co.quchu.quchu.model.MyGeneModel;
import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.presenter.MeActivityPresenter;
import co.quchu.quchu.widget.RoundProgressView;

public class MeActivity extends BaseActivity implements IMeActivity {

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

    @Bind(R.id.curiosity)
    RoundProgressView curiosity;
    @Bind(R.id.eat)
    RoundProgressView eat;
    @Bind(R.id.art)
    RoundProgressView art;
    @Bind(R.id.money)
    RoundProgressView money;
    @Bind(R.id.findPosition)
    CardView findPosition;

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
        MobclickAgent.onPageStart("profile");
        super.onResume();
        initData();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd("profile");
        super.onPause();
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
        curiosity.setOnClickListener(this);
        art.setOnClickListener(this);
        money.setOnClickListener(this);
        eat.setOnClickListener(this);
        findPosition.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        UserInfoModel user = AppContext.user;
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.setting://设置
                MenuSettingDialogFg.newInstance().show(getFragmentManager(), "menu_setting");
                break;
            case R.id.headImage:
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
                if (user.isIsVisitors() && (!user.isIsweixin() && !user.isIsweibo())) {
                    //游客
                    VisitorLoginDialogFg dialogFg = VisitorLoginDialogFg.newInstance(VisitorLoginDialogFg.QACCOUNTSETTING);
                    dialogFg.show(getFragmentManager(), "");
                } else {
                    intent = new Intent(this, MyFootprintActivity.class);
                    intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_ID, AppContext.user.getUserId());
                    intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_AGE, AppContext.user.getAge());
                    intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_FOOTER_COUND, AppContext.user.getCardCount());
                    intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_PHOTO, AppContext.user.getPhoto());
                    intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_FOOTER_TITLE, "我的脚印");
                    startActivity(intent);
                }
                break;
            case R.id.friend://趣友圈
                if (user.isIsVisitors() && (!user.isIsweixin() && !user.isIsweibo())) {
                    //游客
                    VisitorLoginDialogFg dialogFg = VisitorLoginDialogFg.newInstance(VisitorLoginDialogFg.QACCOUNTSETTING);
                    dialogFg.show(getFragmentManager(), "");
                } else {
                    intent = new Intent(this, QuFriendsActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.massage://消息中心
                intent = new Intent(this, MessageCenterActivity.class);
                startActivity(intent);
                break;
            case R.id.curiosity:
                break;
            case R.id.eat:
                break;
            case R.id.art:
                break;
            case R.id.money:
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

//    //趣头像
//    private ArrayList<Integer> imageList;
//
//    @Override
//    public void selectedCamare() {
//        initGralley();
//        int REQUEST_CODE_CAMERA = 0x02;
//        GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, this);
//    }
//
//    @Override
//    public void selectedAblum() {
//        initGralley();
//        int REQUEST_CODE_GALLERY = 0x01;
//        GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, functionConfig, this);
//    }
//
//    @Override
//    public void selectedQuPhtot() {
//        if (imageList == null) {
//            imageList = AccountSettingPresenter.getQAvatar();
//        }
//        QAvatarSettingDialogFg qAvatarDIalogFg = new QAvatarSettingDialogFg();
//        qAvatarDIalogFg.init(imageList, new QAvatarSettingDialogFg.OnItenSelected() {
//            @Override
//            public void itemSelected(int imageId) {
//                headImage.setImageURI(Uri.parse("res:///" + imageId));
//                uploadHeadToService(imageId);
//            }
//        });
//        qAvatarDIalogFg.show(getFragmentManager(), "qAvatar");
//    }
//
//    private FunctionConfig functionConfig;
//
//    private void initGralley() {
//        FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
//        functionConfigBuilder.setEnableEdit(false);
//        functionConfigBuilder.setEnableCrop(true);
//        functionConfigBuilder.setEnableRotate(true);
//        functionConfigBuilder.setEnablePreview(false);
//        functionConfigBuilder.setForceCrop(false);//启动强制裁剪功能,一进入编辑页面就开启图片裁剪，不需要用户手动点击裁剪，此功能只针对单选操作
//        functionConfigBuilder.setForceCropEdit(true);
//        functionConfigBuilder.setRotateReplaceSource(true);
//        functionConfig = functionConfigBuilder.build();
//        CoreConfig coreConfig = new CoreConfig.Builder(this, new FrescoImageLoader(this), null)
//                .setDebug(BuildConfig.DEBUG)
//                .setFunctionConfig(functionConfig)
//                .setPauseOnScrollListener(null)
//                .setNoAnimcation(true)
//                .build();
//        GalleryFinal.init(coreConfig);
//    }
//
//    //照片选择成功
//    @Override
//    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
//        if (resultList.size() > 0) {
//            String path = resultList.get(0).getPhotoPath();
//            if (!TextUtils.isEmpty(path)) {
////                headImage.setImageURI(Uri.fromFile(new File(path)));
//                ImageUtils.ShowImage("file://" + path, headImage);
//                uploadHeadToService(path);
//            }
//        }
//    }
//
//    @Override
//    public void onHanlderFailure(int requestCode, String errorMsg) {
//        Toast.makeText(this, "图片保存失败", Toast.LENGTH_SHORT).show();
//    }
//
//
//    private void uploadHeadToService(String path) {
//        AccountSettingPresenter.getQiNiuToken(this, path, new AccountSettingPresenter.UploadUserPhotoListener() {
//            @Override
//            public void onSuccess(String photoUrl) {
//                AccountSettingPresenter.postUserInfo2Server(MeActivity.this, AppContext.user.getUsername(), "http://7xo7ey.com1.z0.glb.clouddn.com/" + photoUrl,
//                        AppContext.user.getGender(), "", "", "", new AccountSettingPresenter.UploadUserPhotoListener() {
//                            @Override
//                            public void onSuccess(String photoUrl) {
//                            }
//
//                            @Override
//                            public void onError() {
//                                Toast.makeText(MeActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
//
//                            }
//                        });
//            }
//
//            @Override
//            public void onError() {
//                Toast.makeText(MeActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void uploadHeadToService(int ImageId) {
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), ImageId);
//        AccountSettingPresenter.getQiNiuToken(this, bitmap, new AccountSettingPresenter.UploadUserPhotoListener() {
//            @Override
//            public void onSuccess(String photoUrl) {
//                AccountSettingPresenter.postUserInfo2Server(MeActivity.this, AppContext.user.getUsername(), "http://7xo7ey.com1.z0.glb.clouddn.com/" + photoUrl,
//                        AppContext.user.getGender(), "", "", "", new AccountSettingPresenter.UploadUserPhotoListener() {
//                            @Override
//                            public void onSuccess(String photoUrl) {
//                            }
//
//                            @Override
//                            public void onError() {
//                                Toast.makeText(MeActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
//
//                            }
//                        });
//            }
//
//            @Override
//            public void onError() {
//                Toast.makeText(MeActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    @Override
    public void initGene(MyGeneModel data) {
        List<MyGeneModel.GenesEntity> genes = data.getGenes();
        if (genes.size() > 3) {
            curiosity.setProgress(genes.get(0).getWeight());

            eat.setProgress(genes.get(1).getWeight());

            art.setProgress(genes.get(2).getWeight());

            money.setProgress(genes.get(3).getWeight());


        }
    }

}
