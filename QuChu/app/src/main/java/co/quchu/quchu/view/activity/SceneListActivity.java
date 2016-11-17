package co.quchu.quchu.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.model.SceneInfoModel;
import co.quchu.quchu.view.adapter.SceneListAdapter;
import co.quchu.quchu.widget.SpacesItemDecoration;

/**
 * 所有场景列表
 * <p>
 * Created by mwb on 16/11/7.
 */
public class SceneListActivity extends BaseBehaviorActivity {

  @Bind(R.id.scene_recycler_view) RecyclerView mRecyclerView;
  private static String INTENT_KEY_SCENE_LIST = "intent_key_scene_list";
  private List<SceneInfoModel> mSceneList;

  public static void launch(Activity activity, List<SceneInfoModel> sceneList) {
    Intent intent = new Intent(activity, SceneListActivity.class);
    intent.putExtra(INTENT_KEY_SCENE_LIST, (Serializable) sceneList);
    activity.startActivity(intent);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scene_list);
    ButterKnife.bind(this);

    EnhancedToolbar toolbar = getEnhancedToolbar();
    toolbar.getTitleTv().setText("场景列表");

    mSceneList = (List<SceneInfoModel>) getIntent().getSerializableExtra(INTENT_KEY_SCENE_LIST);

    initRecyclerView();
  }

  private void initRecyclerView() {
    mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    SceneListAdapter adapter = new SceneListAdapter(this, mSceneList);
    adapter.setOnSceneListListener(new SceneListAdapter.OnSceneListListener() {
      @Override
      public void onItemClick(SceneInfoModel sceneInfoModel) {
        SceneDetailActivity.enterActivity(SceneListActivity.this, sceneInfoModel.getSceneId(), sceneInfoModel.getSceneName(), true);
      }
    });
    mRecyclerView.setAdapter(adapter);
    mRecyclerView.addItemDecoration(new SpacesItemDecoration(40, 3));
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
