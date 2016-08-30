package co.quchu.quchu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;

/**
 * 基因分析弹窗item view
 *
 * Created by mwb on 16/8/30.
 */
public class MatchUserItemView extends LinearLayout {

  @Bind(R.id.content_tv) TextView mContentTv;
  @Bind(R.id.progressBar) ProgressBar mProgressBar;
  @Bind(R.id.checked_img) ImageView mCheckedImg;

  public MatchUserItemView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    LayoutInflater.from(context).inflate(R.layout.view_match_user_item, this);
    ButterKnife.bind(this);

    if (attrs != null) {
      TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MatchUserItemView);
      String text = typedArray.getString(R.styleable.MatchUserItemView_muiText);
      boolean isProgress = typedArray.getBoolean(R.styleable.MatchUserItemView_muiProgress, true);
      typedArray.recycle();

      if (!TextUtils.isEmpty(text)) {
        mContentTv.setText(text);
      }

      setProgress(isProgress);
    }
  }

  /**
   * 设置文字颜色
   */
  public void setTextColor(int resId) {
    mContentTv.setTextColor(resId);
  }

  /**
   * 进度条是否停止
   */
  public void setProgress(boolean isProgress) {
    if (isProgress) {
      mProgressBar.setVisibility(VISIBLE);
      mCheckedImg.setVisibility(INVISIBLE);
    } else {
      mProgressBar.setVisibility(INVISIBLE);
      mCheckedImg.setVisibility(VISIBLE);
    }
  }
}
