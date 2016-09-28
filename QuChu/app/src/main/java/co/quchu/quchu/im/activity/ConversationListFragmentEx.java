package co.quchu.quchu.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;

import co.quchu.quchu.R;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.XiaoQPresenter;
import co.quchu.quchu.view.activity.XiaoQActivity;
import io.rong.imkit.fragment.ConversationListFragment;

/**
 * Created by mwb on 16/9/23.
 */
public class ConversationListFragmentEx extends ConversationListFragment {

  private TextView mUnreadMsgCount;

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {

    initQViews(view);

    super.onViewCreated(view, savedInstanceState);
  }

  private void initQViews(View view) {
    View qView = view.findViewById(R.id.qLayout);
    mUnreadMsgCount = (TextView) view.findViewById(R.id.qUnreadMsgCountTv);
    qView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(getActivity(), XiaoQActivity.class));
      }
    });
  }

  @Override
  public void onResume() {
    super.onResume();

    new XiaoQPresenter(getActivity()).getUnreadMsgCount(new CommonListener<Integer>() {
      @Override
      public void successListener(Integer response) {
        if (mUnreadMsgCount != null && response > 0) {
          mUnreadMsgCount.setVisibility(View.VISIBLE);
          mUnreadMsgCount.setText("" + response);
        }
      }

      @Override
      public void errorListener(VolleyError error, String exception, String msg) {

      }
    });
  }
}
