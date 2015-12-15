package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
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
    private FavoritePlaceModel model;
    private FavoritePlaceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_place);
        ButterKnife.bind(this);
        initTitleBar();
        title_content_tv.setText(getTitle());
        initData(1);
        favoritePlaceRv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FavoritePlaceAdapter(FavoritePlaceActivity.this, new FavoritePlaceAdapter.CardClickListener() {
            @Override
            public void onCardLick(View view, int position) {
                switch (view.getId()) {
                    case R.id.item_recommend_card_reply_rl:
                        Intent intent = new Intent();
                        intent.putExtra("pId", model.getResult().get(position).getPid());
                        intent.setClass(FavoritePlaceActivity.this, InterestingDetailsActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
        favoritePlaceRv.setAdapter(adapter);
    }

    private void initData(int pageNo) {
        NetService.get(this, String.format(NetApi.getFavoriteList, pageNo, NetApi.FavTypePlace), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json("fav=" + response);
                if (response != null) {
                    Gson gson = new Gson();
                    model = gson.fromJson(response.toString(), FavoritePlaceModel.class);
                    adapter.changeDataSet(model.getResult());
                }
            }

            @Override
            public boolean onError(String error) {
                return false;
            }
        });
    }
}
