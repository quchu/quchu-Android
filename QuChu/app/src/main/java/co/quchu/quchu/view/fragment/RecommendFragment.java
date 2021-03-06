package co.quchu.quchu.view.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.base.GeTuiReceiver;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.PagerModel;
import co.quchu.quchu.model.PushMessageBean;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.model.SceneModel;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.ScenePresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.ScreenUtils;
import co.quchu.quchu.view.activity.QuchuDetailsActivity;
import co.quchu.quchu.view.activity.SceneDetailActivity;
import co.quchu.quchu.view.adapter.AllSceneGridAdapter;
import co.quchu.quchu.view.adapter.MySceneAdapter;
import co.quchu.quchu.widget.ErrorView;
import co.quchu.quchu.widget.SpacesItemDecoration;


/**
 * RecommendFragment
 * User: Chenhs
 * Date: 2015-12-07
 * 推荐
 */
public class RecommendFragment extends BaseFragment implements MySceneAdapter.CardClickListener, ViewPager.PageTransformer {

    @Bind(R.id.viewpager)
    ViewPager vpMyScene;
    @Bind(R.id.errorView)
    ErrorView errorView;
    @Bind(R.id.tvPageIndicatorCurrent)
    TextView tvPageIndicatorCurrent;
    @Bind(R.id.tvPageIndicatorSize)
    TextView TvPageIndicatorSize;
    @Bind(R.id.tvPageIndicatorLabel)
    TextView tvPageIndicatorLabel;
    @Bind(R.id.llPageIndicator)
    LinearLayout llPageIndicator;
    @Bind(R.id.rgDisplayMode)
    RadioGroup radioGroup;
    @Bind(R.id.rvGrid)
    RecyclerView rvGrid;
    @Bind(R.id.rlEmptyView)
    View rlEmptyView;
    @Bind(R.id.action_buttton)
    View action_buttton;

    public static final int ANIMATION_DURATION = 350;

    private List<SceneModel> mAllSceneList = new ArrayList<>();
    private List<SceneModel> mFavoriteSceneList = new ArrayList<>();
    private MySceneAdapter mMySceneAdapter;
    private AllSceneGridAdapter mAllSceneGridAdapter;

    private String from = QuchuDetailsActivity.FROM_TYPE_HOME;

