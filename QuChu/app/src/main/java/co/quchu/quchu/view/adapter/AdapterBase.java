package co.quchu.quchu.view.adapter;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;

/**
 * Created by no21 on 2016/4/15.
 * email:437943145@qq.com
 * desc :
 */
public abstract class AdapterBase<DT, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {


    public static final int ITEM_VIEW_TYPE_FOOTER = -1;
    private boolean loadMoreing = true;
    private OnLoadmoreListener loadmoreListener;
    private boolean loadMoreEnable = true;
    protected List<DT> data;
    protected OnItemClickListener<DT> itemClickListener;

    public void setItemClickListener(OnItemClickListener<DT> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    public void initData(List<DT> data) {
        netError = false;
        this.data = data;
        loadMoreing = false;
        loadMoreEnable = !(data == null || data.size() < 10);
        notifyDataSetChanged();
    }

    public void addMoreData(List<DT> data) {
        netError = false;
        loadMoreing = false;
        if (data == null || data.size() == 0) {
            loadMoreEnable = false;
        } else if (this.data != null) {
            this.data.addAll(data);
        } else {
            this.data = data;
        }
        notifyDataSetChanged();

    }

    public List<DT> getData() {
        return data;
    }

    public void setLoadmoreListener(OnLoadmoreListener loadmoreListener) {
        this.loadmoreListener = loadmoreListener;
    }


    public void setLoadMoreEnable(boolean loadMoreEnable) {
        loadMoreing = false;
        netError = false;
        if (this.loadMoreEnable != loadMoreEnable) {
            this.loadMoreEnable = loadMoreEnable;
        }
        notifyDataSetChanged();
    }

    private boolean netError;
    private View.OnClickListener errorListener;

    public void setNetError(View.OnClickListener errorListener) {
        this.netError = true;
        loadMoreEnable = false;
        this.errorListener = errorListener;
        notifyDataSetChanged();
    }

    @Override
    public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cp_loadmore, parent, false);
            return (VH) new LoadMoreViewHolder(view);
        } else
            return (VH) onCreateView(parent, viewType);
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        if (holder instanceof LoadMoreViewHolder) {
            final LoadMoreViewHolder loadMoreHold = (LoadMoreViewHolder) holder;
            if (loadMoreEnable) {
                loadMoreHold.retryView.setText("加载中~~");
                loadMoreHold.itemView.setVisibility(View.VISIBLE);
                ObjectAnimator rotation = ObjectAnimator.ofFloat(loadMoreHold.loadView, "rotation", 0, 360);
                rotation.setInterpolator(new LinearInterpolator());
                rotation.setRepeatMode(ValueAnimator.RESTART);
                rotation.setRepeatCount(ValueAnimator.INFINITE);
                rotation.setDuration(1500);
                rotation.start();
                if (!loadMoreing && loadmoreListener != null) {
                    loadMoreing = true;
                    loadmoreListener.onLoadmore();
                }
                loadMoreHold.retryView.setOnClickListener(null);
                loadMoreHold.retryView.setBackground(null);
            } else if (netError) {
                loadMoreHold.loadView.clearAnimation();
                loadMoreHold.loadView.setVisibility(View.INVISIBLE);
                loadMoreHold.retryView.setText("网络异常了~~点击重试");
                loadMoreHold.retryView.setClickable(true);
                loadMoreHold.retryView.setBackgroundResource(R.drawable.shape_data_null_button_bg);
                loadMoreHold.retryView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setClickable(false);
                        loadMoreHold.loadView.setVisibility(View.VISIBLE);
                        ObjectAnimator rotation = ObjectAnimator.ofFloat(loadMoreHold.loadView, "rotation", 0, 360);
                        rotation.setInterpolator(new LinearInterpolator());
                        rotation.setRepeatMode(ValueAnimator.RESTART);
                        rotation.setRepeatCount(ValueAnimator.INFINITE);
                        rotation.setDuration(1500);
                        rotation.start();
                        loadMoreHold.retryView.setBackground(null);
                        netError = false;
                        loadMoreHold.retryView.setText("加载中~~");
                        errorListener.onClick(v);
                    }
                });
            } else {
                loadMoreHold.retryView.setBackground(null);
                loadMoreHold.retryView.setOnClickListener(null);
                loadMoreHold.loadView.clearAnimation();
                loadMoreHold.loadView.setVisibility(View.INVISIBLE);
                if (getItemCount() == 1) {
                    loadMoreHold.retryView.setText("这里什么都没有呢~");
                } else {
                    loadMoreHold.retryView.setText("已加载全部!");
                }

            }
        } else {
            onBindView(holder, position);
        }
    }

    @Override
    public final int getItemCount() {
        return getCount() + 1;

    }

    @Override
    public final int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return ITEM_VIEW_TYPE_FOOTER;
        }
        return getItemType(position);
    }

    public int getCount() {
        return data == null ? 0 : data.size();
    }

    public abstract void onBindView(VH holder, int position);

    public abstract RecyclerView.ViewHolder onCreateView(ViewGroup parent, int viewType);

    public int getItemType(int position) {
        return 0;
    }

    public interface OnLoadmoreListener {
        void onLoadmore();
    }

    static class LoadMoreViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ivIndicator)
        ImageView loadView;
        @Bind(R.id.textView)
        TextView retryView;

        public LoadMoreViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener<DT> {
        /**
         *
         * @param position please use holder.getAdapterPosition()
         */
        void itemClick(RecyclerView.ViewHolder holder, DT item, int type, @Deprecated int position);
    }


    public void removeItem(RecyclerView.ViewHolder holder, DT item) {
        data.remove(item);
        notifyItemRemoved(holder.getAdapterPosition());
    }
}
