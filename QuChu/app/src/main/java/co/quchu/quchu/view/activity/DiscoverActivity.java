package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.view.adapter.DiscoverAdapter;

/**
 * DiscoverActivity
 * User: Chenhs
 * Date: 2015-11-04
 * 我的发现
 */
public class DiscoverActivity extends BaseActivity {

    @Bind(R.id.title_back_rl)
    RelativeLayout titleBackRl;
    @Bind(R.id.title_content_tv)
    TextView titleContentTv;
    @Bind(R.id.title_more_rl)
    RelativeLayout titleMoreRl;
    @Bind(R.id.atmosphere_rv)
    RecyclerView atmosphereRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atmosphere);
        ButterKnife.setDebug(true);
        ButterKnife.bind(this);
        titleContentTv.setText("faxian");
//        title_content_tv.setText(getTitle());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        atmosphereRv.setLayoutManager(mLayoutManager);
        atmosphereRv.setAdapter(new DiscoverAdapter(this));
//        titleBackRL.setOnClickListener(this);

        atmosphereRv.scrollTo(0,120);
    }

    @OnClick(R.id.title_more_rl)
    public void click(View view) {
        Toast.makeText(this, "more", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.title_back_rl)
    public void back(View view) {
        Toast.makeText(this, "back", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.title_more_rl:
//
//                break;
//        }
//    }
}
