package co.quchu.quchu.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.view.adapter.MyListAdapter;
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_flickr_list, null);
        ButterKnife.bind(this, view);
        fragmentFlickrLv.setAdapter( new MyListAdapter(getActivity()));
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
