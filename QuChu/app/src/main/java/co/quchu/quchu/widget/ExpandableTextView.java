package co.quchu.quchu.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Nico on 16/8/3.
 */
public class ExpandableTextView extends TextView {

    private String mStringCollapse = "Collapse";
    private String mStringExpand = "Expand";
    private int mMaxLines = 3;
    private String mStringFull;
    private boolean mCollapsed = true;


    public ExpandableTextView(Context context) {
        super(context);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setContent(final String str){
        if (null==mStringFull){
            mStringFull = str;
        }
        mStringFull = str;
        setText(str);
        post(new Runnable() {
            @Override
            public void run() {
                if (getLineCount()>=mMaxLines){
                    SpannableString spannableString;
                    if (mCollapsed){
                        setMaxLines(3);
                        int max = getLayout().getLineEnd(2);
                        Spanned finalSpanned = Html.fromHtml(mStringFull.substring(0,max)+mStringExpand);
                        spannableString = new SpannableString(finalSpanned);
                        setMaxLines(4);
                        ClickableSpan clickableSpan = new ClickableSpan() {
                            @Override
                            public void onClick(View widget) {
                                mCollapsed = false;
                                setContent(mStringFull);
                            }

                            @Override
                            public void updateDrawState(TextPaint ds) {
                                super.updateDrawState(ds);
                                ds.setUnderlineText(false);
                                ds.clearShadowLayer();
                                ds.setColor(Color.parseColor("#666666"));
                                ds.setFakeBoldText(true);
                            }
                        };
                        spannableString.setSpan(clickableSpan,max,spannableString.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }else{
                        setMaxLines(Integer.MAX_VALUE);
                        Spanned finalSpanned = Html.fromHtml(mStringFull.substring(0,str.length())+"<br/>"+mStringCollapse);
                        spannableString = new SpannableString(finalSpanned);
                        ClickableSpan clickableSpan = new ClickableSpan() {
                            @Override
                            public void onClick(View widget) {
                                mCollapsed = true;
                                setContent(mStringFull);
                            }

                            @Override
                            public void updateDrawState(TextPaint ds) {
                                super.updateDrawState(ds);
                                ds.setUnderlineText(false);
                                ds.clearShadowLayer();
                                ds.setColor(Color.parseColor("#666666"));
                                ds.setFakeBoldText(true);
                            }
                        };
                        spannableString.setSpan(clickableSpan,str.length(),str.length()+mStringCollapse.length()+1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    setText(spannableString);
                    setMovementMethod(LinkMovementMethod.getInstance());
                }
                setHighlightColor(Color.TRANSPARENT);
            }
        });
    }

    public void setContent(int resId){
        mStringFull = getContext().getString(resId);
        setText(resId);
    }

}
