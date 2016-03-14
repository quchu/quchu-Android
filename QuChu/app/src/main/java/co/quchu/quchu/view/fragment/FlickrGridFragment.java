package co.quchu.quchu.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.FlickrModel;
import co.quchu.quchu.photo.previewimage.PreviewAlbumImage;
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

    private FlickrModel.ImgsEntity images;
    private FlickrGridAdapter gridAdapter;

    public static FlickrGridFragment newInstance(FlickrModel.ImgsEntity images) {

        Bundle args = new Bundle();
        FlickrGridFragment fragment = new FlickrGridFragment();
        args.putSerializable("images", images);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.images = (FlickrModel.ImgsEntity) getArguments().getSerializable("images");
        if (null==images){
            this.images = new FlickrModel.ImgsEntity();
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_flickr_grid, null);
        ButterKnife.bind(this, view);
        gridAdapter = new FlickrGridAdapter(getActivity(), images);
        fragmentFlickrGv.setAdapter(gridAdapter);
        fragmentFlickrGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 分页显示， 添加数据后刷新
     *
     * @param images imgsEntity
     */
    public void updateDataSet(FlickrModel.ImgsEntity images) {
        this.images.addResult(images.getResult());
        gridAdapter.notifyDataSetChanged();
    }

    /**
     * 切换显示数据源，
     *
     * @param images imgsEntity
     */
    public void changeDataSet(FlickrModel.ImgsEntity images) {
        if (null != gridAdapter) {
            this.images = images;
            gridAdapter.updateDataSet(images);
        }

    }
}
