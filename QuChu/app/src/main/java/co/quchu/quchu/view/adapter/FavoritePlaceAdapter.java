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

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.model.FavoritePlaceModel;
import co.quchu.quchu.presenter.InterestingDetailPresenter;
import co.quchu.quchu.utils.FlyMeUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.widget.ratingbar.ProperRatingBar;

/**
 * RecommendAdapter
 * User: Chenhs
 * Date: 2015-12-08
 * 我收藏的趣处 适配器 adapter
 */
public class FavoritePlaceAdapter extends RecyclerView.Adapter<FavoritePlaceAdapter.FavoritePlaceHolder> {


    private Context mContext;
    private boolean isFlyme = false;
    private List<FavoritePlaceModel.ResultEntity> arrayList;
    private CardClickListener listener;

    public FavoritePlaceAdapter(Context mContext, CardClickListener listener) {
        this.mContext = mContext;
        isFlyme = FlyMeUtils.isFlyme();
        this.listener = listener;
    }

    public void changeDataSet(List<FavoritePlaceModel.ResultEntity> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public FavoritePlaceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FavoritePlaceHolder holder = new FavoritePlaceHolder(LayoutInflater.from(mContext).inflate(R.layout.item_favorite_place_cardview, parent, false), listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(FavoritePlaceHolder holder, int position) {
        FavoritePlaceModel.ResultEntity model = arrayList.get(position);
        if (!StringUtils.isEmpty(model.getRgb())) {
            holder.rootCv.setCardBackgroundColor(Color.parseColor("#" + model.getRgb()));
        } else {
            holder.rootCv.setCardBackgroundColor(Color.parseColor("#808080"));
        }
        holder.itemRecommendCardPhotoSdv.setImageURI(Uri.parse(model.getCover()));
        if (isFlyme) {
            holder.itemRecommendCardPhotoSdv.setAspectRatio(1.45f);
        } else {
            holder.itemRecommendCardPhotoSdv.setAspectRatio(1.33f);
        }
        holder.itemRecommendCardAddressTv.setText(model.getAddress());
        holder.itemRecommendCardCityTv.setText(model.getDescribe());
        holder.itemRecommendCardNameTv.setText(model.getName());
        holder.itemRecommendCardPrb.setRating((int) (model.getSuggest() + 0.5) >= 5 ? 5 : (model.getSuggest()));
        holder.itemRecommendCardCollectIv.setImageDrawable(mContext.getResources().getDrawable(model.isIsf() ? R.drawable.ic_detail_collect : R.drawable.ic_detail_uncollect));

    }


    @Override
    public int getItemCount() {
        if (arrayList == null)
            return 0;
        else
            return arrayList.size();
    }

    class FavoritePlaceHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_recommend_card_name_tv)
        TextView itemRecommendCardNameTv;
        @Bind(R.id.item_recommend_card_city_tv)
        TextView itemRecommendCardCityTv;
        @Bind(R.id.item_recommend_card_photo_sdv)
        SimpleDraweeView itemRecommendCardPhotoSdv;
        @Bind(R.id.item_recommend_card_prb)
        ProperRatingBar itemRecommendCardPrb;
        @Bind(R.id.item_recommend_card_address_tv)
        TextView itemRecommendCardAddressTv;
        @Bind(R.id.item_recommend_card_collect_rl)
        RelativeLayout itemRecommendCardCollectRl;
        @Bind(R.id.item_recommend_card_interest_rl)
        RelativeLayout itemRecommendCardInterestRl;
        @Bind(R.id.item_recommend_card_reply_rl)
        RelativeLayout itemRecommendCardReplyRl;

        @Bind(R.id.root_cv)
        CardView rootCv;
        @Bind(R.id.item_recommend_card_collect_iv)
        ImageView itemRecommendCardCollectIv;
        private CardClickListener listener;

        public FavoritePlaceHolder(View itemView, CardClickListener listener) {
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
        InterestingDetailPresenter.setDetailFavorite(mContext, arrayList.get(position).getPid(), arrayList.get(position).isIsf(), new InterestingDetailPresenter.DetailDataListener() {
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
}
