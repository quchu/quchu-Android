package co.quchu.quchu.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.dialog.ShareDialogFg;
import co.quchu.quchu.model.ArticleDetailModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.model.SimpleArticleModel;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.presenter.ArticlePresenter;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.adapter.ArticleDetailAdapter;
import co.quchu.quchu.view.adapter.CommonItemClickListener;

/**
 * Created by Nico on 16/7/8.
 */
public class ArticleDetailActivity extends BaseActivity {

    private static final String BUNDLE_KEY_ARTICLE_ID = "BUNDLE_KEY_ARTICLE_ID";
    private static final String BUNDLE_KEY_ARTICLE_TITLE = "BUNDLE_KEY_ARTICLE_TITLE";

    @Bind(R.id.rv)
    RecyclerView rv;
    @Bind(R.id.ivFavorite)
    ImageView ivFavorite;
    @Bind(R.id.ivShare)
    ImageView ivShare;
    ArticleDetailModel mArticleDetailModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        ButterKnife.bind(this);
        getEnhancedToolbar();


        String articleId = getIntent().getStringExtra(BUNDLE_KEY_ARTICLE_ID);
        String articleTitle = getIntent().getStringExtra(BUNDLE_KEY_ARTICLE_TITLE);
        getEnhancedToolbar().getTitleTv().setText(articleTitle);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(mLayoutManager);
        getData(articleId);
    }

    private void getData(String id) {
        DialogUtil.showProgess(this,R.string.loading_dialog_text);

        ArticlePresenter.getArticleById(getApplicationContext(), SPUtils.getCityId(), 1, id, new CommonListener<ArticleDetailModel>() {
            @Override
            public void successListener(final ArticleDetailModel response) {
                DialogUtil.dismissProgessDirectly();

                mArticleDetailModel = response;
                rv.setAdapter(new ArticleDetailAdapter(getApplicationContext(), response.getPlaceList().getResult(), response.getArticle(), new CommonItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        Intent intent = new Intent(ArticleDetailActivity.this, QuchuDetailsActivity.class);
                        intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, response.getPlaceList().getResult().get(position).getPlaceId());
                        startActivity(intent);
                    }
                }));

                SimpleArticleModel mSimpleArticleModel = mArticleDetailModel.getArticle();


                ivFavorite.setImageResource(mArticleDetailModel.getArticle().isFavorite()? R.mipmap.ic_favorite_hl:R.mipmap.ic_favorite);
            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {
                DialogUtil.dismissProgessDirectly();

            }
        });
    }

    boolean mFavoriteProgressRunning = false;
    private void setFavorite(){
        if (null==mArticleDetailModel || null==mArticleDetailModel.getArticle()){
            return;
        }

        if (mFavoriteProgressRunning){
            Toast.makeText(getApplicationContext(),R.string.process_running_please_wait,Toast.LENGTH_SHORT).show();;
        }
        mFavoriteProgressRunning = true;

            int aid = mArticleDetailModel.getArticle().getArticleId();
            if (!mArticleDetailModel.getArticle().isFavorite()){
                ArticlePresenter.addFavoriteArticle(getApplicationContext(), aid, new CommonListener() {
                    @Override
                    public void successListener(Object response) {
                        mFavoriteProgressRunning = false;
                        Toast.makeText(getApplicationContext(),R.string.add_to_favorite_article_success,Toast.LENGTH_SHORT).show();
                        ivFavorite.setImageResource(R.mipmap.ic_favorite);
                    }

                    @Override
                    public void errorListener(VolleyError error, String exception, String msg) {
                        Toast.makeText(getApplicationContext(),R.string.add_to_favorite_article_fail,Toast.LENGTH_SHORT).show();
                        mFavoriteProgressRunning = false;
                    }
                });
            }else{
                ArticlePresenter.delFavoriteArticle(getApplicationContext(), aid, new CommonListener() {
                    @Override
                    public void successListener(Object response) {
                        mFavoriteProgressRunning = false;
                        Toast.makeText(getApplicationContext(),R.string.del_to_favorite_article_success,Toast.LENGTH_SHORT).show();
                        ivFavorite.setImageResource(R.mipmap.ic_favorite_hl);
                    }

                    @Override
                    public void errorListener(VolleyError error, String exception, String msg) {
                        Toast.makeText(getApplicationContext(),R.string.del_to_favorite_article_success,Toast.LENGTH_SHORT).show();
                        mFavoriteProgressRunning = false;
                    }
                });
            }


    }

    @OnClick({R.id.ivFavorite,R.id.ivShare})
    public void detailClick(View v) {
        switch (v.getId()){
            case R.id.ivFavorite:
                setFavorite();
                break;
            case R.id.ivShare:

                if (null==mArticleDetailModel || null==mArticleDetailModel.getArticle()){
                    return;
                }
                String url = NetApi.shareArticleUrl + mArticleDetailModel.getArticle().getArticleId();
                ShareDialogFg shareDialogFg = ShareDialogFg.newInstance(url,"趣处");
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
        if (null==event){
            return;
        }
        switch (event.getFlag()){
            case EventFlags.EVENT_GOTO_HOME_PAGE:
                finish();
                break;
        }
    }

    public static void enterActivity(Activity from, String articleId,String articleTitle) {
        Intent intent = new Intent(from, ArticleDetailActivity.class);
        intent.putExtra(BUNDLE_KEY_ARTICLE_ID, articleId);
        intent.putExtra(BUNDLE_KEY_ARTICLE_TITLE, articleTitle);
        from.startActivity(intent);
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

}
