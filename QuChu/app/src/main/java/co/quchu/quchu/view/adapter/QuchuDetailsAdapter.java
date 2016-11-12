package co.quchu.quchu.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import co.quchu.quchu.presenter.NearbyPresenter;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.view.activity.PlaceMapActivity;
import co.quchu.quchu.view.activity.QuchuListSpecifyTagActivity;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.CommentImageModel;
import co.quchu.quchu.model.CommentModel;
import co.quchu.quchu.model.DetailModel;
import co.quchu.quchu.model.ImageModel;
import co.quchu.quchu.model.QuchuDetailArticleModel;
import co.quchu.quchu.model.TagsModel;
import co.quchu.quchu.model.VisitedInfoModel;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.activity.PhotoViewActivity;
import co.quchu.quchu.view.activity.QuchuDetailsActivity;
import co.quchu.quchu.view.activity.WebViewActivity;
import co.quchu.quchu.widget.CircleIndicator;
import co.quchu.quchu.widget.TagCloudView;

/**
 * Created by admin on 2016/3/7.
 */
public class QuchuDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  protected static final int LAYOUT_TYPE_INTRO_IMAGE = 0x0001;
  protected static final int LAYOUT_TYPE_SIMPLE_INFO = 0x0002;
  protected static final int LAYOUT_TYPE_RATING_INFO = 0x0003;
  protected static final int LAYOUT_TYPE_PARTY_INFO = 0x00031;
  protected static final int LAYOUT_TYPE_ADDITIONAL_INFO = 0x0004;
  protected static final int LAYOUT_TYPE_MAP_INFO = 0x0005;
  protected static final int LAYOUT_TYPE_ARTICLE = 0x0006;
  protected static final int LAYOUT_TYPE_COMMENT = 0x0007;
  protected static final int LAYOUT_TYPE_MATCHED_TAGS = 0x008;
  protected static final int LAYOUT_TYPE_NEARBY = 0x009;

  private LayoutInflater mLayoutInflater;
  private Activity mAnchorActivity;
  private DetailModel mData;
  private List<ImageModel> mImageSet = new ArrayList<>();

  protected static final int VIEW_TYPES[] = new int[] {
      LAYOUT_TYPE_INTRO_IMAGE, LAYOUT_TYPE_SIMPLE_INFO, LAYOUT_TYPE_RATING_INFO,
      LAYOUT_TYPE_ADDITIONAL_INFO, LAYOUT_TYPE_ARTICLE, LAYOUT_TYPE_MAP_INFO, LAYOUT_TYPE_COMMENT,
      LAYOUT_TYPE_MATCHED_TAGS
  };

  protected static final int VIEW_TYPES_PARTY[] = new int[] {
      LAYOUT_TYPE_INTRO_IMAGE, LAYOUT_TYPE_SIMPLE_INFO, LAYOUT_TYPE_PARTY_INFO, LAYOUT_TYPE_ARTICLE,
      LAYOUT_TYPE_MAP_INFO, LAYOUT_TYPE_COMMENT, LAYOUT_TYPE_MATCHED_TAGS
  };

  public QuchuDetailsAdapter(Activity activity, DetailModel dModel) {
    mAnchorActivity = activity;
    mData = dModel;
    mLayoutInflater = LayoutInflater.from(activity);
  }

  public void updateVisitedUsers() {
    notifyDataSetChanged();
  }

  public void updateGallery(List<ImageModel> mData) {
    if (null != mData && mData.size() > 0) {
      mImageSet.clear();
      mImageSet.addAll(mData);
    }
  }

  private int getBasicChildCount() {
    if (mData == null) {
      return 0;
    }
    return !mData.isIsActivity() ? VIEW_TYPES.length : VIEW_TYPES_PARTY.length;
  }

  @Override public int getItemViewType(int position) {

    if (!mData.isIsActivity()) {
      return position < VIEW_TYPES.length ? VIEW_TYPES[position] : LAYOUT_TYPE_NEARBY;
    } else {
      return position < VIEW_TYPES_PARTY.length ? VIEW_TYPES_PARTY[position] : LAYOUT_TYPE_NEARBY;
    }
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    switch (viewType) {
      case LAYOUT_TYPE_INTRO_IMAGE:
        return new IntroImageViewHolder(
            mLayoutInflater.inflate(R.layout.item_quchu_detail_image, parent, false));

      case LAYOUT_TYPE_SIMPLE_INFO:
        return new SimpleInfoViewHolder(
            mLayoutInflater.inflate(R.layout.item_quchu_detail_simple_info, parent, false));

      case LAYOUT_TYPE_RATING_INFO:
        return new RatingInfoViewHolder(
            mLayoutInflater.inflate(R.layout.item_quchu_detail_rating_info, parent, false));

      case LAYOUT_TYPE_PARTY_INFO:
        return new PartyInfoViewHolder(
            mLayoutInflater.inflate(R.layout.item_quchu_detail_party_info, parent, false));

      case LAYOUT_TYPE_ADDITIONAL_INFO:
        return new AdditionalInfoViewHolder(
            mLayoutInflater.inflate(R.layout.item_quchu_detail_additional_info, parent, false));
      case LAYOUT_TYPE_MAP_INFO:
        return new MapViewHolder(
            mLayoutInflater.inflate(R.layout.item_quchu_detail_map_info, parent, false));

      case LAYOUT_TYPE_ARTICLE:
        if (null != mData.getReviewList()) {
          return new ArticleViewHolder(
              mLayoutInflater.inflate(R.layout.item_quchu_detail_article, parent, false));
        } else {
          return new BlankViewHolder(
              mLayoutInflater.inflate(R.layout.item_quchu_detail_blank, parent, false));
        }
      case LAYOUT_TYPE_COMMENT:
        if (null != mData.getReviewList() && mData.getReviewList().size() > 0) {
          return new CommentViewHolder(
              mLayoutInflater.inflate(R.layout.item_quchu_detail_comment, parent, false));
        } else {
          return new BlankViewHolder(
              mLayoutInflater.inflate(R.layout.item_quchu_detail_blank, parent, false));
        }
      case LAYOUT_TYPE_MATCHED_TAGS:
        return new MatchedTagsViewHolder(
            mLayoutInflater.inflate(R.layout.item_quchu_detail_matched_tags, parent, false));

      case LAYOUT_TYPE_NEARBY:
        if (null != mData.getNearPlace()) {
          return new NearbyViewHolder(
              mLayoutInflater.inflate(R.layout.item_nearby_quchu_detail, parent, false));
        } else {
          return new BlankViewHolder(
              mLayoutInflater.inflate(R.layout.item_quchu_detail_blank, parent, false));
        }

      default:
        return new BlankViewHolder(
            mLayoutInflater.inflate(R.layout.item_quchu_detail_blank, parent, false));
    }
  }

  @Override public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

    if (holder instanceof IntroImageViewHolder) {

      if (null != mImageSet && mImageSet.size() > 0) {

        GalleryAdapter adapter = new GalleryAdapter(mImageSet);
        ((IntroImageViewHolder) holder).vpGallery.setAdapter(adapter);
        ((IntroImageViewHolder) holder).siv.setViewPager(((IntroImageViewHolder) holder).vpGallery);

        adapter.setListener(new GalleryAdapter.OnGalleryItemClickListener() {
          @Override public void onClick(int position) {
            PhotoViewActivity.enterActivity(mAnchorActivity, position, mImageSet);
          }
        });
        holder.itemView.invalidate();
      }
    } else if (holder instanceof SimpleInfoViewHolder) {

      ((SimpleInfoViewHolder) holder).detail_store_name_tv.setText(
          null != mData.getName() ? mData.getName().trim() : "");
      String tagsString = "";

      if (!TextUtils.isEmpty(mData.getDescribe())) {
        ((SimpleInfoViewHolder) holder).tvDesc.setText(mData.getDescribe());
      } else {
        ((SimpleInfoViewHolder) holder).tvDesc.setText("- 还没有简介");
      }
      if (null != mData.getTags()) {
        for (int i = 0; i < mData.getTags().size(); i++) {
          tagsString += mData.getTags().get(i).getZh();
          tagsString += (i + 1) < mData.getTags().size() ? " | " : "";
        }
      }

      String distanceStr = "";
      if (null!=mData.getLatitude() && null!=mData.getLongitude()){

        distanceStr += StringUtils.getDistance(SPUtils.getLatitude(),SPUtils.getLongitude(),Double.valueOf(mData.getLatitude()),Double.valueOf(mData.getLongitude()));

      }
      tagsString += " | ¥ "+mData.getPrice()+" | "+distanceStr;

      ((SimpleInfoViewHolder) holder).tvTags.setText(tagsString);



    } else if (holder instanceof RatingInfoViewHolder) {


      ((RatingInfoViewHolder) holder).rbRating.setProgress((int) mData.getSuggest());
      if (mData.getSuggest() == mData.getRecentSuggest()){
        ((RatingInfoViewHolder) holder).ivTrend.setVisibility(View.GONE);
      }
      if (mData.getRecentSuggest()-mData.getSuggest()<0){
        ((RatingInfoViewHolder) holder).ivTrend.setImageResource(R.mipmap.ic_rate_fall);
        ((RatingInfoViewHolder) holder).tvRatingChange.setTextColor(Color.parseColor("#00b38a"));
      }else{
        ((RatingInfoViewHolder) holder).ivTrend.setImageResource(R.mipmap.ic_rate_rise);
        ((RatingInfoViewHolder) holder).tvRatingChange.setTextColor(Color.parseColor("#ff4242"));
      }

      ((RatingInfoViewHolder) holder).tvRatingCount.setText(mData.getSuggest()+"分");
      ((RatingInfoViewHolder) holder).tvRatingChange.setText(String.valueOf(Math.abs(mData.getSuggest()-mData.getRecentSuggest()))+"分");

    } else if (holder instanceof PartyInfoViewHolder) {

      ((PartyInfoViewHolder) holder).detail_activity_info_ll.setVisibility(
          mData.isIsActivity() ? View.VISIBLE : View.GONE);
      ((PartyInfoViewHolder) holder).detail_activity_info_tv.setText(
          mData.isIsActivity() && null != mData.getActivityInfo() ? mData.getActivityInfo() : "");
    } else if (holder instanceof AdditionalInfoViewHolder) {

      ((AdditionalInfoViewHolder) holder).ivDeliver.setImageResource(StringUtils.isEmpty(mData.getTakeoutUrl())?R.mipmap.ic_nowaimai:R.mipmap.ic_waimai);
      ((AdditionalInfoViewHolder) holder).ivOrder.setImageResource(StringUtils.isEmpty(mData.getNet())?R.mipmap.ic_noyuding:R.mipmap.ic_yuding);
      ((AdditionalInfoViewHolder) holder).ivQueue.setImageResource(StringUtils.isEmpty(mData.getLineUrl())?R.mipmap.ic_nopaiwei:R.mipmap.ic_paiwei);
      ((AdditionalInfoViewHolder) holder).ivTel.setImageResource(StringUtils.isEmpty(mData.getTel())?R.mipmap.ic_nodianhua:R.mipmap.ic_dianhua);

    } else if (holder instanceof MapViewHolder) {

      ((MapViewHolder) holder).tvAddress.setText(mData.getAddress());
      /**
       *  Learn More -> http://developer.baidu.com/map/static-1.htm
       */
      ((MapViewHolder) holder).ivMap.setImageURI(Uri.parse(
          "http://api.map.baidu.com/staticimage?center="
              + mData.getLongitude()
              + ","
              + mData.getLatitude()
              + "&width="
              + 512
              + "&height="
              + 256
              + "&zoom=18&scale=2"));
      ((MapViewHolder) holder).ivMap.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          Intent mapIntent = new Intent(mAnchorActivity, PlaceMapActivity.class);
          mapIntent.putExtra("pid", mData.getPid());
          mapIntent.putExtra("lat", mData.getLatitude());
          mapIntent.putExtra("lon", mData.getLongitude());
          mapIntent.putExtra("gdlon", mData.gdLongitude);
          mapIntent.putExtra("gdlat", mData.gdLatitude);
          mapIntent.putExtra("title", mData.getName());
          mapIntent.putExtra("entity", mData.convert2NearbyMapItem());
          mapIntent.putExtra("placeAddress", mData.getAddress());
          mAnchorActivity.startActivity(mapIntent);
        }
      });
    } else if (holder instanceof ArticleViewHolder) {
      if (null != mData.getArticleList()) {
        int articleIndex = 0;
        List<QuchuDetailArticleModel> articleModelList = mData.getArticleList();
        QuchuDetailArticleModel article = null;
        if (articleModelList != null && articleModelList.size() > 0) {
          article = articleModelList.get(articleIndex);
        }

        if (article == null) {
          return;
        }

        ((ArticleViewHolder) holder).sdvAuthor.setImageURI(Uri.parse(article.getUserPhoto()));
        ((ArticleViewHolder) holder).tvTitle.setText(String.valueOf(article.getUserName()));
        ((ArticleViewHolder) holder).tvSubTitle.setText(String.valueOf(article.getTitle()));
        if (null != article.getUrl()) {

          final QuchuDetailArticleModel finalArticle = article;
          holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
              WebViewActivity.enterActivity(mAnchorActivity, finalArticle.getUrl(), finalArticle.getTitle(),
                  false);
            }
          });
        }
      }
    } else if (holder instanceof CommentViewHolder) {
      //((CommentViewHolder) holder).vDivider.setVisibility(View.GONE);
      if (null != mData.getReviewList()) {
        int commentIndex = 0;
        final CommentModel commentModel = mData.getReviewList().get(commentIndex);

        ((CommentViewHolder) holder).rvImages.setLayoutManager(
            new GridLayoutManager(mAnchorActivity, 4));
        CommentImageAdapter adapter = new CommentImageAdapter(commentModel.getImageList());
        if (null != commentModel && null != commentModel.getImageList()) {

          adapter.setOnItemClickListener(new CommonItemClickListener() {
            @Override public void onItemClick(View v, int position) {
              List<ImageModel> imageModels = new ArrayList<>();
              for (int i = 0; i < commentModel.getImageList().size(); i++) {
                ImageModel imageModel = new ImageModel();
                imageModel.setWidth(commentModel.getImageList().get(i).getWidth());
                imageModel.setHeight(commentModel.getImageList().get(i).getHeight());
                imageModel.setPath(commentModel.getImageList().get(i).getPathStr());
                imageModels.add(imageModel);
              }
              PhotoViewActivity.enterActivity(mAnchorActivity, position, imageModels);
            }
          });
        }
        ((CommentViewHolder) holder).rvImages.setAdapter(adapter);
        ((CommentViewHolder) holder).tvUsername.setText(commentModel.getUserName());
        if (null != commentModel.getCreateDate()) {
          ((CommentViewHolder) holder).tvDate.setText(
              commentModel.getCreateDate().substring(0, 10));
        } else {
          ((CommentViewHolder) holder).tvDate.setText("-");
        }
        ((CommentViewHolder) holder).tvFrom.setText(commentModel.getSourceContent());
        if (!StringUtils.isEmpty(commentModel.getPqUrl())) {
          ((CommentViewHolder) holder).tvFrom.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
              WebViewActivity.enterActivity(mAnchorActivity, commentModel.getPqUrl(), "查看评论",
                  false);
            }
          });
        }

        final boolean collapsed = mData.getReviewList().get(commentIndex).isCollapsed();

        ((CommentViewHolder) holder).tvUserComment.setText(commentModel.getContent());

        if (collapsed) {
          ((CommentViewHolder) holder).tvCollapse.setText("展开");
          ((CommentViewHolder) holder).tvUserComment.setMaxLines(4);
        } else {
          ((CommentViewHolder) holder).tvCollapse.setText("收起");
          ((CommentViewHolder) holder).tvUserComment.setMaxLines(Integer.MAX_VALUE);
        }
        final int finalCommentIndex = commentIndex;
        ((CommentViewHolder) holder).tvUserComment.post(new Runnable() {
          @Override public void run() {
            if (((CommentViewHolder) holder).tvUserComment.getLineCount() > 4) {
              ((CommentViewHolder) holder).tvCollapse.setVisibility(View.VISIBLE);
            } else {
              ((CommentViewHolder) holder).tvCollapse.setVisibility(View.GONE);
            }
          }
        });

        ((CommentViewHolder) holder).tvCollapse.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            mData.getReviewList().get(finalCommentIndex).setCollapsed(!collapsed);
            notifyDataSetChanged();
          }
        });
        ((CommentViewHolder) holder).sdvAvatar.setImageURI(
            Uri.parse(commentModel.getUserPhoneUrl()));
        if (null != commentModel.getSourceUrl()) {
          ((CommentViewHolder) holder).ivFrom.setImageURI(Uri.parse(commentModel.getSourceUrl()));
        }
        ((CommentViewHolder) holder).tvRatingCount.setVisibility(View.VISIBLE);

      }
    } else if (holder instanceof MatchedTagsViewHolder) {

      final List<MatchedTagModel> tags = new ArrayList<>();

      if (null != mData.getAreaMap()) {
        MatchedTagModel areaTag = new MatchedTagModel();
        areaTag.setType(NearbyPresenter.TYPE_AREA);
        areaTag.setId(mData.getAreaMap().getId());
        areaTag.setZh(mData.getAreaMap().getName());
        tags.add(areaTag);
      }

      if (null != mData.getCircleMap()) {
        MatchedTagModel circleTag = new MatchedTagModel();
        circleTag.setType(NearbyPresenter.TYPE_CIRCLE);
        circleTag.setId(mData.getCircleMap().getId());
        circleTag.setZh(mData.getCircleMap().getName());
        tags.add(circleTag);
      }

      for (int i = 0; i < mData.getTags().size(); i++) {
        MatchedTagModel model = new MatchedTagModel();
        model.setZh(mData.getTags().get(i).getZh());
        model.setId(mData.getTags().get(i).getId());
        model.setType(-1);
        tags.add(model);
      }
      MatchedTagAdapter adapter = new MatchedTagAdapter(tags);
      ((MatchedTagsViewHolder) holder).rvMatchedTags.setAdapter(adapter);
      ((MatchedTagsViewHolder) holder).rvMatchedTags.setLayoutManager(new GridLayoutManager(mAnchorActivity,4));
      adapter.setOnItemClickListener(new CommonItemClickListener() {
        @Override public void onItemClick(View v, int position) {

          MatchedTagModel tag = tags.get(position);
          Intent intent = new Intent(mAnchorActivity, QuchuListSpecifyTagActivity.class);
          intent.putExtra(QuchuListSpecifyTagActivity.BUNDLE_KEY_TAG_ID, tag.getId());

          if (tag.getType()==NearbyPresenter.TYPE_CIRCLE){
            intent.putExtra(QuchuListSpecifyTagActivity.BUNDLE_KEY_DATA_TYPE, NearbyPresenter.TYPE_CIRCLE);
          }else if(tag.getType()==NearbyPresenter.TYPE_AREA){
            intent.putExtra(QuchuListSpecifyTagActivity.BUNDLE_KEY_DATA_TYPE, NearbyPresenter.TYPE_AREA);
          }
          intent.putExtra(QuchuListSpecifyTagActivity.BUNDLE_KEY_TAG_NAME, tag.getZh());

          mAnchorActivity.startActivity(intent);

        }
      });

    } else if (holder instanceof NearbyViewHolder) {
      if (null != mData.getNearPlace()) {
        int imgIndex = position - getBasicChildCount();

        ((NearbyViewHolder) holder).tvName.setText(mData.getNearPlace().get(imgIndex).getName());
        List<String> strTags = new ArrayList<>();
        List<TagsModel> tags = mData.getNearPlace().get(imgIndex).getTags();
        if (null != tags && tags.size() > 0) {
          for (int i = 0; i < Math.min(tags.size(), 3); i++) {
            strTags.add(" " + tags.get(i).getZh() + " ");
          }
        }

        ((NearbyViewHolder) holder).tcvTag.setTags(strTags);
        ((NearbyViewHolder) holder).address.setText(
            mData.getNearPlace().get(imgIndex).getAddress());
        ((NearbyViewHolder) holder).address.setVisibility(View.VISIBLE);
        ((NearbyViewHolder) holder).sdvImage.setImageURI(
            Uri.parse(mData.getNearPlace().get(imgIndex).getCover()));
        final int pid = mData.getNearPlace().get(imgIndex).getPlaceId();
        ((NearbyViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {

            Intent intent = new Intent(mAnchorActivity, QuchuDetailsActivity.class);
            intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_FROM,
                QuchuDetailsActivity.FROM_TYPE_RECOM);
            intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, pid);
            mAnchorActivity.startActivity(intent);
          }
        });
      }
    }
  }

  @Override public int getItemCount() {
    int count = getBasicChildCount();
    if (null != mData && null != mData.getNearPlace()) {
      count += mData.getNearPlace().size();
    }
    return count;
  }

  // INTRO_IMAGE , SIMPLE_INFO , RATING_INFO , PARTY_INFO , ADDITIONAL_INFO

  public static class IntroImageViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.vpGallery) ViewPager vpGallery;
    @Bind(R.id.siv) CircleIndicator siv;

    IntroImageViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  public static class SimpleInfoViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.detail_store_name_tv) TextView detail_store_name_tv;
    @Bind(R.id.tvDesc) TextView tvDesc;
    @Bind(R.id.tvTags) TextView tvTags;

    SimpleInfoViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  public static class RatingInfoViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.rbRating) RatingBar rbRating;
    @Bind(R.id.tvRatingCount) TextView tvRatingCount;
    @Bind(R.id.tvRatingChange) TextView tvRatingChange;
    @Bind(R.id.ivTrend) ImageView ivTrend;

    RatingInfoViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  public static class PartyInfoViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.detail_activity_info_ll) LinearLayout detail_activity_info_ll;
    @Bind(R.id.detail_activity_title_tv) TextView detail_activity_title_tv;
    @Bind(R.id.detail_activity_info_tv) TextView detail_activity_info_tv;

    PartyInfoViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  public static class AdditionalInfoViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.ivOrder) ImageView ivOrder;
    @Bind(R.id.ivDeliver) ImageView ivDeliver;
    @Bind(R.id.ivTel) ImageView ivTel;
    @Bind(R.id.ivQueue) ImageView ivQueue;

    AdditionalInfoViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  // MAP_INFO , ARTICLE , COMMENT , MATCHED_TAGS , NEARBY

  public static class MapViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.ivMap) SimpleDraweeView ivMap;
    @Bind(R.id.tvAddress) TextView tvAddress;

    MapViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  public static class ArticleViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.sdvAuthor) SimpleDraweeView sdvAuthor;
    @Bind(R.id.tvTitle) TextView tvTitle;
    @Bind(R.id.tvSubTitle) TextView tvSubTitle;

    ArticleViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  public static class CommentViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.sdvAvatar) SimpleDraweeView sdvAvatar;
    @Bind(R.id.tvUsername) TextView tvUsername;
    @Bind(R.id.tvDate) TextView tvDate;
    @Bind(R.id.tvUserComment) TextView tvUserComment;
    @Bind(R.id.tvCollapse) TextView tvCollapse;
    @Bind(R.id.ivFrom) SimpleDraweeView ivFrom;
    @Bind(R.id.tvFrom) TextView tvFrom;
    @Bind(R.id.rvImages) RecyclerView rvImages;
    //@Bind(R.id.vDivider) View vDivider;
    @Bind(R.id.tvRatingCount) TextView tvRatingCount;

    CommentViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  public class CommentImageAdapter extends RecyclerView.Adapter<CommentImageAdapter.ViewHolder> {

    List<CommentImageModel> mImageSet;
    CommonItemClickListener mListener;

    public CommentImageAdapter(List<CommentImageModel> dataSet) {
      this.mImageSet = dataSet;
    }

    public void setOnItemClickListener(CommonItemClickListener listener) {
      mListener = listener;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new ViewHolder(LayoutInflater.from(parent.getContext())
          .inflate(R.layout.item_comment_image, parent, false));
    }

    @Override public void onBindViewHolder(ViewHolder holder, final int position) {
      holder.sdvImage.setImageURI(Uri.parse(mImageSet.get(position).getPathStr()));
      holder.sdvImage.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          if (null != mListener) {
            mListener.onItemClick(view, position);
          }
        }
      });
    }

    @Override public int getItemCount() {
      return null != mImageSet ? mImageSet.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
      @Bind(R.id.sdvImage) SimpleDraweeView sdvImage;

      public ViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
      }
    }
  }

  public static class MatchedTagsViewHolder extends RecyclerView.ViewHolder {
    //@Bind(R.id.tcvTags) TagCloudView tags;
    @Bind(R.id.rvMatchedTags) RecyclerView rvMatchedTags;

    MatchedTagsViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  public class MatchedTagAdapter extends RecyclerView.Adapter<MatchedTagAdapter.ViewHolder> {

    List<MatchedTagModel> tags;
    CommonItemClickListener mListener;

    public MatchedTagAdapter(List<MatchedTagModel> dataSet) {
      this.tags = dataSet;
    }

    public void setOnItemClickListener(CommonItemClickListener listener) {
      mListener = listener;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new ViewHolder(LayoutInflater.from(parent.getContext())
          .inflate(R.layout.item_quchu_detail_matched_tags_item, parent, false));
    }

    @Override public void onBindViewHolder(ViewHolder holder, final int position) {
      holder.tvName.setText(tags.get(position).zh);
      holder.tvName.setBackgroundResource(tags.get(position).getType()==NearbyPresenter.TYPE_CIRCLE||tags.get(position).getType()==NearbyPresenter.TYPE_AREA?R.drawable.shape_lineframe_yellow_fill:R.drawable.shape_lineframe_black_thin_rect);
      holder.tvName.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          if (null != mListener) {
            mListener.onItemClick(view, position);
          }
        }
      });
    }

    @Override public int getItemCount() {
      return null != tags ? tags.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
      @Bind(R.id.tvName) TextView tvName;

      public ViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
      }
    }
  }

  public static class NearbyViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.desc) TextView tvName;
    @Bind(R.id.tag) TagCloudView tcvTag;
    @Bind(R.id.simpleDraweeView) SimpleDraweeView sdvImage;
    @Bind(R.id.address) TextView address;

    NearbyViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  public static class BlankViewHolder extends RecyclerView.ViewHolder {
    BlankViewHolder(View view) {
      super(view);
    }
  }

  private class MatchedTagModel {
    private String zh;
    private int type;
    private int id;

    public String getZh() {
      return zh;
    }

    public void setZh(String zh) {
      this.zh = zh;
    }

    public int getType() {
      return type;
    }

    public void setType(int type) {
      this.type = type;
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }
  }
}
