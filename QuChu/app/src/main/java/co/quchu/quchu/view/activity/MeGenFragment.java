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

    private MeActivityPresenter meActivityPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me_gen, container, false);
        ButterKnife.bind(this, view);

        meActivityPresenter = new MeActivityPresenter(getActivity());

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
        meActivityPresenter.getGene(new CommonListener<MyGeneModel>() {
            @Override
            public void successListener(MyGeneModel response) {
                if (response != null) {
                    List<MyGeneModel.GenesEntity> genes = response.getGenes();
                    initGene(genes);
                }
            }

            @Override
            public void errorListener(VolleyError error, String exception, String msg) {
            }
        });
    }

    public void initGene(final List<MyGeneModel.GenesEntity> genes) {
        final String[] labels = new String[genes.size()];
        final float[] values = new float[genes.size()];

        for (int i = 0; i < genes.size(); i++) {
            values[i] = (float) (genes.get(i).getWeight() / 1000);
            labels[i] = genes.get(i).getZh();
        }

        setMaxStatus();

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
                        tuhaoTv.setText(String.valueOf((int) (progress * genes.get(0).getWeight())));
                        chihuoTv.setText(String.valueOf((int) (progress * genes.get(1).getWeight())));
                        haoqiTv.setText(String.valueOf((int) (progress * genes.get(2).getWeight())));
                        shejiaoTv.setText(String.valueOf((int) (progress * genes.get(3).getWeight())));
                        shishangTv.setText(String.valueOf((int) (progress * genes.get(4).getWeight())));
                        wenyiTv.setText(String.valueOf((int) (progress * genes.get(5).getWeight())));
                    }
                });
                va.start();
            }
        }, 100);
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