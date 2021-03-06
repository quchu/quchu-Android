package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.model.ArticleBannerModel;
import co.quchu.quchu.model.ArticleModel;
import co.quchu.quchu.model.ImageModel;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.widget.SimpleIndicatorView;

/**
 * ArticleAdapter
 * User: Chenhs
 * Date: 2015-12-08
 */
public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ArticleModel> mDataSet;
    private List<ImageModel> mBanner = new ArrayList<>();

    private CommonItemClickListener mListener;

    public static final int TYPE_BANNER = 0x001;
    public static final int TYPE_NORMAL = 0x002;

    public ArticleAdapter(Context context, List<ArticleModel> arrayList,List<ArticleBannerModel> articleBannerModels) {
        this.mContext = context;
        this.mDataSet = arrayList;
        if (null!=articleBannerModels){
            mBanner.clear();
            for (int i = 0; i < articleBannerModels.size(); i++) {
                ImageModel imageModel = new ImageModel();
                imageModel.setPath(articleBannerModels.get(i).getImageUrl());
                mBanner.add(imageModel);
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position==0){
            return TYPE_BANNER;
        }else{
            return TYPE_NORMAL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_BANNER){
            return new BannerHolder(LayoutInflater.from(mContext).inflate(R.layout.cp_banner, parent, false));
        }else {
            return new ArticleHolder(LayoutInflater.from(mContext).inflate(R.layout.item_classify_card, parent, false));
        }
    }


    public void setOnItemClickListener(CommonItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (position>0){
            position-=1;
            ((ArticleHolder) holder).itemClassifyImageSdv.setImageURI(Uri.parse(mDataSet.get(position).getImageUrl() + ""));
            ((ArticleHolder) holder).tvTitle.setText(mDataSet.get(position).getArticleName());
            ((ArticleHolder) holder).tvDescription.setText(mDataSet.get(position).getArticleComtent());
            ((ArticleHolder) holder).tvReviews.setText(mDataSet.get(position).getReadCount());
            ((ArticleHolder) holder).tvFavorite.setText(mDataSet.get(position).getFavoriteCount());
            ((ArticleHolder) holder).sdvAvatar.setImageURI(Uri.parse(mDataSet.get(position).getUserUrl()));
        }else {
            if (mBanner.size()>0){
                ((BannerHolder) holder).siv.setIndicators(mBanner.size());
                ((BannerHolder) holder).viewPager.setAdapter(new GalleryAdapter(mBanner));
                ((BannerHolder) holder).viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

                    @Override
                    public void onPageSelected(int position) {
                        ((BannerHolder) holder).siv.setCurrentIndex(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {}
                });

                holder.itemView.setVisibility(View.VISIBLE);
            }else{
                holder.itemView.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return mDataSet == null ? 0 : mDataSet.size()+1;

    }

    public class BannerHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.vpGallery)
        ViewPager viewPager;
        @Bind(R.id.siv)
        SimpleIndicatorView siv;


        public BannerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class ArticleHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.sdvCover)
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



        public ArticleHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.item_classify_root)
        public void onCardClick(View v) {
            if (KeyboardUtils.isFastDoubleClick())
                return;
            switch (v.getId()) {
                case R.id.item_classify_root:
                    if (mListener != null)
                        mListener.onItemClick(v, getAdapterPosition()-1);
                    break;
            }
        }
    }

}
