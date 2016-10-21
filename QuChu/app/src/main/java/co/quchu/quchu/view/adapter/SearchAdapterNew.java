package co.quchu.quchu.view.adapter;

import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.SearchCategoryBean;
import co.quchu.quchu.model.SearchKeywordModel;

/**
 * 搜索相关适配器
 * 搜索分类、搜索历史记录
 *
 * Created by mwb on 16/10/19.
 */
public class SearchAdapterNew extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  public static final int ITEM_TYPE_CATEGORY = -1;//搜索分类
  public static final int ITEM_TYPE_HISTORY = -2;//搜索历史记录

  private List<SearchCategoryBean> mCategoryList;
  private List<SearchKeywordModel> mHistoryList;

  private boolean mIsCategory = true;

  public void setIsCategory(boolean isCategory) {
    mIsCategory = isCategory;
  }

  public void setCategoryList(List<SearchCategoryBean> categoryList) {
    mCategoryList = categoryList;
    notifyDataSetChanged();
  }

  public void setHistoryList(List<SearchKeywordModel> historyList) {
    mHistoryList = historyList;
    notifyDataSetChanged();
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == ITEM_TYPE_CATEGORY) {
      return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_category, parent, false));
    }

    return new HistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_history, parent, false));
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
    if (viewHolder instanceof CategoryViewHolder) {
      //搜索分类
      final CategoryViewHolder holder = (CategoryViewHolder) viewHolder;
      final SearchCategoryBean categoryBean = mCategoryList.get(position);
      if (!TextUtils.isEmpty(categoryBean.getIconUrl())) {
        holder.mCategoryCoverImg.setImageURI(Uri.parse(categoryBean.getIconUrl()));
      } else {
        holder.mCategoryCoverImg.getHierarchy().setPlaceholderImage(R.mipmap.ic_launcher);
      }

      holder.mCategoryNameTv.setText(categoryBean.getZh());

      holder.itemView.setTag(categoryBean);
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          SearchCategoryBean categoryBean = (SearchCategoryBean) v.getTag();
          if (categoryBean != null && mListener != null) {
            mListener.onClick(position, categoryBean, ITEM_TYPE_CATEGORY);
          }
        }
      });

    } else if (viewHolder instanceof HistoryViewHolder) {
      //搜索历史记录
      final HistoryViewHolder holder = (HistoryViewHolder) viewHolder;
      final SearchKeywordModel keywordModel = mHistoryList.get(position);
      holder.mHistoryTv.setText(keywordModel.getKeyword());

      holder.itemView.setTag(keywordModel);
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          SearchKeywordModel keywordModel = (SearchKeywordModel) v.getTag();
          if (keywordModel != null && mListener != null) {
            mListener.onClick(-1, keywordModel, ITEM_TYPE_HISTORY);
          }
        }
      });

      holder.mHistoryDeleteBtn.setTag(keywordModel);
      holder.mHistoryDeleteBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          SearchKeywordModel keywordModel = (SearchKeywordModel) v.getTag();
          if (keywordModel != null && mListener != null) {
            mListener.onDelete(keywordModel, ITEM_TYPE_HISTORY);
          }
        }
      });
    }
  }

  @Override
  public int getItemViewType(int position) {
    return mIsCategory ? ITEM_TYPE_CATEGORY : ITEM_TYPE_HISTORY;
  }

  @Override
  public int getItemCount() {
    if (mIsCategory) {
      return mCategoryList != null ? mCategoryList.size() : 0;
    } else {
      return mHistoryList != null ? mHistoryList.size() : 0;
    }
  }

  /**
   * 搜索历史
   */
  public class HistoryViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.search_item_history_tv) TextView mHistoryTv;
    @Bind(R.id.search_item_history_delete_btn) ImageView mHistoryDeleteBtn;

    public HistoryViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  /**
   * 搜索分类
   */
  public class CategoryViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.simpleDraweeView) SimpleDraweeView mCategoryCoverImg;
    @Bind(R.id.search_item_categoryName) TextView mCategoryNameTv;

    public CategoryViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  private OnSearchItemClickListener mListener;

  public void setOnSearchItemClickListener(OnSearchItemClickListener listener) {
    mListener = listener;
  }

  public interface OnSearchItemClickListener {
    void onClick(int position, Parcelable bean, int itemType);

    void onDelete(Parcelable bean, int itemType);
  }
}
