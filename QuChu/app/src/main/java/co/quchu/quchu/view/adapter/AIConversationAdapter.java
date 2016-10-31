package co.quchu.quchu.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.AIConversationAnswerModel;
import co.quchu.quchu.model.AIConversationQuestionModel;
import co.quchu.quchu.model.CommentImageModel;
import co.quchu.quchu.model.CommentModel;
import co.quchu.quchu.model.ImageModel;
import co.quchu.quchu.model.QAModel;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.activity.PhotoViewActivity;
import co.quchu.quchu.view.activity.WebViewActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nico on 16/8/26.
 */
public class AIConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  List<QAModel> mDataSet;

  public static final int TYPE_QUESTION = 0x001;
  public static final int TYPE_ANSWER = 0x002;

  public AIConversationAdapter(List<QAModel> data) {
    this.mDataSet = data;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    switch (viewType) {
      case TYPE_QUESTION:
        return new QuestionViewHolder(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_ai_conversation_question, parent, false));
      default:
        return new AnswerViewHolder(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_ai_conversation_answer, parent, false));
    }
  }

  @Override public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

    switch (getItemViewType(position)){
      case TYPE_QUESTION:
        ((QuestionViewHolder)holder).tvQuestion.setText(String.valueOf("Test Question Shit Number "+position) + (position %2==0?String.valueOf("Test Question Shit Number "+position):"") + (position %5==0?String.valueOf("Test Question Shit Number "+position):""));
        break;
      case TYPE_ANSWER:
        ((AnswerViewHolder)holder).tvAnswer.setText(String.valueOf("Test Answer Shit Number "+position)+(position %5==0?String.valueOf("Test Question Shit Number "+position):""));
        break;
    }

  }

  @Override public int getItemCount() {
    return null != mDataSet ? mDataSet.size() : 0;
  }

  @Override public int getItemViewType(int position) {
    if (mDataSet.get(position) instanceof AIConversationQuestionModel) {
      return TYPE_QUESTION;
    } else {
      return TYPE_ANSWER;
    }
  }

  public static class QuestionViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.tvQuestion) TextView tvQuestion;

    QuestionViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  public static class AnswerViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.tvAnswer) TextView tvAnswer;

    AnswerViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }
}
