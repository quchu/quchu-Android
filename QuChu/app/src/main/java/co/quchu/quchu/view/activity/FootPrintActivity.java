package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.dialog.CommonDialog;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.FootprintModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.ResponseListener;
import co.quchu.quchu.presenter.FootPrintPresenter;
import co.quchu.quchu.thirdhelp.UserInfoHelper;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.adapter.FootPrintAdapter;
import co.quchu.quchu.widget.EndlessRecyclerOnScrollListener;
import co.quchu.quchu.widget.SpacesItemDecoration;

/**
 * Created by Nico on 16/4/7.
 */
public class FootPrintActivity extends BaseBehaviorActivity {




    @Override
    protected String getPageNameCN() {
        return getString(R.string.pname_footprint);
    }

    @Bind(R.id.rvFootPrint)
    RecyclerView rvFootPrint;
    @Bind(R.id.tvAddFootprint)
    TextView tvAddFootPrint;
    @Bind(R.id.tvEmpty)
    TextView tvEmpty;

    FootPrintAdapter mAdapter;
    private int mMaxPageNo = -1;
    private int mCurrentPageNo = 1;
    private List<FootprintModel> mData = new ArrayList<>();
    private boolean mIsLoading = false;
    private int mQuchuId;
    private String mQuchuName;
    public static final String BUNDLE_KEY_QUCHU_ID = "BUNDLE_KEY_QUCHU_ID";
    public static final String BUNDLE_KEY_QUCHU_NAME = "BUNDLE_KEY_QUCHU_NAME";


    @Override
    public ArrayMap<String, Object> getUserBehaviorArguments() {

        ArrayMap<String,Object> data = new ArrayMap<>();
        data.put("pid",getIntent().getIntExtra(BUNDLE_KEY_QUCHU_ID,-1));
        return data;
    }

    @Override
    public int getUserBehaviorPageId() {
        return 105;
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foot_print);
        ButterKnife.bind(this);
        getEnhancedToolbar();
        mQuchuId = getIntent().getIntExtra(BUNDLE_KEY_QUCHU_ID, -1);
        mQuchuName = getIntent().getStringExtra(BUNDLE_KEY_QUCHU_NAME);

        mAdapter = new FootPrintAdapter(mData, new FootPrintAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(FootPrintActivity.this, MyFootprintDetailActivity.class);
                ArrayList<FootprintModel.Entity> entitys = new ArrayList<>();
                int seletedPosition = 0;
                for (int i = 0, s = mData.size(); i < s; i++) {
                    if (i == position) {
                        seletedPosition = entitys.size() ;
                    }
                    FootprintModel model = mData.get(i);
                    List<FootprintModel.Entity> entity = model.convertToList();
                    entitys.addAll(entity);
                }
                intent.putParcelableArrayListExtra(MyFootprintDetailActivity.REQUEST_KEY_ENTITY_LIST, entitys);
                intent.putExtra(MyFootprintDetailActivity.REQUEST_KEY_SELECTED_POSITION, seletedPosition);
                startActivity(intent);
            }
        });
        rvFootPrint.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.quarter_margin), 2));
        rvFootPrint.setAdapter(mAdapter);
        rvFootPrint.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        tvAddFootPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppContext.user.isIsVisitors()) {

                    showLoginDialog();
                } else {
                    Intent intent = new Intent(FootPrintActivity.this, AddFootprintActivity.class);
                    intent.putExtra(AddFootprintActivity.REQUEST_KEY_ID, mQuchuId);
                    intent.putExtra(AddFootprintActivity.REQUEST_KEY_NAME, mQuchuName);
                    startActivity(intent);
                }
            }
        });
        rvFootPrint.addOnScrollListener(new EndlessRecyclerOnScrollListener(rvFootPrint.getLayoutManager()) {
            @Override
            public void onLoadMore(int current_page) {
                loadData(true);
            }
        });

        loadData(false);
    }

    private void loadData(final boolean loadMore) {
        if (mIsLoading) return;
        if (mCurrentPageNo == mMaxPageNo) return;
        if (!loadMore) {
            mData.clear();
            mCurrentPageNo = 1;
            DialogUtil.showProgess(this, R.string.loading_dialog_text);
        } else if (mCurrentPageNo < mMaxPageNo) {
            mCurrentPageNo += 1;

        }
        mIsLoading = true;
        FootPrintPresenter.getFootprint(getApplicationContext(), mQuchuId, mCurrentPageNo, new FootPrintPresenter.GetFootprintDataListener() {
            @Override
            public void getFootprint(List<FootprintModel> model, int pMaxPageNo) {
                mMaxPageNo = pMaxPageNo;
                if (null != model) {
                    mData.addAll(model);
                    mAdapter.notifyDataSetChanged();
                }
                if (null==mData||mData.size()==0){
                    tvEmpty.setVisibility(View.VISIBLE);
                }else{
                    tvEmpty.setVisibility(View.GONE);
                }
                mIsLoading = false;
                if (DialogUtil.isDialogShowing()) {
                    DialogUtil.dismissProgess();
                }
            }

        });
    }


    @Subscribe
    public void onMessageEvent(QuchuEventModel event) {
        if (event.getFlag() == EventFlags.EVENT_POST_CARD_ADDED ||
                event.getFlag() == EventFlags.EVENT_POST_CARD_DELETED||event.getFlag()==EventFlags.EVENT_FOOTPRINT_UPDATED) {
            mMaxPageNo = -1;
            loadData(false);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        super.onPause();
    }
}
