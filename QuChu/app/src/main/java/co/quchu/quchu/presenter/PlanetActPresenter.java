package co.quchu.quchu.presenter;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

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
    public PlanetActPresenter(Activity context,  PlanetActHolder planetHolder) {
        this.context = context;
    }


    public void setImageGalery(GridView planetImageGv,AdapterView.OnItemClickListener listener){
        planetImageGv.setAdapter(new PlanetImgGridAdapter(context));
        planetImageGv.setOnItemClickListener(listener);
    }
    public void setPlanetGene(TextView view){
        SpannableStringBuilder builder = new SpannableStringBuilder(view.getText().toString());
        ForegroundColorSpan redSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.planet_progress_yellow));
        builder.setSpan(redSpan, 4, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new StyleSpan(Typeface.BOLD), 4, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(builder);
    }

}
