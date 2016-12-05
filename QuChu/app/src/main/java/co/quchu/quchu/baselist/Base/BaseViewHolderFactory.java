package co.quchu.quchu.baselist.Base;

import android.view.ViewGroup;

/**
 * Created by Nico on 16/11/29.
 */

public abstract class BaseViewHolderFactory {
  public abstract BaseViewHolder getViewHolder(ViewGroup parent,int type);
}
