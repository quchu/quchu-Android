package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.DetailModel;

/**
 * FlickrListAdapter
 * User: Chenhs
 * Date: 2015-11-17
 */
public class DetailListViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<DetailModel.ImglistEntity> images;

    public DetailListViewAdapter(Context mContext, List<DetailModel.ImglistEntity> images) {
        this.mContext = mContext;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
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
        FlickrLargeHolder flickrLargeHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_card_image, null);
            flickrLargeHolder = new FlickrLargeHolder(convertView);
            convertView.setTag(flickrLargeHolder);
        } else {
            flickrLargeHolder = (FlickrLargeHolder) convertView.getTag();
        }
        flickrLargeHolder.itemFlickrImageLargeSdv.setImageURI(Uri.parse(images.get(position).getImgpath()));
       flickrLargeHolder.itemFlickrImageLargeSdv.setAspectRatio(0.75f);
        return convertView;
    }

    class FlickrLargeHolder {
        @Bind(R.id.item_card_image_sdv)
        SimpleDraweeView itemFlickrImageLargeSdv;

        public FlickrLargeHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

}
