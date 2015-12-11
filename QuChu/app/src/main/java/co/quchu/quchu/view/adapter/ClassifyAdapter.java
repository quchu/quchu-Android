package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.model.ClassifyModel;

/**
 * ClassifyAdapter
 * User: Chenhs
 * Date: 2015-12-08
 */
public class ClassifyAdapter extends RecyclerView.Adapter<ClassifyAdapter.ClassifyHolder> {

    private Context mContext;
    private ArrayList<ClassifyModel> arrayList;

    private ClasifyClickListener listener;

    public ClassifyAdapter(Context context, ArrayList<ClassifyModel> arrayList) {
        this.mContext = context;
        this.arrayList = arrayList;
    }

    @Override
    public ClassifyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ClassifyHolder holder = new ClassifyHolder(LayoutInflater.from(mContext).inflate(R.layout.item_classify_card, parent, false), listener);
        return holder;
    }

    public void setOnItemCliskListener(ClasifyClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(ClassifyHolder holder, int position) {
        holder.itemClassifyImageSdv.setImageURI(Uri.parse(arrayList.get(position).getMaxImg()));
        holder.itemClassifyImageSdv.setAspectRatio(1.73f);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ClassifyHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_classify_image_sdv)
        SimpleDraweeView itemClassifyImageSdv;
        @Bind(R.id.item_classify_root_cv)
        CardView itemClassifyRootCv;
        private ClasifyClickListener listener;

        public ClassifyHolder(View itemView, ClasifyClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
        }
        @OnClick(R.id.item_classify_root_cv)
        public void onCardClick(View v){
            switch (v.getId()){
                case R.id.item_classify_root_cv:
                    if (listener!=null)
                        listener.cItemClick(v,getAdapterPosition());
                    break;
            }
        }
    }

    public interface ClasifyClickListener {
        void cItemClick(View view, int position);
    }
}
