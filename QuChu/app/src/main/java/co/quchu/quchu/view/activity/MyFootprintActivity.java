package co.quchu.quchu.view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.MyFootprintBean;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.view.adapter.MyFootprintAdapter;
import co.quchu.quchu.widget.ScrollIndexView;

public class MyFootprintActivity extends BaseActivity implements IFootprintActivity, MyFootprintAdapter.OnItemClickListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.headViewBg)
    SimpleDraweeView headViewBg;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.headView)
    SimpleDraweeView headView;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.scrollIndexView)
    ScrollIndexView scrollIndexView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footprint);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        MyFootprintAdapter adapter = new MyFootprintAdapter(null, this);
        recyclerView.setAdapter(adapter);
        initTitle();

        headViewBg.setImageURI(Uri.parse("http://h.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=3dc4538262d0f703e6e79dd83dca7d0b/7a899e510fb30f24f570e996c895d143ac4b03b8.jpg"));
        headView.setImageURI(Uri.parse("res:///" + R.mipmap.ic_launcher));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isIdle = true;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LogUtils.e("state:" + newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        if (isIdle) {
                            scrollIndexView.setVisibility(View.VISIBLE);
                            isIdle = false;
                            scrollIndexView.animate()
                                    .scaleX(1)
                                    .scaleY(1)
                                    .translationXBy(-scrollIndexView.getWidth())
                                    .alpha(1)
                                    .setDuration(500)
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            scrollIndexView.setVisibility(View.VISIBLE);
                                        }
                                    })
                                    .setInterpolator(new DecelerateInterpolator())
                                    .start();
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        isIdle = true;
                        scrollIndexView.animate()
                                .scaleX(0)
                                .scaleY(.4f)
                                .alpha(0)
                                .translationX(scrollIndexView.getWidth())
                                .setDuration(500)
                                .setInterpolator(new DecelerateInterpolator())
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        scrollIndexView.setVisibility(View.INVISIBLE);
                                    }
                                })
                                .start();
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                float height = recyclerView.getHeight();
                View childView = recyclerView.getChildAt(0);
                float firstPosition = recyclerView.getChildAdapterPosition(childView);
                float itemCount = recyclerView.getAdapter().getItemCount();
                //第一个item消失的部分
                float itemOrrset = -childView.getTop() / (float) childView.getHeight();
                float targetPosition = (height * ((firstPosition + itemOrrset) / itemCount)) + recyclerView.getY();

                if (targetPosition > recyclerView.getY() + recyclerView.getHeight() - scrollIndexView.getHeight()) {
                    targetPosition = recyclerView.getY() + recyclerView.getHeight() - scrollIndexView.getHeight();
                }

                scrollIndexView.setY(targetPosition);
                for (int i = 0, size = recyclerView.getChildCount(); i < size; i++) {
                    if (recyclerView.getChildAt(i).getY() >= scrollIndexView.getY()) {
                        Random randomh = new Random();
                        int h = randomh.nextInt(13);
                        int m = randomh.nextInt(61);
                        scrollIndexView.startTimeAnamation(h, m);
                        break;
                    }
                }


            }
        });
    }

    private void initTitle() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    @Override
    public void itemClick(MyFootprintBean bean) {
        Intent intent = new Intent(this, MyFootprintDetailActivity.class);
        startActivity(intent);
    }
}
