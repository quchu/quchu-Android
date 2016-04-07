package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.view.adapter.FootPrintAdapter;
import co.quchu.quchu.widget.FlickrButtonGroup;

/**
 * Created by Nico on 16/4/7.
 */
public class FootPrintActivity extends BaseActivity {

    List<String> mData = new ArrayList<>();
    @Bind(R.id.title_content_tv)
    TextView titleContentTv;
    @Bind(R.id.rvFootPrint)
    RecyclerView rvFootPrint;
    @Bind(R.id.flickr_fbg)
    FlickrButtonGroup flickrFbg;
    FootPrintAdapter mAdapter;

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foot_print);
        ButterKnife.bind(this);
        initTitleBar();
        titleContentTv.setText(getTitle());

        for (int i = 0; i < 50; i++) {
            mData.add(String.valueOf(i));
        }

        mAdapter = new FootPrintAdapter(mData);
        rvFootPrint.setAdapter(mAdapter);
        rvFootPrint.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));

        flickrFbg.setSelectedListener(new FlickrButtonGroup.FlickrOnSelectedistener() {
            @Override
            public void onViewsClick(int flag) {
                switch (flag){
                    case FlickrButtonGroup.SelectedL:
                        rvFootPrint.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        break;

                    case FlickrButtonGroup.SelectedR:
                        rvFootPrint.setLayoutManager(new GridLayoutManager(getApplicationContext(),4));
                        break;
                }
            }
        });
    }
}
