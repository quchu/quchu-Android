package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.model.SearchModel;
import co.quchu.quchu.utils.KeyboardUtils;

/**
 * FlickrListAdapter
 * User: Chenhs
 * Date: 2015-11-17
 */
public class SearchHistoryListAdapter extends BaseAdapter {
    private Context mContext;
    private SearchModel model;
    private SearchHistoryAdapter.SearchHItemClickListener listener;

    public SearchHistoryListAdapter(Context mContext, SearchModel model, SearchHistoryAdapter.SearchHItemClickListener listener) {
        this.mContext = mContext;
        this.model = model;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return model.getSearchList().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchHolder flickrLargeHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_search_history, null);
            flickrLargeHolder = new SearchHolder(convertView, listener);
            convertView.setTag(flickrLargeHolder);
        } else {
            flickrLargeHolder = (SearchHolder) convertView.getTag();
        }
        flickrLargeHolder.itemSearchHistoryStrTv.setText(model.getSearchList().get(position).getSerachStr());
        return convertView;
    }

    public class SearchHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.item_search_history_delete_rl)
        RelativeLayout itemSearchHistoryDeleteRl;
        @Bind(R.id.item_search_history_str_tv)
        TextView itemSearchHistoryStrTv;
        private SearchHistoryAdapter.SearchHItemClickListener listener;

        SearchHolder(View view, SearchHistoryAdapter.SearchHItemClickListener listener) {
            super(view);
            ButterKnife.bind(this, view);
            this.listener = listener;
        }

        @OnClick({R.id.item_search_history_delete_rl, R.id.item_search_history_str_tv})
        @Override
        public void onClick(View v) {
            if (KeyboardUtils.isFastDoubleClick())
                return;
            if (listener != null)
                switch (v.getId()) {
                    case R.id.item_search_history_delete_rl:
                        listener.itemIVClick(getPosition());
                        break;
                    case R.id.item_search_history_str_tv:
                        listener.itemTVClick(getPosition());
                        break;
                }
        }
    }
}
