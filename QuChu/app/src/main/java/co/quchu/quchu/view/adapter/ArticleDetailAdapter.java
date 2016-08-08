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
import co.quchu.quchu.view.activity.QuchuDetailsActivity;

/**
 * Created by Nico on 16/7/8.
 */
public class ArticleDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;
    private List<SimplePlaceModel> mDataSet;
    private SimpleArticleModel mSimpleArticleModel;

    private CommonItemClickListener mListener;

    public static final int TYPE_BANNER = 0x001;
    public static final int TYPE_NORMAL = 0x002;
    public static final int TYPE_PAGE_END = 0x003;


    public ArticleDetailAdapter(Context mContext, List<SimplePlaceModel> mDataSet, SimpleArticleModel mSimpleArticleModel,CommonItemClickListener listener) {
        this.mContext = mContext;
        this.mDataSet = mDataSet;
        this.mSimpleArticleModel = mSimpleArticleModel;
        this.mListener = listener;

    }


    private boolean mShowingNoData = false;

    public void showPageEnd(boolean bl){
        mShowingNoData = bl;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_BANNER;
        }else if(position>0 && position<(mDataSet.size()+1)){
            return TYPE_NORMAL;
        }else{
            return TYPE_PAGE_END;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_BANNER) {
            if (null!=mSimpleArticleModel){
                return new BannerViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_article_banner, parent, false));
            }else{
                return new QuchuDetailsAdapter.BlankViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_quchu_detail_blank, parent, false));
            }
        } else if(viewType == TYPE_NORMAL) {
            return new ArticleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_article_detail, parent, false));
        } else {
            return new PageEndViewHolder(LayoutInflater.from(mContext).inflate(R.layout.cp_page_end, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if ( holder instanceof BannerViewHolder){
            ((BannerViewHolder) holder).tvDescription.setText(mSimpleArticleModel.getArticleComtent());
            ((BannerViewHolder) holder).tvTitle.setText(mSimpleArticleModel.getArticleName());
            ((BannerViewHolder) holder).itemClassifyImageSdv.setImageURI(Uri.parse(mSimpleArticleModel.getImageUrl()));
        }else if(holder instanceof ArticleViewHolder){

            position = position-1;
            ((ArticleViewHolder) holder).tvDescription.setText(mDataSet.get(position).getContent());
            ((ArticleViewHolder) holder).tvTitle.setText(mDataSet.get(position).getName());
            ((ArticleViewHolder) holder).itemClassifyImageSdv.setImageURI(Uri.parse(mDataSet.get(position).getCover()));

        }
        final int finalPosition = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
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
        return mDataSet == null ? 0 : mDataSet.size()+1+(mShowingNoData?1:0);
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.sdvCover)
        SimpleDraweeView itemClassifyImageSdv;
        @Bind(R.id.tvTitle)
        TextView tvTitle;
        @Bind(R.id.tvDescription)
        TextView tvDescription;

        public BannerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
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
