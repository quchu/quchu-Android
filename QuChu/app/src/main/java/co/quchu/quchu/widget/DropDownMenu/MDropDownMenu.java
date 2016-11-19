package co.quchu.quchu.widget.DropDownMenu;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.AreaBean;
import co.quchu.quchu.model.SearchCategoryBean;
import co.quchu.quchu.model.SearchSortBean;
import co.quchu.quchu.widget.DropDownMenu.adapter.MDropChildAdapter;
import co.quchu.quchu.widget.DropDownMenu.adapter.MDropDownAdapter;
import co.quchu.quchu.widget.DropDownMenu.adapter.MDropParentAdapter;

/**
 * Created by mwb on 16/11/15.
 */
public class MDropDownMenu extends LinearLayout {

  private String TAG = "MDropDownMenu";

  private int mCurrentTabIndex = -1;

  private List<MDropBean> mCategoryList = new ArrayList<>();
  private List<MDropBean> mAreaList = new ArrayList<>();
  private List<MDropBean> mSortList = new ArrayList<>();
  private MDropParentAdapter mParentAdapter;
  private MDropChildAdapter mChildAdapter;
  private boolean mIsFirstIn = true;
  private int mSelectedCategoryIndex;
  private int mSelectedAreaIndex;

  @Bind(R.id.drop_tab_1) MDropTabView mTab1;
  @Bind(R.id.drop_tab_2) MDropTabView mTab2;
  @Bind(R.id.drop_tab_3) MDropTabView mTab3;
  @Bind(R.id.drop_title_layout) LinearLayout mTitleLayout;
  @Bind(R.id.drop_content_layout) LinearLayout mContentLayout;
  @Bind(R.id.drop_left_recycler_view) RecyclerView mParentRv;
  @Bind(R.id.drop_right_recycler_view) RecyclerView mChildRv;
  @Bind(R.id.drop_mask_layout) RelativeLayout mMaskLayout;

  public MDropDownMenu(Context context) {
    this(context, null);
  }

