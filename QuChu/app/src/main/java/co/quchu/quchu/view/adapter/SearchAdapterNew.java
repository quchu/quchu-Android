package co.quchu.quchu.view.adapter;

import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.model.SearchCategoryBean;
import co.quchu.quchu.model.SearchKeywordModel;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;

import static co.quchu.quchu.R.id.vDivider2;

/**
 * 搜索相关适配器
 * 搜索分类、搜索历史记录、搜索结果
 * <p>
 * Created by mwb on 16/10/19.
 */
public class SearchAdapterNew extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  public static final int ITEM_TYPE_CATEGORY = -1;//搜索分类
  public static final int ITEM_TYPE_HISTORY = -2;//搜索历史记录
  public static final int ITEM_TYPE_RESULT = -3;//搜索结果

  private List<SearchCategoryBean> mCategoryList;
  private List<SearchKeywordModel> mHistoryList;
  private List<RecommendModel> mResultList;

  private boolean mIsCategory;
  private boolean mIsResult;

  public void setCategoryList(List<SearchCategoryBean> categoryList) {
    mIsCategory = true;
    mCategoryList = categoryList;
    notifyDataSetChanged();
  }

  public void setHistoryList(List<SearchKeywordModel> historyList) {
    mHistoryList = historyList;
    notifyDataSetChanged();
  }

  public void setResultList(List<RecommendModel> resultList) {
    mIsResult = true;
    mResultList = resultList;
    notifyDataSetChanged();
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == ITEM_TYPE_CATEGORY) {
      return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_category, parent, false));

    } else if (viewType == ITEM_TYPE_RESULT) {
      return new ResultViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scene_detail_recommeded, parent, false));
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

    } else if (viewHolder instanceof ResultViewHolder) {
      //搜索结果
      ResultViewHolder holder = (ResultViewHolder) viewHolder;
      RecommendModel model = mResultList.get(position);

      if (null != model.getCover()) {
        holder.mSdvCover.setImageURI(Uri.parse(model.getCover()));
      }
      holder.mTvTitle.setText(model.getName());
      holder.mTvHeader.setText(model.getDescribe());
      holder.mTvHeader.setVisibility(View.VISIBLE);
      holder.mRecommendTag1.setVisibility(View.GONE);
      holder.mRecommendTag2.setVisibility(View.GONE);
      holder.mRecommendTag3.setVisibility(View.GONE);
      for (int i = 0; i < model.getTags().size(); i++) {
        if (holder.mTags.getChildAt(i) != null) {
          holder.mTags.getChildAt(i).setVisibility(View.VISIBLE);
          ((TextView) holder.mTags.getChildAt(i)).setText(model.getTags().get(i).getZh());
        }
      }
      holder.mLlHighLight.setVisibility(View.GONE);
      holder.mTvCircleName.setText(
          null != model.getAreaCircleName() ? model.getAreaCircleName() : "");

      holder.mVDivider1.setVisibility(View.VISIBLE);
      holder.mVDivider2.setVisibility(View.VISIBLE);

      if (TextUtils.isEmpty(String.valueOf(model.getLatitude())) || TextUtils.isEmpty(
          String.valueOf(model.getLongitude()))) {
        holder.mTvDistance.setVisibility(View.GONE);
        holder.mVDivider1.setVisibility(View.GONE);
      } else {
        holder.mTvDistance.setText(
            StringUtils.getDistance(SPUtils.getLatitude(), SPUtils.getLongitude(),
                Double.valueOf(model.getLatitude()), Double.valueOf(model.getLongitude())));
        holder.mTvDistance.setVisibility(View.VISIBLE);
      }
      if (!StringUtils.isEmpty(model.getPrice())) {
        holder.mTvPrice.setText("¥" + model.getPrice() + "元");
      } else {
        holder.mVDivider2.setVisibility(View.GONE);
        holder.mTvPrice.setText("");
      }
      holder.mIvFavorite.setVisibility(View.GONE);

      holder.itemView.setTag(model);
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          RecommendModel model = (RecommendModel) v.getTag();
          if (model != null && mListener != null) {
            mListener.onClick(position, model, ITEM_TYPE_RESULT);
          }
        }
      });
    }
  }

  @Override
  public int getItemViewType(int position) {
    if (mIsResult) {
      return ITEM_TYPE_RESULT;
    }

    return mIsCategory ? ITEM_TYPE_CATEGORY : ITEM_TYPE_HISTORY;
  }

  @Override
  public int getItemCount() {
    if (mIsResult) {
      return mResultList != null ? mResultList.size() : 0;
    }

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

  /**
   * 搜索结果
   */
  public class ResultViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.sdvCover) SimpleDraweeView mSdvCover;
    @Bind(R.id.tvHeader) TextView mTvHeader;
    @Bind(R.id.llHighLight) LinearLayout mLlHighLight;
    @Bind(R.id.ivFavorite) ImageView mIvFavorite;
    @Bind(R.id.tvTitle) TextView mTvTitle;
    @Bind(R.id.tvCircleName) TextView mTvCircleName;
    @Bind(R.id.vDivider1) View mVDivider1;
    @Bind(R.id.tvDistance) TextView mTvDistance;
    @Bind(vDivider2) View mVDivider2;
    @Bind(R.id.tvPrice) TextView mTvPrice;
    @Bind(R.id.recommend_tag1) TextView mRecommendTag1;
    @Bind(R.id.recommend_tag2) TextView mRecommendTag2;
    @Bind(R.id.recommend_tag3) TextView mRecommendTag3;
    @Bind(R.id.tags) LinearLayout mTags;

    public ResultViewHolder(View itemView) {
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
