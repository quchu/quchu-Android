package co.quchu.quchu.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.quchu.quchu.R;

/**
 * PlannetViewHolder
 * User: Chenhs
 * Date: 2015-10-23
 */
public class PlannetViewHolder extends RecyclerView.ViewHolder {
    public ImageView galleryImg;
    public RelativeLayout planet_gallery_mask_rl;
    public TextView planet_gallery_mask_tv;
    public PlannetViewHolder(View itemView) {
        super(itemView);
            galleryImg= (ImageView) itemView.findViewById(R.id.planet_gallery_img);
        planet_gallery_mask_rl= (RelativeLayout) itemView.findViewById(R.id.planet_gallery_mask_rl);
        planet_gallery_mask_tv= (TextView) itemView.findViewById(R.id.planet_gallery_mask_tv);
    }
}
