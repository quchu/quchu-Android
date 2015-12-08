package co.quchu.quchu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import co.quchu.quchu.R;
import co.quchu.quchu.utils.StringUtils;


/**
 * 仿iphone带进度的进度条，线程安全的View，可直接在线程中更新进度
 *
 * @author xiaanming
 */
public class RoundProgressBar extends View {
    /**
     * 画笔对象的引用
     */
    private Paint paint;
    private Paint paintR;

    /**
     * 圆环的颜色
     */
    private int roundColor;

    /**
     * 圆环进度的颜色
     */
    private int roundProgressColor;


    /**
     * 中间进度百分比的字符串的颜色
     */
    private int textColor;

    /**
     * 中间进度百分比的字符串的字体
     */
    private float textSize;

    /**
     * 圆环的宽度
     */
    private float roundWidth;

    /**
     * 最大进度
     */
    private int max = 100;

    /**
     * 当前进度
     */
    private int progress;
    /**
     * 是否显示中间的进度
     */
    private boolean textIsDisplayable;

    /**
     * 进度的风格，实心或者空心
     */
    private int style;
    /**
     * 文字风格
     * 0=隐藏  1=数字  2=文字
     */
    private int textStyle = TextStyleNum;
    public static int TextStyleHide = 0x00;
    public static int TextStyleNum = 0x01;
    public static int TextStyleText = 0x02;
    public static int NumStyleText = 0x03;
    /**
     * 绘制动画进度
     */
    private int drawProgress = 0;
    /**
     * 动画监听间隔时间
     */
    private long AnimationInterval = 60;
    /**
     * 背景颜色
     */
    private int roundProgressBackground;
    private String progressText = "";
    /**
     * 环形
     */
    public static final int STROKE = 0;
    /**
     * 圆形
     */
    public static final int FILL = 1;
    private int spreadCount;
    Typeface fontsType;

    public int progressStyle;
    private static final int DefaultProgressStyle=0x01;
    private static final int AnimationProgressStyle=0x00;

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        paint = new Paint();
        paintR = new Paint();


        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.RoundProgressBar);

        //获取自定义属性和默认值
        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.BLACK);
        roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.BLACK);
        textColor = -1 == (mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.WHITE)) ? roundProgressColor : mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.WHITE);
        textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_textSize, 15);
        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 10);
        max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
        textIsDisplayable = mTypedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, true);
        style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);
        textStyle = mTypedArray.getInt(R.styleable.RoundProgressBar_textStyle, TextStyleNum);
        roundProgressBackground = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressBackground, Color.BLACK);
        progressText = mTypedArray.getString(R.styleable.RoundProgressBar_roundProgressText);
        progress = mTypedArray.getInt(R.styleable.RoundProgressBar_progress, 0);
        progressStyle = mTypedArray.getInt(R.styleable.RoundProgressBar_progress, 0x00);
        mTypedArray.recycle();
        fontsType = Typeface.createFromAsset(getContext().getAssets(), "zzgf_shanghei.otf");
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * 画最外层的大圆环
         */
        int centre = getWidth() / 2; //获取圆心的x坐标
        int radius = (int) (centre - roundWidth / 2); //圆环的半径
        paint.setColor(roundProgressBackground); //设置圆环的颜色
        paint.setStyle(Paint.Style.FILL); //设置空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(centre, centre, radius, paint); //画出圆环
        paintR.setColor(roundColor); //设置圆环的颜色
        paintR.setStyle(Paint.Style.STROKE); //设置空心
        paintR.setStrokeWidth(roundWidth * 2 / 3); //设置圆环的宽度
        paintR.setAntiAlias(true);  //消除锯齿
        int strokeRadius = (int) (centre - roundWidth / 3); //圆环的半径
        canvas.drawCircle(centre, centre, strokeRadius, paintR); //画出圆环
