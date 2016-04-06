package co.quchu.quchu.view.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

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
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHold> {

    private List<FavoriteBean.ResultBean> result;

    public FavoriteAdapter(List<FavoriteBean.ResultBean> result) {
        this.result = result;
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quchu, parent, false);
        return new ViewHold(view);
    }

    @Override
    public void onBindViewHolder(ViewHold holder, int position) {
        FavoriteBean.ResultBean bean = result.get(position);

        holder.name.setText(bean.getName());
        holder.simpleDraweeView.setImageURI(Uri.parse(bean.getAutorPhoto()));
        holder.address.setText(bean.getAddress());
    }

    @Override
    public int getItemCount() {
        return result == null ? 0 : result.size();
    }

    class ViewHold extends RecyclerView.ViewHolder {
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.tag)
        TagCloudView tag;
        @Bind(R.id.simpleDraweeView)
        SimpleDraweeView simpleDraweeView;
        @Bind(R.id.address)
        TextView address;

        public ViewHold(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
