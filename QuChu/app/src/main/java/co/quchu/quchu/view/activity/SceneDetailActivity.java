package co.quchu.quchu.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.ArticleDetailModel;
import co.quchu.quchu.model.PagerModel;
import co.quchu.quchu.model.SceneDetailModel;
import co.quchu.quchu.model.SceneModel;
import co.quchu.quchu.presenter.ArticlePresenter;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.ScenePresenter;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.adapter.ArticleDetailAdapter;
import co.quchu.quchu.view.adapter.CommonItemClickListener;
import co.quchu.quchu.view.adapter.SceneDetailAdapter;

/**
 * Created by Nico on 16/7/11.
 */
public class SceneDetailActivity extends BaseActivity {

    private static final String BUNDLE_KEY_SCENE_ID = "BUNDLE_KEY_SCENE_ID";
    private static final String BUNDLE_KEY_SCENE_NAME = "BUNDLE_KEY_SCENE_NAME";
    private static final String BUNDLE_KEY_SCENE_IS_FAVORITE = "BUNDLE_KEY_SCENE_IS_FAVORITE";



    @Bind(R.id.rv)
    RecyclerView rv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_recyclerview);
        ButterKnife.bind(this);

        int articleId = getIntent().getIntExtra(BUNDLE_KEY_SCENE_ID,-1);
        String name = getIntent().getStringExtra(BUNDLE_KEY_SCENE_NAME);
        boolean isFavorite = getIntent().getBooleanExtra(BUNDLE_KEY_SCENE_IS_FAVORITE,false);


        getEnhancedToolbar().getTitleTv().setText(name);
        getEnhancedToolbar().getRightTv().setText(isFavorite?"取消收藏":"收藏");

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(mLayoutManager);


        ScenePresenter.getSceneDetail(getApplicationContext(), articleId, SPUtils.getCityId(), 1, String.valueOf(SPUtils.getLatitude()), String.valueOf(SPUtils.getLongitude()), null, new CommonListener<SceneDetailModel>() {
                @Override
                public void successListener(SceneDetailModel response) {
                    rv.setAdapter(new SceneDetailAdapter(getApplicationContext(), response, new CommonItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position) {

                        }
                    }));
                }

                @Override
                public void errorListener(VolleyError error, String exception, String msg) {

                }
        });
//        ScenePresenter.getSceneDetail(getApplicationContext(), SPUtils.getCityId(), 1, articleId, new CommonListener<ArticleDetailModel>() {
//            @Override
//            public void successListener(final ArticleDetailModel response) {
//                rv.setAdapter(new ArticleDetailAdapter(getApplicationContext(), response.getPlaceList().getResult(), response.getArticle(), new CommonItemClickListener() {
//                    @Override
//                    public void onItemClick(View v, int position) {
//                        Intent intent = new Intent(SceneDetailActivity.this, QuchuDetailsActivity.class);
//                        intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, response.getPlaceList().getResult().get(position).getPlaceId());
//                        startActivity(intent);
//                    }
//                }));
//            }
//
//            @Override
//            public void errorListener(VolleyError error, String exception, String msg) {
//
//            }
//        });


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
}
