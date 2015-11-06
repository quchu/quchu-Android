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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;

/**
 * DiscoverAdapter
 * User: Chenhs
 * Date: 2015-11-04
 */
public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.DisHolder> {

    private Context mContext;

    public DiscoverAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public DisHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DisHolder disHolder = new DisHolder(LayoutInflater.from(mContext).inflate(R.layout.item_my_discover, parent, false));
        return disHolder;
    }

    @Override
    public void onBindViewHolder(DisHolder holder, int position) {
            holder.itemDiscoverIv.setImageURI(Uri.parse("http://a.hiphotos.baidu.com/image/pic/item/79f0f736afc37931e211bcd9e9c4b74542a911c0.jpg"));
        holder.itemDiscoverIv.setAspectRatio(0.75f);
    }

    @Override
    public int getItemCount() {
        return 10;
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
        public void MyClick(View view){
        }
    }
}
