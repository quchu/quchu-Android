package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.ActManager;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.MenuSettingDialogFg;
import co.quchu.quchu.dialog.VisitorLoginDialogFg;
import co.quchu.quchu.utils.AppKey;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.widget.MoreButtonView;
import co.quchu.quchu.widget.WiperSwitch;
import co.quchu.quchu.widget.textcounter.PullMenusView;

/**
 * MenusActivity
 * User: Chenhs
 * Date: 2015-12-01
 * 下拉菜单
 */
public class MenusActivity extends BaseActivity implements WiperSwitch.StatusListener, MoreButtonView.MoreClicklistener, PullMenusView.PullMenusClickListener {
    @Bind(R.id.menus_pullmenus_pmv)
    PullMenusView menusPullmenusPmv;
    @Bind(R.id.menus_logout_ws)
    WiperSwitch menusLogoutWs;
    @Bind(R.id.menus_search_more_rl)
    MoreButtonView menusSearchMoreRl;
    @Bind(R.id.menus_search_username)
    TextView menusSearchUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menus);
        ButterKnife.bind(this);
        menusSearchMoreRl.setImage(R.drawable.ic_menus_title_more);
        if (AppContext.user != null) {
            menusSearchUsername.setText(AppContext.user.getFullname());
            menusPullmenusPmv.setAvatar(AppContext.user.getPhoto());
            menusLogoutWs.setStatusListener(this);
            menusSearchMoreRl.setMoreClick(this);
            menusPullmenusPmv.setItemClickListener(this);
        }

    }

    @OnClick({R.id.menus_search_rl, R.id.menus_add_topic})
    public void onViewClick(View view) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
        switch (view.getId()) {
            case R.id.menus_search_rl:
              /*  if (isButtonClickable) {
                    isButtonClickable = false;*/
                startActivity(new Intent(this, SearchActivity.class));
                //   mHandler.sendMessageDelayed(mHandler.obtainMessage(0x00), 900);
                //   }
                break;
            case R.id.menus_add_topic:
                Toast.makeText(this, " 即将开放，敬请期待 ", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MenusActivity");
        MobclickAgent.onPause(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != AppContext.user)
            menusSearchUsername.setText(AppContext.user.getFullname());
        if (SPUtils.getBooleanFromSPMap(this, AppKey.IS_MENU_NEED_REFRESH, false)) {
            if (menusSearchUsername != null)
                menusSearchUsername.setText(AppContext.user.getFullname());
            if (menusPullmenusPmv != null)
                menusPullmenusPmv.setAvatar(AppContext.user.getPhoto() + "3");
            SPUtils.putBooleanToSPMap(this, AppKey.IS_MENU_NEED_REFRESH, false);
        }
        MobclickAgent.onPageStart("MenusActivity");
        MobclickAgent.onResume(this);
    }

    private Bitmap bg;

    @Override
    public void statusOpen() {
        //退出登陆
        Toast.makeText(this, "退出登录", Toast.LENGTH_SHORT).show();
        ActManager.getAppManager().finishAllActivity();
        startActivity(new Intent(this, UserLoginActivity.class));
        SPUtils.clearUserinfo(this);
        AppContext.user = null;
        this.finish();
    }

    @Override
    public void statusClose() {

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
                    this.finish();
                }
                break;
            case PullMenusView.ClickMessage:
                //    Toast.makeText(this, " 即将开放，敬请期待 ", Toast.LENGTH_SHORT).show();
                if (AppContext.user.isIsVisitors()) {
                    VisitorLoginDialogFg vDialog = VisitorLoginDialogFg.newInstance(VisitorLoginDialogFg.QMESSAGECENTER);
                    vDialog.show(getFragmentManager(), "visitor");
                } else {
                    startActivity(new Intent(MenusActivity.this, MessageCenterActivity.class));
                    this.finish();
                }
                break;
            case PullMenusView.ClickSetting:
             /*   Bitmap screens = ShotScreenUtils.screenshot(MenusActivity.this);
                int startWidth = screens.getWidth() - 50;
                int startHeight = screens.getHeight() - 50;
        *//*        bg=Bitmap.createBitmap(screens,startWidth,startWidth,450,450);*//*
                bg = BlurUtils.BoxBlurFilter(Bitmap.createBitmap(screens, startWidth, startWidth, 250, 250));
                PopupWindowUtils.initPopupWindow(menusPullmenusPmv, this, bg);*/
                MenuSettingDialogFg.newInstance().show(getFragmentManager(), "menu_setting");
                break;
            case PullMenusView.ClickHome:
                MenusActivity.this.finish();
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
              /*      case UpdateStatus.NoneWifi: // none wifi
                        Toast.makeText(MenusActivity.this, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
                        break;*/
                    case UpdateStatus.Timeout: // time out
                        Toast.makeText(MenusActivity.this, "网络超时,请稍后重试!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        UmengUpdateAgent.forceUpdate(this);
    }
}
