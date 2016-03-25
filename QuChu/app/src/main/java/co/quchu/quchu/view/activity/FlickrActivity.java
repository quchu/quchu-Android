package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.FlickrModel;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.presenter.FlickrPresenter;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
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

    public FlickrModel flickrImagesHot = new FlickrModel();//我的照片 hot
    public FlickrModel flickrImagesNew = new FlickrModel();//我的照片 new
    public FlickrModel flickrFavoriteHot = new FlickrModel();//我的收藏 hot
    public FlickrModel flickrFavoriteNew = new FlickrModel();//我的收藏 new
    @Bind(R.id.flickr_act_swipe_refresh_layout)
    SwipeRefreshLayout flickrActSwipeRefreshLayout;
    @Bind(R.id.action_buttton)
    TextView emptyViewOtherTv;
    @Bind(R.id.empty_view_fl)
    FrameLayout emptyViewFl;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flickr);
        ButterKnife.bind(this);
        initTitleBar();
        title_content_tv.setText(getTitle());
        flickrFbg.setSelectedListener(this);
        flickrActSwipeRefreshLayout.setOnLoadListener(this);
        flickrActSwipeRefreshLayout.setColor(R.color.load_progress_black,
                R.color.load_progress_yellow,
                R.color.load_progress_gray,
                R.color.load_progress_yellow);
        flickrActSwipeRefreshLayout.setMode(SwipeRefreshLayout.Mode.PULL_FROM_END);
        flickrActSwipeRefreshLayout.setLoadNoFull(false);
        tabBar.setSelectedListener(new ImageSubtabLayout.ImageSubtabSelectedListener() {
            @Override
            public void onSelected(int selectedNum) {
                flickrTsl.initIndexView(selectedNum);
                if (selectedNum == 0) {
                    isSelectedImages = true;
                } else {
                    isSelectedImages = false;
                }
                changeDataSet();
                /**
                 * 顶部选中回调
                 */
            }
        });
        flickrTsl.setSelectedListener(new TextSubtabLayout.TextSubtabSelectedListener() {
            @Override
            public void onSelected(int selectedNum) {
                tabBar.initIndexView(selectedNum);
                if (selectedNum == 0) {
                    isSelectedImages = true;
                } else {
                    isSelectedImages = false;
                }
                changeDataSet();
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
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }


    @Override
    public void onViewsClick(int flag) {
        switch (flag) {
            case FlickrButtonGroup.SelectedR: //选中new
                isSelectedHot = false;
                changeDataSet();
                break;
            case FlickrButtonGroup.SelectedL://选中hot
                isSelectedHot = true;
                changeDataSet();
                break;
            case FlickrButtonGroup.SelectedCT: //选中大图
                isSelectedLarge = true;
                changeDataSet();
                transaction = getSupportFragmentManager().beginTransaction();
         /*       transaction.setCustomAnimations(R.anim.in_push_right_to_left,R.anim.out_push_left_to_right);*/
                transaction.replace(R.id.flickr_fl, flickrListFragment);
             /*   transaction.addToBackStack(null);*/
                transaction.commit();

                break;
            case FlickrButtonGroup.SelectedCF://选中小图
                if (flickrGridFragment != null) {
                    isSelectedLarge = false;
                    changeDataSet();
                    transaction = getSupportFragmentManager().beginTransaction();
          /*      transaction.setCustomAnimations(R.anim.in_push_right_to_left,R.anim.out_push_left_to_right);*/
                    transaction.replace(R.id.flickr_fl, flickrGridFragment);
               /* transaction.addToBackStack(null);*/
                    transaction.commit();
                }
                break;
        }
        scrollViewFlickr.smoothScrollTo(0, 0);
    }


    private boolean isSelectedLarge = true; //true=选中大图布局  false=选中小图9宫格布局
    private boolean isSelectedHot = true; //true =选中 hot  false= 选中new
    private boolean isSelectedImages = true;//true=现在我的照片 false=选中我的收藏


    @Override
    public void onLoad() {
        loadMore();
    }


    /**
     *
     */
    private void changeDataSet() {
        if (isSelectedLarge) {      //大图 listFragment
            if (isSelectedImages) {      //大图 listFragment  我的照片
                if (isSelectedHot) {//大图 listFragment  我的照片  hot
                    if (flickrImagesHot != null && flickrImagesHot.getImgs() != null && flickrImagesHot.getImgs().getResult() != null && flickrImagesHot.getImgs().getResult().size() > 0) {
                        LogUtils.json("大图 listFragment  我的照片  hot" + flickrImagesHot.getImgs().getResult().size());
                        flickrListFragment.changeDataSet(flickrImagesHot.getImgs());
                        showEmptyView(flickrImagesHot.getImgs());
                    }
                } else {//大图 listFragment  我的照片  new
                    if (flickrImagesNew != null && flickrImagesNew.getImgs() != null && flickrImagesNew.getImgs().getResult() != null && flickrImagesNew.getImgs().getResult().size() > 0) {
                        LogUtils.json("大图 listFragment  我的照片  new " + flickrImagesNew.getImgs().getResult().size());
                        flickrListFragment.changeDataSet(flickrImagesNew.getImgs());
                        showEmptyView(flickrImagesNew.getImgs());
                    }
                }
            } else {  //大图 listFragment  我的收藏

                if (isSelectedHot) {//大图 listFragment  我的收藏  hot
                    if (flickrFavoriteHot != null && flickrFavoriteHot.getImgs() != null && flickrFavoriteHot.getImgs().getResult() != null && flickrFavoriteHot.getImgs().getResult().size() > 0) {
                        LogUtils.json("大图 listFragment   我的收藏  hot" + flickrFavoriteHot.getImgs().getResult().size());
                        flickrListFragment.changeDataSet(flickrFavoriteHot.getImgs());
                        showEmptyView(flickrFavoriteHot.getImgs());
                        if (flickrFavoriteHot.getImgs().getResult().size() < 2) {
                            flickrTsl.setVisibility(View.INVISIBLE);
                        }
                    }
                } else {//大图 listFragment  我的收藏  new
                    if (flickrFavoriteNew != null && flickrFavoriteNew.getImgs() != null && flickrFavoriteNew.getImgs().getResult() != null && flickrFavoriteNew.getImgs().getResult().size() > 0) {
                        LogUtils.json("大图 listFragment   我的收藏  new" + flickrFavoriteNew.getImgs().getResult().size());
                        flickrListFragment.changeDataSet(flickrFavoriteNew.getImgs());
                        showEmptyView(flickrFavoriteNew.getImgs());
                        if (flickrFavoriteNew.getImgs().getResult().size() < 2) {
                            flickrTsl.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        } else {  //小图 gridFragment

            if (isSelectedImages) {      //小图 gridFragment 我的照片
                if (isSelectedHot) {//小图 gridFragment  我的照片  hot
                    if (flickrImagesHot != null && flickrImagesHot.getImgs() != null && flickrImagesHot.getImgs().getResult() != null && flickrImagesHot.getImgs().getResult().size() > 0) {

                        LogUtils.json("小图 gridFragment  我的照片  hot" + flickrImagesHot.getImgs().getResult().size());
                        flickrGridFragment.changeDataSet(flickrImagesHot.getImgs());
                        showEmptyView(flickrImagesHot.getImgs());
                    }
                } else {//小图 gridFragment  我的照片  new
                    if (flickrImagesNew != null && flickrImagesNew.getImgs() != null && flickrImagesNew.getImgs().getResult() != null && flickrImagesNew.getImgs().getResult().size() > 0) {

                        LogUtils.json("小图 gridFragment  我的照片  new" + flickrImagesNew.getImgs().getResult().size());
                        flickrGridFragment.changeDataSet(flickrImagesNew.getImgs());
                        showEmptyView(flickrImagesNew.getImgs());
                    }
                }
            } else {  //小图 gridFragment  我的收藏

                if (isSelectedHot) {//小图 gridFragment  我的收藏  hot
                    if (flickrFavoriteHot != null && flickrFavoriteHot.getImgs() != null && flickrFavoriteHot.getImgs().getResult() != null && flickrFavoriteHot.getImgs().getResult().size() > 0) {

                        LogUtils.json("小图 gridFragment  我的收藏  hot" + flickrFavoriteHot.getImgs().getResult().size());
                        flickrGridFragment.changeDataSet(flickrFavoriteHot.getImgs());
                        showEmptyView(flickrFavoriteHot.getImgs());
                        if (flickrFavoriteHot.getImgs().getResult().size() < 2) {
                            flickrTsl.setVisibility(View.INVISIBLE);
                        }
                    }
                } else {//小图 gridFragment  我的收藏  new
                    if (flickrFavoriteNew != null && flickrFavoriteNew.getImgs() != null && flickrFavoriteNew.getImgs().getResult() != null && flickrFavoriteNew.getImgs().getResult().size() > 0) {

                        LogUtils.json("小图 gridFragment  我的收藏  new" + flickrFavoriteNew.getImgs().getResult().size());
                        flickrGridFragment.changeDataSet(flickrFavoriteNew.getImgs());
                        showEmptyView(flickrFavoriteNew.getImgs());
                        if (flickrFavoriteNew.getImgs().getResult().size() < 2) {
                            flickrTsl.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        }
    }

    /**
     * 获得初始数据， 并保存
     */
    private void initData() {
        FlickrPresenter.getFlickrAlbum(this, NetApi.GetImageAlbum, NetApi.AlbumTypeHot, 0, new FlickrPresenter.FlickrListener() {
            @Override
            public void onSuccess(FlickrModel flickrModel) {
                flickrImagesHot = flickrModel;
                flickrListFragment = FlickrListFragment.newInstance(flickrImagesHot.getImgs());
                flickrGridFragment = FlickrGridFragment.newInstance(flickrImagesHot.getImgs());
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.flickr_fl, flickrListFragment);
                transaction.commitAllowingStateLoss();
                scrollViewFlickr.smoothScrollTo(0, 0);
                showEmptyView(flickrImagesHot.getImgs());
                if (!StringUtils.isEmpty(flickrImagesHot.getPhoto().getCover()))
                    tabBar.setWidgetLeftImage(flickrImagesHot.getPhoto().getCover());
                if (!StringUtils.isEmpty(flickrImagesHot.getFavorite().getCover()))
                    tabBar.setWidgetRightImage(flickrImagesHot.getFavorite().getCover());
                tabBar.setWidgetLeftNum(flickrImagesHot.getPhoto().getNum());
                tabBar.setWidgetRightNum(flickrImagesHot.getFavorite().getNum());
                flickrTsl.setLeftNum(flickrImagesHot.getPhoto().getNum());
                flickrTsl.setRightNum(flickrImagesHot.getFavorite().getNum());
                getOtherData();
            }

            @Override
            public void onError(String error) {
            }
        });
    }

    private void showEmptyView(FlickrModel.ImgsEntity entity) {
        if (entity.getResult().size() == 0) {
            flickrFl.setVisibility(View.GONE);
            flickrFbg.setVisibility(View.GONE);
            emptyViewFl.setVisibility(View.VISIBLE);
            flickrTsl.setVisibility(View.INVISIBLE);
        } else {
            emptyViewFl.setVisibility(View.GONE);
            flickrFl.setVisibility(View.VISIBLE);
            flickrFbg.setVisibility(View.VISIBLE);
        }
    }

    private void getOtherData() {
        FlickrPresenter.getFlickrAlbum(this, NetApi.GetFavoriteAlbum, NetApi.AlbumTypeHot, 0, new FlickrPresenter.FlickrListener() {
            @Override
            public void onSuccess(FlickrModel flickrModel) {
                flickrFavoriteHot = flickrModel;
            }

            @Override
            public void onError(String error) {
            }
        });
        FlickrPresenter.getFlickrAlbum(this, NetApi.GetImageAlbum, NetApi.AlbumTypeNew, 0, new FlickrPresenter.FlickrListener() {
            @Override
            public void onSuccess(FlickrModel flickrModel) {
                flickrImagesNew = flickrModel;
            }

            @Override
            public void onError(String error) {
            }
        });
        FlickrPresenter.getFlickrAlbum(this, NetApi.GetFavoriteAlbum, NetApi.AlbumTypeNew, 0, new FlickrPresenter.FlickrListener() {
            @Override
            public void onSuccess(FlickrModel flickrModel) {
                flickrFavoriteNew = flickrModel;
            }

            @Override
            public void onError(String error) {
            }
        });
    }

    private String loadMoreUrl = "";
    private boolean isNeedLoadMore = false;//是否需要加载 根据

    private void loadMore() {
        if (isSelectedImages) {      //大图 listFragment  我的照片
            if (isSelectedHot) {//大图 listFragment  我的照片  hot
                loadMoreUrl = String.format(NetApi.GetImageAlbum, SPUtils.getUserToken(this), NetApi.AlbumTypeHot, flickrImagesHot.getImgs().getPagesNo() + 1);
                isNeedLoadMore = !(flickrImagesHot.getImgs().getPagesNo() + 1 >= flickrImagesHot.getImgs().getPageCount());
            } else {//大图 listFragment  我的照片  new
                loadMoreUrl = String.format(NetApi.GetImageAlbum, SPUtils.getUserToken(this), NetApi.AlbumTypeNew, flickrImagesNew.getImgs().getPagesNo() + 1);
                isNeedLoadMore = !(flickrImagesNew.getImgs().getPagesNo() + 1 >= flickrImagesNew.getImgs().getPageCount());
            }
        } else {  //大图 listFragment  我的收藏
            if (isSelectedHot) {//大图 listFragment  我的收藏  hot
                loadMoreUrl = String.format(NetApi.GetFavoriteAlbum, SPUtils.getUserToken(this), NetApi.AlbumTypeHot, flickrFavoriteHot.getImgs().getPagesNo() + 1);
                isNeedLoadMore = !(flickrFavoriteHot.getImgs().getPagesNo() + 1 >= flickrFavoriteHot.getImgs().getPageCount());
            } else {//大图 listFragment  我的收藏  new
                loadMoreUrl = String.format(NetApi.GetFavoriteAlbum, SPUtils.getUserToken(this), NetApi.AlbumTypeNew, flickrFavoriteNew.getImgs().getPagesNo() + 1);
                isNeedLoadMore = !(flickrFavoriteNew.getImgs().getPagesNo() + 1 >= flickrFavoriteNew.getImgs().getPageCount());
            }
        }
        if (isNeedLoadMore && !StringUtils.isEmpty(loadMoreUrl)) {
            FlickrPresenter.loadMoreAlbum(this, loadMoreUrl, new FlickrPresenter.FlickrListener() {
                @Override
                public void onSuccess(FlickrModel flickrModel) {
                    if (isSelectedImages) {      //大图 listFragment  我的照片
                        if (isSelectedHot) {//大图 listFragment  我的照片  hot
                            flickrImagesHot.getImgs().addResult(flickrModel.getImgs().getResult());
                            flickrImagesHot.getImgs().setPagesNo(flickrImagesHot.getImgs().getPagesNo() + 1);
                        } else {//大图 listFragment  我的照片  new
                            flickrImagesNew.getImgs().addResult(flickrModel.getImgs().getResult());
                            flickrImagesNew.getImgs().setPagesNo(flickrImagesNew.getImgs().getPagesNo() + 1);
                        }
                    } else {  //大图 listFragment  我的收藏
                        if (isSelectedHot) {//大图 listFragment  我的收藏  hot
                            flickrFavoriteHot.getImgs().addResult(flickrModel.getImgs().getResult());
                            flickrFavoriteHot.getImgs().setPagesNo(flickrFavoriteHot.getImgs().getPagesNo() + 1);
                        } else {//大图 listFragment  我的收藏  new
                            flickrFavoriteNew.getImgs().addResult(flickrModel.getImgs().getResult());
                            flickrFavoriteNew.getImgs().setPagesNo(flickrFavoriteNew.getImgs().getPagesNo() + 1);
                        }
                    }
                    changeDataSet();
                    flickrActSwipeRefreshLayout.setLoading(false);
                }

                @Override
                public void onError(String error) {
                    flickrActSwipeRefreshLayout.setLoading(false);
                }
            });
        } else {
            flickrActSwipeRefreshLayout.setLoading(false);
        }
    }

    @OnClick(R.id.action_buttton)
    public void flickrClick(View view) {
        this.finish();
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("FlickrActivity");

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("FlickrActivity");
        initData();
    }
}
