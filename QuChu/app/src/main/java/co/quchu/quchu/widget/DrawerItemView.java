package co.quchu.quchu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;

/**
 * Created by mwb on 16/10/18.
 */
public class DrawerItemView extends LinearLayout {

  @Bind(R.id.drawerItemImg) ImageView mDrawerItemImg;
  @Bind(R.id.drawerTitleTv) TextView mDrawerTitleTv;
  @Bind(R.id.unReadMassageView) View mUnReadMassageView;

  public DrawerItemView(Context context, AttributeSet attrs) {
    super(context, attrs);

    LayoutInflater.from(context).inflate(R.layout.view_drawer_item, this);
    ButterKnife.bind(this);

    if (attrs != null) {
      TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DrawerItemView);
      String text = typedArray.getString(R.styleable.DrawerItemView_divText);
      int imageResId = typedArray.getResourceId(R.styleable.DrawerItemView_divImage, -1);
      typedArray.recycle();

      if (!TextUtils.isEmpty(text)) {
          mDrawerTitleTv.setText(text);
      }

      if (imageResId != -1) {
        mDrawerItemImg.setImageResource(imageResId);
      }
    }
  }

  public void showRedDot() {
    if (mUnReadMassageView != null) {
      mUnReadMassageView.setVisibility(VISIBLE);
    }
  }

  public void hideRedDot() {
    if (mUnReadMassageView != null) {
      mUnReadMassageView.setVisibility(INVISIBLE);
    }
  }

  public int getRedDotVisibility() {
    return mUnReadMassageView.getVisibility();
  }
}
