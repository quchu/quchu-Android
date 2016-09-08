package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.SearchCategoryBean;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.SearchPresenter;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.adapter.SearchAdapter;
import co.quchu.quchu.widget.ErrorView;
import com.android.volley.VolleyError;
import java.util.ArrayList;
import java.util.List;

/**
 * SearchFragment
 * User: Chenhs
 * Date: 2015-12-04
 */
public class SearchFragment extends BaseFragment implements View.OnClickListener {

  @Override protected String getPageNameCN() {
    return getString(R.string.pname_search);
  }

  @Bind(R.id.search_input_et) EditText searchInputEt;
  @Bind(R.id.search_button_rl) TextView searchButtonRl;

  @Bind(R.id.search_result_rv) RecyclerView searchResultRv;
  @Bind(R.id.errorView) ErrorView errorView;


  private SearchAdapter resultAdapter;

  private ArrayList<SearchCategoryBean> categoryParentList = new ArrayList<>();


  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_search, container, false);
    ButterKnife.bind(this, v);
    initEdittext();
    initData();
    if (!NetUtil.isNetworkConnected(getActivity()) || categoryParentList==null){
      errorView.showViewDefault(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          DialogUtil.showProgess(getActivity(), "加载中");
          getData();
        }
      });
    }
    getData();
    searchInputEt.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        searchInputEt.setCursorVisible(true);
      }
    });
    return v;
  }

  private void getData(){
    SearchPresenter.getCategoryTag(this, new CommonListener<ArrayList<SearchCategoryBean>>() {
      @Override public void successListener(ArrayList<SearchCategoryBean> response) {
        initCategoryList(response);
        errorView.hideView();
      }

      @Override public void errorListener(VolleyError error, String exception, String msg) {
        errorView.showViewDefault(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            DialogUtil.showProgess(getActivity(), "加载中");
            getData();
          }
        });
      }
    });
  }


  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  //获取大的分类
  public void initCategoryList(ArrayList<SearchCategoryBean> pData) {

    if (resultAdapter.isCategory()) {
      categoryParentList.clear();
      categoryParentList.addAll(pData);
      resultAdapter.setCategoryList(categoryParentList);
    }
  }

  @Override public void onClick(View v) {
    if (KeyboardUtils.isFastDoubleClick()) return;
    switch (v.getId()) {
      case R.id.search_button_rl:
        searchInputEt.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
        SearchActivity.enterActivity(getActivity(),null,null,searchInputEt.getText().toString());
        break;

      case R.id.search_input_et:
        break;
    }
  }

  @Override public void onResume() {
    super.onResume();
    if (null!=searchInputEt){
      searchInputEt.setText("");
    }
  }

  private void initData() {
    searchButtonRl.setOnClickListener(this);
    searchInputEt.setOnClickListener(this);
    resultAdapter = new SearchAdapter();
    searchResultRv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
    searchResultRv.setAdapter(resultAdapter);

    resultAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
      @Override public void onClick(int position, Parcelable bean, int itemYype) {

        switch (position) {
          case 0:
            UMEvent("food_c");
            break;
          case 1:
            UMEvent("hotel_c");
            break;
          case 2:
            UMEvent("entertainment_c");
            break;
          case 3:
            UMEvent("relaxation_c");
            break;
          case 4:
            UMEvent("shopping_c");
            break;
          case 5:
            UMEvent("event_c");
            break;
        }
        searchInputEt.setText(((SearchCategoryBean) bean).getZh());

        searchInputEt.setSelection(searchInputEt.getText().toString().trim().length());
        searchInputEt.setCursorVisible(false);
        SearchActivity.enterActivity(getActivity(),((SearchCategoryBean) bean).getZh(),((SearchCategoryBean) bean).getCode(),searchInputEt.getText().toString());

      }
    });
  }

  private void initEdittext() {

    searchInputEt.setOnKeyListener(new View.OnKeyListener() {//输入完后按键盘上的搜索键【回车键改为了搜索键】

      public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {//修改回车键功能
          if (NetUtil.isNetworkConnected(getActivity())) {
            if (StringUtils.isEmpty(searchInputEt.getText().toString())) {
              Toast.makeText(getActivity(), "请输入搜索内容!", Toast.LENGTH_SHORT).show();
            } else {
              if (StringUtils.containsEmoji(searchInputEt.getText().toString())) {
                Toast.makeText(getActivity(),
                    getResources().getString(R.string.search_content_has_emoji), Toast.LENGTH_SHORT)
                    .show();
              } else {
                SearchActivity.enterActivity(getActivity(),null,null,searchInputEt.getText().toString());
              }
            }
          } else {
            Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
            ((InputMethodManager) getActivity().getSystemService(
                getActivity().INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                searchResultRv.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
          }
        }
        return false;
      }
    });
  }
}
