package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.galleryfinal.utils.ImageUtils;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.MenuSettingDialogFg;
import co.quchu.quchu.dialog.VisitorLoginDialogFg;
import co.quchu.quchu.model.MyGeneModel;
import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.presenter.MeActivityPresenter;
import co.quchu.quchu.widget.RoundProgressView;

public class MeActivity extends BaseActivity implements IMeActivity, View.OnClickListener {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.setting)
    ImageView setting;
    @Bind(R.id.headImage)
    SimpleDraweeView headImage;

    @Bind(R.id.quchu)
    FrameLayout quchu;
    @Bind(R.id.footPrint)
    FrameLayout footPrint;
    @Bind(R.id.friend)
    FrameLayout friend;
    @Bind(R.id.massage)
    FrameLayout massage;

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

    //用户头像
    private Uri userHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        ButterKnife.bind(this);
        presenter = new MeActivityPresenter(this, this);
        initListener();
        initData();
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart("profile");

        name.setText(AppContext.user.isIsVisitors() ? "未知生物" : AppContext.user.getFullname());
//更换了头像
        if (!userHead.equals(Uri.parse(AppContext.user.getPhoto()))) {
            userHead = Uri.parse(AppContext.user.getPhoto());
            ImageUtils.loadWithAppropriateSize(headImage, userHead);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd("profile");
        super.onPause();
    }

    private void initData() {
        userHead = Uri.parse(AppContext.user.getPhoto());
        ImageUtils.loadWithAppropriateSize(headImage, userHead);
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
        UserInfoModel user = AppContext.user;
        switch (v.getId()) {
            case R.id.back:
                MeActivity.this.finish();
                break;
            case R.id.setting://设置
                MenuSettingDialogFg.newInstance().show(getSupportFragmentManager(), "menu_setting");
                break;
            case R.id.headImage:
                if (user.isIsVisitors()) {
                    //游客
                    VisitorLoginDialogFg dialogFg = VisitorLoginDialogFg.newInstance(VisitorLoginDialogFg.QACCOUNTSETTING);
                    dialogFg.show(getSupportFragmentManager(), "");
                } else {
                    intent = new Intent(this, AccountSettingActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.quchu://趣处
                intent = new Intent(this, QuchuActivity.class);
                startActivity(intent);
                break;
            case R.id.footPrint://脚印
                if (user.isIsVisitors()) {
                    //游客
                    VisitorLoginDialogFg dialogFg = VisitorLoginDialogFg.newInstance(VisitorLoginDialogFg.QACCOUNTSETTING);
                    dialogFg.show(getSupportFragmentManager(), "");
                } else {
                    intent = new Intent(this, MyFootprintActivity.class);
                    intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_ID, AppContext.user.getUserId());
                    intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_AGE, AppContext.user.getAge());
                    intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_FOOTER_COUND, AppContext.user.getCardCount());
                    intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_PHOTO, AppContext.user.getPhoto());
                    intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_FOOTER_TITLE, "我的脚印");
                    intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_NAME, AppContext.user.getFullname());
                    startActivity(intent);
                }
                break;
            case R.id.friend://趣友圈
                if (user.isIsVisitors() && (!user.isIsweixin() && !user.isIsweibo())) {
                    //游客
                    VisitorLoginDialogFg dialogFg = VisitorLoginDialogFg.newInstance(VisitorLoginDialogFg.QACCOUNTSETTING);
                    dialogFg.show(getSupportFragmentManager(), "");
                } else {
                    intent = new Intent(this, QuFriendsActivity.class);
                    startActivity(intent);
                }
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
        return TRANSITION_TYPE_BOTTOM;
    }


    @Override
    public void initGene(MyGeneModel data) {
        List<MyGeneModel.GenesEntity> genes = data.getGenes();
        for (int i = 0; i < genes.size(); i++) {
            double progress = genes.get(i).getWeight();
            String label = genes.get(i).getZh();
            switch (i) {
                case 0:
                    if (null != genes.get(i).getMark()) {
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

    public void checkUpdate() {

        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                switch (updateStatus) {
                    case UpdateStatus.Yes: // has update
                        UmengUpdateAgent.showUpdateDialog(MeActivity.this, updateInfo);
                        break;
                    case UpdateStatus.No: // has no update
                        Toast.makeText(MeActivity.this, "当前已是最新版本!", Toast.LENGTH_SHORT).show();
                        break;
                    case UpdateStatus.Timeout: // time out
                        Toast.makeText(MeActivity.this, "网络超时,请稍后重试!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        UmengUpdateAgent.forceUpdate(this);
    }
}
