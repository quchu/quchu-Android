package co.quchu.quchu.widget.DropDownMenu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.widget.DropDownMenu.MDropBean;

/**
 * Created by mwb on 16/11/15.
 */
public abstract class MDropDownAdapter extends RecyclerView.Adapter<MDropDownAdapter.MDropViewHolder> {

  //保存选中的 parent 下标
  protected int mSelectedCategoryIndex = 0;
  protected int mSelectedAreaIndex = 0;
  protected int mSelectedSortIndex = 0;

  //保存选中的 child 下标
  protected int mChildCategorySelectedIndex = 0;
  protected int mChildAreaSelectedIndex = 0;

  //保存选中的 child
  protected MDropBean mSelectedCategoryDropBean;
  protected MDropBean mSelectedAreaDropBean;

  protected List<MDropBean> mParentList;
  protected List<MDropBean> mChildList;
  protected boolean mIsParent;

  @Override
  public MDropViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new MDropViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_drop_conent, parent, false));
  }

  @Override
  public void onBindViewHolder(MDropViewHolder holder, int position) {
    onBindView(holder, position);
  }

  protected abstract void onBindView(MDropViewHolder holder, int position);

  @Override
  public int getItemCount() {
    if (mIsParent) {
      return mParentList != null ? mParentList.size() : 0;
    } else {
      return mChildList != null ? mChildList.size() : 0;
    }
  }

  public void setParentList(List<MDropBean> parentList) {
    mIsParent = true;
    mParentList = parentList;
    notifyDataSetChanged();
  }

  public void setChildList(List<MDropBean> childList) {
    mIsParent = false;
    mChildList = childList;
    notifyDataSetChanged();
  }

  public class MDropViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.item_drop_content_tv) TextView mContentTv;
    @Bind(R.id.item_drop_content_iv) ImageView mContentIv;
    @Bind(R.id.item_drop_content_divider) View mContentDivider;

    public MDropViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  protected OnDropItemClickListener mListener;

  public void setOnDropItemClickListener(OnDropItemClickListener listener) {
    mListener = listener;
  }

  public interface OnDropItemClickListener {
    void onItemClick(MDropBean dropBean);
  }
}
