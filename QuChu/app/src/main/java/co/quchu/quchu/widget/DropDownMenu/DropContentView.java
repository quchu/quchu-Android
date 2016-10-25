package co.quchu.quchu.widget.DropDownMenu;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.AreaBean;
import co.quchu.quchu.model.DetailModel;
import co.quchu.quchu.model.SearchCategoryBean;
import co.quchu.quchu.model.SearchSortBean;

import static co.quchu.quchu.base.AppContext.mContext;

/**
 * Created by mwb on 16/10/20.
 */
public class DropContentView extends LinearLayout {

  @Bind(R.id.drop_left_recycler_view) RecyclerView mLeftRecyclerView;
  @Bind(R.id.drop_right_recycler_view) RecyclerView mRightRecyclerView;

  //保存选中的 parent 下标
  private static int mCategorySelectedIndex = 0;
  private static int mAreaSelectedIndex = 0;
  private static int mSortSelectedIndex = 0;

  private List<DropBean> mCategoryList = new ArrayList<>();
  private List<DropBean> mAreaList = new ArrayList<>();
  private List<DropBean> mSortList = new ArrayList<>();

  private ContentLeftAdapter mContentLeftAdapter;
  private ContentRightAdapter mContentRightAdapter;
  private boolean isFirstIn = true;

  public DropContentView(Context context) {
    this(context, null);
  }

