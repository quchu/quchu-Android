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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.ShareDialogFg;
import co.quchu.quchu.model.ArticleDetailModel;
import co.quchu.quchu.model.SimpleArticleModel;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.presenter.ArticlePresenter;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.adapter.ArticleDetailAdapter;
import co.quchu.quchu.view.adapter.CommonItemClickListener;

/**
 * Created by Nico on 16/7/8.
 */
public class ArticleDetailActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener {

    private static final String BUNDLE_KEY_ARTICLE_ID = "BUNDLE_KEY_ARTICLE_ID";
    @Bind(R.id.rv)
    RecyclerView rv;
    @Bind(R.id.sdvCover)
    SimpleDraweeView itemClassifyImageSdv;
    @Bind(R.id.tvTitle)
    TextView tvTitle;
    @Bind(R.id.tvDescription)
    TextView tvDescription;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
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


        String articleId = getIntent().getStringExtra(BUNDLE_KEY_ARTICLE_ID);

        getEnhancedToolbar().getLeftIv().setImageResource(R.mipmap.ic_forward);
        getEnhancedToolbar().getLeftIv().setRotation(180);
        getEnhancedToolbar().getTitleTv().setAlpha(0f);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(mLayoutManager);
        ArticlePresenter.getArticleById(getApplicationContext(), SPUtils.getCityId(), 1, articleId, new CommonListener<ArticleDetailModel>() {
            @Override
            public void successListener(final ArticleDetailModel response) {
                mArticleDetailModel = response;
                rv.setAdapter(new ArticleDetailAdapter(getApplicationContext(), mArticleDetailModel.getPlaceList().getResult(),new CommonItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        Intent intent = new Intent(ArticleDetailActivity.this, QuchuDetailsActivity.class);
                        intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, mArticleDetailModel.getPlaceList().getResult().get(position).getPlaceId());
                        startActivity(intent);
                    }
                }));

                SimpleArticleModel mSimpleArticleModel = mArticleDetailModel.getArticle();
                tvDescription.setText(mSimpleArticleModel.getArticleComtent());
                tvTitle.setText(mSimpleArticleModel.getArticleName());
                itemClassifyImageSdv.setImageURI(Uri.parse(mSimpleArticleModel.getImageUrl()));

                ivFavorite.setImageResource(mArticleDetailModel.getArticle().isFavorite()? R.mipmap.ic_favorite_hl:R.mipmap.ic_favorite);
            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {

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
    protected void onResume() {
        appbar.addOnOffsetChangedListener(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        appbar.removeOnOffsetChangedListener(this);
        super.onPause();

    }

    public static void enterActivity(Activity from, String articleId) {
        Intent intent = new Intent(from, ArticleDetailActivity.class);
        intent.putExtra(BUNDLE_KEY_ARTICLE_ID, articleId);
        from.startActivity(intent);
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (appbar.getTotalScrollRange()==0 || verticalOffset==0){
            return;
        }
        float alpha = Math.abs(Float.valueOf(verticalOffset))/appbar.getTotalScrollRange();
        getEnhancedToolbar().getTitleTv().setAlpha(alpha);
        int color = (int) (255-(alpha*255));
        getEnhancedToolbar().getLeftIv().setColorFilter(Color.argb(255,color,color,color), PorterDuff.Mode.MULTIPLY);


    }
}
