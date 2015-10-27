package co.quchu.quchu.presenter;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.nineoldandroids.animation.AnimatorSet;

import co.quchu.quchu.R;
import co.quchu.quchu.view.adapter.PlanetImgGridAdapter;
import co.quchu.quchu.view.holder.PlanetActHolder;

/**
 * PlanetActPresenter
 * User: Chenhs
 * Date: 2015-10-27
 */
public class PlanetActPresenter {
    private Activity context;
    private PlanetActHolder planetHolder;
    private int heigh=0;
    private int AnimationDuration = 10 * 1000;
private  AnimatorSet animatorSet;
    public PlanetActPresenter(Activity context,  PlanetActHolder planetHolder) {
        this.context = context;
        this.planetHolder = planetHolder;
    }

    public void setAnimationDur(int animationDur) {
        AnimationDuration = animationDur;
    }

    public void setImageGalery(){
        planetHolder.planetImageGv.setAdapter(new PlanetImgGridAdapter(context));
    }
    public void setPlanetGene(TextView view){
        SpannableStringBuilder builder = new SpannableStringBuilder(view.getText().toString());
        ForegroundColorSpan redSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.planet_progress_yellow));
        builder.setSpan(redSpan, 4, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new StyleSpan(Typeface.BOLD), 4, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(builder);
    }

}
//    design_rpv = (RoundProgressView) findViewById(R.id.design_rpv);
//    pavilion_rpv = (RoundProgressView) findViewById(R.id.pavilion_rpv);
//    atmosphere_rpv = (RoundProgressView) findViewById(R.id.atmosphere_rpv);
//    atmosphere_rpv.setProgress(99);
//    atmosphere_rpv.setTextStyle(2);
//    atmosphere_rpv.setTextSize(14);
//    atmosphere_rpv.setProgressText("氛围");
//    atmosphere_rpv.setImage("http://imgdn.paimeilv.com/1444721523235");
//    stroll_rpv = (RoundProgressView) findViewById(R.id.stroll_rpv);
//    cate_rpv = (RoundProgressView) findViewById(R.id.cate_rpv);
//    GridView gv = (GridView) findViewById(R.id.id_recyclerview);
//    gv.setAdapter(new PlanetImgGridAdapter(this));
//    planet_avatar_icon = (ImageView) findViewById(R.id.planet_avatar_icon);
//    planet_avatar_icon.getViewTreeObserver();
//    ViewTreeObserver vto = planet_avatar_icon.getViewTreeObserver();
//    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//        public void onGlobalLayout() {
//            heigh = planet_avatar_icon.getHeight();
//            planet_avatar_icon.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//            myHandler.sendMessageDelayed(myHandler.obtainMessage(0), 3000);
//        }
//    });
//    pavilion_rpv.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Toast.makeText(PlanetActivity.this, "imageVIew2 is click", Toast.LENGTH_SHORT).show();
//            if (animatorSet != null) {
//                if (animatorSet.isRunning()) {
//                    animatorSet.cancel();
//                } else {
//                    animatorSet.start();
//                }
//            }
//        }
//    });
