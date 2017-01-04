package co.quchu.quchu.utils;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Pattern;

/**
 * Emoji表情过滤器、字数符限制
 * <p>
 * Created by W.b on 2016/12/28.
 */
public class EmojiFilter implements InputFilter {

  //可允许输入的最大长度
  private final int maxLength;
  private Context mContext;

  private String mEmoji = "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";

  private Pattern emoji = Pattern.compile(mEmoji, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

  public EmojiFilter(Context context, int maxLength) {
    this.mContext = context;
    this.maxLength = maxLength;
  }

  @Override
  public CharSequence filter(CharSequence source, int start, int end,
                             Spanned dest, int dstart, int dend) {

//    Matcher emojiMatcher = emoji.matcher(source);
//    if (emojiMatcher.find()) {
//      ToastManager.getInstance(mContext).show("不能输入表情");
//      return "";
//    }

    //--------------以下限制EditText字符数--------------
    int keep = maxLength - (dest.length() - (dend - dstart));

    if (keep <= 0) {
      ToastManager.getInstance(mContext).show(String.format("最多输入%d字", maxLength));
      return "";
    } else if (keep >= end - start) {
      return null;
    } else {
      return source;
    }
  }
}