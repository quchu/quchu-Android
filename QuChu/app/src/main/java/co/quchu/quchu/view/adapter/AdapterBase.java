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
    private static final int ITEM_VIEW_TYPE_FOOTER = -1;
    private boolean loadMoreing = true;
    private OnLoadmoreListener loadmoreListener;
    private boolean loadMoreEnable = true;
    protected List<DT> data;
    protected OnItemClickListener<DT> itemClickListener;

    public void setItemClickListener(OnItemClickListener<DT> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void initData(List<DT> data) {
        this.data = data;
        loadMoreing = false;
        loadMoreEnable = !(data == null || data.size() < 10);
        notifyDataSetChanged();
    }

    public void addMoreData(List<DT> data) {
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

    public void setLoadmoreListener(OnLoadmoreListener loadmoreListener) {
        this.loadmoreListener = loadmoreListener;
    }


    public void setLoadMoreEnable(boolean loadMoreEnable) {
        loadMoreing = false;

        if (this.loadMoreEnable != loadMoreEnable) {
            this.loadMoreEnable = loadMoreEnable;
            notifyDataSetChanged();
        }
    }

    @Override
    public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cp_loadmore, parent, false);
            return (VH) new LoadMoreViewHolder(view);
        } else
            return onCreateView(parent, viewType);
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        if (holder instanceof LoadMoreViewHolder) {
            LoadMoreViewHolder loadMoreHold = (LoadMoreViewHolder) holder;
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
            } else {
                loadMoreHold.loadView.clearAnimation();
                loadMoreHold.loadView.setVisibility(View.INVISIBLE);

                loadMoreHold.retryView.setText("没有更多了~~");

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
        return getItemType();
    }

    public int getCount() {
        return data == null ? 0 : data.size();
    }

    public abstract void onBindView(VH holder, int position);

    public abstract VH onCreateView(ViewGroup parent, int viewType);

    public int getItemType() {
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
        void itemClick(DT item, int type, int position);
    }
}