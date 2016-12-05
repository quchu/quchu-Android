package co.quchu.quchu.baselist.Base;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import co.quchu.quchu.R;
import co.quchu.quchu.baselist.View.PageEndViewHolder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Nico on 16/11/29.
 */

public class BaseRecyclerViewAdapter<E> extends RecyclerView.Adapter<BaseViewHolder> {

  private List<E> mDataSet = new ArrayList<>();
  private BaseViewHolderFactory mFactory;
  private boolean mShowLoadMore = false;
  private boolean mNoMoreData = false;
  public static final int TYPE_PAGE_END = 0x0002;

  public BaseRecyclerViewAdapter(BaseViewHolderFactory factory) {
    this.notifyDataSetChanged();
    this.mFactory = factory;
  }

  public void reloadData(List<E> data){
    int sizeB4 = null!=mDataSet&& mDataSet.size()>0?mDataSet.size()-1:0;
    mDataSet.clear();
    notifyItemRangeRemoved(0,sizeB4);
    addData(data);
  }

  public void addData(List<E> data){
    int rangeStart = null!=mDataSet&& mDataSet.size()>0?mDataSet.size()-1:0;
    if (null!=data && data.size()>0){
      mDataSet.addAll(data);
    }
    notifyItemRangeInserted(rangeStart,mDataSet.size()-1);
  }

  @Override public int getItemViewType(int position) {
    if (position<mDataSet.size()-1){
      return super.getItemViewType(position);
    }else{
      return TYPE_PAGE_END;
    }
  }

  @Override public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    switch (viewType){
      case TYPE_PAGE_END:
        return new PageEndViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cp_page_end,parent,false));
      default:
        return mFactory.getViewHolder(parent,viewType);
    }
  }

  @Override public void onBindViewHolder(BaseViewHolder holder, int position) {
    if (mDataSet.size()>0&&position<mDataSet.size()-1){
      holder.onBind(mDataSet.get(position));
    }else if(holder instanceof PageEndViewHolder){
      holder.onBind(mNoMoreData);
    }
  }

  @Override public int getItemCount() {
    return (null!= mDataSet ? mDataSet.size():0)+(mShowLoadMore?1:0);
  }

  public void updateLoadMore(boolean status) {
    mShowLoadMore = status;
    if (getItemCount()>0){
      notifyDataSetChanged();
    }
  }

  public void updateNoMore(boolean status) {
    mNoMoreData = status;
    if (getItemCount()>0){
      notifyDataSetChanged();
    }
  }

}
