package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.text.TextUtilsCompat;
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
import co.quchu.quchu.model.DetailModel;
import co.quchu.quchu.model.SceneHeaderModel;
import co.quchu.quchu.model.SceneInfoModel;
import co.quchu.quchu.model.SimpleArticleModel;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.widget.CornerLabelTextView;

/**
 * Created by Nico on 16/7/8.
 */
public class SceneDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private Context mContext;

  private OnSceneItemClickListener mListener;

  public static final int TYPE_INFO = 0x001;
  public static final int TYPE_RECOMMENDED = 0x002;
  public static final int TYPE_ARTICLE = 0x003;
  public static final int TYPE_PLACE_LIST = 0x004;
  public static final int TYPE_EMPTY = 0x005;
  public static final int TYPE_PAGE_END = 0x006;

  private List<DetailModel> mData;
  private List<SceneHeaderModel> mBestPlace;
  private SimpleArticleModel mArticleModel;
  private SceneInfoModel mSceneInfoModel;

  public void updateFavorite(int index, boolean status, boolean fromRec) {
    System.out.println("place " + index + " | " + status + " | " + fromRec);
    if (fromRec) {
      if (mBestPlace.size() > index) {
        System.out.println("rec place changed");
        mBestPlace.get(index).getPlaceInfo().setIsf(status);
      }
    } else {
      if (mData.size() > index) {
        System.out.println("place changed");
        mData.get(index).setIsf(status);
      }
    }
    notifyDataSetChanged();
  }

  private boolean mShowingNoData = false;

  public void showPageEnd(boolean bl) {
    mShowingNoData = bl;
    notifyDataSetChanged();
  }

  public SceneDetailAdapter(Context mContext, List<DetailModel> pData,
      List<SceneHeaderModel> pDataBanner, SimpleArticleModel articleModel, SceneInfoModel sceneInfo,
      OnSceneItemClickListener listener) {
    this.mContext = mContext;
    this.mData = pData;
    this.mBestPlace = pDataBanner;
    this.mListener = listener;
    this.mSceneInfoModel = sceneInfo;
    this.mArticleModel = articleModel;
  }

  @Override public int getItemViewType(int position) {
    if (position == 0) {
      return TYPE_INFO;
    } else if (position > 0 && position <= getRecommendedListSize()) {
      return TYPE_RECOMMENDED;
    } else if (position > getRecommendedListSize() && position <= getRecommendedListSize() + 1) {
      if (null == mArticleModel) {
        return TYPE_EMPTY;
      } else {
        return TYPE_ARTICLE;
      }
    } else if (position > getRecommendedListSize() + 1
        && position < getRecommendedListSize() + getPlaceListSize() + 2) {
      return TYPE_PLACE_LIST;
    } else {
      return TYPE_PAGE_END;
    }
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    switch (viewType) {
      case TYPE_INFO:
        return new InfoViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.item_scene_detail_info, parent, false));
      case TYPE_RECOMMENDED:
        return new RecommendedViewHolder(LayoutInflater.from(mContext)
            .inflate(R.layout.item_scene_detail_recommeded, parent, false));
      case TYPE_ARTICLE:
        return new ArticleViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.item_scene_detail, parent, false));
      case TYPE_PLACE_LIST:
        return new PlaceViewHolder(LayoutInflater.from(mContext)
            .inflate(R.layout.item_scene_detail_recommeded, parent, false));
      case TYPE_PAGE_END:
        return new PageEndViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.cp_page_end, parent, false));
      default:
        return new QuchuDetailsAdapter.BlankViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.item_quchu_detail_blank, parent, false));
    }
  }

  @Override public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
    if (null == mData) {
      return;
    }
    if (holder instanceof PageEndViewHolder) {
      ((PageEndViewHolder) holder).vDivider.setVisibility(View.VISIBLE);
    } else if (holder instanceof InfoViewHolder) {
      if (null != mSceneInfoModel) {

        Typeface face =
            Typeface.createFromAsset(((InfoViewHolder) holder).tvENTitle.getContext().getAssets(),
                "AGENCYFB.TTF");

        ((InfoViewHolder) holder).tvENTitle.setTypeface(face);
        ((InfoViewHolder) holder).tvENTitle.setText(mSceneInfoModel.getEn());
        ((InfoViewHolder) holder).tvCNTitle.setText(mSceneInfoModel.getSceneName());
        ((InfoViewHolder) holder).sdvCover.setImageURI(Uri.parse(mSceneInfoModel.getSceneCover()));
        ((InfoViewHolder) holder).desc.setText(mSceneInfoModel.getSceneName());
        ((InfoViewHolder) holder).tvDescription.setText(mSceneInfoModel.getSceneContent());
        String[] tags = mSceneInfoModel.getSceneTitle();
        ((InfoViewHolder) holder).recommendTag1.setVisibility(View.GONE);
        ((InfoViewHolder) holder).recommendTag2.setVisibility(View.GONE);
        ((InfoViewHolder) holder).recommendTag3.setVisibility(View.GONE);

        ((InfoViewHolder) holder).vDivider1.setVisibility(View.INVISIBLE);
        ((InfoViewHolder) holder).vDivider2.setVisibility(View.INVISIBLE);
        if (null != tags) {
          for (int i = 0; i < tags.length; i++) {
            switch (i) {
              case 0:
                ((InfoViewHolder) holder).recommendTag1.setVisibility(View.VISIBLE);
                ((InfoViewHolder) holder).recommendTag1.setText(tags[i]);
                break;
              case 1:
                ((InfoViewHolder) holder).recommendTag2.setVisibility(View.VISIBLE);
                ((InfoViewHolder) holder).recommendTag2.setText(tags[i]);
                ((InfoViewHolder) holder).vDivider1.setVisibility(View.VISIBLE);
                break;
              case 2:
                ((InfoViewHolder) holder).recommendTag3.setVisibility(View.VISIBLE);
                ((InfoViewHolder) holder).recommendTag3.setText(tags[i]);
                ((InfoViewHolder) holder).vDivider2.setVisibility(View.VISIBLE);
                break;
            }
          }
        }
      }
    } else if (holder instanceof RecommendedViewHolder) {

      final int finalPosition = position - 1;
      final SceneHeaderModel objScene = mBestPlace.get(finalPosition);
      if (null != objScene.getPlaceInfo() && null != objScene.getPlaceInfo().getCover()) {
        ((RecommendedViewHolder) holder).sdvCover.setImageURI(
            Uri.parse(objScene.getPlaceInfo().getCover()));
      }
      ((RecommendedViewHolder) holder).tvTitle.setText(objScene.getPlaceInfo().getName());
      ((RecommendedViewHolder) holder).tvHeader.setText(objScene.getTitle());
      ((RecommendedViewHolder) holder).tvHeader.setVisibility(View.VISIBLE);
      ((RecommendedViewHolder) holder).recommendTag1.setVisibility(View.GONE);
      ((RecommendedViewHolder) holder).recommendTag2.setVisibility(View.GONE);
      ((RecommendedViewHolder) holder).recommendTag3.setVisibility(View.GONE);
      for (int i = 0; i < objScene.getPlaceInfo().getTags().size(); i++) {
        if (((RecommendedViewHolder) holder).tags.getChildAt(i) != null) {
          ((RecommendedViewHolder) holder).tags.getChildAt(i).setVisibility(View.VISIBLE);
          ((TextView) ((RecommendedViewHolder) holder).tags.getChildAt(i)).setText(
              objScene.getPlaceInfo().getTags().get(i).getZh());
        }
      }
      ((RecommendedViewHolder) holder).llHighLight.setVisibility(View.VISIBLE);
      ((RecommendedViewHolder) holder).tvCircleName.setText(
          null != objScene.getPlaceInfo().getAreaCircleName() ? objScene.getPlaceInfo()
              .getAreaCircleName() : "");

      ((RecommendedViewHolder) holder).vDivider1.setVisibility(View.VISIBLE);
      ((RecommendedViewHolder) holder).vDivider2.setVisibility(View.VISIBLE);
      if (TextUtils.isEmpty(objScene.getPlaceInfo().getLatitude()) || TextUtils.isEmpty(
          objScene.getPlaceInfo().getLongitude())) {
        ((RecommendedViewHolder) holder).tvDistance.setVisibility(View.GONE);
        ((RecommendedViewHolder) holder).vDivider1.setVisibility(View.GONE);
      } else {
        ((RecommendedViewHolder) holder).tvDistance.setText(
            StringUtils.getDistance(SPUtils.getLatitude(), SPUtils.getLongitude(),
                Double.valueOf(objScene.getPlaceInfo().getLatitude()),
                Double.valueOf(objScene.getPlaceInfo().getLongitude())));
        ((RecommendedViewHolder) holder).tvDistance.setVisibility(View.VISIBLE);
      }
      if (!StringUtils.isEmpty(objScene.getPlaceInfo().getPrice())) {
        ((RecommendedViewHolder) holder).tvPrice.setText(
            "¥" + objScene.getPlaceInfo().getPrice() + "元");
      } else {
        ((RecommendedViewHolder) holder).tvPrice.setText("");
        ((SearchAdapter.ResultHolder) holder).vDivider2.setVisibility(View.GONE);
      }
      ((RecommendedViewHolder) holder).ivFavorite.setImageResource(
          objScene.getPlaceInfo().isIsf() ? R.mipmap.ic_xuanzhong : R.mipmap.ic_weishoucang);

      ((RecommendedViewHolder) holder).ivFavorite.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          if (null != mListener) {
            mListener.onFavoriteClick(objScene.getPlaceInfo().getPid(),
                objScene.getPlaceInfo().isIsf(), finalPosition, true);
          }
        }
      });
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          if (null != mListener) {
            mListener.onPlaceClick(objScene.getPlaceInfo().getPid(),
                objScene.getPlaceInfo().getName());
          }
        }
      });
    } else if (holder instanceof ArticleViewHolder) {

      if (null != mArticleModel) {
        ((ArticleViewHolder) holder).sdvCover.setImageURI(Uri.parse(mArticleModel.getImageUrl()));
        ((ArticleViewHolder) holder).tvTitle.setText(mArticleModel.getName());
        ((ArticleViewHolder) holder).tvLikes.setText(
            String.valueOf(mArticleModel.getFavoriteCount()));
        ((ArticleViewHolder) holder).tvReviews.setText(
            String.valueOf(mArticleModel.getReadCount()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View view) {
            if (null != mListener) {
              mListener.onArticleClick(mArticleModel.getArticleId(), mArticleModel.getName());
            }
          }
        });
      }
    } else if (holder instanceof PlaceViewHolder) {

      final int finalPosition = position - 2 - getRecommendedListSize();
      final DetailModel objScene = mData.get(finalPosition);
      ((PlaceViewHolder) holder).sdvCover.setImageURI(Uri.parse(objScene.getCover()));
      ((PlaceViewHolder) holder).tvTitle.setText(objScene.getName());
      ((PlaceViewHolder) holder).tvHeader.setVisibility(View.GONE);
      ((PlaceViewHolder) holder).recommendTag1.setVisibility(View.GONE);
      ((PlaceViewHolder) holder).recommendTag2.setVisibility(View.GONE);
      ((PlaceViewHolder) holder).recommendTag3.setVisibility(View.GONE);
      ((PlaceViewHolder) holder).llHighLight.setVisibility(View.GONE);

      for (int i = 0; i < objScene.getTags().size(); i++) {
        if (null != ((PlaceViewHolder) holder).tags.getChildAt(i)) {
          ((PlaceViewHolder) holder).tags.getChildAt(i).setVisibility(View.VISIBLE);
          ((TextView) ((PlaceViewHolder) holder).tags.getChildAt(i)).setText(
              objScene.getTags().get(i).getZh());
        }
      }
      ((PlaceViewHolder) holder).tvCircleName.setText(
          null != objScene.getAreaCircleName() ? objScene.getAreaCircleName() : "");
      if (TextUtils.isEmpty(objScene.getLatitude()) || TextUtils.isEmpty(objScene.getLongitude())) {
        ((PlaceViewHolder) holder).tvDistance.setVisibility(View.GONE);
      } else {
        ((PlaceViewHolder) holder).tvDistance.setText(
            StringUtils.getDistance(SPUtils.getLatitude(), SPUtils.getLongitude(),
                Double.valueOf(objScene.getLongitude()), Double.valueOf(objScene.gdLongitude)));
        ((PlaceViewHolder) holder).tvDistance.setVisibility(View.VISIBLE);
      }

      if (!StringUtils.isEmpty(objScene.getPrice())) {
        ((PlaceViewHolder) holder).tvPrice.setText("¥" + objScene.getPrice() + "元");
      } else {
        ((PlaceViewHolder) holder).tvPrice.setText("¥- 元");
      }

      ((PlaceViewHolder) holder).ivFavorite.setImageResource(
          objScene.isIsf() ? R.mipmap.ic_shoucang_yellow : R.mipmap.ic_weishoucang);
      ((PlaceViewHolder) holder).ivFavorite.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          if (null != mListener) {
            mListener.onFavoriteClick(objScene.getPid(), objScene.isIsf(), finalPosition, false);
          }
        }
      });

      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          if (null != mListener) {
            mListener.onPlaceClick(objScene.getPid(), objScene.getName());
          }
        }
      });
    }
  }

  public interface OnSceneItemClickListener {
    void onArticleClick(int aid, String articleTitle);

    void onFavoriteClick(int pid, boolean status, int index, boolean recommend);

    void onPlaceClick(int pid, String placeName);
  }

  private int getRecommendedListSize() {
    int recommended = 0;

    if (null != mData && null != mBestPlace) {

      recommended += mBestPlace.size();
    }
    return recommended;
  }

  private int getPlaceListSize() {
    int placeList = 0;
    if (null != mData && null != mData && null != mData) {
      placeList += mData.size();
    }
    return placeList;
  }

  @Override public int getItemCount() {
    int intro = 1;
    int article = 1;
    int placeSize = getPlaceListSize();

    return getRecommendedListSize() + intro + article + placeSize + (mShowingNoData ? 1 : 0);
  }

  public class InfoViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.sdvCover) SimpleDraweeView sdvCover;
    @Bind(R.id.desc) TextView desc;
    @Bind(R.id.recommend_tag1) TextView recommendTag1;
    @Bind(R.id.recommend_tag2) TextView recommendTag2;
    @Bind(R.id.recommend_tag3) TextView recommendTag3;
    @Bind(R.id.tags) LinearLayout tags;
    @Bind(R.id.tvDescription) TextView tvDescription;
    @Bind(R.id.llRoot) LinearLayout llRoot;
    @Bind(R.id.vHorizontalDivider1) View vDivider1;
    @Bind(R.id.vHorizontalDivider2) View vDivider2;
    @Bind(R.id.tvCNTitle) TextView tvCNTitle;
    @Bind(R.id.tvENTitle) TextView tvENTitle;

    public InfoViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  public class RecommendedViewHolder extends RecyclerView.ViewHolder {

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

    public RecommendedViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  public class ArticleViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.sdvCover) SimpleDraweeView sdvCover;
    @Bind(R.id.tvTitle) TextView tvTitle;
    @Bind(R.id.tvReviews) TextView tvReviews;
    @Bind(R.id.tvLikes) TextView tvLikes;

    public ArticleViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  public class PlaceViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.sdvCover) SimpleDraweeView sdvCover;
    @Bind(R.id.tvHeader) TextView tvHeader;
    @Bind(R.id.tvTitle) TextView tvTitle;
    @Bind(R.id.recommend_tag1) TextView recommendTag1;
    @Bind(R.id.recommend_tag2) TextView recommendTag2;
    @Bind(R.id.recommend_tag3) TextView recommendTag3;
    @Bind(R.id.tags) LinearLayout tags;
    @Bind(R.id.tvCircleName) TextView tvCircleName;
    @Bind(R.id.tvDistance) TextView tvDistance;
    @Bind(R.id.tvPrice) TextView tvPrice;
    @Bind(R.id.ivFavorite) ImageView ivFavorite;
    @Bind(R.id.llHighLight) LinearLayout llHighLight;

    public PlaceViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
