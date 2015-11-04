package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.view.adapter.PostCardAdapter;
import co.quchu.quchu.widget.recyclerviewpager.RecyclerViewPager;
import co.quchu.quchu.widget.swipbacklayout.SwipeBackLayout;

/**
 * PostCardActivity
 * User: Chenhs
 * Date: 2015-11-03
 */
public class PostCardActivity extends BaseActivity {
    @Bind(R.id.atmosphere_rv)
    RecyclerViewPager mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postcard);
        ButterKnife.bind(this);

        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layout);
        mRecyclerView.setAdapter(new PostCardAdapter(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLongClickable(true);
        updateState(RecyclerView.SCROLL_STATE_IDLE);

        mRecyclerView.addOnScrollListener();
        mRecyclerView.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() {
            @Override
            public void OnPageChanged(int oldPosition, int newPosition) {
                Log.d("test", "oldPosition:" + oldPosition + " newPosition:" + newPosition);
                if (newPosition ==0){
                    mSwipeBackLayout.setEnableGesture(true);
                    mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
                    mSwipeBackLayout.setEdgeSize(400);
                }else{
                    mSwipeBackLayout.setEnableGesture(false);
                }
            }
        });

        mRecyclerView.addOnLayoutChangeListener();
    }


    private void updateState(int scrollState) {
        String stateName = "Undefined";
        switch (scrollState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                stateName = "Idle";
                break;

            case RecyclerView.SCROLL_STATE_DRAGGING:
                stateName = "Dragging";
                break;

            case RecyclerView.SCROLL_STATE_SETTLING:
                stateName = "Flinging";
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
