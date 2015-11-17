package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;

/**
 * FlickrLargeAdapter
 * User: Chenhs
 * Date: 2015-11-16
 */
public class FlickrLargeAdapter extends RecyclerView.Adapter<FlickrLargeAdapter.FlickrLargeHolder> {

    private Context mContext;
    private FlickrLargeHolder holder;

    public FlickrLargeAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public FlickrLargeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        holder = new FlickrLargeHolder(LayoutInflater.from(mContext).inflate(R.layout.item_flickr_image_large, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(FlickrLargeHolder holder, int position) {
       /* if (position == 0) {
            holder.itemFlickrImageLargeEmptyview.setVisibility(View.VISIBLE);
        } else {
            holder.itemFlickrImageLargeEmptyview.setVisibility(View.GONE);
        }*/
        holder.itemFlickrImageLargeSdv.setImageURI(Uri.parse("http://pic.nipic.com/2007-11-09/200711912453162_2.jpg"));
        holder.itemFlickrImageLargeSdv.setAspectRatio(1.0f);
    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public int getItemHeight() {
        if (holder != null) {
            return holder.itemFlickrImageLargeSdv.getHeight() * 15;
        } else {
            return 0;
        }
    }

    class FlickrLargeHolder extends RecyclerView.ViewHolder {
        /*    @Bind(R.id.item_flickr_image_large_emptyview)
            View itemFlickrImageLargeEmptyview;*/
        @Bind(R.id.item_flickr_image_large_sdv)
        SimpleDraweeView itemFlickrImageLargeSdv;

        public FlickrLargeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
