package co.quchu.quchu.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.quchu.quchu.R;
import co.quchu.quchu.model.EssayBean;

/**
 * Created by no21 on 2016/6/21.
 * email:437943145@qq.com
 * desc :
 */
public class FavoriteEssayAdapter extends AdapterBase<EssayBean, FavoriteEssayAdapter.ViewHold> {
    @Override
    public void onBindView(ViewHold holder, int position) {

    }

    @Override
    public RecyclerView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_essay, parent, false);
        return new ViewHold(view);
    }

    class ViewHold extends RecyclerView.ViewHolder {
        public ViewHold(View itemView) {
            super(itemView);
        }
    }
}
