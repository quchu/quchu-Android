package co.quchu.quchu.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

import co.quchu.quchu.R;
import co.quchu.quchu.model.AreaBean;
import co.quchu.quchu.view.adapter.SearchFilterSortAdapter;

/**
 * Created by no21 on 2016/6/29.
 * email:437943145@qq.com
 * desc :
 */
public class AreaView extends RelativeLayout {

    private RecyclerView area;
    private RecyclerView row;

    private List<AreaBean> datas;

    public AreaView(Context context) {
        this(context, null);
    }

    public AreaView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void setDatas(List<AreaBean> datas) {
        this.datas = datas;
        area = (RecyclerView) findViewById(R.id.area_area);
        row = (RecyclerView) findViewById(R.id.area_row);

        area.setHasFixedSize(true);
        row.setHasFixedSize(true);

        area.setLayoutManager(new LinearLayoutManager(getContext()));
        row.setLayoutManager(new LinearLayoutManager(getContext()));

        area.setAdapter(new SearchFilterSortAdapter());
        row.setAdapter(new SearchFilterSortAdapter());
    }

    public AreaView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    static class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.ViewHolder> {
        private List<AreaBean> areaBeen;
        private List<AreaBean.CircleListBean> rowBean;

        private boolean isArea = true;

        public AreaAdapter(List<AreaBean> areaBeen) {
            this.areaBeen = areaBeen;
        }

        public boolean isArea() {
            return isArea;
        }

        public List<AreaBean.CircleListBean> getRowBean() {
            return rowBean;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return isArea ? areaBeen == null ? 0 : areaBeen.size() : rowBean == null ? 0 : areaBeen.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

}
