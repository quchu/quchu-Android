package co.quchu.quchu.view.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.net.Uri;
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
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.model.RecommendModel;

/**
 * Created by Nico on 16/4/7.
 */
public class RecommendGridAdapter extends RecyclerView.Adapter<RecommendGridAdapter.ViewHolder> {

    List<RecommendModel> mData;

    public OnItemClickListener mOnItemClickListener;

    public RecommendGridAdapter(List<RecommendModel> pData, OnItemClickListener onItemClickListener) {
        this.mData = pData;
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foot_print, parent, false);
        return new ViewHolder(view);
    }
    private int mFinalAnimatedIndex = -1;

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


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

        holder.sdv.setAspectRatio(.8f);
        holder.sdv.setImageURI(Uri.parse(mData.get(position).getCover()));

        holder.sdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null!=mOnItemClickListener){
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
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
