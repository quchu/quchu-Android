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
import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.model.AIConversationModel;
import co.quchu.quchu.presenter.AIConversationPresenter;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.utils.ScreenUtils;
import co.quchu.quchu.view.activity.RecommendActivity;
import co.quchu.quchu.view.adapter.AIConversationAdapter;
import com.android.volley.VolleyError;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nico on 16/11/3.
 */

public class AIConversationFragment extends BaseFragment{


  private AIConversationAdapter mAdapter;
  private List<AIConversationModel> mConversation = new ArrayList<>();
  public static final int CONVERSATION_REQUEST_DELAY = 500;
  public static final int CONVERSATION_ANSWER_DELAY = 300;

  private int mScreenHeight;
  private int mAppbarOffSet;
  @Bind(R.id.rv) RecyclerView rv;


  @Override protected String getPageNameCN() {
    return null;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View v = inflater.inflate(R.layout.fragment_ai_conversation,container,false);

    ButterKnife.bind(this, v);

    mScreenHeight = ScreenUtils.getScreenHeight(getActivity());
    mAppbarOffSet = getActivity().findViewById(R.id.appbar).getHeight()+ScreenUtils.getStatusHeight(getActivity());

    rv.setLayoutManager(new LinearLayoutManager(getActivity()));
    rv.setItemAnimator(new DefaultItemAnimator());
    mAdapter = new AIConversationAdapter(getActivity(), mConversation, new AIConversationAdapter.OnAnswerListener() {
      @Override public void onAnswer(final String answer, final String additionalShit) {
        int position = mConversation.size()-1;
        mConversation.get(position).getAnswerPramms().clear();
        mAdapter.notifyItemChanged(position);


        rv.postDelayed(new Runnable() {
          @Override public void run() {
            AIConversationModel answerModel = new AIConversationModel();
            answerModel.setDataType(AIConversationModel.EnumDataType.ANSWER);
            answerModel.setAnswer(answer);
            mConversation.add(answerModel);
            mAdapter.notifyItemInserted(mConversation.size()-1);
            scrollToBottom();
            getNext(answer,additionalShit);
          }
        },CONVERSATION_REQUEST_DELAY);
      }
    });
    rv.setAdapter(mAdapter);
    startConversation(true);

    return v;
  }


  private void addModel(AIConversationModel model){

    if (Integer.valueOf(model.getType())==0 && TextUtils.isEmpty(model.getAnswer())){

    }else{
      mConversation.add(model);
      mAdapter.notifyItemInserted(mConversation.size()-1);
      scrollToBottom();

    }


    if (Integer.valueOf(model.getType())==0 && model.getAnswerPramms().size()>0){
      final AIConversationModel modelOption = new AIConversationModel();
      modelOption.setAnswerPramms(model.getAnswerPramms());
      modelOption.setDataType(AIConversationModel.EnumDataType.OPTION);
      modelOption.setPlaceList(model.getPlaceList());
      modelOption.setFlash(model.getFlash());
      rv.postDelayed(new Runnable() {
        @Override public void run() {
          mConversation.add(modelOption);
          mAdapter.notifyItemInserted(mConversation.size()-1);
          scrollToBottom();
        }
      },CONVERSATION_ANSWER_DELAY);
      scrollToBottom();
    }
  }

  private void getNext(final String question, final String flash) {
    rv.postDelayed(new Runnable() {
      @Override public void run() {
        AIConversationPresenter.getNext(getActivity(), question, flash, new CommonListener<AIConversationModel>() {
          @Override
          public void successListener(AIConversationModel response) {
            addModel(response);

            if (Integer.valueOf(response.getType())==1){
              getNext(response.getAnswerPramms().get(0),response.getFlash());
            }
          }

          @Override
          public void errorListener(VolleyError error, String exception, String msg) {}
        });
      }
    },CONVERSATION_REQUEST_DELAY);

  }


  private void scrollToBottom(){

    rv.postDelayed(new Runnable() {
      @Override public void run() {
        View v = rv.getChildAt(rv.getChildCount()-1);

        if (null!=v &&(v.getTop()+v.getHeight()+mAppbarOffSet)>=mScreenHeight){
          ((AppBarLayout)getActivity().findViewById(R.id.appbar)).setExpanded(false);
          rv.smoothScrollToPosition(mAdapter.getItemCount());
        }
      }
    },50);


  }

  private void startConversation(final boolean starter) {
    AIConversationPresenter.startConversation(getActivity(), starter, new CommonListener<AIConversationModel>() {
      @Override
      public void successListener(AIConversationModel response) {
        addModel(response);
        if (starter){
          getNext(response.getAnswerPramms().get(0), response.getFlash());
        }
      }

      @Override
      public void errorListener(VolleyError error, String exception, String msg) {}
    });
  }

}
