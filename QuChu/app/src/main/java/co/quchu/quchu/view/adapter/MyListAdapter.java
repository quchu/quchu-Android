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

/**
 * MyListAdapter
 * User: Chenhs
 * Date: 2015-11-17
 */
public class MyListAdapter extends BaseAdapter {
    private Context mContext;

    public MyListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return 15;
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
            convertView=LayoutInflater.from(mContext).inflate(R.layout.item_flickr_image_large, null);
            flickrLargeHolder = new FlickrLargeHolder(convertView);
            convertView.setTag(flickrLargeHolder);
        } else {
            flickrLargeHolder = (FlickrLargeHolder) convertView.getTag();
        }
        flickrLargeHolder.itemFlickrImageLargeSdv.setImageURI(Uri.parse("http://pic.nipic.com/2007-11-09/200711912453162_2.jpg"));
        flickrLargeHolder.itemFlickrImageLargeSdv.setAspectRatio(1.0f);
        return convertView;
    }

    class FlickrLargeHolder {
        /*    @Bind(R.id.item_flickr_image_large_emptyview)
            View itemFlickrImageLargeEmptyview;*/
        @Bind(R.id.item_flickr_image_large_sdv)
        SimpleDraweeView itemFlickrImageLargeSdv;

        public FlickrLargeHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
