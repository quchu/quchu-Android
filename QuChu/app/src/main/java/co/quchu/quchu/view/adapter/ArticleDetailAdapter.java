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
import co.quchu.quchu.R;
import co.quchu.quchu.model.SimpleArticleModel;
import co.quchu.quchu.model.SimplePlaceModel;

/**
 * Created by Nico on 16/7/8.
 */
public class ArticleDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;
    private List<SimplePlaceModel> mDataSet;

    private CommonItemClickListener mListener;

    public ArticleDetailAdapter(Context mContext, List<SimplePlaceModel> mDataSet,CommonItemClickListener listener) {
        this.mContext = mContext;
        this.mDataSet = mDataSet;
        this.mListener = listener;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ArticleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_article_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ArticleViewHolder) holder).tvDescription.setText(mDataSet.get(position).getContent());
            ((ArticleViewHolder) holder).tvTitle.setText(mDataSet.get(position).getName());
            ((ArticleViewHolder) holder).itemClassifyImageSdv.setImageURI(Uri.parse(mDataSet.get(position).getCover()));
            final int finalPosition = position;
            ((ArticleViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null!=mListener){
                        mListener.onItemClick(v, finalPosition);
                    }
                }
            });
    }




    @Override
    public int getItemCount() {
        return null != mDataSet ? mDataSet.size() : 0;
    }



    public class ArticleViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.sdvCover)
        SimpleDraweeView itemClassifyImageSdv;
        @Bind(R.id.tvTitle)
        TextView tvTitle;
        @Bind(R.id.tvDescription)
        TextView tvDescription;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
