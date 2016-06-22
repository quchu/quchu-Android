package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.dialog.MenuSettingDialogFg;
import co.quchu.quchu.dialog.VisitorLoginDialogFg;
import co.quchu.quchu.gallery.utils.ImageUtils;
import co.quchu.quchu.model.MyGeneModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.presenter.MeActivityPresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.widget.LinearProgressView;

public class MeFragment extends BaseFragment implements IMeActivity, View.OnClickListener {

    @Bind(R.id.headImage)
    SimpleDraweeView headImage;
    @Bind(R.id.quchu)
    LinearLayout quchu;
    @Bind(R.id.footPrint)
    LinearLayout footPrint;
    @Bind(R.id.friend)
    LinearLayout friend;
    @Bind(R.id.massage)
    RelativeLayout massage;
    @Bind(R.id.alias)
    TextView tvUserNickName;
    @Bind(R.id.desc)
    TextView name;
    @Bind(R.id.progress1)
    LinearProgressView progress1;
    @Bind(R.id.progress2)
    LinearProgressView progress2;
    @Bind(R.id.progress3)
    LinearProgressView progress3;
    @Bind(R.id.progress4)
    LinearProgressView progress4;
    @Bind(R.id.findPosition)
    TextView findPosition;
    @Bind(R.id.editOrLoginTV)
    TextView editOrLogin;
    @Bind(R.id.editIcon)
    ImageView editIcon;
    @Bind(R.id.unReadMassage)
    TextView unReadMassage;
    @Bind(R.id.editOrLoginAction)
    RelativeLayout editOrLoginAction;

    private MeActivityPresenter presenter;

    //用户头像
    private String userHead;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_me,container,false);

        ButterKnife.bind(this,v);

        presenter = new MeActivityPresenter(this, getActivity());
//        EnhancedToolbar toolbar = ((BaseActivity)getActivity()).getEnhancedToolbar();
//
//        ImageView imageView = toolbar.getRightIv();
//        imageView.setImageResource(R.mipmap.ic_tools);
//        imageView.setOnClickListener(this);
        presenter.getUnreadMassageCound();
        initListener();

        userHead = AppContext.user.getPhoto();
        ImageUtils.loadWithAppropriateSize(headImage, Uri.parse(AppContext.user.getPhoto()));
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("profile");
        presenter.getGene();
        if (AppContext.user.isIsVisitors()) {
            //游客
            editOrLogin.setText("登陆");
            editIcon.setVisibility(View.GONE);
        } else {
            editOrLogin.setText("编辑");
            editIcon.setVisibility(View.VISIBLE);
        }
        name.setText(AppContext.user.isIsVisitors() ? "未知生物" : AppContext.user.getFullname());
//更换了头像
        if (!userHead.equals(AppContext.user.getPhoto())) {
            userHead = AppContext.user.getPhoto();
            ImageUtils.loadWithAppropriateSize(headImage, Uri.parse(AppContext.user.getPhoto()));
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("profile");
    }

    private void initListener() {
        quchu.setOnClickListener(this);
        footPrint.setOnClickListener(this);
        friend.setOnClickListener(this);
        massage.setOnClickListener(this);
        findPosition.setOnClickListener(this);
        editOrLoginAction.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Intent intent;
        UserInfoModel user = AppContext.user;
        switch (v.getId()) {

            case R.id.quchu://收藏
                intent = new Intent(getActivity(), FavoriteActivity.class);
                startActivity(intent);
                break;
            case R.id.footPrint://脚印
                if (user.isIsVisitors()) {
                    //游客
                    VisitorLoginDialogFg dialogFg = VisitorLoginDialogFg.newInstance(0);
                    dialogFg.show(getActivity().getSupportFragmentManager(), "");
                } else {
                    intent = new Intent(getActivity(), MyFootprintActivity.class);
                    intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_ID, AppContext.user.getUserId());
                    intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_AGE, AppContext.user.getAge());
                    intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_FOOTER_COUND, AppContext.user.getCardCount());
                    intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_PHOTO, AppContext.user.getPhoto());
                    intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_FOOTER_TITLE, "我的脚印");
                    intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_NAME, AppContext.user.getFullname());
                    startActivity(intent);
                    if (!EventBus.getDefault().isRegistered(this)) {
                        EventBus.getDefault().register(this);
                    }
                }
                break;
            case R.id.friend://趣友圈
                if (user.isIsVisitors() && (!user.isIsweixin() && !user.isIsweibo())) {
                    //游客
                    VisitorLoginDialogFg dialogFg = VisitorLoginDialogFg.newInstance(0);
                    dialogFg.show(getActivity().getSupportFragmentManager(), "");
                } else {
                    intent = new Intent(getActivity(), QuFriendsActivity.class);
//                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this, headImage, QuFriendsActivity.KEY_TRANSITION_ANIMATION).toBundle();
                    startActivity(intent);
                }
                break;
            case R.id.massage://消息中心
                intent = new Intent(getActivity(), MessageCenterActivity.class);
                startActivity(intent);
                unReadMassage.setVisibility(View.INVISIBLE);
                break;
            case R.id.findPosition://发现新去处
                intent = new Intent(getActivity(), FindPositionListActivity.class);
                startActivity(intent);
                break;
            case R.id.toolbar_iv_right:
                MenuSettingDialogFg.newInstance().show(getActivity().getSupportFragmentManager(), "menu_setting");
                break;
            case R.id.editOrLoginAction:
                if (user.isIsVisitors()) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), AccountSettingActivity.class);
                    startActivity(intent);
                }
                break;
        }

    }



    @Override
    public void initGene(MyGeneModel data) {
        List<MyGeneModel.GenesEntity> genes = data.getGenes();
        for (int i = 0; i < genes.size(); i++) {
            int progress = (int) genes.get(i).getWeight();
            String label = genes.get(i).getZh();
            switch (i) {
                case 0:
                    if (null != genes.get(i).getMark()) {
                        tvUserNickName.setText(genes.get(i).getMark());
                    }
                    progress1.setProgress(progress, label, i);
                    break;
                case 1:
                    progress2.setProgress(progress, label, i);
                    break;
                case 2:
                    progress3.setProgress(progress, label, i);
                    break;
                case 3:
                    progress4.setProgress(progress, label, i);
                    break;
            }
        }
    }

//    @Subscribe
//    public void onEvenBug(QuchuEventModel model) {
//        if (model.getFlag() == EventFlags.EVENT_GOTO_HOME_PAGE) {
//            finish();
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void notReadMassage(int cound) {
        unReadMassage.setText(String.valueOf(cound));
        unReadMassage.setVisibility(View.VISIBLE);
    }

    public void checkUpdate() {

        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                switch (updateStatus) {
                    case UpdateStatus.Yes: // has update
                        UmengUpdateAgent.showUpdateDialog(getActivity(), updateInfo);
                        break;
                    case UpdateStatus.No: // has no update
                        Toast.makeText(getActivity(), "当前已是最新版本!", Toast.LENGTH_SHORT).show();
                        break;
                    case UpdateStatus.Timeout: // time out
                        Toast.makeText(getActivity(), "网络超时,请稍后重试!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        UmengUpdateAgent.forceUpdate(getActivity());
    }
}