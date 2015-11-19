package co.quchu.quchu.view.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.view.fragment.FlickrListFragment;
import co.quchu.quchu.widget.FlickrButtonGroup;
import co.quchu.quchu.widget.ImageSubtabLayout;
import co.quchu.quchu.widget.OutSideScrollView;
import co.quchu.quchu.widget.TextSubtabLayout;

/**
 * FlickrActivity
 * User: Chenhs
 * Date: 2015-11-12
 * 我的网络相册
 * 我的照片/我的收藏
 */
public class FlickrActivity extends BaseActivity implements FlickrButtonGroup.FlickrOnSelectedistener {
    @Bind(R.id.tab_bar)
    ImageSubtabLayout tabBar;
    @Bind(R.id.flickr_fl)
    FrameLayout flickrFl;
    @Bind(R.id.scrollView_flickr)
    OutSideScrollView scrollViewFlickr;
    @Bind(R.id.flickr_fbg)
    FlickrButtonGroup flickrFbg;
    @Bind(R.id.flickr_tsl)
    TextSubtabLayout flickrTsl;
    @Bind(R.id.title_back_rl)
    RelativeLayout titleBackRL;
    @Bind(R.id.title_more_rl)
    RelativeLayout titleMoreRl;
    @Bind(R.id.title_content_tv)
    TextView title_content_tv;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flickr);
        ButterKnife.bind(this);
        title_content_tv.setText(getTitle());
        flickrFbg.setSelectedListener(this);

        FlickrListFragment flickrF = new FlickrListFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.flickr_fl, flickrF);
        transaction.commit();

        scrollViewFlickr.smoothScrollTo(0, 0);
       tabBar.setSelectedListener(new ImageSubtabLayout.ImageSubtabSelectedListener() {
           @Override
           public void onSelected(int selectedNum) {
               flickrTsl.initIndexView(selectedNum);
           }
       });
        flickrTsl.setSelectedListener(new TextSubtabLayout.TextSubtabSelectedListener() {
            @Override
            public void onSelected(int selectedNum) {
                tabBar.initIndexView(selectedNum);
            }
        });
        scrollViewFlickr.setOverScrollListener(new OutSideScrollView.OverScrolledListener() {
            @Override
            public void onOverScrolled(int scrollX, int scrollY) {
                if (scrollY >= (tabBar.getHeight()-flickrTsl.getHeight())) {
                    flickrTsl.setVisibility(View.VISIBLE);
                } else {
                    flickrTsl.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onViewsClick(int flag) {
        switch (flag) {
            case FlickrButtonGroup.SelectedR: //选中new
                break;
            case FlickrButtonGroup.SelectedL://选中hot
                break;
           /* case FlickrButtonGroup.SelectedCT: //选中大图
                flickrRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                flickrRv.setAdapter(new FlickrLargeAdapter(this));
                break;
            case FlickrButtonGroup.SelectedCF://选中小图
                flickrRv.setLayoutManager(new GridLayoutManager(this, 4));
                FlickrDecoration decoration = new FlickrDecoration(this, 2);
                decoration.setLineNum(4);
                flickrRv.addItemDecoration(decoration);
                break;*/
        }
    }

}
