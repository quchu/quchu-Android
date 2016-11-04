package co.quchu.quchu.widget;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.model.MyGeneModel;
import co.quchu.quchu.presenter.CommonListener;
import co.quchu.quchu.presenter.MeActivityPresenter;
import co.quchu.quchu.utils.SPUtils;

/**
 * Created by mwb on 16/10/18.
 */
public class DrawerHeaderView extends LinearLayout {

  @Bind(R.id.drawerHeaderAvatarImg) SimpleDraweeView mDrawerHeaderAvatarImg;
  @Bind(R.id.drawerHeaderGenderImg) SimpleDraweeView mDrawerHeaderGenderImg;
  @Bind(R.id.drawerHeaderNameTv) TextView mDrawerHeaderNameTv;
  @Bind(R.id.drawerHeaderMarkTv) TextView mDrawerHeaderMarkTv;

  private List<MyGeneModel.GenesEntity> mGenes;

  public DrawerHeaderView(Context context, AttributeSet attrs) {
    super(context, attrs);

    LayoutInflater.from(context).inflate(R.layout.view_drawer_header, this);
    ButterKnife.bind(this);

    mDrawerHeaderAvatarImg.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mListener != null) {
          mListener.onAvatarClick();
        }
      }
    });
  }

  public void setUser() {
    if (AppContext.user == null) {
      return;
    }

    if (!AppContext.user.isIsVisitors()) {
      mDrawerHeaderNameTv.setText(AppContext.user.getFullname());
    } else {
      mDrawerHeaderNameTv.setText("未知生物");
    }

    mDrawerHeaderAvatarImg.setImageURI(AppContext.user.getPhoto());
    mDrawerHeaderGenderImg.setImageURI(Uri.parse(
        "res:///" + (AppContext.user.getGender().equals("男") ? R.mipmap.ic_male
            : R.mipmap.ic_female)));
  }

  public void setMark(String mark) {
    if (TextUtils.isEmpty(SPUtils.getUserMark())) {
      mDrawerHeaderMarkTv.setVisibility(GONE);
    } else {
      mDrawerHeaderMarkTv.setVisibility(VISIBLE);
      mDrawerHeaderMarkTv.setText(SPUtils.getUserMark());
    }
  }

  /**
   * 获取基因,取最大值的基因
   * 登录接口限制,后期根据接口调整
   */
  public void getGenes() {
    new MeActivityPresenter(getContext()).getGene(new CommonListener<MyGeneModel>() {
      @Override
      public void successListener(MyGeneModel response) {
        if (null != response) {
          mGenes = response.getGenes();
          getMaxGene(mGenes);
        }
      }

      @Override
      public void errorListener(VolleyError error, String exception, String msg) {
      }
    });
  }

  private void getMaxGene(List<MyGeneModel.GenesEntity> genes) {
    double defaultValue = 0;
    int index = 0;
    for (int i = 0; i < genes.size(); i++) {
      if (genes.get(i).getWeight() > defaultValue) {
        defaultValue = genes.get(i).getWeight();
        index = i;
      }
    }

    String mark = genes.get(index).getMark();
    SPUtils.setUserMark(mark);
    setMark(mark);
  }

  private OnDrawerAvatarClickListener mListener;

  public void setOnDrawerAvatarClickListener(OnDrawerAvatarClickListener listener) {
    mListener = listener;
  }

  public interface OnDrawerAvatarClickListener {
    void onAvatarClick();
  }
}
