package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.AtmosphereItemModel;
import co.quchu.quchu.presenter.AtmospherePresenter;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.view.adapter.AtmAdapter;

/**
 * AtmosphereActivity
 * User: Chenhs
 * Date: 2015-10-29
 * 氛围
 */
public class AtmosphereActivity extends BaseActivity {
    /** title ***/
    @Bind(R.id.title_back_rl)
    RelativeLayout titleBackRL;
    @Bind(R.id.title_more_rl)
    RelativeLayout titleMoreRl;
    @Bind(R.id.title_content_tv)
    TextView title_content_tv;
    /** title ***/
    @Bind(R.id.atmosphere_rv)
    RecyclerView atmosphereRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atmosphere);
        ButterKnife.bind(this);
        atmosphereRv.setHasFixedSize(true);
        title_content_tv.setText(getTitle());
        // use a linear layout manager
        LinearLayoutManager  mLayoutManager = new LinearLayoutManager(this);
        atmosphereRv.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        AtmospherePresenter.getAtmData(this, new AtmospherePresenter.AtmosphereListener() {
            @Override
            public void onSuccess(ArrayList<AtmosphereItemModel> arrayList) {
           AtmAdapter atmAdapter=     new AtmAdapter(AtmosphereActivity.this, arrayList);
                atmosphereRv.setAdapter(atmAdapter);
                atmAdapter.setOnitemClickListener(new AtmAdapter.AtmItemClickListener() {
                    @Override
                    public void itemClickListener(View v, int position) {
                        LogUtils.json("/////position==" + position);
                    }
                });
            }
        });
    }

    @OnClick({R.id.title_back_rl})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.title_back_rl:
                this.finish();
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}
