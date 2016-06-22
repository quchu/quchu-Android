package co.quchu.quchu.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.widget.TagCloudView;

/**
 * RecommendAdapter
 * User: Chenhs
 * Date: 2015-12-08
 * 趣处推荐 适配器 adapter
 */
public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<RecommendModel> arrayList;
    RecommendModel model;

    public static final int ITEM_TYPE_CATEGORY = -1;
    public static final int ITEM_TYPE_RESULT = -2;


    private boolean isCategory = true;

    public void setCategory(boolean category) {
        isCategory = category;
    }

    @Override
    public int getItemViewType(int position) {
        return isCategory ? ITEM_TYPE_CATEGORY : ITEM_TYPE_RESULT;
    }

    public void changeDataSet(ArrayList<RecommendModel> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_RESULT) {
            return new ResultHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false));
        }
        return new CategoryViewHold(View.inflate(parent.getContext(), R.layout.item_search_category, null));
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holde, final int position) {
//        if (getItemViewType(position) == ITEM_TYPE_RESULT) {
//            ResultHolder holder = (ResultHolder) holde;
//
//            model = arrayList.get(position);
//            holder.tvName.setText(model.getName());
//            List<String> strTags = new ArrayList<>();
//            List<RecommendModel.TagsEntity> tags = model.getTags();
//            if (null != tags && tags.size() > 0) {
//                for (int i = 0; i < tags.size(); i++) {
//                    strTags.add(tags.get(i).getZh());
//                }
//            }
//            holder.tcvTag.setTags(strTags);
//            holder.sdvImage.setImageURI(Uri.parse(model.getCover()));
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (null != mListener) {
//                        mListener.onClick(position);
//                    }
//                }
//            });
//        }else {
//            CategoryViewHold holder = (CategoryViewHold) holde;
//
//        }

    }

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onClick(int position);
    }


    @Override
    public int getItemCount() {
        return 20;
//        return arrayList == null ? 0 : arrayList.size();
    }

    class ResultHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.desc)
        TextView tvName;
        @Bind(R.id.tag)
        TagCloudView tcvTag;
        @Bind(R.id.simpleDraweeView)
        SimpleDraweeView sdvImage;

        public ResultHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class CategoryViewHold extends RecyclerView.ViewHolder {


        public CategoryViewHold(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
