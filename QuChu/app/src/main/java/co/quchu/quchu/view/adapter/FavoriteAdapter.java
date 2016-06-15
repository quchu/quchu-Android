package co.quchu.quchu.view.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.FavoriteBean;
import co.quchu.quchu.widget.TagCloudView;

/**
 * Created by no21 on 2016/4/5.
 * email:437943145@qq.com
 * desc :
 */
public class FavoriteAdapter extends AdapterBase<FavoriteBean.ResultBean, FavoriteAdapter.ViewHold> {


    @Override
    public void onBindView(final ViewHold holder, final int position) {
        final FavoriteBean.ResultBean bean = data.get(position);

        holder.name.setText(bean.getName());
        holder.simpleDraweeView.setImageURI(Uri.parse(bean.getCover()));
        holder.tag.setTags(bean.getTagsString());
        holder.address.setText(bean.getAddress());
        holder.address.setSelected(true);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.itemClick(holder, bean, v.getId(), position);
                }
            }
        });
    }

    @Override
    public ViewHold onCreateView(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nearby_quchu, parent, false);
        return new ViewHold(view);
    }

    class ViewHold extends RecyclerView.ViewHolder {

        @Bind(R.id.simpleDraweeView)
        SimpleDraweeView simpleDraweeView;
        @Bind(R.id.desc)
        TextView name;
        @Bind(R.id.tag)
        TagCloudView tag;
        @Bind(R.id.address)
        TextView address;

        public ViewHold(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            address.setVisibility(View.VISIBLE);
        }
    }


}
