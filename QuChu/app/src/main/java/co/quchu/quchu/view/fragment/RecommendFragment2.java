package co.quchu.quchu.view.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.LinearInterpolator;
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
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.analysis.GatherCollectModel;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.blurdialogfragment.FastBlurHelper;
import co.quchu.quchu.blurdialogfragment.RenderScriptBlurHelper;
import co.quchu.quchu.dialog.ShareDialogFg;
import co.quchu.quchu.dialog.VisitorLoginDialogFg;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.presenter.InterestingDetailPresenter;
import co.quchu.quchu.presenter.RecommentFragPresenter;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.view.activity.InterestingDetailsActivity;
import co.quchu.quchu.view.adapter.RecommendAdapter2;
import co.quchu.quchu.widget.recyclerviewpager.RecyclerViewPager;

/**
 * RecommendFragment
 * User: Chenhs
 * Date: 2015-12-07
 * 推荐
 */
public class RecommendFragment2 extends Fragment implements RecommendAdapter2.CardClickListener, IRecommendFragment, RecyclerViewPager.OnPageChangedListener {
    @Bind(R.id.f_recommend_rvp)
    RecyclerViewPager recyclerView;
    public List<RecommendModel> cardList;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.f_recommend_bimg_bottom)
    ImageView fRecommendBimgBottom;
    @Bind(R.id.f_recommend_bimg_top)
    ImageView fRecommendBimgTop;
    private boolean isLoading = false;
    private RecommendAdapter2 adapter;

    private RecommentFragPresenter presenter;

    private final int MESSAGE_FLAG_DELAY_TRIGGER = 0x0001;
    private final int MESSAGE_FLAG_BLUR_RENDERING_FINISH = 0x0002;
    public static final String MESSAGE_KEY_URI = "MESSAGE_KEY_URI";
    public static final String MESSAGE_KEY_BITMAP = "MESSAGE_KEY_BITMAP";

    private int currentIndex = -1;
    private int currentBGIndex = -1;

    private class BlurEffectRunnable implements Runnable {

        private final int index;

        public BlurEffectRunnable(int postIndex){
            index = postIndex;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (-1!=currentIndex && index == currentIndex && currentBGIndex != currentIndex){
                if (null!=cardList&&cardList.size()>currentIndex){
                    String strUri = cardList.get(currentIndex).getCover();
                    Uri imgUri;
                    if (null!=strUri){
                        imgUri = Uri.parse(strUri);
                        Message message = new Message();
                        message.what = MESSAGE_FLAG_DELAY_TRIGGER;
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(MESSAGE_KEY_URI,imgUri);
                        message.setData(bundle);
                        mBlurEffectAnimationHandler.sendMessage(message);
                    }
                }
            }
        }
    }

    private ObjectAnimator animFadeIn;
    private ObjectAnimator animFadeOut;
    private Handler mBlurEffectAnimationHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){

                case MESSAGE_FLAG_BLUR_RENDERING_FINISH:
                    Bitmap sourceBitmap = msg.getData().getParcelable(MESSAGE_KEY_BITMAP);
                    Log.e("BITMAP SIZE",sourceBitmap.getWidth()+"|"+sourceBitmap.getHeight());
//                                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
//                                        sourceBitmap = RenderScriptBlurHelper.doBlur(sourceBitmap,60,false,getActivity());
//                                    }else{
                    sourceBitmap = Bitmap.createScaledBitmap(sourceBitmap,fRecommendBimgBottom.getWidth()/4,fRecommendBimgBottom.getHeight()/4,false);
                    long tsCur = System.currentTimeMillis();
                    sourceBitmap = FastBlurHelper.doBlur(sourceBitmap,10,false);
//                                    }
                    Log.e("Rendering time spend",String.valueOf(System.currentTimeMillis() - tsCur));


                    if (fRecommendBimgBottom.getVisibility()==View.VISIBLE){
                        fRecommendBimgBottom.setImageBitmap(sourceBitmap);
                    }else {
                        fRecommendBimgTop.setImageBitmap(sourceBitmap);
                    }

                    AnimatorSet animatorSet = new AnimatorSet();
                    animFadeIn  = ObjectAnimator.ofFloat(fRecommendBimgBottom, "alpha", .2f, 1f);
                    animFadeOut = ObjectAnimator.ofFloat(fRecommendBimgTop, "alpha", 1f, 0.2f);
                    animatorSet.setDuration(animationDuration);
                    animatorSet.setInterpolator(new LinearInterpolator());
                    animatorSet.playTogether(animFadeOut, animFadeIn);
                    animatorSet.start();
                    currentBGIndex = currentIndex;

                    break;


                case MESSAGE_FLAG_DELAY_TRIGGER:
                    Uri imageUri = msg.getData().getParcelable(MESSAGE_KEY_URI);

                    //if true > both of views are invisible
                    //boolean fact = fRecommendBimgBottom.getVisibility()==fRecommendBimgTop.getVisibility()&&fRecommendBimgTop.getVisibility()!=View.VISIBLE;
                    if (Fresco.getImagePipeline().isInBitmapMemoryCache(imageUri)){
                        ImageRequest request = ImageRequestBuilder
                            .newBuilderWithSource(imageUri)
                            .setImageType(ImageRequest.ImageType.SMALL)
                            .setPostprocessor(new Postprocessor() {
                                @Override
                                public CloseableReference<Bitmap> process(Bitmap sourceBitmap, PlatformBitmapFactory bitmapFactory) {
                                    Message msg = new Message();
                                    msg.what = MESSAGE_FLAG_BLUR_RENDERING_FINISH;
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable(MESSAGE_KEY_BITMAP,sourceBitmap);
                                    msg.setData(bundle);
                                    mBlurEffectAnimationHandler.sendMessage(msg);
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

                        Fresco.getImagePipeline().fetchImageFromBitmapCache(request,getActivity());
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
        presenter = new RecommentFragPresenter(getContext(), this);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layout);
        adapter = new RecommendAdapter2(getActivity(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener();
        recyclerView.addOnPageChangedListener(this);
        recyclerView.addOnLayoutChangeListener();

        presenter.init();

        return view;
    }

    @Override
    public void OnPageChanged(int oldPosition, int newPosition) {
        LogUtils.json("newPosition=" + newPosition + "//oldPosition=" + oldPosition + "//cardList.size() - 1===" + (cardList.size() - 1));
        if (newPosition > oldPosition && cardList.size() > 3 && newPosition == cardList.size() - 1 && !isLoading) {
            if (pageNums < pageCounts) {
                isLoading = true;
                presenter.loadMore("", true);
            }
        }
        currentIndex = newPosition;
        new Thread(new BlurEffectRunnable(newPosition)).start();
    }


    private int pageCounts = 2, pageNums = 1;
    private int hasChangePosition = 0;

    @Override
    public void onCardLick(View view, int position) {

        switch (view.getId()) {
            case R.id.root_cv:
                AppContext.selectedPlace = cardList.get(position);
                hasChangePosition = position;
                Intent intent = new Intent(getActivity(), InterestingDetailsActivity.class);
                intent.putExtra("pPosition", position);
                intent.putExtra("pId", cardList.get(position).getPid());
                startActivity(intent);
                break;
            case R.id.item_recommend_card_collect_rl:
                setFavorite(position);
                break;
            case R.id.item_recommend_card_interest_rl:
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
        cardList.set(hasChangePosition, AppContext.selectedPlace);
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    public void removeDataSet(int removeIndex) {
        if (adapter != null && removeIndex < cardList.size()) {
            LogUtils.json("removeDataSet==" + removeIndex);
            cardList.remove(removeIndex);
            adapter.notifyDataSetChanged();
        }
    }

    public void hint() {
        if (recyclerView != null)
            recyclerView.setVisibility(View.GONE);
    }

    public void show() {
        if (recyclerView != null)
            recyclerView.setVisibility(View.VISIBLE);
    }


    @Override
    public void initTab() {
        for (int i = 0; i < 10; i++) {
            TextView textView = (TextView) View.inflate(getActivity(), R.layout.text_view, null);
            textView.setText("兴趣45");
            if (i == 0) {
                textView.setTextSize(15);
            } else {
                textView.setTextSize(13);
            }
//            tabLayout.addTab(tabLayout.newTab().setText("兴趣1"));
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
                presenter.initTabData(true);
                recyclerView.setVisibility(View.INVISIBLE);
                startAnimation();
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
        presenter.initTabData(true);
    }

    @Override
    public void initTabData(boolean isError, List<RecommendModel> arrayList, int pageCount, int pageNum) {
        if (isError) {
            Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
        } else {
            cardList = arrayList;
            adapter.changeDataSet(cardList);
            pageCounts = pageCount;
            pageNums = pageNum;
            if (cardList.size() > 0)
                recyclerView.smoothScrollToPosition(0);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void loadMore(boolean isError, List<RecommendModel> arrayList, int pageCount, int pageNum) {
        isLoading = false;
        if (isError) {
            Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
        } else {
            pageCounts = pageCount;
            pageNums = pageNum;
            if (arrayList != null && arrayList.size() > 0) {
                cardList.addAll(arrayList);
                adapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void tabChangeAnimaton() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    /**
     * animation start
     */
    private long animationDuration = 500;

    private void startAnimation() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(fRecommendBimgTop, "alpha", 1f, 0.2f);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(fRecommendBimgBottom, "alpha", 0.2f, 1f);
        fRecommendBimgTop.setVisibility(View.INVISIBLE);
        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.setDuration(animationDuration);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.playTogether(objectAnimator, objectAnimator2);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animatorSet.start();
    }
    /**
     * animation end
     */
}
