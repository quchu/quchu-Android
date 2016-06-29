package co.quchu.quchu.view.adapter;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;

/**
 * Created by no21 on 2016/6/22.
 * email:437943145@qq.com
 * desc :
 */
public class SearchPopWinAdapter extends RecyclerView.Adapter<SearchPopWinAdapter.ViewHolder> {


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_pop_win, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.searchPopItemContent.setBackgroundColor(Color.BLACK);
                holder.searchPopItemTv.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.standard_color_white));
            }
        });
    }

    @Override
    public int getItemCount() {
        return 100;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.search_pop_item_iv)
        ImageView searchPopItemIv;
        @Bind(R.id.search_pop_item_tv)
        TextView searchPopItemTv;
        @Bind(R.id.search_pop_item_content)
        LinearLayout searchPopItemContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
