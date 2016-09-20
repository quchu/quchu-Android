package co.quchu.quchu.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.presenter.SettingPresenter;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.widget.SettingItemView;

/**
 * 小Q设置
 * <p/>
 * Created by mwb on 16/8/26.
 */
public class SettingXioaQActivity extends BaseBehaviorActivity {

  @Bind(R.id.setting_item_news) SettingItemView itemNews;
  @Bind(R.id.setting_item_quchu) SettingItemView itemQuchu;
  @Bind(R.id.setting_item_quchu_user) SettingItemView itemQuchuUser;
  @Bind(R.id.xiaoq_title_tv) TextView mXiaoqTitleTv;

  public static void launch(Activity activity) {
    Intent intent = new Intent(activity, SettingXioaQActivity.class);
    activity.startActivity(intent);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setting_xiaoq);
    ButterKnife.bind(this);

    EnhancedToolbar toolbar = getEnhancedToolbar();
    TextView titleTv = toolbar.getTitleTv();
    titleTv.setText("Little Q设置");

    initView();
  }

  private void initView() {
    Typeface face = Typeface.createFromAsset(getAssets(), "AGENCYFB.TTF");
    mXiaoqTitleTv.setTypeface(face);

    //推荐文章
    itemNews.setSwitchChecked(SPUtils.getNewsSwitch(), new SettingItemView.SwitchChangedListener() {
      @Override
      public void onSwitch(boolean isChecked) {
        SettingPresenter
            .setUserMsg(SettingXioaQActivity.this, SettingPresenter.SETTING_TYPE_NEWS, isChecked);
      }
    });

    //推荐趣处
    itemQuchu.setSwitchChecked(SPUtils.getQuchuSwitch(), new SettingItemView.SwitchChangedListener() {
      @Override
      public void onSwitch(boolean isChecked) {
        SettingPresenter
            .setUserMsg(SettingXioaQActivity.this, SettingPresenter.SETTING_TYPE_QUCHU, isChecked);
      }
    });

    //推荐趣星人
    itemQuchuUser.setSwitchChecked(SPUtils.getQuchuUserSwitch(), new SettingItemView.SwitchChangedListener() {
      @Override
      public void onSwitch(boolean isChecked) {
        SettingPresenter
            .setUserMsg(SettingXioaQActivity.this, SettingPresenter.SETTING_TYPE_QUCHU_USER,
                isChecked);
      }
    });
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
    return null;
  }
}
