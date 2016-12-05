package co.quchu.quchu.baselist.Base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Nico on 16/11/29.
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

  public BaseViewHolder(View itemView) {
    super(itemView);
  }

  public abstract void onBind(T d);

}
