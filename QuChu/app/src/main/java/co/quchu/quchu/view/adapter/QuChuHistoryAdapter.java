package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.QuChuHistoryModel;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;

/**
 * Created by mwb on 16/11/5.
 */
public class QuChuHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private int TYPE_HEADER = 1;
  private int TYPE_BEST_HISTORY = 2;
  private int TYPE_HISTORY = 3;

  private Context mContext;
  private List<QuChuHistoryModel.BestListBean> mBestList;
  private List<QuChuHistoryModel.PlaceListBean.ResultBean> mResultBeanList;

  public QuChuHistoryAdapter(Context context) {
    mContext = context;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == TYPE_HEADER) {
      return new QuChuHistoryHeaderViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_quchu_history_header, parent, false));
    }

    return new QuChuHistoryViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_quchu_history, parent, false));
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
    if (viewHolder instanceof QuChuHistoryHeaderViewHolder) {
      QuChuHistoryHeaderViewHolder holder = (QuChuHistoryHeaderViewHolder) viewHolder;

    } else if (viewHolder instanceof QuChuHistoryViewHolder) {
      QuChuHistoryViewHolder holder = (QuChuHistoryViewHolder) viewHolder;
      if (getItemViewType(position) == TYPE_BEST_HISTORY) {
        //最优的
        int actualPosition = position - 1;
        QuChuHistoryModel.BestListBean bestListBean = mBestList.get(actualPosition);
        QuChuHistoryModel.PlaceListBean.ResultBean bestPlaceInfo = bestListBean.getPlaceInfo();
        holder.mCoverImg.setImageURI(Uri.parse(bestPlaceInfo.getCover()));
        holder.mTitleTv.setText(bestPlaceInfo.getName());
        holder.mSubtitleTv.setText("- " + bestPlaceInfo.getAreaCircleName());

        StringBuffer sb = new StringBuffer();
        if (bestPlaceInfo.getTags() != null) {
          for (QuChuHistoryModel.PlaceListBean.ResultBean.TagsBean tagsBean : bestPlaceInfo.getTags()) {
            sb.append(tagsBean.getZh() + " ");
          }
        }
        String distance = StringUtils.getDistance(SPUtils.getLatitude(), SPUtils.getLongitude(), bestPlaceInfo.getLatitude(), bestPlaceInfo.getLongitude());
        holder.mTagTv.setText(TextUtils.isEmpty(sb.toString()) ? distance : sb.toString() + " | " + distance);

        holder.mTopLableLayout.setVisibility(View.VISIBLE);

        holder.mBestDescribeTv.setVisibility(View.VISIBLE);
        holder.mLeftIcon.setVisibility(View.GONE);
        holder.mOffsetTv.setVisibility(View.GONE);
        holder.mBestDescribeTv.setText(bestListBean.getTitle());
        if (bestListBean.isBest()) {
          holder.mRightIcon.setVisibility(View.GONE);
        } else {
          holder.mRightIcon.setVisibility(View.VISIBLE);
          holder.mRightIcon.setTag(actualPosition);
          holder.mRightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              int position = (int) v.getTag();
              chooseNextBest(position);
            }
          });
        }

        //此时选择了下一条最优数据
        if (bestListBean.isChooseNextBest()) {
          holder.mBestDescribeTv.setVisibility(View.GONE);
          holder.mLeftIcon.setVisibility(View.VISIBLE);
          holder.mOffsetTv.setVisibility(View.VISIBLE);
          holder.mRightIcon.setVisibility(View.GONE);
          holder.mOffsetTv.setText(bestListBean.getGapStr());
          holder.mLeftIcon.setTag(actualPosition);
          holder.mLeftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              int position = (int) v.getTag();
              chooseNextBest(position);
            }
          });
        }

        setItemClick(holder, bestPlaceInfo);

      } else {
        //除了最优的以外,所有记录
        int actualPosition = position - 1;
        if (mBestList != null) {
          actualPosition = position - 1 - getBestListSize();
        }

        QuChuHistoryModel.PlaceListBean.ResultBean resultBean = mResultBeanList.get(actualPosition);
        holder.mCoverImg.setImageURI(Uri.parse(resultBean.getCover()));
        holder.mTitleTv.setText(resultBean.getName());
        holder.mSubtitleTv.setText("- " + resultBean.getAreaCircleName());

        StringBuffer sb = new StringBuffer();
        if (resultBean.getTags() != null) {
          for (QuChuHistoryModel.PlaceListBean.ResultBean.TagsBean tagsBean : resultBean.getTags()) {
            sb.append(tagsBean.getZh() + " ");
          }
        }
        String distance = StringUtils.getDistance(SPUtils.getLatitude(), SPUtils.getLongitude(), resultBean.getLatitude(), resultBean.getLongitude());
        holder.mTagTv.setText(TextUtils.isEmpty(sb.toString()) ? distance : sb.toString() + " | " + distance);

        holder.mTopLableLayout.setVisibility(actualPosition == 0 ? View.VISIBLE : View.GONE);
        holder.mBestDescribeTv.setVisibility(View.VISIBLE);
        holder.mLeftIcon.setVisibility(View.GONE);
        holder.mRightIcon.setVisibility(View.GONE);
        holder.mOffsetTv.setVisibility(View.GONE);
        holder.mBestDescribeTv.setText("其他");

        setItemClick(holder, resultBean);
      }
    }
  }

  /**
   * 列表点击
   */
  private void setItemClick(QuChuHistoryViewHolder holder, QuChuHistoryModel.PlaceListBean.ResultBean resultBean) {
    holder.itemView.setTag(resultBean);
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        QuChuHistoryModel.PlaceListBean.ResultBean resultBean = (QuChuHistoryModel.PlaceListBean.ResultBean) v.getTag();
        if (resultBean != null && mListener != null) {
          mListener.onItemClick(resultBean);
        }
      }
    });
  }

  /**
   * 选择下一条最优
   */
  private void chooseNextBest(int position) {
    QuChuHistoryModel.BestListBean bestListBean = mBestList.get(position);
    QuChuHistoryModel.PlaceListBean.ResultBean bestPlaceInfo = bestListBean.getPlaceInfo();
    QuChuHistoryModel.PlaceListBean.ResultBean secondPlaceInfo = bestListBean.getSecondPlaceInfo();

    if (mBestList.contains(bestListBean)) {
      mBestList.remove(bestListBean);
    }

    if (bestListBean.isChooseNextBest()) {
      bestListBean.setSecondPlaceInfo(bestPlaceInfo);
      bestListBean.setPlaceInfo(secondPlaceInfo);
      bestListBean.setChooseNextBest(false);
    } else {
      bestListBean.setPlaceInfo(secondPlaceInfo);
      bestListBean.setSecondPlaceInfo(bestPlaceInfo);
      bestListBean.setChooseNextBest(true);
    }
    mBestList.add(position, bestListBean);
    notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    if (mBestList != null) {
      return mResultBeanList != null ? mBestList.size() + mResultBeanList.size() + 1 : mBestList.size() + 1;

    } else {
      return mResultBeanList != null ? mResultBeanList.size() + 1 : 1;
    }
  }

  @Override
  public int getItemViewType(int position) {
    if (position == 0) {
      return TYPE_HEADER;

    } else {
      if (position >= 1 && position <= getBestListSize()) {
        return TYPE_BEST_HISTORY;
      } else {
        return TYPE_HISTORY;
      }
    }
  }

  public void setData(List<QuChuHistoryModel.BestListBean> bestList, List<QuChuHistoryModel.PlaceListBean.ResultBean> resultBeanList) {
    mBestList = bestList;
    mResultBeanList = resultBeanList;
    notifyDataSetChanged();
  }

  private int getBestListSize() {
    int size = 0;

    if (mBestList != null) {
      size = mBestList.size();
    }

    return size;
  }

  private int getResultListSize() {
    int size = 0;

    if (mResultBeanList != null) {
      size = mResultBeanList.size();
    }

    return size;
  }

  /**
   * 最优记录
   */
  public void setBestList(List<QuChuHistoryModel.BestListBean> bestList) {
    mBestList = bestList;
    notifyDataSetChanged();
  }

  /**
   * 其他记录
   */
  public void setResultList(List<QuChuHistoryModel.PlaceListBean.ResultBean> resultBeanList) {
    mResultBeanList = resultBeanList;
    notifyDataSetChanged();
  }

  /**
   * 更多
   */
  public void addMoreResultList(List<QuChuHistoryModel.PlaceListBean.ResultBean> resultBeanList) {
    if (mResultBeanList != null) {
      mResultBeanList.addAll(resultBeanList);
      notifyDataSetChanged();
    }
  }

  public class QuChuHistoryViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.best_describe_tv) TextView mBestDescribeTv;
    @Bind(R.id.history_right_icon) ImageView mRightIcon;
    @Bind(R.id.history_left_icon) ImageView mLeftIcon;
    @Bind(R.id.history_offset_tv) TextView mOffsetTv;
    @Bind(R.id.history_cover_img) SimpleDraweeView mCoverImg;
    @Bind(R.id.history_top_label_layout) RelativeLayout mTopLableLayout;
    @Bind(R.id.history_title_tv) TextView mTitleTv;
    @Bind(R.id.history_subtitle_tv) TextView mSubtitleTv;
    @Bind(R.id.history_tag_tv) TextView mTagTv;

    public QuChuHistoryViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  public class QuChuHistoryHeaderViewHolder extends RecyclerView.ViewHolder {

    public QuChuHistoryHeaderViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  private QuChuHistoryClickListener mListener;

  public void setQuChuHistoryClickListener(QuChuHistoryClickListener listener) {
    mListener = listener;
  }

  public interface QuChuHistoryClickListener {
    void onItemClick(QuChuHistoryModel.PlaceListBean.ResultBean resultBean);
  }
}
