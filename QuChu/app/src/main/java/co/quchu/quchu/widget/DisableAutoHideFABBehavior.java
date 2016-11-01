package co.quchu.quchu.widget;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;

/**
 * Created by Nico on 16/11/1.
 */

public class DisableAutoHideFABBehavior extends FloatingActionButton.Behavior {
  public DisableAutoHideFABBehavior(Context context, AttributeSet attrs) {
    super(context, attrs);
    setAutoHideEnabled(false);
  }
}
