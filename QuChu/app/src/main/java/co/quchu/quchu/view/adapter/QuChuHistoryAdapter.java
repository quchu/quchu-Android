package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
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
import co.quchu.quchu.model.QuChuHistoryModel;

/**
 * Created by mwb on 16/11/5.
 */
public class QuChuHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private int TYPE_BEST_HISTORY = 1;
  private int TYPE_HISTORY = 2;

  private Context mContext;
  private List<QuChuHistoryModel.BestListBean> mBestList;
  private List<QuChuHistoryModel.PlaceListBean.ResultBean> mResultBeanList;

  public QuChuHistoryAdapter(Context context) {
    mContext = context;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new HistoryViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_quchu_history, parent, false));
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
    HistoryViewHolder holder = (HistoryViewHolder) viewHolder;
    if (getItemViewType(position) == TYPE_BEST_HISTORY) {
      QuChuHistoryModel.BestListBean bestListBean = mBestList.get(position);
      QuChuHistoryModel.PlaceListBean.ResultBean bestPlaceInfo = bestListBean.getPlaceInfo();
      holder.mBestDescribeTv.setText(bestListBean.getTitle());
      holder.mCoverImg.setImageURI(Uri.parse(bestPlaceInfo.getCover()));

    } else {
      int actualPosition = position;
      if (mBestList != null) {
        actualPosition = position - getBestListSize();
      }
      QuChuHistoryModel.PlaceListBean.ResultBean resultBean = mResultBeanList.get(actualPosition);
      holder.mBestDescribeTv.setText("其他");
      holder.mCoverImg.setImageURI(Uri.parse(resultBean.getCover()));
    }
  }

  @Override
  public int getItemCount() {
    if (mBestList != null) {
      return mResultBeanList != null ? mBestList.size() + mResultBeanList.size() : mBestList.size();

    } else {
      return mResultBeanList != null ? mResultBeanList.size() : 0;
    }
  }

  @Override
  public int getItemViewType(int position) {
    if (mBestList != null) {
      if (position >= 0 && position < getBestListSize()) {
        return TYPE_BEST_HISTORY;
      } else {
        return TYPE_HISTORY;
      }
    } else {
      return TYPE_HISTORY;
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

  public class HistoryViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.best_describe_tv) TextView mBestDescribeTv;
    @Bind(R.id.right_icon) ImageView mRightIcon;
    @Bind(R.id.left_icon) ImageView mLeftIcon;
    @Bind(R.id.distance_tv) TextView mDistanceTv;
    @Bind(R.id.cover_img) SimpleDraweeView mCoverImg;

    public HistoryViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
