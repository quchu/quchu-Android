package co.quchu.quchu.baselist.Sample;

import android.view.View;
import android.widget.TextView;
import co.quchu.quchu.R;
import co.quchu.quchu.baselist.Base.BaseViewHolder;

/**
 * Created by Nico on 16/11/29.
 */

public class SimpleItemViewHolder extends BaseViewHolder<CommentsModel> {

  TextView tvItem;


  public SimpleItemViewHolder(View itemView) {
    super(itemView);
    tvItem = (TextView) itemView.findViewById(R.id.tvItem);
  }

  @Override public void onBind(CommentsModel d) {
    tvItem.setText(d.getUserName()+":"+d.getContent());
  }
}
