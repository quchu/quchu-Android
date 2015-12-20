package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
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
import co.quchu.quchu.model.PlacePostCardModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.presenter.InterestingDetailPresenter;
import co.quchu.quchu.utils.FlyMeUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.widget.ratingbar.ProperRatingBar;

/**
 * PlacePostCardListAdapter
 * User: Chenhs
 * Date: 2015-12-18
 */
public class PlacePostCardListAdapter extends RecyclerView.Adapter<PlacePostCardListAdapter.PPCHolder> {


    private Context mContext;
    private boolean isFlyme = false;
    private List<PlacePostCardModel.PageEntity.pPostCardEntity> arrayList;
    private CardClickListener listener;

    public PlacePostCardListAdapter(Context mContext, CardClickListener listener) {
        this.mContext = mContext;
        isFlyme = FlyMeUtils.isFlyme();
        this.listener = listener;
    }

    public void changeDataSet(List<PlacePostCardModel.PageEntity.pPostCardEntity> arrayList) {
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
        PlacePostCardModel.PageEntity.pPostCardEntity model = arrayList.get(position);
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
        private CardClickListener listener;

        public PPCHolder(View itemView, CardClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;

        }

        @OnClick({R.id.root_cv, R.id.item_recommend_card_collect_rl, R.id.item_recommend_card_interest_rl, R.id.item_recommend_card_reply_rl})
        public void cardClick(View view) {
            switch (view.getId()) {
                case R.id.item_recommend_card_collect_rl:
                    setFavorite(getPosition());
                    break;

            }
            if (listener != null)
                listener.onCardLick(view, getPosition());
        }
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