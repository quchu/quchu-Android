package co.quchu.quchu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;

import co.quchu.quchu.R;


/**
 * 环形进度条
 */
public class RoundProgressView extends RelativeLayout {

    private SimpleDraweeView imageView;
    private final RoundProgressBar bar;
    private boolean opacity = false;
    private boolean greyscale;
    private Context context;

    /**
     * 初始化
     *
     * @param context
     */
    public RoundProgressView(Context context) {
        this(context, null);
    }

    public RoundProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.round_progress_view, this, true);
        bar = (RoundProgressBar) findViewById(R.id.roundPrgoress_rpb);
        initRoundProgressBar(context, attrs, defStyle);
        imageView = (SimpleDraweeView) findViewById(R.id.roundPrgoress_img);
        bar.bringToFront();
    }

    private void initRoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
        TypedArray mTypedArray = this.context.obtainStyledAttributes(attrs,
                R.styleable.RoundProgressBar);
        //获取自定义属性和默认值
        bar.setroundColor(mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.BLACK));
        bar.setCricleProgressColor(mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.WHITE));
        bar.setTextColor(-1 == mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.WHITE) ? mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.WHITE) : mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.WHITE));
        bar.setTextSize(mTypedArray.getDimension(R.styleable.RoundProgressBar_textSize, 16));
        bar.setRoundWidth(mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 15));
        bar.setMax(mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100));
        bar.setStyle(mTypedArray.getInt(R.styleable.RoundProgressBar_style, RoundProgressBar.STROKE));
        bar.setTextStyle(mTypedArray.getInt(R.styleable.RoundProgressBar_textStyle, RoundProgressBar.TextStyleHide));
        bar.setRoundProgressBackground(mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressBackground, Color.BLACK));
        bar.setProgressText(mTypedArray.getString(R.styleable.RoundProgressBar_roundProgressText));
        bar.setProgress(mTypedArray.getInt(R.styleable.RoundProgressBar_progress,0));
        mTypedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    /**
     * Sets the image scale type according to {@link ScaleType}.
     *
     * @param scale the image ScaleType
     * @author thiagokimo
     * @since 1.3.0
     */
    public void setImageScaleType(ScaleType scale) {
        imageView.setScaleType(scale);
    }

    /**
     * @param progress the progress
     * @since 1.0.0
     */
    public void setProgress(double progress) {
        bar.setProgress((int) progress);
    }

    public void setProgress( int progress) {
bar.setProgress(progress);
    }

    public RoundProgressBar getProgress() {
        return bar;
    }

    /**
     * holo color. <br/>
     * <b>Examples:</b>
     * <ul>
     * <li>holo_blue_bright</li>
     * <li>holo_blue_dark</li>
     * <li>holo_blue_light</li>
     * <li>holo_green_dark</li>
     * <li>holo_green_light</li>
     * <li>holo_orange_dark</li>
     * <li>holo_orange_light</li>
     * <li>holo_purple</li>
     * <li>holo_red_dark</li>
     * <li>holo_red_light</li>
     * </ul>
     *
     * @param androidHoloColor
     * @since 1.0.0
     */
    public void setProgressColor(int androidHoloColor) {
        bar.setCricleProgressColor(getContext().getResources().getColor(androidHoloColor));
    }

    /**
     * @since 1.1.0
     */
    public void setColor(String colorString) {
        bar.setCricleProgressColor(Color.parseColor(colorString));
    }

    /**
     * @param r red
     * @param g green
     * @param b blue
     * @since 1.1.0
     */
    public void setColorRGB(int r, int g, int b) {
        bar.setCricleProgressColor(Color.rgb(r, g, b));
    }

    /**
     * Works when used with
     * <code>android.graphics.Color.rgb(int, int, int)</code>
     * <p/>
     * red
     * green
     * blue
     *
     * @since 1.4.0
     */
    public void setColorRGB(int rgb) {
        bar.setCricleProgressColor(rgb);
    }

    /**
     * This sets the width of the {@link DrawProgressView}.
     *
     * @param width in Dp
     * @since 1.1.0
     */