//        Log.e("log", centre + "");

        /**
         * 画进度百分比
         */
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);

        int percent = (int) (((float) drawProgress / (float) max) * 100);  //中间的进度百分比，先转换成float在进行除法运算，不然都为0

        //自定义
        if (textStyle != TextStyleHide) {
            float textWidth = paint.measureText(String.valueOf(spreadCount));   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间

            if (textIsDisplayable && spreadCount >= 0 && style == STROKE) {
                if (textStyle == TextStyleNum) {
                    paint.setTypeface(fontsType); //设置字体
                    canvas.drawText(percent + "%", centre - (textWidth * 1.5f), centre + textSize / 2, paint);//画出进度百分比
                }else if (textStyle == TextStyleText && !StringUtils.isEmpty(progressText))
                    canvas.drawText(progressText, centre - (textWidth * 1.8f), centre + textSize / 2, paint); //画出文字
                else if (textStyle == NumStyleText && !StringUtils.isEmpty(progressText))
                    canvas.drawText(progressText, centre - ((textWidth * 1.8f)/2), centre +((textWidth * 1.8f)/3), paint); //画出文字
            }
        }
        /**
         * 画圆弧 ，画圆环的进度
         */
        //设置进度是实心还是空心

        paint.setStrokeWidth(roundWidth * 2 / 3); //设置圆环的宽度
        paint.setColor(roundProgressColor);  //设置进度的颜色
        RectF oval = new RectF(centre - strokeRadius, centre - strokeRadius, centre
                + strokeRadius, centre + strokeRadius);  //用于定义的圆弧的形状和大小的界限

        switch (style) {
            case STROKE: {
                paint.setStyle(Paint.Style.STROKE);
//                if (progress > 0) {
//                    canvas.drawArc(oval, -94, 360 * (progress + 2) / max, false, paintR);  //根据进度画圆弧
//                }
                canvas.drawArc(oval, -90, 360 * drawProgress / max, false, paint);  //根据进度画圆弧
                break;
            }
            case FILL: {
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (progress != 0)
                    canvas.drawArc(oval, -90, 360 * drawProgress / max, true, paint);  //根据进度画圆弧
                break;
            }
        }

    }

    public synchronized void setSpreadCount(int count) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.spreadCount = count;
    }

    public synchronized int getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     *
     * @param max
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * 获取进度.需要同步
     *
     * @return
     */
    public synchronized int getProgress() {
        return progress;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     *
     * @param progress
     */
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progressStyle==DefaultProgressStyle) {
            if (progress > max) {
                progress = max;
                handler.sendMessageDelayed(handler.obtainMessage(0), AnimationInterval);
            }
            if (progress <= max) {
                this.progress = progress;
                handler.sendMessageDelayed(handler.obtainMessage(0), AnimationInterval);
            }
        }else {
            this.progress=drawProgress=progress>=max?max:progress;
            postInvalidate();
        }
    }

    public int getCricleColor() {
        return roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
        this.textColor = cricleProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

    public void setroundColor(int roundColors) {
        this.roundColor = roundColors;
    }

    public void setProgressText(String progressText) {
        this.progressText = progressText;
    }

    public void setRoundProgressBackground(int roundProgressBackground) {
        this.roundProgressBackground = roundProgressBackground;
    }

    /**
     * 设置空间显示文字风格
     *
     * @param textStyle textStyle== 0=隐藏  1=数字  2=文字
     */
    public void setTextStyle(int textStyle) {
        this.textStyle = textStyle;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (drawProgress < progress) {
                        postInvalidate();
                        drawProgress += 1;
                        if (drawProgress >= progress) {
                            handler.sendMessageDelayed(handler.obtainMessage(1), AnimationInterval);
                        } else {
                            handler.sendMessageDelayed(handler.obtainMessage(0), AnimationInterval);
                        }
                    } else {
                        handler.sendMessageDelayed(handler.obtainMessage(1), AnimationInterval);
                    }
                    break;
                case 1:
                    drawProgress = progress;
                    postInvalidate();
                    break;
            }
        }
    };
}
