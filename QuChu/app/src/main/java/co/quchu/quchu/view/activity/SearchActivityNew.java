package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.widget.TagCloudView;

/**
 * Created by mwb on 16/10/18.
 */
public class SearchActivityNew extends BaseBehaviorActivity {

  @Bind(R.id.search_back) ImageView mSearchBack;
  @Bind(R.id.search_input_et) EditText mSearchInputEt;
  @Bind(R.id.search_btn) TextView mSearchBtn;
  @Bind(R.id.tag_cloud_view) TagCloudView mTagCloudView;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_new);
    ButterKnife.bind(this);

    initTags();

    findViewById(R.id.testImg).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        makeToast("click");
      }
    });
  }

  private void initTags() {
    List<String> tags = new ArrayList<>();
    tags.add("厦门");
    tags.add("厦门观音山");
    tags.add("软件园");
    tags.add("厦门观音山趣处科技");
    tags.add("趣处 App");
    tags.add("厦门");
    tags.add("厦门观音山");
    tags.add("软件园");
    tags.add("厦门观音山趣处科技");
    tags.add("趣处 App");
    mTagCloudView.setTags(tags);
  }

  @Override
  public ArrayMap<String, Object> getUserBehaviorArguments() {
    return null;
  }

  @Override
  public int getUserBehaviorPageId() {
    return 114;
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
