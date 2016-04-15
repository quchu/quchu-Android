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

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;

/**
 * Created by no21 on 2016/4/15.
 * email:437943145@qq.com
 * desc :
 */
public abstract class AdapterBase<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private static final int ITEM_VIEW_TYPE_FOOTER = -1;
    private boolean loadmoreing;
    private OnLoadmoreListener loadmoreListener;
    private boolean loadMoreEnable = true;

    public void setLoadmoreListener(OnLoadmoreListener loadmoreListener) {
        this.loadmoreListener = loadmoreListener;
    }

    public void setLoadmoreing(boolean loadmoreing) {
        this.loadmoreing = loadmoreing;
    }

    public void setLoadMoreEnable(boolean loadMoreEnable) {
        this.loadMoreEnable = loadMoreEnable;
        if (!loadMoreEnable)
            notifyDataSetChanged();
    }

    @Override
    public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cp_loadmore, parent, false);
            return (VH) new ViewHold(view);
        } else
            return onCreateView(parent, viewType);
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        if (holder instanceof ViewHold) {
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
            onBindView(holder, position);
        }
    }

    @Override
    public final int getItemCount() {
        if (loadMoreEnable) {
            return getCount() + 1;
        } else {
            return getCount();
        }
    }

    @Override
    public final int getItemViewType(int position) {
        if (loadMoreEnable && position == getItemCount() - 1) {
            return ITEM_VIEW_TYPE_FOOTER;
        }
        return getItemType();
    }

    public abstract int getCount();

    public abstract void onBindView(VH holder, int position);

    public abstract VH onCreateView(ViewGroup parent, int viewType);

    public int getItemType() {
        return 0;
    }

    public interface OnLoadmoreListener {
        void onLoadmore();
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

}
