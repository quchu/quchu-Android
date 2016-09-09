package co.quchu.quchu.view.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.gallery.utils.ImageUtils;
import co.quchu.quchu.model.MyGeneModel;
import co.quchu.quchu.model.QuchuEventModel;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.MeActivityPresenter;
import co.quchu.quchu.utils.EventFlags;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.widget.PolygonProgressView;

/**
 * Created by mwb on 16/8/22.
 */
public class MeAvatarFragment extends BaseFragment {

  @Bind(R.id.polygonProgressView) PolygonProgressView polygonProgressView;
  @Bind(R.id.headImage) SimpleDraweeView headImage;
  @Bind(R.id.llheadImage) RelativeLayout llheadImage;
  @Bind(R.id.user_name_tv) TextView userNameTv;
  @Bind(R.id.user_mark_tv) TextView userMarkTv;
  @Bind(R.id.user_mark_img) ImageView userMarkImg;
  @Bind(R.id.user_mark_layout) LinearLayout userMarkLayout;

  //用户头像
  private String userAvatar;
  private MeActivityPresenter meActivityPresenter;
  private List<MyGeneModel.GenesEntity> genes;
  private static final int[] mBitmapSet = new int[] {
      R.mipmap.ic_tuhao_blue, R.mipmap.ic_chihuo_blue, R.mipmap.ic_haoqi_blue,
      R.mipmap.ic_shejiao_blue, R.mipmap.ic_shishang_blue, R.mipmap.ic_wenyi_blue
  };

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_me_avatar, container, false);
    ButterKnife.bind(this, view);

    meActivityPresenter = new MeActivityPresenter(getActivity());

    userAvatar = AppContext.user.getPhoto();

    getGenes();

    return view;
  }

  private void getGenes() {
    meActivityPresenter.getGene(new CommonListener<MyGeneModel>() {
      @Override public void successListener(MyGeneModel response) {
        if (null != response) {

          genes = response.getGenes();
          initGene();
        }
      }

      @Override public void errorListener(VolleyError error, String exception, String msg) {
      }
    });
  }

  public void initGene() {
    final String[] labels = new String[genes.size()];
    final float[] values = new float[genes.size()];

    for (int i = 0; i < genes.size(); i++) {
      values[i] = (float) (genes.get(i).getWeight() / 1000);
      labels[i] = genes.get(i).getZh();
    }

    polygonProgressView.postDelayed(new Runnable() {
      @Override public void run() {

        Bitmap[] bm = new Bitmap[mBitmapSet.length];
        for (int i = 0; i < mBitmapSet.length; i++) {
          bm[i] = BitmapFactory.decodeResource(getResources(), mBitmapSet[i]);
        }

        polygonProgressView.initial(genes.size(), values, labels, bm);
        polygonProgressView.animateProgress();

        final long before = System.currentTimeMillis();
        Uri uri = Uri.parse(AppContext.user.getPhoto());
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
          @Override public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo,
              @Nullable Animatable anim) {
            if (imageInfo == null) {
              return;
            }

            int delay;
            if (System.currentTimeMillis() - before > 400) {
              delay = 0;
            } else {
              delay = (int) (400 - (System.currentTimeMillis() - before));
            }
            headImage.setScaleX(0);
            headImage.setScaleY(0);
            ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(headImage, "scaleX", 1);
            ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(headImage, "scaleY", 1);
            //                        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(headImage,"alpha",0,1);
            AnimatorSet scaleAnimatorSet = new AnimatorSet();
            scaleAnimatorSet.playTogether(scaleXAnimator, scaleYAnimator);
            scaleAnimatorSet.setDuration(800);
            scaleAnimatorSet.setStartDelay(delay);
            scaleAnimatorSet.setInterpolator(new OvershootInterpolator(1.2f));
            scaleAnimatorSet.start();
          }

          @Override public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
          }

          @Override public void onFailure(String id, Throwable throwable) {
          }
        };

        DraweeController controller = Fresco.newDraweeControllerBuilder()
            .setControllerListener(controllerListener)
            .setUri(uri)
            .setAutoPlayAnimations(false)
            .build();
        headImage.setController(controller);
      }
    }, 100);
  }

  @Override protected String getPageNameCN() {
    return null;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override public void onStart() {
    super.onStart();
    EventBus.getDefault().register(this);
  }

  @Override public void onResume() {
    super.onResume();

    if (AppContext.user != null && !AppContext.user.isIsVisitors()) {
      userNameTv.setText(AppContext.user.getFullname());
    } else {
      userNameTv.setText("未知生物");
    }

    String userMark = SPUtils.getUserMark();
    if (userMark != null) {
      userMarkLayout.setVisibility(View.VISIBLE);
      userMarkTv.setText(userMark);

      switch (userMark) {
        case "小食神":
          userMarkImg.setImageResource(R.mipmap.ic_chihuo_ahsy);
          break;

        case "艺术家":
          userMarkImg.setImageResource(R.mipmap.ic_wenyi_ahsy);
          break;

        case "外交官":
          userMarkImg.setImageResource(R.mipmap.ic_shejiao_ahsy);
          break;

        case "时尚精":
          userMarkImg.setImageResource(R.mipmap.ic_shishang_ahsy);
          break;

        case "大财阀":
          userMarkImg.setImageResource(R.mipmap.ic_tuhao_ahsy);
          break;

        case "玩乐咖":
          userMarkImg.setImageResource(R.mipmap.ic_haoqi_ahsy);
          break;
      }
    }

    //更换了头像
    if (!userAvatar.equals(AppContext.user.getPhoto())) {
      userAvatar = AppContext.user.getPhoto();
      ImageUtils.loadWithAppropriateSize(headImage, Uri.parse(AppContext.user.getPhoto()));
    }
  }

  @Override public void onStop() {
    EventBus.getDefault().unregister(this);
    super.onStop();
  }

  @Subscribe public void onMessageEvent(QuchuEventModel event) {
    switch (event.getFlag()) {
      case EventFlags.EVENT_USER_LOGIN_SUCCESS:
        //头像
        userAvatar = AppContext.user.getPhoto();
        ImageUtils.loadWithAppropriateSize(headImage, Uri.parse(AppContext.user.getPhoto()));
        break;

      case EventFlags.EVENT_USER_LOGOUT:

        break;

      case EventFlags.EVENT_USER_INFO_UPDATE:
        //姓名
        userNameTv.setText(AppContext.user.getFullname());
        break;
    }
  }
}