    private boolean mAddFavoriteRunning = false;
    private boolean mRefreshRunning = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recommend_hvp_new, container, false);
        ButterKnife.bind(this, view);


        action_buttton.setVisibility(View.GONE);
        if (NetUtil.isNetworkConnected(getActivity())){
            rlEmptyView.setVisibility(View.GONE);
        }else{
            rlEmptyView.setVisibility(View.VISIBLE);
        }

        rvGrid.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.half_margin), 2));
        mMySceneAdapter = new MySceneAdapter(this, mFavoriteSceneList, this);
        vpMyScene.setClipToPadding(false);
        int padding = getResources().getDimensionPixelSize(R.dimen.recommend_card_padding);
        vpMyScene.setPadding(padding, 0, padding, 0);
        vpMyScene.setPageMargin(padding / 2);
        vpMyScene.setAdapter(mMySceneAdapter);


        mAllSceneGridAdapter = new AllSceneGridAdapter(mAllSceneList, new AllSceneGridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v,int position) {



//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    Intent transitionIntent = new Intent(getActivity(), SceneDetailActivity.class);
//                    SimpleDraweeView placeImage = (SimpleDraweeView) v.findViewById(R.id.sdv);
//                    TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
//
//
//                    Pair<View, String> imagePair = Pair.create((View) placeImage, "tImage");
//                    Pair<View, String> textPair = Pair.create((View) tvTitle, "tNameHolder");
//
//                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), imagePair, textPair);
//                    ActivityCompat.startActivity(getActivity(), transitionIntent, options.toBundle());
//                    ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle();
//                }else{
                    SceneDetailActivity.enterActivity(getActivity(), mAllSceneList.get(position).getSceneId(), mAllSceneList.get(position).getSceneName(), false);
//                }
            }

            @Override
            public void onItemFavoriteClick(int position) {
                addFavoriteRunning(position);
            }
        });
        rvGrid.setAdapter(mAllSceneGridAdapter);
        rvGrid.setLayoutManager(new GridLayoutManager(getContext(), 2));
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "AGENCYFB.TTF");
        TvPageIndicatorSize.setTypeface(face);
        tvPageIndicatorLabel.setTypeface(face);
        tvPageIndicatorCurrent.setTypeface(face);
        vpMyScene.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mAllSceneList.size() > 0 && mFavoriteSceneList.size() > 0){
                    tvPageIndicatorCurrent.setText(String.valueOf(position + 1));
                    TvPageIndicatorSize.setText(String.valueOf(mMySceneAdapter.getCount()));
                }else{
                    tvPageIndicatorCurrent.setText("");
                    tvPageIndicatorLabel.setText("");
                    TvPageIndicatorSize.setText("");
                }


                if (mAllSceneList.size() == 0 && mFavoriteSceneList.size() > 0) {
                    tvPageIndicatorLabel.setText("你已经收藏全部场景");
                } else if (mFavoriteSceneList.size() == 0 && mAllSceneList.size() > 0) {
                    tvPageIndicatorLabel.setText("你还没有收藏的详情");
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbFavorites:

                        vpMyScene.clearAnimation();
                        rvGrid.clearAnimation();
                        vpMyScene.setVisibility(View.VISIBLE);

                        if (rvGrid.getChildCount()==0){
                            rvGrid.setVisibility(View.GONE);
                        }
                        for (int i = rvGrid.getChildCount() - 1; i >= 0; i--) {
                            if (rvGrid.getChildAt(i) == null) {
                                return;
                            }
                            if (i == 0) {
                                rvGrid.getChildAt(i).animate()
                                        .translationY(150)
                                        .setDuration(ANIMATION_DURATION)
                                        .setInterpolator(new AccelerateDecelerateInterpolator())
                                        .setStartDelay((rvGrid.getChildCount() - i) * 30).withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        rvGrid.setVisibility(View.GONE);
                                    }
                                }).start();
                            } else {
                                rvGrid.getChildAt(i).animate()
                                        .translationY(150)
                                        .setDuration(ANIMATION_DURATION)
                                        .setInterpolator(new AccelerateDecelerateInterpolator())
                                        .setStartDelay((rvGrid.getChildCount() - i) * 30).start();
                            }

                        }
                        rvGrid.animate().alpha(0).setDuration(ANIMATION_DURATION).start();
                        rvGrid.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                vpMyScene.animate().translationX(0).alpha(1).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(ANIMATION_DURATION).start();
                            }
                        }, rvGrid.getChildCount() * 30);

                        llPageIndicator.setVisibility(View.VISIBLE);
//                        tvPageIndicator.animate()
//                                .withLayer()
//                                .alpha(1)
//                                .translationY(0)
//                                .setInterpolator(new AccelerateDecelerateInterpolator())
//                                .setDuration(ANIMATION_DURATION)
//                                .withStartAction(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        tvPageIndicator.setVisibility(View.VISIBLE);
//                                    }
//                                }).start();
                        break;
                    case R.id.rbAll:

                        vpMyScene.clearAnimation();
                        rvGrid.clearAnimation();
                        int edge = ScreenUtils.getScreenWidth(getActivity());
                        vpMyScene.animate().translationX(edge).alpha(0).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(ANIMATION_DURATION).start();
                        rvGrid.setVisibility(View.VISIBLE);
                        rvGrid.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < rvGrid.getChildCount(); i++) {
                                    rvGrid.getChildAt(i).setTranslationY(150);
                                    rvGrid.getChildAt(i).animate().translationY(0).setDuration(ANIMATION_DURATION).setInterpolator(new AccelerateDecelerateInterpolator()).setStartDelay(i * 30).start();
                                }
                            }
                        }, 10);
                        rvGrid.animate().alpha(1).setDuration(ANIMATION_DURATION).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                vpMyScene.setVisibility(View.GONE);
                            }
                        }).start();
