package co.quchu.quchu.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import co.quchu.quchu.R;
import co.quchu.quchu.view.activity.XiaoQActivity;
import io.rong.imkit.fragment.ConversationListFragment;

/**
 * Created by mwb on 16/9/23.
 */
public class ConversationListFragmentEx extends ConversationListFragment {

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    View qView = view.findViewById(R.id.qLayout);
    qView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(getActivity(), XiaoQActivity.class));
      }
    });
    super.onViewCreated(view, savedInstanceState);
  }
}
