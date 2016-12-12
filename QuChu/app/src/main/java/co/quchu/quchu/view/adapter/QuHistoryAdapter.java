package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.QuChuHistoryModel;
import co.quchu.quchu.widget.CircleIndicator;
import co.quchu.quchu.widget.TagCloudView;

/**
 * Created by mwb on 2016/12/12.
 */
public class QuHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private int TYPE_BEST_HISTORY = 2;
  private int TYPE_COMMON_HISTORY = 3;

  private final LayoutInflater mInflater;
  private final Resources mResources;
  private List<QuChuHistoryModel.BestListBean> mBestList;
  private List<QuChuHistoryModel.PlaceListBean.ResultBean> mResultBeanList;
  private int mLastSelectedPosition = -1;
  private int mLastPageSelectedIndex = -1;
  private boolean mNotifyItemChanged;

  public QuHistoryAdapter(Context context) {
    mInflater = LayoutInflater.from(context);
    mResources = context.getResources();
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == TYPE_COMMON_HISTORY) {
      return new QuChuCommonHistoryViewHolder(mInflater.inflate(R.layout.item_quchu_history_common, parent, false));

    }

    return new QuChuBestHistoryViewHolder(mInflater.inflate(R.layout.item_quchu_history_best, parent, false));
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
    if (viewHolder instanceof QuChuHistoryHeaderViewHolder) {
      QuChuHistoryHeaderViewHolder holder = (QuChuHistoryHeaderViewHolder) viewHolder;

    } else if (viewHolder instanceof QuChuBestHistoryViewHolder) {
      final QuChuBestHistoryViewHolder holder = (QuChuBestHistoryViewHolder) viewHolder;

      final int actualPosition = position;
      final QuChuHistoryModel.BestListBean bestListBean = mBestList.get(actualPosition);
      holder.historyDescribeImg.setVisibility(View.GONE);
      holder.historyDescribeTv.setText(bestListBean.getTitle());
      holder.historyDescribeTv.setTextColor(mResources.getColor(R.color.standard_color_h3_dark));
      final HistoryViewPagerAdapter adapter = new HistoryViewPagerAdapter(bestListBean);
      holder.historyVp.setAdapter(adapter);
      holder.historySiv.setVisibility(bestListBean.getSecondPlaceInfo() != null ? View.VISIBLE : View.GONE);
      holder.historySiv.setViewPager(holder.historyVp);

      holder.historyVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
          if (mLastSelectedPosition != -1 && mLastPageSelectedIndex != -1) {
            if (mLastSelectedPosition == actualPosition) {
              mNotifyItemChanged = false;
            } else {
              mNotifyItemChanged = true;
            }
          }

          if (mLastPageSelectedIndex == 0) {
            mNotifyItemChanged = false;
          }

          mLastSelectedPosition = actualPosition;
          mLastPageSelectedIndex = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
          if (state == ViewPager.SCROLL_STATE_IDLE) {

            if (mLastSelectedPosition != -1 && mNotifyItemChanged) {
              notifyItemChanged(mLastSelectedPosition == 0 ? 1 : 0);
              mNotifyItemChanged = false;
            }

            if (bestListBean.getSecondPlaceInfo() != null && mLastSelectedPosition == actualPosition) {
              holder.historyDescribeImg.setVisibility(mLastPageSelectedIndex == 0 ? View.GONE : View.VISIBLE);
              holder.historyDescribeTv.setText(mLastPageSelectedIndex == 0
                  ? bestListBean.getTitle() : bestListBean.getGapStr());
              holder.historyDescribeTv.setTextColor(mLastPageSelectedIndex == 0
                  ? mResources.getColor(R.color.standard_color_h3_dark) : mResources.getColor(R.color.standard_color_red));
            }
          }
        }
      });

    } else if (viewHolder instanceof QuChuCommonHistoryViewHolder) {
      QuChuCommonHistoryViewHolder holder = (QuChuCommonHistoryViewHolder) viewHolder;

      int actualPosition = position;
      if (mBestList != null) {
        actualPosition = position - getBestListSize();
      }

      QuChuHistoryModel.PlaceListBean.ResultBean resultBean = mResultBeanList.get(actualPosition);

      holder.historyLabelLayout.setVisibility(actualPosition == 0 ? View.VISIBLE : View.GONE);
      holder.historyDescribeTv.setText("其他浏览记录");
      holder.tvName.setText(resultBean.getName());
      List<String> strTags = new ArrayList<>();
      List<QuChuHistoryModel.PlaceListBean.ResultBean.TagsBean> tags = resultBean.getTags();
      if (null != tags && tags.size() > 0) {
        for (int i = 0; i < Math.min(tags.size(), 3); i++) {
          strTags.add(" " + tags.get(i).getZh() + " ");
        }
      }

      holder.tcvTag.setTags(strTags);
      holder.address.setText(resultBean.getAreaCircleName());
      holder.address.setVisibility(View.VISIBLE);
      holder.sdvImage.setImageURI(Uri.parse(resultBean.getCover()));

      setItemClick(holder, resultBean);
    }
  }

  /**
   * 列表点击
   */
  private void setItemClick(RecyclerView.ViewHolder holder, QuChuHistoryModel.PlaceListBean.ResultBean resultBean) {
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
      return mResultBeanList != null ? mBestList.size() + mResultBeanList.size() : mBestList.size();

    } else {
      return mResultBeanList != null ? mResultBeanList.size() : 0;
    }
  }

  @Override
  public int getItemViewType(int position) {
    if (position < getBestListSize()) {
      return TYPE_BEST_HISTORY;
    }
    return TYPE_COMMON_HISTORY;
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

  /**
   * 重置最优列表
   */
  public void resetBestList() {
    if (mLastSelectedPosition != -1) {
      notifyItemChanged(mLastSelectedPosition);
      mLastSelectedPosition = -1;
    }
  }

  public class QuChuBestHistoryViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.history_describe_img) ImageView historyDescribeImg;
    @Bind(R.id.history_describe_tv) TextView historyDescribeTv;
    @Bind(R.id.history_siv) CircleIndicator historySiv;
    @Bind(R.id.history_vp) ViewPager historyVp;

    public QuChuBestHistoryViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  public class QuChuCommonHistoryViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.history_label_layout) RelativeLayout historyLabelLayout;
    @Bind(R.id.history_describe_tv) TextView historyDescribeTv;
    @Bind(R.id.desc_tv) TextView tvName;
    @Bind(R.id.tag) TagCloudView tcvTag;
    @Bind(R.id.simpleDraweeView) SimpleDraweeView sdvImage;
    @Bind(R.id.address_tv) TextView address;

    public QuChuCommonHistoryViewHolder(View itemView) {
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

  public class HistoryViewPagerAdapter extends PagerAdapter {

    private List<QuChuHistoryModel.PlaceListBean.ResultBean> mResultBeanList = new ArrayList<>();

    public HistoryViewPagerAdapter(QuChuHistoryModel.BestListBean bestListBean) {
      QuChuHistoryModel.PlaceListBean.ResultBean resultBean1 = bestListBean.getPlaceInfo();
      QuChuHistoryModel.PlaceListBean.ResultBean resultBean2 = bestListBean.getSecondPlaceInfo();

      mResultBeanList.add(resultBean1);
      if (resultBean2 != null) {
        mResultBeanList.add(resultBean2);
      }
    }

    @Override
    public int getCount() {
      return mResultBeanList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
      return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
      View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_nearby_quchu_detail, container, false);

      QuChuHistoryModel.PlaceListBean.ResultBean resultBean = mResultBeanList.get(position);

      TextView tvName = (TextView) view.findViewById(R.id.desc_tv);
      TagCloudView tcvTag = (TagCloudView) view.findViewById(R.id.tag);
      SimpleDraweeView sdvImage = (SimpleDraweeView) view.findViewById(R.id.simpleDraweeView);
      TextView tvAddress = (TextView) view.findViewById(R.id.address_tv);

      tvName.setText(resultBean.getName());
      List<String> strTags = new ArrayList<>();
      List<QuChuHistoryModel.PlaceListBean.ResultBean.TagsBean> tags = resultBean.getTags();
      if (null != tags && tags.size() > 0) {
        for (int i = 0; i < Math.min(tags.size(), 3); i++) {
          strTags.add(" " + tags.get(i).getZh() + " ");
        }
      }

      tcvTag.setTags(strTags);
      tvAddress.setText(resultBean.getAreaCircleName());
      tvAddress.setVisibility(View.VISIBLE);
      sdvImage.setImageURI(Uri.parse(resultBean.getCover()));

      view.setTag(resultBean);
      view.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          QuChuHistoryModel.PlaceListBean.ResultBean resultBean = (QuChuHistoryModel.PlaceListBean.ResultBean) v.getTag();
          if (resultBean != null && mListener != null) {
            mListener.onItemClick(resultBean);
          }
        }
      });

      container.addView(view);
      return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
      container.removeView((View) object);
    }
  }
}
