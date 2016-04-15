package co.quchu.quchu.view.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.FootprintModel;
import co.quchu.quchu.model.ImageModel;

/**
 * Created by Nico on 16/4/7.
 */
public class GalleryAdapter extends PagerAdapter {

    List<ImageModel> mData;
    private Context mContext;


    public GalleryAdapter(List<ImageModel> pData,Context context) {
        this.mData = pData;
        this.mContext = context;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_sdv, container, false);
        container.addView(view);
        ((SimpleDraweeView)view).setImageURI(Uri.parse(mData.get(position).getPath()));
        ((SimpleDraweeView)view).setAspectRatio(1.2f);
        return view;
    }



    @Override
    public int getCount() {

        return null != mData ? mData.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
