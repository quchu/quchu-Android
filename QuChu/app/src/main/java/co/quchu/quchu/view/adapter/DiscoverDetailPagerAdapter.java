package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
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
import co.quchu.quchu.R;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.widget.ratingbar.ProperRatingBar;

/**
 * Created by Nico on 16/4/7.
 */
public class DiscoverDetailPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<RecommendModel> mData;
    private OnItemClickListener mListener;

    public DiscoverDetailPagerAdapter(List<RecommendModel> pData, Context context, OnItemClickListener pListener) {
        this.mData = pData;
        this.mContext = context;
        this.mListener = pListener;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recommend_cardview_new_miui, container, false);
        container.addView(view);
        ViewHolder holder = new ViewHolder(view);
        RecommendModel model = mData.get(position);
        holder.rootCv.setCardBackgroundColor(Color.parseColor("#E6EEEFEF"));
        holder.itemRecommendCardPhotoSdv.setImageURI(Uri.parse(model.getCover()));
        if (model.isIsActivity()) {
            holder.item_place_event_tv.setVisibility(View.VISIBLE);
        } else {
            holder.item_place_event_tv.setVisibility(View.GONE);
        }
        holder.itemRecommendCardAddressTv.setText(model.getAddress());
        holder.itemRecommendCardPrb.setRating((int) ((model.getSuggest() + 0.5f) >= 5 ? 5 : (model.getSuggest())));
        holder.item_recommend_card_name_tv.setText(model.getName());
        if (model.isout) {
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
        }
        if (0 == SPUtils.getLatitude() && 0 == SPUtils.getLongitude()) {
            holder.item_recommend_card_distance_tv.setVisibility(View.GONE);
        } else {

            String distance = StringUtils.getDistance(model.getLatitude(), model.getLongitude(), SPUtils.getLatitude(), SPUtils.getLongitude());
            holder.item_recommend_card_distance_tv.setText("距您" + distance);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onItemClick(position);
                }
            }
        });
        return view;
    }

    public class ViewHolder {
        @Bind(R.id.photo)
        SimpleDraweeView itemRecommendCardPhotoSdv;
        @Bind(R.id.ProperRatingBar)
        ProperRatingBar itemRecommendCardPrb;
        @Bind(R.id.item_recommend_card_address_tv)
        TextView itemRecommendCardAddressTv;
        @Bind(R.id.activity)
        TextView item_place_event_tv;
        @Bind(R.id.name)
        TextView item_recommend_card_name_tv;
        @Bind(R.id.recommend_tag1)
        TextView tag1;
        @Bind(R.id.recommend_tag2)
        TextView tag2;
        @Bind(R.id.recommend_tag3)
        TextView tag3;
        @Bind(R.id.root_cv)
        CardView rootCv;
        @Bind(R.id.distance)
        TextView item_recommend_card_distance_tv;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
            tag1.setVisibility(View.GONE);
            tag2.setVisibility(View.GONE);
            tag3.setVisibility(View.GONE);
        }
    }


    @Override
    public int getCount() {

        return null != mData ? mData.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
