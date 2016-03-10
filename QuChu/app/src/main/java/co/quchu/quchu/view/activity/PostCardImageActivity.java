package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.model.PostCardItemModel;
import co.quchu.quchu.photo.previewimage.ImageBDInfo;
import co.quchu.quchu.photo.previewimage.PreviewImage;
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
    @Bind(R.id.animation1)
    RelativeLayout itemRecommendCardTitleTextrl;
    @Bind(R.id.add_postcard_image_igv)
    GridView addPostcardImageIgv;
    @Bind(R.id.root_cv)
    CardView rootCv;

    PostCardItemModel defaulModel;
    PostCardImageAdapter adapter;
    ImageBDInfo bdInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postcard_image);
        ButterKnife.bind(this);
        initTitleBar();
        bdInfo = new ImageBDInfo();
        adapter = new PostCardImageAdapter(this);
        addPostcardImageIgv.setAdapter(adapter);
        addPostcardImageIgv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             //   Toast.makeText(PostCardImageActivity.this, "点击图片" + position, Toast.LENGTH_SHORT).show();
                View c = addPostcardImageIgv.getChildAt(0);
                int top = c.getTop();
                int firstVisiblePosition = addPostcardImageIgv.getFirstVisiblePosition() / 4;
                int a, b;
                a = position / 4;
                b = position % 4;
                bdInfo.width = (AppContext.Width - (2 * StringUtils.dip2px(34))) / 4;
                bdInfo.height = bdInfo.width;
                bdInfo.x = StringUtils.dip2px(32) + b * bdInfo.width + b * StringUtils.dip2px(4);
                bdInfo.y = StringUtils.dip2px(1) + bdInfo.height * (a - firstVisiblePosition) + top + (a - firstVisiblePosition) * StringUtils.dip2px(2) + addPostcardImageIgv.getTop() - StringUtils.dip2px(1)
                        + StringUtils.dip2px(128);

                Intent intent = new Intent(PostCardImageActivity.this, PreviewImage.class);
                intent.putExtra("data", (Serializable) defaulModel);
                intent.putExtra("bdinfo", bdInfo);
                intent.putExtra("index", position);
                intent.putExtra("type", 2);
                startActivity(intent);
            }
        });
        getDatas();

    }
/*Error:Execution failed for task ':app:transformClassesAndResourcesWithProguardForRelease'.
> java.io.IOException: Please correct the above warnings first.*/

    private void getDatas() {
        defaulModel = (PostCardItemModel) getIntent().getSerializableExtra("pCardModel");
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
    @Override
    protected void onResume() {
        MobclickAgent.onPageStart("PostCardImageActivity");
        MobclickAgent.onResume(this);

        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd("PostCardImageActivity");
        MobclickAgent.onPause(this);
    }

}
