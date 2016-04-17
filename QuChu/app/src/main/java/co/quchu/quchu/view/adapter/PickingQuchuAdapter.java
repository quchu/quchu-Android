package co.quchu.quchu.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.SimpleQuchuSearchResultModel;

/**
 * Created by Nikolai on 2016/4/17.
 */
public class PickingQuchuAdapter extends RecyclerView.Adapter<PickingQuchuAdapter.ViewHolder> {

    private List<SimpleQuchuSearchResultModel> mData;
    private OnItemClickListener mListener;
    public PickingQuchuAdapter(List<SimpleQuchuSearchResultModel> pData,OnItemClickListener pListener) {
        this.mData = pData;
        this.mListener = pListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picking_quchu,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvItem.setText(mData.get(position).getName());
        holder.tvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null!=mListener){
                    mListener.onItemClick(position);
                }
            }
        });
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    @Override
    public int getItemCount() {
        return null!=mData?mData.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tvItem)
        public TextView tvItem;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
