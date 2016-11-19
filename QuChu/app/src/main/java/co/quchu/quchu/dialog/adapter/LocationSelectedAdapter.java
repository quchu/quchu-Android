package co.quchu.quchu.dialog.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.model.CityModel;
import co.quchu.quchu.utils.SPUtils;

/**
 * LocationSelectedAdapter
 * User: Chenhs
 * Date: 2015-12-24
 */
public class LocationSelectedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private int ITEM_TYPE_HEADER = 1;
  private int ITEM_TYPE_NORMAL = 2;

  private Context mContext;
  private ArrayList<CityModel> mCityList;
  private int selectedIndex = 0;
  private OnItemSelectedListener mListener;

  public LocationSelectedAdapter(ArrayList<CityModel> cityList, Context mContext, OnItemSelectedListener listener) {
    mCityList = cityList;
    this.mContext = mContext;
    this.mListener = listener;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == ITEM_TYPE_HEADER) {
      return new CityHeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city_list_header, parent, false));
    }

    return new LocationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_item_city, parent, false));
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
    if (viewHolder instanceof CityHeaderViewHolder) {
      CityHeaderViewHolder holder = (CityHeaderViewHolder) viewHolder;
      holder.mCityGroupTv.setText(mCityList.get(position).getCvalue());

    } else if (viewHolder instanceof LocationViewHolder) {
      LocationViewHolder holder = (LocationViewHolder) viewHolder;
      CityModel cityModel = mCityList.get(position);
      holder.dialogItemCityCb.setText(cityModel.getCvalue());
      int cityId = SPUtils.getCityId();
      if (cityId == cityModel.getCid()) {
        holder.dialogItemCityCb.setChecked(true);
        holder.dialogItemCityCb.setClickable(false);
//            holder.dialogItemCityCb.setTextColor(mContext.getResources().getColor(R.color.standard_color_yellow));
      } else {
        holder.dialogItemCityCb.setChecked(false);
        holder.dialogItemCityCb.setClickable(true);
//            holder.dialogItemCityCb.setTextColor(mContext.getResources().getColor(R.color.standard_color_white));
      }
    }
  }

  @Override
  public int getItemCount() {
    return mCityList != null ? mCityList.size() : 0;
  }

  @Override
  public int getItemViewType(int position) {
    if (mCityList.get(position).isGroupHeader()) {
      return ITEM_TYPE_HEADER;
    }
    return ITEM_TYPE_NORMAL;
  }

  public class LocationViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.dialog_item_city_cb)
    public CheckBox dialogItemCityCb;

    public LocationViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    @OnClick(R.id.dialog_item_city_cb)
    public void locationClick(View view) {
      CityModel model = mCityList.get(getAdapterPosition());
      notifyDataSetChanged();
      if (null != mListener) {
        mListener.onSelected(model.getCvalue(), model.getCid());
      }
    }
  }

  public class CityHeaderViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.city_group_tv) TextView mCityGroupTv;

    public CityHeaderViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  public interface OnItemSelectedListener {
    void onSelected(String cityName, int cityId);
  }

  public int getSelectedIndex() {
    return selectedIndex;
  }

}
