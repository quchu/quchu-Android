package co.quchu.quchu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
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
 * Created by mwb on 2016/12/9.
 */
public class GeneItemView extends LinearLayout {

  @Bind(R.id.gene_cover_img) ImageView mGeneCoverImg;
  @Bind(R.id.gene_text_tv) TextView mGeneTextTv;
  @Bind(R.id.gene_weight_tv) TextView mGeneWeightTv;
  @Bind(R.id.gene_text_en_tv) TextView mGeneTextEnTv;

  public GeneItemView(Context context) {
    this(context, null);
  }

  public GeneItemView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public GeneItemView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    LayoutInflater.from(context).inflate(R.layout.view_gene_item, this);

    ButterKnife.bind(this);

    if (attrs != null) {
      TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GeneItemView);
      String textStr = typedArray.getString(R.styleable.GeneItemView_givText);
      String textStrEn = typedArray.getString(R.styleable.GeneItemView_givTextEn);
      int imageResId = typedArray.getResourceId(R.styleable.GeneItemView_givImage, -1);
      typedArray.recycle();

      if (!TextUtils.isEmpty(textStr)) {
        mGeneTextTv.setText(textStr);
      }

      if (!TextUtils.isEmpty(textStrEn)) {
        mGeneTextEnTv.setText(textStrEn);
      }

      if (imageResId != -1) {
        mGeneCoverImg.setImageResource(imageResId);
      }
    }
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    Typeface face = Typeface.createFromAsset(getContext().getAssets(), "BEBAS.OTF");
    mGeneWeightTv.setTypeface(face);
    mGeneTextEnTv.setTypeface(face);
  }

  public void setWeightValue(String value) {
    if (mGeneWeightTv != null) {
      mGeneWeightTv.setText(value);
    }
  }
}
