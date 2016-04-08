package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.analysis.GatherCollectModel;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.ShareDialogFg;
import co.quchu.quchu.dialog.VisitorLoginDialogFg;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.presenter.InterestingDetailPresenter;
import co.quchu.quchu.presenter.RecommendPresenter;
import co.quchu.quchu.view.adapter.ClassifyDecoration;
import co.quchu.quchu.view.adapter.RecommendAdapterLite;

public class ClassifyDetailActivity extends BaseActivity implements RecommendAdapterLite.CardClickListener {


    @Bind(R.id.title_content_tv)
    TextView titleContentTv;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    public ArrayList<RecommendModel> dCardList;

    public static final String PARAMETER_TITLE = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify_detail);
        ButterKnife.bind(this);
        initTitleBar();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new ClassifyDecoration(this));
        adapter = new RecommendAdapterLite(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        titleContentTv.setText(getIntent().getExtras().getString(PARAMETER_TITLE));
        changeDataSetFromServer();
    }

    @Override
    protected int activitySetup() {
        return TRANSITION_TYPE_LEFT;
    }

    private RecommendAdapterLite adapter;


    public void changeDataSetFromServer() {
        RecommendPresenter.getRecommendList(this, false, new RecommendPresenter.GetRecommendListener() {
            @Override
            public void onSuccess(ArrayList<RecommendModel> arrayList, int pageCount, int pageNum) {
                dCardList = arrayList;
                adapter.changeDataSet(dCardList);
                recyclerView.smoothScrollToPosition(0);
            }

            @Override
            public void onError() {

            }
        });


    }


    @Override
    public void onCardLick(View view, int position) {

        switch (view.getId()) {
            case R.id.root_cv:
                AppContext.selectedPlace = dCardList.get(position);
                Intent intent = new Intent(this, QuchuDetailsActivity.class);
                intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, dCardList.get(position).getPid());
                this.startActivity(intent);
                break;
            case R.id.item_recommend_card_collect_rl:
                setFavorite(position);
                break;
            case R.id.item_recommend_card_interest_rl:
                ShareDialogFg shareDialogFg = ShareDialogFg.newInstance(dCardList.get(position).getPid(), dCardList.get(position).getName(), true);
                shareDialogFg.show(this.getFragmentManager(), "share_place");
                break;
        }
    }

    private void setFavorite(final int position) {
        if (AppContext.user.isIsVisitors()) {
            VisitorLoginDialogFg vDialog = VisitorLoginDialogFg.newInstance(VisitorLoginDialogFg.QFAVORITE);
            vDialog.show(this.getFragmentManager(), "visitor");
        } else {
            InterestingDetailPresenter.setDetailFavorite(this, dCardList.get(position).getPid(), dCardList.get(position).isIsf(), new InterestingDetailPresenter.DetailDataListener() {
                @Override
                public void onSuccessCall(String str) {
                    dCardList.get(position).setIsf(!dCardList.get(position).isIsf());
                    adapter.notifyDataSetChanged();
                    if (dCardList.get(position).isIsf()) {
                        Toast.makeText(ClassifyDetailActivity.this, "收藏成功!", Toast.LENGTH_SHORT).show();
                        AppContext.gatherList.add(new GatherCollectModel(GatherCollectModel.collectPlace, dCardList.get(position).getPid()));
                    } else {
                        Toast.makeText(ClassifyDetailActivity.this, "取消收藏!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onErrorCall(String str) {

                }
            });
        }
    }
}