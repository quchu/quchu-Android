package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.model.SearchModel;

/**
 * AtmAdapter
 * User: Chenhs
 * Date: 2015-10-30
 * 氛围列表adapter
 */
public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.SearchHolder> {


    private Context activity;
    private SearchModel searchModel;
    private SearchHItemClickListener listener;

    public SearchHistoryAdapter(Context atmosphereActivity, SearchModel searchModel) {
        activity = atmosphereActivity;
        this.searchModel = searchModel;
    }


    public SearchHistoryAdapter(Context activity, SearchModel searchModel, SearchHItemClickListener listener) {
        this.activity = activity;
        this.searchModel = searchModel;
        this.listener = listener;
    }

    public void setOnitemClickListener(SearchHItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SearchHolder pvh = new SearchHolder((LayoutInflater.from(
                activity).inflate(R.layout.item_search_history, parent,
                false)), listener);
        return pvh;
    }

    @Override
    public void onBindViewHolder(SearchHolder holder, int position) {
        if (position<getItemCount()-1) {
            holder.itemSearchHistoryStrTv.setText(searchModel.getSearchList().get(position).getSerachStr());
            holder.itemSearchHistoryClearRl.setVisibility(View.GONE);
        }else {
            holder.itemSearchHistoryStrTv.setVisibility(View.GONE);
            holder.itemSearchHistoryDeleteRl.setVisibility(View.GONE);
            holder.itemSearchHistoryClearRl.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (searchModel == null) {
            return 0;
        } else {
            return searchModel.getSearchList().size() + 1;
        }
    }

/*    private int defaultView = 0, buttonView = 1;

    @Override
    public int getItemViewType(int position) {
        if (position < searchModel.getSearchList().size() + 1) {
            return defaultView;
        } else {
            return buttonView;
        }
    }*/

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_atmosphere_view.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    public class SearchHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.item_search_history_delete_rl)
        RelativeLayout itemSearchHistoryDeleteRl;
        @Bind(R.id.item_search_history_str_tv)
        TextView itemSearchHistoryStrTv;
        @Bind(R.id.item_search_history_clear_rl)
        RelativeLayout itemSearchHistoryClearRl;
        private SearchHItemClickListener listener;

        SearchHolder(View view, SearchHItemClickListener listener) {
            super(view);
            ButterKnife.bind(this, view);
            this.listener = listener;
        }

        @OnClick({R.id.item_search_history_delete_rl, R.id.item_search_history_str_tv, R.id.item_search_history_clear_rl})
        @Override
        public void onClick(View v) {
            if (listener != null)
                switch (v.getId()) {
                    case R.id.item_search_history_delete_rl:
                        listener.itemIVClick(getPosition());
                        break;
                    case R.id.item_search_history_str_tv:
                        listener.itemTVClick(getPosition());
                        break;
                    case R.id.item_search_history_clear_rl:
                        listener.itemRemoveAllClick();
                        break;
                }
        }
    }

    public interface SearchHItemClickListener {
        void itemTVClick(int position);

        void itemIVClick(int position);
        void itemRemoveAllClick();
    }
}

