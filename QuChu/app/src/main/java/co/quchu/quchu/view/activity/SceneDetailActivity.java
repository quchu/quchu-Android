package co.quchu.quchu.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.model.SceneDetailModel;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.ScenePresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.adapter.SceneDetailAdapter;

/**
 * Created by Nico on 16/7/11.
 */
public class SceneDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String BUNDLE_KEY_SCENE_ID = "BUNDLE_KEY_SCENE_ID";
    private static final String BUNDLE_KEY_SCENE_NAME = "BUNDLE_KEY_SCENE_NAME";
    private static final String BUNDLE_KEY_SCENE_IS_FAVORITE = "BUNDLE_KEY_SCENE_IS_FAVORITE";

    @Bind(R.id.rv)
    RecyclerView rv;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    boolean isFavorite = false;
    int sceneId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_recyclerview);
        ButterKnife.bind(this);

        sceneId = getIntent().getIntExtra(BUNDLE_KEY_SCENE_ID,-1);
        String name = getIntent().getStringExtra(BUNDLE_KEY_SCENE_NAME);
        isFavorite= getIntent().getBooleanExtra(BUNDLE_KEY_SCENE_IS_FAVORITE,false);


        getEnhancedToolbar().getTitleTv().setText(name);
        getEnhancedToolbar().getRightTv().setText(isFavorite?"取消收藏":"收藏");

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(mLayoutManager);
        getData(true);
        mSwipeRefreshLayout.setOnRefreshListener(this);

    }

    private void getData(final boolean firstLoad){
        if (firstLoad){
            DialogUtil.showProgess(this,R.string.loading_dialog_text);
        }

        ScenePresenter.getSceneDetail(getApplicationContext(), sceneId, SPUtils.getCityId(), 1, String.valueOf(SPUtils.getLatitude()), String.valueOf(SPUtils.getLongitude()), null, new CommonListener<SceneDetailModel>() {
            @Override
            public void successListener(SceneDetailModel response) {
                if (firstLoad){
                    DialogUtil.dismissProgessDirectly();
                }
                mSwipeRefreshLayout.setRefreshing(false);

                getEnhancedToolbar().getRightTv().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mFavoriteRunning){
                            Toast.makeText(getApplicationContext(),R.string.process_running_please_wait,Toast.LENGTH_SHORT).show();
                            return;
                        }
                        System.out.println("scene "+isFavorite+"~"+ sceneId);
                        if (isFavorite){
                            ScenePresenter.delFavoriteScene(getApplicationContext(), sceneId, new CommonListener() {
                                @Override
                                public void successListener(Object response) {
                                    mFavoriteRunning = false;
                                    isFavorite = false;
                                    getEnhancedToolbar().getRightTv().setText("收藏");
                                    Toast.makeText(getApplicationContext(),R.string.del_to_favorite_success,Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void errorListener(VolleyError error, String exception, String msg) {
                                    mFavoriteRunning = false;
                                    Toast.makeText(getApplicationContext(),R.string.del_to_favorite_fail,Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            ScenePresenter.addFavoriteScene(getApplicationContext(), sceneId, new CommonListener() {
                                @Override
                                public void successListener(Object response) {
                                    mFavoriteRunning = false;
                                    isFavorite = true;
                                    getEnhancedToolbar().getRightTv().setText("取消收藏");
                                    Toast.makeText(getApplicationContext(),R.string.add_to_favorite_success,Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void errorListener(VolleyError error, String exception, String msg) {
                                    mFavoriteRunning = false;
                                    Toast.makeText(getApplicationContext(),R.string.add_to_favorite_fail,Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });

                rv.setAdapter(new SceneDetailAdapter(getApplicationContext(), response, new SceneDetailAdapter.OnSceneItemClickListener() {
                    @Override
                    public void onArticleClick() {

                    }

                    @Override
                    public void onPlaceClick(int pid) {
                        Intent intent = new Intent(SceneDetailActivity.this, QuchuDetailsActivity.class);
                        intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, pid);
                        startActivity(intent);
                    }
                }));
            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {
                if (firstLoad){
                    DialogUtil.dismissProgessDirectly();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private boolean mFavoriteRunning = false;

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
        if (null==event){
            return;
        }
        switch (event.getFlag()){
            case EventFlags.EVENT_GOTO_HOME_PAGE:
                finish();
                break;
        }
    }

    public static void enterActivity(Activity from, int sceneId,String sceneName,boolean favorite) {
        Intent intent = new Intent(from, SceneDetailActivity.class);
        intent.putExtra(BUNDLE_KEY_SCENE_ID, sceneId);
        intent.putExtra(BUNDLE_KEY_SCENE_NAME,sceneName);
        intent.putExtra(BUNDLE_KEY_SCENE_IS_FAVORITE,favorite);
        from.startActivity(intent);
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    @Override
    public void onRefresh() {
        if (NetUtil.isNetworkConnected(getApplicationContext())){
            getData(false);
        }else{
            Toast.makeText(getApplicationContext(),R.string.network_error,Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