  public DropContentView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public DropContentView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    init(context);
  }

  private void init(Context context) {
    LayoutInflater.from(context).inflate(R.layout.view_drop_content, this);
    ButterKnife.bind(this);

    mContentLeftAdapter = new ContentLeftAdapter();
    mLeftRecyclerView.setAdapter(mContentLeftAdapter);

    mContentRightAdapter = new ContentRightAdapter();
    mRightRecyclerView.setAdapter(mContentRightAdapter);

    mLeftRecyclerView.setHasFixedSize(true);
    mRightRecyclerView.setHasFixedSize(true);

    mLeftRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
      @Override
      public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        mRightRecyclerView.getLayoutParams().height = mLeftRecyclerView.getHeight();
      }
    });
    mRightRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    mContentLeftAdapter.setOnDropContentItemClickListener(onLeftItemClickListener);
    mContentRightAdapter.setOnDropContentItemClickListener(onRightItemClickListener);
  }

  /**
   * 父数据 点击监听
   */
  private ContentLeftAdapter.OnDropContentItemClickListener onLeftItemClickListener = new ContentLeftAdapter.OnDropContentItemClickListener() {
    @Override
    public void onItemClick(DropBean parent) {
      if (mListener == null) {
        return;
      }

      mListener.onItemSelected(parent, null);
      if (parent.getType() != DropBean.DATA_TYPE_SORT) {
        mContentRightAdapter.setChildren(parent.getChildren());
      }
    }
  };

  /**
   * 子数据 点击监听
   */
  private ContentRightAdapter.OnDropContentItemClickListener onRightItemClickListener = new ContentRightAdapter.OnDropContentItemClickListener() {
    @Override
    public void onItemClick(DropBean child) {
      if (mListener != null) {
        mListener.onItemSelected(null, child);
      }
    }
  };

  /**
   * 设置分类
   */
  public void setDropCategory(int categoryPosition, List<SearchCategoryBean> response) {
    if (mCategoryList.size() == 0) {
      if (response == null || response.size() == 0) {
        return;
      }

      SearchCategoryBean parentAll = new SearchCategoryBean();
      parentAll.setTagId(-1);
      parentAll.setZh("全部");

      List<DetailModel.TagsEntity> tagsEntities = new ArrayList<>();
      DetailModel.TagsEntity tagsEntity = new DetailModel.TagsEntity();
      tagsEntity.setTagId(-1);
      tagsEntity.setZh("全部");

      parentAll.setDatas(tagsEntities);
      response.add(0, parentAll);

      for (SearchCategoryBean bean : response) {
        bean.getDatas().add(0, tagsEntity);
      }

      for (SearchCategoryBean categoryBean : response) {
        DropBean dropBean = new DropBean();
        dropBean.setId(String.valueOf(categoryBean.getTagId()));
        dropBean.setType(DropBean.DATA_TYPE_CATEGORY);
        dropBean.setText(categoryBean.getZh());

        List<DetailModel.TagsEntity> children = categoryBean.getDatas();
        if (children == null || children.size() == 0) {
          break;
        }
        List<DropBean> newChildren = new ArrayList<>();
        for (DetailModel.TagsEntity child : children) {
          DropBean bean = new DropBean();
          bean.setId(String.valueOf(child.getTagId()));
          bean.setText(child.getZh());
          bean.setType(DropBean.DATA_TYPE_CATEGORY);
          newChildren.add(bean);
        }
        dropBean.setChildren(newChildren);
        mCategoryList.add(dropBean);
      }
    }

    if (categoryPosition != -1 && isFirstIn) {
      mCategorySelectedIndex = categoryPosition;
      isFirstIn = false;
    }

    fillData(mCategorySelectedIndex, mCategoryList);
  }

  /**
   * 设置地区商圈
   */
  public void setDropArea(List<AreaBean> response) {
    if (mAreaList.size() == 0) {
      if (response == null || response.size() == 0) {
        return;
      }

      AreaBean parentAll = new AreaBean();
      parentAll.setAreaId("-1");
      parentAll.setAreaName("全部商圈");

      List<AreaBean.CircleListBean> circleListBeen = new ArrayList<>();
      AreaBean.CircleListBean circleListBean = new AreaBean.CircleListBean();
      circleListBean.setCircleId("-1");
      circleListBean.setCircleName("全部");

      parentAll.setCircleList(circleListBeen);
      response.add(0, parentAll);

      for (AreaBean bean : response) {
        bean.getCircleList().add(0, circleListBean);
      }

      for (AreaBean areaBean : response) {
        DropBean dropBean = new DropBean();
        dropBean.setId(areaBean.getAreaId());
        dropBean.setText(areaBean.getAreaName());
        dropBean.setType(DropBean.DATA_TYPE_AREA);

        List<AreaBean.CircleListBean> children = areaBean.getCircleList();
        if (children == null || children.size() == 0) {
          break;
        }
        List<DropBean> newChildren = new ArrayList<>();
        for (AreaBean.CircleListBean child : children) {
          DropBean bean = new DropBean();
          bean.setId(child.getCircleId());
          bean.setText(child.getCircleName());
          bean.setType(DropBean.DATA_TYPE_AREA);
          newChildren.add(bean);
        }
        dropBean.setChildren(newChildren);
        mAreaList.add(dropBean);
      }
    }

    fillData(mAreaSelectedIndex, mAreaList);
  }

  /**
   * 设置排序
   */
  public void setDropSort(List<SearchSortBean> response) {
    if (mSortList.size() == 0) {
      if (response == null || response.size() == 0) {
        return;
      }

      for (SearchSortBean searchSortBean : response) {
        DropBean dropBean = new DropBean();
        dropBean.setId(String.valueOf(searchSortBean.getSortId()));
        dropBean.setText(searchSortBean.getSortName());
        dropBean.setType(DropBean.DATA_TYPE_SORT);
        dropBean.setChildren(null);
        mSortList.add(dropBean);
      }
    }

    fillData(0, mSortList);
  }

  /**
   * 统一填充适配器数据
   */
  private void fillData(final int index, final List<DropBean> parent) {
    if (parent == null) {
      return;
    }

    mContentLeftAdapter.setParent(parent);

    if (parent.get(index) != null) {
      if (parent.get(index).getChildren() != null) {
        mRightRecyclerView.setVisibility(VISIBLE);
      } else {
        mRightRecyclerView.setVisibility(GONE);
      }
    }

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        mContentRightAdapter.setChildren(parent.get(index).getChildren());
      }
    }, 200);
  }

  /**
   * 获取已经选中 child 的 parent 的值
   */
  public String getSelectedParentValue(int dataType) {
    if (dataType == DropBean.DATA_TYPE_CATEGORY) {
      int categorySelectedIndex = mContentLeftAdapter.getCategorySelectedIndex();
      return mCategoryList.get(categorySelectedIndex).getText();
    }

    int areaSelectedIndex = mContentLeftAdapter.getAreaSelectedIndex();
    return mAreaList.get(areaSelectedIndex).getText();
  }

  /**
   * 获取已经选中 child 的 parent 的 id
   */
  public String getSelectedParentId(int dataType) {
    if (dataType == DropBean.DATA_TYPE_CATEGORY) {
      int categorySelectedIndex = mContentLeftAdapter.getCategorySelectedIndex();
      return categorySelectedIndex == 0 ? "" : mCategoryList.get(categorySelectedIndex).getId();
    }

    int areaSelectedIndex = mContentLeftAdapter.getAreaSelectedIndex();
    return areaSelectedIndex == 0 ? "" : mAreaList.get(areaSelectedIndex).getId();
  }

  private DropDownMenu.OnDropTabClickListener mListener;

  public void setOnDropTabClickListener(DropDownMenu.OnDropTabClickListener listener) {
    mListener = listener;
  }

  public static class ContentLeftAdapter extends RecyclerView.Adapter<ContentLeftAdapter.ContentViewHolder> {

    private List<DropBean> mParent;

    @Override
    public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new ContentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_drop_conent, parent, false));
    }

    @Override
    public void onBindViewHolder(ContentViewHolder holder, final int position) {
      DropBean parent = mParent.get(position);
      holder.mContentTv.setText(parent.getText());
      holder.mContentDivider.setVisibility(GONE);
      holder.mContentIv.setVisibility(GONE);
      holder.mContentTv.setTextColor(mContext.getResources().getColor(R.color.standard_color_h2_dark));

      if (parent.getType() == DropBean.DATA_TYPE_CATEGORY) {
        holder.mContentTv.setSelected(mCategorySelectedIndex == position ? true : false);

      } else if (parent.getType() == DropBean.DATA_TYPE_AREA) {
        holder.mContentTv.setSelected(mAreaSelectedIndex == position ? true : false);

      } else if (parent.getType() == DropBean.DATA_TYPE_SORT) {
        holder.mContentTv.setSelected(true);
        holder.mContentTv.setTextColor(mSortSelectedIndex == position ?
            mContext.getResources().getColor(R.color.standard_color_yellow) : mContext.getResources().getColor(R.color.standard_color_h2_dark));
        holder.mContentDivider.setVisibility(VISIBLE);
        holder.mContentIv.setVisibility(mSortSelectedIndex == position ? VISIBLE : GONE);
      }

      holder.itemView.setTag(parent);
      holder.itemView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          DropBean dropBean = (DropBean) v.getTag();
          if (dropBean != null && mListener != null) {

            if (dropBean.getType() == DropBean.DATA_TYPE_CATEGORY) {
              mCategorySelectedIndex = position;

            } else if (dropBean.getType() == DropBean.DATA_TYPE_AREA) {
              mAreaSelectedIndex = position;

            } else if (dropBean.getType() == DropBean.DATA_TYPE_SORT) {
              mSortSelectedIndex = position;
            }

            notifyDataSetChanged();

            mListener.onItemClick(dropBean);
          }
        }
      });
    }

    @Override
    public int getItemCount() {
      return mParent != null ? mParent.size() : 0;
    }

    public void setParent(List<DropBean> parent) {
      mParent = parent;
      notifyDataSetChanged();
    }

    public int getCategorySelectedIndex() {
      return mCategorySelectedIndex;
    }

    public int getAreaSelectedIndex() {
      return mAreaSelectedIndex;
    }

    private OnDropContentItemClickListener mListener;

    public void setOnDropContentItemClickListener(OnDropContentItemClickListener listener) {
      mListener = listener;
    }

    public interface OnDropContentItemClickListener {
      void onItemClick(DropBean dropBean);
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {

      @Bind(R.id.item_drop_content_tv) TextView mContentTv;
      @Bind(R.id.item_drop_content_iv) ImageView mContentIv;
      @Bind(R.id.item_drop_content_divider) View mContentDivider;

      public ContentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
      }
    }
  }

  public static class ContentRightAdapter extends RecyclerView.Adapter<ContentRightAdapter.ContentViewHolder> {

    //保存选中的 child
    private DropBean categoryDropBean;
    private DropBean areaDropBean;

    private int categoryIndex = 0;
    private int areaIndex = 0;

    private List<DropBean> mChildren;
    private Context mContext;

    @Override
    public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      mContext = parent.getContext();
      return new ContentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_drop_conent, parent, false));
    }

    @Override
    public void onBindViewHolder(ContentViewHolder holder, final int position) {
      DropBean child = mChildren.get(position);
      holder.mContentTv.setText(child.getText());
      holder.mContentTv.setSelected(true);
      holder.mContentDivider.setVisibility(VISIBLE);

      if (child.getType() == DropBean.DATA_TYPE_CATEGORY) {

        if (categoryDropBean != null) {
          holder.mContentTv.setTextColor(categoryDropBean.equals(child) ? mContext.getResources().getColor(R.color.standard_color_yellow)
              : mContext.getResources().getColor(R.color.standard_color_h2_dark));
          holder.mContentIv.setVisibility(categoryDropBean.equals(child) ? VISIBLE : GONE);

        } else {
          holder.mContentTv.setTextColor(categoryIndex == position ? mContext.getResources().getColor(R.color.standard_color_yellow)
              : mContext.getResources().getColor(R.color.standard_color_h2_dark));
          holder.mContentIv.setVisibility(categoryIndex == position ? VISIBLE : GONE);
        }

      } else if (child.getType() == DropBean.DATA_TYPE_AREA) {

        if (areaDropBean != null) {
          holder.mContentTv.setTextColor(areaDropBean.equals(child) ? mContext.getResources().getColor(R.color.standard_color_yellow)
              : mContext.getResources().getColor(R.color.standard_color_h2_dark));
          holder.mContentIv.setVisibility(areaDropBean.equals(child) ? VISIBLE : GONE);

        } else {
          holder.mContentTv.setTextColor(areaIndex == position ? mContext.getResources().getColor(R.color.standard_color_yellow)
              : mContext.getResources().getColor(R.color.standard_color_h2_dark));
          holder.mContentIv.setVisibility(areaIndex == position ? VISIBLE : GONE);
        }
      }

      holder.itemView.setTag(child);
      holder.itemView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          DropBean dropBean = (DropBean) v.getTag();
          if (dropBean != null && mListener != null) {

            if (dropBean.getType() == DropBean.DATA_TYPE_CATEGORY) {
              categoryDropBean = dropBean;

            } else if (dropBean.getType() == DropBean.DATA_TYPE_AREA) {
              areaDropBean = dropBean;
            }

            mListener.onItemClick(dropBean);
          }
        }
      });
    }

    @Override
    public int getItemCount() {
      return mChildren != null ? mChildren.size() : 0;
    }

    public void setChildren(List<DropBean> children) {
      mChildren = children;
      notifyDataSetChanged();
    }

    private OnDropContentItemClickListener mListener;

    public void setOnDropContentItemClickListener(OnDropContentItemClickListener listener) {
      mListener = listener;
    }

    public interface OnDropContentItemClickListener {
      void onItemClick(DropBean dropBean);
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {

      @Bind(R.id.item_drop_content_tv) TextView mContentTv;
      @Bind(R.id.item_drop_content_iv) ImageView mContentIv;
      @Bind(R.id.item_drop_content_divider) View mContentDivider;

      public ContentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
      }
    }
  }

  public class DropBean {

    public static final int DATA_TYPE_CATEGORY = 1;
    public static final int DATA_TYPE_AREA = 2;
    public static final int DATA_TYPE_SORT = 3;

    private String id;
    private String text;
    private int type;
    private List<DropBean> children;

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getText() {
      return text;
    }

    public void setText(String text) {
      this.text = text;
    }

    public int getType() {
      return type;
    }

    public void setType(int type) {
      this.type = type;
    }

    public List<DropBean> getChildren() {
      return children;
    }

    public void setChildren(List<DropBean> children) {
      this.children = children;
    }
  }
}
