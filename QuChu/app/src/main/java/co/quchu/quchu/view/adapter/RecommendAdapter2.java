package co.quchu.quchu.view.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.DynamicDrawableSpan;
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
import co.quchu.quchu.utils.MIUIUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.widget.ratingbar.ProperRatingBar;

/**
 * RecommendAdapter
 * User: Chenhs
 * Date: 2015-12-08
 * 趣处推荐 适配器 adapter
 */
public class RecommendAdapter2 extends RecyclerView.Adapter<RecommendAdapter2.RecommendHolder> {


    private Activity mContext;
    private boolean isFlyme = false,isMIUI=false;
    private List<RecommendModel> arrayList;
    private CardClickListener listener;

    public RecommendAdapter2(Activity mContext, CardClickListener listener) {
        this.mContext = mContext;
        isFlyme = FlyMeUtils.isFlyme();
        isMIUI= MIUIUtils.isMIUI();
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
    public RecommendAdapter2.RecommendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      /*  if (isMIUI) {*/
            return new RecommendHolder(LayoutInflater.from(mContext).inflate(R.layout.item_recommend_cardview_new_miui, parent, false), listener);
       /* }else {
            return new RecommendHolder(LayoutInflater.from(mContext).inflate(R.layout.item_recommend_cardview_new_other, parent, false), listener);
        }*/
    }

    @Override
    public void onBindViewHolder(RecommendAdapter2.RecommendHolder holder, int position) {
        RecommendModel model = arrayList.get(position);
        holder.rootCv.setCardBackgroundColor(Color.parseColor("#E6EEEFEF"));
        holder.itemRecommendCardPhotoSdv.setImageURI(Uri.parse(model.getCover()));
        if (!model.isIsActivity()) {
            holder.item_place_event_tv.setVisibility(View.VISIBLE);
        } else {
            holder.item_place_event_tv.setVisibility(View.GONE);
        }
        holder.itemRecommendCardAddressTv.setText(model.getAddress());
        holder.itemRecommendCardPrb.setRating((int) ((model.getSuggest() + 0.5f) >= 5 ? 5 : (model.getSuggest())));
        holder.itemRecommendCardCollectIv.setImageResource(
                model.isIsf() ? R.drawable.ic_detail_collect : R.drawable.ic_detail_uncollect);
        holder.item_recommend_card_name_tv.setText(model.getName());

        List<RecommendModel.GenesEntity> genes = model.getGenes();
        holder.tag1.setText(genes.get(0).getKey());
        holder.tag2.setText(genes.get(1).getKey());
        holder.tag3.setText(genes.get(2).getKey());

        holder.itemRecommendCardCollectIv.setImageDrawable(mContext.getResources().
                getDrawable(model.isIsf() ? R.drawable.ic_detail_collect : R.drawable.ic_detail_uncollect));

        if (true) {//用户去过该趣处
            //去过标签 start
            SpannableString spanText = new SpannableString(model.getName() + "，");
            DynamicDrawableSpan drawableSpan2 = new DynamicDrawableSpan(
                    DynamicDrawableSpan.ALIGN_BOTTOM) {
                @Override
                public Drawable getDrawable() {
                    Drawable d = mContext.getResources().getDrawable(R.drawable.recommend_actiion_tag);
                    d.setBounds(StringUtils.dip2px(8), -StringUtils.dip2px(16), StringUtils.dip2px(40), 0);
                    return d;
                }
            };
            spanText.setSpan(drawableSpan2, model.getName().length(), model.getName().length() + 1
                    , Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            holder.item_recommend_card_name_tv.setText(spanText);
            //去过标签 end
        } else {
            holder.item_recommend_card_name_tv.setText(model.getName());
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
        @Bind(R.id.item_recommend_card_photo_sdv)
        SimpleDraweeView itemRecommendCardPhotoSdv;
        @Bind(R.id.item_recommend_card_prb)
        ProperRatingBar itemRecommendCardPrb;
        @Bind(R.id.item_recommend_card_address_tv)
        TextView itemRecommendCardAddressTv;
        @Bind(R.id.item_place_event_tv)
        TextView item_place_event_tv;
        @Bind(R.id.item_recommend_card_name_tv)
        TextView item_recommend_card_name_tv;

        @Bind(R.id.recommend_action_tag1)
        TextView tag1;
        @Bind(R.id.recommend_action_tag2)
        TextView tag2;
        @Bind(R.id.recommend_action_tag3)
        TextView tag3;

        @Bind(R.id.root_cv)
        CardView rootCv;
        @Bind(R.id.item_recommend_card_collect_iv)
        ImageView itemRecommendCardCollectIv;
        private CardClickListener listener;

        public RecommendHolder(View itemView, CardClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
            if (isFlyme) {
                itemRecommendCardPhotoSdv.setAspectRatio(1.6f);
            } else {
                itemRecommendCardPhotoSdv.setAspectRatio(1.4f);
            }
        }

        @OnClick({R.id.root_cv, R.id.item_recommend_card_collect_iv, R.id.item_recommend_card_interest_iv})
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
