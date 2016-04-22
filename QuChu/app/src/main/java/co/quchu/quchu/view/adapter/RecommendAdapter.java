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
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.utils.FlyMeUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.widget.ratingbar.ProperRatingBar;

/**
 * RecommendAdapter
 * User: Chenhs
 * Date: 2015-12-08
 * 趣处推荐 适配器 adapter
 */
public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.RecommendHolder> {


    private Activity mContext;
    private boolean isFlyme = false;
    private List<RecommendModel> dataSet;
    private CardClickListener listener;

    public RecommendAdapter(Activity mContext, List<RecommendModel> arrayList, CardClickListener listener) {
        this.mContext = mContext;
        isFlyme = FlyMeUtils.isFlyme();
        dataSet = arrayList;
        this.listener = listener;
    }


    @Override
    public RecommendAdapter.RecommendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecommendHolder(LayoutInflater.from(mContext).inflate(R.layout.item_recommend_cardview_new_miui, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(RecommendAdapter.RecommendHolder holder, int position) {
        RecommendModel model = dataSet.get(position);
        holder.rootCv.setCardBackgroundColor(Color.parseColor("#E6EEEFEF"));
        //ImageUtils.loadWithAppropriateSize(holder.itemRecommendCardPhotoSdv,Uri.parse(model.getCover()));
        holder.itemRecommendCardPhotoSdv.setImageURI(Uri.parse(model.getCover()));
        if (model.isIsActivity()) {
            holder.item_place_event_tv.setVisibility(View.VISIBLE);
        } else {
            holder.item_place_event_tv.setVisibility(View.GONE);
        }
        holder.itemRecommendCardAddressTv.setText(model.getAddress());
        holder.itemRecommendCardPrb.setRating((int) ((model.getSuggest() + 0.5f) >= 5 ? 5 : (model.getSuggest())));
        holder.item_recommend_card_name_tv.setText(model.getName());

//        List<RecommendModel.GenesEntity> genes = model.getGenes();
//        holder.tag1.setText(genes.get(0).getKey());
//        holder.tag2.setText(genes.get(1).getKey());
//        holder.tag3.setText(genes.get(2).getKey());

        //holder.itemRecommendCardCollectIv.setImageDrawable(mContext.getResources().getDrawable(model.isIsf() ? R.mipmap.ic_detail_collect_dark : R.mipmap.ic_atmophere_unselected_dark));

        if (model.isout) {//用户去过该趣处
            //去过标签 start
            SpannableString spanText = new SpannableString(model.getName() + "，");
            DynamicDrawableSpan drawableSpan2 = new DynamicDrawableSpan(
                    DynamicDrawableSpan.ALIGN_BOTTOM) {
                @Override
                public Drawable getDrawable() {
                    Drawable d = mContext.getResources().getDrawable(R.mipmap.ic_span_been);
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
        if (null != model.getTags() && model.getTags().size() > 0) {
            //ArrayList<String> tags = new ArrayList<String>();
            for (int i = 0; i < model.getTags().size(); i++) {
                switch (i){
                    case 0:
                        holder.tag1.setText(model.getTags().get(i).getZh());
                        holder.tag1.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        holder.tag2.setText(model.getTags().get(i).getZh());
                        holder.tag2.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        holder.tag3.setText(model.getTags().get(i).getZh());
                        holder.tag3.setVisibility(View.VISIBLE);
                        break;
                }
            }
//            holder.detailStoreTagcloundTcv.setVisibility(View.VISIBLE);
//            holder.detailStoreTagcloundTcv.setTags(tags);
        } else {
            //holder.detailStoreTagcloundTcv.setVisibility(View.INVISIBLE);
            holder.tag1.setVisibility(View.GONE);
            holder.tag2.setVisibility(View.GONE);
            holder.tag3.setVisibility(View.GONE);
        }
        System.out.println(SPUtils.getLatitude() +"|"+SPUtils.getLongitude());
        if (0 == SPUtils.getLatitude() && 0 == SPUtils.getLongitude()) {
            holder.item_recommend_card_distance_tv.setVisibility(View.GONE);
        } else {
            String distance = StringUtils.formatDouble(Double.parseDouble(model.getDistance())) + "km";
            holder.item_recommend_card_distance_tv.setText("距您" + distance);
//            if (StringUtils.isDouble(model.getDistance())) {
//
//                holder.item_recommend_card_distance_tv.setText("距您" + distance);
//                StringUtils.alterBoldTextColor(holder.item_recommend_card_distance_tv, 2, 2 + distance.length(), R.color.white);
//            }
        }

    }

    @Override
    public int getItemCount() {
        if (dataSet == null)
            return 0;
        else
            return dataSet.size();
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

        @Bind(R.id.recommend_tag1)
        TextView tag1;
        @Bind(R.id.recommend_tag2)
        TextView tag2;
        @Bind(R.id.recommend_tag3)
        TextView tag3;

        @Bind(R.id.root_cv)
        CardView rootCv;
        @Bind(R.id.item_recommend_card_collect_iv)
        TextView itemRecommendCardCollectIv;
        //        @Bind(R.id.detail_store_tagclound_tcv)
//        TagCloudView detailStoreTagcloundTcv;
        @Bind(R.id.item_recommend_card_distance_tv)
        TextView item_recommend_card_distance_tv;
        private CardClickListener listener;

        public RecommendHolder(View itemView, CardClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
//            if (isFlyme) {
//                itemRecommendCardPhotoSdv.setAspectRatio(1.4f);
//            } else {
//                itemRecommendCardPhotoSdv.setAspectRatio(1.2f);
//            }
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
