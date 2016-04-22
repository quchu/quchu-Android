package co.quchu.quchu.view.adapter;

import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import co.quchu.quchu.R;
import co.quchu.quchu.model.NearbyMapModel;
import co.quchu.quchu.model.TagsModel;
import co.quchu.quchu.widget.TagCloudView;

/**
 * Created by Nico on 16/4/11.
 */
public class AMapNearbyVPAdapter extends PagerAdapter {

    List<NearbyMapModel> mData;
    OnMapItemClickListener mListener;
    public AMapNearbyVPAdapter(List<NearbyMapModel> pData,OnMapItemClickListener listener) {
        mData = pData;
        mListener = listener;
    }

    @Override
    public int getCount() {
        return null!=mData?mData.size():0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View v = LayoutInflater.from(container.getContext()).inflate(R.layout.item_quchu_favorite, container, false);

        TextView name = (TextView )v.findViewById(R.id.name);
        TagCloudView tag = (TagCloudView )v.findViewById(R.id.tag);
        TextView address = (TextView )v.findViewById(R.id.address);
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView )v.findViewById(R.id.simpleDraweeView);
        address.setVisibility(View.GONE);

        List<String> tagStr = new ArrayList<>();
        List<TagsModel> tags = mData.get(position).getTags();
        if (null!=tags){
            for (int i = 0; i < tags.size(); i++) {
                tagStr.add(tags.get(i).getZh());
            }
        }
        tag.setTags(tagStr);
        simpleDraweeView.setImageURI(Uri.parse(mData.get(position).getCover()));
        name.setText(mData.get(position).getName());
        container.addView(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null!=mListener){
                    mListener.onItemClick(position);
                }
            }
        });
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public interface OnMapItemClickListener{
        void onItemClick(int position);
    }
}
