package co.quchu.quchu.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.Random;

import co.quchu.quchu.MainActivity;

/**
 * BiubiuEdittext
 * User: Chenhs
 * Date: 2015-12-07
 */
public class BiubiuEdittext extends EditText {
    private ViewGroup contentContainer;
    private int height;
    private String cacheStr = "";
    private Handler mainHandler = null; //与主Activity通信的Handler对象


    public BiubiuEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        setlistener();
        if (isInEditMode()) {
            return;
        }

    }

    public BiubiuEdittext(Context context) {
        this(context, null);
    }

    public BiubiuEdittext(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BiubiuEdittext(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs);
    }


    private void setlistener() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e("BIU", "beforeTextChanged: s=" + s + "...start=" + start + "//after==" + after + "///count=" + count);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 || cacheStr.length() > 0) {
                    if (cacheStr.length() < s.length()) {
                        String last = s.toString().substring(start, start + count);
                        Log.e("BIU", "onTextChanged: s=" + s + "...start=" + start + "//before==" + before + "///count=" + count + "last=" + last);
                        update(last, true);
                    } else {
                        char last = cacheStr.charAt(start);
                        update(String.valueOf(last), false);
                    }
                    cacheStr = s.toString();
                    Log.e("BIU", "onTextChanged: s=" + s + "...start=" + start + "//before==" + before + "///count=" + count + "cacheStr==0" + cacheStr);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private int animationDuration = 0;

    private void update(String last, boolean isUp) {
        final TextView textView = new TextView(getContext());
        textView.setTextColor(this.getHintTextColors());
        textView.setTextSize(this.getTextSize() * 2 / 5);
        textView.setText(last);
        textView.setGravity(Gravity.CENTER);
        contentContainer.addView(textView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.measure(0, 0);


        int pos = getSelectionStart();
        Layout layout = getLayout();
        int line = layout.getLineForOffset(pos);
        int baseline = layout.getLineBaseline(line);
        int ascent = layout.getLineAscent(line);

        float startX = 0;
        float startY = 0;
        float endX = 0;
        float endY = 0;
        if (isUp) {
            startX = layout.getPrimaryHorizontal(pos) + 50;
            startY = height / 3 * 2;
            endX = startX;
            endY = baseline + ascent;
            animationDuration = 300;
        } else {
            endX = new Random().nextInt(contentContainer.getWidth());
            endY = height / 3 * 2;
            startX = layout.getPrimaryHorizontal(pos) + 50;
            startY = baseline + ascent;
            animationDuration = 600;
        }
        final AnimatorSet animSet = new AnimatorSet();
        ObjectAnimator animX = ObjectAnimator.ofFloat(textView, "translationX", startX, endX);
        ObjectAnimator animY = ObjectAnimator.ofFloat(textView, "translationY", startY, endY);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(textView, "scaleX", 0.6f, 0.9f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(textView, "scaleY", 0.6f, 0.9f);

        animY.setInterpolator(new DecelerateInterpolator());
        animSet.setDuration(animationDuration);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                contentContainer.removeView(textView);
            }
        });
        animSet.playTogether(animX, animY, scaleX, scaleY);
        animSet.start();
    }


    private void init() {
        contentContainer = (ViewGroup) ((MainActivity) getContext()).findViewById(android.R.id.content);
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        height = windowManager.getDefaultDisplay().getHeight();
    }
}
