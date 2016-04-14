package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.FootprintModel;
import co.quchu.quchu.presenter.FootPrintPresenter;
import co.quchu.quchu.view.adapter.FootPrintAdapter;

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
    public static final String BUNDLE_KEY_QUCHU_ID = "BUNDLE_KEY_QUCHU_ID";

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foot_print);
        ButterKnife.bind(this);
        mQuchuId = getIntent().getIntExtra(BUNDLE_KEY_QUCHU_ID,-1);
        getEnhancedToolbar().getRightIv().setImageResource(R.drawable.gf_ic_preview);
        getEnhancedToolbar().getRightIv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FootPrintActivity.this,AddFootprintActivity.class));
            }
        });




        mAdapter = new FootPrintAdapter(mData, new FootPrintAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(FootPrintActivity.this,MyFootprintDetailActivity.class));
            }
        });
        rvFootPrint.setAdapter(mAdapter);
        rvFootPrint.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));


        loadData(false);
    }

    private void loadData(final boolean loadMore) {
        if (mIsLoading) return;
        if (mCurrentPageNo == mMaxPageNo) return;
        if (!loadMore){
            mData.clear();
            mCurrentPageNo = 1;
            DialogUtil.showProgess(this,R.string.loading_dialog_text);
        }else if (mCurrentPageNo<mMaxPageNo){
            mCurrentPageNo +=1;

        }
        mIsLoading = true;
        FootPrintPresenter.getFootprint(getApplicationContext(), mQuchuId, mCurrentPageNo, new FootPrintPresenter.GetFootprintDataListener() {
            @Override
            public void getFootprint(List<FootprintModel> model, int pMaxPageNo) {
                System.out.println("getFootPrint");
                mMaxPageNo = pMaxPageNo;
                mData.addAll(model);
                mAdapter.notifyDataSetChanged();
                mIsLoading = false;
                if (DialogUtil.isDialogShowing()){
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
}
