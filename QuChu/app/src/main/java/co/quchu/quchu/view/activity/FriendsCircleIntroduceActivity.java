package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.utils.KeyboardUtils;

/**
 * 什么是趣友圈
 * FriendsCircleIntroduceActivity
 * User: Chenhs
 * Date: 2015-10-28
 */
public class FriendsCircleIntroduceActivity extends BaseActivity {
    @Bind(R.id.gene_introduce_tv)
    TextView geneIntroduceTv;
    @Bind(R.id.gene_introduce)
    RelativeLayout geneIntroduce;
    @Bind(R.id.gene_introduce_iv)
    ImageView geneIntroduceIv;
    @Bind(R.id.title_back_rl)
    RelativeLayout titleBackRL;
    @Bind(R.id.title_more_rl)
    RelativeLayout titleMoreRl;
    @Bind(R.id.title_content_tv)
    TextView title_content_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gene_introduce);
        ButterKnife.bind(this);
        title_content_tv.setText(getTitle().toString());
        geneIntroduceTv.setText(getResources().getString(R.string.subtitle_word_gene_introduce));
        titleMoreRl.setVisibility(View.GONE);
        geneIntroduceIv.setImageResource(R.mipmap.ic_friends_introduce);
    }

    @OnClick({R.id.title_back_rl, R.id.gene_introduce})
    public void click(View v) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
        switch (v.getId()) {
            case R.id.title_back_rl:
                this.finish();
                break;
            case R.id.gene_introduce:
                startActivity(new Intent(FriendsCircleIntroduceActivity.this, GeneIntroduceActivity.class));
                break;
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


    @Override
    protected void onResume() {
        MobclickAgent.onPageStart("FriendsCircleIntroduceActivity");
        overridePendingTransition(R.anim.in_push_right_to_left,
                R.anim.in_stable);
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd("FriendsCircleIntroduceActivity");
    }
}
