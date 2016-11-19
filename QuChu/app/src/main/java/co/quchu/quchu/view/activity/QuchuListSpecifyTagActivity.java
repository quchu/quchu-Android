package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.CommentModel;
import co.quchu.quchu.model.PagerModel;
import co.quchu.quchu.presenter.CommentsPresenter;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.view.adapter.CommentAdapter;
import co.quchu.quchu.widget.EndlessRecyclerOnScrollListener;
import com.android.volley.VolleyError;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.NearbyItemModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.presenter.NearbyPresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.adapter.NearbyAdapter;
import co.quchu.quchu.widget.SpacesItemDecoration;

/**
 * Created by Nico on 16/5/25.
 */
public class QuchuListSpecifyTagActivity extends BaseActivity {


    public static String BUNDLE_KEY_TAG_ID = "BUNDLE_KEY_TAG_ID";
    public static String BUNDLE_KEY_TAG_NAME = "BUNDLE_KEY_TAG_NAME";
    public static String BUNDLE_KEY_TAG_QUCHU_NAME = "BUNDLE_KEY_TAG_QUCHU_NAME";
    public static String BUNDLE_KEY_DATA_TYPE = "BUNDLE_KEY_DATA_TYPE";

    @Override
    protected String getPageNameCN() {
        return getString(R.string.pname_specify_tags);
    }
    private int mTagId = 0;
    private int mDataType = -1;
    private String mTagName;

    private String mQuchuName;
    private int mMaxPageNo = -1;
    private int mPageNo = 1;
    private boolean mActivityStop = false;

    private EndlessRecyclerOnScrollListener mLoadingListener;

    @Bind(R.id.rv)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    NearbyAdapter mAdapter;
    private List<NearbyItemModel> mData = new ArrayList<>();


    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_recyclerview);
        ButterKnife.bind(this);
        mTagId = getIntent().getIntExtra(BUNDLE_KEY_TAG_ID, -1);
        mTagName = getIntent().getStringExtra(BUNDLE_KEY_TAG_NAME);
        mQuchuName = getIntent().getStringExtra(BUNDLE_KEY_TAG_QUCHU_NAME);
        mDataType = getIntent().getIntExtra(BUNDLE_KEY_DATA_TYPE,-1);
        ArrayMap<String,Object> params = new ArrayMap<>();
        params.put("趣处名称",mQuchuName);
        params.put("标签名称",mTagName);
        ZGEvent(params,"点击标签查询");
        getEnhancedToolbar().getTitleTv().setText(mTagName);

        mAdapter = new NearbyAdapter(mData, new NearbyAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {


                ArrayMap<String,Object> params = new ArrayMap<>();
                params.put("趣处名称",mData.get(position).getName());
                params.put("入口名称",getPageNameCN());
                ZGEvent(params,"进入趣处详情页");

                Intent intent = new Intent(QuchuListSpecifyTagActivity.this,QuchuDetailsActivity.class);
                intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID,mData.get(position).getPlaceId());
                startActivity(intent);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                getData(false,true);
            }
        });

        mLoadingListener = new EndlessRecyclerOnScrollListener( mRecyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int current_page) {
                getData(false, true);
            }
        };
        mRecyclerView.addOnScrollListener(mLoadingListener);

        DialogUtil.showProgess(QuchuListSpecifyTagActivity.this, R.string.loading_dialog_text);
        getData(true, false);
    }



    private void getData(final boolean firstLoad, final boolean loadMore) {
        if (firstLoad) {
            mData.clear();
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
        DialogUtil.showProgess(QuchuListSpecifyTagActivity.this, R.string.loading_dialog_text);


        NearbyPresenter.getQuchuListViaTagId(getApplicationContext(),mDataType, mTagId, SPUtils.getCityId(), String.valueOf(SPUtils.getLatitude()),
            String.valueOf(SPUtils.getLongitude()), new CommonListener<PagerModel>() {
                @Override public void successListener(PagerModel response) {
                    DialogUtil.dismissProgessDirectly();
                    System.out.println(response.getResult().size()+" |||");
                    mData.addAll(response.getResult());
                    if (mMaxPageNo == -1) {
                        mMaxPageNo = response.getPageCount();
                    }
                    if (null!=mAdapter && !loadMore){
                        mAdapter.showPageEnd(false);
                    }else{
                        mAdapter.notifyDataSetChanged();
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                    mLoadingListener.loadingComplete();
                }

                @Override
                public void errorListener(VolleyError error, String exception, String msg) {
                    DialogUtil.dismissProgessDirectly();
                    if (!loadMore) {
                        //errorView.showViewDefault(new View.OnClickListener() {
                        //    @Override
                        //    public void onClick(View v) {
                        //        DialogUtil.showProgess(getActivity(), "加载中");
                        //        getData(true, false);
                        //    }
                        //});
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
        if (event.getFlag()== EventFlags.EVENT_GOTO_HOME_PAGE) {
            finish();
        }
    }
}
