package co.quchu.quchu.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import co.quchu.quchu.R;

/**
 * Created by linqipeng on 2016/3/29 11:55
 * email:437943145@qq.com
 * desc:
 */
public class AdapterWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;

    private static final int ITEM_VIEW_TYPE_FOOTER = -1;


    public AdapterWrapper(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        this.adapter = adapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_FOOTER) {
            View view = View.inflate(parent.getContext(), R.layout.item_foot_loadmore, null);
            return new ViewHold(view);
        }
        return adapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (position == getItemCount() - 1) {
//            (ViewHold) holder
        } else {
            adapter.onBindViewHolder(holder, --position, payloads);
        }
    }

    @Override
    public int getItemViewType(int position) {
//        if (position == 0)
//            return ITEM_VIEW_TYPE_HEAD;
        return adapter.getItemViewType(position);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        adapter.setHasStableIds(hasStableIds);
    }

    @Override
    public int getItemCount() {
        return adapter.getItemCount() + 1;
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

        public ViewHold(View itemView) {
            super(itemView);
        }
    }
}
