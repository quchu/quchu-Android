package co.quchu.quchu.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.PlacePostCardModel;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.adapter.PostCardImageAdapter;

/**
 * PostCardImageActivity
 * User: Chenhs
 * Date: 2015-12-29
 * 明信片查看小图界面
 */
public class PostCardImageActivity extends BaseActivity {

    @Bind(R.id.postcard_detail_finish_tv)
    TextView postcardDetailFinishTv;
    @Bind(R.id.item_recommend_card_title_textrl)
    RelativeLayout itemRecommendCardTitleTextrl;
    @Bind(R.id.add_postcard_image_igv)
    GridView addPostcardImageIgv;
    @Bind(R.id.root_cv)
    CardView rootCv;

    PlacePostCardModel.PageEntity.pPostCardEntity defaulModel;
    PostCardImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postcard_image);
        ButterKnife.bind(this);
        initTitleBar();
        adapter = new PostCardImageAdapter(this);
        addPostcardImageIgv.setAdapter(adapter);
        addPostcardImageIgv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        getDatas();

    }


    private void getDatas() {
        defaulModel = (PlacePostCardModel.PageEntity.pPostCardEntity) getIntent().getSerializableExtra("pCardModel");
        if (defaulModel != null) {
            if (!StringUtils.isEmpty(defaulModel.getRgb())) {
                rootCv.setCardBackgroundColor(Color.parseColor("#" + defaulModel.getRgb()));
            } else {
                rootCv.setCardBackgroundColor(Color.parseColor("#808080"));
            }
            if (defaulModel.getImglist() != null && defaulModel.getImglist().size() > 0)
                adapter.setData(defaulModel.getImglist());
        }
    }

    @OnClick({R.id.postcard_detail_finish_tv})
    public void cardImageClick(View view) {
        switch (view.getId()) {
            case R.id.postcard_detail_finish_tv:
                finish();
                break;
        }
    }


}
