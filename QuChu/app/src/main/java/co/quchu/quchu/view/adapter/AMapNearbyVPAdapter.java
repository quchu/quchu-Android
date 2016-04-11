package co.quchu.quchu.view.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import co.quchu.quchu.R;
import co.quchu.quchu.widget.TagCloudView;

/**
 * Created by Nico on 16/4/11.
 */
public class AMapNearbyVPAdapter extends PagerAdapter {

    List<String> mData;

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = LayoutInflater.from(container.getContext()).inflate(R.layout.item_quchu_favorite, container, false);

        TextView name = (TextView )v.findViewById(R.id.name);
        TagCloudView tag = (TagCloudView )v.findViewById(R.id.tag);
        TextView address = (TextView )v.findViewById(R.id.address);
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView )v.findViewById(R.id.simpleDraweeView);
        address.setVisibility(View.GONE);
        name.setText("Position");
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
