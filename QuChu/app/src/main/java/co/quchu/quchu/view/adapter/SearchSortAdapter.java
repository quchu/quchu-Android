package co.quchu.quchu.view.adapter;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;

import java.util.List;

import co.quchu.quchu.R;
import co.quchu.quchu.model.SearchSortBean;

/**
 * Created by no21 on 2016/7/1.
 * email:437943145@qq.com
 * desc :
 */
public class SearchSortAdapter extends SearchPopWinBaseAdapter {

    List<SearchSortBean> datas;

    public void setDatas(List<SearchSortBean> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final SearchSortBean bean = datas.get(position);
        holder.searchPopItemTv.setText(bean.getSortName());
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
        return datas == null ? 0 : datas.size();
    }
}
