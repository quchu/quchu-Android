package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.FootprintModel;
import co.quchu.quchu.model.PostCardItemModel;
import co.quchu.quchu.presenter.FootPrintPresenter;
import co.quchu.quchu.view.adapter.FootPrintAdapter;
import co.quchu.quchu.widget.SpacesItemDecoration;

/**
 * Created by Nico on 16/4/7.
 */
public class FootPrintActivity extends BaseActivity {

    @Bind(R.id.rvFootPrint)
    RecyclerView rvFootPrint;
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
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foot_print);
        ButterKnife.bind(this);
        mQuchuId = getIntent().getIntExtra(BUNDLE_KEY_QUCHU_ID, -1);
        mQuchuName = getIntent().getStringExtra(BUNDLE_KEY_QUCHU_NAME);
        getEnhancedToolbar().getRightIv().setImageResource(R.mipmap.ic_dismiss_dialog);
        getEnhancedToolbar().getRightIv().setRotation(45);
        getEnhancedToolbar().getRightIv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FootPrintActivity.this, AddFootprintActivity.class);
                intent.putExtra(AddFootprintActivity.REQUEST_KEY_ID, mQuchuId);
                intent.putExtra(AddFootprintActivity.REQUEST_KEY_NAME, mQuchuName);
                startActivity(intent);
            }
        });


        mAdapter = new FootPrintAdapter(mData, new FootPrintAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(FootPrintActivity.this, MyFootprintDetailActivity.class);
                ArrayList<PostCardItemModel> mBundleData = new ArrayList<>();
                for (int i = 0; i < mData.size(); i++) {
                    mBundleData.add(mData.get(i).convertToCompatModel());
                }
                intent.putExtra(MyFootprintDetailActivity.REQUEST_KEY_POSITION, position);
                intent.putExtra(MyFootprintDetailActivity.REQUEST_KEY_MODEL, mBundleData);
                startActivity(intent);
            }
        });
        rvFootPrint.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.half_margin)));
        rvFootPrint.setAdapter(mAdapter);
        rvFootPrint.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));


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
                mData.addAll(model);
                mAdapter.notifyDataSetChanged();
                mIsLoading = false;
                if (DialogUtil.isDialogShowing()) {
                    DialogUtil.dismissProgess();
                }

            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart("pic");
        super.onResume();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd("pic");
        super.onPause();
    }
}
