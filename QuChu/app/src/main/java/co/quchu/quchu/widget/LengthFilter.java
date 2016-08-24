package co.quchu.quchu.widget;

import android.text.InputFilter;
import android.text.Spanned;

import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.utils.ToastManager;

/**
 * 输入框输入字符限制
 * <p/>
 * Created by mwb on 16/8/23.
 */
public class LengthFilter implements InputFilter {

    private int maxLength;

    public LengthFilter(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        int keep = maxLength - (dest.length() - (dend - dstart));

        if (keep <= 0) {
            ToastManager.getInstance(AppContext.mContext).show("输入字数过长");
            return "";
        } else if (keep >= end - start) {
            return null;
        } else {
            return source;
        }
    }
}
