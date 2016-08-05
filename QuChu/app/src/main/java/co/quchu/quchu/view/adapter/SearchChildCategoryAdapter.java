package co.quchu.quchu.view.adapter;

import android.support.v4.content.ContextCompat;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import co.quchu.quchu.R;
import co.quchu.quchu.model.DetailModel;

/**
 * Created by no21 on 2016/7/1.
 * email:437943145@qq.com
 * desc :
 */
public class SearchChildCategoryAdapter extends SearchPopWinBaseAdapter {

    List<DetailModel.TagsEntity> data = new ArrayList<>();

    public void setData(List<DetailModel.TagsEntity> data) {

        this.data.clear();
        DetailModel.TagsEntity all = new DetailModel.TagsEntity();
        all.setId(0);
        all.setZh("全部");
        this.data.add(0,all);
        this.data.addAll(data);

        notifyDataSetChanged();
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final DetailModel.TagsEntity bean = data.get(position);
        holder.searchPopItemTv.setText(bean.getZh());
        if (selectedPosition == position) {
            holder.searchPopItemTv.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.standard_color_yellow));
            holder.searchPopItemIv.setVisibility(View.VISIBLE);
        } else {
            holder.searchPopItemContent.setBackground(null);
            holder.searchPopItemTv.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.standard_color_h2_dark));
            holder.searchPopItemIv.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = holder.getAdapterPosition();

                if (itemClickListener != null)
                    itemClickListener.itemClick(holder.getAdapterPosition(), bean);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }
}
