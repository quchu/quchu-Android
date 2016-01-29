package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.adapter.GeneProgressAdapter;
import co.quchu.quchu.widget.RoundProgressView;
import co.quchu.quchu.widget.planetanimations.Interpolator.BezierInterpolators;
import co.quchu.quchu.widget.planetanimations.MovePath;
import co.quchu.quchu.widget.planetanimations.MyAnimation;

/**
 * GeneActivity
 * User: Chenhs
 * Date: 2015-10-27
 */
public class GeneActivity extends BaseActivity implements ViewTreeObserver.OnGlobalLayoutListener {
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
    private int AnimationDuration = 160 * 1000;
    private AnimatorSet animatorSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gene);
        ButterKnife.bind(this);
        title_content_tv.setText(getTitle());
        initTitleBar();
        DialogUtil.showProgess(this, getResources().getString(R.string.loading_dialog_text));
        adapter = new GeneProgressAdapter(GeneActivity.this, GeneProgressAdapter.GENE, null);
        geneProgressGv.setAdapter(adapter);
        setGeneData();
        planetAvatarIcon.setImageURI(Uri.parse(AppContext.user.getPhoto()));
        ViewTreeObserver vto = planetAvatarIcon.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(this);
    }


    @OnClick({R.id.gene_introduce, R.id.atmosphere_rpv})
    public void Click(View view) {
        if (KeyboardUtils.isFastDoubleClick())
            return;
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.gene_introduce:
                //  跳转什么是趣基因
                intent.setClass(this, GeneIntroduceActivity.class);
                startActivity(intent);
                break;
            case R.id.atmosphere_rpv:
                //  跳转氛围
              /*  intent.setClass(this, AtmosphereActivity.class);
                startActivity(intent);*/
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (isOnResume) {
                switch (msg.what) {
                    case 0:
                        if (animatorSet != null) {
                            animatorSet.start();
                        } else {
                            initAnimation();
                        }
                        break;
                    case 1:
                        if (animatorSet != null) {
                            if (!animatorSet.isRunning())
                                animatorSet.start();
                        } else {
                            initAnimation();
                        }
                        //   myHandler.sendMessageDelayed(myHandler.obtainMessage(1), 12000);
                        break;
                }
                super.handleMessage(msg);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isOnResume = false;
        if (handler != null)
            handler = null;
        if (animatorSet != null) {
            animatorSet.cancel();
            animatorSet = null;
        }
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
                        adapter.updateGeneData(model.getGenes());
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
                DialogUtil.dismissProgess();
                return false;
            }
        });
    }

    private boolean isOnResume = false;

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isOnResume = true;
     /*   if (adapter != null) {
            adapter.notifyDataSetChanged();
        }*/
    }


    public void initAnimation() {
        final MovePath movePath = new MovePath();
        List animationList = new ArrayList();  //动画集合
        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        //movePath.getCircleData 获取圆形移动路径
        List lis4t = movePath.getCircleData(designRpv, new float[]{StringUtils.dip2px(this, -26), StringUtils.dip2px(this, 88)});
        List list2 = movePath.getCircleData(pavilionRpv, new float[]{StringUtils.dip2px(this, -108), StringUtils.dip2px(this, 14)});
        List list3 = movePath.getCircleData(atmosphereRpv, new float[]{StringUtils.dip2px(this, -107), StringUtils.dip2px(this, -89)});
        List list1 = movePath.getCircleData(strollRpv, new float[]{StringUtils.dip2px(this, 1), StringUtils.dip2px(this, -88)});
        List list5 = movePath.getCircleData(cateRpv, new float[]{StringUtils.dip2px(this, 126), 0});
        MyAnimation moveAnimation = new MyAnimation();
        //将5个button 的移动动画加入list集合中
        animationList.add(moveAnimation.setTranslation(designRpv, (List) lis4t.get(0), (List) lis4t.get(1), AnimationDuration));
        animationList.add(moveAnimation.setTranslation(pavilionRpv, (List) list2.get(0), (List) list2.get(1), AnimationDuration));

        animationList.add(moveAnimation.setTranslation(atmosphereRpv, (List) list3.get(0), (List) list3.get(1), AnimationDuration));
        animationList.add(moveAnimation.setTranslation(strollRpv, (List) list1.get(0), (List) list1.get(1), AnimationDuration));
        animationList.add(moveAnimation.setTranslation(cateRpv, (List) list5.get(0), (List) list5.get(1), AnimationDuration));

        animatorSet = moveAnimation.playTogether(animationList); //动画集合
        animatorSet.setDuration(AnimationDuration);
        animatorSet.setInterpolator(new BezierInterpolators(0.03f, 0.08f, 0.1f, 0.1f));
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                LogUtils.json("planet animation is end");
                if (handler!=null)
                handler.sendMessageDelayed(handler.obtainMessage(1), 200);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        handler.sendMessageDelayed(handler.obtainMessage(0), 3000);
    }

    @Override
    public void onGlobalLayout() {
        initAnimation();
    }


}
