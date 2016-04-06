package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.QuchuBean;
import co.quchu.quchu.presenter.IQuchuActivity;
import co.quchu.quchu.presenter.QuchuPresenter;
import co.quchu.quchu.view.adapter.QuchuAdapter;
import co.quchu.quchu.widget.ErrorView;

/**
 * 趣处 包含我收藏的和我发现的
 */
public class QuchuActivity extends BaseActivity implements IQuchuActivity {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.Favorite)
    TextView Favorite;
    @Bind(R.id.find)
    TextView find;
    @Bind(R.id.errorView)
    ErrorView errorView;
    private QuchuPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quchu);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        Favorite.setSelected(true);
        initListener();
        presenter = new QuchuPresenter(this, this);
        presenter.getFavoriteData(1);
    }

    private void initListener() {
        Favorite.setOnClickListener(this);
        find.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Favorite:
                Favorite.setSelected(true);
                find.setSelected(false);
                presenter.getFavoriteData(1);
                break;
            case R.id.find:
                Favorite.setSelected(false);
                find.setSelected(true);
                presenter.getFindData(1);
                break;
        }
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }


    @Override
    public void showFavorite(boolean isError, QuchuBean bean) {
        if (isError) {
            errorView.showViewDefault(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.getFavoriteData(1);
                }
            });
            recyclerView.setVisibility(View.GONE);
        } else {
            errorView.himeView();
            recyclerView.setVisibility(View.VISIBLE);
            List<QuchuBean.ResultBean> result = bean.getResult();
            recyclerView.setAdapter(new QuchuAdapter(result));
        }
    }

    @Override
    public void showFind(boolean isError, QuchuBean bean) {
        if (isError) {
            errorView.showViewDefault(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.getFindData(1);
                }
            });
            recyclerView.setVisibility(View.GONE);
        } else {
            errorView.himeView();
            recyclerView.setVisibility(View.VISIBLE);
            List<QuchuBean.ResultBean> result = bean.getResult();
            recyclerView.setAdapter(new QuchuAdapter(result));
        }
    }
}
