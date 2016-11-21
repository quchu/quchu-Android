package co.quchu.quchu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.quchu.quchu.R;

/**
 * Created by mwb on 16/11/21.
 */
public class DrawerMenu extends LinearLayout {

  private TextView mUnreadView;

  public DrawerMenu(Context context) {
    this(context, null);
  }

  public DrawerMenu(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public DrawerMenu(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    LayoutInflater.from(context).inflate(R.layout.view_drawer_menu, this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    mUnreadView = (TextView) findViewById(R.id.tvDrawerUnread);
  }

  public void showRedDot() {
    mUnreadView.setVisibility(VISIBLE);
  }

  public void hideRedDot() {
    if (mUnreadView.getVisibility() == VISIBLE) {
      mUnreadView.setVisibility(INVISIBLE);
    }
  }
}
