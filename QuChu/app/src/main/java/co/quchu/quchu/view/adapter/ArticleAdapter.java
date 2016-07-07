package co.quchu.quchu.view.adapter;

import android.content.Context;
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
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.model.ArticleModel;
import co.quchu.quchu.utils.KeyboardUtils;

/**
 * ArticleAdapter
 * User: Chenhs
 * Date: 2015-12-08
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ClassifyHolder> {

    private Context mContext;
    private List<ArticleModel> mDataSet;

    private ClasifyClickListener listener;

    public ArticleAdapter(Context context, List<ArticleModel> arrayList) {
        this.mContext = context;
        this.mDataSet = arrayList;
    }

    @Override
    public ClassifyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ClassifyHolder(LayoutInflater.from(mContext).inflate(R.layout.item_classify_card, parent, false), listener);
    }

    public void setOnItemCliskListener(ClasifyClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(ClassifyHolder holder, int position) {
        holder.itemClassifyImageSdv.setImageURI(Uri.parse(mDataSet.get(position).getImageUrl() + ""));
        holder.itemClassifyImageSdv.setAspectRatio(1.73f);
        holder.tvTitle.setText(mDataSet.get(position).getArticleName());
        holder.tvDescription.setText(mDataSet.get(position).getArticleComtent());
        holder.tvReviews.setText(mDataSet.get(position).getReadCount());
        holder.tvFavorite.setText(mDataSet.get(position).getFavoriteCount());
        holder.sdvAvatar.setImageURI(Uri.parse(mDataSet.get(position).getUserUrl()));

    }

    @Override
    public int getItemCount() {
        return mDataSet == null ? 0 : mDataSet.size();
    }

    public class ClassifyHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_classify_image_sdv)
        SimpleDraweeView itemClassifyImageSdv;

        @Bind(R.id.tvTitle)
        TextView tvTitle;
        @Bind(R.id.tvDescription)
        TextView tvDescription;
        @Bind(R.id.tvReviews)
        TextView tvReviews;
        @Bind(R.id.tvFavorite)
        TextView tvFavorite;
        @Bind(R.id.sdvAvatar)
        SimpleDraweeView sdvAvatar;


        private ClasifyClickListener listener;

        public ClassifyHolder(View itemView, ClasifyClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
        }

        @OnClick(R.id.item_classify_root)
        public void onCardClick(View v) {
            if (KeyboardUtils.isFastDoubleClick())
                return;
            switch (v.getId()) {
                case R.id.item_classify_root:
                    if (listener != null)
                        listener.cItemClick(v, getAdapterPosition());
                    break;
            }
        }
    }

    public interface ClasifyClickListener {
        void cItemClick(View view, int position);
    }
}
