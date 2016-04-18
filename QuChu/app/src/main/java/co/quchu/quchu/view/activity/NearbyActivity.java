package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.TagsFilterDialog;
import co.quchu.quchu.model.NearbyItemModel;
import co.quchu.quchu.model.TagsModel;
import co.quchu.quchu.presenter.NearbyPresenter;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.adapter.NearbyAdapter;
import co.quchu.quchu.widget.EndlessRecyclerOnScrollListener;
import co.quchu.quchu.widget.MoreButtonView;
import co.quchu.quchu.widget.TagCloudView;

/**
 * Created by Nico on 16/4/11.
 */
public class NearbyActivity extends BaseActivity {


    @Bind(R.id.detail_recyclerview)
    RecyclerView detailRecyclerview;
    @Bind(R.id.detail_store_tagclound_tcv)
    TagCloudView tagCloudView;

    NearbyAdapter mAdapter;
    private int mMaxPageNo = -1;
    private int mCurrentPageNo = 1;
    private String mTagsInfo;
    private List<NearbyItemModel> mData = new ArrayList<>();
    private boolean mIsLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        ButterKnife.bind(this);
        getEnhancedToolbar().getRightIv().setImageResource(R.mipmap.ic_tags_filter);
        getEnhancedToolbar().getRightIv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<TagsModel> data = new ArrayList<>();
                            for (int i = 0; i < 19; i++) {
                                TagsModel tag = new TagsModel();
                                tag.setCode(String.valueOf(i));
                                tag.setEn("EN"+i);
                                tag.setZh("标签"+ (i%7==0?"凑数":""));
                                tag.setTagId(i*1000);
                                data.add(tag);
                            }
                            ArrayList<Boolean> selection = new ArrayList<>();
                            selection.add(false);
                            selection.add(true);
                            selection.add(false);
                            selection.add(true);
                            selection.add(false);
                            TagsFilterDialog tagsFilterDialog = TagsFilterDialog.newInstance(data,selection);
                            tagsFilterDialog.show(getFragmentManager(),"");

            }
        });
        mAdapter = new NearbyAdapter(mData);
        detailRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        detailRecyclerview.setAdapter(mAdapter);
        detailRecyclerview.setOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager)detailRecyclerview.getLayoutManager()) {
            @Override
            public void onLoadMore(int current_page) {
                loadData(true);
            }
        });
        loadData(false);
    }

    private void loadData(final boolean loadMore) {
        if (mIsLoading) return;
        if (mCurrentPageNo >= mMaxPageNo &&mMaxPageNo !=-1) return;
        if (!loadMore){
            mData.clear();
            mCurrentPageNo = 1;
        }else if (mCurrentPageNo<mMaxPageNo){
            mCurrentPageNo +=1;
        }
        mIsLoading = true;
        NearbyPresenter.getNearbyData(getApplicationContext(), SPUtils.getCityId(), mTagsInfo, SPUtils.getLatitude(), SPUtils.getLongitude(), mCurrentPageNo, new NearbyPresenter.getNearbyDataListener() {
            @Override
            public void getNearbyData(List<NearbyItemModel> model, int pMaxPageNo) {
                if (mMaxPageNo==-1){
                    mMaxPageNo = pMaxPageNo;
                }
                mData.addAll(model);
                mAdapter.notifyDataSetChanged();
                mIsLoading = false;
            }
        });
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }
}
