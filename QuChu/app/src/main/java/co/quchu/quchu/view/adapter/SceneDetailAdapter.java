package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.net.Uri;
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
import co.quchu.quchu.model.DetailModel;
import co.quchu.quchu.model.SceneDetailModel;
import co.quchu.quchu.model.SceneHeaderModel;

/**
 * Created by Nico on 16/7/8.
 */
public class SceneDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private SceneDetailModel mData;

    private OnSceneItemClickListener mListener;

    public static final int TYPE_INFO = 0x001;
    public static final int TYPE_RECOMMENDED = 0x002;
    public static final int TYPE_ARTICLE = 0x003;
    public static final int TYPE_PLACE_LIST = 0x004;


    public SceneDetailAdapter(Context mContext, SceneDetailModel sceneDetailModel, OnSceneItemClickListener listener) {
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
                return new InfoViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_scene_detail_info, parent, false));
            case TYPE_RECOMMENDED:
                return new RecommendedViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_scene_detail_recommeded, parent, false));
            case TYPE_ARTICLE:
                //if (null!=mData.getArticleModel()){}
                return new ArticleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_article_detail, parent, false));
            case TYPE_PLACE_LIST:
                return new PlaceViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_scene_detail_recommeded, parent, false));
            default:
                return new QuchuDetailsAdapter.BlankViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_quchu_detail_blank, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (null == mData) {
            return;
        }
        if (holder instanceof InfoViewHolder) {

            ((InfoViewHolder) holder).sdvCover.setImageURI(Uri.parse(mData.getSceneInfo().getSceneCover()));
            ((InfoViewHolder) holder).desc.setText(mData.getSceneInfo().getSceneName());
            String[] tags = mData.getSceneInfo().getSceneTitle();
            ((InfoViewHolder) holder).recommendTag1.setVisibility(View.GONE);
            ((InfoViewHolder) holder).recommendTag2.setVisibility(View.GONE);
            ((InfoViewHolder) holder).recommendTag3.setVisibility(View.GONE);
            for (int i = 0; i < tags.length; i++) {
                switch (i) {
                    case 0:
                        ((InfoViewHolder) holder).recommendTag1.setVisibility(View.VISIBLE);
                        ((InfoViewHolder) holder).recommendTag1.setText(tags[i]);
                        break;
                    case 1:
                        ((InfoViewHolder) holder).recommendTag2.setVisibility(View.VISIBLE);
                        ((InfoViewHolder) holder).recommendTag2.setText(tags[i]);
                        break;
                    case 2:
                        ((InfoViewHolder) holder).recommendTag3.setVisibility(View.VISIBLE);
                        ((InfoViewHolder) holder).recommendTag3.setText(tags[i]);
                        break;
                }
            }


        } else if (holder instanceof RecommendedViewHolder) {

            final SceneHeaderModel objScene = mData.getBestList().get(position-1);
            ((RecommendedViewHolder) holder).sdvCover.setImageURI(Uri.parse(objScene.getPlaceInfo().getCover()));
            ((RecommendedViewHolder) holder).tvTitle.setText(objScene.getPlaceInfo().getName());
            ((RecommendedViewHolder) holder).tvHeader.setText(objScene.getTitle());

            ((RecommendedViewHolder) holder).recommendTag1.setVisibility(View.GONE);
            ((RecommendedViewHolder) holder).recommendTag2.setVisibility(View.GONE);
            ((RecommendedViewHolder) holder).recommendTag3.setVisibility(View.GONE);
            for (int i = 0; i < objScene.getPlaceInfo().getTags().size(); i++) {
                if (((RecommendedViewHolder) holder).tags.getChildAt(i)!=null){
                    ((RecommendedViewHolder) holder).tags.getChildAt(i).setVisibility(View.VISIBLE);
                    ((TextView)((RecommendedViewHolder) holder).tags.getChildAt(i)).setText(objScene.getPlaceInfo().getTags().get(i).getZh());
                }

            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null!=mListener) {
                        mListener.onPlaceClick(objScene.getPlaceInfo().getPid());
                    }
                }
            });


        } else if (holder instanceof ArticleViewHolder) {

            if (null!=mData.getArticleModel()){
                ((ArticleViewHolder) holder).sdvCover.setImageURI(Uri.parse(mData.getArticleModel().getImageUrl()));
                ((ArticleViewHolder) holder).tvTitle.setText(mData.getArticleModel().getArticleName());
                ((ArticleViewHolder) holder).tvDescription.setText(mData.getArticleModel().getArticleComtent());
            }

        } else if (holder instanceof PlaceViewHolder) {

            final DetailModel objScene = mData.getPlaceList().getResult().get(position-2-getRecommendedListSize());
            ((PlaceViewHolder) holder).sdvCover.setImageURI(Uri.parse(objScene.getCover()));
            ((PlaceViewHolder) holder).tvTitle.setText(objScene.getName());
            ((PlaceViewHolder) holder).tvHeader.setVisibility(View.GONE);

            ((PlaceViewHolder) holder).recommendTag1.setVisibility(View.GONE);
            ((PlaceViewHolder) holder).recommendTag2.setVisibility(View.GONE);
            ((PlaceViewHolder) holder).recommendTag3.setVisibility(View.GONE);
            for (int i = 0; i < objScene.getTags().size(); i++) {
                if (null!=((PlaceViewHolder) holder).tags.getChildAt(i)){
                    ((PlaceViewHolder) holder).tags.getChildAt(i).setVisibility(View.VISIBLE);
                    ((TextView)((PlaceViewHolder) holder).tags.getChildAt(i)).setText(objScene.getTags().get(i).getZh());
                }

            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null!=mListener) {
                        mListener.onPlaceClick(objScene.getPid());
                    }
                }
            });
        }

    }


    public interface OnSceneItemClickListener{
        void onArticleClick();
        void onPlaceClick(int pid);
    }

    private int getRecommendedListSize() {
        int recommended = 0;

        if (null != mData && null != mData.getBestList()) {

            recommended += mData.getBestList().size();
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

        @Bind(R.id.sdvCover)
        SimpleDraweeView sdvCover;
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
            ButterKnife.bind(this, itemView);
        }
    }

    public class RecommendedViewHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.sdvCover)
        SimpleDraweeView sdvCover;
        @Bind(R.id.tvHeader)
        TextView tvHeader;
        @Bind(R.id.tvTitle)
        TextView tvTitle;
        @Bind(R.id.recommend_tag1)
        TextView recommendTag1;
        @Bind(R.id.recommend_tag2)
        TextView recommendTag2;
        @Bind(R.id.recommend_tag3)
        TextView recommendTag3;
        @Bind(R.id.tags)
        LinearLayout tags;

        public RecommendedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.sdvCover)
        SimpleDraweeView sdvCover;
        @Bind(R.id.tvTitle)
        TextView tvTitle;
        @Bind(R.id.tvDescription)
        TextView tvDescription;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.sdvCover)
        SimpleDraweeView sdvCover;
        @Bind(R.id.tvHeader)
        TextView tvHeader;
        @Bind(R.id.tvTitle)
        TextView tvTitle;
        @Bind(R.id.recommend_tag1)
        TextView recommendTag1;
        @Bind(R.id.recommend_tag2)
        TextView recommendTag2;
        @Bind(R.id.recommend_tag3)
        TextView recommendTag3;
        @Bind(R.id.tags)
        LinearLayout tags;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
