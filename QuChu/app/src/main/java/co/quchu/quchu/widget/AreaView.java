package co.quchu.quchu.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.AreaBean;

/**
 * Created by no21 on 2016/6/29.
 * email:437943145@qq.com
 * desc :
 */
public class AreaView extends LinearLayout {


    public AreaView(Context context) {
        this(context, null);
    }

    public AreaView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AreaView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private List<AreaBean> datas;
    private OnAreaSelected areaSelectedListener;

    public void setAreaSelectedListener(OnAreaSelected areaSelectedListener) {
        this.areaSelectedListener = areaSelectedListener;
    }

    public void setDatas(final List<AreaBean> datas) {
        if (this.datas == null) {
            this.datas = datas;
            final AreaBean.CircleListBean circleListBeanAll = new AreaBean.CircleListBean();
            circleListBeanAll.setCircleId("");
            circleListBeanAll.setCircleName("全部");

            for (AreaBean item : datas) {
                item.getCircleList().add(0, circleListBeanAll);
            }

            final AreaBean areaBeanAll = new AreaBean();
            areaBeanAll.setAreaId("");
            areaBeanAll.setAreaName("全部商圈");
            datas.add(0, areaBeanAll);

            final RecyclerView area = (RecyclerView) findViewById(R.id.area_area);
            final RecyclerView row = (RecyclerView) findViewById(R.id.area_row);
            area.setHasFixedSize(true);
            row.setHasFixedSize(true);

            area.setLayoutManager(new LinearLayoutManager(getContext()){
                @Override
                public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                    super.onLayoutChildren(recycler, state);
                    row.getLayoutParams().height = area.getHeight();
                }
            });
            row.setLayoutManager(new LinearLayoutManager(getContext()));

            final AreaAdapter areaAdapter = new AreaAdapter();
            areaAdapter.setAreaBeen(datas);

            final AreaAdapter rowAdapter = new AreaAdapter();
            rowAdapter.setRowBean(datas.get(0).getCircleList());
            rowAdapter.setArea(false);


            areaAdapter.setItemClickListener(new OnItemClickListener() {
                @Override
                public void itemClick(int position) {
                    rowAdapter.setRowBean(datas.get(position).getCircleList());
                    rowAdapter.setSelectedPosition(-1);
                    rowAdapter.notifyDataSetChanged();
                    if (position == 0) {
                        areaSelectedListener.areaSelected(areaBeanAll, circleListBeanAll);
                    }
                }
            });
            rowAdapter.setItemClickListener(new OnItemClickListener() {
                @Override
                public void itemClick(int position) {
                    if (areaSelectedListener != null) {

                        AreaBean areaBean = datas.get(areaAdapter.getSelectedPosition());
                        AreaBean.CircleListBean circleListBean = areaBean.getCircleList().get(rowAdapter.getSelectedPosition());
                        areaSelectedListener.areaSelected(areaBean, circleListBean);
                    }
                }
            });

            areaAdapter.displayDivider(false);
            area.setAdapter(areaAdapter);

            row.setAdapter(rowAdapter);
        }
    }


    static class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.ViewHolder> {
        private List<AreaBean> areaBeen;
        private List<AreaBean.CircleListBean> rowBean;
        private OnItemClickListener itemClickListener;
        private boolean isArea = true;


        boolean displayDivider = true;


        public void displayDivider(boolean toggle){
            displayDivider = toggle;
            notifyDataSetChanged();
        }


        private int selectedPosition = 0;

        public void setSelectedPosition(int selectedPosition) {
            this.selectedPosition = selectedPosition;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public void setArea(boolean area) {
            isArea = area;
        }

        public void setItemClickListener(OnItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public void setAreaBeen(List<AreaBean> areaBeen) {
            this.areaBeen = areaBeen;
        }

        public void setRowBean(List<AreaBean.CircleListBean> rowBean) {
            this.rowBean = rowBean;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_pop_win, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            if (isArea) {
                AreaBean areaBean = areaBeen.get(position);
                holder.searchPopItemTv.setText(areaBean.getAreaName());
            } else {
                AreaBean.CircleListBean circleListBean = rowBean.get(position);
                holder.searchPopItemTv.setText(circleListBean.getCircleName());
            }


            holder.vDivider.setVisibility(displayDivider ?View.VISIBLE:View.GONE);


            if (displayDivider){
                if (selectedPosition == position) {
                    holder.searchPopItemTv.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.standard_color_yellow));
                    holder.searchPopItemIv.setVisibility(VISIBLE);
                } else {
                    holder.searchPopItemContent.setBackground(null);
                    holder.searchPopItemTv.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.standard_color_h2_dark));
                    holder.searchPopItemIv.setVisibility(GONE);
                }

            }else{
                holder.searchPopItemIv.setVisibility(View.GONE);
                if (selectedPosition == position) {
                    holder.searchPopItemContent.setBackgroundColor(holder.searchPopItemContent.getContext().getResources().getColor(R.color.standard_color_white));
                }else{
                    holder.searchPopItemContent.setBackgroundColor(holder.searchPopItemContent.getContext().getResources().getColor(R.color.colorBackground));
                }
            }



            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    selectedPosition = holder.getAdapterPosition();
                    itemClickListener.itemClick(holder.getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.search_pop_item_iv)
            ImageView searchPopItemIv;
            @Bind(R.id.search_pop_item_tv)
            TextView searchPopItemTv;
            @Bind(R.id.search_pop_item_content)
            RelativeLayout searchPopItemContent;
            @Bind(R.id.vDivider)
            View vDivider;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        @Override
        public int getItemCount() {
            return isArea ? areaBeen == null ? 0 : areaBeen.size() : rowBean == null ? 0 : rowBean.size();
        }
    }

    public interface OnItemClickListener {

        void itemClick(int position);
    }

    public interface OnAreaSelected {
        void areaSelected(AreaBean areaBean, AreaBean.CircleListBean circleListBean);
    }
}
