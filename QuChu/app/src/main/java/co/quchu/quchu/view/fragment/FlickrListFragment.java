package co.quchu.quchu.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.FlickrModel;
import co.quchu.quchu.view.adapter.FlickrListAdapter;
import co.quchu.quchu.widget.InnerListView;

/**
 * FlickrListFragment
 * User: Chenhs
 * Date: 2015-11-18
 */
public class FlickrListFragment extends Fragment {
    @Bind(R.id.fragment_flickr_lv)
    InnerListView fragmentFlickrLv;

    private View view;
    private Context mContext;
    private FlickrModel.ImgsEntity images;
    private FlickrListAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_flickr_list, null);
        ButterKnife.bind(this, view);
        listAdapter = new FlickrListAdapter(mContext, images);
        fragmentFlickrLv.setAdapter(listAdapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public FlickrListFragment(Context context, FlickrModel.ImgsEntity images) {
        this.mContext = context;
        this.images = images;
    }

    public void updateDataSet(FlickrModel.ImgsEntity images) {
        this.images.getResult().addAll(images.getResult());
        listAdapter.notifyDataSetChanged();
    }


}
