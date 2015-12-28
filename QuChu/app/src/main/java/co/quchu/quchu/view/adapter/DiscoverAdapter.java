package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.model.DiscoverModel;

/**
 * DiscoverAdapter
 * User: Chenhs
 * Date: 2015-11-04
 */
public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.DisHolder> {

    private Context mContext;
    private List<DiscoverModel.ResultEntity> resultList;

    public DiscoverAdapter(Context mContext, List<DiscoverModel.ResultEntity> result) {
        this.mContext = mContext;
        this.resultList = result;
    }

    @Override
    public DisHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DisHolder disHolder = new DisHolder(LayoutInflater.from(mContext).inflate(R.layout.item_my_discover, parent, false));
        return disHolder;
    }

    @Override
    public void onBindViewHolder(DisHolder holder, int position) {
        if (resultList.get(position).getImage().size() > 0) {
            holder.itemDiscoverIv.setImageURI(Uri.parse(resultList.get(position).getImage().get(0).getImgpath()));
            holder.itemDiscoverIv.setAspectRatio(0.86f);
        }
        holder.itemDiscoverAddressTv.setText(resultList.get(position).getAddress());
        holder.itemDiscoverTitleTv.setText(resultList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    class DisHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_discover_title_tv)
        TextView itemDiscoverTitleTv;
        @Bind(R.id.item_discover_address_tv)
        TextView itemDiscoverAddressTv;
        @Bind(R.id.item_discover_editbutton_tv)
        TextView itemDiscoverEditbuttonTv;
        @Bind(R.id.item_discover_iv)
        SimpleDraweeView itemDiscoverIv;
        @Bind(R.id.item_discover_root_ll)
        LinearLayout itemDiscoverRootLl;

        public DisHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.item_discover_root_ll)
        public void MyClick(View view) {
        }
    }
}
