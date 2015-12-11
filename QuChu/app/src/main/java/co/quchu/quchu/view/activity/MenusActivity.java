package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.ActManager;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
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
    @Bind(R.id.menus_add_topic)
    RelativeLayout menusAddTopic;
    @Bind(R.id.menus_search_rl)
    RelativeLayout menusSearchRl;

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
        switch (view.getId()) {
            case R.id.menus_search_rl:
                Toast.makeText(this, " 搜索", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.menus_add_topic:
                Toast.makeText(this, "发现新趣处", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private Bitmap bg;

    @Override
    public void statusOpen() {
        //退出登陆
        Toast.makeText(this, "退出登陆", Toast.LENGTH_SHORT).show();
        ActManager.getAppManager().finishActivitiesAndKeepLastOne();
        SPUtils.clearUserinfo(this);
        AppContext.user = null;
        startActivity(new Intent(this, UserLoginActivity.class));
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
        switch (itemID) {
            case PullMenusView.ClickAvatar:
                startActivity(new Intent(MenusActivity.this, PlanetActivity.class));
                this.finish();
                break;
            case PullMenusView.ClickMessage:
                Toast.makeText(this, " ClickMessage ", Toast.LENGTH_SHORT).show();
                break;
            case PullMenusView.ClickSetting:
             /*   Bitmap screens = ShotScreenUtils.screenshot(MenusActivity.this);
                int startWidth = screens.getWidth() - 50;
                int startHeight = screens.getHeight() - 50;
        *//*        bg=Bitmap.createBitmap(screens,startWidth,startWidth,450,450);*//*
                bg = BlurUtils.BoxBlurFilter(Bitmap.createBitmap(screens, startWidth, startWidth, 250, 250));
                PopupWindowUtils.initPopupWindow(menusPullmenusPmv, this, bg);*/
                break;
            case PullMenusView.ClickHome:
              MenusActivity.this.finish();
                break;
        }
    }


}
