package co.quchu.quchu.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.model.SearchCategoryBean;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.widget.DropDownMenu.DropDownMenu;
import co.quchu.quchu.widget.SearchView;

/**
 * 搜索结果展示
 * <p>
 * Created by mwb on 16/10/20.
 */
public class SearchResultActivity extends BaseBehaviorActivity {

  @Bind(R.id.search_view) SearchView mSearchView;
  @Bind(R.id.drop_down_menu) DropDownMenu mDropDownMenu;
  private EditText mSearchInputEt;

  public static void launch(Activity activity, SearchCategoryBean categoryBean, String input) {
    Intent intent = new Intent(activity, SearchResultActivity.class);
    activity.startActivity(intent);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_result);
    ButterKnife.bind(this);

    initSearchView();

    initDropDownMenu();
  }

  private void initSearchView() {
    ImageView backBtn = mSearchView.getSearchBackBtn();
    TextView searchBtn = mSearchView.getSearchBtn();
    mSearchInputEt = mSearchView.getSearchInputEt();

    mSearchInputEt.clearFocus();
    mSearchInputEt.setFocusable(false);
    mSearchInputEt.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        mSearchInputEt.setFocusable(true);
        mSearchInputEt.setFocusableInTouchMode(true);
        mSearchInputEt.requestFocus();
        return false;
      }
    });

    mSearchInputEt.setOnKeyListener(new View.OnKeyListener() {
      @Override
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        //修改回车键功能
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
          doSearch();
        }
        return false;
      }
    });

    //返回
    backBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    //搜索
    searchBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        doSearch();
      }
    });
  }

  private void doSearch() {
    if (!NetUtil.isNetworkConnected(this)) {
      makeToast(R.string.network_error);
      return;
    }

    String inputStr = mSearchInputEt.getText().toString().trim();

    if (TextUtils.isEmpty(inputStr)) {
      makeToast("请输入搜索内容!");
      return;
    }

    if (StringUtils.containsEmoji(inputStr)) {
      makeToast("搜索内容不能含有表情字符!");
      return;
    }
  }

  private void initDropDownMenu() {
    List<String> tabs = new ArrayList<>();
    tabs.add("美食");
    tabs.add("商圈");
    tabs.add("排序");
    mDropDownMenu.addTab(tabs);
  }

  @Override
  public ArrayMap<String, Object> getUserBehaviorArguments() {
    return null;
  }

  @Override
  public int getUserBehaviorPageId() {
    return 0;
  }

  @Override
  protected int activitySetup() {
    return TRANSITION_TYPE_LEFT;
  }

  @Override
  protected String getPageNameCN() {
    return null;
  }
}
