package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.AtmosphereItemModel;
import co.quchu.quchu.presenter.AtmospherePresenter;
import co.quchu.quchu.view.adapter.AtmAdapter;

/**
 * AtmosphereActivity
 * User: Chenhs
 * Date: 2015-10-29
 */
public class AtmosphereActivity extends BaseActivity {
    @Bind(R.id.atmosphere_rv)
    RecyclerView atmosphereRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atmosphere);
        ButterKnife.bind(this);
        atmosphereRv.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager  mLayoutManager = new LinearLayoutManager(this);
        atmosphereRv.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        AtmospherePresenter.getAtmData(this, new AtmospherePresenter.AtmosphereListener() {
            @Override
            public void onSuccess(ArrayList<AtmosphereItemModel> arrayList) {
                atmosphereRv.setAdapter(new AtmAdapter(AtmosphereActivity.this,arrayList));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
