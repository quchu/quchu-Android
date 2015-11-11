package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import co.quchu.quchu.R;
import co.quchu.quchu.widget.RoundProgressBar;

/**
 * PlanetImgGalleryAda
 * User: Chenhs
 * Date: 2015-10-23
 */
public class GeneProgressAdapter extends BaseAdapter {
    private Context context;
    /**
     * adapter 复用
     * 0=我的趣星球
     * 1=我的趣基因
     */
    private int adapterTyle=0;
    private  String[] textArray;
    private int[] imagesArray;
    public static final int GENE=1;
    public static final int PLANET=0;


    public GeneProgressAdapter(Context context) {
        this.context = context;
    }
    public GeneProgressAdapter(Context context,int adapterTyle) {
        this.context = context;
        this.adapterTyle=adapterTyle;
        switch (adapterTyle){
            case GENE:
                textArray=context.getResources().getStringArray(R.array.MyGene);
                TypedArray ar = context.getResources().obtainTypedArray(R.array.gene_images);
                int len = ar.length();
                imagesArray = new int[len];
                for (int i = 0; i < len; i++)
                    imagesArray[i] = ar.getResourceId(i, 0);
                ar.recycle();
                break;
        }
    }
    @Override
    public int getCount() {
        return 4;
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
        switch (adapterTyle){
            case 0:
                return    getPlanetView(position,convertView,parent);
            case 1:
                return  getGeneView(position, convertView, parent);
        }

        return convertView;
    }

    public View getGeneView(int position, View convertView, ViewGroup parent){
        GeneProgressItemHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(
                    context).inflate(R.layout.item_gene_progress, null);
            holder = new GeneProgressItemHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GeneProgressItemHolder) convertView.getTag();
        }
        holder.gene_progress_item_rpb.setRoundWidth(20);
        holder.gene_progress_item_rpb.setProgress(110);
        holder.gene_progress_item_tv.setText(textArray[position]);
        holder.gene_progress_item_iv.setImageResource(imagesArray[position]);
        SpannableStringBuilder builder = new SpannableStringBuilder(textArray[position]);
        ForegroundColorSpan redSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.planet_progress_yellow));
        builder.setSpan(redSpan, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.gene_progress_item_tv.setText(builder);
        return convertView;
    }


    public View getPlanetView(int position, View convertView, ViewGroup parent){
        PlanetImgItemHolder pvh;
        if (convertView == null) {
            convertView = LayoutInflater.from(
                    context).inflate(R.layout.planet_gaallery_item, null);
            pvh = new PlanetImgItemHolder(convertView);
            convertView.setTag(pvh);
        } else {
            pvh = (PlanetImgItemHolder) convertView.getTag();
        }
        if (position==3) {
            pvh.planet_gallery_mask_rl.setVisibility(View.VISIBLE);
            pvh.planet_gallery_mask_tv.setText(" 88\r\n 照片");
        }else{
            pvh.planet_gallery_mask_rl.setVisibility(View.INVISIBLE);
        }

       /* Picasso.with(context).load("http://imgdn.paimeilv.com/1444721523235").config(Bitmap.Config.RGB_565).resize(StringUtils.dip2px(context,80), StringUtils.dip2px(context,80))
                .centerCrop().into(pvh.sdv);*/

        pvh.sdv.setImageURI(Uri.parse("http://imgdn.paimeilv.com/1444721523235"));
        return convertView;
    }

    class PlanetImgItemHolder {
        public SimpleDraweeView sdv;
        public RelativeLayout planet_gallery_mask_rl;
        public TextView planet_gallery_mask_tv;
        public PlanetImgItemHolder(View view) {
            sdv = (SimpleDraweeView) view.findViewById(R.id.planet_gallery_img);
            planet_gallery_mask_rl= (RelativeLayout) view.findViewById(R.id.planet_gallery_mask_rl);
            planet_gallery_mask_tv= (TextView) view.findViewById(R.id.planet_gallery_mask_tv);
        }
    }
    class GeneProgressItemHolder {
        public ImageView gene_progress_item_iv;
        public RoundProgressBar gene_progress_item_rpb;
        public TextView gene_progress_item_tv;
        public GeneProgressItemHolder(View view) {
            gene_progress_item_iv = (ImageView) view.findViewById(R.id.gene_progress_item_iv);
            gene_progress_item_rpb= (RoundProgressBar) view.findViewById(R.id.gene_progress_item_rpb);
            gene_progress_item_tv= (TextView) view.findViewById(R.id.gene_progress_item_tv);
        }
    }

}
