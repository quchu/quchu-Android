package co.quchu.quchu.view.adapter;

import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.PostCardItemModel;

/**
 * Created by no21 on 2016/4/7.
 * email:437943145@qq.com
 * desc :
 */
public class MyFootprintAdapter extends RecyclerView.Adapter<MyFootprintAdapter.ViewHold> {

    private List<PostCardItemModel> data;
    private OnItemClickListener listener;

    public MyFootprintAdapter(List<PostCardItemModel> data, OnItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_footprint, parent, false);

        return new ViewHold(view);
    }

    @Override
    public void onBindViewHolder(final ViewHold holder, int position) {

        final PostCardItemModel model = data.get(position);

        float ratio = (float) model.getWidth() / model.getHeight();
        holder.simpleDraweeView.setAspectRatio(ratio);

        holder.simpleDraweeView.setImageURI(Uri.parse(model.getPlcaeCover()));
        holder.headView.setImageURI(Uri.parse(model.getAutorPhoto()));

        StringBuilder text1 = new StringBuilder();
        text1.append(model.getAutor());
        text1.append(":");
        int index1 = text1.length();
        text1.append(" 在 ");
        int index2 = text1.length();
        text1.append(model.getPlcaeName());
        int index3 = text1.length();

        SpannableStringBuilder builder = new SpannableStringBuilder(text1.toString());

        builder.setSpan(new ForegroundColorSpan(Color.argb(255, 255, 255, 255)), 0, index1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(Color.parseColor("#838181")), index1, index2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(Color.argb(255, 255, 255, 255)), index2, index3, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        holder.name.setText(builder);


        StringBuilder text2 = new StringBuilder();
        text2.append(model.getTime());
        text2.append(" 来至 ");
        text2.append(model.getPlcaeAddress());

        SpannableStringBuilder builder2 = new SpannableStringBuilder(text2.toString());
        builder2.setSpan(new ForegroundColorSpan(Color.parseColor("#838181")), 0, text2.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        holder.timeAndAddress.setText(builder2);


        holder.simpleDraweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.itemClick(model);
            }
        });

//        holder.itemView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                holder.line.getLayoutParams().height = holder.itemView.getHeight();
//                holder.itemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    static class ViewHold extends RecyclerView.ViewHolder {
        @Bind(R.id.simpleDraweeView)
        SimpleDraweeView simpleDraweeView;
        @Bind(R.id.cardView)
        CardView cardView;
        @Bind(R.id.headView)
        SimpleDraweeView headView;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.timeAndAddress)
        TextView timeAndAddress;
        @Bind(R.id.containerNone)
        RelativeLayout containerNone;
        @Bind(R.id.line)
        View line;

        static int itemHeight;

        public ViewHold(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public interface OnItemClickListener {
        void itemClick(PostCardItemModel bean);
    }
}