//                        tvPageIndicator.animate()
//                                .alpha(0)
//                                .translationY(tvPageIndicator.getHeight())
//                                .setInterpolator(new AccelerateDecelerateInterpolator())
//                                .setDuration(ANIMATION_DURATION)
//                                .withEndAction(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        tvPageIndicator.setVisibility(View.GONE);
//                                    }
//                                })
//                                .start();
                        llPageIndicator.setVisibility(View.GONE);
                        break;
                }
            }
        });

        view.setClickable(true);

        DialogUtil.showProgess(getActivity(), R.string.loading_dialog_text);

        getMyScene();
        getData(false);
        return view;
    }


    private void addFavoriteRunning(final int position) {
        if (!NetUtil.isNetworkConnected(getActivity())){
            Toast.makeText(getActivity(),R.string.network_error,Toast.LENGTH_SHORT).show();
            return;
        }
        if (mAddFavoriteRunning) {
            return;
        }
        mAddFavoriteRunning = true;

        int sid = mAllSceneList.get(position).getSceneId();
        ScenePresenter.addFavoriteScene(getContext(), sid, new CommonListener() {
            @Override
            public void successListener(Object response) {
                notifyAdapters(position, true);
                mAddFavoriteRunning = false;
                //Toast.makeText(getActivity(),R.string.add_to_favorite_success,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {
                //Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
                mAddFavoriteRunning = false;
            }
        });
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        accessPushMessage();
    }

    public void accessPushMessage() {
        Parcelable extra = getActivity().getIntent().getParcelableExtra(GeTuiReceiver.REQUEST_KEY_MODEL);
        if (extra == null)
            return;

        PushMessageBean bean = (PushMessageBean) extra;
//            说明： 类型：( 01 PGC新内容发布  02  新场景发布  03 事件营销 )
//            eventId  : 根据类别，打开应用相应页面的ID  type: 01 为文章ID  02:场景ID  03：文章ID
        switch (bean.getType()) {
            case "01":

                break;
            case "02"://切换到场景工坊页面
                vpMyScene.setVisibility(View.GONE);
                radioGroup.check(R.id.rbAll);
                break;
            case "03":

                break;
        }

    }


    public void getMyScene() {
        ScenePresenter.getMyScene(getContext(), SPUtils.getCityId(), 1, new CommonListener<PagerModel<SceneModel>>() {
            @Override
            public void successListener(PagerModel<SceneModel> response) {
                DialogUtil.dismissProgessDirectly();
                errorView.hideView();

                if (response != null && response.getResult() != null) {
                    mFavoriteSceneList.clear();
                    mFavoriteSceneList.addAll(response.getResult());
                    mMySceneAdapter.notifyDataSetChanged();
                    tvPageIndicatorCurrent.setText(String.valueOf(vpMyScene.getCurrentItem() + 1));
                    TvPageIndicatorSize.setText(String.valueOf(mMySceneAdapter.getCount()));
                }

            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {
                DialogUtil.dismissProgessDirectly();
                errorView.showViewDefault(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtil.showProgess(getActivity(), "加载中");
                        getMyScene();
                    }
                });
            }
        });
    }

    public void getData(final boolean loadMore) {
        mRefreshRunning = true;

        ScenePresenter.getAllScene(getContext(), SPUtils.getCityId(), 1, new CommonListener<PagerModel<SceneModel>>() {

            @Override
            public void successListener(PagerModel<SceneModel> response) {
                if (response != null && response.getResult() != null) {
                    if (!loadMore) {
                        mAllSceneList.clear();
                    }
                    mAllSceneList.addAll(response.getResult());
                    mAllSceneGridAdapter.notifyDataSetChanged();

                }
                mRefreshRunning = false;
            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {
                mRefreshRunning = false;
            }
        });
    }


    @Override
    public void onCardLick(View view, int position) {

        if (from.equals(QuchuDetailsActivity.FROM_TYPE_HOME)) {
            MobclickAgent.onEvent(getContext(), "detail_home_c");
        } else {
            MobclickAgent.onEvent(getContext(), "detail_tag_c");
        }
        MobclickAgent.onEvent(getActivity(), "detail_c");

        SceneDetailActivity.enterActivity(getActivity(), mFavoriteSceneList.get(position).getSceneId(), mFavoriteSceneList.get(position).getSceneName(), true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle();
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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

    @Override
    public void onResume() {
        MobclickAgent.onPageStart("h_recommendtion");
        super.onResume();
    }

    @Override
    public void onPause() {
        MobclickAgent.onPageEnd("h_recommendtion");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Subscribe
    public void onMessageEvent(QuchuEventModel event) {
        if (null == event) {
            return;
        }
        int sid;

        int index = -1;

        switch (event.getFlag()) {
            case EventFlags.EVENT_SCENE_FAVORITE:
                sid = (int) event.getContent()[0];
                for (int j = 0; j < mAllSceneList.size(); j++) {
                    if (mAllSceneList.get(j).getSceneId() == sid) {
                        index = j;
                    }
                }

                notifyAdapters(index, true);
                break;
            case EventFlags.EVENT_SCENE_CANCEL_FAVORITE:
                sid = (int) event.getContent()[0];
                for (int j = 0; j < mFavoriteSceneList.size(); j++) {
                    if (mFavoriteSceneList.get(j).getSceneId() == sid) {
                        index = j;
                    }
                }
                notifyAdapters(index, false);

                break;

            case EventFlags.EVENT_DEVICE_NETWORK_AVAILABLE:
                rlEmptyView.setVisibility(View.GONE);
                getMyScene();
                getData(false);
                break;


            case EventFlags.EVENT_USER_LOGIN_SUCCESS:
            case EventFlags.EVENT_USER_LOGOUT:
                mAllSceneList.clear();
                mFavoriteSceneList.clear();
                getMyScene();
                getData(false);
                break;
        }
    }

    private void notifyAdapters(int index, boolean add) {

        if (index == -1) {
            return;
        }

        if (add) {
            mFavoriteSceneList.add(0, mAllSceneList.get(index));
            mAllSceneList.remove(index);
        } else {
            mAllSceneList.add(mFavoriteSceneList.get(index));
            mFavoriteSceneList.remove(index);
        }


        if (mAllSceneList.size() == 0 && mFavoriteSceneList.size() > 0) {
            tvPageIndicatorLabel.setText("你已经收藏全部场景");
            tvPageIndicatorCurrent.setText("");
            TvPageIndicatorSize.setText("");
        } else if (mFavoriteSceneList.size() == 0 && mAllSceneList.size() > 0) {
            tvPageIndicatorLabel.setText("你还没有收藏的详情");
            tvPageIndicatorCurrent.setText("");
            TvPageIndicatorSize.setText("");

        } else {

            if (mAllSceneList.size() > 0 && mFavoriteSceneList.size() > 0){

                tvPageIndicatorLabel.setText("of");
                if (vpMyScene.getChildCount()>0){
                    tvPageIndicatorCurrent.setText(String.valueOf(vpMyScene.getCurrentItem() + 1));
                }else{
                    if (null!=mFavoriteSceneList){
                        tvPageIndicatorCurrent.setText(String.valueOf(1));
                    }else{
                        tvPageIndicatorCurrent.setText(mFavoriteSceneList.size());
                    }
                }
                TvPageIndicatorSize.setText(String.valueOf(mFavoriteSceneList.size()));
            }else{
                tvPageIndicatorCurrent.setText("");
                tvPageIndicatorLabel.setText("");
            }



        }

        mMySceneAdapter.notifyDataSetChanged();
        mAllSceneGridAdapter.notifyDataSetChanged();
    }


    public static float MIN_SCALE = .9f;

    @Override
    public void transformPage(View page, float position) {
//        LogUtils.e("id: " + page + " position:" + position);
        if (position <= 1) {
            if (position < 0) {//滑出的页 0.0 ~ -1 *
                float scaleFactor = (1 - MIN_SCALE) * (0 - position);
                page.setScaleY(1 - scaleFactor);
            } else {//滑进的页 1 ~ 0.0 *
                float scaleFactor = (1 - MIN_SCALE) * (1 - position);
                page.setScaleY(MIN_SCALE + scaleFactor);
            }
        }

    }

    public ViewPager getVpMyScene() {
        return vpMyScene;
    }

}
