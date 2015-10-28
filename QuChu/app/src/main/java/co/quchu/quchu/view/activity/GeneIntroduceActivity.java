package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;

/**
 * 什么是趣基因
 * GeneIntroduceActivity
 * User: Chenhs
 * Date: 2015-10-28
 */
public class GeneIntroduceActivity extends BaseActivity {
    @Bind(R.id.title_back_rl)
    RelativeLayout titleBackRL;
    @Bind(R.id.title_more_rl)
    RelativeLayout titleMoreRl;
    @Bind(R.id.title_content_tv)
    TextView title_content_tv;
    @Bind(R.id.gene_introduce_tv)
    TextView geneIntroduceTv;
    @Bind(R.id.gene_introduce)
    RelativeLayout geneIntroduce;
    @Bind(R.id.gene_introduce_iv)
    ImageView geneIntroduceIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gene_introduce);
        ButterKnife.bind(this);
        title_content_tv.setText(getTitle().toString());
        geneIntroduceTv.setText(getResources().getString(R.string.subtitle_word_friends_introduce));
        titleMoreRl.setVisibility(View.GONE);
    }

    @OnClick({R.id.title_back_rl, R.id.gene_introduce})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.title_back_rl:
                this.finish();
                break;
            case R.id.gene_introduce:
                startActivity(new Intent(GeneIntroduceActivity.this, FriendsCircleIntroduceActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
