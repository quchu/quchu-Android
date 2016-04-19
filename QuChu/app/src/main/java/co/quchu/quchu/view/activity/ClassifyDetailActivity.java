package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.ConfirmDialogFg;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.presenter.RecommendPresenter;
import co.quchu.quchu.presenter.VersionInfoPresenter;
import co.quchu.quchu.utils.DeviceUtils;
import co.quchu.quchu.view.adapter.DiscoverDetailPagerAdapter;
import co.quchu.quchu.view.adapter.RecommendAdapterLite;



public class ClassifyDetailActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    public static final String PARAMETER_TITLE = "title";
    @Bind(R.id.vpContent)
    ViewPager vpContent;
    ArrayList<RecommendModel> mData = new ArrayList<>();
    DiscoverDetailPagerAdapter mAdapter;
    boolean mNetworkBusy = false;
    int mClickTimes = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify_detail);
        ButterKnife.bind(this);
        getEnhancedToolbar().getRightTv().setText(" ");
        getEnhancedToolbar().getRightIv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickTimes += 1;
                if (mClickTimes>=5){
                    getVersionInfo();
                }

                vpContent.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mClickTimes = 0;
                    }
                },1500);
            }
        });

        mAdapter = new DiscoverDetailPagerAdapter(mData, this, new DiscoverDetailPagerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(ClassifyDetailActivity.this, QuchuDetailsActivity.class);
                intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, mData.get(position).getPid());
                startActivity(intent);
            }
        });
        vpContent.setOnPageChangeListener(this);
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


    public void getVersionInfo(){
        if (mNetworkBusy) return;
        mNetworkBusy = true;
        VersionInfoPresenter.getVersionInfo(getApplicationContext(), new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                mNetworkBusy = false;
                ConfirmDialogFg.newInstance("版本信息",response.toString()).show(getFragmentManager(),"");
            }

            @Override
            public boolean onError(String error) {
                mNetworkBusy = false;
                return false;
            }
        });
    }


    public void getDataSetFromServer() {
        RecommendPresenter.getRecommendList(this, false, new RecommendPresenter.GetRecommendListener() {
            @Override
            public void onSuccess(ArrayList<RecommendModel> arrayList, int pageCount, int pageNum) {
                mData.clear();
                mData.addAll(arrayList);
                getSwipeBackLayout().setEnableGesture(false);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {

            }
        });


    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position==0){
            getSwipeBackLayout().setEnableGesture(true);
        }else{
            getSwipeBackLayout().setEnableGesture(false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        if (null!=vpContent){
            vpContent.removeOnPageChangeListener(this);
        }
        super.onDestroy();
    }
}