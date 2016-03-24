package co.quchu.quchu.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.model.FlickrModel;
import co.quchu.quchu.photo.previewimage.ImageBDInfo;
import co.quchu.quchu.photo.previewimage.PreviewAlbumImage;
import co.quchu.quchu.view.adapter.FlickrListAdapter;
import co.quchu.quchu.widget.InnerListView;

/**
 * FlickrListFragment
 * User: Chenhs
 * Date: 2015-11-18
 */
public class FlickrListFragment extends BaseFragment {
    @Bind(R.id.fragment_flickr_lv)
    InnerListView fragmentFlickrLv;

    private View view;
    private FlickrModel.ImgsEntity images;
    private FlickrListAdapter listAdapter;
    ImageBDInfo bdInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_flickr_list, null);
        ButterKnife.bind(this, view);
        listAdapter = new FlickrListAdapter(getActivity(), images);
        fragmentFlickrLv.setAdapter(listAdapter);
        bdInfo = new ImageBDInfo();
        fragmentFlickrLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           /*     View c = fragmentFlickrLv.getChildAt(0);
                int top = c.getTop();
                int firstVisiblePosition = fragmentFlickrLv.getFirstVisiblePosition() / 4;
                int a, b;
                a = position / 4;
                b = position % 4;
                bdInfo.width = (AppContext.Width - (2 * StringUtils.dip2px(24)) - 4 * StringUtils.dip2px(2)) / 4;
                bdInfo.height = bdInfo.width;
                bdInfo.x = StringUtils.dip2px(8) + b * bdInfo.width + b * StringUtils.dip2px(4);
                bdInfo.y = StringUtils.dip2px(1) + bdInfo.height * (a - firstVisiblePosition) + top + (a - firstVisiblePosition) * StringUtils.dip2px(2) + fragmentFlickrLv.getTop() - StringUtils.dip2px(1)
                        + StringUtils.dip2px(128);

                intent.putExtra("data", (Serializable) defaulModel);
                intent.putExtra("bdinfo", bdInfo);
                intent.putExtra("index", position);
                intent.putExtra("type", 2);
                startActivity(intent);*/
                Intent intent = new Intent(getActivity(), PreviewAlbumImage.class);
                intent.putExtra("data", (Serializable) images.getResult());
                intent.putExtra("index", position);
                intent.putExtra("type", 2);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static FlickrListFragment newInstance(FlickrModel.ImgsEntity images) {

        Bundle args = new Bundle();
        FlickrListFragment fragment = new FlickrListFragment();
        args.putSerializable("images", images);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.images = (FlickrModel.ImgsEntity) getArguments().getSerializable("images");
    }

    public void updateDataSet(FlickrModel.ImgsEntity images) {
        this.images.addResult(images.getResult());
        listAdapter.notifyDataSetChanged();
    }

    public void changeDataSet(FlickrModel.ImgsEntity images) {
        if (listAdapter != null) {
            this.images = images;
            listAdapter.updateDataSet(images);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
