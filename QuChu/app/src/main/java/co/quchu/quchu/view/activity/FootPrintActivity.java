package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
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
    @Bind(R.id.rvFootPrint)
    RecyclerView rvFootPrint;
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
        getEnhancedToolbar().getRightIv().setImageResource(R.drawable.gf_ic_preview);
        getEnhancedToolbar().getRightIv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FootPrintActivity.this,AddFootprintActivity.class));
            }
        });

        for (int i = 0; i < 50; i++) {
            mData.add(String.valueOf(i));
        }

        mAdapter = new FootPrintAdapter(mData, new FootPrintAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(FootPrintActivity.this,MyFootprintDetailActivity.class));
            }
        });
        rvFootPrint.setAdapter(mAdapter);
        rvFootPrint.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
