package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.PostCardItemModel;
import co.quchu.quchu.presenter.MyFootprintPresenter;
import co.quchu.quchu.utils.DateUtils;
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
    private MyFootprintPresenter presenter;

    private List<PostCardItemModel> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footprint);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);

        initTitle();
        presenter = new MyFootprintPresenter(this, this);
        presenter.getMyFoiotrintList();
        headViewBg.setImageURI(Uri.parse("res:///" + R.mipmap.bg_user));
        headView.setImageURI(Uri.parse(AppContext.user.getPhoto()));
        initAnimation();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isIdle = true;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LogUtils.e("state:" + newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        if (isIdle) {
                            isIdle = false;
                            scrollIndexView.show();
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        isIdle = true;
                        scrollIndexView.hide();
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
                    View view = recyclerView.getChildAt(i);
                    if (view.getY() + view.getHeight() >= scrollIndexView.getY()) {
                        int position = recyclerView.getChildAdapterPosition(view);
                        String time = data.get(position).getTime();

                        scrollIndexView.startTimeAnamation(DateUtils.getHour(time), DateUtils.getMin(time));
                        break;
                    }
                }


            }
        });
    }

    private void initAnimation() {


    }


    private void initTitle() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setLogo(R.mipmap.ic_back);
            actionBar.setDisplayHomeAsUpEnabled(false);

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
    public void itemClick(PostCardItemModel bean) {
        Intent intent = new Intent(this, MyFootprintDetailActivity.class);
        intent.putExtra(MyFootprintDetailActivity.REQUEST_KEY_MODEL, bean);
        startActivity(intent);
    }

    @Override
    public void initData(boolean isError, List<PostCardItemModel> data) {
        if (isError || data.size() == 0) {
        } else {
            this.data = data;
            MyFootprintAdapter adapter = new MyFootprintAdapter(data, this);
            recyclerView.setAdapter(adapter);
        }
    }
}
