package co.quchu.quchu.widget.DropDownMenu;

import android.content.Context;
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

/**
 * Created by mwb on 16/10/20.
 */
public class DropContentView extends LinearLayout {

  public static final int DATA_TYPE_CATEGORY = 1;
  public static final int DATA_TYPE_AREA = 2;
  public static final int DATA_TYPE_SORT = 3;

  @Bind(R.id.drop_left_recycler_view) RecyclerView mLeftRecyclerView;
  @Bind(R.id.drop_right_recycler_view) RecyclerView mRightRecyclerView;

  private List<DropBean> categoryList = new ArrayList<>();
  private List<DropBean> areaList = new ArrayList<>();
  private List<DropBean> sortList = new ArrayList<>();
  private List<DropBean> children = new ArrayList<>();

  private ContentLeftAdapter mContentLeftAdapter;
  private ContentRightAdapter mContentRightAdapter;
  private static int mDataType;

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

  private ContentLeftAdapter.OnDropContentItemClickListener onLeftItemClickListener = new ContentLeftAdapter.OnDropContentItemClickListener() {
    @Override
    public void onItemClick(DropBean dropBean) {
      if (mDataType == DATA_TYPE_SORT) {
        if (mListener != null) {
          mListener.onItemSelected(dropBean);
        }
      } else {
        mContentRightAdapter.setChildren(dropBean.getChildren());
      }
    }
  };

  private ContentRightAdapter.OnDropContentItemClickListener onRightItemClickListener = new ContentRightAdapter.OnDropContentItemClickListener() {
    @Override
    public void onItemClick(DropBean dropBean) {
      if (mListener != null) {
        mListener.onItemSelected(dropBean);
      }
    }
  };

  public void setDropCategory(List<SearchCategoryBean> response) {
    if (categoryList.size() == 0) {
      if (response == null || response.size() == 0) {
        return;
      }

      for (SearchCategoryBean categoryBean : response) {
        DropBean dropBean = new DropBean();
        dropBean.setId(String.valueOf(categoryBean.getTagId()));
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
          newChildren.add(bean);
        }
        dropBean.setChildren(newChildren);
        categoryList.add(dropBean);
      }
    }

    fillData(categoryList, DATA_TYPE_CATEGORY);
  }

  public void setDropArea(List<AreaBean> response) {
    if (areaList.size() == 0) {
      if (response == null || response.size() == 0) {
        return;
      }

      for (AreaBean areaBean : response) {
        DropBean dropBean = new DropBean();
        dropBean.setId(areaBean.getAreaId());
        dropBean.setText(areaBean.getAreaName());

        List<AreaBean.CircleListBean> children = areaBean.getCircleList();
        if (children == null || children.size() == 0) {
          break;
        }
        List<DropBean> newChildren = new ArrayList<>();
        for (AreaBean.CircleListBean child : children) {
          DropBean bean = new DropBean();
          bean.setId(child.getCircleId());
          bean.setText(child.getCircleName());
          newChildren.add(bean);
        }
        dropBean.setChildren(newChildren);
        areaList.add(dropBean);
      }
    }

    fillData(areaList, DATA_TYPE_AREA);
  }

  public void setDropSort(List<SearchSortBean> response) {
    if (sortList.size() == 0) {
      if (response == null || response.size() == 0) {
        return;
      }

      for (SearchSortBean searchSortBean : response) {
        DropBean dropBean = new DropBean();
        dropBean.setId(String.valueOf(searchSortBean.getSortId()));
        dropBean.setText(searchSortBean.getSortName());
        dropBean.setChildren(null);
        sortList.add(dropBean);
      }
    }

    fillData(sortList, DATA_TYPE_SORT);
  }

  private void fillData(List<DropBean> data, int dataType) {
    if (data != null && data.size() > 0 && data.get(0).getChildren() != null) {
      children.clear();
      children.addAll(data.get(0).getChildren());
    } else {
      children.clear();
    }

    if (children.size() >0) {
      mRightRecyclerView.setVisibility(VISIBLE);

    } else {
      mRightRecyclerView.setVisibility(GONE);
    }

    mContentLeftAdapter.setParent(data, dataType);
    mContentRightAdapter.setChildren(children);
  }

  private DropDownMenu.OnDropTabClickListener mListener;

  public void setOnDropTabClickListener(DropDownMenu.OnDropTabClickListener listener) {
    mListener = listener;
  }

  public static class ContentLeftAdapter extends RecyclerView.Adapter<ContentLeftAdapter.ContentViewHolder> {

    private int categorySelectedIndex = 0;
    private int areaSelectedIndex = 0;
    private int sortSelectedIndex = 0;

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

      if (mDataType == DATA_TYPE_CATEGORY) {
        holder.mContentTv.setSelected(categorySelectedIndex == position ? true : false);

      } else if (mDataType == DATA_TYPE_AREA) {
        holder.mContentTv.setSelected(areaSelectedIndex == position ? true : false);

      } else if (mDataType == DATA_TYPE_SORT) {
        holder.mContentTv.setSelected(true);
        holder.mContentIv.setVisibility(sortSelectedIndex == position ? VISIBLE : GONE);
      }

      holder.itemView.setTag(parent);
      holder.itemView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          DropBean dropBean = (DropBean) v.getTag();
          if (dropBean != null && mListener != null) {

            if (mDataType == DATA_TYPE_CATEGORY) {
              categorySelectedIndex = position;

            } else if (mDataType == DATA_TYPE_AREA) {
              areaSelectedIndex = position;

            } else if (mDataType == DATA_TYPE_SORT) {
              sortSelectedIndex = position;
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

    public void setParent(List<DropBean> parent, int dataType) {
      mParent = parent;
      mDataType = dataType;
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

  public static class ContentRightAdapter extends RecyclerView.Adapter<ContentRightAdapter.ContentViewHolder> {

    private List<DropBean> mChildren;

    @Override
    public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new ContentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_drop_conent, parent, false));
    }

    @Override
    public void onBindViewHolder(ContentViewHolder holder, final int position) {
      DropBean child = mChildren.get(position);
      holder.mContentTv.setText(child.getText());
      holder.mContentTv.setSelected(true);
      holder.mContentDivider.setVisibility(VISIBLE);

      holder.itemView.setTag(child);
      holder.itemView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          DropBean dropBean = (DropBean) v.getTag();
          if (dropBean != null && mListener != null) {
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
