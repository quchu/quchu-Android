package co.quchu.quchu.widget.AnimationViewPager;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * User: Chenhs
 * Date: 2015-12-08
 *  viewpager 切换动画
 */
public class RotatePageTransformer implements ViewPager.PageTransformer {
         float rotation;

    public void transformPage(View view, float position) {
        rotation = 180f * position;

        view.setAlpha(rotation > 90f || rotation < -90f ? 0 : 1);
        view.setPivotX(view.getWidth() * 0.5f);
        view.setPivotY(view.getHeight() * 0.5f);
        view.setRotationY(rotation);

    }


}
