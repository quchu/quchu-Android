package co.quchu.quchu.view.fragment;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.model.AIConversationModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.AIConversationPresenter;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.ScreenUtils;
import co.quchu.quchu.view.activity.SearchActivity;
import co.quchu.quchu.view.adapter.AIConversationAdapter;
import co.quchu.quchu.view.adapter.TextOptionAdapter;
import co.quchu.quchu.widget.ConversationListAnimator;
import co.quchu.quchu.widget.DynamicItemDecoration;
import co.quchu.quchu.widget.ScrollToLinearLayoutManager;
import co.quchu.quchu.widget.XiaoQFab;
import com.android.volley.VolleyError;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import me.everything.android.ui.overscroll.IOverScrollDecor;
import me.everything.android.ui.overscroll.IOverScrollUpdateListener;
import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.IOverScrollDecoratorAdapter;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Nico on 16/11/3.
 */

public class AIConversationFragment extends BaseFragment
    implements TextOptionAdapter.OnInteractiveClick, AIConversationAdapter.OnPlaceClickListener {

  private AIConversationAdapter mAdapter;
  private List<AIConversationModel> mConversation = new ArrayList<>();
  private List<AIConversationModel> mHistory;
  private List<AIConversationModel> mHistoryHourBefore = new ArrayList<>();
  public static final int CONVERSATION_REQUEST_DELAY = 500;

  private int mScreenHeight;
  private int mAppbarOffSet;
  private XiaoQFab mXiaoQFab;
  private boolean mNetworkBusy = false;
  private boolean mHideAnimRunning = false;
  private boolean mShowAnimRunning = false;
  public float offSetY = 0;

  //是否断开网络
  private boolean mNetworkInterrupted = false;

  @Bind(R.id.rv) RecyclerView mRecyclerView;
  @Bind(R.id.rvOptions) RecyclerView rvOptions;
  @Bind(R.id.llOptions) View llOptions;
  @Bind(R.id.tvOption) TextView mTvOption;
  @Bind(R.id.ivGuide) ImageView ivGuide;
  @Bind(R.id.tvPullUpToLoad) TextView tvPullUpToLoad;
  @Bind(R.id.quickReturn) View quickReturn;

  private boolean mHistoryLoaded = false;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View v = inflater.inflate(R.layout.fragment_ai_conversation, container, false);

    ButterKnife.bind(this, v);

    mScreenHeight = ScreenUtils.getScreenHeight(getActivity());

    final View appbar = getActivity().findViewById(R.id.appbar);
    appbar.getViewTreeObserver()
        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override public void onGlobalLayout() {
            appbar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            mAppbarOffSet = appbar.getHeight() + ScreenUtils.getStatusHeight(getActivity());
          }
        });

    mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
      }

      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (null != mRecyclerView.getAdapter()) {
          if (mRecyclerView.getAdapter().getItemCount()
              - ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition()
              >= 15) {
            quickReturn.setVisibility(View.VISIBLE);
          } else {
            quickReturn.setVisibility(View.GONE);
          }
        }

        if (mConversation == null || mConversation.size() < 1) {
          return;
        }

        int visibleItemCount = mRecyclerView.getLayoutManager().getChildCount();
        int totalItemCount = mRecyclerView.getLayoutManager().getItemCount();
        int pastVisibleItems =
            ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();

        if (pastVisibleItems + visibleItemCount >= totalItemCount) {
          //End of list
          showOptions();
        } else {
          if (!mHideAnimRunning) {
            hideOptions();
          }
        }

        if (!mRecyclerView.canScrollVertically(1)) {
          mShowAnimRunning = false;
          showOptions();
        }
      }
    });

    mRecyclerView.setLayoutManager(new ScrollToLinearLayoutManager(getActivity()));
    mRecyclerView.addItemDecoration(new DynamicItemDecoration());
    mRecyclerView.setItemAnimator(new ConversationListAnimator());
    mAdapter = new AIConversationAdapter(getActivity(), mConversation, this,this);
    mRecyclerView.setAdapter(mAdapter);

    VerticalOverScrollBounceEffectDecorator s =
        new VerticalOverScrollBounceEffectDecorator(new IOverScrollDecoratorAdapter() {

          @Override public View getView() {
            return mRecyclerView;
          }

          @Override public boolean isInAbsoluteStart() {
            // canScrollUp() is an example of a method you must implement
            return !mRecyclerView.canScrollVertically(-1) && (offset == 0);
          }

          @Override public boolean isInAbsoluteEnd() {
            // canScrollDown() is an example of a method you must implement
            return !mRecyclerView.canScrollVertically(1);
          }
        });
    s.setOverScrollUpdateListener(new IOverScrollUpdateListener() {
      @Override public void onOverScrollUpdate(IOverScrollDecor decor, int state, float offset) {
        float scrollDistance = ScreenUtils.getScreenHeight(getActivity())/20;

        if (offset > scrollDistance && mHistoryHourBefore.size() > 0) {
          tvPullUpToLoad.setVisibility(View.VISIBLE);
        } else {
          tvPullUpToLoad.setVisibility(View.GONE);
        }

        if (offset > scrollDistance && state == 3 && !mHistoryLoaded && mHistoryHourBefore.size() > 0) {
          mHistoryLoaded = true;
          new Handler().postDelayed(new Runnable() {
            @Override public void run() {
              AIConversationModel divider = new AIConversationModel();
              divider.setDataType(AIConversationModel.EnumDataType.DIVIDER);
              mConversation.add(0, divider);
              mConversation.addAll(0, mHistoryHourBefore);
              mAdapter.notifyItemRangeInserted(0, mHistoryHourBefore.size() + 1);
              mRecyclerView.smoothScrollToPosition(mHistoryHourBefore.size() - 1);
            }
          }, 300);
        }
      }
    });

    mXiaoQFab = (XiaoQFab) getActivity().findViewById(R.id.fab);

    mXiaoQFab.postDelayed(new Runnable() {
      @Override public void run() {
        mXiaoQFab.animateInitial();
      }
    }, 200);

    final int deletedRows = deleteHistoryIfNeed();
    AIConversationPresenter.delOptionMessages(getActivity());

    mHistoryHourBefore = AIConversationPresenter.getMessages(getActivity(), 1);
    mHistory = AIConversationPresenter.getMessages(getActivity(), 2);

    //TODO
    mConversation.addAll(mHistory);
    mAdapter.notifyDataSetChanged();
    if (mConversation.size() > 0) {
      mRecyclerView.scrollToPosition(mConversation.size() - 1);
    }

    quickReturn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        quickReturn.setVisibility(View.GONE);
        if (null != mRecyclerView.getAdapter()) {
          if (mRecyclerView.getAdapter().getItemCount()
              - ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition()
              >= 30) {
            mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
          } else {
            scrollToBottom();
          }
        }
      }
    });

    mXiaoQFab.postDelayed(new Runnable() {
      @Override public void run() {

        if (mHistory.size() > 0) {
          startConversation("03");
        } else if (deletedRows > 0) {
          startConversation("04");
        } else if (mConversation.size() > 1
            && System.currentTimeMillis() - mConversation.get(mConversation.size() - 1)
            .getTimeStamp() > (1000 * 60 * 60)) {
          //最后一条数据若大于1小时重启对话
          startConversation("05");
        } else {
          startConversation("01");
        }
      }
    }, 1500);
    return v;
  }

  @Override protected String getPageNameCN() {
    return null;
  }

  /**
   * 更新选择项
   */
  private void hideAndUpdate(int selected) {
    int index = rvOptions.getChildCount() == 1 ? 0 : selected;

    playTheFuckingSound(index);

    if(null==rvOptions.getChildAt(index) || null==rvOptions.findViewById(R.id.tvOption)){
      return;
    }
    final TextView selectedTarget =
        (TextView) rvOptions.getChildAt(index).findViewById(R.id.tvOption);

    int[] answerLocation = new int[2];
    int[] targetLocation = new int[2];
    selectedTarget.getLocationInWindow(answerLocation);

    mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 2).getLocationInWindow(targetLocation);

    float translationY =
        targetLocation[1] - answerLocation[1] + getResources().getDimensionPixelSize(
            R.dimen.half_margin);
    float translationX = mRecyclerView.getWidth()
        - selectedTarget.getWidth()
        - answerLocation[0]
        - getResources().getDimensionPixelSize(R.dimen.ai_conversation_x_offset);

    int duration = 500;
    selectedTarget.animate()
        .alpha(0)
        .translationY(translationY)
        .translationX(translationX)
        .setStartDelay(200)
        .setInterpolator(new AccelerateDecelerateInterpolator())
        .setDuration(300)
        .start();

    if (rvOptions.getChildCount() > 1) {
      View disappearView = rvOptions.getChildAt(index == 0 ? 1 : 0);
      disappearView.animate()
          .alpha(0)
          .setInterpolator(new AccelerateDecelerateInterpolator())
          .setDuration(300)
          .start();
    }
    if (mConversation.size() <= 10) {
      llOptions.animate()
          .translationYBy(ScreenUtils.getScreenHeight(getActivity()))
          .alpha(1)
          .setDuration(0)
          .setStartDelay(duration)
          .start();
    } else {
      llOptions.animate()
          .translationYBy(ScreenUtils.getScreenHeight(getActivity()) / 2)
          .alpha(1)
          .setDuration(0)
          .setStartDelay(duration)
          .start();
    }

    //rvOptions.getAdapter().notifyItemRemoved(selected==1?0:1);

  }

  /**
   * 隐藏选项
   */
  private void hideOptions() {
    if (mHideAnimRunning) {
      return;
    }
    mHideAnimRunning = true;
    llOptions.animate().translationY(llOptions.getHeight()).setDuration(350).start();
    new Handler().postDelayed(new Runnable() {
      @Override public void run() {
        mHideAnimRunning = false;
      }
    }, 700);
  }

  /**
   * 显示选项
   */
  private void showOptions() {

    if (mConversation.size() < 1) {
      return;
    }

    AIConversationModel last = mConversation.get(mConversation.size() - 1);

    if (mShowAnimRunning) {
      return;
    }

    if (last.getAnswerPramms() != null && last.getAnswerPramms().size() > 0 && !(null
        != last.getType() && Integer.valueOf(last.getType()) == 1)) {

      mShowAnimRunning = true;

      int offSet = 0;
      if (mConversation.size() <= 2) {
        int[] location = new int[2];
        llOptions.getLocationInWindow(location);
        offSet = ScreenUtils.getScreenHeight(getActivity()) - location[1] - llOptions.getHeight();
        ivGuide.setTranslationY(offSet - offSetY);
      }

      llOptions.animate()
          .translationY(offSet - offSetY)
          .setDuration(350)
          .setInterpolator(new OvershootInterpolator(0.75f))
          .start();
      new Handler().postDelayed(new Runnable() {
        @Override public void run() {
          mShowAnimRunning = false;
        }
      }, 700);
    }
  }

  /**
   * 重置选项
   */
  private void resetOptions(final List<String> list, final String addition, final int type) {
    if (!SPUtils.getConversationGuide()) {
      ivGuide.setVisibility(View.VISIBLE);
    }

    if (type == 1 || null == list) {
      return;
    }

    if (mConversation.size() > 3) {
      ((AppBarLayout) getActivity().findViewById(R.id.appbar)).setExpanded(false);
    }

        TextOptionAdapter textOptionAdapter =
            new TextOptionAdapter(list, addition, type, AIConversationFragment.this);
        boolean vertical = false;
        boolean singleAnswer = list.size() == 1 ? true : false;
        for (int i = 0; i < list.size(); i++) {

          if (mTvOption.getPaint().measureText(list.get(i)) >= (ScreenUtils.getScreenWidth(getActivity()) / 2) * 0.7) {
            vertical = true;
          }
        }

        rvOptions.setAdapter(textOptionAdapter);
        textOptionAdapter.updateWrapContent(false);
        rvOptions.setLayoutManager(
            new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        if (vertical || singleAnswer) {
          textOptionAdapter.updateWrapContent(false);
          rvOptions.setLayoutManager(
              new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        } else {
          textOptionAdapter.updateWrapContent(true);

          rvOptions.setLayoutManager(
              new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        }
  }

  @Override public void onResume() {
    super.onResume();
    if (null != mAdapter && null != mRecyclerView) {
      mAdapter.notifyDataSetChanged();
      mRecyclerView.clearAnimation();
      mRecyclerView.invalidate();

      int effectedRows = deleteHistoryIfNeed();
      if (effectedRows > 0) {
        AIConversationPresenter.delOptionMessages(getActivity());
        mHistory = AIConversationPresenter.getMessages(getActivity(), 3);
        mConversation.clear();
        mConversation.addAll(mHistory);
        mAdapter.notifyDataSetChanged();
        if (mConversation.size() == 0 && !mNetworkBusy) {
          startConversation("04");
          hideOptions();
        }
      } else if (mConversation.size() > 1
          && System.currentTimeMillis() - mConversation.get(mConversation.size() - 1).getTimeStamp()
          > (1000 * 60 * 60)) {
        //最后一条数据若大于1小时重启对话
        startConversation("05");
      }
    }
  }

  /**
   * 删除一天前的缓存如果现在大于4点
   */
  private int deleteHistoryIfNeed() {

    int effectedRows = 0;
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());

    if (calendar.get(Calendar.HOUR_OF_DAY) >= 4) {
      calendar.set(Calendar.HOUR_OF_DAY, 4);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);

      effectedRows =
          AIConversationPresenter.delMessagesBefore(getActivity(), calendar.getTimeInMillis());
    }
    return effectedRows;
  }

  /**
   * 添加一条对话(存在数据库操作)
   */
  private void addModel(AIConversationModel model) {

    if (!TextUtils.isEmpty(model.getAnswer())) {

      if (Integer.valueOf(model.getType()) == 2
          && mConversation.size() > 0
          && null != mConversation.get(mConversation.size() - 1)
          .getType()
          && Integer.valueOf(mConversation.get(mConversation.size() - 1).getType()) == 2) {
      } else {
        model.setTimeStamp(System.currentTimeMillis());
        mConversation.add(model);
        AIConversationPresenter.insertMessage(getActivity(), model);
        mAdapter.notifyItemInserted(mConversation.size() - 1);
        scrollToBottom();
      }
    }

    if (model.getAnswerPramms().size() > 0 && !"1".equals(model.getType())) {

      final AIConversationModel modelOption = new AIConversationModel();
      modelOption.setAnswerPramms(model.getAnswerPramms());
      modelOption.setDataType(AIConversationModel.EnumDataType.OPTION);
      modelOption.setFlash(model.getFlash());
      modelOption.setType(model.getType());

      final AIConversationModel galleryModel = new AIConversationModel();
      galleryModel.setPlaceList(model.getPlaceList());
      galleryModel.setDataType(AIConversationModel.EnumDataType.GALLERY);
      galleryModel.setAnswerPramms(model.getAnswerPramms());
      galleryModel.setTimeStamp(System.currentTimeMillis());

      mRecyclerView.postDelayed(new Runnable() {
        @Override public void run() {
          //boolean galleryAdded = false;
          if (null != galleryModel.getPlaceList() && galleryModel.getPlaceList().size() > 0) {
            mConversation.add(galleryModel);
            AIConversationPresenter.insertMessage(getActivity(), galleryModel);
            mAdapter.notifyItemInserted(mConversation.size() - 1);
            scrollToBottom();
          }
        }
      }, 750);
      scrollToBottom();
      resetOptions(modelOption.getAnswerPramms(), modelOption.getFlash(),
          null != modelOption.getType() ? Integer.valueOf(modelOption.getType()) : 0);
    }
  }

  /**
   * 滚动至底部
   */
  private void scrollToBottom() {

    mRecyclerView.postDelayed(new Runnable() {
      @Override public void run() {
        View v = mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1);

        if (null != v && (v.getTop() + v.getHeight() + mAppbarOffSet) >= mScreenHeight) {
          ((AppBarLayout) getActivity().findViewById(R.id.appbar)).setExpanded(false);
          mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
        }
      }
    }, 50);
    mRecyclerView.postDelayed(new Runnable() {
      @Override public void run() {
        showOptions();
      }
    }, 100);
  }

  /**
   * 获得下一对话
   */
  private void getNext(final String question, final String flash) {
    if (!NetUtil.isNetworkConnected(getActivity())) {
      mNetworkInterrupted = true;
      updateNoNetwork(true);
      scrollToBottom();

      return;
    }
    if (!mXiaoQFab.mLoading) {
      mXiaoQFab.animateLoading();
    }
    mNetworkBusy = true;

    mRecyclerView.postDelayed(new Runnable() {
      @Override public void run() {
        AIConversationPresenter.getNext(getActivity(), question, flash,
            new CommonListener<AIConversationModel>() {
              @Override public void successListener(AIConversationModel response) {
                if (mNetworkInterrupted) {
                  mNetworkInterrupted = false;
                  updateNoNetwork(false);
                }
                addModel(response);

                if (Integer.valueOf(response.getType()) == 1) {
                  getNext(response.getAnswerPramms().get(0), response.getFlash());
                } else {
                  mXiaoQFab.endLoading();
                }
                mNetworkBusy = false;
              }

              @Override public void errorListener(VolleyError error, String exception, String msg) {
                mNetworkInterrupted = true;
                updateNoNetwork(true);
                scrollToBottom();
                mXiaoQFab.endLoading();
                mNetworkBusy = false;
              }
            });
      }
    }, CONVERSATION_REQUEST_DELAY);
  }

  /**
   * 开始对话
   */
  private void startConversation(final String type) {
    mXiaoQFab.animateLoading();

    if (!NetUtil.isNetworkConnected(getActivity())) {
      mNetworkInterrupted = true;
      updateNoNetwork(true);
      scrollToBottom();
      return;
    }

    mNetworkBusy = true;

    AIConversationPresenter.startConversation(getActivity(), type,
        new CommonListener<AIConversationModel>() {
          @Override public void successListener(AIConversationModel response) {

            if (mNetworkInterrupted) {
              mNetworkInterrupted = false;
              updateNoNetwork(false);
            }
            System.out.println("6 "+response.toString()+"||");
            if (!TextUtils.isEmpty(response.getAnswer()) && null!=response.getAnswerPramms()) {
              addModel(response);
            }
            if (Integer.valueOf(response.getType()) == 1) {
              getNext(response.getAnswerPramms().get(0), response.getFlash());
            } else {

              String s = "";
              for (int i = 0; i < response.getAnswerPramms().size(); i++) {
                s+= response.getAnswerPramms().get(i)+",";
              }

              System.out.println("1 "+response.getAnswer()+"||"+s);
              if (mConversation.get(mConversation.size() - 1).getDataType()
                  != AIConversationModel.EnumDataType.OPTION) {
                System.out.println("2 add model ||");
                AIConversationModel modelOption = new AIConversationModel();
                modelOption.setAnswerPramms(response.getAnswerPramms());
                modelOption.setDataType(AIConversationModel.EnumDataType.OPTION);
                modelOption.setFlash(response.getFlash());
                modelOption.setType(response.getType());
                addModel(modelOption);
                mXiaoQFab.endLoading();
              }
            }
            mNetworkBusy = false;
          }

          @Override public void errorListener(VolleyError error, String exception, String msg) {
            mNetworkInterrupted = true;
            updateNoNetwork(true);
            scrollToBottom();
            mXiaoQFab.endLoading();
            mNetworkBusy = false;
          }
        });
  }

  @Override public void onStart() {
    super.onStart();
    EventBus.getDefault().register(this);
  }

  @Override public void onStop() {
    EventBus.getDefault().unregister(this);
    super.onStop();
  }

  @Subscribe public void onMessageEvent(QuchuEventModel event) {
    if (null == event) {
      return;
    }
    switch (event.getFlag()) {
      case EventFlags.EVENT_DEVICE_NETWORK_AVAILABLE:
        if (mNetworkInterrupted) {
          updateNoNetwork(false);
          startConversation("03");
        }
        break;
      case EventFlags.EVENT_USER_LOGIN_SUCCESS:
      case EventFlags.EVENT_USER_LOGOUT:
        if (null != mAdapter) {
          mAdapter.notifyDataSetChanged();
        }
        break;
    }
  }

  boolean mAnswering = false;

  @Override
  public void onAnswer(final String answer, final String additionalShit, final int index) {

    if (mAnswering){
      return;
    }
    mAnswering = true;
    ivGuide.setVisibility(View.GONE);

    if (!NetUtil.isNetworkConnected(getActivity())) {
      makeToast(R.string.network_error);
      return;
    }
    if (mNetworkBusy) {
      return;
    }
    int position = mConversation.size() - 1;
    if (null != mConversation.get(position).getAnswerPramms()) {
      mConversation.get(position).getAnswerPramms().clear();
    }
    AIConversationPresenter.delOptionMessages(getActivity());
    mAdapter.notifyItemChanged(position);

    mRecyclerView.postDelayed(new Runnable() {
      @Override public void run() {
        AIConversationModel answerModel = new AIConversationModel();
        answerModel.setDataType(AIConversationModel.EnumDataType.ANSWER);
        answerModel.setAnswer(answer);
        AIConversationPresenter.insertMessage(getActivity(), answerModel);
        mConversation.add(answerModel);
        mAdapter.notifyItemInserted(mConversation.size() - 1);
        hideAndUpdate(index);
        getNext(answer, additionalShit);
        mAnswering = false;
      }
    }, 300);
  }

  @Override public void onRetry() {
    startConversation("03");
    hideOptions();
  }

  @Override public void onSearch() {
    startActivity(new Intent(getActivity(), SearchActivity.class));
  }

  /**
   * 更新无网络状态
   */
  private void updateNoNetwork(boolean noNetWork) {

    if (noNetWork) {
      if (mConversation.size() < 1
          || mConversation.get(mConversation.size() - 1).getDataType()
          != AIConversationModel.EnumDataType.QUESTION) {
        AIConversationModel noNetworkModel = new AIConversationModel();
        noNetworkModel.setAnswer("你好，Alice暂时无法和总部取得 联系！");
        noNetworkModel.setDataType(AIConversationModel.EnumDataType.QUESTION);
        noNetworkModel.setType("2");
        List<String> retryAction = new ArrayList<>();
        retryAction.add("手动刷新");
        retryAction.add("手动搜索");
        noNetworkModel.setAnswerPramms(retryAction);
        addModel(noNetworkModel);
      }
    } else {
      if (mConversation.size() > 0
          && null != mConversation.get(mConversation.size() - 1).getType()
          && !noNetWork
          && mConversation.get(mConversation.size() - 1).getType().equals("2")) {
        mConversation.remove(mConversation.size() - 1);
      }
      mAdapter.notifyDataSetChanged();
    }

    if (noNetWork) {
      List<String> retryAction = new ArrayList<>();
      retryAction.add("手动刷新");
      retryAction.add("手动搜索");
      resetOptions(retryAction, null, 2);
    } else {
      hideOptions();
    }
  }

  ///**
  // * 重置选项偏移量
  // */
  //public void resetOffset() {
  //
  //  int[] location = new int[2];
  //  llOptions.getLocationOnScreen(location);
  //  if (location[1]<= ScreenUtils.getStatusHeight(getActivity())-llOptions.getHeight()) {
  //    int offSet = ((ScreenUtils.getScreenHeight(getActivity())-location[1]-ScreenUtils.getScreenHeight(getActivity())-llOptions.getHeight()));
  //    ivGuide.setTranslationY(-offSet);
  //    quickReturn.setTranslationY(-offSet);
  //    llOptions.setTranslationY(-offSet);
  //  }
  //}

  /**
   * 重置选项偏移量
   */
  public void resetOffset(float scrollRange) {

    quickReturn.setTranslationY(-scrollRange);
    if (mConversation.size() <= 2) {
      int[] location = new int[2];
      llOptions.getLocationOnScreen(location);
      ivGuide.setTranslationY(-scrollRange);
      llOptions.setTranslationY(-scrollRange);
      offSetY = scrollRange;
    } else {
      offSetY = 0;
    }
  }

  private float offset;

  public void resetOffsetPassive(float scrollRangePassive) {
    offset = scrollRangePassive;
  }

  private void playTheFuckingSound(int index) {
    if (!SPUtils.isEnableSound()){
      return;
    }
    final MediaPlayer mPlayer =
        MediaPlayer.create(getActivity(), index == 0 ? R.raw.sound_0 : R.raw.sound_1);
    mPlayer.setLooping(false);
    mPlayer.setVolume(20, 20);
    mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
      @Override public void onPrepared(MediaPlayer mp) {
        mPlayer.seekTo(0);
        mPlayer.start();
      }
    });
  }

  @Override public void onClick() {
    mXiaoQFab.setPromote(true);
  }
}
