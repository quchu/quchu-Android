package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.MyGeneModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
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
    SimpleDraweeView planetAvatarIcon;
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
        initTitleBar();
        DialogUtil.showProgess(this, getResources().getString(R.string.loading_dialog_text));
        setGeneData();
        planetAvatarIcon.setImageURI(Uri.parse(AppContext.user.getPhoto()));

    }


    @OnClick({R.id.gene_introduce, R.id.atmosphere_rpv})
    public void Click(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.gene_introduce:
                //  跳转什么是趣基因
                intent.setClass(this, GeneIntroduceActivity.class);
                startActivity(intent);
                break;
            case R.id.atmosphere_rpv:
                //  跳转氛围
                intent.setClass(this, AtmosphereActivity.class);
                startActivity(intent);
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private GeneProgressAdapter adapter;

    private void setGeneData() {
        NetService.get(this, NetApi.getUserGene, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response != null) {
                    Gson gson = new Gson();
                    MyGeneModel model = gson.fromJson(response.toString(), MyGeneModel.class);
                    if (model != null) {
                        adapter = new GeneProgressAdapter(GeneActivity.this, GeneProgressAdapter.GENE, model.getGenes());
                        geneProgressGv.setAdapter(adapter);
                        DialogUtil.dismissProgess();
                        for (int i = 0; i < model.getStar().size(); i++) {
                            switch (i) {
                                case 0:
                                    designRpv.setImage(model.getStar().get(i).getMinImg());
                                    designRpv.setProgress(model.getStar().get(i).getWeight());
                                    designRpv.setProgressText(model.getStar().get(i).getZh());
                                    break;
                                case 1:
                                    pavilionRpv.setImage(model.getStar().get(i).getMinImg());
                                    pavilionRpv.setProgress(model.getStar().get(i).getWeight());
                                    pavilionRpv.setProgressText(model.getStar().get(i).getZh());
                                    break;
                                case 2:
                                    atmosphereRpv.setImage(model.getStar().get(i).getMinImg());
                                    atmosphereRpv.setProgress(model.getStar().get(i).getWeight());
                                    atmosphereRpv.setProgressText(model.getStar().get(i).getZh());
                                    break;
                                case 3:
                                    cateRpv.setImage(model.getStar().get(i).getMinImg());
                                    cateRpv.setProgress(model.getStar().get(i).getWeight());
                                    cateRpv.setProgressText(model.getStar().get(i).getZh());
                                    break;
                                case 4:
                                    strollRpv.setImage(model.getStar().get(i).getMinImg());
                                    strollRpv.setProgress(model.getStar().get(i).getWeight());
                                    strollRpv.setProgressText(model.getStar().get(i).getZh());
                                    break;
                            }
                        }
                    }
                }
            }

            @Override
            public boolean onError(String error) {
                return false;
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
     /*   if (adapter != null) {
            adapter.notifyDataSetChanged();
        }*/
    }
}
