package co.quchu.quchu.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.view.adapter.FlickrGridAdapter;
import co.quchu.quchu.widget.InnerGridView;

/**
 * FlickrGridFragment
 * User: Chenhs
 * Date: 2015-11-18
 */
public class FlickrGridFragment extends Fragment {

    @Bind(R.id.fragment_flickr_gv)
    InnerGridView fragmentFlickrGv;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_flickr_grid, null);
        ButterKnife.bind(this, view);
        fragmentFlickrGv.setAdapter(new FlickrGridAdapter(getActivity()));
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
}
