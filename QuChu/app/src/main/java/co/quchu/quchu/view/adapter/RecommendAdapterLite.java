package co.quchu.quchu.view.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.utils.FlyMeUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.widget.ratingbar.ProperRatingBar;

/**
 * RecommendAdapter
 * User: Chenhs
 * Date: 2015-12-08
 * 趣处推荐 适配器 adapter
 */
public class RecommendAdapterLite extends RecyclerView.Adapter<RecommendAdapterLite.RecommendHolder> {


    private Activity mContext;
    private boolean isFlyme = false;
    private List<RecommendModel> arrayList;
    private CardClickListener listener;

    public RecommendAdapterLite(Activity mContext, CardClickListener listener) {
        this.mContext = mContext;
        isFlyme = FlyMeUtils.isFlyme();
        this.listener = listener;
    }

    public void changeDataSet(List<RecommendModel> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    public void loadMoreDataSet(ArrayList<RecommendModel> arrayList) {
        this.arrayList.addAll(arrayList);
        notifyDataSetChanged();
    }


    @Override
    public RecommendAdapterLite.RecommendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecommendHolder(LayoutInflater.from(mContext).inflate(R.layout.item_recommend_cardview_lite, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(RecommendAdapterLite.RecommendHolder holder, int position) {
        RecommendModel model = arrayList.get(position);

        if (!StringUtils.isEmpty(model.getRgb())) {
            holder.rootCv.setCardBackgroundColor(Color.parseColor("#" + model.getRgb()));
        } else {
            holder.rootCv.setCardBackgroundColor(Color.parseColor("#808080"));
        }
        holder.itemRecommendCardPhotoSdv.setImageURI(Uri.parse(model.getCover()));
        if (isFlyme) {
            holder.itemRecommendCardPhotoSdv.setAspectRatio(1.5f);
        } else {
            holder.itemRecommendCardPhotoSdv.setAspectRatio(1.33f);
        }
        if (model.isIsActivity()) {
            holder.item_place_event_tv.setVisibility(View.VISIBLE);
        } else {
            holder.item_place_event_tv.setVisibility(View.GONE);
        }
        holder.itemRecommendCardAddressTv.setText(model.getAddress());
        holder.itemRecommendCardCityTv.setText(model.getDescribe());
        holder.itemRecommendCardNameTv.setText(model.getName());
        holder.itemRecommendCardPrb.setRating((int) ((model.getSuggest() + 0.5f) >= 5 ? 5 : (model.getSuggest())));
        holder.itemRecommendCardCollectIv.setImageDrawable(mContext.getResources().
                getDrawable(model.isIsf() ? R.drawable.ic_detail_collect : R.drawable.ic_detail_uncollect));

    }

    @Override
    public int getItemCount() {
        if (arrayList == null)
            return 0;
        else
            return arrayList.size();
    }

    class RecommendHolder extends RecyclerView.ViewHolder {
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
        @Bind(R.id.item_place_event_tv)
        TextView item_place_event_tv;
//        @Bind(R.id.item_recommend_card_collect_rl)
//        RelativeLayout itemRecommendCardCollectRl;
//        @Bind(R.id.item_recommend_card_interest_rl)
//        RelativeLayout itemRecommendCardInterestRl;
//        @Bind(R.id.item_recommend_card_reply_rl)
//        RelativeLayout itemRecommendCardReplyRl;

        @Bind(R.id.root_cv)
        CardView rootCv;
        @Bind(R.id.item_recommend_card_collect_iv)
        ImageView itemRecommendCardCollectIv;
        private CardClickListener listener;

        public RecommendHolder(View itemView, CardClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
        }

        @OnClick({R.id.root_cv, R.id.item_recommend_card_collect_rl, R.id.item_recommend_card_interest_rl})
        public void cardClick(View view) {
            if (isFastDoubleClick())
                return;
            if (listener != null)
                listener.onCardLick(view, getPosition());
        }
    }

    private static long lastClickTime = 0L;

    /**
     * 防止重复点击
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public interface CardClickListener {
        void onCardLick(View view, int position);
    }

}