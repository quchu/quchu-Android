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
import co.quchu.quchu.utils.StringUtils;

/**
 * LocationSelectedAdapter
 * User: Chenhs
 * Date: 2015-12-24
 */
public class LocationSelectedAdapter extends RecyclerView.Adapter<LocationSelectedAdapter.LocationHodler> {

    private Context mContext;
    private ArrayList<CityModel> cityList;
    private TextView titleText;
    private int selectedIndex = 0;
    private int dataType = 0;

    public LocationSelectedAdapter(ArrayList<CityModel> cList, TextView titleText, Context mContext) {
        cityList = cList;
        this.titleText = titleText;
        this.mContext = mContext;
    }

    public LocationSelectedAdapter(ArrayList<CityModel> cList, TextView titleText, Context mContext, int dataType) {
        cityList = cList;
        this.titleText = titleText;
        this.mContext = mContext;
        this.dataType = dataType;
    }

    @Override
    public LocationHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        LocationHodler holder = new LocationHodler((LayoutInflater.from(
                mContext).inflate(R.layout.dialog_item_city, parent,
                false)));
        return holder;
    }


    @Override
    public void onBindViewHolder(LocationHodler holder, int position) {
        holder.dialogItemCityCb.setText(cityList.get(position).getCvalue());
        if (cityList.get(position).isSelected()) {
            holder.dialogItemCityCb.setChecked(true);
            holder.dialogItemCityCb.setClickable(false);
            selectedIndex = position;
            holder.dialogItemCityCb.setTextColor(mContext.getResources().getColor(R.color.gene_textcolor_yellow));
            if (dataType == 0) {
                titleText.setText("所在城市:" + cityList.get(position).getCvalue());
            } else {
                titleText.setText("设置性别:" + cityList.get(position).getCvalue());
            }
            StringUtils.alterTextColor(titleText, 5, 5 + cityList.get(position).getCvalue().length(), R.color.gene_textcolor_yellow);
        } else {
            holder.dialogItemCityCb.setChecked(false);
            holder.dialogItemCityCb.setClickable(true);
            holder.dialogItemCityCb.setTextColor(mContext.getResources().getColor(R.color.text_color_white));
        }
    }


    @Override
    public int getItemCount() {
        if (cityList != null)
            return cityList.size();
        else
            return 0;
    }

    public class LocationHodler extends RecyclerView.ViewHolder {
        @Bind(R.id.dialog_item_city_cb)
        public CheckBox dialogItemCityCb;

        public LocationHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.dialog_item_city_cb)
        public void locationClick(View view) {
            cityList.get(selectedIndex).setIsSelected(false);
            cityList.get(getPosition()).setIsSelected(true);
            notifyDataSetChanged();
        }
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

}
