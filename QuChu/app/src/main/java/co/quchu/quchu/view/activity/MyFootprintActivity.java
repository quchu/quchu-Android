package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.model.PostCardItemModel;
import co.quchu.quchu.model.PostCardModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.presenter.MyFootprintPresenter;
import co.quchu.quchu.presenter.PageLoadListener;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.view.adapter.AdapterBase;
import co.quchu.quchu.view.adapter.MyFootprintAdapter;
import co.quchu.quchu.widget.ScrollIndexView;

/**
 * 我的脚印,如果没有穿id参数 默认显示自己的
 */
public class MyFootprintActivity extends BaseActivity implements PageLoadListener<PostCardModel>, AdapterBase.OnLoadmoreListener, AdapterBase.OnItemClickListener<PostCardItemModel> {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.headView)
    SimpleDraweeView headView;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.scrollIndexView)
    ScrollIndexView scrollIndexView;
    @Bind(R.id.ageAndCound)
    TextView ageAndCound;
    private MyFootprintPresenter presenter;

    private MyFootprintAdapter adapter;
    private int pagesNo = 1;

    public static final String REQUEST_KEY_USER_ID = "userId";
    public static final String REQUEST_KEY_USER_AGE = "age";
    public static final String REQUEST_KEY_USER_PHOTO = "photo";
    public static final String REQUEST_KEY_USER_FOOTER_COUND = "cound";
    public static final String REQUEST_KEY_USER_FOOTER_TITLE = "title";
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footprint);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        initTitle();
        adapter = new MyFootprintAdapter();
        adapter.setLoadmoreListener(this);
        recyclerView.setAdapter(adapter);
        adapter.setItemClickListener(this);
        userId = getIntent().getIntExtra(REQUEST_KEY_USER_ID, AppContext.user.getUserId());

        presenter = new MyFootprintPresenter(this, this);
        presenter.getMyFoiotrintList(userId, pagesNo);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isIdle = true;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
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
                if (targetPosition < recyclerView.getY()) {
                    targetPosition = recyclerView.getY();
                }

                scrollIndexView.setY(targetPosition);
                for (int i = 0, size = recyclerView.getChildCount(); i < size; i++) {
                    View view = recyclerView.getChildAt(i);
                    if (view.getY() + view.getHeight() >= scrollIndexView.getY()) {
                        int position = recyclerView.getChildAdapterPosition(view);
                        if (adapter.getData().size() > position) {
                            String time = adapter.getData().get(position).getTime();
                            scrollIndexView.startTimeAnamation(time);
                        }
                        break;
                    }
                }
            }
        });
    }

    private void initTitle() {
        EnhancedToolbar toolbar = getEnhancedToolbar();
        int age = getIntent().getIntExtra(REQUEST_KEY_USER_AGE, 0);
        int cound = getIntent().getIntExtra(REQUEST_KEY_USER_FOOTER_COUND, 0);
        String uri = getIntent().getStringExtra(REQUEST_KEY_USER_PHOTO);
        String title = getIntent().getStringExtra(REQUEST_KEY_USER_FOOTER_TITLE);
        toolbar.getTitleTv().setText(title);
        String builder = String.valueOf(age) +
                "年," +
                cound +
                "个脚印";
        name.setText(AppContext.user.getFullname());
        ageAndCound.setText(builder);
        headView.setImageURI(Uri.parse(uri + ""));
        ImageView rightIv = toolbar.getRightIv();
        rightIv.setImageResource(R.mipmap.ic_dismiss_dialog);
        rightIv.setRotation(45);
        rightIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyFootprintActivity.this, PickingQuchuActivity.class);
                intent.putExtra(PickingQuchuActivity.REQUEST_KEY_FROM_MY_FOOTPRINT, true);
                startActivity(intent);
            }
        });
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }


    @Override
    public void onLoadmore() {
        presenter.getMyFoiotrintList(userId, pagesNo + 1);
    }

    @Override
    public void itemClick(PostCardItemModel item, int type, int position) {
        Intent intent = new Intent(this, MyFootprintDetailActivity.class);
        intent.putExtra(MyFootprintDetailActivity.REQUEST_KEY_POSITION, position);
        intent.putParcelableArrayListExtra(MyFootprintDetailActivity.REQUEST_KEY_MODEL, (ArrayList) adapter.getData());
        startActivity(intent);
    }

    @Override
    public void initData(PostCardModel data) {
        pagesNo = data.getPagesNo();
        recyclerView.setVisibility(View.VISIBLE);
        adapter.initData(data.getResult());
    }

    @Override
    public void moreData(PostCardModel data) {
        pagesNo = data.getPagesNo();
        adapter.addMoreData(data.getResult());
    }

    @Override
    public void nullData() {
        adapter.setLoadMoreEnable(false);
    }

    @Override
    public void netError(final int pageNo, String massage) {
        adapter.setNetError(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getMyFoiotrintList(userId, pageNo );
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onResume() {
        MobclickAgent.onPageStart("my pic");
        super.onResume();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPageEnd("my pic");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Subscribe
    public void postCardDelete(QuchuEventModel model) {
        if (model.getFlag() == EventFlags.EVENT_POST_CARD_DELETED) {
            pagesNo = 1;
            presenter.getMyFoiotrintList(userId, pagesNo);
        }
    }
}
