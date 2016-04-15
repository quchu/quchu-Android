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
 * Created by linqipeng on 2016/3/29 11:55
 * email:437943145@qq.com
 * desc:
 */
public class AdapterWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;

    private static final int ITEM_VIEW_TYPE_FOOTER = -1;
    private boolean loadmoreing;
    private OnLoadmoreListener loadmoreListener;
    private boolean loadMoreEnable = true;

    public void setLoadmoreListener(OnLoadmoreListener loadmoreListener) {
        this.loadmoreListener = loadmoreListener;
    }

    public void setLoadMoreEnable(boolean loadMoreEnable) {
        this.loadMoreEnable = loadMoreEnable;
        notifyDataSetChanged();
    }

    public void showRetry(View.OnClickListener listener) {

    }

    public AdapterWrapper(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cp_loadmore, parent, false);
            return new ViewHold(view);
        }
        return adapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (loadMoreEnable && position == getItemCount() - 1) {
            ObjectAnimator rotation = ObjectAnimator.ofFloat(((ViewHold) holder).loadView, "rotation", 0, 360);
            rotation.setInterpolator(new LinearInterpolator());
            rotation.setRepeatMode(ValueAnimator.RESTART);
            rotation.setRepeatCount(ValueAnimator.INFINITE);
            rotation.setDuration(1500);
            rotation.start();
            if (!loadmoreing && loadmoreListener != null) {
                loadmoreing = true;
                loadmoreListener.onLoadmore();
            }

        } else {
            adapter.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (loadMoreEnable && position == getItemCount() - 1)
            return ITEM_VIEW_TYPE_FOOTER;
        return adapter.getItemViewType(position);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        adapter.setHasStableIds(hasStableIds);
    }

    @Override
    public int getItemCount() {
        if (loadMoreEnable)
            return adapter.getItemCount() + 1;
        else
            return adapter.getItemCount();
    }

    @Override
    public long getItemId(int position) {
        return adapter.getItemId(--position);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        adapter.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        return adapter.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        adapter.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        adapter.onViewDetachedFromWindow(holder);
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        adapter.registerAdapterDataObserver(observer);
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        adapter.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        adapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        adapter.onDetachedFromRecyclerView(recyclerView);
    }

    static class ViewHold extends RecyclerView.ViewHolder {
        @Bind(R.id.ivIndicator)
        ImageView loadView;
        @Bind(R.id.textView)
        TextView retryView;

        public ViewHold(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnLoadmoreListener {
        void onLoadmore();
    }
}
