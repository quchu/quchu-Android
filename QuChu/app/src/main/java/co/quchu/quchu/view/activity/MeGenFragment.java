package co.quchu.quchu.view.activity;

import android.animation.ValueAnimator;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseFragment;
import co.quchu.quchu.model.MyGeneModel;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.MeActivityPresenter;
import co.quchu.quchu.presenter.UserCenterPresenter;
import co.quchu.quchu.utils.SPUtils;

/**
 * Created by mwb on 16/8/22.
 */
public class MeGenFragment extends BaseFragment {

  @Bind(R.id.tuhao_img)
  ImageView tuhaoImg;
  @Bind(R.id.chihuo_img)
  ImageView chihuoImg;
  @Bind(R.id.haoqi_img)
  ImageView haoqiImg;
  @Bind(R.id.shejiao_img)
  ImageView shejiaoImg;
  @Bind(R.id.shishang_img)
  ImageView shishangImg;
  @Bind(R.id.wenyi_img)
  ImageView wenyiImg;
  @Bind(R.id.tuhao_tv)
  TextView tuhaoTv;
  @Bind(R.id.chihuo_tv)
  TextView chihuoTv;
  @Bind(R.id.haoqi_tv)
  TextView haoqiTv;
  @Bind(R.id.shejiao_tv)
  TextView shejiaoTv;
  @Bind(R.id.shishang_tv)
  TextView shishangTv;
  @Bind(R.id.wenyi_tv)
  TextView wenyiTv;
  @Bind(R.id.gene_top_layout) LinearLayout mGeneTopLayout;

  private static String IS_ME_BUNDLE_KEY = "is_me_bundle_key";
  private static String USER_ID_BUNDLE_KEY = "user_id_bundle_key";
  private MeActivityPresenter meActivityPresenter;
  private boolean mIsMe;//是否是自己
  private int mUserId;
  private List<MyGeneModel.GenesEntity> mGenes;

  public static MeGenFragment newInstance(boolean isMe, int userId) {
    MeGenFragment fragment = new MeGenFragment();
    Bundle bundle = new Bundle();
    bundle.putBoolean(IS_ME_BUNDLE_KEY, isMe);
    bundle.putInt(USER_ID_BUNDLE_KEY, userId);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_me_gen, container, false);
    ButterKnife.bind(this, view);

    meActivityPresenter = new MeActivityPresenter(getActivity());

    mIsMe = getArguments().getBoolean(IS_ME_BUNDLE_KEY, false);
    mUserId = getArguments().getInt(USER_ID_BUNDLE_KEY, -1);

    mGeneTopLayout.setVisibility(mIsMe ? View.VISIBLE : View.GONE);

    Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "AGENCYFB.TTF");
    tuhaoTv.setTypeface(face);
    chihuoTv.setTypeface(face);
    haoqiTv.setTypeface(face);
    shejiaoTv.setTypeface(face);
    shishangTv.setTypeface(face);
    wenyiTv.setTypeface(face);

    getGenes();

    return view;
  }

  private void getGenes() {
    if (mIsMe) {
      meActivityPresenter.getGene(new CommonListener<MyGeneModel>() {
        @Override
        public void successListener(MyGeneModel response) {
          if (response != null) {
            mGenes = response.getGenes();
            initGene(mGenes);
          }
        }

        @Override
        public void errorListener(VolleyError error, String exception, String msg) {
        }
      });

    } else {
      UserCenterPresenter.getPersonGene(getActivity(), mUserId, new CommonListener<MyGeneModel>() {
        @Override
        public void successListener(MyGeneModel response) {
          if (response != null) {
            mGenes = response.getGenes();
            initGene(mGenes);
          }
        }

        @Override
        public void errorListener(VolleyError error, String exception, String msg) {

        }
      });
    }
  }

  public void initGene(final List<MyGeneModel.GenesEntity> genes) {
    if (genes == null || genes.size() < 6) {
      return;
    }
    final String[] labels = new String[genes.size()];
    final float[] values = new float[genes.size()];

    for (int i = 0; i < genes.size(); i++) {
      values[i] = (float) (genes.get(i).getWeight() / 1000);
      labels[i] = genes.get(i).getZh();
    }

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        ValueAnimator va = ValueAnimator.ofFloat(0, 1);
        va.setInterpolator(new AccelerateDecelerateInterpolator());
        va.setDuration(800);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          @Override
          public void onAnimationUpdate(ValueAnimator animation) {
            float progress = (float) animation.getAnimatedValue();
            if (!mIsFragmentAlive) {
              return;
            }

            wenyiTv.setText(String.valueOf((int) (progress * genes.get(0).getWeight())));
            shejiaoTv.setText(String.valueOf((int) (progress * genes.get(1).getWeight())));
            tuhaoTv.setText(String.valueOf((int) (progress * genes.get(2).getWeight())));
            chihuoTv.setText(String.valueOf((int) (progress * genes.get(3).getWeight())));
            shishangTv.setText(String.valueOf((int) (progress * genes.get(4).getWeight())));
            haoqiTv.setText(String.valueOf((int) (progress * genes.get(5).getWeight())));
          }
        });
        va.start();
      }
    }, 100);

    setMaxStatus();
  }

  /**
   * 设置基因
   */
  private void setGenes(float progress, List<MyGeneModel.GenesEntity> genes) {
    for (MyGeneModel.GenesEntity gene : genes) {
      switch (gene.getMark()) {
        case "小食神":
          chihuoTv.setText(String.valueOf((int) (progress * gene.getWeight())));
          break;

        case "艺术家":
          wenyiTv.setText(String.valueOf((int) (progress * gene.getWeight())));
          break;

        case "外交官":
          shejiaoTv.setText(String.valueOf((int) (progress * gene.getWeight())));
          break;

        case "时尚精":
          shishangTv.setText(String.valueOf((int) (progress * gene.getWeight())));
          break;

        case "大财阀":
          tuhaoTv.setText(String.valueOf((int) (progress * gene.getWeight())));
          break;

        case "玩乐咖":
          haoqiTv.setText(String.valueOf((int) (progress * gene.getWeight())));
          break;
      }
    }
  }

  /**
   * 设置最大值的状态
   */
  private void setMaxStatus() {
    String userMark = SPUtils.getUserMark();
    if (userMark == null) {
      return;
    }

    switch (userMark) {
      case "小食神":
        chihuoImg.setImageResource(R.mipmap.ic_chihuo_yellow);
        chihuoTv.setTextColor(getResources().getColor(R.color.colorPrimary));
        break;

      case "艺术家":
        wenyiImg.setImageResource(R.mipmap.ic_wenyi_yellow);
        wenyiTv.setTextColor(getResources().getColor(R.color.colorPrimary));
        break;

      case "外交官":
        shejiaoImg.setImageResource(R.mipmap.ic_shejiao_yellow);
        shejiaoTv.setTextColor(getResources().getColor(R.color.colorPrimary));
        break;

      case "时尚精":
        shishangImg.setImageResource(R.mipmap.ic_shishang_yellow);
        shishangTv.setTextColor(getResources().getColor(R.color.colorPrimary));
        break;

      case "大财阀":
        tuhaoImg.setImageResource(R.mipmap.ic_tuhao_yellow);
        tuhaoTv.setTextColor(getResources().getColor(R.color.colorPrimary));
        break;

      case "玩乐咖":
        haoqiImg.setImageResource(R.mipmap.ic_haoqi_yellow);
        haoqiTv.setTextColor(getResources().getColor(R.color.colorPrimary));
        break;
    }
  }

  @Override
  protected String getPageNameCN() {
    return null;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }
}
