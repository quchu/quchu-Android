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
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.nineoldandroids.animation.AnimatorSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.presenter.PlanetActPresenter;
import co.quchu.quchu.utils.LogUtils;
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



    @Bind(R.id.planet_gene_tv)
    TextView planetGeneTv;
    private int AnimationDuration = 50 * 1000;
    private PlanetActPresenter presenter;
    private PlanetActHolder planetHolder;
    private AnimatorSet animatorSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet);
        ButterKnife.bind(this);
        initTitleBar();
        initActivityViewHolder();
        presenter = new PlanetActPresenter(this, planetHolder);
        presenter.setPlanetGene(planetGeneTv);
        presenter.setImageGalery(planetImageGv, this);
        ViewTreeObserver vto = planetAvatarIcon.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(this);
        planetAvatarIcon.setImageURI(Uri.parse(AppContext.user.getPhoto()));
        atmosphereRpv.setImage("http://e.hiphotos.baidu.com/image/pic/item/dcc451da81cb39db026e7657d2160924ab183000.jpg");

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        LogUtils.json("PlanetActivity   onDestroy ");
    }

    @Override
    protected void onPause() {
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        super.onPause();
        LogUtils.json("PlanetActivity   onPause ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.json("PlanetActivity   onResume ");
    }

    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (animatorSet != null) {
                        animatorSet.start();
                    } else {
                        myHandler.sendMessageDelayed(myHandler.obtainMessage(0), 1000);
                    }
                    break;
                case 1:
                    if (animatorSet != null) {
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
        List animationList = new ArrayList();  //动画集合
        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        //movePath.getCircleData 获取圆形移动路径
        List lis4t = movePath.getCircleData(designRpv, new float[]{0, 140});
        List list2 = movePath.getCircleData(pavilionRpv, new float[]{-(screenWidth / 2 - heigh / 2) + 40, (heigh / 4) + 20});
        List list3 = movePath.getCircleData(atmosphereRpv, new float[]{-heigh * 2 / 3, -heigh / 2});
        List list1 = movePath.getCircleData(strollRpv, new float[]{20, -heigh});
        List list5 = movePath.getCircleData(cateRpv, new float[]{Math.abs((screenWidth / 2 - heigh / 2) - 20), 0});
        MyAnimation moveAnimation = new MyAnimation();
        //将5个button 的移动动画加入list集合中
        animationList.add(moveAnimation.setTranslation(designRpv, (List) lis4t.get(0), (List) lis4t.get(1), AnimationDuration));
        animationList.add(moveAnimation.setTranslation(pavilionRpv, (List) list2.get(0), (List) list2.get(1), AnimationDuration));
        animationList.add(moveAnimation.setTranslation(atmosphereRpv, (List) list3.get(0), (List) list3.get(1), AnimationDuration));
        animationList.add(moveAnimation.setTranslation(strollRpv, (List) list1.get(0), (List) list1.get(1), AnimationDuration));
        animationList.add(moveAnimation.setTranslation(cateRpv, (List) list5.get(0), (List) list5.get(1), AnimationDuration));

        animatorSet = moveAnimation.playTogether(animationList); //动画集合
        animatorSet.setDuration(AnimationDuration);
        animatorSet.setInterpolator(new BezierInterpolators(0.1f, 0.1f, 0.1f, 0.1f));
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
        Toast.makeText(this, "Image click=" + position, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(PlanetActivity.this, FlickrActivity.class));
    }

    Intent intent;

    @OnClick({R.id.design_rpv, R.id.atmosphere_rpv, R.id.cate_rpv, R.id.pavilion_rpv, R.id.stroll_rpv, R.id.planet_postcard_ll, R.id.planet_discover_ll, R.id.planet_collect_ll, R.id.planet_gene_tv})
    public void click(View v) {
        intent = new Intent();
        switch (v.getId()) {
            case R.id.design_rpv: //设计
                intent.setClass(this, FriendsCircleIntroduceActivity.class);
                startActivity(intent);
                break;
            case R.id.pavilion_rpv://展馆
                Toast.makeText(this,"开发中。。。",Toast.LENGTH_SHORT).show();
                break;
            case R.id.atmosphere_rpv: //氛围
                intent.setClass(this, AtmosphereActivity.class);
                startActivity(intent);
                break;
            case R.id.stroll_rpv://逛店
                Toast.makeText(this,"开发中。。。",Toast.LENGTH_SHORT).show();
                break;
            case R.id.cate_rpv: //美食
                Toast.makeText(this,"开发中。。。",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this,"开发中。。。",Toast.LENGTH_SHORT).show();
                break;
            case R.id.planet_gene_tv:
                startActivity(new Intent(PlanetActivity.this, GeneActivity.class));
                break;

        }

    }



}