//    public void setWidth(int width) {
//        int padding = CalculationUtil.convertDpToPx(width, getContext());
//        imageView.setPadding(padding, padding, padding, padding);
//        bar.setWidthInDp(width);
//    }

    /**
     * This sets the alpha of the image in the view. Actually I need to use the
     * deprecated method here as the new one is only available for the API-level
     * 16. And the min API level of this library is 14.
     * <p/>
     * Use this only as private method.
     *
     * @param progress the progress
     */
    private void setOpacity(int progress) {
        imageView.setAlpha((int) (2.55 * progress));
    }

    /**
     * Switches the opacity state of the image. This forces the
     * SquareProgressBar to redraw with the current progress. As bigger the
     * progress is, then more of the image comes to view. If the progress is 0,
     * then you can't see the image at all. If the progress is 100, the image is
     * shown full.
     *
     * @param opacity true if opacity should be enabled.
     * @since 1.2.0
     */
    public void setOpacity(boolean opacity) {
        this.opacity = opacity;
        setProgress(bar.getProgress());
    }

    /**
     * Switches the opacity state of the image. This forces the
     * SquareProgressBar to redraw with the current progress. As bigger the
     * progress is, then more of the image comes to view. If the progress is 0,
     * then you can't see the image at all. If the progress is 100, the image is
     * shown full.
     * <p/>
     * You can also set the flag if the fading should get inverted so the image
     * disappears when the progress increases.
     *
     * @param opacity            true if opacity should be enabled.
     * @param isFadingOnProgress default false. This changes the behavior the opacity works. If
     *                           the progress increases then the images fades. When the
     *                           progress reaches 100, then the image disappears.
     * @since 1.4.0
     */
    public void setOpacity(boolean opacity, boolean isFadingOnProgress) {
        this.opacity = opacity;
        setProgress(bar.getProgress());
    }

    /**
     * You can set the image to b/w with this method. Works fine with the
     * opacity.
     *
     * @param greyscale true if the grayscale should be activated.
     * @since 1.2.0
     */
    public void setImageGrayscale(boolean greyscale) {
        this.greyscale = greyscale;
        if (greyscale) {
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);
            imageView.setColorFilter(new ColorMatrixColorFilter(matrix));
        } else {
            imageView.setColorFilter(null);
        }
    }

    /**
     * If opacity is enabled.
     *
     * @return true if opacity is enabled.
     */
    public boolean isOpacity() {
        return opacity;
    }

    /**
     * If greyscale is enabled.
     *
     * @return true if greyscale is enabled.
     */
    public boolean isGreyscale() {
        return greyscale;
    }

    /**
     * @param image the image as a ressourceId
     * @since 1.0
     */
    public void setImage(int image) {
        imageView.setImageResource(image);
    }

    public void setImage(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    public void setImage(String imageURL) {
       // Picasso.with(context).load(imageURL).resize(65,65).centerInside().transform(new CircleTransform()).into(imageView);
        imageView.setImageURI(Uri.parse(imageURL));
        imageView.setAspectRatio(1.0f);
    }

    public void setCricleColor(int cricleColor) {
        bar.setCricleColor(cricleColor);
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        bar.setCricleProgressColor(cricleProgressColor);
        bar.setTextColor(cricleProgressColor);
    }
    public void setTextColor(int textColor) {
        bar.setTextColor(textColor);
    }
    public void setTextSize(float textSize) {
        bar.setTextSize(textSize);
    }
    public void setRoundWidth(float roundWidth) {
        bar.setRoundWidth(roundWidth);
    }
    public void setroundColor(int roundColors) {
        bar.setroundColor(roundColors);
    }
    public void setProgressText(String progressText) {
        bar.setProgressText(progressText);
    }
    /**
     * 设置空间显示文字风格
     *
     * @param textStyle textStyle== 0=隐藏  1=数字  2=文字
     */
    public void setTextStyle(int textStyle) {
        bar.setTextStyle(textStyle);
    }
}
