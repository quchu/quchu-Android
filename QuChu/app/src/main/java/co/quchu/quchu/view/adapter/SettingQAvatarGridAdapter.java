package co.quchu.quchu.view.adapter;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;

/**
 * FlickrGridAdapter
 * User: Chenhs
 * Date: 2015-11-20
 */
public class SettingQAvatarGridAdapter extends BaseAdapter {
    private Activity mContext;
    List<String> mPhotoList;
    DisplayImageOptions options;

    public SettingQAvatarGridAdapter(Activity context, List<String> mPhotoList) {
        this.mContext = context;
        this.mPhotoList = mPhotoList;

    }

    @Override
    public int getCount() {
        if (mPhotoList == null) {
            return 0;
        } else {
            return mPhotoList.size();
        }
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_qavatar_gridview, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.itemAddpostcardSdv.setImageURI(Uri.parse(mPhotoList.get(position)));
        holder.itemAddpostcardSdv.setAspectRatio(1.0f);

        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.item_qavatar_iv)
        SimpleDraweeView itemAddpostcardSdv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
