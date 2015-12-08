package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.utils.FlyMeUtils;
import co.quchu.quchu.widget.HorizontalNumProgressBar;
import co.quchu.quchu.widget.ratingbar.ProperRatingBar;

/**
 * RecommendAdapter
 * User: Chenhs
 * Date: 2015-12-08
 * 趣处推荐 适配器 adapter
 */
public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.RecommendHolder> {

    private Context mContext;
    private boolean isFlyme = false;
    private ArrayList<RecommendModel> arrayList;

    public RecommendAdapter(Context mContext) {
        this.mContext = mContext;
        isFlyme = FlyMeUtils.isFlyme();

    }

    public void changeDataSet(ArrayList<RecommendModel> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public RecommendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecommendHolder holder = new RecommendHolder(LayoutInflater.from(mContext).inflate(R.layout.item_recommend_cardview, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecommendHolder holder, int position) {
        RecommendModel model = arrayList.get(position);
        holder.rootCv.setCardBackgroundColor(Color.parseColor("#" + model.getRgb()));
        holder.itemRecommendCardPhotoSdv.setImageURI(Uri.parse(model.getCover()));
        if (isFlyme) {
            holder.itemRecommendCardPhotoSdv.setAspectRatio(1.9f);
        } else {
            holder.itemRecommendCardPhotoSdv.setAspectRatio(1.33f);
        }
        holder.itemRecommendCardAddressTv.setText(model.getAddress());
        holder.itemRecommendCardCityTv.setText(model.getDescribe());
        holder.itemRecommendCardNameTv.setText(model.getName());
        if (model.getGenes().size() >= 3) {
            holder.itemRecommendCardProgressOne.setProgress(model.getGenes().get(0).getValue());
            holder.itemRecommendCardProgressNameOne.setText(model.getGenes().get(0).getKey());
            holder.itemRecommendCardProgressTwo.setProgress(model.getGenes().get(1).getValue());
            holder.itemRecommendCardProgressNameTwo.setText(model.getGenes().get(1).getKey());
            holder.itemRecommendCardProgressThree.setProgress(model.getGenes().get(2).getValue());
            holder.itemRecommendCardProgressNameThree.setText(model.getGenes().get(2).getKey());
            holder.itemRecommendCardProgressOneLl.setVisibility(View.VISIBLE);
            holder.itemRecommendCardProgressTwoLl.setVisibility(View.VISIBLE);
            holder.itemRecommendCardProgressThreeLl.setVisibility(View.VISIBLE);
        }else {
            holder.itemRecommendCardProgressOneLl.setVisibility(View.INVISIBLE);
            holder.itemRecommendCardProgressTwoLl.setVisibility(View.INVISIBLE);
            holder.itemRecommendCardProgressThreeLl.setVisibility(View.INVISIBLE);
        }
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
        @Bind(R.id.item_recommend_card_collect_rl)
        RelativeLayout itemRecommendCardCollectRl;
        @Bind(R.id.item_recommend_card_interest_rl)
        RelativeLayout itemRecommendCardInterestRl;
        @Bind(R.id.item_recommend_card_reply_rl)
        RelativeLayout itemRecommendCardReplyRl;

        @Bind(R.id.item_recommend_card_progress_name_one)
        TextView itemRecommendCardProgressNameOne;
        @Bind(R.id.item_recommend_card_progress_one)
        HorizontalNumProgressBar itemRecommendCardProgressOne;
        @Bind(R.id.item_recommend_card_progress_one_ll)
        LinearLayout itemRecommendCardProgressOneLl;
        @Bind(R.id.item_recommend_card_progress_name_two)
        TextView itemRecommendCardProgressNameTwo;
        @Bind(R.id.item_recommend_card_progress_two)
        HorizontalNumProgressBar itemRecommendCardProgressTwo;
        @Bind(R.id.item_recommend_card_progress_two_ll)
        LinearLayout itemRecommendCardProgressTwoLl;
        @Bind(R.id.item_recommend_card_progress_name_three)
        TextView itemRecommendCardProgressNameThree;
        @Bind(R.id.item_recommend_card_progress_three)
        HorizontalNumProgressBar itemRecommendCardProgressThree;
        @Bind(R.id.item_recommend_card_progress_three_ll)
        LinearLayout itemRecommendCardProgressThreeLl;
        @Bind(R.id.root_cv)
        CardView rootCv;

        public RecommendHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
