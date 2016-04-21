package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.ShareDialogFg;
import co.quchu.quchu.model.FavoritePlaceModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.view.adapter.FavoritePlaceAdapter;

/**
 * FavoritePlaceActivity
 * User: Chenhs
 * Date: 2015-12-15
 * 我收藏的趣处
 */
public class FavoritePlaceActivity extends BaseActivity {
    @Bind(R.id.favorite_place_rv)
    RecyclerView favoritePlaceRv;
    @Bind(R.id.title_content_tv)
    TextView title_content_tv;
    @Bind(R.id.favorite_place_empty_view)
    FrameLayout favoritePlaceEmptyView;
    private FavoritePlaceModel model;
    private FavoritePlaceAdapter adapter;
    int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_place);
        ButterKnife.bind(this);
        initTitleBar();
        userId = getIntent().getIntExtra("userId", -1);
        if (-1 == userId) {
            title_content_tv.setText(getTitle());
            initData(1);
        } else {
            getUserFavoritePlaseFromeUserId(1);
        }
        favoritePlaceRv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FavoritePlaceAdapter(FavoritePlaceActivity.this, new FavoritePlaceAdapter.CardClickListener() {
            @Override
            public void onCardLick(View view, int position) {
                switch (view.getId()) {
                    case R.id.root_cv:
                        MobclickAgent.onEvent(FavoritePlaceActivity.this,"detail_profile_c");
                        Intent intent = new Intent();
                        intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, model.getResult().get(position).getPid());
                        intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_FROM,QuchuDetailsActivity.FROM_TYPE_PROFILE);
                        intent.setClass(FavoritePlaceActivity.this, QuchuDetailsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_recommend_card_interest_rl:
                        ShareDialogFg shareDialogFg = ShareDialogFg.newInstance(model.getResult().get(position).getPid(), model.getResult().get(position).getName(), true);
                        shareDialogFg.show(getFragmentManager(), "share_place_dialog");
                        break;
                }
            }
        });
        favoritePlaceRv.setAdapter(adapter);
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    private void initData(int pageNo) {
        NetService.get(this, String.format(NetApi.getFavoriteList, pageNo, NetApi.FavTypePlace), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json("fav=" + response);

                if (response != null && (!response.has("data") || !response.has("msg"))) {
                    Gson gson = new Gson();
                    model = gson.fromJson(response.toString(), FavoritePlaceModel.class);
                    if (model != null && model.getResult().size() > 0) {
                        adapter.changeDataSet(model.getResult());
                        favoritePlaceRv.setVisibility(View.VISIBLE);
                        favoritePlaceEmptyView.setVisibility(View.GONE);
                    } else {
                        favoritePlaceEmptyView.setVisibility(View.VISIBLE);
                        favoritePlaceRv.setVisibility(View.GONE);
                    }
                } else {
                    favoritePlaceEmptyView.setVisibility(View.VISIBLE);
                    favoritePlaceRv.setVisibility(View.GONE);
                }
            }

            @Override
            public boolean onError(String error) {
                favoritePlaceEmptyView.setVisibility(View.VISIBLE);
                favoritePlaceRv.setVisibility(View.GONE);
                return false;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("FavoritePlaceActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("FavoritePlaceActivity");
    }

    private void getUserFavoritePlaseFromeUserId(int pageNum) {
        NetService.get(this, String.format(NetApi.getUsercenterFavoriteList, userId, pageNum), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json("fav=" + response);

                if (response != null && (!response.has("data") || !response.has("msg"))) {
                    Gson gson = new Gson();
                    model = gson.fromJson(response.toString(), FavoritePlaceModel.class);
                    if (model != null && model.getResult().size() > 0) {
                        adapter.changeDataSet(model.getResult());
                        favoritePlaceRv.setVisibility(View.VISIBLE);
                        favoritePlaceEmptyView.setVisibility(View.GONE);
                    } else {
                        favoritePlaceEmptyView.setVisibility(View.VISIBLE);
                        favoritePlaceRv.setVisibility(View.GONE);
                    }

                } else {
                    favoritePlaceEmptyView.setVisibility(View.VISIBLE);
                    favoritePlaceRv.setVisibility(View.GONE);
                }
            }


            @Override
            public boolean onError(String error) {
                favoritePlaceEmptyView.setVisibility(View.VISIBLE);
                favoritePlaceRv.setVisibility(View.GONE);
                return false;
            }
        });
    }
}
