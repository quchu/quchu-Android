package co.quchu.quchu.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.volley.VolleyError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.DetailModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.model.SceneDetailModel;
import co.quchu.quchu.model.SceneHeaderModel;
import co.quchu.quchu.model.SceneInfoModel;
import co.quchu.quchu.model.SimpleArticleModel;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.ScenePresenter;
import co.quchu.quchu.utils.AppKey;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.adapter.SceneDetailAdapter;
import co.quchu.quchu.widget.EndlessRecyclerOnScrollListener;
import co.quchu.quchu.widget.ErrorView;

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

    @Bind(R.id.errorView)
    ErrorView errorView;

    private boolean isFavorite = false;
    private int sceneId;
    private int mMaxPageNo = -1;
    private int mPageNo = 1;
    private int[] placeIds;
    private EndlessRecyclerOnScrollListener mLoadingListener;

    private List<DetailModel> mSceneList = new ArrayList<>();
    private List<SceneHeaderModel> mRecommendedList = new ArrayList<>();

    private SimpleArticleModel mArticleModel;
    private SceneInfoModel mSceneInfo;
    private SceneDetailAdapter mAdapter;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;

    private boolean mFavoriteRunning = false;
    private boolean mActivityStop = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_recyclerview);
        ButterKnife.bind(this);
        sceneId = getIntent().getIntExtra(BUNDLE_KEY_SCENE_ID, -1);
        String name = getIntent().getStringExtra(BUNDLE_KEY_SCENE_NAME);
        isFavorite = getIntent().getBooleanExtra(BUNDLE_KEY_SCENE_IS_FAVORITE, false);


        getEnhancedToolbar().getTitleTv().setText(name);
        getEnhancedToolbar().getRightTv().setText(isFavorite ? "移除" : "设为常用");

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(mLayoutManager);
        if (!NetUtil.isNetworkConnected(getApplicationContext()) && mAdapter == null) {
            errorView.showViewDefault(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtil.showProgess(SceneDetailActivity.this, "加载中");
                    getData(true, false);
                }
            });
        }
        mSwipeRefreshLayout.setOnRefreshListener(this);


        mLoadingListener = new EndlessRecyclerOnScrollListener((LinearLayoutManager) rv.getLayoutManager()) {
            @Override
            public void onLoadMore(int current_page) {
                getData(false, true);
            }
        };
        rv.addOnScrollListener(mLoadingListener);


        errorView.hideView();
        getEnhancedToolbar().getRightTv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFavorite();
            }
        });


        getData();

    }

    public void getData() {
        DialogUtil.showProgess(this, R.string.loading_dialog_text);
        int delay = 0;
        if (Math.abs(AppContext.mLastLocatingTimeStamp - System.currentTimeMillis())>=(60000*5)){
            AppContext.initLocation();
            delay += 3000;
        }

        rv.postDelayed(new Runnable() {
            @Override
            public void run() {
                getData(true, false);
            }
        },delay);
    }


    private void changeFavorite() {
        if (!NetUtil.isNetworkConnected(SceneDetailActivity.this)) {
            Toast.makeText(SceneDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            return;
        }
        if (mFavoriteRunning) {
            Toast.makeText(getApplicationContext(), R.string.process_running_please_wait, Toast.LENGTH_SHORT).show();
            return;
        }
        if (isFavorite) {
            ScenePresenter.delFavoriteScene(getApplicationContext(), sceneId, new CommonListener() {
                @Override
                public void successListener(Object response) {
                    mFavoriteRunning = false;
                    isFavorite = false;
                    getEnhancedToolbar().getRightTv().setText("设为常用");
                    Toast.makeText(getApplicationContext(), R.string.del_to_favorite_success, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void errorListener(VolleyError error, String exception, String msg) {
                    mFavoriteRunning = false;
                    Toast.makeText(getApplicationContext(), R.string.del_to_favorite_fail, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            ScenePresenter.addFavoriteScene(getApplicationContext(), sceneId, new CommonListener() {
                @Override
                public void successListener(Object response) {
                    mFavoriteRunning = false;
                    isFavorite = true;
                    getEnhancedToolbar().getRightTv().setText("移除");
                    Toast.makeText(getApplicationContext(), R.string.add_to_favorite_success, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void errorListener(VolleyError error, String exception, String msg) {
                    mFavoriteRunning = false;
                    Toast.makeText(getApplicationContext(), R.string.add_to_favorite_fail, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void getData(final boolean firstLoad, final boolean loadMore) {
        if (firstLoad) {

            mSceneList.clear();
            mRecommendedList.clear();
            mMaxPageNo = -1;
            mPageNo = 1;
        }

        if (mMaxPageNo != -1 && mPageNo >= mMaxPageNo && loadMore) {
            mAdapter.showPageEnd(true);
            return;
        }

        if (loadMore) {
            mPageNo += 1;
        }

        if (mActivityStop){
            return;
        }
        DialogUtil.showProgess(this, R.string.loading_dialog_text);

        ScenePresenter.getSceneDetail(getApplicationContext(), sceneId, SPUtils.getCityId(), mPageNo, String.valueOf(SPUtils.getLatitude()), String.valueOf(SPUtils.getLongitude()), placeIds, new CommonListener<SceneDetailModel>() {
            @Override
            public void successListener(SceneDetailModel response) {
                DialogUtil.dismissProgessDirectly();

                if (mMaxPageNo == -1) {
                    mMaxPageNo = response.getPlaceList().getPageCount();
                    mRecommendedList.addAll(response.getBestList());
                    placeIds = response.getPlaceIds();

                }
                if (null!=mAdapter && !loadMore){
                    mAdapter.showPageEnd(false);
                }
                mSceneList.addAll(response.getPlaceList().getResult());

                if (firstLoad) {

                    mArticleModel = response.getArticleModel();
                    mSceneInfo = response.getSceneInfo();
                    mAdapter = new SceneDetailAdapter(getApplicationContext(), mSceneList, mRecommendedList, mArticleModel, mSceneInfo, new SceneDetailAdapter.OnSceneItemClickListener() {
                        @Override
                        public void onArticleClick() {

                        }

                        @Override
                        public void onPlaceClick(int pid) {
                            Intent intent = new Intent(SceneDetailActivity.this, QuchuDetailsActivity.class);
                            intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, pid);
                            startActivity(intent);
                        }
                    });
                    rv.setAdapter(mAdapter);
                } else {
                    mAdapter.notifyDataSetChanged();
                }
                mSwipeRefreshLayout.setRefreshing(false);
                mLoadingListener.loadingComplete();


            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {
                DialogUtil.dismissProgessDirectly();
                if (!loadMore) {
                    errorView.showViewDefault(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogUtil.showProgess(SceneDetailActivity.this, "加载中");
                            getData(true, false);
                        }
                    });
                }
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                mLoadingListener.loadingComplete();

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        mActivityStop = false;
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        mActivityStop = true;
        super.onStop();
    }

    @Subscribe
    public void onMessageEvent(QuchuEventModel event) {
        if (null == event) {
            return;
        }
        switch (event.getFlag()) {
            case EventFlags.EVENT_GOTO_HOME_PAGE:
                finish();
                break;
        }
    }

    public static void enterActivity(Activity from, int sceneId, String sceneName, boolean favorite) {
        Intent intent = new Intent(from, SceneDetailActivity.class);
        intent.putExtra(BUNDLE_KEY_SCENE_ID, sceneId);
        intent.putExtra(BUNDLE_KEY_SCENE_NAME, sceneName);
        intent.putExtra(BUNDLE_KEY_SCENE_IS_FAVORITE, favorite);
        from.startActivity(intent);
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    @Override
    public void onRefresh() {
        if (NetUtil.isNetworkConnected(getApplicationContext())) {
            getData();
        } else {
            Toast.makeText(getApplicationContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
