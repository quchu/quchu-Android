package co.quchu.quchu.view.adapter;

import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.MyFootprintBean;

/**
 * Created by no21 on 2016/4/7.
 * email:437943145@qq.com
 * desc :
 */
public class MyFootprintAdapter extends RecyclerView.Adapter<MyFootprintAdapter.ViewHold> {

    private List<MyFootprintBean> data;
    private OnItemClickListener listener;

    public MyFootprintAdapter(List<MyFootprintBean> data, OnItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_footprint, parent, false);

        return new ViewHold(view);
    }

    @Override
    public void onBindViewHolder(final ViewHold holder, int position) {
        Uri uri = Uri.parse("http://h.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=3dc4538262d0f703e6e79dd83dca7d0b/7a899e510fb30f24f570e996c895d143ac4b03b8.jpg");

        holder.simpleDraweeView.setImageURI(uri);
        holder.headView.setImageURI(uri);
        holder.simpleDraweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.itemClick(null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    static class ViewHold extends RecyclerView.ViewHolder {
        @Bind(R.id.simpleDraweeView)
        SimpleDraweeView simpleDraweeView;
        @Bind(R.id.cardView)
        CardView cardView;
        @Bind(R.id.headView)
        SimpleDraweeView headView;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.timeAndAddress)
        TextView timeAndAddress;
        @Bind(R.id.containerNone)
        RelativeLayout containerNone;
        @Bind(R.id.line)
        View line;

        static int itemHeight;

        public ViewHold(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (itemHeight != 0) {
                line.getLayoutParams().height = itemHeight;
            } else {
                itemView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        itemHeight = itemView.getHeight();
                        line.getLayoutParams().height = itemHeight;
                        itemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        }
    }

    public interface OnItemClickListener {
        void itemClick(MyFootprintBean bean);
    }
}