  public MDropDownMenu(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public MDropDownMenu(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    LayoutInflater.from(context).inflate(R.layout.view_drop_down_menu, this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);

    initMenu();
  }

  private void initMenu() {
    initTab();

    mContentLayout.setVisibility(GONE);
    mMaskLayout.setVisibility(GONE);

    initContent();

    mMaskLayout.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (isShowing()) {
          closeMenu();
        }
      }
    });
  }

  /**
   * init drop tab
   */
  private void initTab() {
    List<String> tabs = new ArrayList<>();
    tabs.add("全部分类");
    tabs.add("全部商圈");
    tabs.add("智能排序");

    for (int i = 0; i < mTitleLayout.getChildCount(); i++) {
      final MDropTabView tabView = (MDropTabView) mTitleLayout.getChildAt(i);
      tabView.setTag(i);
      tabView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          switchMenu(tabView, (int) v.getTag());
        }
      });
      tabView.getTextView().setText(tabs.get(i));
      tabView.getImageView().setImageResource(R.mipmap.ic_down);
    }
  }

  /**
   * init drop content
   */
  private void initContent() {
    mParentAdapter = new MDropParentAdapter();
    mParentRv.setAdapter(mParentAdapter);

    mChildAdapter = new MDropChildAdapter();
    mChildRv.setAdapter(mChildAdapter);

    mParentRv.setHasFixedSize(true);
    mChildRv.setHasFixedSize(true);

    mParentRv.setLayoutManager(new LinearLayoutManager(getContext()) {
      @Override
      public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        mChildRv.getLayoutParams().height = mParentRv.getHeight();
      }
    });
    mChildRv.setLayoutManager(new LinearLayoutManager(getContext()));

    mParentAdapter.setOnDropItemClickListener(onDropParentItemClickListener);
    mChildAdapter.setOnDropItemClickListener(onDropChildItemClickListener);
  }

  private MDropDownAdapter.OnDropItemClickListener onDropParentItemClickListener = new MDropDownAdapter.OnDropItemClickListener() {
    @Override
    public void onItemClick(MDropBean dropBean) {
      if (mListener == null) {
        return;
      }

      mListener.onItemSelected(dropBean, null);
      if (dropBean.getType() != MDropBean.DATA_TYPE_SORT) {
        mChildAdapter.setChildList(dropBean.getChildren());
      }
    }
  };

  private MDropDownAdapter.OnDropItemClickListener onDropChildItemClickListener = new MDropDownAdapter.OnDropItemClickListener() {
    @Override
    public void onItemClick(MDropBean dropBean) {
      if (mListener != null) {
        mListener.onItemSelected(null, dropBean);
      }
    }
  };

  private void switchMenu(MDropTabView target, int index) {
    boolean processDataFinish = true;
    if (!processDataFinish) {
      return;
    }

    for (int i = 0; i < mTitleLayout.getChildCount(); i++) {
      if (target == mTitleLayout.getChildAt(i)) {
        if (mCurrentTabIndex == i) {
          closeMenu();

        } else {
          showMenu(i);

          if (mListener != null) {
            mListener.onTabSelected(mCurrentTabIndex);
          }
        }
      }
    }
  }

  /**
   * 是否处于可见状态
   */
  public boolean isShowing() {
    return mCurrentTabIndex != -1;
  }

  /**
   * 显示菜单
   */
  private void showMenu(int position) {
    MDropTabView tabView = (MDropTabView) mTitleLayout.getChildAt(position);
    ImageView imageView = tabView.getImageView();
    imageView.animate().rotation(180).setDuration(250).start();

    if (!isShowing()) {
      mContentLayout.setVisibility(View.VISIBLE);
      mContentLayout.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_in));
      mMaskLayout.setVisibility(View.VISIBLE);
      mMaskLayout.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_in));

    } else {
      //上一次点击的 Tab 状态还原
      MDropTabView lastTabView = (MDropTabView) mTitleLayout.getChildAt(mCurrentTabIndex);
      ImageView lastImageView = lastTabView.getImageView();
      lastImageView.animate().rotation(0).setDuration(250).start();
    }

    mCurrentTabIndex = position;
  }

  /**
   * 关闭菜单
   */
  public void closeMenu() {
    if (isShowing()) {
      MDropTabView tabView = (MDropTabView) mTitleLayout.getChildAt(mCurrentTabIndex);
      ImageView imageView = tabView.getImageView();
      imageView.animate().rotation(0).setDuration(250).start();

      mContentLayout.setVisibility(GONE);
      mContentLayout.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_out));
      mMaskLayout.setVisibility(GONE);
      mMaskLayout.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_out));
      mCurrentTabIndex = -1;
    }
  }

  /**
   * 改变 tab 文字
   */
  public void setTabText(String text) {
    if (mCurrentTabIndex != -1) {
      MDropTabView tabView = (MDropTabView) mTitleLayout.getChildAt(mCurrentTabIndex);
      tabView.getTextView().setText(text);
    }
  }

  public void setTabText(int tabIndex, String text) {
    MDropTabView tabView = (MDropTabView) mTitleLayout.getChildAt(tabIndex);
    tabView.getTextView().setText(text);
  }

  /**
   * 分类
   *
   * @param categoryPosition 当前的分类
   * @param categoryList
   */
  public void setDropCategory(int categoryPosition, List<SearchCategoryBean> categoryList) {
    if (mCategoryList.size() == 0) {
      mCategoryList = MDropHelper.processCategory(categoryList);
    }

    mSelectedCategoryIndex = mParentAdapter.getSelectedCategoryIndex();

    if (categoryPosition != -1 && mIsFirstIn) {
      mParentAdapter.setSelectedCategoryIndex(categoryPosition);
      mSelectedCategoryIndex = categoryPosition;
      mIsFirstIn = false;
    }

    mChildRv.setVisibility(VISIBLE);
    mParentAdapter.setParentList(mCategoryList);
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        if (mCategoryList != null && mCategoryList.get(mSelectedCategoryIndex) != null) {
          mChildAdapter.setChildList(mCategoryList.get(mSelectedCategoryIndex).getChildren());
        }
      }
    }, 200);
  }

  /**
   * 商圈
   */
  public void setDropArea(List<AreaBean> areaList) {
    if (mAreaList.size() == 0) {
      mAreaList = MDropHelper.processArea(areaList);
    }

    mSelectedAreaIndex = mParentAdapter.getSelectedAreaIndex();

    mChildRv.setVisibility(VISIBLE);
    mParentAdapter.setParentList(mAreaList);
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        if (mAreaList != null && mAreaList.get(mSelectedAreaIndex) != null) {
          mChildAdapter.setChildList(mAreaList.get(mSelectedAreaIndex).getChildren());
        }
      }
    }, 200);
  }

  /**
   * 排序
   */
  public void setDropSort(List<SearchSortBean> sortList) {
    if (mSortList.size() == 0) {
      mSortList = MDropHelper.processSort(sortList);
    }

    mChildRv.setVisibility(GONE);
    mParentAdapter.setParentList(mSortList);
  }

  /**
   * 获取已经选中 child 的 parent id
   */
  public String getSelectedParentId(int dataType) {
    if (dataType == MDropBean.DATA_TYPE_CATEGORY) {
      int categorySelectedIndex = mParentAdapter.getSelectedCategoryIndex();
      return categorySelectedIndex == 0 ? "" : mCategoryList.get(categorySelectedIndex).getId();
    }

    int areaSelectedIndex = mParentAdapter.getSelectedAreaIndex();
    return areaSelectedIndex == 0 ? "" : mAreaList.get(areaSelectedIndex).getId();
  }

  /**
   * 获取已经选中 child 的 parent value
   */
  public String getSelectedParentValue(int dataType) {
    if (dataType == MDropBean.DATA_TYPE_CATEGORY) {
      int categorySelectedIndex = mParentAdapter.getSelectedCategoryIndex();
      if (mCategoryList != null && mCategoryList.get(categorySelectedIndex) != null) {
        return mCategoryList.get(categorySelectedIndex).getText();
      }

    } else if (dataType == MDropBean.DATA_TYPE_AREA) {
      int areaSelectedIndex = mParentAdapter.getSelectedAreaIndex();
      if (mAreaList != null && mAreaList.get(areaSelectedIndex) != null) {
        return mAreaList.get(areaSelectedIndex).getText();
      }

    } else {
      int sortSelectedIndex = mParentAdapter.getSelectedSortIndex();
      if (mSortList != null && mSortList.get(sortSelectedIndex) != null) {
        return mSortList.get(sortSelectedIndex).getText();
      }
    }

    return "默认分类";
  }

  /**
   * 状态重置
   */
  public void reset() {
    setTabText(0, "全部分类");
    setTabText(1, "全部商圈");
    setTabText(2, "智能排序");

    mParentAdapter.setSelectedCategoryIndex(0);
    mParentAdapter.setSelectedAreaIndex(0);
    mParentAdapter.setSelectedSortIndex(0);
    mChildAdapter.setSelectedCategoryDropBean(null);
    mChildAdapter.setSelectedAreaDropBean(null);
    mChildAdapter.setCategorySelectedIndex(0);
    mChildAdapter.setAreaSelectedIndex(0);
  }

  private OnDropDownMenuClickListener mListener;

  public void setOnDropDownMenuClickListener(OnDropDownMenuClickListener listener) {
    mListener = listener;
  }

  public interface OnDropDownMenuClickListener {
    void onTabSelected(int tabIndex);

    void onItemSelected(MDropBean parent, MDropBean child);
  }
}
