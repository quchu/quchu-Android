package co.quchu.quchu.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.gallery.model.PhotoInfo;
import co.quchu.quchu.utils.ImageUtils;

/**
 * Created by linqipeng on 2016/3/15 16:03
 * email:437943145@qq.com
 * desc: 发现趣处照片
 */
public class FindPositionAdapter extends RecyclerView.Adapter<FindPositionAdapter.ViewHold> {
    List<PhotoInfo> images;
    ItemClickListener listener;

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public void setImages(List<PhotoInfo> images) {
        this.images = images;
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = View.inflate(parent.getContext(), R.layout.item_find_position_image, null);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_find_position_image, parent, false);

        return new ViewHold(view);
    }

    @Override
    public void onBindViewHolder(ViewHold holder, final int position) {
        final PhotoInfo info = images.get(position);
        ImageUtils.ShowImage(info.getPhotoPath(), holder.simpleDraweeView);
        if (info.getPhotoPath().contains("res:///")) {
            holder.delete.setVisibility(View.GONE);
        } else {
            holder.delete.setVisibility(View.VISIBLE);

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.itemClick(false, position, info);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.itemClick(true, position, info);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images == null ? 0 : images.size();
    }

    class ViewHold extends RecyclerView.ViewHolder {
        @Bind(R.id.simpleDraweeView)
        SimpleDraweeView simpleDraweeView;
        @Bind(R.id.delete)
        ImageView delete;

        public ViewHold(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ItemClickListener {
        void itemClick(boolean isDelete, int position, PhotoInfo photoInfo);
    }

}
