package co.quchu.quchu.view.fragment;

import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.cache.common.CacheKey;
import com.facebook.common.references.CloseableReference;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.analysis.GatherCollectModel;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.dialog.ShareDialogFg;
import co.quchu.quchu.dialog.VisitorLoginDialogFg;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.model.TagsModel;
import co.quchu.quchu.presenter.InterestingDetailPresenter;
import co.quchu.quchu.presenter.RecommentFragPresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.ImageUtils;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.view.activity.QuchuDetailsActivity;
import co.quchu.quchu.view.adapter.RecommendAdapter;
import co.quchu.quchu.widget.ErrorView;
import co.quchu.quchu.widget.RefreshLayout.HorizontalSwipeRefLayout;
import co.quchu.quchu.widget.recyclerviewpager.RecyclerViewPager;


/**
 * RecommendFragment
 * User: Chenhs
 * Date: 2015-12-07
 * 推荐
 */
public class RecommendFragment extends BaseFragment implements RecommendAdapter.CardClickListener, IRecommendFragment, RecyclerViewPager.OnPageChangedListener {
    @Bind(R.id.recyclerView)
    RecyclerViewPager recyclerView;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.f_recommend_bimg_top)
    ImageView fRecommendBimgTop;
    @Bind(R.id.refreshLayout)
    HorizontalSwipeRefLayout refreshLayout;
    @Bind(R.id.errorView)
    ErrorView errorView;

    private boolean isLoading = false;
    public List<RecommendModel> cardList = new ArrayList<>();
    private RecommendAdapter adapter;
    private RecommentFragPresenter presenter;
    private int currentIndex = -1;
    private int currentBGIndex = -1;
    private final int MESSAGE_FLAG_DELAY_TRIGGER = 0x0001;
    private final int MESSAGE_FLAG_BLUR_RENDERING_FINISH = 0x0002;
    public static final String MESSAGE_KEY_BITMAP = "MESSAGE_KEY_BITMAP";
    private boolean mFragmentStoped;

    Bitmap mSourceBitmap;
    private int index;
    private Handler mBlurEffectAnimationHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mFragmentStoped) {
                return;
            }
            switch (msg.what) {
                case MESSAGE_FLAG_BLUR_RENDERING_FINISH:
                    mSourceBitmap = msg.getData().getParcelable(MESSAGE_KEY_BITMAP);
                    if (null != mSourceBitmap && !mSourceBitmap.isRecycled()) {
                        executeSwitchAnimation(ImageUtils.doBlur(mSourceBitmap, fRecommendBimgTop.getWidth() / 4, fRecommendBimgTop.getHeight() / 4));
                    }
                    currentBGIndex = currentIndex;
                    break;

                case MESSAGE_FLAG_DELAY_TRIGGER:
                    if (-1 != currentIndex && index == currentIndex && currentBGIndex != currentIndex) {
                        String strUri = cardList.get(currentIndex).getCover();
                        if (!TextUtils.isEmpty(strUri)) {
                            Uri imageUri = Uri.parse(strUri);
                            if (Fresco.getImagePipeline().isInBitmapMemoryCache(imageUri)) {
                                ImageRequest request = ImageRequestBuilder
                                        .newBuilderWithSource(imageUri)
                                        .setImageType(ImageRequest.ImageType.SMALL)
                                        .setPostprocessor(new Postprocessor() {
                                            @Override
                                            public CloseableReference<Bitmap> process(Bitmap sourceBitmap, PlatformBitmapFactory bitmapFactory) {
                                                if (null != sourceBitmap) {
                                                    Message msg = new Message();
                                                    msg.what = MESSAGE_FLAG_BLUR_RENDERING_FINISH;
                                                    Bundle bundle = new Bundle();
                                                    bundle.putParcelable(MESSAGE_KEY_BITMAP, sourceBitmap);
                                                    msg.setData(bundle);
                                                    mBlurEffectAnimationHandler.sendMessage(msg);
                                                }
                                                return null;
                                            }

                                            @Override
                                            public String getName() {
                                                return null;
                                            }

                                            @Override
                                            public CacheKey getPostprocessorCacheKey() {
                                                return null;
                                            }
                                        })
                                        .build();
                                Fresco.getImagePipeline().fetchImageFromBitmapCache(request, getActivity());
                            }
                        }
                    }
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend_hvp_new, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layout);
        adapter = new RecommendAdapter(getActivity(), cardList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener();
        recyclerView.addOnPageChangedListener(this);
        presenter = new RecommentFragPresenter(getContext(), this);
        recyclerView.addOnLayoutChangeListener();
        refreshLayout.setColorSchemeResources(R.color.planet_progress_yellow);
        initData();

        refreshLayout.setOnRefreshListener(new HorizontalSwipeRefLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.initTabData(true, selectedTag);
            }
        });

        return view;
    }

    public void initData() {
        presenter.init();

    }

    @Override
    public void OnPageChanged(int oldPosition, int newPosition) {
        LogUtils.json("newPosition=" + newPosition + "//oldPosition=" + oldPosition + "//cardList.size() - 1===" + (cardList.size() - 1));
        if (newPosition > oldPosition && cardList.size() > 9 && pageNums < pageCounts) {
            if (newPosition == cardList.size() - 2 && !isLoading) {
                isLoading = true;
                presenter.loadMore(selectedTag, pageNums);
            } else if (isLoading) {
                DialogUtil.showProgess(getActivity(), R.string.loading_dialog_text);
            }
        }
        currentIndex = newPosition;
        index = newPosition;
        mBlurEffectAnimationHandler.sendEmptyMessageDelayed(MESSAGE_FLAG_DELAY_TRIGGER, 300L);
    }


    private int pageCounts, pageNums;
    private int hasChangePosition = 0;

    @Override
    public void onCardLick(View view, int position) {

        switch (view.getId()) {
            case R.id.root_cv:
                AppContext.selectedPlace = cardList.get(position);
                hasChangePosition = position;
                if (!KeyboardUtils.isFastDoubleClick()) {
                    Intent intent = new Intent(getActivity(), QuchuDetailsActivity.class);
                    intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_POSITION, position);
                    intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, cardList.get(position).getPid());
                    startActivity(intent);
                }
                break;
            case R.id.item_recommend_card_collect_iv:
                //收藏
                setFavorite(position);
                break;
            case R.id.item_recommend_card_interest_iv:
                //分享
                ShareDialogFg shareDialogFg = ShareDialogFg.newInstance(cardList.get(position).getPid(), cardList.get(position).getName(), true);
                shareDialogFg.show(getActivity().getFragmentManager(), "share_place");
                break;
        }
    }

    private void setFavorite(final int position) {
        if (AppContext.user.isIsVisitors()) {
            VisitorLoginDialogFg vDialog = VisitorLoginDialogFg.newInstance(VisitorLoginDialogFg.QFAVORITE);
            vDialog.show(getActivity().getFragmentManager(), "visitor");
        } else {
            InterestingDetailPresenter.setDetailFavorite(getActivity(), cardList.get(position).getPid(), cardList.get(position).isIsf(), new InterestingDetailPresenter.DetailDataListener() {
                @Override
                public void onSuccessCall(String str) {
                    cardList.get(position).setIsf(!cardList.get(position).isIsf());
                    adapter.notifyDataSetChanged();
                    if (cardList.get(position).isIsf()) {
                        Toast.makeText(getActivity(), "收藏成功!", Toast.LENGTH_SHORT).show();
                        AppContext.gatherList.add(new GatherCollectModel(GatherCollectModel.collectPlace, cardList.get(position).getPid()));
                    } else {
                        Toast.makeText(getActivity(), "取消收藏!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onErrorCall(String str) {

                }
            });
        }
    }

    public void updateDateSet() {
        if (null != cardList && cardList.size() > hasChangePosition) {
            cardList.set(hasChangePosition, AppContext.selectedPlace);
            if (adapter != null)
                adapter.notifyDataSetChanged();
        }
    }


    private List<TagsModel> tagList;

    @Override
    public void initTab(boolean isError, List<TagsModel> list) {

        if (isError) {
            errorView.showViewDefault(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.init();
                }
            });
            tabLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            return;
        }
        tabLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        errorView.himeView();

        tagList = list;
        for (int i = 0; i < list.size(); i++) {
            TextView textView = (TextView) View.inflate(getActivity(), R.layout.text_view, null);
            textView.setText(list.get(i).getZh());
            if (i == 0) {
                textView.setTextSize(15);
            } else {
                textView.setTextSize(13);
            }
            tabLayout.addTab(tabLayout.newTab().setCustomView(textView));
        }
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView view = (TextView) tab.getCustomView();
                if (view != null) {
                    view.setTextSize(15);
                }
                selectedTag = tagList.get(tab.getPosition()).getEn();
                LogUtils.json("selectedTag=" + selectedTag);
                presenter.initTabData(false, selectedTag);
                refreshLayout.setRefreshing(false);
                mBlurEffectAnimationHandler.sendEmptyMessageDelayed(MESSAGE_FLAG_DELAY_TRIGGER, 500L);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView view = (TextView) tab.getCustomView();
                if (view != null) {
                    view.setTextSize(13);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        if (tagList.size() > 0) {
            selectedTag = tagList.get(0).getEn();
            presenter.initTabData(false, selectedTag);
        }
    }

    private String selectedTag = "";

    @Override
    public void initTabData(boolean isError, List<RecommendModel> arrayList, int pageCount, int pageNum) {
        refreshLayout.setRefreshing(false);
        if (isError) {
            errorView.showViewDefault(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.initTabData(false, selectedTag);
                }
            });
            recyclerView.setVisibility(View.GONE);
        } else {
            if (recyclerView.getVisibility() == View.GONE) {
                errorView.himeView();
                recyclerView.setVisibility(View.VISIBLE);
            }
            cardList.clear();
            cardList.addAll(arrayList);
            adapter.notifyDataSetChanged();
            pageCounts = pageCount;
            pageNums = ++pageNum;
            if (cardList.size() > 0)
                recyclerView.smoothScrollToPosition(0);

            index = currentIndex = 0;
            mBlurEffectAnimationHandler.sendEmptyMessageDelayed(MESSAGE_FLAG_DELAY_TRIGGER, 300L);
        }
    }

    @Override
    public void loadMore(boolean isError, List<RecommendModel> arrayList, int pageCount, int pageNum) {
        isLoading = false;
        DialogUtil.dismissProgessDirectly();
        if (isError) {
            Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
        } else {
            pageCounts = pageCount;
            pageNums = ++pageNum;
            if (arrayList != null && arrayList.size() > 0) {
                cardList.addAll(arrayList);
                adapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    /**
     * 执行切换动画
     */
    private void executeSwitchAnimation(Bitmap bm) {
        if (null == bm || bm.isRecycled()) {
            return;
        }

        final Bitmap finalBm = bm;
        fRecommendBimgTop.animate()
                .alpha(.6f)
                .alphaBy(1f)
                .setDuration(800)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(android.animation.Animator animation) {
                        fRecommendBimgTop.setAlpha(.9f);
                        fRecommendBimgTop.setImageBitmap(finalBm);
                        if (null != fRecommendBimgTop)
                            fRecommendBimgTop.animate()
                                    .alpha(1f)
                                    .alphaBy(.6f)
                                    .setInterpolator(new AccelerateInterpolator())
                                    .setDuration(800)
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(android.animation.Animator animation) {
                                            if (null != fRecommendBimgTop)
                                                fRecommendBimgTop.setAlpha(1f);
                                        }
                                    })
                                    .start();
                    }
                }).start();


    }

    @Override
    public void onStop() {
        mFragmentStoped = true;
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFragmentStoped = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (null != mSourceBitmap && !mSourceBitmap.isRecycled())
            mSourceBitmap.recycle();
        if (mSourceBitmap != null)
            mSourceBitmap = null;
    }

    @Subscribe
    public void onMessageEvent(QuchuEventModel event) {

        if (event.getFlag() == EventFlags.EVENT_QUCHU_DETAIL_UPDATED) {
            if ((Integer) event.getContent() == cardList.get(currentIndex).getPid()) {
                cardList.get(currentIndex).isout = true;
                adapter.notifyItemChanged(currentIndex);
                EventBus.getDefault().register(this);
            }
        }
    }
}
