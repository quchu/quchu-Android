package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.FavoriteModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.view.adapter.FavoriteGridAdapter;
import co.quchu.quchu.widget.textcounter.CounterView;

/**
 * FavoriteActivity
 * User: Chenhs
 * Date: 2015-12-14
 */
public class FavoriteActivity extends BaseActivity {
    @Bind(R.id.favorite_place_name_tv)
    TextView favoritePlaceNameTv;
    @Bind(R.id.title_content_tv)
    TextView titleContentTv;
    @Bind(R.id.favorite_place_counter_cv)
    CounterView favoritePlaceCounterCv;
    @Bind(R.id.favorite_place_gv)
    GridView favoritePlaceGv;
    @Bind(R.id.favorite_postcard_name_tv)
    TextView favoritePostcardNameTv;
    @Bind(R.id.favorite_postcard_counter_cv)
    CounterView favoritePostcardCounterCv;
    @Bind(R.id.favorite_postcard_gv)
    GridView favoritePostcardGv;
    @Bind(R.id.favorite_place_cv)
    CardView favoritePlaceCv;
    @Bind(R.id.favorite_postcard_cv)
    CardView favoritePostcardCv;
    @Bind(R.id.favorite_postcard_gvcv)
    CardView favoritePostcardGvcv;
    @Bind(R.id.favorite_place_name_gvcv)
    CardView favoritePlaceNameGvcv;
    private FavoriteModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ButterKnife.bind(this);
        titleContentTv.setText(getTitle());
        initTitleBar();
        initFavoriteData();
    }

    private void initFavoriteData() {
        DialogUtil.showProgess(this, getResources().getString(R.string.loading_dialog_text));
        NetService.get(this, NetApi.getFavorite, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json("Favorite==" + response);
                if (!response.has("msg")) {
                    Gson gson = new Gson();
                    model = gson.fromJson(response.toString(), FavoriteModel.class);
                    if (model != null) {
                        favoritePlaceGv.setAdapter(new FavoriteGridAdapter(FavoriteActivity.this, model, true));
                        favoritePostcardGv.setAdapter(new FavoriteGridAdapter(FavoriteActivity.this, model, false));
                        initCountView();
                        favoritePlaceCounterCv.start();
                        favoritePostcardCounterCv.start();
                        favoritePlaceGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                startActivity(new Intent(FavoriteActivity.this, FavoritePlaceActivity.class));
                            }
                        });
                        favoritePostcardGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //跳转
                                if (model.getCard() != null && model.getCard().getData().size() > 0)
                                    startActivity(new Intent(FavoriteActivity.this, PostCardActivity.class).putExtra("isFavoritePostCard", true));
                                else
                                    Toast.makeText(FavoriteActivity.this, "暂未收藏明信片!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    DialogUtil.dismissProgess();
                }
            }

            @Override
            public boolean onError(String error) {
                DialogUtil.dismissProgess();
                return false;
            }
        });
    }

    private void initCountView() {
        favoritePlaceCounterCv.setEndValue(model.getPlace().getCount());
        favoritePlaceCounterCv.setIncrement(model.getPlace().getCount() % 10);
        favoritePlaceCounterCv.setTimeInterval(80); // the time interval (ms) at which the text changes
        favoritePostcardCounterCv.setEndValue(model.getCard().getCount());
        favoritePostcardCounterCv.setIncrement(model.getCard().getCount() % 10);
        favoritePostcardCounterCv.setTimeInterval(80); // the time interval (ms) at which the text changes

    }

    @OnClick({R.id.favorite_place_cv, R.id.favorite_postcard_cv, R.id.favorite_postcard_gvcv, R.id.favorite_place_name_gvcv})
    public void favoriteClick(View v) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
        switch (v.getId()) {
            case R.id.favorite_postcard_cv:
            case R.id.favorite_postcard_gvcv:
                if (model.getCard() != null && model.getCard().getData().size() > 0)
                    startActivity(new Intent(this, PostCardActivity.class).putExtra("isFavoritePostCard", true));
                else
                    Toast.makeText(this, "暂未收藏明信片!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.favorite_place_cv:
            case R.id.favorite_place_name_gvcv:
                startActivity(new Intent(this, FavoritePlaceActivity.class));
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd("FavoriteActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("FavoriteActivity");
    }
}
