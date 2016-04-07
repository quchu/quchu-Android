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
import co.quchu.quchu.model.FindBean;

/**
 * Created by no21 on 2016/4/5.
 * email:437943145@qq.com
 * desc :
 */
public class FindAdapter extends RecyclerView.Adapter<FindAdapter.ViewHold> {

    private List<FindBean.ResultEntity> result;

    private OnItenClickListener listener;

    public FindAdapter(List<FindBean.ResultEntity> result) {
        this.result = result;
    }

    public void setListener(OnItenClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quchu_find, parent, false);
        return new ViewHold(view);
    }

    @Override
    public void onBindViewHolder(ViewHold holder, final int position) {
        final FindBean.ResultEntity bean = result.get(position);

        holder.name.setText(bean.getName());

        if (bean.getImage().size() > 0)
            holder.simpleDraweeView.setImageURI(Uri.parse(bean.getImage().get(0).getImgpath()));
        holder.address.setText(bean.getAddress());
        if (listener != null)
            holder.editContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.itemClick(bean);
                }
            });
    }

    @Override
    public int getItemCount() {
        return result == null ? 0 : result.size();
    }

    class ViewHold extends RecyclerView.ViewHolder {
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.deitContent)
        TextView editContent;

        @Bind(R.id.simpleDraweeView)
        SimpleDraweeView simpleDraweeView;
        @Bind(R.id.address)
        TextView address;

        public ViewHold(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItenClickListener {
        void itemClick(FindBean.ResultEntity entity);
    }
}
