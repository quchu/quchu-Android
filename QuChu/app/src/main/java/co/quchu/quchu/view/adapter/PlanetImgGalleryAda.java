package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import co.quchu.quchu.R;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.view.holder.PlannetViewHolder;

/**
 * PlanetImgGalleryAda
 * User: Chenhs
 * Date: 2015-10-23
 */
public class PlanetImgGalleryAda extends RecyclerView.Adapter<PlannetViewHolder> {
    private Context context;
    public PlanetImgGalleryAda( Context context){
        this.context=context;
    }
    @Override
    public PlannetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            PlannetViewHolder pvh= new PlannetViewHolder((LayoutInflater.from(
                    context).inflate(R.layout.planet_gaallery_item, parent,
                    false)));
        LogUtils.json("PlanetImgGalleryAda===onCreateViewHolder=");
        return pvh;
    }

    @Override
    public void onBindViewHolder(PlannetViewHolder holder, int position) {
            holder.galleryImg.setImageURI(Uri.parse("http://imgdn.paimeilv.com/1444721523235"));
        LogUtils.json("PlanetImgGalleryAda===onBindViewHolder=");
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
