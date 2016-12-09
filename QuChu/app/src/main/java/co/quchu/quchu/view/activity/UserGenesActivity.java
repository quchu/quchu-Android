package co.quchu.quchu.view.activity;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseBehaviorActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.model.MyGeneModel;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.MeActivityPresenter;
import co.quchu.quchu.widget.GeneItemView;

/**
 * 趣基因介绍
 * <p>
 * Created by mwb on 2016/12/8.
 */
public class UserGenesActivity extends BaseBehaviorActivity {

  @Bind(R.id.tuhao_view) GeneItemView mTuhaoView;
  @Bind(R.id.haoqi_view) GeneItemView mHaoqiView;
  @Bind(R.id.shishang_view) GeneItemView mShishangView;
  @Bind(R.id.waijiao_view) GeneItemView mWaijiaoView;
  @Bind(R.id.wenyi_view) GeneItemView mWenyiView;
  @Bind(R.id.chihuo_view) GeneItemView mChihuoView;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_genes);
    ButterKnife.bind(this);

    initToolbar();

    getGenes();
  }

  private void getGenes() {
    new MeActivityPresenter(this).getGene(new CommonListener<MyGeneModel>() {
      @Override
      public void successListener(MyGeneModel response) {
        if (response != null) {
          fillGenes(response.getGenes());
        }
      }

      @Override
      public void errorListener(VolleyError error, String exception, String msg) {

      }
    });
  }

  private void fillGenes(final List<MyGeneModel.GenesEntity> genes) {
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

            fillGenesWeight(progress, genes);
          }
        });
        va.start();
      }
    }, 100);
  }

  private void fillGenesWeight(float progress, List<MyGeneModel.GenesEntity> genes) {
    for (MyGeneModel.GenesEntity gene : genes) {
      String percent = String.valueOf(((int) (progress * gene.getPrecent()))) + "%";
      switch (gene.getMark()) {
        case "小食神":
          mChihuoView.setWeightValue(percent);
          break;

        case "艺术家":
          mWenyiView.setWeightValue(percent);
          break;

        case "外交官":
          mWaijiaoView.setWeightValue(percent);
          break;

        case "时尚精":
          mShishangView.setWeightValue(percent);
          break;

        case "大财阀":
          mTuhaoView.setWeightValue(percent);
          break;

        case "玩乐咖":
          mHaoqiView.setWeightValue(percent);
          break;
      }
    }
  }

  private void initToolbar() {
    EnhancedToolbar toolbar = getEnhancedToolbar();
    TextView textView = toolbar.getTitleTv();
    toolbar.setBackground(null);
    textView.setText("趣基因介绍");
  }

  @Override
  public ArrayMap<String, Object> getUserBehaviorArguments() {
    return null;
  }

  @Override
  public int getUserBehaviorPageId() {
    return 0;
  }

  @Override
  protected String getPageNameCN() {
    return null;
  }
}
