package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.MenusSettingDialog;
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

    @Override
    public void statusOpen() {
        //退出登陆
        Toast.makeText(this, "退出登陆", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void statusClose() {

    }

    @Override
    public void moreClick() {

    }

    @Override
    public void onItemClick(int itemID) {
        switch (itemID) {
            case PullMenusView.ClickAvatar:
                Toast.makeText(this, " ClickAvatar ", Toast.LENGTH_SHORT).show();
                break;
            case PullMenusView.ClickMessage:
                Toast.makeText(this, " ClickMessage ", Toast.LENGTH_SHORT).show();
                break;
            case PullMenusView.ClickSetting:
                Toast.makeText(this, " ClickSetting ", Toast.LENGTH_SHORT).show();
            new MenusSettingDialog(this).show();
                break;
            case PullMenusView.ClickHome:
                Toast.makeText(this, " ClickHome ", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
