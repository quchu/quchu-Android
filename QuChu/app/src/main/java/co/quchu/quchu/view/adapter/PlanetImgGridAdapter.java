package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import co.quchu.quchu.R;
import co.quchu.quchu.utils.StringUtils;

/**
 * PlanetImgGalleryAda
 * User: Chenhs
 * Date: 2015-10-23
 */
public class PlanetImgGridAdapter extends BaseAdapter {
    private Context context;

    public PlanetImgGridAdapter(Context context) {
        this.context = context;
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
        PlanetImgItemHolder pvh;
        if (convertView == null) {
//             pvh= new PlanetImgItemHolder((LayoutInflater.from(
//                    context).inflate(R.layout.planet_gaallery_item, parent,
//                    false)));
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

        Picasso.with(context).load("http://imgdn.paimeilv.com/1444721523235").config(Bitmap.Config.RGB_565).resize(StringUtils.dip2px(context,80), StringUtils.dip2px(context,80))
                .centerCrop().into(pvh.sdv);
        return convertView;
    }


    class PlanetImgItemHolder {
        public ImageView sdv;
        public RelativeLayout planet_gallery_mask_rl;
        public TextView planet_gallery_mask_tv;
        public PlanetImgItemHolder(View view) {
            sdv = (ImageView) view.findViewById(R.id.planet_gallery_img);
            planet_gallery_mask_rl= (RelativeLayout) view.findViewById(R.id.planet_gallery_mask_rl);
            planet_gallery_mask_tv= (TextView) view.findViewById(R.id.planet_gallery_mask_tv);
        }
    }
}
