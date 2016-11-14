package co.quchu.quchu.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.model.SceneInfoModel;
import co.quchu.quchu.model.SearchCategoryBean;
import co.quchu.quchu.net.NetUtil;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.SearchPresenter;
import co.quchu.quchu.utils.SoftInputUtils;
import co.quchu.quchu.view.adapter.SearchAdapterNew;
import co.quchu.quchu.widget.SearchView;
import co.quchu.quchu.widget.TagCloudView;

/**
 * 搜索界面
 * <p>
 * Created by mwb on 16/10/18.
 */
public class SearchActivityNew extends BaseBehaviorActivity {

  @Bind(R.id.search_view) SearchView mSearchView;
  @Bind(R.id.tag_cloud_view) TagCloudView mTagCloudView;
  @Bind(R.id.search_category_grid_view) GridView mCategoryGridView;
  @Bind(R.id.tag_refresh_btn) TextView mTagRefreshBtn;

  private static String INTENT_KEY_ALL_SCENE_LIST = "intent_key_all_scene_list";

  private CategoryGridAdapter mCategoryAdapter;
  private SearchAdapterNew mSearchHistoryAdapter;
  private EditText mSearchInputEt;

  private List<String> mSearchHistoryList = new ArrayList<>();
  private ArrayList<SearchCategoryBean> mSearchCategoryList = new ArrayList<>();
  private List<String> mTags;
  private List<SceneInfoModel> mAllSceneList;

  public static void launch(Activity activity, List<SceneInfoModel> allSceneList) {
    Intent intent = new Intent(activity, SearchActivityNew.class);
    intent.putExtra(INTENT_KEY_ALL_SCENE_LIST, (Serializable) allSceneList);
    activity.startActivity(intent);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_new);
    ButterKnife.bind(this);

    mAllSceneList = (List<SceneInfoModel>) getIntent().getSerializableExtra(INTENT_KEY_ALL_SCENE_LIST);

    initTags();

    initSearchView();

    initCategory();

//    getNetArticleKeyword();
  }

  private void initTags() {
    mTags = new ArrayList<>();
    if (mAllSceneList != null && mAllSceneList.size() > 0) {
      mTagRefreshBtn.setVisibility(View.VISIBLE);
      for (SceneInfoModel model : mAllSceneList) {
        mTags.add(model.getSceneName());
      }
    }

    mTagCloudView.setTags(mTags);
    mTagCloudView.setOnTagClickListener(new TagCloudView.OnTagClickListener() {
      @Override
      public void onTagClick(int position) {
        SceneInfoModel model = mAllSceneList.get(position);
        SceneDetailActivity.enterActivity(SearchActivityNew.this, model.getSceneId(), model.getSceneName(), true);
      }
    });
  }

  /**
   * 对场景列表随机排序
   */
  private void shuffleTagList() {
    if (mAllSceneList != null && mAllSceneList.size() > 3) {
      Collections.shuffle(mAllSceneList);
    }

    initTags();
  }

  /**
   * 获取文章关键字
   */
//  private void getNetArticleKeyword() {
//    SearchPresenter.getNetArticleKeyword(this, new CommonListener<List<ArticleKeyword>>() {
//      @Override
//      public void successListener(List<ArticleKeyword> response) {
//        initTags(response);
//      }
//
//      @Override
//      public void errorListener(VolleyError error, String exception, String msg) {
//
//      }
//    });
//  }

  /**
   * 编辑框
   */
  private void initSearchView() {
    mSearchView.setOnSearchViewClickListener(new SearchView.OnSearchViewClickListener() {
      @Override
      public void onClickBack() {
        onBackPressed();
      }

      @Override
      public void onTouchEditText() {
        mSearchView.showHistory();
      }

      @Override
      public void onClickSearch(String inputStr) {
        doSearch(inputStr);
      }

      @Override
      public void onClickHistory(String history) {
        doSearch(history);
      }
    });
  }

  /**
   * 开始搜索
   */
  private void doSearch(String inputStr) {
    if (!NetUtil.isNetworkConnected(this)) {
      makeToast(R.string.network_error);
      return;
    }

    SearchResultActivity.launch(SearchActivityNew.this, null, inputStr, -1);
  }

  /**
   * 搜索分类
   */
  private void initCategory() {
    mCategoryAdapter = new CategoryGridAdapter(this);
    mCategoryGridView.setAdapter(mCategoryAdapter);
    mCategoryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mSearchCategoryList == null) {
          return;
        }

        if (mSearchView.isShowHistory()) {
          mSearchView.hideHistory();
        }

        SoftInputUtils.hideSoftInput(SearchActivityNew.this);

        SearchCategoryBean categoryBean = mSearchCategoryList.get(position);
        if (categoryBean == null) {
          return;
        }

        //统计
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

        //启动搜索结果界面
        SearchResultActivity.launch(SearchActivityNew.this, categoryBean, "", position + 1);
      }
    });

    mCategoryGridView.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (mSearchView.isShowHistory()) {
          mSearchView.hideHistory();
        }
        return false;
      }
    });

    querySearchCategory();
  }

