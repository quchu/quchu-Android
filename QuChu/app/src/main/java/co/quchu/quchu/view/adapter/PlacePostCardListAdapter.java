package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.analysis.GatherCollectModel;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.model.PostCardItemModel;
import co.quchu.quchu.model.PostCardModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.presenter.InterestingDetailPresenter;
import co.quchu.quchu.presenter.PostCardPresenter;
import co.quchu.quchu.utils.FlyMeUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.activity.AddPostCardActivity;
import co.quchu.quchu.view.activity.PostCardImageActivity;
import co.quchu.quchu.widget.ratingbar.ProperRatingBar;

/**
 * PlacePostCardListAdapter
 * User: Chenhs
 * Date: 2015-12-18
 */
public class PlacePostCardListAdapter extends RecyclerView.Adapter<PlacePostCardListAdapter.PPCHolder> {


    private Context mContext;
    private boolean isFlyme = false;
    private List<PostCardItemModel> arrayList;
    private CardClickListener listener;

    public PlacePostCardListAdapter(Context mContext, CardClickListener listener) {
        this.mContext = mContext;
        isFlyme = FlyMeUtils.isFlyme();
        this.listener = listener;
    }

    public void changeDataSet(List<PostCardItemModel> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public PPCHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PPCHolder holder = new PPCHolder(LayoutInflater.from(mContext).inflate(R.layout.item_my_postcard_list_view, parent, false), listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(PPCHolder holder, int position) {
        PostCardItemModel model = arrayList.get(position);
        if (!StringUtils.isEmpty(model.getRgb())) {
            holder.rootCv.setCardBackgroundColor(Color.parseColor("#" + model.getRgb()));
        } else {
            holder.rootCv.setCardBackgroundColor(Color.parseColor("#808080"));
        }
        if (StringUtils.isEmpty(model.getPlcaeCover())) {
            holder.itemRecommendCardPhotoSdv.setImageURI(Uri.parse(model.getImglist().get(0).getPath()));
        } else {
            holder.itemRecommendCardPhotoSdv.setImageURI(Uri.parse(model.getPlcaeCover()));
        }
        if (isFlyme) {
            holder.itemRecommendCardPhotoSdv.setAspectRatio(1.45f);
        } else {
            holder.itemRecommendCardPhotoSdv.setAspectRatio(1.33f);
        }
        holder.itemMyPostcardCardTiemTv.setText(StringUtils.isEmpty(model.getTime()) ? "" : model.getTime().substring(0, 10));
        holder.itemRecommendCardCityTv.setText(model.getPlcaeAddress());
        holder.itemRecommendCardNameTv.setText(model.getPlcaeName());
        holder.itemMyPostcardCardPrb.setRating((int) (model.getScore() + 0.5) >= 5 ? 5 : (model.getScore()));
        holder.itemRecommendCardCollectIv.setImageDrawable(mContext.getResources().getDrawable(model.isIsf() ? R.drawable.ic_detail_collect : R.drawable.ic_detail_uncollect));
        if (model.isIsme()) {
            holder.itemMyPostcardCardHeartIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_post_card_editer));
        } else {
            holder.itemMyPostcardCardHeartIv.setImageDrawable(mContext.getResources().getDrawable(model.isIsp() ? R.drawable.ic_detail_heart_full : R.drawable.ic_detail_heart));
        }
        if (model.getImglist().size() > 0) {
            holder.item_recommend_card_photo_num_tv.setVisibility(View.VISIBLE);
            holder.item_recommend_card_photo_num_tv.setText("1/" + model.getImglist().size());
        } else {
            holder.item_recommend_card_photo_num_tv.setVisibility(View.INVISIBLE);
        }
        holder.itemMyPostcardAvatarSdv.setImageURI(Uri.parse(model.getAutorPhoto()));
        holder.itemMyPostcardCardNicknameTv.setText(model.getAutor());
        holder.itemMyPostcardCardCommentTv.setText(model.getComment());

    }


    @Override
    public int getItemCount() {
        if (arrayList == null)
            return 0;
        else
            return arrayList.size();
    }

