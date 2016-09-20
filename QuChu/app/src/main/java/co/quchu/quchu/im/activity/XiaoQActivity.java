package co.quchu.quchu.im.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;

/**
 * Created by mwb on 16/9/19.
 */
public class XiaoQActivity extends BaseBehaviorActivity {

  public static void launch(Activity activity) {
    Intent intent = new Intent(activity, XiaoQActivity.class);
    activity.startActivity(intent);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_xiaoq);
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
