package co.quchu.quchu.view.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.FindBean;
import co.quchu.quchu.widget.TagCloudView;

/**
 * Created by no21 on 2016/4/5.
 * email:437943145@qq.com
 * desc :
 */
public class FindAdapter extends AdapterBase<FindBean.ResultEntity, FindAdapter.ViewHold> {


    @Override
    public void onBindView(ViewHold holder, final int position) {
        final FindBean.ResultEntity bean = data.get(position);


        holder.name.setText(bean.getName());
        if (bean.getImage().size() > 0) {
            holder.simpleDraweeView.setImageURI(Uri.parse(bean.getImage().get(0).getImgpath()));
        } else {
            holder.simpleDraweeView.setImageURI(Uri.EMPTY);
        }

        if (itemClickListener != null) {

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.itemClick(bean, v.getId(), position);
                }
            };

            holder.swipeDeleteContent.setOnClickListener(onClickListener);
            holder.swipeDeleteAction.setOnClickListener(onClickListener);
        }
    }

    @Override
    public ViewHold onCreateView(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new ViewHold(view);
    }

    class ViewHold extends RecyclerView.ViewHolder {
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.tag)
        TagCloudView tag;
        @Bind(R.id.simpleDraweeView)
        SimpleDraweeView simpleDraweeView;
        @Bind(R.id.swipe_delete_content)
        RelativeLayout swipeDeleteContent;
        @Bind(R.id.swipe_delete_action)
        FrameLayout swipeDeleteAction;

        public ViewHold(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
