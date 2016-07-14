package co.quchu.quchu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * Created by Nico on 16/6/3.
 * 防止EditText 无法点击
 */

public class RitalinLayout extends FrameLayout {
    View sticky;

    public RitalinLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        ViewTreeObserver vto = getViewTreeObserver();

        vto.addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                if (newFocus == null) return;

                View baby = getChildAt(0);

                if (newFocus != baby) {
                    ViewParent parent = newFocus.getParent();
                    while (parent != null && parent != parent.getParent()) {
                        if (parent == baby) {
                            sticky = newFocus;
                            break;
                        }
                        parent = parent.getParent();
                    }
                }
            }
        });

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                if (sticky != null) {
                    sticky.requestFocus();
                }
            }
        });
    }
}