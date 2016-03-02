package co.quchu.quchu.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.model.FlickrModel;
import co.quchu.quchu.photo.previewimage.ImageBDInfo;
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
    private Context mContext;
    private FlickrModel.ImgsEntity images;
    private FlickrListAdapter listAdapter;
    ImageBDInfo bdInfo;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_flickr_list, null);
        ButterKnife.bind(this, view);
        listAdapter = new FlickrListAdapter(mContext, images);
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

                Intent intent = new Intent(getActivity(), PreviewImage.class);
                intent.putExtra("data", (Serializable) defaulModel);
                intent.putExtra("bdinfo", bdInfo);
                intent.putExtra("index", position);
                intent.putExtra("type", 2);
                startActivity(intent);*/
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

    public FlickrListFragment(Context context, FlickrModel.ImgsEntity images) {
        this.mContext = context;
        this.images = images;
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
