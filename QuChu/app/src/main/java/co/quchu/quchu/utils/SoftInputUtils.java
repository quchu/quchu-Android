package co.quchu.quchu.utils;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by mwb on 16/11/9.
 */
public class SoftInputUtils {

  /**
   * 隐藏软键盘
   */
  public static void hideSoftInput(Activity activity) {
    InputMethodManager manager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
    if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
      if (activity.getCurrentFocus() != null)
        manager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

//    InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//    manager.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
  }

  public static void hideSoftInput(Context context, EditText editText) {
    InputMethodManager manager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
    manager.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
  }

  /**
   * 显示软键盘
   */
  public static void showSoftInput(Context context, final EditText editText) {
    final InputMethodManager manager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
    editText.postDelayed(new Runnable() {
      @Override
      public void run() {
        manager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
      }
    }, 200);
  }

}
