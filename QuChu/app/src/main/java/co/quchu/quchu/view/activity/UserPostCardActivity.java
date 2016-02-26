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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.PlacePostCardModel;
import co.quchu.quchu.model.PostCardItemModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.adapter.PlacePostCardListAdapter;

/**
 * PlacePostCardActivity
 * User: Chenhs
 * Date: 2015-12-16
 * 用户的明信片列表
 */
public class UserPostCardActivity extends BaseActivity {
    @Bind(R.id.title_content_tv)
    TextView titleContentTv;

    @Bind(R.id.place_postcard_center_rv)
    RecyclerView placePostcardCenterRv;
    @Bind(R.id.place_postcard_hint_fl)
    FrameLayout placePostcardHintFl;
    private int userId = 0;
    PlacePostCardModel model;
    PlacePostCardListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_postcard);
        ButterKnife.bind(this);
        titleContentTv.setText(getTitle());
        initTitleBar();
        placePostcardHintFl.setVisibility(View.INVISIBLE);
        userId = getIntent().getIntExtra("userId", 0);
        initPostCardData(1);

        adapter = new PlacePostCardListAdapter(UserPostCardActivity.this, new PlacePostCardListAdapter.CardClickListener() {

            @Override
            public void onCardLick(View view, int position) {
                switch (view.getId()) {
                    case R.id.item_recommend_card_reply_rl:
                        startActivity(new Intent(UserPostCardActivity.this, PostCardDetailActivity.class).putExtra("cInfo", model.getPage().getResult().get(position)));
                        break;
                }
            }
        });
        placePostcardCenterRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        placePostcardCenterRv.setAdapter(adapter);
    }

    private void initPostCardData(int pageNos) {
        DialogUtil.showProgess(this, R.string.loading_dialog_text);
        NetService.get(this, String.format(NetApi.getUserCardList, userId, pageNos), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json(";;;;respone=" + response);
                if ((response.has("data")) || response.has("msg")) {
                    placePostcardHintFl.setVisibility(View.VISIBLE);
                    placePostcardCenterRv.setVisibility(View.GONE);
                } else {
                    Gson gson = new Gson();
                    try {
                        if (response.has("result") && !StringUtils.isEmpty(response.getString("result"))) {
                            JSONArray array = response.getJSONArray("result");
                            ArrayList<PostCardItemModel> pArray = new ArrayList<PostCardItemModel>();
                            PostCardItemModel pModel;
                            for (int i = 0; i < array.length(); i++) {
                                pModel = gson.fromJson(array.getString(i), PostCardItemModel.class);
                                pArray.add(pModel);
                            }
                            //  model = gson.fromJson(response.toString(), PlacePostCardModel.class);
                            placePostcardCenterRv.setVisibility(View.VISIBLE);
                            placePostcardHintFl.setVisibility(View.INVISIBLE);
                            adapter.changeDataSet(pArray);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                DialogUtil.dismissProgess();
            }

            @Override
            public boolean onError(String error) {
                DialogUtil.dismissProgess();
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("PlacePostCardActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("PlacePostCardActivity");
        MobclickAgent.onPause(this);
    }
}
