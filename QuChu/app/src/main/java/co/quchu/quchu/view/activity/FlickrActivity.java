package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.FlickrModel;
import co.quchu.quchu.presenter.FlickrPresenter;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.view.fragment.FlickrGridFragment;
import co.quchu.quchu.view.fragment.FlickrListFragment;
import co.quchu.quchu.widget.FlickrButtonGroup;
import co.quchu.quchu.widget.ImageSubtabLayout;
import co.quchu.quchu.widget.OutSideScrollView;
import co.quchu.quchu.widget.SwipeRefreshLayout.SwipeRefreshLayout;
import co.quchu.quchu.widget.TextSubtabLayout;

/**
 * FlickrActivity
 * User: Chenhs
 * Date: 2015-11-12
 * 我的网络相册
 * 我的照片/我的收藏
 */
public class FlickrActivity extends BaseActivity implements FlickrButtonGroup.FlickrOnSelectedistener, SwipeRefreshLayout.OnLoadListener {
    @Bind(R.id.tab_bar)
    ImageSubtabLayout tabBar;
    @Bind(R.id.flickr_fl)
    FrameLayout flickrFl;
    @Bind(R.id.scrollView_flickr)
    OutSideScrollView scrollViewFlickr;
    @Bind(R.id.flickr_fbg)
    FlickrButtonGroup flickrFbg;
    @Bind(R.id.flickr_tsl)
    TextSubtabLayout flickrTsl;
    @Bind(R.id.title_back_rl)
    RelativeLayout titleBackRL;
    @Bind(R.id.title_more_rl)
    RelativeLayout titleMoreRl;
    @Bind(R.id.title_content_tv)
    TextView title_content_tv;
    FlickrListFragment flickrListFragment;
    FlickrGridFragment flickrGridFragment;
    FragmentTransaction transaction;

    public FlickrModel flickrImageModel = new FlickrModel();//我的照片
    public FlickrModel flickrFavoriteModel = new FlickrModel();//我的收藏
    @Bind(R.id.flickr_act_swipe_refresh_layout)
    SwipeRefreshLayout flickrActSwipeRefreshLayout;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flickr);
        ButterKnife.bind(this);
        title_content_tv.setText(getTitle());
        flickrFbg.setSelectedListener(this);
        flickrActSwipeRefreshLayout.setOnLoadListener(this);
        flickrActSwipeRefreshLayout.setColor(R.color.planet_progress_yellow,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                R.color.appBackground);
        flickrActSwipeRefreshLayout.setMode(SwipeRefreshLayout.Mode.PULL_FROM_END);
        flickrActSwipeRefreshLayout.setLoadNoFull(false);

        FlickrPresenter.getImageAlbum(this, new FlickrPresenter.FlickrListener() {
            @Override
            public void onSuccess(FlickrModel flickrModel) {
                flickrImageModel = flickrModel;
                flickrListFragment = new FlickrListFragment(FlickrActivity.this, flickrModel.getImgs());

                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.flickr_fl, flickrListFragment);
                transaction.commit();
                scrollViewFlickr.smoothScrollTo(0, 0);
                tabBar.setSelectedListener(new ImageSubtabLayout.ImageSubtabSelectedListener() {
                    @Override
                    public void onSelected(int selectedNum) {
                        flickrTsl.initIndexView(selectedNum);
                        if (selectedNum == 0) {
                            isSelectedImages = true;
                        } else {
                            isSelectedImages = false;
                        }
                        /**
                         * 顶部选中回调
                         */
                    }
                });
                flickrTsl.setSelectedListener(new TextSubtabLayout.TextSubtabSelectedListener() {
                    @Override
                    public void onSelected(int selectedNum) {
                        tabBar.initIndexView(selectedNum);
                    }
                });
                scrollViewFlickr.setOverScrollListener(new OutSideScrollView.OverScrolledListener() {
                    @Override
                    public void onOverScrolled(int scrollX, int scrollY) {
                        if (scrollY >= (tabBar.getHeight() - flickrTsl.getHeight())) {
                            flickrTsl.setVisibility(View.VISIBLE);
                        } else {
                            flickrTsl.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }

            @Override
            public void onError(String error) {
            }
        });
        flickrGridFragment = new FlickrGridFragment();
    }

    @Override
    public void onViewsClick(int flag) {
        switch (flag) {
            case FlickrButtonGroup.SelectedR: //选中new
                isSelectedHot = true;
                break;
            case FlickrButtonGroup.SelectedL://选中hot
                isSelectedHot = false;
                break;
            case FlickrButtonGroup.SelectedCT: //选中大图
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.flickr_fl, flickrListFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                isSelectedLarge = true;
                break;
            case FlickrButtonGroup.SelectedCF://选中小图
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.flickr_fl, flickrGridFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                isSelectedLarge = false;
                break;
        }
    }

    private boolean isSelectedLarge = true; //true=选中大图布局  false=选中小图9宫格布局
    private boolean isSelectedHot = true; //true =选中 hot  false= 选中new
    private boolean isSelectedImages = true;//true=现在我的照片 false=选中我的收藏


    @OnClick({R.id.title_back_rl, R.id.title_more_rl})
    public void titleClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_rl:
                finish();
                break;
            case R.id.title_more_rl:

                break;
        }
    }

    private void selectedLargeImage() {
        if (isSelectedLarge) {
            //选中大图时更新数据
            flickrListFragment.updateDataSet(null);
        } else {
            //选中九宫格布局时更新数据
            flickrGridFragment.updateDataSet(null);
        }
    }


    private void selectedFavorite() {

    }

    @Override
    public void onLoad() {
        LogUtils.json("onLoad");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LogUtils.json("onLoad");
                flickrActSwipeRefreshLayout.setLoading(false);
            }
        }, 2000);
    }

    private void changeDataSet() {
        if (isSelectedLarge) {      //大图 listFragment
            if (isSelectedImages) {      //大图 listFragment  我的照片
                if (isSelectedHot) {//大图 listFragment  我的照片  hot

                } else {//大图 listFragment  我的照片  new

                }
            } else {  //大图 listFragment  我的收藏
                if (isSelectedHot) {//大图 listFragment  我的收藏  hot

                } else {//大图 listFragment  我的收藏  new

                }
            }
        } else {  //小图 gridFragment

            if (isSelectedImages) {      //小图 gridFragment 我的照片
                if (isSelectedHot) {//小图 gridFragment  我的照片  hot

                } else {//小图 gridFragment  我的照片  new

                }
            } else {  //小图 gridFragment  我的收藏
                if (isSelectedHot) {//小图 gridFragment  我的收藏  hot

                } else {//小图 gridFragment  我的收藏  new

                }
            }
        }
    }

}
