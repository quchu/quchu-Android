package co.quchu.quchu.view.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.NearbyItemModel;
import co.quchu.quchu.widget.TagCloudView;

/**
 * Created by Nico on 16/4/11.
 */
public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.ViewHolder> {

    private List<NearbyItemModel> mData;

    public NearbyAdapter(List<NearbyItemModel> pData) {
        mData = pData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quchu_favorite, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(mData.get(position).getName());
        holder.simpleDraweeView.setImageURI(Uri.parse(mData.get(position).getCover()));
        List<String> tags = new ArrayList<>();
        for (int i = 0; i < mData.get(position).getTags().size(); i++) {
            tags.add(mData.get(position).getTags().get(i).getZh());
        }
        holder.tag.setTags(tags);
    }

    @Override
    public int getItemCount() {
        return null!=mData?mData.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.tag)
        TagCloudView tag;
        @Bind(R.id.address)
        TextView address;
        @Bind(R.id.simpleDraweeView)
        SimpleDraweeView simpleDraweeView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
