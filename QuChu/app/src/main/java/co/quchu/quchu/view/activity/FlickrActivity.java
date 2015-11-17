package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.view.adapter.MyListAdapter;
import co.quchu.quchu.widget.FlickrButtonGroup;
import co.quchu.quchu.widget.InnerListView;
import co.quchu.quchu.widget.OutSideScrollView;

/**
 * FlickrActivity
 * User: Chenhs
 * Date: 2015-11-12
 * 我的网络相册
 * 我的照片/我的收藏
 */
public class FlickrActivity extends BaseActivity implements FlickrButtonGroup.FlickrOnSelectedistener {
    @Bind(R.id.flickr_rv)
    InnerListView flickrRv;
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
    @Bind(R.id.scrollView_flickr)
    OutSideScrollView scrollView_flickr;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flickr);
        ButterKnife.bind(this);
        title_content_tv.setText(getTitle());
        flickrFbg.setSelectedListener(this);
        flickrRv.setAdapter(new MyListAdapter(this));
        setListViewHeightBasedOnChildren(flickrRv);
        flickrRv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        scrollView_flickr.scrollBy(0, 0);
        scrollView_flickr.setOverScrollListener(new OutSideScrollView.OverScrolledListener() {
            @Override
            public void onOverScrolled(int scrollX, int scrollY) {

            }
        });
       /*FlickrLargeAdapter fla= new FlickrLargeAdapter(this);
        final FlickrLinearLayoutManager manager = new  FlickrLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        manager.setSmoothScrollbarEnabled(false);
        flickrRv.setLayoutManager(manager);
        int viewHeight = StringUtils.dip2px(this,130) *15;
        flickrRv.getLayoutParams().height = viewHeight;
        flickrRv. setNestedScrollingEnabled(false);
        flickrRv.setAdapter(fla);*/



       /* int viewHeight =fla.getItemHeight();
        flickrRv.getLayoutParams().height = viewHeight;*/
   /*     flickrRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean b = recyclerView.computeVerticalScrollOffset() > (StringUtils.dip2px(FlickrActivity.this, 60));
              boolean b = recyclerView.computeVerticalScrollOffset() > ((flickr_root_ll.getHeight()) * 2 / 3);

            }
        });*/
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

    public void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
