package co.quchu.quchu.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;
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
import co.quchu.quchu.utils.ScreenUtils;
import co.quchu.quchu.view.activity.SearchActivityNew;
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
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Nico on 16/11/3.
 */

public class AIConversationFragment extends BaseFragment
    implements AIConversationAdapter.OnInteractiveClick {

  private AIConversationAdapter mAdapter;
  private List<AIConversationModel> mConversation = new ArrayList<>();
  public static final int CONVERSATION_REQUEST_DELAY = 500;
  public static final int CONVERSATION_ANSWER_DELAY = 300;

  private int mScreenHeight;
  private int mAppbarOffSet;
  private XiaoQFab mXiaoQFab;
  private boolean mNetworkBusy = false;
  private List<AIConversationModel> history;
  private boolean mHideAnimRunning = false;
  private boolean mShowAnimRunning = false;

  //是否断开网络
  private boolean mNetworkInterrupted = false;


  @Bind(R.id.rv) RecyclerView mRecyclerView;
  @Bind(R.id.rvOptions) RecyclerView rvOptions;
  @Bind(R.id.tvOption) TextView mTvOption;

  @Override protected String getPageNameCN() {
    return null;
  }



  private void hideOptions(){
    rvOptions.animate().translationY(rvOptions.getHeight()).setDuration(200).start();
    new Handler().postDelayed(new Runnable() {
      @Override public void run() {
        mHideAnimRunning = false;
      }
    },200);
  }

  private void showOptions(){
    rvOptions.animate().translationY(0).setDuration(200).start();
    new Handler().postDelayed(new Runnable() {
      @Override public void run() {
        mShowAnimRunning = false;
      }
    },200);
  }

  private void resetOptions(List<String>list,String addition,int type){
    TextOptionAdapter textOptionAdapter = new TextOptionAdapter(list, addition, type, this);
    boolean vertical = false;
    boolean singleAnswer = list.size()==1?true:false;
    for (int i = 0; i < list.size(); i++) {

      if (mTvOption.getPaint().measureText(list.get(i))>=(ScreenUtils.getScreenWidth(getActivity())/2)*0.8){
        makeToast(String.valueOf(mTvOption.getWidth()));
        vertical = true;
      }
    }

    rvOptions.setAdapter(textOptionAdapter);

    if (vertical||singleAnswer){
      textOptionAdapter.updateGravity(vertical);
      rvOptions.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
    }else{
      textOptionAdapter.updateGravity(vertical);
      rvOptions.setLayoutManager(new GridLayoutManager(getActivity(),2,LinearLayoutManager.VERTICAL,false));
    }
    showOptions();
  }


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
        int visibleItemCount = mRecyclerView.getLayoutManager().getChildCount();
        int totalItemCount = mRecyclerView.getLayoutManager().getItemCount();
        int pastVisibleItems = ((LinearLayoutManager)mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();

          if (pastVisibleItems + visibleItemCount >= totalItemCount-1) {
            //End of list
            if (!mShowAnimRunning){
              mShowAnimRunning = true;
              showOptions();
            }
          }else{
            if (!mHideAnimRunning){
              mHideAnimRunning = true;
              hideOptions();
            }
          }
        }

    });


    mRecyclerView.setLayoutManager(new ScrollToLinearLayoutManager(getActivity()));
    mRecyclerView.addItemDecoration(new DynamicItemDecoration());
    mRecyclerView.setItemAnimator(new ConversationListAnimator());
    mAdapter = new AIConversationAdapter(getActivity(), mConversation,
        this);
    mRecyclerView.setAdapter(mAdapter);

    mXiaoQFab = (XiaoQFab) getActivity().findViewById(R.id.fab);

    mXiaoQFab.postDelayed(new Runnable() {
      @Override public void run() {
        mXiaoQFab.animateInitial();
      }
    },200);


    deleteHistoryIfNeed();
    AIConversationPresenter.delOptionMessages(getActivity());
    history = AIConversationPresenter.getMessages(getActivity());
    mConversation.addAll(history);
    mAdapter.notifyDataSetChanged();

    if (mConversation.size()>0){
      mRecyclerView.scrollToPosition(mConversation.size()-1);
    }

    mXiaoQFab.postDelayed(new Runnable() {
      @Override public void run() {

        if (history.size()>0 ){
          startConversation(false);
        }else{
          startConversation(true);
        }

      }
    },1500);
    return v;
  }


  private void deleteHistoryIfNeed(){

    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());

    if (calendar.get(Calendar.HOUR_OF_DAY)>=4) {
      //TODO delete < current date
      calendar.set(Calendar.HOUR_OF_DAY,0);
      calendar.set(Calendar.MINUTE,0);
      calendar.set(Calendar.SECOND,0);

      AIConversationPresenter.delMessagesBefore(getActivity(),calendar.getTimeInMillis());
    }

  }

  private void addModel(AIConversationModel model) {

    if (Integer.valueOf(model.getType()) == 0 && TextUtils.isEmpty(model.getAnswer())) {

    } else {
      mConversation.add(model);
      AIConversationPresenter.insertMessage(getActivity(),model);

      mAdapter.notifyItemInserted(mConversation.size() - 1);
      scrollToBottom();
    }

    if ((Integer.valueOf(model.getType()) == 0 && model.getAnswerPramms().size() > 0)||Integer.valueOf(model.getType())==2) {


      final AIConversationModel modelOption = new AIConversationModel();
      modelOption.setAnswerPramms(model.getAnswerPramms());
      modelOption.setDataType(AIConversationModel.EnumDataType.OPTION);
      modelOption.setFlash(model.getFlash());

      resetOptions(modelOption.getAnswerPramms(),modelOption.getFlash(),null!=modelOption.getType()? Integer.valueOf(modelOption.getType()):0);

      final AIConversationModel galleryModel = new AIConversationModel();
      galleryModel.setPlaceList(model.getPlaceList());
      galleryModel.setDataType(AIConversationModel.EnumDataType.GALLERY);
      mRecyclerView.postDelayed(new Runnable() {
        @Override public void run() {
          boolean galleryAdded = false;
          if (null!=galleryModel.getPlaceList() && galleryModel.getPlaceList().size()>0){
            mConversation.add(galleryModel);
            AIConversationPresenter.insertMessage(getActivity(),galleryModel);
            mAdapter.notifyItemInserted(mConversation.size() - 1);
            scrollToBottom();
            galleryAdded = true;
          }

          int delay = galleryAdded?CONVERSATION_ANSWER_DELAY:0;
          new Handler().postDelayed(new Runnable() {
            @Override public void run() {
              mConversation.add(modelOption);
              AIConversationPresenter.insertMessage(getActivity(),modelOption);
              mAdapter.notifyItemInserted(mConversation.size() - 1);
            }
          },delay);
        }
      }, CONVERSATION_ANSWER_DELAY);
      scrollToBottom();
    }
  }

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
  }

  private void getNext(final String question, final String flash) {

    if (!NetUtil.isNetworkConnected(getActivity())) {
      mNetworkInterrupted = true;
      mAdapter.updateNoNetwork(true);
      scrollToBottom();

      return;
    }
    if (!mXiaoQFab.mLoading){
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
                  mAdapter.updateNoNetwork(false);
                }
                addModel(response);

                if (Integer.valueOf(response.getType()) == 1) {
                  getNext(response.getAnswerPramms().get(0), response.getFlash());
                }else{
                  mXiaoQFab.endLoading();
                }
                mNetworkBusy = false;
              }

              @Override public void errorListener(VolleyError error, String exception, String msg) {
                mNetworkInterrupted = true;
                mAdapter.updateNoNetwork(true);
                scrollToBottom();
                mXiaoQFab.endLoading();
                mNetworkBusy = false;
              }
            });
      }
    }, CONVERSATION_REQUEST_DELAY);
  }

  private void startConversation(final boolean starter) {
    mXiaoQFab.animateLoading();

    if (!NetUtil.isNetworkConnected(getActivity())) {
      mNetworkInterrupted = true;
      mAdapter.updateNoNetwork(true);
      scrollToBottom();
      return;
    }

    mNetworkBusy = true;

    AIConversationPresenter.startConversation(getActivity(), starter,
        new CommonListener<AIConversationModel>() {
          @Override public void successListener(AIConversationModel response) {

            if (mNetworkInterrupted) {
              mNetworkInterrupted = false;
              mAdapter.updateNoNetwork(false);
            }
            if (!TextUtils.isEmpty(response.getAnswer())) {
              addModel(response);
            }
            if (starter) {
              getNext(response.getAnswerPramms().get(0), response.getFlash());
            } else {

              if (mConversation.get(mConversation.size()-1).getDataType()!= AIConversationModel.EnumDataType.OPTION){
                AIConversationModel modelOption = new AIConversationModel();
                modelOption.setAnswerPramms(response.getAnswerPramms());
                modelOption.setDataType(AIConversationModel.EnumDataType.OPTION);
                modelOption.setFlash(response.getFlash());
                modelOption.setType("0");
                addModel(modelOption);
                mXiaoQFab.endLoading();
              }

            }
            mNetworkBusy = false;
          }

          @Override public void errorListener(VolleyError error, String exception, String msg) {
            mNetworkInterrupted = true;
            mAdapter.updateNoNetwork(true);
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
          mAdapter.updateNoNetwork(false);
          startConversation(false);
        }
        break;
    }
  }

  @Override public void onAnswer(final String answer, final String additionalShit) {


    if (!NetUtil.isNetworkConnected(getActivity())) {
      makeToast(R.string.network_error);
      return;
    }

    if (mNetworkBusy){
      return;
    }

    hideOptions();

    int position = mConversation.size() - 1;
    mConversation.get(position).getAnswerPramms().clear();
    AIConversationPresenter.delOptionMessages(getActivity());
    mAdapter.notifyItemChanged(position);

    mRecyclerView.postDelayed(new Runnable() {
      @Override public void run() {
        AIConversationModel answerModel = new AIConversationModel();
        answerModel.setDataType(AIConversationModel.EnumDataType.ANSWER);
        answerModel.setAnswer(answer);
        AIConversationPresenter.insertMessage(getActivity(),answerModel);
        mConversation.add(answerModel);
        mAdapter.notifyItemInserted(mConversation.size() - 1);
        //scrollToBottom();
        getNext(answer, additionalShit);
      }
    }, CONVERSATION_REQUEST_DELAY);
  }

  @Override public void onRetry() {
    startConversation(false);
    hideOptions();
  }

  @Override public void onSearch() {
    startActivity(new Intent(getActivity(), SearchActivityNew.class));
    hideOptions();
  }



}