    class PPCHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_recommend_card_name_tv)
        TextView itemRecommendCardNameTv;
        @Bind(R.id.item_recommend_card_city_tv)
        TextView itemRecommendCardCityTv;
        @Bind(R.id.item_recommend_card_title_textrl)
        RelativeLayout itemRecommendCardTitleTextrl;
        @Bind(R.id.item_recommend_card_photo_sdv)
        SimpleDraweeView itemRecommendCardPhotoSdv;
        @Bind(R.id.item_my_postcard_avatar_sdv)
        SimpleDraweeView itemMyPostcardAvatarSdv;
        @Bind(R.id.item_my_postcard_card_prb)
        ProperRatingBar itemMyPostcardCardPrb;
        @Bind(R.id.item_my_postcard_card_nickname_tv)
        TextView itemMyPostcardCardNicknameTv;
        @Bind(R.id.item_my_postcard_card_tiem_tv)
        TextView itemMyPostcardCardTiemTv;
        @Bind(R.id.item_my_postcard_card_comment_tv)
        TextView itemMyPostcardCardCommentTv;
        @Bind(R.id.item_my_postcard_card_heart_iv)
        ImageView itemMyPostcardCardHeartIv;
        @Bind(R.id.item_my_postcard_heart_rl)
        RelativeLayout itemMyPostcardHeartRl;
        @Bind(R.id.item_recommend_card_collect_iv)
        ImageView itemRecommendCardCollectIv;
        @Bind(R.id.item_recommend_card_collect_rl)
        RelativeLayout itemRecommendCardCollectRl;
        @Bind(R.id.item_recommend_card_interest_iv)
        ImageView itemRecommendCardInterestIv;
        @Bind(R.id.item_recommend_card_interest_rl)
        RelativeLayout itemRecommendCardInterestRl;
        @Bind(R.id.item_recommend_card_reply_Iv)
        ImageView itemRecommendCardReplyIv;
        @Bind(R.id.item_recommend_card_reply_rl)
        RelativeLayout itemRecommendCardReplyRl;
        @Bind(R.id.root_cv)
        CardView rootCv;
        @Bind(R.id.item_recommend_card_photo_num_tv)
        TextView item_recommend_card_photo_num_tv;
        private CardClickListener listener;

        public PPCHolder(View itemView, CardClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;

        }

        @OnClick({R.id.root_cv, R.id.item_recommend_card_collect_rl, R.id.item_recommend_card_interest_rl, R.id.item_recommend_card_reply_rl,
                R.id.item_recommend_card_photo_sdv, R.id.item_my_postcard_heart_rl})
        public void cardClick(View view) {
            switch (view.getId()) {
                case R.id.item_recommend_card_collect_rl:
                    setFavorite(getPosition());
                    break;
                case R.id.item_my_postcard_heart_rl:
                    if (arrayList.get(getPosition()).isIsme()) {
                        Intent intent = new Intent(mContext, AddPostCardActivity.class).putExtra("pName", arrayList.get(getPosition()).getPlcaeName());
                        intent.putExtra("pId", arrayList.get(getPosition()).getPlaceId());
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable("pCardModel", arrayList.get(getPosition()));
                        intent.putExtras(mBundle);
                        mContext.startActivity(intent);
                    } else {
                        doParise(getPosition());
                    }
                    break;
                case R.id.item_recommend_card_photo_sdv:
                    if (arrayList.get(getPosition()).getImglist().size() > 0) {
                        Intent intent = new Intent(mContext, PostCardImageActivity.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable("pCardModel", arrayList.get(getPosition()));
                        intent.putExtras(mBundle);
                        mContext.startActivity(intent);
                    }
                    break;
            }
            if (listener != null)
                listener.onCardLick(view, getPosition());
        }
    }

    private void doParise(final int positions) {

        PostCardPresenter.setPraise(mContext, arrayList.get(positions).isIsp(), true, arrayList.get(positions).getCardId(), new PostCardPresenter.MyPostCardListener() {
            @Override
            public void onSuccess(PostCardModel model) {
                arrayList.get(positions).setIsp(!arrayList.get(positions).isIsp());
                notifyDataSetChanged();
                if (arrayList.get(positions).isIsp()) {
                    Toast.makeText(mContext, "点赞成功!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "取消点赞!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    public interface CardClickListener {
        void onCardLick(View view, int position);
    }

    private void setFavorite(final int position) {
        setDetailFavorite(mContext, arrayList.get(position).getCardId(), arrayList.get(position).isIsf(), new InterestingDetailPresenter.DetailDataListener() {
            @Override
            public void onSuccessCall(String str) {
                arrayList.get(position).setIsf(!arrayList.get(position).isIsf());
                notifyDataSetChanged();
                if (arrayList.get(position).isIsf()) {
                    Toast.makeText(mContext, "收藏成功!", Toast.LENGTH_SHORT).show();
                    AppContext.gatherDataModel.collectList.add(new GatherCollectModel(GatherCollectModel.collectCard, arrayList.get(position).getCardId() + ""));
                } else {
                    Toast.makeText(mContext, "取消收藏!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorCall(String str) {

            }
        });
    }

    public void setDetailFavorite(Context context, int pId, boolean isFavorite, final InterestingDetailPresenter.DetailDataListener listener) {
        String favoUrl = "";
        if (isFavorite) {
            favoUrl = String.format(NetApi.userDelFavorite, pId, NetApi.FavTypeCard);
        } else {
            favoUrl = String.format(NetApi.userFavorite, pId, NetApi.FavTypeCard);
        }
        NetService.get(context, favoUrl, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                listener.onSuccessCall("");
            }

            @Override
            public boolean onError(String error) {
                listener.onErrorCall("");
                return false;
            }
        });
    }
}