package co.quchu.quchu.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;

/**
 * Created by Nico on 16/7/18.
 */
public class PageEndViewHolder extends RecyclerView.ViewHolder {



    @Bind(R.id.textView) TextView textView;
    @Bind(R.id.vDivider) View vDivider;


    public PageEndViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

}
