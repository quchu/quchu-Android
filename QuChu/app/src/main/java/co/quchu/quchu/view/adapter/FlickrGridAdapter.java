package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.FlickrModel;

/**
 * FlickrGridAdapter
 * User: Chenhs
 * Date: 2015-11-20
 */
public class FlickrGridAdapter extends BaseAdapter {
    private Context mContext;
    private FlickrModel.ImgsEntity imgsEntity;

    public FlickrGridAdapter(Context context,FlickrModel.ImgsEntity imgsEntity) {
        this.mContext = context;
        this.imgsEntity=imgsEntity;
    }

    @Override
    public int getCount() {
        return imgsEntity.getResult().size();
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
        FlickrGridHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_flickr_gridview, null);
            holder = new FlickrGridHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (FlickrGridHolder) convertView.getTag();
        }
        holder.itemFlickrGridviewSdv.setImageURI(Uri.parse(imgsEntity.getResult().get(position).getPath()));
        holder.itemFlickrGridviewSdv.setAspectRatio(1.0f);
        return convertView;
    }


    class FlickrGridHolder {
        @Bind(R.id.item_flickr_gridview_sdv)
        SimpleDraweeView itemFlickrGridviewSdv;

        public FlickrGridHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
    public void updateDataSet(FlickrModel.ImgsEntity imgsEntity) {
        this.imgsEntity = imgsEntity;
        notifyDataSetChanged();
    }
}
