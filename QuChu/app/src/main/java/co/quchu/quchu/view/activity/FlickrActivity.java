package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.view.adapter.DiscoverAdapter;
import co.quchu.quchu.view.adapter.PostCardAdapter;
import co.quchu.quchu.widget.FlickrButtonGroup;

/**
 * FlickrActivity
 * User: Chenhs
 * Date: 2015-11-12
 * 我的网络相册
 * 我的照片/我的收藏
 */
public class FlickrActivity extends BaseActivity implements FlickrButtonGroup.FlickrOnSelectedistener {
    @Bind(R.id.flickr_rv)
    RecyclerView flickrRv;
    @Bind(R.id.flickr_fbg)
    FlickrButtonGroup flickrFbg;

    /************************
     * title
     ************************/
    @Bind(R.id.title_back_rl)
    RelativeLayout titleBackRL;
    @Bind(R.id.title_more_rl)
    RelativeLayout titleMoreRl;
    @Bind(R.id.title_content_tv)
    TextView title_content_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flickr);
        ButterKnife.bind(this);
        title_content_tv.setText(getTitle());
        flickrFbg.setSelectedListener(this);

        flickrRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        flickrRv.setAdapter(new PostCardAdapter(this));
    }

    @Override
    public void onViewsClick(int flag) {
        switch (flag) {
            case FlickrButtonGroup.SelectedR: //选中new
                break;
            case FlickrButtonGroup.SelectedL://选中hot
                break;
            case FlickrButtonGroup.SelectedCT: //选中大图
                flickrRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                flickrRv.setAdapter(new PostCardAdapter(this));
                break;
            case FlickrButtonGroup.SelectedCF://选中小图
                flickrRv.setLayoutManager(new GridLayoutManager(this, 3));
                flickrRv.setAdapter(new DiscoverAdapter(FlickrActivity.this));
                break;
        }
    }
}
