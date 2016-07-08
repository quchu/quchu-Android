package co.quchu.quchu.view.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.VolleyError;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.ArticleDetailModel;
import co.quchu.quchu.presenter.ArticlePresenter;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.adapter.ArticleDetailAdapter;
import co.quchu.quchu.view.adapter.CommonItemClickListener;

/**
 * Created by Nico on 16/7/8.
 */
public class ArticleDetailActivity extends BaseActivity {

    private static final String BUNDLE_KEY_ARTICLE_ID = "BUNDLE_KEY_ARTICLE_ID";
    @Bind(R.id.rv)
    RecyclerView rv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        ButterKnife.bind(this);

        String articleId = getIntent().getStringExtra(BUNDLE_KEY_ARTICLE_ID);


        ArticlePresenter.getArticleById(getApplicationContext(), SPUtils.getCityId(), 1, articleId, new CommonListener<ArticleDetailModel>() {
            @Override
            public void successListener(final ArticleDetailModel response) {
                rv.setAdapter(new ArticleDetailAdapter(getApplicationContext(), response.getPlaceList().getResult(), response.getArticle(), new CommonItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        Intent intent = new Intent(ArticleDetailActivity.this, QuchuDetailsActivity.class);
                        intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, response.getPlaceList().getResult().get(position).getPlaceId());
                        startActivity(intent);
                    }
                }));
            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {

            }
        });


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
}
