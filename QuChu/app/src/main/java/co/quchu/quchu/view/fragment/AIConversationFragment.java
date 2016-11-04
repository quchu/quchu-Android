package co.quchu.quchu.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
import co.quchu.quchu.view.adapter.AIConversationAdapter;
import com.android.volley.VolleyError;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Nico on 16/11/3.
 */

public class AIConversationFragment extends BaseFragment {

  private AIConversationAdapter mAdapter;
  private List<AIConversationModel> mConversation = new ArrayList<>();
  public static final int CONVERSATION_REQUEST_DELAY = 500;
  public static final int CONVERSATION_ANSWER_DELAY = 300;

  private int mScreenHeight;
  private int mAppbarOffSet;
  //是否断开网络
  private boolean mNetworkInterrupted = false;
  @Bind(R.id.rv) RecyclerView mRecyclerView;

  @Override protected String getPageNameCN() {
    return null;
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

    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    mAdapter = new AIConversationAdapter(getActivity(), mConversation,
        new AIConversationAdapter.OnAnswerListener() {
          @Override public void onAnswer(final String answer, final String additionalShit) {

            if (!NetUtil.isNetworkConnected(getActivity())) {
              makeToast(R.string.network_error);
              return;
            }

            int position = mConversation.size() - 1;
            mConversation.get(position).getAnswerPramms().clear();
            mAdapter.notifyItemChanged(position);

            mRecyclerView.postDelayed(new Runnable() {
              @Override public void run() {
                AIConversationModel answerModel = new AIConversationModel();
                answerModel.setDataType(AIConversationModel.EnumDataType.ANSWER);
                answerModel.setAnswer(answer);
                mConversation.add(answerModel);
                mAdapter.notifyItemInserted(mConversation.size() - 1);
                scrollToBottom();
                getNext(answer, additionalShit);
              }
            }, CONVERSATION_REQUEST_DELAY);
          }
        });
    mRecyclerView.setAdapter(mAdapter);
    startConversation(true);

    return v;
  }

  private void addModel(AIConversationModel model) {

    if (Integer.valueOf(model.getType()) == 0 && TextUtils.isEmpty(model.getAnswer())) {

    } else {
      mConversation.add(model);
      mAdapter.notifyItemInserted(mConversation.size() - 1);
      scrollToBottom();
    }

    if (Integer.valueOf(model.getType()) == 0 && model.getAnswerPramms().size() > 0) {
      final AIConversationModel modelOption = new AIConversationModel();
      modelOption.setAnswerPramms(model.getAnswerPramms());
      modelOption.setDataType(AIConversationModel.EnumDataType.OPTION);
      modelOption.setPlaceList(model.getPlaceList());
      modelOption.setFlash(model.getFlash());
      mRecyclerView.postDelayed(new Runnable() {
        @Override public void run() {
          mConversation.add(modelOption);
          mAdapter.notifyItemInserted(mConversation.size() - 1);
          scrollToBottom();
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
    }, 100);
  }

  private void getNext(final String question, final String flash) {

    if (!NetUtil.isNetworkConnected(getActivity())) {
      mNetworkInterrupted = true;
      mAdapter.updateNoNetwork(true);
      scrollToBottom();

      return;
    }

    mRecyclerView.postDelayed(new Runnable() {
      @Override public void run() {
        AIConversationPresenter.getNext(getActivity(), question, flash,
            new CommonListener<AIConversationModel>() {
              @Override public void successListener(AIConversationModel response) {
                if (mNetworkInterrupted) {
                  mNetworkInterrupted = false;
                }
                addModel(response);

                if (Integer.valueOf(response.getType()) == 1) {
                  getNext(response.getAnswerPramms().get(0), response.getFlash());
                }
              }

              @Override public void errorListener(VolleyError error, String exception, String msg) {
                mNetworkInterrupted = true;
                mAdapter.updateNoNetwork(true);
                scrollToBottom();
              }
            });
      }
    }, CONVERSATION_REQUEST_DELAY);
  }

  private void startConversation(final boolean starter) {

    if (!NetUtil.isNetworkConnected(getActivity())) {
      mNetworkInterrupted = true;
      mAdapter.updateNoNetwork(true);
      scrollToBottom();
      return;
    }

    AIConversationPresenter.startConversation(getActivity(), starter,
        new CommonListener<AIConversationModel>() {
          @Override public void successListener(AIConversationModel response) {
            if (mNetworkInterrupted) {
              mNetworkInterrupted = false;
            }
            if (!TextUtils.isEmpty(response.getAnswer())) {
              addModel(response);
            }
            if (starter) {
              getNext(response.getAnswerPramms().get(0), response.getFlash());
            } else {
              //断开网络情况
              //mConversation.remove(mConversation.size()-1);
              //mAdapter.notifyItemRemoved(mConversation.size()-1);
              getNext(response.getAnswerPramms().get(0), response.getFlash());
            }
          }

          @Override public void errorListener(VolleyError error, String exception, String msg) {
            mNetworkInterrupted = true;
            mAdapter.updateNoNetwork(true);
            scrollToBottom();
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
}
