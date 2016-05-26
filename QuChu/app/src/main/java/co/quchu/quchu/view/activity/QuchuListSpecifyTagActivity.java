package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.NearbyItemModel;
import co.quchu.quchu.presenter.NearbyPresenter;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.adapter.NearbyAdapter;
import co.quchu.quchu.widget.SpacesItemDecoration;

/**
 * Created by Nico on 16/5/25.
 */
public class QuchuListSpecifyTagActivity extends BaseActivity {

    private int mTagId = 0;
    private String mTagName;
    public static String BUNDLE_KEY_TAG_ID = "BUNDLE_KEY_TAG_ID";
    public static String BUNDLE_KEY_TAG_NAME = "BUNDLE_KEY_TAG_NAME";
    @Bind(R.id.rv)
    RecyclerView mRecyclerView;
    NearbyAdapter mAdapter;
    private List<NearbyItemModel> mData = new ArrayList<>();

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_recyclerview);
        ButterKnife.bind(this);
        mTagId = getIntent().getIntExtra(BUNDLE_KEY_TAG_ID, -1);
        mTagName = getIntent().getStringExtra(BUNDLE_KEY_TAG_NAME);
        getEnhancedToolbar().getTitleTv().setText("包含"+mTagName+"的趣处");

        mAdapter = new NearbyAdapter(mData, new NearbyAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(QuchuListSpecifyTagActivity.this,QuchuDetailsActivity.class);
                intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID,mData.get(position).getPlaceId());
                startActivity(intent);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.half_margin)));
        NearbyPresenter.getQuchuListViaTagId(getApplicationContext(), mTagId, SPUtils.getCityId(), String.valueOf(SPUtils.getLatitude()), String.valueOf(SPUtils.getLongitude()),
                new NearbyPresenter.getNearbyDataListener() {
            @Override
            public void getNearbyData(List<NearbyItemModel> model, int i) {
                mData.clear();
                mData.addAll(model);
                mAdapter.notifyDataSetChanged();
            }
        });

    }
}
