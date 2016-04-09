package co.quchu.quchu.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;


public class FootprintDetailFragment extends BaseFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_footprint_detail, container, false);
        return rootView;
    }


}
