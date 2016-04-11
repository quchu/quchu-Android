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
public class TagsFilterDialogAdapter extends RecyclerView.Adapter<TagsFilterDialogAdapter.ViewHolder> {

    List<TagsModel> mData;
    List<Boolean> mSelection;

    private OnItemSelectedListener mListener;

    public TagsFilterDialogAdapter(List<TagsModel> pData, List<Boolean> pSelection, OnItemSelectedListener pListener) {
        this.mData = pData;
        this.mSelection = pSelection;
        this.mListener = pListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tags_filter, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvTag.setText(mData.get(position).getZh());
        holder.tvTag.setBackgroundResource(mSelection.get(position)?R.drawable.shape_lineframe_white_fill:R.drawable.shape_lineframe_white);
        int color = holder.tvTag.getResources().getColor(mSelection.get(position)?R.color.appBackground:R.color.white);
        holder.tvTag.setTextColor(color);

        holder.tvTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null!=mListener) mListener.onSelected(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return null != mData ? mData.size() : 0;
    }

    public interface OnItemSelectedListener{
        void onSelected(int index);
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
