package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.presenter.SettingPresenter;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.widget.SettingItemView;
import co.quchu.quchu.widget.swithbutton.SwitchButton;

/**
 * 设置
 * <p/>
 * Created by mwb on 16/8/22.
 */
public class SettingActivity extends BaseBehaviorActivity {

  @Bind(R.id.setting_item_dahuo) SettingItemView itemDahuo;
  @Bind(R.id.setting_item_about) SettingItemView itemAbout;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setting);
    ButterKnife.bind(this);

    EnhancedToolbar toolbar = getEnhancedToolbar();
    TextView titleTv = toolbar.getTitleTv();
    titleTv.setText("设置");

    setDahuoSwitch();
  }

  /**
   * 设置搭伙开关
   */
  private void setDahuoSwitch() {
    final SwitchButton switchButton = itemDahuo.getSwitchButton();
    if (AppContext.user != null && AppContext.user.isIsVisitors() && switchButton != null) {
      switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
          switchButton.setChecked(true);

          showLoginDialog();
        }
      });

    } else {
      itemDahuo.setSwitchChecked(SPUtils.getDahuoSwitch(), new SettingItemView.SwitchChangedListener() {
        @Override
        public void onSwitch(boolean isChecked) {
          SettingPresenter.setUserMsg(SettingActivity.this, SettingPresenter.SETTING_TYPE_DAHUO, isChecked);
        }
      });
    }
  }

  @Override
  public ArrayMap<String, Object> getUserBehaviorArguments() {
    return null;
  }

  @Override
  public int getUserBehaviorPageId() {
    return 0;
  }

  @Override
  protected int activitySetup() {
    return TRANSITION_TYPE_LEFT;
  }

  @Override
  protected String getPageNameCN() {
    return "设置";
  }

  @OnClick({R.id.setting_item_dahuo, R.id.setting_item_about})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.setting_item_dahuo:
        //搭伙
        break;

      case R.id.setting_item_about:
        //关于
        Intent intent = new Intent(this, StatementActivity.class);
        intent.putExtra(StatementActivity.REQUEST_KEY_TITLE, "关于我们");
        String about = String.format(Locale.CHINA, getString(R.string.about_us_text),
            AppContext.packageInfo.versionName);
        intent.putExtra(StatementActivity.REQUEST_KEY_CONTENT, about);
        startActivity(intent);
        break;
    }
  }
}
