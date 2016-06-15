package co.quchu.quchu.view.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.PostCardItemModel;
import co.quchu.quchu.utils.DateUtils;

/**
 * Created by no21 on 2016/4/7.
 * email:437943145@qq.com
 * desc :
 */
public class MyFootprintAdapter extends AdapterBase<PostCardItemModel, MyFootprintAdapter.ViewHold> {

    public static final int CLICK_TYPE_IMAGE = 2;
    public static final int CLICK_TYPE_NAME = 1;

    @Override
    public void onBindView(final ViewHold holder, final int position) {
        final PostCardItemModel model = data.get(position);

//        float ratio = (float) model.getWidth() / model.getHeight();
//        holder.simpleDraweeView.setAspectRatio(ratio);

        holder.simpleDraweeView.setImageURI(Uri.parse(model.getPlcaeCover()));
//        holder.headView.setImageURI(Uri.parse(model.getAutorPhoto()));

//        StringBuilder text1 = new StringBuilder();
//        text1.append(model.getAutor());
//        text1.append(":");
//        int index1 = text1.length();
//        text1.append(" 在 ");
//        int index2 = text1.length();
//        text1.append(model.getPlcaeName());
//        int index3 = text1.length();
//
//        SpannableStringBuilder builder = new SpannableStringBuilder(text1.toString());
//
//        builder.setSpan(new ForegroundColorSpan(Color.argb(255, 255, 255, 255)), 0, index1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//        builder.setSpan(new ForegroundColorSpan(Color.parseColor("#838181")), index1, index2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//        builder.setSpan(new ForegroundColorSpan(Color.argb(255, 255, 255, 255)), index2, index3, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        holder.name.setText(model.getComment());
        holder.address.setText(model.getPlcaeName());

//        StringBuilder text2 = new StringBuilder();
//        text2.append(model.getTime());
//        text2.append(" 来至 ");
//        text2.append(model.getPlcaeAddress());
//
//        SpannableStringBuilder builder2 = new SpannableStringBuilder(text2.toString());
//        builder2.setSpan(new ForegroundColorSpan(Color.parseColor("#838181")), 0, text2.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        if (model.getPlaceId() == 0) {
            holder.commemt.setVisibility(View.INVISIBLE);
        } else {
            holder.commemt.setVisibility(View.VISIBLE);
        }
        holder.commemt.setText(model.getPraiseNum() + "次");
        holder.time.setText(DateUtils.getDateToString(DateUtils.DATA_FORMAT_MM_DD_YYYY, model.getTime()));

        if (itemClickListener != null) {
            holder.simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.itemClick(holder, model, CLICK_TYPE_IMAGE, position);
                }
            });
            holder.address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.itemClick(holder, model, CLICK_TYPE_NAME, position);

                }
            });
        }

    }

    @Override
    public ViewHold onCreateView(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_footprint, parent, false);

        return new ViewHold(view);
    }

    static class ViewHold extends RecyclerView.ViewHolder {
        @Bind(R.id.simpleDraweeView)
        SimpleDraweeView simpleDraweeView;
        @Bind(R.id.desc)
        TextView name;
        @Bind(R.id.commemt)
        TextView commemt;

        @Bind(R.id.name)
        TextView address;
        @Bind(R.id.time)
        TextView time;

        public ViewHold(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public interface OnItemClickListener {
        void itemClick(PostCardItemModel bean);
    }
}
