package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.DiscoverModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.view.adapter.DiscoverAdapter;

/**
 * DiscoverActivity
 * User: Chenhs
 * Date: 2015-11-04
 * 我的发现
 */
public class DiscoverActivity extends BaseActivity {

    @Bind(R.id.title_content_tv)
    TextView titleContentTv;

    @Bind(R.id.atmosphere_rv)
    RecyclerView atmosphereRv;
    @Bind(R.id.atmosphere_empty_view_fl)
    FrameLayout atmosphereEmptyViewFl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atmosphere);
        ButterKnife.bind(this);
        initTitleBar();
        titleContentTv.setText(getTitle());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        atmosphereRv.setLayoutManager(mLayoutManager);
        initDiscoverData(1);

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void initDiscoverData(int pageNo) {
        NetService.get(this, String.format(NetApi.getProposalPlaceList, pageNo), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                LogUtils.json("initDiscoverData==" + response);
                if (response != null) {
                    Gson gson = new Gson();
                    DiscoverModel model = gson.fromJson(response.toString(), DiscoverModel.class);
                    if (model != null && model.getResult().size() > 0) {
                        atmosphereRv.setAdapter(new DiscoverAdapter(DiscoverActivity.this, model.getResult()));
                        atmosphereEmptyViewFl.setVisibility(View.GONE);
                        atmosphereRv.setVisibility(View.VISIBLE);
                    } else {
                        atmosphereEmptyViewFl.setVisibility(View.VISIBLE);
                        atmosphereRv.setVisibility(View.GONE);
                    }
                } else {
                    atmosphereEmptyViewFl.setVisibility(View.VISIBLE);
                    atmosphereRv.setVisibility(View.GONE);
                }


            }

            @Override
            public boolean onError(String error) {
                return false;
            }
        });
    }

}
