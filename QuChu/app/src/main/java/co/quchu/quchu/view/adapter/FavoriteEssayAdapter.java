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
import co.quchu.quchu.model.FavoriteEssayBean;

/**
 * Created by no21 on 2016/6/21.
 * email:437943145@qq.com
 * desc :
 */
public class FavoriteEssayAdapter extends AdapterBase<FavoriteEssayBean.ResultBean, FavoriteEssayAdapter.ViewHold> {


    @Override
    public void onBindView(final ViewHold holder, final int position) {
        final FavoriteEssayBean.ResultBean bean = getData().get(position);
        holder.simpleDraweeView.setImageURI(Uri.parse(bean.getImageUrl()));
        holder.favoriteEssayName.setText(bean.getArticleName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.itemClick(holder, bean, 0, position);
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_essay, parent, false);
        return new ViewHold(view);
    }

    class ViewHold extends RecyclerView.ViewHolder {
        @Bind(R.id.simpleDraweeView)
        SimpleDraweeView simpleDraweeView;
        @Bind(R.id.favorite_essay_name)
        TextView favoriteEssayName;

        public ViewHold(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
