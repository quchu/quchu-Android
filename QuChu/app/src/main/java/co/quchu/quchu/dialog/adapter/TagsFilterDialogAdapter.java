package co.quchu.quchu.dialog.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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

    private OnItemSelectedListener mListener;

    public TagsFilterDialogAdapter(List<TagsModel> pData, OnItemSelectedListener pListener) {
        this.mData = pData;
        this.mListener = pListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nearby_tag_filter, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvTag.setText(mData.get(position).getZh());
        holder.cbTag.setChecked(mData.get(position).isPraise());
        if (mData.get(position).getZh().equals("饿了")){
            holder.cbTag.setBackgroundResource(R.drawable.ic_nearby_tag_ele_selector);
        }else if(mData.get(position).getZh().equals("累了")){
            holder.cbTag.setBackgroundResource(R.drawable.ic_nearby_tag_leile_selector);
        }else if(mData.get(position).getZh().equals("逛逛")){
            holder.cbTag.setBackgroundResource(R.drawable.ic_nearby_tag_guang_selector);
        }else if(mData.get(position).getZh().equals("找乐子")){
            holder.cbTag.setBackgroundResource(R.drawable.ic_nearby_tag_zhaolezi_selector);
        }

        holder.cbTag.setOnClickListener(new View.OnClickListener() {
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

        @Bind(R.id.cbTag)
        CheckBox cbTag;
        @Bind(R.id.tvTag)
        TextView tvTag;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
