package co.quchu.quchu.view.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;

/**
 * Created by Nico on 16/4/7.
 */
public class FootPrintAdapter extends RecyclerView.Adapter<FootPrintAdapter.ViewHolder> {

    List<String> mData;

    public FootPrintAdapter(List<String> mData) {
        this.mData = mData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foot_print, parent, false);
        return new ViewHolder(view);
    }
    private int mFinalAnimatedIndex = -1;

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (position>mFinalAnimatedIndex) {
            mFinalAnimatedIndex = position;
            AnimatorSet animator = new AnimatorSet();
            int scrollY = position * 20;
            animator.playTogether(
                    ObjectAnimator.ofFloat(holder.sdv, "translationY", scrollY, 0.0f),
                    ObjectAnimator.ofFloat(holder.sdv, "alpha", 0f, 1.0f)
            );
            animator.setDuration(500);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.start();
            int delay = 10 * position;
            animator.setStartDelay(delay);
        }
        holder.sdv.setAspectRatio(1);
        holder.sdv.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public int getItemCount() {
        return null != mData ? mData.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.sdv)
        SimpleDraweeView sdv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
