package co.quchu.quchu.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.quchu.quchu.R;

/**
 * HorizontalNumProgressBar
 * User: Chenhs
 * Date: 2015-12-08
 */
public class HorizontalNumProgressBar extends RelativeLayout {
    private ProgressBar progressBar;
    private TextView progressNum;
    private TextView progressName;

    public HorizontalNumProgressBar(Context context) {
        this(context, null);
    }

    public HorizontalNumProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalNumProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(getContext(),R.layout.widget_horizontal_progressbar_num, this);
        Typeface fontsType = Typeface.createFromAsset(getContext().getAssets(), "zzgf_shanghei.otf");
        progressBar = (ProgressBar) findViewById(R.id.widget_horizontal_pb);
        progressNum = (TextView) findViewById(R.id.widget_horizontal_progress_num_tv);
        progressName = (TextView) findViewById(R.id.widget_horizontal_progress_name_tv);
        progressNum.setTypeface(fontsType);
    }

    public void setProgress(String progress) {
        setProgress(Integer.valueOf(progress));
    }


    public void setProgress(int progress) {
        if (progress <= 80) {
            setProgressBarWhite();
        //    progressNum.setTextColor(getResources().getColor(R.color.load_progress_white));
        } else {
            setProgressBarYellow();
          /*  if (progress >= 95) {
                progressNum.setTextColor(getResources().getColor(R.color.black));
            } else {
                progressNum.setTextColor(getResources().getColor(R.color.load_progress_white));
            }*/
        }
        progressBar.setProgress(progress);
        progressNum.setText(progress + "%");
    }

    public void setProgress(double progress) {
        setProgress(progress);
    }

    public void setProgressBarYellow() {
        progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar_yellow));
    }

    public void setProgressBarWhite() {
        progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar_white));
    }

    public void setProgressName(String names) {
        progressName.setText(names.length() == 2 ? names.substring(0, 1) + "　" + names.substring(1, 2) : names);
    }

    public void setProgressNameColor(int colorId) {
        progressName.setTextColor(getResources().getColor(colorId));
    }
}
