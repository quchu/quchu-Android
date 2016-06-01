package co.quchu.quchu.view.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;

/**
 * RecommendAdapter
 * User: Chenhs
 * Date: 2015-12-08
 * 趣处推荐 适配器 adapter
 */
public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.RecommendHolder> {


    private Activity mContext;
    private List<RecommendModel> dataSet;
    private CardClickListener listener;

    public RecommendAdapter(Activity mContext, List<RecommendModel> arrayList, CardClickListener listener) {
        this.mContext = mContext;
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
        holder.itemRecommendCardPhotoSdv.setImageURI(Uri.parse(model.getCover()));
        if (model.isIsActivity()) {
            holder.item_place_event_tv.setVisibility(View.VISIBLE);
        } else {
            holder.item_place_event_tv.setVisibility(View.GONE);
        }

        //Html.fromHtml(mContext.getString(R.string.avg_cost)
        // +"<font color=#"+Integer.toHexString(mContext.getResources().getColor(R.color.standard_color_red)& 0x00ffffff)+">"
        // +model.getPrice()+"</font>元")


        String price;
        if (!TextUtils.isEmpty(model.getPrice())){
            price = model.getPrice().split(",")[0];
        }else{
            price = "-";
        }

        holder.itemRecommendCardAddressTv.setText(StringUtils.getColorSpan(mContext,R.color.standard_color_red,mContext.getString(R.string.avg_cost_with_rmb_symbol),price,"起"));
        holder.rbRating.setRating(model.getSuggest());
        //holder.itemRecommendCardPrb.setRating((int) ((model.getSuggest() + 0.5f) >= 5 ? 5 : (model.getSuggest())));
        holder.item_recommend_card_name_tv.setText(model.getName());


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
            for (int i = 0; i < model.getTags().size(); i++) {
                switch (i) {
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
        } else {
            holder.tag1.setVisibility(View.GONE);
            holder.tag2.setVisibility(View.GONE);
            holder.tag3.setVisibility(View.GONE);
        }
        if (0 == SPUtils.getLatitude() || 0 == SPUtils.getLongitude()) {
            holder.item_recommend_card_distance_tv.setVisibility(View.GONE);
        } else {
            String distance = StringUtils.getDistance(model.getLatitude(), model.getLongitude(), SPUtils.getLatitude(), SPUtils.getLongitude());
            String text = "距您当前位置" + distance;

            SpannableStringBuilder builder = new SpannableStringBuilder();
            builder.append(text);
            ForegroundColorSpan span_1 = new ForegroundColorSpan(mContext.getResources().getColor(R.color.standard_color_red));
            builder.setSpan(span_1, 6, text.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.item_recommend_card_distance_tv.setText(builder);

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
        @Bind(R.id.photo)
        SimpleDraweeView itemRecommendCardPhotoSdv;
        @Bind(R.id.item_recommend_card_address_tv)
        TextView itemRecommendCardAddressTv;
        @Bind(R.id.activity)
        TextView item_place_event_tv;
        @Bind(R.id.desc)
        TextView item_recommend_card_name_tv;
        @Bind(R.id.recommend_tag1)
        TextView tag1;
        @Bind(R.id.recommend_tag2)
        TextView tag2;
        @Bind(R.id.recommend_tag3)
        TextView tag3;
        @Bind(R.id.rbRating)
        RatingBar rbRating;

        @Bind(R.id.root_cv)
        CardView rootCv;
        @Bind(R.id.distance)
        TextView item_recommend_card_distance_tv;
        private CardClickListener listener;

        public RecommendHolder(View itemView, CardClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
            tag1.setVisibility(View.GONE);
            tag2.setVisibility(View.GONE);
            tag3.setVisibility(View.GONE);
        }

        @OnClick({R.id.root_cv})
        public void cardClick(View view) {
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
