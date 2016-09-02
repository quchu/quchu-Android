package co.quchu.quchu.view.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Typeface;
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
public class AllSceneGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_HEADER = 0x0001;
    public static final int TYPE_CONTENT = 0x0002;

    List<SceneModel> mData;

    public OnItemClickListener mOnItemClickListener;

    public AllSceneGridAdapter(List<SceneModel> pData, OnItemClickListener onItemClickListener) {
        this.mData = pData;
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override public int getItemViewType(int position) {
        return position==0?TYPE_HEADER:TYPE_CONTENT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==TYPE_HEADER){
            return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scene_header, parent, false));
        }else{
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend_grid, parent, false));
        }

    }

    private int mFinalAnimatedIndex = -1;

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof AllSceneGridAdapter.ViewHolder){
            position -= 1;
            if (position > mFinalAnimatedIndex) {
                mFinalAnimatedIndex = position;
                AnimatorSet animator = new AnimatorSet();
                int scrollY = position * 20;
                animator.playTogether(
                    ObjectAnimator.ofFloat(((ViewHolder) holder).sdv, "translationY", scrollY, 0.0f),
                    ObjectAnimator.ofFloat(((ViewHolder) holder).sdv, "alpha", 0f, 1.0f)
                );
                animator.setDuration(500);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.start();
                int delay = 10 * position;
                animator.setStartDelay(delay);
            }

            if (null != mData.get(position) && null != mData.get(position).getSceneCover()) {
                ((ViewHolder) holder).sdv.setImageURI(Uri.parse(mData.get(position).getSceneCover()));
            }

            final int finalPosition = position;
            ((ViewHolder) holder).sdv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mOnItemClickListener) {
                        mOnItemClickListener.onItemClick(holder.itemView, finalPosition);
                    }
                }
            });


            ((ViewHolder) holder).tvTitle.setText(mData.get(position).getSceneName());
            ((ViewHolder) holder).tvDesc.setText(mData.get(position).getIntro());
            if (((ViewHolder) holder).tvDesc.getLineCount()>1){
                ((ViewHolder) holder).tvDesc.setGravity(Gravity.LEFT);
            }else{
                ((ViewHolder) holder).tvDesc.setGravity(Gravity.CENTER);
            }
            ((ViewHolder) holder).tvTag3.setVisibility(View.GONE);
            ((ViewHolder) holder).tvTag2.setVisibility(View.GONE);
            ((ViewHolder) holder).tvTag1.setVisibility(View.GONE);


            ((ViewHolder) holder).vDivider1.setVisibility(View.INVISIBLE);
            ((ViewHolder) holder).vDivider2.setVisibility(View.INVISIBLE);
            if (null != mData.get(position).getSceneTitle() && mData.get(position).getSceneTitle().length > 0) {
                for (int i = 0; i < mData.get(position).getSceneTitle().length; i++) {
                    switch (i) {
                        case 0:
                            ((ViewHolder) holder).tvTag1.setText(mData.get(position).getSceneTitle()[i]);
                            ((ViewHolder) holder).tvTag1.setVisibility(View.VISIBLE);
                            break;
                        case 1:
                            ((ViewHolder) holder).tvTag2.setText(mData.get(position).getSceneTitle()[i]);
                            ((ViewHolder) holder).tvTag2.setVisibility(View.VISIBLE);
                            ((ViewHolder) holder).vDivider1.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            ((ViewHolder) holder).tvTag3.setText(mData.get(position).getSceneTitle()[i]);
                            ((ViewHolder) holder).tvTag3.setVisibility(View.VISIBLE);
                            ((ViewHolder) holder).vDivider2.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }
            holder.itemView.setPivotX(0);
            holder.itemView.setPivotY(0);
        }


    }

    public interface OnItemClickListener {
        void onItemClick(View v,int position);
    }

    @Override
    public int getItemCount() {
        return null != mData ? mData.size()+1 : 1;
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder{
        public HeaderViewHolder(View itemView){
            super(itemView);
        }
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
        @Bind(R.id.vHorizontalDivider1)
        View vDivider1;
        @Bind(R.id.vHorizontalDivider2)
        View vDivider2;

        @Bind(R.id.tvDesc)
        TextView tvDesc;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
