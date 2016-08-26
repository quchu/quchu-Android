package co.quchu.quchu.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.CommentModel;
import co.quchu.quchu.model.DetailModel;
import co.quchu.quchu.model.PagerModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.model.SceneDetailModel;
import co.quchu.quchu.model.SceneHeaderModel;
import co.quchu.quchu.model.SceneInfoModel;
import co.quchu.quchu.model.SimpleArticleModel;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.CommentsPresenter;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.ScenePresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.adapter.CommentAdapter;
import co.quchu.quchu.view.adapter.SceneDetailAdapter;
import co.quchu.quchu.widget.EndlessRecyclerOnScrollListener;
import co.quchu.quchu.widget.ErrorView;
import com.android.volley.VolleyError;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Nico on 16/7/11.
 */
public class CommentListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {


    @Override
    protected String getPageNameCN() {
        return getString(R.string.pname_comment_list);
    }

    private static final String BUNDLE_KEY_PLACE_ID = "BUNDLE_KEY_PLACE_ID";

    @Bind(R.id.rv)
    RecyclerView rv;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.errorView)
    ErrorView errorView;

    private int sceneId;
    private int mMaxPageNo = -1;
    private int mPageNo = 1;
    private EndlessRecyclerOnScrollListener mLoadingListener;

    private List<CommentModel> mSceneList = new ArrayList<>();
    private CommentAdapter mAdapter;

    private boolean mActivityStop = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_recyclerview);
        ButterKnife.bind(this);
        sceneId = getIntent().getIntExtra(BUNDLE_KEY_PLACE_ID, -1);
        getEnhancedToolbar().getTitleTv().setText(R.string.view_comments);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(mLayoutManager);
        if (!NetUtil.isNetworkConnected(getApplicationContext()) && mAdapter == null) {
            errorView.showViewDefault(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtil.showProgess(CommentListActivity.this, "加载中");
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




    private void getData(final boolean firstLoad, final boolean loadMore) {
        if (firstLoad) {

            mSceneList.clear();
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

        CommentsPresenter.getComments(getApplicationContext(), sceneId, mPageNo, new CommonListener<PagerModel<CommentModel>>() {
            @Override
            public void successListener(PagerModel<CommentModel> response) {
                DialogUtil.dismissProgessDirectly();

                if (mMaxPageNo == -1) {
                    mMaxPageNo = response.getPageCount();
                }
                if (null!=mAdapter && !loadMore){
                    mAdapter.showPageEnd(false);
                }
                mSceneList.addAll(response.getResult());

                if (firstLoad) {
                    mAdapter = new CommentAdapter(CommentListActivity.this,mSceneList);
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
                            DialogUtil.showProgess(CommentListActivity.this, "加载中");
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
        mActivityStop = false;
    }

    @Override
    public void onStop() {
        mActivityStop = true;
        super.onStop();
    }



    public static void enterActivity(Activity from, long pid) {
        Intent intent = new Intent(from, CommentListActivity.class);
        intent.putExtra(BUNDLE_KEY_PLACE_ID, pid);
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
