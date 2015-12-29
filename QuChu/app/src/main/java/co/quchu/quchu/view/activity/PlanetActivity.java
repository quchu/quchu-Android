package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.model.PlanetModel;
import co.quchu.quchu.presenter.PlanetActPresenter;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.adapter.PlanetImgGridAdapter;
import co.quchu.quchu.view.holder.PlanetActHolder;
import co.quchu.quchu.widget.RoundProgressView;
import co.quchu.quchu.widget.planetanimations.Interpolator.BezierInterpolators;
import co.quchu.quchu.widget.planetanimations.MovePath;
import co.quchu.quchu.widget.planetanimations.MyAnimation;
import co.quchu.quchu.widget.textcounter.CounterView;

/**
 * PlanetActivity
 * User: Chenhs
 * Date: 2015-10-21
 * 我的趣星球
 */
public class PlanetActivity extends BaseActivity implements ViewTreeObserver.OnGlobalLayoutListener, AdapterView.OnItemClickListener {
    @Bind(R.id.mid_luncher)
    FrameLayout midLuncher;
    @Bind(R.id.planet_avatar_icon)
    SimpleDraweeView planetAvatarIcon;
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
    @Bind(R.id.planet_myfocus_rl)
    RelativeLayout planetMyfocusRl;
    @Bind(R.id.planet_focusonme_rl)
    RelativeLayout planetFocusonmeRl;
    @Bind(R.id.planet_image_gv)
    GridView planetImageGv;
    @Bind(R.id.planet_collect_ll)
    LinearLayout planetCollectLl;
    @Bind(R.id.planet_discover_ll)
    LinearLayout planetDiscoverLl;
    @Bind(R.id.planet_postcard_ll)
    LinearLayout planetPostcardLl;

    @Bind(R.id.title_content_tv)
    TextView title_content_tv;

    @Bind(R.id.planet_gene_tv)
    TextView planetGeneTv;
    @Bind(R.id.planet_collect_num_tv)
    CounterView planetCollectNumTv;
    @Bind(R.id.planet_discover_num_tv)
    CounterView planetDiscoverNumTv;
    @Bind(R.id.planet_postcard_num_tv)
    CounterView planetPostcardNumTv;
    @Bind(R.id.planet_myfocus_count_cv)
    CounterView planetMyfocusCountCv;
    @Bind(R.id.planet_focusonme_count_cv)
    CounterView planetFocusonmeCountCv;
    private int AnimationDuration = 160 * 1000;
    private PlanetActPresenter presenter;
    private PlanetActHolder planetHolder;
    private AnimatorSet animatorSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet);
        ButterKnife.bind(this);
        initTitleBar();
        title_content_tv.setText(getTitle());
        DialogUtil.showProgess(this, getResources().getString(R.string.loading_dialog_text));
        initActivityViewHolder();
        presenter = new PlanetActPresenter(this);
        presenter.setPlanetGene(planetGeneTv);

        ViewTreeObserver vto = planetAvatarIcon.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(this);

        presenter.initUserStarData(new PlanetActPresenter.PlanetNetListener() {
            @Override
            public void onNetSuccess(PlanetModel model) {
                DialogUtil.dismissProgess();
                planetImageGv.setAdapter(new PlanetImgGridAdapter(PlanetActivity.this, model.getImgs(), model.getImgNum()));
                planetImageGv.setOnItemClickListener(PlanetActivity.this);
                planetAvatarIcon.setImageURI(Uri.parse(AppContext.user.getPhoto()));

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

                planetCollectNumTv.setEndValue(model.getFovNum());
                planetDiscoverNumTv.setEndValue(model.getProposalNum());
                planetPostcardNumTv.setEndValue(model.getCardNum());
                planetMyfocusCountCv.setEndValue(model.getHostNum());
                planetFocusonmeCountCv.setEndValue(model.getFollowNum());
                planetCollectNumTv.start();
                planetDiscoverNumTv.start();
                planetPostcardNumTv.start();
                planetMyfocusCountCv.start();
                planetFocusonmeCountCv.start();

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        ButterKnife.unbind(this);
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
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
    };

    /**
     *
     */
    private void initActivityViewHolder() {
        planetHolder = new PlanetActHolder();
        planetHolder.planetAvatarIcon = planetAvatarIcon;
        planetHolder.designRpv = designRpv;
        planetHolder.pavilionRpv = pavilionRpv;
        planetHolder.atmosphereRpv = atmosphereRpv;
        planetHolder.strollRpv = strollRpv;
        planetHolder.cateRpv = cateRpv;
        planetHolder.planetMyfocusRl = planetMyfocusRl;
        planetHolder.planetFocusonmeRl = planetFocusonmeRl;
        planetHolder.planetImageGv = planetImageGv;
        planetHolder.planetCollectLl = planetCollectLl;
        planetHolder.planetDiscoverLl = planetDiscoverLl;
        planetHolder.planetPostcardLl = planetPostcardLl;
    }

    /**
     * 初始化  旋转动画
     *
     * @return
     */
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
                myHandler.sendMessageDelayed(myHandler.obtainMessage(1), 200);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        myHandler.sendMessageDelayed(myHandler.obtainMessage(0), 3000);
    }

    private int heigh = 0;

    @Override
    public void onGlobalLayout() {
        heigh = midLuncher.getHeight() / 2;
        midLuncher.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        initAnimation();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(PlanetActivity.this, FlickrActivity.class));
    }

    Intent intent;

    @OnClick({R.id.design_rpv, R.id.atmosphere_rpv, R.id.cate_rpv, R.id.pavilion_rpv, R.id.stroll_rpv, R.id.planet_postcard_ll, R.id.planet_discover_ll,
            R.id.planet_collect_ll, R.id.planet_gene_tv, R.id.planet_myfocus_rl, R.id.planet_focusonme_rl})
    public void click(View v) {
        intent = new Intent();
        switch (v.getId()) {
            case R.id.design_rpv: //设计
                intent.setClass(this, FriendsCircleIntroduceActivity.class);
                startActivity(intent);
                break;
            case R.id.pavilion_rpv://展馆
                break;
            case R.id.atmosphere_rpv: //氛围
               /* intent.setClass(this, AtmosphereActivity.class);
                startActivity(intent);*/
                break;
            case R.id.stroll_rpv://逛店

                break;
            case R.id.cate_rpv: //美食

                break;
            case R.id.planet_postcard_ll: //明信片
                intent.setClass(this, PostCardActivity.class);
                startActivity(intent);
                break;
            case R.id.planet_discover_ll: //发现
                intent.setClass(this, DiscoverActivity.class);
                startActivity(intent);
                break;
            case R.id.planet_collect_ll: //收藏
                intent.setClass(this, FavoriteActivity.class);
                startActivity(intent);
                break;
            case R.id.planet_gene_tv:
                startActivity(new Intent(PlanetActivity.this, GeneActivity.class));
                break;
            case R.id.planet_myfocus_rl://趣星人
            case R.id.planet_focusonme_rl://趣星人
                startActivity(new Intent(PlanetActivity.this, QuFriendsActivity.class));
                break;
        }

    }


}
