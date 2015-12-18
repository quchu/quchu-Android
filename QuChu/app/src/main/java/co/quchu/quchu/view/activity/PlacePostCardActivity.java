package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.PlacePostCardModel;
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
 * 趣处中的明信片
 */
public class PlacePostCardActivity extends BaseActivity {
    @Bind(R.id.title_content_tv)
    TextView titleContentTv;
    @Bind(R.id.place_postcard_bottom_text_tv)
    TextView placePostcardBottomTextTv;
    @Bind(R.id.place_postcard_bottom_botton_rl)
    RelativeLayout placePostcardBottomBottonRl;
    @Bind(R.id.place_postcard_center_rv)
    RecyclerView placePostcardCenterRv;
    @Bind(R.id.place_postcard_hint_fl)
    FrameLayout placePostcardHintFl;
    private int pId = 0, pageNo = 1;
    private String pName = "";
    PlacePostCardModel model;
    PlacePostCardListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_postcard);
        ButterKnife.bind(this);
        titleContentTv.setText(getTitle());
        pId = getIntent().getIntExtra("pId", 2);
        pName = getIntent().getStringExtra("pName");
        initPostCardData(1);

        adapter = new PlacePostCardListAdapter(PlacePostCardActivity.this,new PlacePostCardListAdapter.CardClickListener(){

            @Override
            public void onCardLick(View view, int position) {

            }
        });
        placePostcardCenterRv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        placePostcardCenterRv.setAdapter(adapter);
    }


    private void initPostCardData(int pageNos) {
        NetService.get(this, String.format(NetApi.getPlaceCardList, pageNos, pId), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json(";;;;respone=" + response);
                try {
                    if (response.has("data")&& (StringUtils.isEmpty(response.getString("data")) ||"null".equals(response.getString("data")))) {
                        placePostcardBottomTextTv.setText(getResources().getString(R.string.place_postcard_add_new_postcard));
                        placePostcardHintFl.setVisibility(View.VISIBLE);
                    } else {
                        Gson gson = new Gson();
                        model = gson.fromJson(response.toString(), PlacePostCardModel.class);
                        placePostcardHintFl.setVisibility(View.INVISIBLE);
                        adapter.changeDataSet(model.getPage().getResult());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public boolean onError(String error) {
                return false;
            }
        });

    }

    @OnClick({R.id.place_postcard_bottom_botton_rl})
    public void placePostCardClick(View v) {
        switch (v.getId()) {
            case R.id.place_postcard_bottom_botton_rl:
                Intent intent = new Intent();
                intent.putExtra("pName", pName);
                intent.putExtra("pId", pId);
                intent.setClass(this, AddPostCardActivity.class);
                startActivity(intent);
                break;
        }
    }
}
