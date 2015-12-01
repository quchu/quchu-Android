package co.quchu.quchu.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * KeyboardUtils
 * User: Chenhs
 * Date: 2015-12-01
 */
public class KeyboardUtils {

    /**
     * 隐藏软键盘
     *
     * @param mcontext 上下文
     */
    public static void closeBoard(Context mcontext, View view) {
        InputMethodManager imm = (InputMethodManager) mcontext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
