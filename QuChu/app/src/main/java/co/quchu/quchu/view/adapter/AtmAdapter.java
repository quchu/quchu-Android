package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.AtmosphereItemModel;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.activity.AtmosphereActivity;

/**
 * AtmAdapter
 * User: Chenhs
 * Date: 2015-10-30
 */
public class AtmAdapter extends RecyclerView.Adapter<AtmAdapter.AtmHolder> {
  private Context activity;
   private ArrayList<AtmosphereItemModel> arrayList;

    public AtmAdapter(AtmosphereActivity atmosphereActivity, ArrayList<AtmosphereItemModel> arrayList) {
        activity=atmosphereActivity;
        this.arrayList=arrayList;
    }


    public void AtmAdapter(Context activity,ArrayList<AtmosphereItemModel> arrayList){
        this.activity=activity;
        this.arrayList=arrayList;
    }

    @Override
    public AtmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AtmHolder pvh= new AtmHolder((LayoutInflater.from(
                activity).inflate(R.layout.atmosphere_item_view, parent,
                false)));
        return pvh;
    }

    @Override
    public void onBindViewHolder(AtmHolder holder, int position) {
//            holder.root_cv.setBackground();
        Picasso.with(activity).load(arrayList.get(position).getCover())
                .config(Bitmap.Config.RGB_565).resize(StringUtils.dip2px(activity, 160), StringUtils.dip2px(activity, 160))
                .centerCrop() .into(holder.atrmosphereItemImageIv);
        holder.root_cv.setCardBackgroundColor(colorBurn(Color.parseColor("#" + arrayList.get(position).getRgb())));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'atmosphere_item_view.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    public class AtmHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.atrmosphere_item_title_tv)
        TextView atrmosphereItemTitleTv;
        @Bind(R.id.atrmosphere_item_image_iv)
        ImageView atrmosphereItemImageIv;
        @Bind(R.id.root_cv)
        CardView root_cv;

        AtmHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    private int colorBurn(int RGBValues) {
        int alpha = RGBValues >> 24;
        int red = RGBValues >> 16 & 0xFF;
        int green = RGBValues >> 8 & 0xFF;
        int blue = RGBValues & 0xFF;
        red = (int) Math.floor(red * (1 - 0.1));
        green = (int) Math.floor(green * (1 - 0.1));
        blue = (int) Math.floor(blue * (1 - 0.1));
        return Color.rgb(red, green, blue);
    }
}
