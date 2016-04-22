package co.quchu.quchu.view.adapter;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.utils.FlyMeUtils;
import co.quchu.quchu.widget.TagCloudView;

/**
 * RecommendAdapter
 * User: Chenhs
 * Date: 2015-12-08
 * 趣处推荐 适配器 adapter
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.RecommendHolder> {


    private AppCompatActivity mContext;
    private boolean isFlyme = false;
    private ArrayList<RecommendModel> arrayList;
    private RecommendHolder holder;
    RecommendModel model;

    public SearchAdapter(AppCompatActivity mContext) {
        this.mContext = mContext;
        isFlyme = FlyMeUtils.isFlyme();

    }

    public void changeDataSet(ArrayList<RecommendModel> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public RecommendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        holder = new RecommendHolder(LayoutInflater.from(mContext).inflate(R.layout.item_quchu_favorite, parent, false));
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @Override
    public void onBindViewHolder(RecommendHolder holder, final int position) {
        this.holder = holder;
        model = arrayList.get(position);
        holder.tvName.setText(model.getName());
        List<String> strTags = new ArrayList<>();
        List<RecommendModel.TagsEntity> tags = model.getTags();
        if (null!=tags && tags.size()>0){
            for (int i = 0; i < tags.size(); i++) {
                strTags.add(tags.get(i).getZh());
            }
        }
        holder.tcvTag.setTags(strTags);
        holder.tvAddress.setText(model.getAddress());
        holder.sdvImage.setImageURI(Uri.parse(model.getCover()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null!=mListener){
                    mListener.onClick(position);
                }
            }
        });

    }

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onClick(int position);
    }


    @Override
    public int getItemCount() {
        if (arrayList == null)
            return 0;
        else
            return arrayList.size();
    }

    class RecommendHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.name)
        TextView tvName;
        @Bind(R.id.tag)
        TagCloudView tcvTag;
        @Bind(R.id.address)
        TextView tvAddress;
        @Bind(R.id.simpleDraweeView)
        SimpleDraweeView sdvImage;

        public RecommendHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }
}
