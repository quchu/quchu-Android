package co.quchu.quchu.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.dialog.ShareDialogFg;
import co.quchu.quchu.model.ArticleDetailModel;
import co.quchu.quchu.model.ArticleWithBannerModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.model.SimpleArticleModel;
import co.quchu.quchu.model.SimplePlaceModel;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.ArticlePresenter;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.adapter.ArticleDetailAdapter;
import co.quchu.quchu.view.adapter.CommonItemClickListener;
import co.quchu.quchu.widget.EndlessRecyclerOnScrollListener;
import co.quchu.quchu.widget.ErrorView;

/**
 * Created by Nico on 16/7/8.
 */
public class ArticleDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String BUNDLE_KEY_ARTICLE_ID = "BUNDLE_KEY_ARTICLE_ID";
    private static final String BUNDLE_KEY_ARTICLE_TITLE = "BUNDLE_KEY_ARTICLE_TITLE";

    @Bind(R.id.rv)
    RecyclerView rv;
    @Bind(R.id.ivFavorite)
    ImageView ivFavorite;
    @Bind(R.id.ivShare)
    ImageView ivShare;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.errorView)
    ErrorView errorView;

    ArticleDetailModel mArticleDetailModel;
    String articleId;
    String articleTitle;

    private int mMaxPageNo = -1;
    private int mPageNo = 1;
    private EndlessRecyclerOnScrollListener mListener;
    public boolean mLoadMoreRunning = false;
    private ArticleDetailAdapter mAdapter;
    private List<SimplePlaceModel> mSimplePlaceModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        ButterKnife.bind(this);
        getEnhancedToolbar();


        articleId = getIntent().getStringExtra(BUNDLE_KEY_ARTICLE_ID);
        articleTitle = getIntent().getStringExtra(BUNDLE_KEY_ARTICLE_TITLE);
        getEnhancedToolbar().getTitleTv().setText(articleTitle);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(mLayoutManager);
        getData(articleId,true);
        if (!NetUtil.isNetworkConnected(getApplicationContext()) && mArticleDetailModel==null){
            errorView.showViewDefault(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtil.showProgess(ArticleDetailActivity.this, "加载中");
                    getData(articleId,true);
                }
            });
        }

        mListener = new EndlessRecyclerOnScrollListener((LinearLayoutManager) rv.getLayoutManager()) {
            @Override
            public void onLoadMore(int current_page) {
                loadMore(articleId);
            }
        };
        rv.addOnScrollListener(mListener);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void loadMore(String id) {

        if (mLoadMoreRunning) {
            Toast.makeText(ArticleDetailActivity.this, R.string.process_running_please_wait, Toast.LENGTH_SHORT).show();
            return;
        }
        mPageNo += 1;
        if (mMaxPageNo != -1 && mMaxPageNo <= mPageNo) {
            Toast.makeText(ArticleDetailActivity.this, R.string.no_more_data, Toast.LENGTH_SHORT).show();
            return;
        }
        DialogUtil.showProgess(ArticleDetailActivity.this, R.string.loading_dialog_text);

        ArticlePresenter.getArticleById(getApplicationContext(), SPUtils.getCityId(), mPageNo, id, new CommonListener<ArticleDetailModel>() {
            @Override
            public void successListener(ArticleDetailModel response) {
                mSimplePlaceModels.addAll(response.getPlaceList().getResult());
                mAdapter.notifyDataSetChanged();
                DialogUtil.dismissProgessDirectly();
                mListener.loadingComplete();
            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {
                DialogUtil.dismissProgessDirectly();
                mListener.loadingComplete();
            }
        });


    }

    private void getData(String id, final boolean firstLoad) {
        if (firstLoad){
            DialogUtil.showProgess(this,R.string.loading_dialog_text);
        }

        ArticlePresenter.getArticleById(getApplicationContext(), SPUtils.getCityId(), 1, id, new CommonListener<ArticleDetailModel>() {
            @Override
            public void successListener(final ArticleDetailModel response) {
                if (firstLoad){
                    DialogUtil.dismissProgessDirectly();
                }
                mMaxPageNo = response.getPlaceList().getPageCount();
                mArticleDetailModel = response;
                mSimplePlaceModels.clear();
                mSimplePlaceModels.addAll(response.getPlaceList().getResult());
                mAdapter = new ArticleDetailAdapter(getApplicationContext(), mSimplePlaceModels, response.getArticle(), new CommonItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        Intent intent = new Intent(ArticleDetailActivity.this, QuchuDetailsActivity.class);
                        intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, response.getPlaceList().getResult().get(position).getPlaceId());
                        startActivity(intent);
                    }
                });
                rv.setAdapter(mAdapter);

                ivFavorite.setImageResource(mArticleDetailModel.getArticle().isFavorite()? R.mipmap.ic_favorite_hl:R.mipmap.ic_favorite);
                mSwipeRefreshLayout.setRefreshing(false);
                errorView.hideView();

            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {
                if (firstLoad){
                    DialogUtil.dismissProgessDirectly();
                }
                errorView.showViewDefault(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtil.showProgess(ArticleDetailActivity.this, "加载中");
                        getData(articleId,false);
                    }
                });
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });

    }

    boolean mFavoriteProgressRunning = false;

    private void setFavorite() {
        if (null == mArticleDetailModel || null == mArticleDetailModel.getArticle()) {
            return;
        }

        if (mFavoriteProgressRunning) {
            Toast.makeText(getApplicationContext(), R.string.process_running_please_wait, Toast.LENGTH_SHORT).show();
            ;
        }
        mFavoriteProgressRunning = true;

        int aid = mArticleDetailModel.getArticle().getArticleId();
        if (!mArticleDetailModel.getArticle().isFavorite()) {
            ArticlePresenter.addFavoriteArticle(getApplicationContext(), aid, new CommonListener() {
                @Override
                public void successListener(Object response) {
                    mFavoriteProgressRunning = false;
                    Toast.makeText(getApplicationContext(), R.string.add_to_favorite_article_success, Toast.LENGTH_SHORT).show();
                    ivFavorite.setImageResource(R.mipmap.ic_favorite_hl);
                    mArticleDetailModel.getArticle().setFavorite(true);
                }

                @Override
                public void errorListener(VolleyError error, String exception, String msg) {
                    Toast.makeText(getApplicationContext(), R.string.add_to_favorite_article_fail, Toast.LENGTH_SHORT).show();
                    mFavoriteProgressRunning = false;
                }
            });
        } else {
            ArticlePresenter.delFavoriteArticle(getApplicationContext(), aid, new CommonListener() {
                @Override
                public void successListener(Object response) {
                    mFavoriteProgressRunning = false;
                    Toast.makeText(getApplicationContext(), R.string.del_to_favorite_article_success, Toast.LENGTH_SHORT).show();
                    ivFavorite.setImageResource(R.mipmap.ic_favorite);
                    mArticleDetailModel.getArticle().setFavorite(false);
                }

                @Override
                public void errorListener(VolleyError error, String exception, String msg) {
                    Toast.makeText(getApplicationContext(), R.string.del_to_favorite_article_success, Toast.LENGTH_SHORT).show();
                    mFavoriteProgressRunning = false;
                }
            });
        }


    }

    @OnClick({R.id.ivFavorite, R.id.ivShare})
    public void detailClick(View v) {
        switch (v.getId()) {
            case R.id.ivFavorite:
                setFavorite();
                break;
            case R.id.ivShare:

                if (null == mArticleDetailModel || null == mArticleDetailModel.getArticle()) {
                    return;
                }
                String url = NetApi.shareArticleUrl + mArticleDetailModel.getArticle().getArticleId();
                ShareDialogFg shareDialogFg = ShareDialogFg.newInstance(url, mArticleDetailModel.getArticle().getArticleName(),mArticleDetailModel.getArticle().getImageUrl());
                shareDialogFg.show(getSupportFragmentManager(), "share_dialog");
//                ShareDialogFg shareDialogFg = new ShareDialogFg();
//                shareDialogFg.show(getSupportFragmentManager(),"share");
                break;
        }
    }

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
        if (null == event) {
            return;
        }
        switch (event.getFlag()) {
            case EventFlags.EVENT_GOTO_HOME_PAGE:
                finish();
                break;
        }
    }

    public static void enterActivity(Activity from, String articleId, String articleTitle) {
        Intent intent = new Intent(from, ArticleDetailActivity.class);
        intent.putExtra(BUNDLE_KEY_ARTICLE_ID, articleId);
        intent.putExtra(BUNDLE_KEY_ARTICLE_TITLE, articleTitle);
        from.startActivity(intent);
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    @Override
    public void onRefresh() {
        if (NetUtil.isNetworkConnected(getApplicationContext())){
            getData(articleId,false);
        }else{
            Toast.makeText(getApplicationContext(),R.string.network_error,Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
