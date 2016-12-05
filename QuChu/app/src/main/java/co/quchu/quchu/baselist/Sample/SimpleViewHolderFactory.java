package co.quchu.quchu.baselist.Sample;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import co.quchu.quchu.R;
import co.quchu.quchu.baselist.Base.BaseViewHolder;
import co.quchu.quchu.baselist.Base.BaseViewHolderFactory;

/**
 * Created by Nico on 16/11/29.
 */

public class SimpleViewHolderFactory extends BaseViewHolderFactory {

  public BaseViewHolder getViewHolder(ViewGroup parent,int type){
    return new SimpleItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_txt,parent,false));
  }

}