//  private SearchAdapterNew.OnSearchItemClickListener onItemClickListener = new SearchAdapterNew.OnSearchItemClickListener() {
//    @Override
//    public void onClick(int position, Parcelable bean, int itemType) {
//      if (itemType == SearchAdapterNew.ITEM_TYPE_CATEGORY) {
//
//        //统计
//        switch (position) {
//          case 0:
//            UMEvent("food_c");
//            break;
//          case 1:
//            UMEvent("hotel_c");
//            break;
//          case 2:
//            UMEvent("entertainment_c");
//            break;
//          case 3:
//            UMEvent("relaxation_c");
//            break;
//          case 4:
//            UMEvent("shopping_c");
//            break;
//          case 5:
//            UMEvent("event_c");
//            break;
//        }
//
//        //启动搜索结果界面
//        SearchCategoryBean categoryBean = (SearchCategoryBean) bean;
//        if (categoryBean != null) {
//
//          SearchResultActivity.launch(SearchActivityNew.this, categoryBean, "", position + 1);
//        }
//      }
//    }
//
//    @Override
//    public void onClickHistory(String keyword) {
//      //空实现
//    }
//
//    @Override
//    public void onDeleteHistory(String keyword) {
//     //空实现
//    }
//  };

  /**
   * 获取搜索分类
   */
  private void querySearchCategory() {
    SearchPresenter.getCategoryTag(this, new CommonListener<ArrayList<SearchCategoryBean>>() {
      @Override
      public void successListener(ArrayList<SearchCategoryBean> response) {

        mSearchCategoryList.clear();
        mSearchCategoryList.addAll(response);
        mCategoryAdapter.notifyDataSetChanged();
      }

      @Override
      public void errorListener(VolleyError error, String exception, String msg) {
      }
    });
  }

  /**
   * 搜索标签
   */
//  private void initTags(final List<ArticleKeyword> articleKeywords) {
//    final List<String> tags = new ArrayList<>();
//    if (articleKeywords != null && articleKeywords.size() > 0) {
//      for (ArticleKeyword keyword : articleKeywords) {
//        tags.add(keyword.getKeyword());
//      }
//    }
//    mTagCloudView.setTags(tags);
//    mTagCloudView.setOnTagClickListener(new TagCloudView.OnTagClickListener() {
//      @Override
//      public void onTagClick(int position) {
//        ArticleKeyword keyword = articleKeywords.get(position);
//        ArticleDetailActivity.enterActivity(SearchActivityNew.this, String.valueOf(keyword.getNetArticleId()), keyword.getKeyword(), "");
//      }
//    });
//  }
  @Override
  public void onBackPressed() {
    SoftInputUtils.hideSoftInput(this);

    if (mSearchView.isShowHistory()) {
      mSearchView.hideHistory();
      return;
    }

    super.onBackPressed();
  }

  @OnClick(R.id.tag_refresh_btn)
  public void onClick() {
    shuffleTagList();
  }

  @Override
  public ArrayMap<String, Object> getUserBehaviorArguments() {
    return null;
  }

  @Override
  public int getUserBehaviorPageId() {
    return 114;
  }

  @Override
  protected int activitySetup() {
    return TRANSITION_TYPE_LEFT;
  }

  @Override
  protected String getPageNameCN() {
    return null;
  }

  /**
   * 搜索分类适配器
   */
  private class CategoryGridAdapter extends BaseAdapter {

    private Context mContext;

    public CategoryGridAdapter(Context context) {
      mContext = context;
    }

    @Override
    public int getCount() {
      return mSearchCategoryList.size();
    }

    @Override
    public Object getItem(int position) {
      return null;
    }

    @Override
    public long getItemId(int position) {
      return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      CategoryViewHolder holder;
      if (convertView == null) {
        holder = new CategoryViewHolder();

        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_search_category, parent, false);
        holder.coverImg = (SimpleDraweeView) convertView.findViewById(R.id.simpleDraweeView);
        holder.nameTv = (TextView) convertView.findViewById(R.id.search_item_categoryName);

        convertView.setTag(holder);
      } else {
        holder = (CategoryViewHolder) convertView.getTag();
      }

      //搜索分类
      final SearchCategoryBean categoryBean = mSearchCategoryList.get(position);
      if (!TextUtils.isEmpty(categoryBean.getIconUrl())) {
        holder.coverImg.setImageURI(Uri.parse(categoryBean.getIconUrl()));
      } else {
        holder.coverImg.getHierarchy().setPlaceholderImage(R.mipmap.ic_launcher);
      }

      holder.nameTv.setText(categoryBean.getZh());

      return convertView;
    }

    private class CategoryViewHolder {
      SimpleDraweeView coverImg;
      TextView nameTv;
    }
  }
}
