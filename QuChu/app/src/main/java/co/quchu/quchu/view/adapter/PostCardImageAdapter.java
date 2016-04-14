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
import co.quchu.quchu.model.ImageModel;

/**
 * PostCardImageAdapter
 * User: Chenhs
 * Date: 2015-12-29
 */
public class PostCardImageAdapter extends BaseAdapter {

    private List<ImageModel> imglist;
    private Context mContext;
    public PostCardImageAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<ImageModel> imglist) {
        this.imglist = imglist;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (imglist==null) {
            return 0;
        }else {
            return imglist.size();

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
    public View getView(int position, View convertView, ViewGroup parent) {
        PCIHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_post_card_image_views, null);
            holder = new PCIHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (PCIHolder) convertView.getTag();
        }
        holder.itemAddpostcardSdv.setImageURI(Uri.parse(imglist.get(position).getPath()));
        holder.itemAddpostcardSdv.setAspectRatio(1f);

        return convertView;
    }

    public class PCIHolder {
        @Bind(R.id.item_addpostcard_sdv)
        SimpleDraweeView itemAddpostcardSdv;
/*        @Bind(R.id.item_addpostcard_del_iv)
        ImageView itemAddpostcardDelIv;*/

        PCIHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
