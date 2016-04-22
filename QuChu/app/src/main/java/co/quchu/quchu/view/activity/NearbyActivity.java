package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.dialog.TagsFilterDialog;
import co.quchu.quchu.model.NearbyItemModel;
import co.quchu.quchu.model.TagsModel;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.NearbyPresenter;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.adapter.NearbyAdapter;
import co.quchu.quchu.widget.EndlessRecyclerOnScrollListener;
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
    private List<NearbyItemModel> mData = new ArrayList<>();
    private boolean mIsLoading = false;
    public static final String BUNDLE_KEY_PID = "BUNDLE_KEY_PID";
    public static final String BUNDLE_KEY_RECOMMEND_PIDS = "BUNDLE_KEY_RECOMMEND_PIDS";
    public static final String BUNDLE_KEY_DATA = "BUNDLE_KEY_DATA";
    private int mPlaceId;
    private String mRecommendPlaceIds;
    private String mStrFilterPattern;
    private ArrayList<TagsModel> mFilterTags = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        mStrFilterPattern = "";
        getEnhancedToolbar().getRightIv().setImageResource(R.mipmap.ic_tags_filter);
        getEnhancedToolbar().getRightIv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFilterTags.size() > 0) {
                    TagsFilterDialog tagsFilterDialog = TagsFilterDialog.newInstance(mFilterTags);
                    tagsFilterDialog.show(getFragmentManager(), "");
                    tagsFilterDialog.setPickingListener(new TagsFilterDialog.OnFinishPickingListener() {
                        @Override
                        public void onFinishPicking(List<TagsModel> selection,boolean selectAll) {

                            mStrFilterPattern = "";

                            if (selection.size() == 1) {
                                mStrFilterPattern += selection.get(0).getTagId();
                            } else {
                                for (int i = 0; i < selection.size(); i++) {
                                    if (selection.get(i).isPraise()){
                                        mStrFilterPattern += selection.get(i).getTagId();
                                        mStrFilterPattern += "|";
                                    }
                                }
                                mStrFilterPattern = mStrFilterPattern.substring(0, mStrFilterPattern.length() - 1);
                            }

                            if (selectAll){
                                mStrFilterPattern = "";
                            }

                            DialogUtil.showProgess(NearbyActivity.this, R.string.loading_dialog_text);
                            NearbyPresenter.getNearbyData(getApplicationContext(), mRecommendPlaceIds, mStrFilterPattern, 0, mPlaceId, SPUtils.getCityId(), SPUtils.getLatitude(), SPUtils.getLongitude(), mCurrentPageNo, new NearbyPresenter.getNearbyDataListener() {
                                @Override
                                public void getNearbyData(List<NearbyItemModel> model, int pMaxPageNo) {
                                    mData.clear();
                                    if (mMaxPageNo == -1) {
                                        mMaxPageNo = pMaxPageNo;
                                    }
                                    mData.addAll(model);
                                    mAdapter.notifyDataSetChanged();
                                    DialogUtil.dismissProgess();
                                }
                            });
                        }
                    });
                }

            }
        });
        mPlaceId = getIntent().getIntExtra(BUNDLE_KEY_PID, -1);
        mRecommendPlaceIds = getIntent().getStringExtra(BUNDLE_KEY_RECOMMEND_PIDS);
        mData.addAll((List<NearbyItemModel>) getIntent().getSerializableExtra(BUNDLE_KEY_DATA));

        ButterKnife.bind(this);
        mAdapter = new NearbyAdapter(mData);
        detailRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        detailRecyclerview.setAdapter(mAdapter);
        detailRecyclerview.setOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager) detailRecyclerview.getLayoutManager()) {
            @Override
            public void onLoadMore(int current_page) {
                loadData(true);
            }
        });

        loadFilterData();
        //loadData(false);
    }


    private void loadFilterData() {
        NearbyPresenter.getFilterData(getApplicationContext(), new CommonListener<List<TagsModel>>() {
            @Override
            public void successListener(List<TagsModel> response) {
                mFilterTags.clear();
                mFilterTags.addAll(response);

            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {

            }
        });
    }


    private void loadData(final boolean loadMore) {
        if (mIsLoading) return;
        if (mCurrentPageNo >= mMaxPageNo && mMaxPageNo != -1) return;
        if (!loadMore) {
            mData.clear();
            mCurrentPageNo = 1;
        } else if (mCurrentPageNo < mMaxPageNo) {

            mCurrentPageNo += 1;
        }
        DialogUtil.showProgess(this, R.string.loading_dialog_text);

        mIsLoading = true;

        NearbyPresenter.getNearbyData(getApplicationContext(), mRecommendPlaceIds, mStrFilterPattern, 0, mPlaceId, SPUtils.getCityId(), SPUtils.getLatitude(), SPUtils.getLongitude(), mCurrentPageNo, new NearbyPresenter.getNearbyDataListener() {
            @Override
            public void getNearbyData(List<NearbyItemModel> model, int pMaxPageNo) {
                System.out.println("7");
                if (mMaxPageNo == -1) {
                    mMaxPageNo = pMaxPageNo;
                }
                mData.addAll(model);
                mAdapter.notifyDataSetChanged();
                mIsLoading = false;
                DialogUtil.dismissProgess();
            }
        });
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart("all_recommendtion");
        super.onResume();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd("all_recommendtion");
        super.onPause();
    }
}
