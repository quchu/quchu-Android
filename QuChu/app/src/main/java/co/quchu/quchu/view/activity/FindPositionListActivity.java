package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.gallery.model.PhotoInfo;
import co.quchu.quchu.model.FindBean;
import co.quchu.quchu.presenter.PageLoadListener;
import co.quchu.quchu.presenter.QuchuPresenter;
import co.quchu.quchu.view.adapter.AdapterBase;
import co.quchu.quchu.view.adapter.FindAdapter;

/**
 * 我发现的趣处列表
 */
public class FindPositionListActivity extends BaseActivity implements AdapterBase.OnLoadmoreListener, PageLoadListener<FindBean>, AdapterBase.OnItemClickListener<FindBean.ResultEntity> {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.findPosition)
    LinearLayout findPosition;
    private QuchuPresenter presenter;
    private int pagesNo = 1;
    private FindAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_position_list);
        ButterKnife.bind(this);
        EnhancedToolbar toolbar = getEnhancedToolbar();

        TextView titleTv = toolbar.getTitleTv();
        titleTv.setText("发现新趣处");

        findPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindPositionListActivity.this, FindPositionActivity.class);
                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        presenter = new QuchuPresenter(this);

        adapter = new FindAdapter();
        adapter.setLoadmoreListener(this);
        adapter.setItemClickListener(this);

        recyclerView.setAdapter(adapter);





    }

    @Override
    protected void onResume() {
        super.onResume();
        pagesNo = 1;
        presenter.getFindData(pagesNo, this);
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    @Override
    public void onLoadmore() {
        presenter.getFindData(pagesNo + 1, this);
    }

    @Override
    public void itemClick(RecyclerView.ViewHolder holder, FindBean.ResultEntity entity, int type, int position) {
        if (type == R.id.swipe_delete_content) {
            String address = entity.getAddress();
            String name = entity.getName();
            int pId = entity.getPId();
            String desc = entity.getInstruction();
            ArrayList<PhotoInfo> photos = new ArrayList<>();
            List<FindBean.ResultEntity.ImageEntity> image = entity.getImage();
            for (FindBean.ResultEntity.ImageEntity item : image) {
                String path = item.getImgpath();
                PhotoInfo info = new PhotoInfo();
                info.setPhotoPath(path);
                photos.add(info);
            }
            Intent intent = new Intent(this, FindPositionActivity.class);
            intent.putExtra(FindPositionActivity.REQUEST_KEY_NAME, name);
            intent.putExtra(FindPositionActivity.REQUEST_KEY_ID, pId);
            intent.putExtra(FindPositionActivity.REQUEST_KEY_POSITION, address);
            intent.putExtra(FindPositionActivity.REQUEST_KEY_DESC, desc);
            intent.putParcelableArrayListExtra(FindPositionActivity.REQUEST_KEY_IMAGE_LIST, photos);
            startActivity(intent);
        } else {
            presenter.deleteMyFindQuchu(entity.getPId(), entity, holder, this);
        }
    }

    public void deleteSucceed(RecyclerView.ViewHolder holder ,FindBean.ResultEntity entity) {
        adapter.removeItem(holder, entity);
    }


    @Override
    public void initData(FindBean bean) {
        pagesNo = bean.getPagesNo();
        adapter.initData(bean.getResult());
    }

    @Override
    public void moreData(FindBean data) {
        pagesNo = data.getPagesNo();
        adapter.addMoreData(data.getResult());
    }

    @Override
    public void nullData() {
        adapter.setLoadMoreEnable(false);
    }

    @Override
    public void netError(final int pagesNo, String massage) {
        adapter.setNetError(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getFindData(pagesNo, FindPositionListActivity.this);
            }
        });

    }
}
