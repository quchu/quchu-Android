package co.quchu.quchu.view.adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.CommentImageModel;
import co.quchu.quchu.model.CommentModel;
import co.quchu.quchu.model.DetailModel;
import co.quchu.quchu.model.ImageModel;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.activity.PhotoViewActivity;
import co.quchu.quchu.view.activity.WebViewActivity;
import co.quchu.quchu.widget.SpacesItemDecoration;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nico on 16/8/26.
 */
public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private float mAvgRating = 0;
  private int mRatingCount = 0;
  private List<DetailModel.BizInfoModel> mBizList;
  private List<CommentModel> mDataSet;
  private List<String> mTagsList;

  private Activity mAnchorActivity;
  private boolean mShowingNoData = false;

  public static final int TYPE_HEADER = 0x001;
  public static final int TYPE_INFO = 0x002;
  public static final int TYPE_PAGE_END = 0x003;



  public void showPageEnd(boolean bl) {
    mShowingNoData = bl;
    notifyDataSetChanged();
  }

  public CommentAdapter(Activity activity, List<CommentModel> mDataSet,int ratingCount,float avgRating,List<DetailModel.BizInfoModel> bizList,List<String> tagList) {
    this.mDataSet = mDataSet;
    this.mAnchorActivity = activity;
    this.mTagsList = new ArrayList<>();
    this.mTagsList.addAll(tagList);
    this.mBizList = new ArrayList<>();
    this.mBizList.addAll(bizList);
    this.mAvgRating = avgRating;
    this.mRatingCount = ratingCount;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    switch (viewType) {
      case TYPE_HEADER:
        return new HeaderViewHolder(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_rating_detail_header, parent, false));
      case TYPE_INFO:
        return new CommentViewHolder(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_quchu_detail_comment, parent, false));
      default:
        return new PageEndViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.cp_page_end, parent, false));
    }
  }

  @Override public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

    if (position==0){
      ((HeaderViewHolder)holder).tvAvgRating.setText(String.valueOf(mAvgRating)+"分");
      ((HeaderViewHolder) holder).rbRating.setRating(mAvgRating);
      ((HeaderViewHolder) holder).tvRatingCount.setText(String.valueOf("全网"+mRatingCount+"人评价过"));

      if (null!=mTagsList){
        TagAdapter adapter = new TagAdapter(mTagsList);
        ((HeaderViewHolder) holder).rvTagList.setAdapter(adapter);
        ((HeaderViewHolder) holder).rvTagList.setLayoutManager(new GridLayoutManager(mAnchorActivity,4,GridLayoutManager.VERTICAL,false));
      }

      if (null!=mBizList){
        CommentSourceAdapter adapter = new CommentSourceAdapter(mBizList);
        ((HeaderViewHolder) holder).rvBizList.setAdapter(adapter);
        ((HeaderViewHolder) holder).rvBizList.setLayoutManager(new GridLayoutManager(mAnchorActivity,2,GridLayoutManager.VERTICAL,false));
      }


    }else{
      position-=1;

    if (holder instanceof CommentViewHolder) {
      final CommentModel commentModel = mDataSet.get(position);

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

      //((CommentViewHolder) holder).rbRating.setRating(commentModel.getScore());
      ((CommentViewHolder) holder).tvUsername.setText(commentModel.getUserName());
      if (null != commentModel.getCreateDate()) {
        ((CommentViewHolder) holder).tvDate.setText(commentModel.getCreateDate().substring(0, 10));
      } else {
        ((CommentViewHolder) holder).tvDate.setText("-");
      }
      ((CommentViewHolder) holder).tvFrom.setText(commentModel.getSourceContent());

      ((CommentViewHolder) holder).tvUserComment.setText(commentModel.getContent());

      final boolean collapsed = commentModel.isCollapsed();
      if (collapsed) {
        ((CommentViewHolder) holder).tvCollapse.setText("展开");
        ((CommentViewHolder) holder).tvUserComment.setMaxLines(4);
      } else {
        ((CommentViewHolder) holder).tvCollapse.setText("收起");
        ((CommentViewHolder) holder).tvUserComment.setMaxLines(Integer.MAX_VALUE);
      }
      final int finalCommentIndex = position;
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
          mDataSet.get(finalCommentIndex).setCollapsed(!collapsed);
          notifyDataSetChanged();
        }
      });
      ((CommentViewHolder) holder).sdvAvatar.setImageURI(Uri.parse(commentModel.getUserPhoneUrl()));
      if (null != commentModel.getSourceUrl()) {
        ((CommentViewHolder) holder).ivFrom.setImageURI(Uri.parse(commentModel.getSourceUrl()));
      }

      if (!StringUtils.isEmpty(commentModel.getPqUrl())) {
        ((CommentViewHolder) holder).tvFrom.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View view) {
            WebViewActivity.enterActivity(mAnchorActivity, commentModel.getPqUrl(), "查看评论", false);
          }
        });
      }

      //if (position==0 ){
      //  ((CommentViewHolder) holder).vDivider.setVisibility(View.GONE);
      //}
    }
    }
  }

  @Override public int getItemCount() {
    return null != mDataSet ? mDataSet.size() + (mShowingNoData ? 1 : 0)+1 : 0;
  }

  @Override public int getItemViewType(int position) {
    if (position==0){
      return TYPE_HEADER;
    }else{
      if (position < mDataSet.size()+1) {
        return TYPE_INFO;
      } else {
        return TYPE_PAGE_END;
      }
    }

  }

  public static class HeaderViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.rbRating) RatingBar rbRating;
    @Bind(R.id.tvAvgRating) TextView tvAvgRating;
    @Bind(R.id.tvRatingCount) TextView tvRatingCount;
    @Bind(R.id.rvBizList) RecyclerView rvBizList;
    @Bind(R.id.rvTagList) RecyclerView rvTagList;

    HeaderViewHolder(View v) {
      super(v);
      ButterKnife.bind(this, v);
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
    //@Bind(R.id.rbRating) RatingBar rbRating;
    @Bind(R.id.rvImages) RecyclerView rvImages;
    //@Bind(R.id.vDivider) View vDivider;

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


  public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

    List<String> tags;

    public TagAdapter(List<String> dataSet) {
      this.tags = dataSet;
    }


    @Override public TagAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new TagAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
          .inflate(R.layout.item_quchu_detail_matched_tags_item, parent, false));
    }

    @Override public void onBindViewHolder(TagAdapter.ViewHolder holder, final int position) {
      holder.tvName.setText(tags.get(position));
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

  public class CommentSourceAdapter extends RecyclerView.Adapter<CommentSourceAdapter.ViewHolder> {

    List<DetailModel.BizInfoModel> bizList;

    public CommentSourceAdapter(List<DetailModel.BizInfoModel> dataSet) {
      this.bizList = dataSet;
    }


    @Override public CommentSourceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new CommentSourceAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
          .inflate(R.layout.item_comment_header_data_source, parent, false));
    }

    @Override public void onBindViewHolder(CommentSourceAdapter.ViewHolder holder, final int position) {
      holder.tvFrom.setText(bizList.get(position).getSourceName()+" : "+ bizList.get(position).getScore()+" 分");
      holder.ivFrom.setImageURI(Uri.parse(bizList.get(position).getSourceImgUrl()));

    }

    @Override public int getItemCount() {
      return null != bizList ? bizList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
      @Bind(R.id.tvFrom) TextView tvFrom;
      @Bind(R.id.ivFrom) SimpleDraweeView ivFrom;

      public ViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
      }
    }
  }
}
