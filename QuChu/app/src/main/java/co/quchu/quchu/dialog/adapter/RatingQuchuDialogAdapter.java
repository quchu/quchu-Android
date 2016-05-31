package co.quchu.quchu.dialog.adapter;

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
 * Created by Nico on 16/4/11.
 */
public class RatingQuchuDialogAdapter extends RecyclerView.Adapter<RatingQuchuDialogAdapter.ViewHolder> {

    List<TagsModel> mData;

    private OnItemSelectedListener mListener;

    public RatingQuchuDialogAdapter(List<TagsModel> pData, OnItemSelectedListener pListener) {
        this.mData = pData;
        this.mListener = pListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tags_filter, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvTag.setText(mData.get(position).getZh());
        holder.tvTag.setBackgroundResource(mData.get(position).isPraise()?R.drawable.shape_lineframe_white_fill:R.drawable.shape_lineframe_white);
        int color = holder.tvTag.getResources().getColor(mData.get(position).isPraise()?R.color.standard_color_black:android.R.color.white);
        holder.tvTag.setTextColor(color);

        holder.tvTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null!=mListener) mListener.onSelected(position,mData.get(position).isPraise());
            }
        });
    }

    @Override
    public int getItemCount() {
        return null != mData ? mData.size() : 0;
    }

    public interface OnItemSelectedListener{
        void onSelected(int index,boolean selected);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tvTag)
        TextView tvTag;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
