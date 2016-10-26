package co.quchu.quchu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;

/**
 * Created by mwb on 16/10/25.
 */
public class MeItemView extends LinearLayout {

  @Bind(R.id.me_cover_img) ImageView mMeCoverImg;
  @Bind(R.id.me_text_tv) TextView mMeTextTv;

  public MeItemView(Context context, AttributeSet attrs) {
    super(context, attrs);

    LayoutInflater.from(context).inflate(R.layout.view_me_item, this);
    ButterKnife.bind(this);

    if (attrs != null) {
      TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MeItemView);
      String textStr = typedArray.getString(R.styleable.MeItemView_miText);
      int imageResId = typedArray.getResourceId(R.styleable.MeItemView_miImage, -1);
      typedArray.recycle();

      if (!TextUtils.isEmpty(textStr)) {
        mMeTextTv.setText(textStr);
      }

      if (imageResId != -1) {
        mMeCoverImg.setImageResource(imageResId);
      }
    }
  }
}
