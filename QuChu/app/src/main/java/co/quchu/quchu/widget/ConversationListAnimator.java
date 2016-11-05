package co.quchu.quchu.widget;

import android.animation.Animator;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.animation.AccelerateDecelerateInterpolator;
import co.quchu.quchu.view.adapter.AIConversationAdapter;

/**
 * Created by Nico on 16/11/2.
 */

public class ConversationListAnimator extends DefaultItemAnimator {

  @Override public boolean animateRemove(RecyclerView.ViewHolder holder) {
    return super.animateRemove(holder);
  }

  @Override
  public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX,
      int toY) {
    return super.animateMove(holder, fromX, fromY, toX, toY);
  }

  @Override
  public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder,
      int fromX, int fromY, int toX, int toY) {
    return super.animateChange(oldHolder, newHolder, fromX, fromY, toX, toY);
  }

  @Override public boolean animateAdd(final RecyclerView.ViewHolder holder) {
    int holderWidth = holder.itemView.getWidth();
    if (holder instanceof AIConversationAdapter.QuestionViewHolder) {
      holder.itemView.setTranslationX(-holderWidth);
      holder.itemView.animate()
          .translationX(0)
          .setInterpolator(new AccelerateDecelerateInterpolator())
          .setDuration(300)
          .setListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) {
              dispatchAddStarting(holder);
            }

            @Override public void onAnimationEnd(Animator animation) {
              dispatchAddFinished(holder);
            }

            @Override public void onAnimationCancel(Animator animation) {}

            @Override public void onAnimationRepeat(Animator animation) {}
          })
          .start();
    } else if (holder instanceof AIConversationAdapter.AnswerViewHolder || holder instanceof AIConversationAdapter.OptionViewHolder) {
      holder.itemView.setTranslationX(holderWidth);
      holder.itemView.animate()
          .translationX(0)
          .setInterpolator(new AccelerateDecelerateInterpolator())
          .setDuration(300)
          .setListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) {
              dispatchAddStarting(holder);
            }

            @Override public void onAnimationEnd(Animator animation) {
              dispatchAddFinished(holder);
            }

            @Override public void onAnimationCancel(Animator animation) {}

            @Override public void onAnimationRepeat(Animator animation) {}
          })
          .start();
    }
    return true;
  }
}
