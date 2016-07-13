package co.quchu.quchu.view.activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.dialog.MenuSettingDialogFg;
import co.quchu.quchu.dialog.VisitorLoginDialogFg;
import co.quchu.quchu.gallery.utils.ImageUtils;
import co.quchu.quchu.model.MyGeneModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.MeActivityPresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.widget.PolygonProgressView;

public class MeFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.headImage)
    SimpleDraweeView headImage;
    @Bind(R.id.quchu)
    View quchu;
    @Bind(R.id.footPrint)
    View footPrint;
    @Bind(R.id.friend)
    View friend;
    @Bind(R.id.polygonProgressView)
    PolygonProgressView polygonProgressView;
    @Bind(R.id.massage)
    View massage;
    @Bind(R.id.alias)
    TextView tvUserNickName;
    @Bind(R.id.desc)
    TextView name;
    //    @Bind(R.id.findPosition)
//    TextView findPosition;
    @Bind(R.id.editOrLoginTV)
    TextView editOrLogin;
    @Bind(R.id.editIcon)
    ImageView editIcon;
    @Bind(R.id.unReadMassage)
    TextView unReadMassage;
    @Bind(R.id.editOrLoginAction)
    RelativeLayout editOrLoginAction;

    @Bind(R.id.tv1)
    TextView tv1;
    @Bind(R.id.tv2)
    TextView tv2;
    @Bind(R.id.tv3)
    TextView tv3;
    @Bind(R.id.tv4)
    TextView tv4;
    @Bind(R.id.tv5)
    TextView tv5;
    @Bind(R.id.tv6)
    TextView tv6;

    private MeActivityPresenter presenter;

    //用户头像
    private String userHead;
    private boolean mProgressViewAnimated = false;

    private int[] bmResource = new int[]{R.mipmap.ic_chihuo, R.mipmap.ic_haoqi, R.mipmap.ic_shejiao, R.mipmap.ic_wenyi, R.mipmap.ic_tuhao, R.mipmap.ic_shishang};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_me, container, false);

        ButterKnife.bind(this, v);

        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "AGENCYFB.TTF");
        tv1.setTypeface(face);
        tv2.setTypeface(face);
        tv3.setTypeface(face);
        tv4.setTypeface(face);
        tv5.setTypeface(face);
        tv6.setTypeface(face);

        presenter = new MeActivityPresenter(getActivity());
