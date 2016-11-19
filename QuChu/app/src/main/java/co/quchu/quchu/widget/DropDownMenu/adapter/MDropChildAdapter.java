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
public class MDropChildAdapter extends MDropDownAdapter {

  public void setSelectedAreaDropBean(MDropBean dropBean) {
    mSelectedAreaDropBean = dropBean;
  }

  public void setSelectedCategoryDropBean(MDropBean dropBean) {
    mSelectedCategoryDropBean = dropBean;
  }

  public void setAreaSelectedIndex(int areaSelectedIndex) {
    mChildAreaSelectedIndex = areaSelectedIndex;
  }

  public void setCategorySelectedIndex(int categorySelectedIndex) {
    mChildCategorySelectedIndex = categorySelectedIndex;
  }

  @Override
  protected void onBindView(MDropViewHolder holder, int position) {
    if (mChildList == null && mChildList.get(position) == null) {
      return;
    }

    MDropBean dropBean = mChildList.get(position);

    if (dropBean == null) {
      return;
    }

    holder.mContentTv.setText(dropBean.getText());
    holder.mContentTv.setSelected(true);
    holder.mContentDivider.setVisibility(VISIBLE);

    if (dropBean.getType() == MDropBean.DATA_TYPE_CATEGORY) {

      if (mSelectedCategoryDropBean != null) {
        holder.mContentTv.setTextColor(mSelectedCategoryDropBean.equals(dropBean) ? mContext.getResources().getColor(R.color.standard_color_yellow)
            : mContext.getResources().getColor(R.color.standard_color_h2_dark));
        holder.mContentIv.setVisibility(mSelectedCategoryDropBean.equals(dropBean) ? VISIBLE : GONE);

      } else {
        holder.mContentTv.setTextColor(mChildCategorySelectedIndex == position ? mContext.getResources().getColor(R.color.standard_color_yellow)
            : mContext.getResources().getColor(R.color.standard_color_h2_dark));
        holder.mContentIv.setVisibility(mChildCategorySelectedIndex == position ? VISIBLE : GONE);
      }

    } else if (dropBean.getType() == MDropBean.DATA_TYPE_AREA) {

      if (mSelectedAreaDropBean != null) {
        holder.mContentTv.setTextColor(mSelectedAreaDropBean.equals(dropBean) ? mContext.getResources().getColor(R.color.standard_color_yellow)
            : mContext.getResources().getColor(R.color.standard_color_h2_dark));
        holder.mContentIv.setVisibility(mSelectedAreaDropBean.equals(dropBean) ? VISIBLE : GONE);

      } else {
        holder.mContentTv.setTextColor(mChildAreaSelectedIndex == position ? mContext.getResources().getColor(R.color.standard_color_yellow)
            : mContext.getResources().getColor(R.color.standard_color_h2_dark));
        holder.mContentIv.setVisibility(mChildAreaSelectedIndex == position ? VISIBLE : GONE);
      }
    }

    holder.itemView.setTag(dropBean);
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        MDropBean dropBean = (MDropBean) v.getTag();
        if (dropBean != null && mListener != null) {

          if (dropBean.getType() == MDropBean.DATA_TYPE_CATEGORY) {
            mSelectedCategoryDropBean = dropBean;

          } else if (dropBean.getType() == MDropBean.DATA_TYPE_AREA) {
            mSelectedAreaDropBean = dropBean;
          }

          mListener.onItemClick(dropBean);
        }
      }
    });
  }
}
