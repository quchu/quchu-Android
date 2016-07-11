package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.SceneDetailModel;

/**
 * Created by Nico on 16/7/8.
 */
public class SceneDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;
    private SceneDetailModel mData;

    private CommonItemClickListener mListener;

    public static final int TYPE_INFO = 0x001;
    public static final int TYPE_RECOMMENDED = 0x002;
    public static final int TYPE_ARTICLE = 0x003;
    public static final int TYPE_PLACE_LIST = 0x004;


    public SceneDetailAdapter(Context mContext, SceneDetailModel sceneDetailModel, CommonItemClickListener listener) {
        this.mContext = mContext;
        this.mData = sceneDetailModel;
        this.mListener = listener;

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_INFO;
        } else if (position > 0 && position <= getRecommendedListSize()) {
            return TYPE_RECOMMENDED;
        } else if (position > getRecommendedListSize() && position <= getRecommendedListSize() + 1) {
            return TYPE_ARTICLE;
        } else {
            return TYPE_PLACE_LIST;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case TYPE_INFO:
                return new InfoViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_recommend_cardview_new_miui, parent, false));
            case TYPE_RECOMMENDED:
                return new RecommendedViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_article_detail, parent, false));
            case TYPE_ARTICLE:
                return new ArticleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_article_detail, parent, false));
            case TYPE_PLACE_LIST:
                return new PlaceViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_article_detail, parent, false));
            default:
                return new QuchuDetailsAdapter.BlankViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_quchu_detail_blank, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (null == mData) {
            return;
        }
        if (holder instanceof InfoViewHolder) {

        } else if (holder instanceof RecommendedViewHolder) {

        } else if (holder instanceof ArticleViewHolder) {

        } else if (holder instanceof PlaceViewHolder) {

        }
    }


    private int getRecommendedListSize() {
        int recommended = 0;

        if (null != mData && null != mData.getBestList()) {

            for (int i = 0; i < mData.getBestList().size(); i++) {
                if (null != mData.getBestList().get(i) && null != mData.getBestList().get(i).getPlaceInfo()) {
                    for (int j = 0; j < mData.getBestList().get(i).getPlaceInfo().size(); j++) {
                        recommended += 1;
                    }
                }
            }
        }
        return recommended;
    }

    private int getPlaceListSize() {
        int placeList = 0;
        if (null != mData && null != mData.getPlaceList() && null != mData.getPlaceList().getResult()) {
            placeList += mData.getPlaceList().getResultCount();
        }
        return placeList;
    }

    @Override
    public int getItemCount() {
        int intro = 1;
        int article = 1;
        int placeSize = getPlaceListSize();

        return getRecommendedListSize() + intro + article + placeSize;
    }

    public class InfoViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.photo)
        SimpleDraweeView photo;
        @Bind(R.id.activity)
        SimpleDraweeView activity;
        @Bind(R.id.desc)
        TextView desc;
        @Bind(R.id.recommend_tag1)
        TextView recommendTag1;
        @Bind(R.id.recommend_tag2)
        TextView recommendTag2;
        @Bind(R.id.recommend_tag3)
        TextView recommendTag3;
        @Bind(R.id.tags)
        LinearLayout tags;
        @Bind(R.id.tvDescription)
        TextView tvDescription;
        @Bind(R.id.llRoot)
        LinearLayout llRoot;

        public InfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public class RecommendedViewHolder extends RecyclerView.ViewHolder {

        public RecommendedViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {

        public ArticleViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder {

        public PlaceViewHolder(View itemView) {
            super(itemView);
        }
    }


}
