package co.quchu.quchu.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.HangoutUserModel;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.HangoutPresenter;
import co.quchu.quchu.view.adapter.InviteHangoutUsersAdapter;
import co.quchu.quchu.widget.ErrorView;
import com.android.volley.VolleyError;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nico on 16/8/29.
 */
public class InviteHangoutUsersActivity extends BaseActivity {
  @Bind(R.id.rv) RecyclerView rv;
  @Bind(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
  @Bind(R.id.errorView) ErrorView errorView;
  InviteHangoutUsersAdapter mAdapter;
  List<HangoutUserModel> mUsers = new ArrayList<>();
  boolean mLoading = false;

  public static final String REQUEST_INVITE_USER_PID = "REQUEST_INVITE_USER_PID";
  private int mPid;

  @Override protected int activitySetup() {
    return TRANSITION_TYPE_LEFT;
  }

  @Override protected String getPageNameCN() {
    return getString(R.string.pname_invite_user);
  }

  public static void enterActivity(Activity from){
    Intent intent = new Intent(from,InviteHangoutUsersActivity.class);
    //intent.putExtra(REQUEST_INVITE_USER_PID,pid);
    from.startActivity(intent);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_simple_recyclerview);
    ButterKnife.bind(this);
    mPid = getIntent().getIntExtra(REQUEST_INVITE_USER_PID,-1);
    getEnhancedToolbar().getRightTv().setText(R.string.change_hangout_users_list);
    getEnhancedToolbar().getRightTv().setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        getUsers();
      }
    });

    swipeRefreshLayout.setEnabled(false);
    mAdapter = new InviteHangoutUsersAdapter(mUsers);
    rv.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
    rv.setAdapter(mAdapter);
    getUsers();
  }

  private void getUsers(){
    if (mLoading){
     return;
    }
    mLoading = true;
    DialogUtil.showProgess(this,R.string.loading_dialog_text);

    HangoutPresenter.getHangoutUsers(getApplicationContext(), new CommonListener<List<HangoutUserModel>>() {
      @Override public void successListener(List<HangoutUserModel> response) {
        DialogUtil.dismissProgessDirectly();
        mUsers.clear();
        mUsers.addAll(response);
        mAdapter.notifyDataSetChanged();
        mLoading = false;
      }

      @Override public void errorListener(VolleyError error, String exception, String msg) {
        DialogUtil.dismissProgessDirectly();
        mLoading = false;
      }
    });
  }

}
