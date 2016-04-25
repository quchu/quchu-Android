package co.quchu.quchu.view.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.TagsModel;

/**
 * Created by Nikolai on 2016/4/25.
 */
public class NearbyFilterSelectionAdapter extends RecyclerView.Adapter<NearbyFilterSelectionAdapter.ViewHolder> {

    private List<TagsModel> mData;
    private OnTagClickListener mListener;

    public NearbyFilterSelectionAdapter(List<TagsModel> mData, OnTagClickListener mListener) {
        this.mData = mData;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nearby_filter_selection,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvTag.setText(mData.get(position).getZh());
        holder.tvTag.setTextColor(Color.BLACK);
        holder.tvTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onTagClick(mData.get(position).getTagId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return null!=mData?mData.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tvTag)
        TextView tvTag;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnTagClickListener{
        void onTagClick(int tagId);
    }
}
