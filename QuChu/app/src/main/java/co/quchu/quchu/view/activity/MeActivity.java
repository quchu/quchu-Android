package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.galleryfinal.FunctionConfig;
import co.quchu.galleryfinal.GalleryFinal;
import co.quchu.galleryfinal.model.PhotoInfo;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.ASUserPhotoDialogFg;
import co.quchu.quchu.dialog.QAvatarSettingDialogFg;
import co.quchu.quchu.presenter.AccountSettingPresenter;
import co.quchu.quchu.widget.RoundProgressView;

public class MeActivity extends BaseActivity implements ASUserPhotoDialogFg.UserPhotoOriginSelectedListener, GalleryFinal.OnHanlderResultCallback {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.setting)
    ImageView setting;
    @Bind(R.id.bg_simpleDraweeView)
    SimpleDraweeView bgSimpleDraweeView;
    @Bind(R.id.headImage)
    SimpleDraweeView headImage;
    @Bind(R.id.changeHeadImage)
    ImageView changeHeadImage;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        ButterKnife.bind(this);
        initListener();
        initData();
    }

    private void initData() {

        if (AppContext.user != null) {
            headImage.setImageURI(Uri.parse(AppContext.user.getPhoto()));
            name.setText(AppContext.user.getFullname());
        }
        bgSimpleDraweeView.setImageURI(Uri.parse("res:///" + R.mipmap.bg_user));
    }

    private void initListener() {
        back.setOnClickListener(this);
        setting.setOnClickListener(this);
        headImage.setOnClickListener(this);
        changeHeadImage.setOnClickListener(this);
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
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.setting://设置
                intent = new Intent(this, AccountSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.headImage:
                break;
            case R.id.changeHeadImage:
                ASUserPhotoDialogFg photoDialogFg = ASUserPhotoDialogFg.newInstance();
                photoDialogFg.setOnOriginListener(this);
                photoDialogFg.show(getFragmentManager(), "photo");

                break;
            case R.id.quchu://趣处
                intent = new Intent(this, QuchuActivity.class);
                startActivity(intent);
                break;
            case R.id.footPrint://脚印
                break;
            case R.id.friend://趣友圈
                intent = new Intent(this, QuFriendsActivity.class);
                startActivity(intent);
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

    private final int REQUEST_CODE_GALLERY = 0x01;
    private final int REQUEST_CODE_CAMERA = 0x02;
    //趣头像
    private ArrayList<Integer> imageList;

    @Override
    public void selectedCamare() {
        initGralley();
        GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, this);
    }

    @Override
    public void selectedAblum() {
        initGralley();
        GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, functionConfig, this);
    }

    @Override
    public void selectedQuPhtot() {
        if (imageList == null) {
            imageList = AccountSettingPresenter.getQAvatar();
        }
        QAvatarSettingDialogFg qAvatarDIalogFg = QAvatarSettingDialogFg.newInstance(imageList);
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

    }

    //照片选择成功
    @Override
    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {

    }

    @Override
    public void onHanlderFailure(int requestCode, String errorMsg) {
        Toast.makeText(this, "图片保存失败", Toast.LENGTH_SHORT).show();
    }
}
