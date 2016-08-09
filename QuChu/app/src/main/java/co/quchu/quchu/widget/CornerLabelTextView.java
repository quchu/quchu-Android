package co.quchu.quchu.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import co.quchu.quchu.R;

/**
 * Created by Nico on 16/8/3.
 */
public class CornerLabelTextView extends TextView {

    public CornerLabelTextView(Context context) {
        super(context);
        init();
    }

    public CornerLabelTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public CornerLabelTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    Paint paint;
    Paint paintBG;
    private void init() {
        paint = new Paint();
        paint.setTextSize(getTextSize());


        paint.setColor(getContext().getResources().getColor(R.color.standard_color_h1_dark));
        paint.setAntiAlias(true);
        paintBG = new Paint(paint);
        paintBG.setColor(getContext().getResources().getColor(R.color.standard_color_yellow));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(-45,getWidth()/2,getWidth()/2);
        float textStartY = (getWidth()/2)-textHeight;

        canvas.drawRect(-40,textStartY-textHeight-16,getWidth()+40,textStartY+8,paintBG);
        canvas.drawText(getText(),0,getText().length(),(getWidth()/2)-(textWidth/2)-4,textStartY-8,paint);

    }

    float textHeight = 0;
    float textWidth = 0;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Rect rect = new Rect();
        paint.getTextBounds((String) getText(),0,getText().length(),rect);
        textHeight = rect.height();
        textWidth = rect.width();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
