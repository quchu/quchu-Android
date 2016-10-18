package co.quchu.quchu.widget;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.UserInfoModel;

/**
 * Created by mwb on 16/10/18.
 */
public class DrawerHeaderView extends LinearLayout {

  @Bind(R.id.drawerHeaderAvatarImg) SimpleDraweeView mDrawerHeaderAvatarImg;
  @Bind(R.id.drawerHeaderGenderImg) SimpleDraweeView mDrawerHeaderGenderImg;
  @Bind(R.id.drawerHeaderNameTv) TextView mDrawerHeaderNameTv;
  @Bind(R.id.drawerHeaderMarkTv) TextView mDrawerHeaderMarkTv;

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

  public void setUser(UserInfoModel user) {
    if (user == null) {
      return;
    }

    mDrawerHeaderAvatarImg.setImageURI(user.getPhoto());
    mDrawerHeaderNameTv.setText(user.getFullname());
    mDrawerHeaderGenderImg.setImageURI(Uri.parse(
        "res:///" + (user.getGender().equals("男") ? R.mipmap.ic_male
            : R.mipmap.ic_female)));
    mDrawerHeaderMarkTv.setText("");
  }

  private OnDrawerAvatarClickListener mListener;

  public void setOnDrawerAvatarClickListener(OnDrawerAvatarClickListener listener) {
    mListener = listener;
  }

  public interface OnDrawerAvatarClickListener {
    void onAvatarClick();
  }
}
