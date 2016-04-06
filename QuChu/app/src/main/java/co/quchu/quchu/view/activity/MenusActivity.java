package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.ActManager;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.MenuSettingDialogFg;
import co.quchu.quchu.dialog.VisitorLoginDialogFg;
import co.quchu.quchu.dialog.ConfirmDialogFg;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.utils.AppKey;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.widget.MoreButtonView;
import co.quchu.quchu.widget.textcounter.PullMenusView;

/**
 * MenusActivity
 * User: Chenhs
 * Date: 2015-12-01
 * 下拉菜单
 */
public class MenusActivity extends BaseActivity implements MoreButtonView.MoreClicklistener, PullMenusView.PullMenusClickListener {
    @Bind(R.id.menus_pullmenus_pmv)
    PullMenusView menusPullmenusPmv;
    @Bind(R.id.menus_search_more_rl)
    MoreButtonView menusSearchMoreRl;
    @Bind(R.id.menus_search_username)
    TextView menusSearchUsername;
    @Bind(R.id.menu_visitor_login_iv)
    ImageView menuVisitorLoginIv;
    @Bind(R.id.menu_user_logout_tv)
    TextView menuUserLogoutTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menus);
        ButterKnife.bind(this);
        menusSearchMoreRl.setImage(R.mipmap.ic_menus_title_more);
        if (AppContext.user != null) {
            menusSearchUsername.setText(AppContext.user.getFullname());
            menusPullmenusPmv.setAvatar(AppContext.user.getPhoto());
            menusSearchMoreRl.setMoreClick(this);
            menusPullmenusPmv.setItemClickListener(this);
            if (AppContext.user.isIsVisitors()) {
                menuVisitorLoginIv.setVisibility(View.VISIBLE);
                menuUserLogoutTv.setVisibility(View.GONE);
            } else {
                menuVisitorLoginIv.setVisibility(View.GONE);
                menuUserLogoutTv.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_BOTTOM;
    }

    @OnClick({R.id.menus_search_rl, R.id.menus_add_topic, R.id.menu_visitor_login_iv, R.id.menu_user_logout_tv})
    public void onViewClick(View view) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
        switch (view.getId()) {
            case R.id.menus_search_rl:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.menus_add_topic:
//                Toast.makeText(this, " 即将开放，敬请期待 ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, FindPositionActivity.class);
                startActivity(intent);

                break;
            case R.id.menu_visitor_login_iv:
                startActivity(new Intent(this, UserLoginActivity.class));
                this.finish();
                break;
            case R.id.menu_user_logout_tv:



                ConfirmDialogFg confirmDialog = ConfirmDialogFg.newInstance(R.string.confirm,R.string.cancel);
                confirmDialog.setActionListener(new ConfirmDialogFg.OnActionListener() {
                    @Override
                    public void onClick(int index) {
                        if (index==ConfirmDialogFg.INDEX_OK){
                            VisitorLoginDialogFg vDialog = VisitorLoginDialogFg.newInstance(VisitorLoginDialogFg.QBEEN);
                            vDialog.show(getFragmentManager(), "visitor");
                            SPUtils.clearUserinfo(AppContext.mContext);
                            AppContext.user = null;
                            startActivity(new Intent(MenusActivity.this, UserLoginActivity.class).putExtra("IsVisitorLogin", true));
                            MenusActivity.this.finish();
                        }
                    }
                });
                confirmDialog.show(getFragmentManager(),"confirm");


                break;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MenusActivity");

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != AppContext.user) {
            if (null != menusSearchUsername)
                menusSearchUsername.setText(AppContext.user.getFullname());
            if (null != menuVisitorLoginIv && null != menuUserLogoutTv)
                if (AppContext.user.isIsVisitors()) {
                    menuVisitorLoginIv.setVisibility(View.VISIBLE);
                    menuUserLogoutTv.setVisibility(View.GONE);
                } else {
                    menuVisitorLoginIv.setVisibility(View.GONE);
                    menuUserLogoutTv.setVisibility(View.VISIBLE);
                }
        }
        if (SPUtils.getBooleanFromSPMap(this, AppKey.IS_MENU_NEED_REFRESH, false)) {
            if (menusSearchUsername != null)
                menusSearchUsername.setText(AppContext.user.getFullname());
            if (menusPullmenusPmv != null)
                menusPullmenusPmv.setAvatar(AppContext.user.getPhoto() + "3");
            SPUtils.putBooleanToSPMap(this, AppKey.IS_MENU_NEED_REFRESH, false);
        }
        MobclickAgent.onPageStart("MenusActivity");
    }


    public void userLogout() {
        //退出登陆
        Toast.makeText(this, "退出登录", Toast.LENGTH_SHORT).show();
        ActManager.getAppManager().finishAllActivity();
        startActivity(new Intent(this, UserLoginActivity.class));
        SPUtils.clearUserinfo(this);
        AppContext.user = null;
        this.finish();
    }


    @Override
    public void moreClick() {
        this.finish();
    }

    @Override
    public void onItemClick(int itemID) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
        switch (itemID) {
            case PullMenusView.ClickAvatar:
                if (AppContext.user.isIsVisitors()) {
                    VisitorLoginDialogFg vDialog = VisitorLoginDialogFg.newInstance(VisitorLoginDialogFg.QAVATAR);
                    vDialog.show(getFragmentManager(), "visitor");
                } else {
                    startActivity(new Intent(MenusActivity.this, PlanetActivity.class));
                }
                break;
            case PullMenusView.ClickMessage:
                if (AppContext.user.isIsVisitors()) {
                    VisitorLoginDialogFg vDialog = VisitorLoginDialogFg.newInstance(VisitorLoginDialogFg.QMESSAGECENTER);
                    vDialog.show(getFragmentManager(), "visitor");
                } else {
                    startActivity(new Intent(MenusActivity.this, MessageCenterActivity.class));
                }
                break;
            case PullMenusView.ClickSetting:
                MenuSettingDialogFg.newInstance().show(getFragmentManager(), "menu_setting");
                break;
            case PullMenusView.ClickHome:
                ActManager.getAppManager().finishActivitiesAndKeepLastOne();
                break;
        }
    }


    public void checkUpdate() {

        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                switch (updateStatus) {
                    case UpdateStatus.Yes: // has update
                        UmengUpdateAgent.showUpdateDialog(MenusActivity.this, updateInfo);
                        break;
                    case UpdateStatus.No: // has no update
                        Toast.makeText(MenusActivity.this, "当前已是最新版本!", Toast.LENGTH_SHORT).show();
                        break;
                    case UpdateStatus.Timeout: // time out
                        Toast.makeText(MenusActivity.this, "网络超时,请稍后重试!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        UmengUpdateAgent.forceUpdate(this);
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onMessageEvent(QuchuEventModel event) {

        if (event.getFlag() == EventFlags.EVENT_USER_LOGIN_SUCCESS) {
            menusPullmenusPmv.setAvatar(AppContext.user.getPhoto());
        }
    }
}
