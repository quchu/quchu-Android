package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.FavoriteModel;
import co.quchu.quchu.widget.ratingbar.ProperRatingBar;

/**
 * FlickrGridAdapter
 * User: Chenhs
 * Date: 2015-11-20
 */
public class FavoriteGridAdapter extends BaseAdapter {
    private Context mContext;
    private FavoriteModel model;
    private boolean isPlace = true;//是否填充趣处gridview

    public FavoriteGridAdapter(Context context, FavoriteModel model, boolean isPlace) {
        this.mContext = context;
        this.model = model;
        this.isPlace = isPlace;
    }

    @Override
    public int getCount() {
//        if (isPlace) {
//
//            return model.getPlace().getData().size();
//        } else {
//            return model.getCard().getData().size();
//        }
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_favorite_small_card, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (isPlace) {
            if (position < model.getPlace().getData().size()) {
                holder.itemFavoriteSdv.setImageURI(Uri.parse(model.getPlace().getData().get(position).getCover()));
                holder.itemFavoriteSdv.setAspectRatio(1.33f);
                holder.itemFavoritePrb.setRating(model.getPlace().getData().get(position).getScore());
                holder.itemDiscoverRootLl.setCardBackgroundColor(Color.parseColor("#" + model.getPlace().getData().get(position).getRgb()));
            } else {
                holder.itemFavoritePrb.setRating(0);
                holder.itemFavoriteSdv.setImageDrawable(mContext.getResources().getDrawable(R.color.favorite_scard_none_bg));
                holder.itemFavoriteSdv.setAspectRatio(1.33f);
                holder.itemDiscoverRootLl.setCardBackgroundColor(mContext.getResources().getColor(R.color.detail_card_bg_color));
            }


        } else {

            if (position < model.getCard().getData().size()) {
                holder.itemFavoriteSdv.setImageURI(Uri.parse(model.getCard().getData().get(position).getCover()));
                holder.itemFavoriteSdv.setAspectRatio(1.33f);
                holder.itemFavoritePrb.setRating(model.getCard().getData().get(position).getScore());
                holder.itemDiscoverRootLl.setCardBackgroundColor(Color.parseColor("#" + model.getCard().getData().get(position).getRgb()));
            } else {
                holder.itemFavoritePrb.setRating(0);
                holder.itemFavoriteSdv.setImageDrawable(mContext.getResources().getDrawable(R.color.favorite_scard_none_bg));
                holder.itemFavoriteSdv.setAspectRatio(1.33f);
                holder.itemDiscoverRootLl.setCardBackgroundColor(mContext.getResources().getColor(R.color.detail_card_bg_color));
            }
        }

        return convertView;
    }


    class ViewHolder {

        @Bind(R.id.item_favorite_sdv)
        SimpleDraweeView itemFavoriteSdv;
        @Bind(R.id.item_favorite_prb)
        ProperRatingBar itemFavoritePrb;
        @Bind(R.id.item_favorite_root_ll)
        CardView itemDiscoverRootLl;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
