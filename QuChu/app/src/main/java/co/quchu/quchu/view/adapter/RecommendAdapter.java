package co.quchu.quchu.view.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
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
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.fragment.RecommendFragment;

/**
 * RecommendAdapter
 * User: Chenhs
 * Date: 2015-12-08
 * 趣处推荐 适配器 adapter
 */
public class RecommendAdapter extends PagerAdapter {


    private Activity mContext;
    private List<RecommendModel> dataSet;
    private CardClickListener listener;

    public RecommendAdapter(Activity mContext, List<RecommendModel> arrayList, CardClickListener listener) {
        this.mContext = mContext;
        dataSet = arrayList;
        this.listener = listener;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recommend_cardview_new_miui, container, false);
        if (position != 0) {
            view.setScaleY(RecommendFragment.MIN_SCALE);
        }
        RecommendHolder holder = new RecommendHolder(view, listener, position);

        RecommendModel model = dataSet.get(position);
        holder.itemRecommendCardPhotoSdv.setImageURI(Uri.parse(model.getCover()));
        if (model.isIsActivity()) {
            holder.item_place_event_tv.setVisibility(View.VISIBLE);
        } else {
            holder.item_place_event_tv.setVisibility(View.GONE);
        }

        String price;
        if (!TextUtils.isEmpty(model.getPrice())) {
            price = model.getPrice().split(",")[0];
        } else {
            price = "-";
        }

        holder.itemRecommendCardAddressTv.setText(StringUtils.getColorSpan(mContext, R.color.standard_color_red, mContext.getString(R.string.avg_cost_with_rmb_symbol), price, "起"));
        holder.linearLayout.removeAllViews();
        int space = (int) mContext.getResources().getDimension(R.dimen.quarter_margin);
        for (int i = 0; i < model.getSuggest(); i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(R.mipmap.ic_ratingbar_heart_fill);
            imageView.setPadding(space, 0, space, 0);
            holder.linearLayout.addView(imageView);
        }
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
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    class RecommendHolder {
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
        @Bind(R.id.llRating)
        LinearLayout linearLayout;

        @Bind(R.id.distance)
        TextView item_recommend_card_distance_tv;
        private CardClickListener listener;
        private int position;
        View itemView;

        public RecommendHolder(View itemView, CardClickListener listener, int position) {
            ButterKnife.bind(this, itemView);
            LogUtils.e("初始化page" + position);
            this.itemView = itemView;
            this.listener = listener;
            tag1.setVisibility(View.GONE);
            tag2.setVisibility(View.GONE);
            tag3.setVisibility(View.GONE);
            this.position = position;
        }

        @OnClick({R.id.root_cv})
        public void cardClick(View view) {
            if (listener != null)
                listener.onCardLick(view, position);
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
