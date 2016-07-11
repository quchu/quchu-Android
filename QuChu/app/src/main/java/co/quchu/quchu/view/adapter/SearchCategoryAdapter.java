package co.quchu.quchu.view.adapter;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;

import java.util.List;

import co.quchu.quchu.R;
import co.quchu.quchu.model.SearchCategoryBean;

/**
 * Created by no21 on 2016/7/1.
 * email:437943145@qq.com
 * desc :
 */
public class SearchCategoryAdapter extends SearchPopWinBaseAdapter {

    List<SearchCategoryBean> datas;

    public void setDatas(List<SearchCategoryBean> datas, String allData) {
        this.datas = datas;
        if (datas != null) {
            SearchCategoryBean bean = new SearchCategoryBean();
            bean.setCode("");
            bean.setZh(allData);
            datas.add(0, bean);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final SearchCategoryBean bean = datas.get(position);
        holder.searchPopItemTv.setText(bean.getZh());
        if (selectedPosition == position) {
            holder.searchPopItemContent.setBackgroundColor(Color.BLACK);
            holder.searchPopItemTv.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.standard_color_white));
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
