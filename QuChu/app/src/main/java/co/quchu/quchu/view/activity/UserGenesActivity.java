package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;

/**
 * 趣基因介绍
 * <p>
 * Created by mwb on 2016/12/8.
 */
public class UserGenesActivity extends BaseBehaviorActivity {

  @Bind(R.id.enhancedToolbarDivider) View mEnhancedToolbarDivider;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_genes);
    ButterKnife.bind(this);

    initToolbar();
  }

  private void initToolbar() {
    EnhancedToolbar toolbar = getEnhancedToolbar();
    TextView textView = toolbar.getTitleTv();
    toolbar.setBackground(null);
    textView.setText("");
    mEnhancedToolbarDivider.setVisibility(View.GONE);
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
  protected String getPageNameCN() {
    return null;
  }
}
