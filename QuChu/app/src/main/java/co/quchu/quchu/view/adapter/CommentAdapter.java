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
import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.CommentImageModel;
import co.quchu.quchu.model.CommentModel;
import co.quchu.quchu.model.ImageModel;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.activity.PhotoViewActivity;
import co.quchu.quchu.view.activity.WebViewActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nico on 16/8/26.
 */
public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  List<CommentModel> mDataSet;
  Activity mAnchorActivity;
  private boolean mShowingNoData = false;

  public static final int TYPE_INFO = 0x001;
  public static final int TYPE_PAGE_END = 0x006;



  public void showPageEnd(boolean bl){
    mShowingNoData = bl;
    notifyDataSetChanged();
  }

  public CommentAdapter(Activity activity,List<CommentModel> mDataSet) {
    this.mDataSet = mDataSet;
    this.mAnchorActivity = activity;
  }
  
  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    switch (viewType){
      case TYPE_INFO:
        return new CommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quchu_detail_comment, parent, false));
      default:
        return new PageEndViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cp_page_end, parent, false));
    }
  }

  @Override public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
      if (holder instanceof CommentViewHolder){
        final CommentModel commentModel = mDataSet.get(position);

        ((CommentViewHolder) holder).rvImages.setLayoutManager(new GridLayoutManager(mAnchorActivity,2));
        CommentImageAdapter adapter = new CommentImageAdapter(commentModel.getImageList());
        if (null!=commentModel && null!=commentModel.getImageList()){

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
              PhotoViewActivity.enterActivity(mAnchorActivity,position,imageModels);
            }
          });
        }
        ((CommentViewHolder) holder).rvImages.setAdapter(adapter);


        ((CommentViewHolder) holder).rbRating.setRating(commentModel.getScore());
        ((CommentViewHolder) holder).tvUsername.setText(commentModel.getUserName());
        if (null != commentModel.getCreateDate()) {
          ((CommentViewHolder) holder).tvDate.setText(
              commentModel.getCreateDate().substring(0, 10));
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
        ((CommentViewHolder) holder).sdvAvatar.setImageURI(
            Uri.parse(commentModel.getUserPhoneUrl()));
        ((CommentViewHolder) holder).ivFrom.setImageURI(Uri.parse(commentModel.getSourceUrl()));

        if (!StringUtils.isEmpty(commentModel.getPqUrl())){
          ((CommentViewHolder) holder).tvFrom.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
              WebViewActivity.enterActivity(mAnchorActivity,commentModel.getPqUrl(),"查看评论",false);
            }
          });
        }
      }
  }

  @Override public int getItemCount() {
    return null!=mDataSet?mDataSet.size()+(mShowingNoData?1:0):0;
  }
  @Override
  public int getItemViewType(int position) {
    if (position <mDataSet.size()) {
      return TYPE_INFO;
    } else{
      return TYPE_PAGE_END;
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
    @Bind(R.id.rbRating) RatingBar rbRating;
    @Bind(R.id.rvImages) RecyclerView rvImages;

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

    public void setOnItemClickListener(CommonItemClickListener listener){
      mListener = listener;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
          return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_image, parent, false));
    }

    @Override public void onBindViewHolder(ViewHolder holder, final int position) {
      holder.sdvImage.setImageURI(Uri.parse(mImageSet.get(position).getPathStr()));
      holder.sdvImage.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          if (null!=mListener){
            mListener.onItemClick(view,position);
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
}
