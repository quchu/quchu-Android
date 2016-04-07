package co.quchu.quchu.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by no21 on 2016/4/7.
 * email:437943145@qq.com
 * desc :
 */
public class FootprintAdapter extends RecyclerView.Adapter<FootprintAdapter.ViewHold> {
    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView view = new TextView(parent.getContext());

        return new ViewHold(view);
    }

    @Override
    public void onBindViewHolder(ViewHold holder, int position) {
        ((TextView) holder.itemView).setText("" + position);

    }

    @Override
    public int getItemCount() {
        return 100;
    }

    class ViewHold extends RecyclerView.ViewHolder {
        public ViewHold(View itemView) {
            super(itemView);
        }
    }
}
