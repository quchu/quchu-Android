package co.quchu.quchu.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * DiscoverAdapter
 * User: Chenhs
 * Date: 2015-11-04
 */
public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.DisHolder> {


    @Override
    public DisHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(DisHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class DisHolder extends RecyclerView.ViewHolder{

        public DisHolder(View itemView) {
            super(itemView);
        }
    }
}
