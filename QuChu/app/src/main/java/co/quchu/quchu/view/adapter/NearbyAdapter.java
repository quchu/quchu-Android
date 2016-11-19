package co.quchu.quchu.view.adapter;

import android.net.Uri;
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
import co.quchu.quchu.model.NearbyItemModel;
import co.quchu.quchu.widget.TagCloudView;

/**
 * Created by Nico on 16/4/11.
 */
public class NearbyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_NORMAL = 0x001;
    public static final int TYPE_PAGE_END = 0x002;
    private List<NearbyItemModel> mData;
    private OnItemClickListener mListener;
    private boolean mShowingNoData = false;



    @Override public int getItemCount() {
        return null != mData ? mData.size() + (mShowingNoData ? 1 : 0)+1 : 0;
    }

    @Override public int getItemViewType(int position) {
        if (position < mData.size()+1) {
            return TYPE_NORMAL;
        } else {
            return TYPE_PAGE_END;
        }
    }

    public void showPageEnd(boolean bl) {
        if (null!=mData&&mData.size()>0){
            mShowingNoData = bl;
            notifyDataSetChanged();
        }
    }
    public NearbyAdapter(List<NearbyItemModel> pData, OnItemClickListener pListener) {
        mData = pData;
        mListener = pListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_NORMAL:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nearby_quchu_detail, parent, false));
            default:
                return new PageEndViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cp_page_end, parent, false));
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
       if (holder instanceof NearbyAdapter.ViewHolder){
           ((ViewHolder)holder).name.setText(mData.get(position).getName());
           ((ViewHolder)holder).simpleDraweeView.setImageURI(Uri.parse(mData.get(position).getCover()));
           List<String> tags = new ArrayList<>();
           for (int i = 0; i < Math.min(mData.get(position).getTags().size(),3); i++) {
               tags.add(" "+mData.get(position).getTags().get(i).getZh()+" ");
           }
           ((ViewHolder)holder).tag.setTags(tags);
           ((ViewHolder)holder).tvAddress.setText(mData.get(position).getAddress());
           ((ViewHolder)holder).tvAddress.setVisibility(View.VISIBLE);
           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if (null != mListener) {
                       mListener.onClick(position);
                   }
               }
           });
       }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.desc)
        TextView name;
        @Bind(R.id.tag)
        TagCloudView tag;
        @Bind(R.id.simpleDraweeView)
        SimpleDraweeView simpleDraweeView;
        @Bind(R.id.address)
        TextView tvAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }
}