//        EnhancedToolbar toolbar = ((BaseActivity)getActivity()).getEnhancedToolbar();
//
//        ImageView imageView = toolbar.getRightIv();
//        imageView.setImageResource(R.mipmap.ic_tools);
//        imageView.setOnClickListener(this);

        initListener();

        getData();
        return v;
    }

    private void getData(){

        userHead = AppContext.user.getPhoto();
        ImageUtils.loadWithAppropriateSize(headImage, Uri.parse(AppContext.user.getPhoto()));
        presenter.getUnreadMassageCound(new CommonListener<Integer>() {
            @Override
            public void successListener(Integer response) {
                notReadMassage(response);
            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {
            }
        });
        presenter.getGene(new CommonListener<MyGeneModel>() {
            @Override
            public void successListener(MyGeneModel response) {
                genes = response.getGenes();
            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("profile");


        if (AppContext.user.isIsVisitors()) {
            //游客
            editOrLogin.setText("登陆");
            editIcon.setVisibility(View.GONE);
        } else {
            editOrLogin.setText("编辑");
            editIcon.setVisibility(View.VISIBLE);
        }
        name.setText(AppContext.user.isIsVisitors() ? "未知生物" : AppContext.user.getFullname());
//更换了头像
        if (!userHead.equals(AppContext.user.getPhoto())) {
            userHead = AppContext.user.getPhoto();
            ImageUtils.loadWithAppropriateSize(headImage, Uri.parse(AppContext.user.getPhoto()));
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("profile");
    }

    private void initListener() {
        quchu.setOnClickListener(this);
        footPrint.setOnClickListener(this);
        friend.setOnClickListener(this);
        massage.setOnClickListener(this);
        //findPosition.setOnClickListener(this);
        editOrLoginAction.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Intent intent;
        UserInfoModel user = AppContext.user;
        switch (v.getId()) {

            case R.id.quchu://收藏
                intent = new Intent(getActivity(), FavoriteActivity.class);
                startActivity(intent);
                break;
            case R.id.footPrint://脚印
                if (user.isIsVisitors()) {
                    //游客
                    VisitorLoginDialogFg dialogFg = VisitorLoginDialogFg.newInstance(0);
                    dialogFg.show(getActivity().getSupportFragmentManager(), "");
                } else {
                    intent = new Intent(getActivity(), MyFootprintActivity.class);
                    intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_ID, AppContext.user.getUserId());
                    intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_AGE, AppContext.user.getAge());
                    intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_FOOTER_COUND, AppContext.user.getCardCount());
                    intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_PHOTO, AppContext.user.getPhoto());
                    intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_FOOTER_TITLE, "我的脚印");
                    intent.putExtra(MyFootprintActivity.REQUEST_KEY_USER_NAME, AppContext.user.getFullname());
                    startActivity(intent);
//                    if (!EventBus.getDefault().isRegistered(this)) {
//                        EventBus.getDefault().register(this);
//                    }
                }
                break;
            case R.id.friend://趣友圈
                if (user.isIsVisitors()) {
                    //游客
                    VisitorLoginDialogFg dialogFg = VisitorLoginDialogFg.newInstance(0);
                    dialogFg.show(getActivity().getSupportFragmentManager(), "");
                } else {
                    intent = new Intent(getActivity(), QuFriendsActivity.class);
//                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this, headImage, QuFriendsActivity.KEY_TRANSITION_ANIMATION).toBundle();
                    startActivity(intent);
                }
                break;
            case R.id.massage://消息中心
                intent = new Intent(getActivity(), MessageCenterActivity.class);
                startActivity(intent);
                unReadMassage.setVisibility(View.INVISIBLE);
                break;
            case R.id.findPosition://发现新去处
                intent = new Intent(getActivity(), FindPositionListActivity.class);
                startActivity(intent);
                break;
            case R.id.toolbar_iv_right:
                MenuSettingDialogFg.newInstance().show(getActivity().getSupportFragmentManager(), "menu_setting");
                break;
            case R.id.editOrLoginAction:
                if (user.isIsVisitors()) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), AccountSettingActivity.class);
                    startActivity(intent);
                }
                break;
        }

    }


    List<MyGeneModel.GenesEntity> genes;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (null != polygonProgressView && !hidden && null != genes && !mProgressViewAnimated) {
            polygonProgressView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initGene();
                    mProgressViewAnimated = true;
                }
            }, 800l);
        }
    }


    private static final int []mBitmapSet = new int[]{R.mipmap.ic_tuhao,R.mipmap.ic_chihuo,R.mipmap.ic_haoqi,R.mipmap.ic_shejiao,R.mipmap.ic_shishang,R.mipmap.ic_wenyi};
    public void initGene() {

        final String[] labels = new String[genes.size()];
        final float[] values = new float[genes.size()];

        for (int i = 0; i < genes.size(); i++) {
            values[i] = (float) (genes.get(i).getWeight() / 1000);
            labels[i] = genes.get(i).getZh();
        }

        polygonProgressView.postDelayed(new Runnable() {
            @Override
            public void run() {

                Bitmap[] bm = new Bitmap[mBitmapSet.length];
                for (int i = 0; i < mBitmapSet.length; i++) {
                    bm[i] = BitmapFactory.decodeResource(getResources(), mBitmapSet[i]);
                }

                polygonProgressView.initial(genes.size(), values, labels, bm);
                polygonProgressView.animateProgress();
                ValueAnimator va = ValueAnimator.ofFloat(0, 1);
                va.setInterpolator(new AccelerateDecelerateInterpolator());
                va.setDuration(800);
                va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float progress = (float) animation.getAnimatedValue();
                        tv1.setText(String.valueOf((int)(progress * genes.get(0).getWeight())));
                        tv2.setText(String.valueOf((int)(progress * genes.get(1).getWeight())));
                        tv3.setText(String.valueOf((int)(progress * genes.get(2).getWeight())));
                        tv4.setText(String.valueOf((int)(progress * genes.get(3).getWeight())));
                        tv5.setText(String.valueOf((int)(progress * genes.get(4).getWeight())));
                        tv6.setText(String.valueOf((int)(progress * genes.get(5).getWeight())));
                    }
                });
                va.start();
            }
        }, 100);
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onMessageEvent(QuchuEventModel event) {
        switch (event.getFlag()){
            case EventFlags.EVENT_DEVICE_NETWORK_AVAILABLE:
                getData();
                break;
        }
    }

    public void notReadMassage(int cound) {
        unReadMassage.setText(String.valueOf(cound));
        unReadMassage.setVisibility(View.VISIBLE);
    }

    public void checkUpdate() {

        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                switch (updateStatus) {
                    case UpdateStatus.Yes: // has update
                        UmengUpdateAgent.showUpdateDialog(getActivity(), updateInfo);
                        break;
                    case UpdateStatus.No: // has no update
                        Toast.makeText(getActivity(), "当前已是最新版本!", Toast.LENGTH_SHORT).show();
                        break;
                    case UpdateStatus.Timeout: // time out
                        Toast.makeText(getActivity(), "网络超时,请稍后重试!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        UmengUpdateAgent.forceUpdate(getActivity());
    }
}
