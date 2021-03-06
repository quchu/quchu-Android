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

    public AMapNearbyVPAdapter(List<NearbyMapModel> pData, OnMapItemClickListener listener) {
        mData = pData;
        mListener = listener;
    }

    @Override
    public int getCount() {
        return null != mData ? mData.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View v = LayoutInflater.from(container.getContext()).inflate(R.layout.item_nearby_quchu, container, false);

        TextView name = (TextView) v.findViewById(R.id.desc);
        TagCloudView tagCloudView = (TagCloudView) v.findViewById(R.id.tag);
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) v.findViewById(R.id.simpleDraweeView);

        simpleDraweeView.setImageURI(Uri.parse(mData.get(position).getCover()));
        name.setText(mData.get(position).getName());
        List<TagsModel> tags = mData.get(position).getTags();
        ArrayList<String> tagsString = new ArrayList<>();
        if (tags != null)
            for (int i = 0; i < tags.size(); i++) {
                tagsString.add(tags.get(i).getZh());
            }
        tagCloudView.setTags(tagsString);

//        if (!StringUtils.isEmpty(mData.get(position).getGdLatitude())&&!StringUtils.isEmpty(mData.get(position).getGdLatitude())){
//            float distance = AMapUtils.calculateLineDistance(new LatLng(Double.valueOf(mData.get(position).getGdLatitude()), Double.valueOf(mData.get(position).getGdLongitude())),new LatLng(SPUtils.getLatitude(),SPUtils.getLongitude()));
//            address.setText("距离当前位置：" + new DecimalFormat("#.##").format(((distance / 1000) / 100f) * 100) + "km");
//            address.setTextColor(Color.WHITE);
//            address.setVisibility(View.VISIBLE);
//        }else{
//            address.setVisibility(View.INVISIBLE);
//        }


        container.addView(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
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

    public interface OnMapItemClickListener {
        void onItemClick(int position);
    }
}
