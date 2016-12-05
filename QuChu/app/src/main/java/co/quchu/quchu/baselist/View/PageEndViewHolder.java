package co.quchu.quchu.baselist.View;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import co.quchu.quchu.R;
import co.quchu.quchu.baselist.Base.BaseViewHolder;

/**
 * Created by Nico on 16/12/2.
 */

public class PageEndViewHolder extends BaseViewHolder {

  ImageView ivPageEndDrawable;
  TextView tvPageEnd;


  public PageEndViewHolder(View itemView) {
    super(itemView);
    ivPageEndDrawable = (ImageView) itemView.findViewById(R.id.ivPageEndDrawable);
    tvPageEnd = (TextView) itemView.findViewById(R.id.tvPageEndText);
  }

  @Override public void onBind(Object d) {
    tvPageEnd.setText(((Boolean)d)?R.string.no_more_data:R.string.loading_dialog_text);
    if ((Boolean)d){
      ivPageEndDrawable.clearAnimation();
    }else{
      RotateAnimation rotateAnimation = new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
      rotateAnimation.setDuration(500);
      rotateAnimation.setRepeatCount(Animation.INFINITE);
      rotateAnimation.setRepeatMode(Animation.RESTART);
      rotateAnimation.setInterpolator(new LinearInterpolator());
      ivPageEndDrawable.setAnimation(rotateAnimation);
      rotateAnimation.start();
    }
  }
}
