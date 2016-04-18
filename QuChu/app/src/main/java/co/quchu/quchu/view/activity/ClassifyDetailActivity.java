package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.presenter.RecommendPresenter;
import co.quchu.quchu.view.adapter.DiscoverDetailPagerAdapter;
import co.quchu.quchu.view.adapter.RecommendAdapterLite;

public class ClassifyDetailActivity extends BaseActivity {

    @Bind(R.id.vpContent)
    ViewPager vpContent;
    public ArrayList<RecommendModel> mData = new ArrayList<>();
    public DiscoverDetailPagerAdapter mAdapter;

    public static final String PARAMETER_TITLE = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify_detail);
        ButterKnife.bind(this);
        getEnhancedToolbar();

        mAdapter = new DiscoverDetailPagerAdapter(mData, this, new DiscoverDetailPagerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(ClassifyDetailActivity.this, QuchuDetailsActivity.class);
                intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, mData.get(position).getPid());
                startActivity(intent);
            }
        });
        vpContent.setAdapter(mAdapter);
        vpContent.setClipToPadding(false);
        vpContent.setPadding(80,40,80,40);
        vpContent.setPageMargin(40);
        vpContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        getDataSetFromServer();
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }



    public void getDataSetFromServer() {
        RecommendPresenter.getRecommendList(this, false, new RecommendPresenter.GetRecommendListener() {
            @Override
            public void onSuccess(ArrayList<RecommendModel> arrayList, int pageCount, int pageNum) {
                mData.clear();
                mData.addAll(arrayList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {

            }
        });


    }



}