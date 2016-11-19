package co.quchu.quchu.widget.DropDownMenu.adapter;

import android.view.View;

import co.quchu.quchu.R;
import co.quchu.quchu.widget.DropDownMenu.MDropBean;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static co.quchu.quchu.base.AppContext.mContext;

/**
 * Created by mwb on 16/11/15.
 */
public class MDropParentAdapter extends MDropDownAdapter {

  @Override
  protected void onBindView(MDropViewHolder holder, final int position) {
    if (mParentList == null || mParentList.get(position) == null) {
      return;
    }

    MDropBean dropBean = mParentList.get(position);

    if (dropBean == null) {
      return;
    }

    holder.mContentTv.setText(dropBean.getText());
    holder.mContentDivider.setVisibility(GONE);
    holder.mContentIv.setVisibility(GONE);
    holder.mContentTv.setTextColor(mContext.getResources().getColor(R.color.standard_color_h2_dark));

    if (dropBean.getType() == MDropBean.DATA_TYPE_CATEGORY) {
      holder.mContentTv.setSelected(mSelectedCategoryIndex == position ? true : false);

    } else if (dropBean.getType() == MDropBean.DATA_TYPE_AREA) {
      holder.mContentTv.setSelected(mSelectedAreaIndex == position ? true : false);

    } else if (dropBean.getType() == MDropBean.DATA_TYPE_SORT) {
      holder.mContentTv.setSelected(true);
      holder.mContentTv.setTextColor(mSelectedSortIndex == position ?
          mContext.getResources().getColor(R.color.standard_color_yellow) : mContext.getResources().getColor(R.color.standard_color_h2_dark));
      holder.mContentDivider.setVisibility(VISIBLE);
      holder.mContentIv.setVisibility(mSelectedSortIndex == position ? VISIBLE : GONE);
    }

    holder.itemView.setTag(dropBean);
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        MDropBean dropBean = (MDropBean) v.getTag();
        if (dropBean != null && mListener != null) {

          if (dropBean.getType() == MDropBean.DATA_TYPE_CATEGORY) {
            mSelectedCategoryIndex = position;

          } else if (dropBean.getType() == MDropBean.DATA_TYPE_AREA) {
            mSelectedAreaIndex = position;

          } else if (dropBean.getType() == MDropBean.DATA_TYPE_SORT) {
            mSelectedSortIndex = position;
          }

          notifyDataSetChanged();

          mListener.onItemClick(dropBean);
        }
      }
    });
  }

  public int getSelectedCategoryIndex() {
    return mSelectedCategoryIndex;
  }

  public int getSelectedAreaIndex() {
    return mSelectedAreaIndex;
  }

  public int getSelectedSortIndex() {
    return mSelectedSortIndex;
  }

  public void setSelectedCategoryIndex(int categoryIndex) {
    mSelectedCategoryIndex = categoryIndex;
  }

  public void setSelectedAreaIndex(int areaIndex) {
    mSelectedAreaIndex = areaIndex;
  }

  public void setSelectedSortIndex(int sortIndex) {
    mSelectedSortIndex = sortIndex;
  }
}
