package co.quchu.quchu.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.animation.AnimatorSet;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.presenter.PlanetActPresenter;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.holder.PlanetActHolder;
import co.quchu.quchu.widget.RoundProgressView;
import co.quchu.quchu.widget.planetanimations.Interpolator.BezierInterpolators;
import co.quchu.quchu.widget.planetanimations.MovePath;
import co.quchu.quchu.widget.planetanimations.MyAnimation;

/**
 * PlanetActivity
 * User: Chenhs
 * Date: 2015-10-21
 * 我的趣星球
 */
public class PlanetActivity extends BaseActivity implements ViewTreeObserver.OnGlobalLayoutListener ,AdapterView.OnItemClickListener {

    @Bind(R.id.mid_luncher)
    FrameLayout midLuncher;
    @Bind(R.id.planet_avatar_icon)
    ImageView planetAvatarIcon;
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
    @Bind(R.id.title_back_rl)
    RelativeLayout titleBackRL;
    @Bind(R.id.title_more_rl)
    RelativeLayout titleMoreRl;
    @Bind(R.id.planet_gene_tv)
    TextView planetGeneTv;
    private int AnimationDuration = 10 * 1000;
    private PlanetActPresenter presenter;
    private PlanetActHolder planetHolder;
    private AnimatorSet animatorSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet);
        ButterKnife.bind(this);
        initActivityViewHolder();
        presenter = new PlanetActPresenter(this, planetHolder);
        presenter.setPlanetGene(planetGeneTv);
        presenter.setImageGalery(planetImageGv, this);
        ViewTreeObserver vto = planetAvatarIcon.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(this);
        Picasso.with(this).load("http://imgdn.paimeilv.com/1444721523235").config(Bitmap.Config.RGB_565)
                .resize(StringUtils.dip2px(this, 50), StringUtils.dip2px(this, 50))
                .centerCrop().into(planetAvatarIcon);
    }

    @OnClick({R.id.title_more_rl,R.id.title_back_rl,R.id.planet_gene_tv})
    public void ViewClick(View v) {
        switch (v.getId()){
            case R.id.title_more_rl:
                Toast.makeText(PlanetActivity.this,"more is click",Toast.LENGTH_SHORT).show();
                break;
            case R.id.title_back_rl:
                Toast.makeText(PlanetActivity.this,"back is click",Toast.LENGTH_SHORT).show();
                break;
               case R.id.planet_gene_tv:
             startActivity(new Intent(PlanetActivity.this,GeneActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (animatorSet!=null){
            animatorSet.cancel();
        }
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
                    if (animatorSet!=null) {
                        animatorSet.start();
                    }else{
                        myHandler.sendMessageDelayed(myHandler.obtainMessage(0), 1000);
                    }
                    break;
                case 1:
                    if (animatorSet != null) {
                        LogUtils.json("animation is running?=" + animatorSet.isRunning());
                        if (!animatorSet.isRunning())
                            animatorSet.start();
                    }
                    myHandler.sendMessageDelayed(myHandler.obtainMessage(1), 12000);
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
        List animationList = new ArrayList();
        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        List lis4t = movePath.getCircleData(designRpv, new float[]{0, 140});
        List list2 = movePath.getCircleData(pavilionRpv, new float[]{-(screenWidth / 2 - heigh / 2) + 40, (heigh / 4) + 20});
        List list3 = movePath.getCircleData(atmosphereRpv, new float[]{-heigh * 2 / 3, -heigh / 2});
        List list1 = movePath.getCircleData(strollRpv, new float[]{20, -heigh});
        List list5 = movePath.getCircleData(cateRpv, new float[]{Math.abs((screenWidth / 2 - heigh / 2) - 20), 0});
        MyAnimation moveAnimation = new MyAnimation();
        animationList.add(moveAnimation.setTranslation(designRpv, (List) lis4t.get(0), (List) lis4t.get(1), AnimationDuration));
        animationList.add(moveAnimation.setTranslation(pavilionRpv, (List) list2.get(0), (List) list2.get(1), AnimationDuration));
        animationList.add(moveAnimation.setTranslation(atmosphereRpv, (List) list3.get(0), (List) list3.get(1), AnimationDuration));
        animationList.add(moveAnimation.setTranslation(strollRpv, (List) list1.get(0), (List) list1.get(1), AnimationDuration));
        animationList.add(moveAnimation.setTranslation(cateRpv, (List) list5.get(0), (List) list5.get(1), AnimationDuration));

        animatorSet = moveAnimation.playTogether(animationList);
        animatorSet.setDuration(AnimationDuration);
        animatorSet.setInterpolator(new BezierInterpolators(0.1f, 0.1f, 0.1f, 0.1f));
        myHandler.sendMessageDelayed(myHandler.obtainMessage(0), 3000);
    }

    private int heigh = 0;

    @Override
    public void onGlobalLayout() {
        heigh = midLuncher.getHeight();
        midLuncher.getViewTreeObserver().removeGlobalOnLayoutListener(this);
         initAnimation();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this,"Image click="+position,Toast.LENGTH_SHORT).show();
    }
}
