package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.NearbyItemModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.presenter.NearbyPresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.adapter.NearbyAdapter;
import co.quchu.quchu.widget.SpacesItemDecoration;

/**
 * Created by Nico on 16/5/25.
 */
public class QuchuListSpecifyTagActivity extends BaseActivity {


    @Override
    protected String getPageNameCN() {
        return getString(R.string.pname_specify_tags);
    }

    private int mTagId = 0;
    private String mTagName;
    private String mQuchuName;
    public static String BUNDLE_KEY_TAG_ID = "BUNDLE_KEY_TAG_ID";
    public static String BUNDLE_KEY_TAG_NAME = "BUNDLE_KEY_TAG_NAME";
    public static String BUNDLE_KEY_TAG_QUCHU_NAME = "BUNDLE_KEY_TAG_QUCHU_NAME";
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
        mQuchuName = getIntent().getStringExtra(BUNDLE_KEY_TAG_QUCHU_NAME);
        ArrayMap<String,Object> params = new ArrayMap<>();
        params.put("趣处名称",mQuchuName);
        params.put("标签名称",mTagName);
        ZGEvent(params,"点击标签查询");
        getEnhancedToolbar().getTitleTv().setText(mTagName);

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


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    @Subscribe
    public void onMessageEvent(QuchuEventModel event) {
        if (event.getFlag()== EventFlags.EVENT_GOTO_HOME_PAGE) {
            finish();
        }
    }
}
