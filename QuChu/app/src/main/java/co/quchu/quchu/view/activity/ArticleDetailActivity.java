package co.quchu.quchu.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.presenter.ArticlePresenter;
import co.quchu.quchu.utils.SPUtils;

/**
 * Created by Nico on 16/7/8.
 */
public class ArticleDetailActivity extends BaseActivity {

    private static final String BUNDLE_KEY_ARTICLE_ID = "BUNDLE_KEY_ARTICLE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String articleId = getIntent().getStringExtra(BUNDLE_KEY_ARTICLE_ID);

        ArticlePresenter.getArticleById(getApplicationContext(), SPUtils.getCityId(),1,articleId);


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
