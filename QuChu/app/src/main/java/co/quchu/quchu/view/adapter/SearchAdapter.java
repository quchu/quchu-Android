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

import co.quchu.quchu.model.SceneHeaderModel;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.model.SearchCategoryBean;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.widget.TagCloudView;

/**
 * User: Chenhs
 * Date: 2015-12-08
 * 趣处推荐 适配器 adapter
 */
public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private ArrayList<RecommendModel> resultList;
  private ArrayList<SearchCategoryBean> categoryList;

  public static final int ITEM_TYPE_CATEGORY = -1;
  public static final int ITEM_TYPE_RESULT = -2;

  private boolean isCategory = true;

  public void setCategory(boolean category) {
    isCategory = category;
  }

  public void setCategoryList(ArrayList<SearchCategoryBean> categoryList) {
    this.categoryList = categoryList;
    notifyDataSetChanged();
  }

  public boolean isCategory() {
    return isCategory;
  }

  @Override public int getItemViewType(int position) {
    return isCategory ? ITEM_TYPE_CATEGORY : ITEM_TYPE_RESULT;
  }

  public void changeDataSet(ArrayList<RecommendModel> arrayList) {
    this.resultList = arrayList;
    notifyDataSetChanged();
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == ITEM_TYPE_RESULT) {
      return new ResultHolder(LayoutInflater.from(parent.getContext())
          .inflate(R.layout.item_scene_detail_recommeded, parent, false));
    }
    return new CategoryViewHold(
        View.inflate(parent.getContext(), R.layout.item_search_category, null));
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    mListener = listener;
  }

  @Override public void onBindViewHolder(final RecyclerView.ViewHolder holde, int position) {
    if (getItemViewType(position) == ITEM_TYPE_RESULT) {
      ResultHolder holder = (ResultHolder) holde;
      final int finalPosition = position - 1;
      final RecommendModel model = resultList.get(position);

      if (null != model.getCover()) {
        holder.sdvCover.setImageURI(Uri.parse(model.getCover()));
      }
      holder.tvTitle.setText(model.getName());
      holder.tvHeader.setText(model.getDescribe());
      holder.tvHeader.setVisibility(View.VISIBLE);
      holder.recommendTag1.setVisibility(View.GONE);
      holder.recommendTag2.setVisibility(View.GONE);
      holder.recommendTag3.setVisibility(View.GONE);
      for (int i = 0; i < model.getTags().size(); i++) {
        if (holder.tags.getChildAt(i) != null) {
          holder.tags.getChildAt(i).setVisibility(View.VISIBLE);
          ((TextView) holder.tags.getChildAt(i)).setText(model.getTags().get(i).getZh());
        }
      }
      holder.llHighLight.setVisibility(View.GONE);
      holder.tvCircleName.setText(
          null != model.getAreaCircleName() ? model.getAreaCircleName() : "");

      ((ResultHolder) holde).vDivider1.setVisibility(View.VISIBLE);
      ((ResultHolder) holde).vDivider2.setVisibility(View.VISIBLE);

      if (TextUtils.isEmpty(String.valueOf(model.getLatitude())) || TextUtils.isEmpty(
          String.valueOf(model.getLongitude()))) {
        holder.tvDistance.setVisibility(View.GONE);
        ((ResultHolder) holde).vDivider1.setVisibility(View.GONE);
      } else {
        holder.tvDistance.setText(
            StringUtils.getDistance(SPUtils.getLatitude(), SPUtils.getLongitude(),
                Double.valueOf(model.getLatitude()), Double.valueOf(model.getLongitude())));
        holder.tvDistance.setVisibility(View.VISIBLE);
      }
      if (!StringUtils.isEmpty(model.getPrice())) {
        holder.tvPrice.setText("¥" + model.getPrice() + "元");
      } else {
        ((ResultHolder) holde).vDivider2.setVisibility(View.GONE);
        holder.tvPrice.setText("");
      }
      holder.ivFavorite.setVisibility(View.GONE);

      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          if (null != mListener) {
            mListener.onClick(holde.getAdapterPosition(), model, ITEM_TYPE_RESULT);
          }
        }
      });
    } else {
      CategoryViewHold holder = (CategoryViewHold) holde;
      final SearchCategoryBean bean = categoryList.get(position);
      ((CategoryViewHold) holde).searchItemCategoryName.setText(bean.getZh());
      if (!TextUtils.isEmpty(bean.getIconUrl())) {
        holder.simpleDraweeView.setImageURI(Uri.parse(bean.getIconUrl()));
      }
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          mListener.onClick(holde.getAdapterPosition(), bean, ITEM_TYPE_CATEGORY);
        }
      });
    }
  }

  private OnItemClickListener mListener;

  public interface OnItemClickListener {
    void onClick(int position, Parcelable bean, int itemYype);
  }

  @Override public int getItemCount() {
    return isCategory ? categoryList == null ? 0 : categoryList.size()
        : resultList == null ? 0 : resultList.size();
  }

  class ResultHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.sdvCover) SimpleDraweeView sdvCover;
    @Bind(R.id.tvTitle) TextView tvTitle;

    @Bind(R.id.tvHeader) TextView tvHeader;
    @Bind(R.id.recommend_tag1) TextView recommendTag1;
    @Bind(R.id.recommend_tag2) TextView recommendTag2;
    @Bind(R.id.recommend_tag3) TextView recommendTag3;
    @Bind(R.id.tags) LinearLayout tags;
    @Bind(R.id.tvCircleName) TextView tvCircleName;
    @Bind(R.id.tvDistance) TextView tvDistance;
    @Bind(R.id.tvPrice) TextView tvPrice;
    @Bind(R.id.ivFavorite) ImageView ivFavorite;
    @Bind(R.id.llHighLight) LinearLayout llHighLight;
    @Bind(R.id.vDivider1) View vDivider1;
    @Bind(R.id.vDivider2) View vDivider2;

    public ResultHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  class CategoryViewHold extends RecyclerView.ViewHolder {

    @Bind(R.id.simpleDraweeView) SimpleDraweeView simpleDraweeView;
    @Bind(R.id.search_item_categoryName) TextView searchItemCategoryName;

    public CategoryViewHold(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
