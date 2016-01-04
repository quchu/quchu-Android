package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import co.quchu.quchu.R;
import co.quchu.quchu.model.PlanetModel;
import co.quchu.quchu.widget.textcounter.CounterView;

/**
 * PlanetImgGalleryAda
 * User: Chenhs
 * Date: 2015-10-23
 */
public class PlanetImgGridAdapter extends BaseAdapter {
    private Context context;
    List<PlanetModel.ImgsEntity> imgs = new ArrayList<>();
    private int imageCount = 0;

    public PlanetImgGridAdapter(Context context) {
        this.context = context;

    }

    int count = 1;

    @Override
    public int getCount() {

        if (imgs != null && imgs.size() > 0) {
            count = imgs.size() >= 4 ? 4 : imgs.size();
        }
        return count;

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
        if (imgs == null || imgs.size() == 0) {
            pvh.planet_gallery_mask_rl.setVisibility(View.VISIBLE);
            pvh.planet_gallery_mask_tv.setText("照片");
            pvh.planet_collect_num_tv.setEndValue(0);
            pvh.planet_collect_num_tv.start();
            pvh.sdv.setImageURI(Uri.parse("res://" + context.getPackageName() + "/" + R.drawable.ic_image_empty));
            pvh.sdv.setAspectRatio(1.0f);
        } else {
            if (position == count - 1) {
                pvh.planet_gallery_mask_rl.setVisibility(View.VISIBLE);
                pvh.planet_gallery_mask_tv.setText("照片");
                pvh.planet_collect_num_tv.setEndValue(imageCount);
                pvh.planet_collect_num_tv.start();

            } else {
                pvh.planet_gallery_mask_rl.setVisibility(View.INVISIBLE);
            }

      /*  AppContext.picasso.with(context).load("http://imgdn.paimeilv.com/1444721523235").config(Bitmap.Config.RGB_565).resize(StringUtils.dip2px(context,40), StringUtils.dip2px(context,40))
                .centerCrop().into(pvh.sdv);*/
            pvh.sdv.setImageURI(Uri.parse(imgs.get(position).getPath()));
            pvh.sdv.setAspectRatio(1.0f);
        }
        return convertView;
    }


    public void updateDate(List<PlanetModel.ImgsEntity> imgs, int imgNum) {
        imageCount = imgNum;
        this.imgs = imgs;
        notifyDataSetChanged();
    }

    class PlanetImgItemHolder {
        public SimpleDraweeView sdv;
        public RelativeLayout planet_gallery_mask_rl;
        public TextView planet_gallery_mask_tv;
        public CounterView planet_collect_num_tv;

        public PlanetImgItemHolder(View view) {
            sdv = (SimpleDraweeView) view.findViewById(R.id.planet_gallery_img);
            planet_gallery_mask_rl = (RelativeLayout) view.findViewById(R.id.planet_gallery_mask_rl);
            planet_gallery_mask_tv = (TextView) view.findViewById(R.id.planet_gallery_mask_tv);
            planet_collect_num_tv = (CounterView) view.findViewById(R.id.planet_collect_num_tv);
        }
    }
}
