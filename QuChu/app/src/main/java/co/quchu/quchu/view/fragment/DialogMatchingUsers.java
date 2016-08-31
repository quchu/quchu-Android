package co.quchu.quchu.view.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.widget.MatchUserItemView;

/**
 * 基因分析弹窗
 *
 * Created by Nico on 16/8/30.
 */
public class DialogMatchingUsers extends DialogFragment {

  @Bind(R.id.tv_title) TextView mTvTitle;
  @Bind(R.id.match_item_view_1) MatchUserItemView mItemView1;
  @Bind(R.id.match_item_view_2) MatchUserItemView mItemView2;
  @Bind(R.id.match_item_view_3) MatchUserItemView mItemView3;
  @Bind(R.id.match_item_view_4) MatchUserItemView mItemView4;

  private final int TITLE_CHANGE_TASK = 1;
  private final int CONTENT_CHANGE_TASK = 2;

  private String match_gens_title_1 = "趣基因分析中.";
  private String match_gens_title_2 = "趣基因分析中..";
  private String match_gens_title_3 = "趣基因分析中...";

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.dialog_match_genes, container, false);
    ButterKnife.bind(this, view);
    getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    setCancelable(false);

    startTitleTimer(1);
    startItemTimer(1, 500);

    return view;
  }

  /**
   * 标题“...”刷新
   */
  private void startTitleTimer(int i) {
    Message msg = new Message();
    msg.what = TITLE_CHANGE_TASK;
    msg.arg1 = i;
    handler.sendMessageDelayed(msg, 500);
  }

  /**
   * item定时任务
   */
  private void startItemTimer(int i, long delayMillis) {
    handler.removeMessages(CONTENT_CHANGE_TASK);

    Message msg = new Message();
    msg.what = CONTENT_CHANGE_TASK;
    msg.arg1 = i;
    handler.sendMessageDelayed(msg, delayMillis);
  }

  private Handler handler = new Handler() {
    @Override public void handleMessage(Message msg) {
      if (msg.what == TITLE_CHANGE_TASK) {
        //刷新标题
        refreshTitle(msg.arg1);
      } else if (msg.what == CONTENT_CHANGE_TASK) {
        //刷新item
        refreshItem(msg.arg1);
      }
    }
  };

  /**
   * 刷新标题
   */
  private void refreshTitle(int arg1) {
    switch (arg1) {
      case 1:
        mTvTitle.setText(match_gens_title_1);
        startTitleTimer(2);
        break;

      case 2:
        mTvTitle.setText(match_gens_title_2);
        startTitleTimer(3);
        break;

      case 3:
        mTvTitle.setText(match_gens_title_3);
        startTitleTimer(1);
        break;
    }
  }

  /**
   * 刷新item
   */
  private void refreshItem(int arg1) {
    switch (arg1) {
      case 1:
        mItemView1.setProgress(false);
        mItemView1.setTextColor(getActivity().getResources().getColor(R.color.standard_color_yellow));
        startItemTimer(2, 400);
        break;

      case 2:
        mItemView2.setProgress(false);
        mItemView2.setTextColor(getActivity().getResources().getColor(R.color.standard_color_yellow));
        startItemTimer(3, 600);
        break;

      case 3:
        mItemView3.setProgress(false);
        mItemView3.setTextColor(getActivity().getResources().getColor(R.color.standard_color_yellow));
        startItemTimer(4, 300);
        break;

      case 4:
        mItemView4.setProgress(false);
        mItemView4.setTextColor(getActivity().getResources().getColor(R.color.standard_color_yellow));
        startItemTimer(5, 200);
        break;

      case 5:
        mTvTitle.setText(match_gens_title_3);
        handler.removeMessages(TITLE_CHANGE_TASK);
        handler.removeMessages(CONTENT_CHANGE_TASK);

        if (mListener != null) {
          mListener.onMatchfinish();
        }
        break;
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();

    ButterKnife.unbind(this);
  }

  private OnDialogMatchListener mListener;

  public void setOnDialogMatchListener(OnDialogMatchListener listener) {
    mListener = listener;
  }

  public interface OnDialogMatchListener {
    void onMatchfinish();
  }
}
