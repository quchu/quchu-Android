package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.view.adapter.GeneProgressAdapter;
import co.quchu.quchu.widget.RoundProgressView;

/**
 * GeneActivity
 * User: Chenhs
 * Date: 2015-10-27
 */
public class GeneActivity extends BaseActivity {
    @Bind(R.id.gene_introduce)
    RelativeLayout geneIntroduce;
    @Bind(R.id.planet_avatar_icon)
    ImageView planetAvatarIcon;
    @Bind(R.id.mid_luncher)
    FrameLayout midLuncher;
    @Bind(R.id.design_rpv)
    RoundProgressView designRpv;
    @Bind(R.id.pavilion_rpv)
    RoundProgressView pavilionRpv;
    @Bind(R.id.atmosphere_rpv)
    RoundProgressView atmosphereRpv;
    @Bind(R.id.stroll_rpv)
    RoundProgressView strollRpv;
    @Bind(R.id.cate_rpv)
    RoundProgressView cateRpv;
    @Bind(R.id.gene_progress_gv)
    GridView geneProgressGv;
    @Bind(R.id.title_back_rl)
    RelativeLayout titleBackRL;
    @Bind(R.id.title_more_rl)
    RelativeLayout titleMoreRl;
    @Bind(R.id.title_content_tv)
    TextView title_content_tv;
    @Bind(R.id.gene_introduce_tv)
    TextView gene_introduce_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gene);
        ButterKnife.bind(this);
        title_content_tv.setText(getTitle());
        Typeface face= Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
//        Typeface face=Typeface.createFromAsset(getAssets(), "Roboto-Bold.ttf");
        gene_introduce_tv.setTypeface(face);
        geneProgressGv.setAdapter(new GeneProgressAdapter(this,GeneProgressAdapter.GENE));
    }

    @OnClick({R.id.title_back_rl,R.id.gene_introduce})
    public void Click(View view){
        Intent intent=new Intent();
        switch (view.getId()){
            case R.id.title_back_rl:
                GeneActivity.this.finish();
                break;
            case R.id.gene_introduce:
//              跳转什么是趣基因
                intent.setClass(this,GeneIntroduceActivity.class);
                startActivity(intent);
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
