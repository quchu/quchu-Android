package co.quchu.quchu.view.adapter;

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
public abstract class SearchPopWinBaseAdapter extends RecyclerView.Adapter<SearchPopWinBaseAdapter.ViewHolder> {

    protected int selectedPosition = 0;
    protected OnItemClickListener itemClickListener;

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public <DT> void setItemClickListener(OnItemClickListener<DT> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_pop_win, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public abstract void onBindViewHolder(ViewHolder holder, int position);

    @Override
    public abstract int getItemCount();


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

    public interface OnItemClickListener<DT> {

        void itemClick(int position, DT item);
    }
}
