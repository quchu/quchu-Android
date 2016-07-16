package co.quchu.quchu.view.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.SceneModel;

/**
 * Created by Nico on 16/4/7.
 */
public class AllSceneGridAdapter extends RecyclerView.Adapter<AllSceneGridAdapter.ViewHolder> {

    List<SceneModel> mData;

    public OnItemClickListener mOnItemClickListener;

    public AllSceneGridAdapter(List<SceneModel> pData, OnItemClickListener onItemClickListener) {
        this.mData = pData;
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend_grid, parent, false);
        return new ViewHolder(view);
    }

    private int mFinalAnimatedIndex = -1;

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        if (position > mFinalAnimatedIndex) {
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

        if (null != mData.get(position) && null != mData.get(position).getSceneCover()) {
            holder.sdv.setImageURI(Uri.parse(mData.get(position).getSceneCover()));
        }

        holder.sdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnItemClickListener) {
                    mOnItemClickListener.onItemClick(holder.itemView,position);
                }
            }
        });

        holder.tvTitle.setText(mData.get(position).getSceneName());
        holder.tvDesc.setText(mData.get(position).getIntro());
        holder.ivAddToFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null!=mOnItemClickListener){
                    mOnItemClickListener.onItemFavoriteClick(holder.itemView,position);
                }
            }
        });
        if (holder.tvDesc.getLineCount()>1){
            holder.tvDesc.setGravity(Gravity.LEFT);
        }else{
            holder.tvDesc.setGravity(Gravity.CENTER);
        }
        holder.tvTag3.setVisibility(View.GONE);
        holder.tvTag2.setVisibility(View.GONE);
        holder.tvTag1.setVisibility(View.GONE);


        if (null != mData.get(position).getSceneTitle() && mData.get(position).getSceneTitle().length > 0) {
            for (int i = 0; i < mData.get(position).getSceneTitle().length; i++) {
                switch (i) {
                    case 0:
                        holder.tvTag1.setText(mData.get(position).getSceneTitle()[i]);
                        holder.tvTag1.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        holder.tvTag2.setText(mData.get(position).getSceneTitle()[i]);
                        holder.tvTag2.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        holder.tvTag3.setText(mData.get(position).getSceneTitle()[i]);
                        holder.tvTag3.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }
        holder.itemView.setPivotX(0);
        holder.itemView.setPivotY(0);

    }

    public interface OnItemClickListener {
        void onItemClick(View v,int position);
        void onItemFavoriteClick(View v,int position);
    }

    @Override
    public int getItemCount() {

        return null != mData ? mData.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.sdv)
        SimpleDraweeView sdv;

        @Bind(R.id.tvTitle)
        TextView tvTitle;
        @Bind(R.id.recommend_tag1)
        TextView tvTag1;
        @Bind(R.id.recommend_tag2)
        TextView tvTag2;
        @Bind(R.id.recommend_tag3)
        TextView tvTag3;

        @Bind(R.id.tvDesc)
        TextView tvDesc;

        @Bind(R.id.ivAddToFavorite)
        ImageView ivAddToFavorite;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
