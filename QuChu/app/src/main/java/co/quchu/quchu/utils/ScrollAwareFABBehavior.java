package co.quchu.quchu.utils;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;

/**
 * Created by Nico on 16/10/28.
 */

public class ScrollAwareFABBehavior extends FloatingActionButton.Behavior {

  public ScrollAwareFABBehavior(Context context, AttributeSet attrs) {
    super();
    setAutoHideEnabled(false);
  }


}