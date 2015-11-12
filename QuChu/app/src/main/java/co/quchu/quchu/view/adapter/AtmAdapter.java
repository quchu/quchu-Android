package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
import com.facebook.imagepipeline.image.ImmutableQualityInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.model.AtmosphereItemModel;
import co.quchu.quchu.view.activity.AtmosphereActivity;
import co.quchu.quchu.widget.ratingbar.ProperRatingBar;

/**
 * AtmAdapter
 * User: Chenhs
 * Date: 2015-10-30
 * 氛围列表adapter
 */
public class AtmAdapter extends RecyclerView.Adapter<AtmAdapter.AtmHolder> {

    private Context activity;
    private ArrayList<AtmosphereItemModel> arrayList;
    private AtmItemClickListener listener;

    public AtmAdapter(Context atmosphereActivity, ArrayList<AtmosphereItemModel> arrayList) {
        activity = atmosphereActivity;
        this.arrayList = arrayList;
        ProgressiveJpegConfig pjpegConfig = new ProgressiveJpegConfig() {
            @Override
            public int getNextScanNumberToDecode(int scanNumber) {
                return scanNumber + 2;
            }

            public QualityInfo getQualityInfo(int scanNumber) {
                boolean isGoodEnough = (scanNumber >= 2);
                return ImmutableQualityInfo.of(scanNumber, isGoodEnough, false);
            }
        };
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(activity)
                .setProgressiveJpegConfig(pjpegConfig)
                .build();
        Fresco.initialize(activity, config);
    }


    public void AtmAdapter(AtmosphereActivity activity, ArrayList<AtmosphereItemModel> arrayList, AtmItemClickListener listener) {
        this.activity = activity;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    public void setOnitemClickListener(AtmItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public AtmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AtmHolder pvh = new AtmHolder((LayoutInflater.from(
                activity).inflate(R.layout.item_atmosphere_view, parent,
                false)), listener);
        return pvh;
    }

    @Override
    public void onBindViewHolder(AtmHolder holder, int position) {
        holder.rootCv.setCardBackgroundColor(Color.parseColor("#" + arrayList.get(position % 10).getRgb()));
        holder.atrmosphereItemTitleTv.setText(arrayList.get(position % 10).getName());
        holder.atrmosphereItemAddressTv.setText(arrayList.get(position % 10).getAddress());
        holder.atrmosphereItemRb.setRating(arrayList.get(position % 10).getTakeIndex());

        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(arrayList.get(position % 10).getCover()))
                .setProgressiveRenderingEnabled(true)
                .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(holder.atrmosphereItemImageIv.getController())
                .build();
        holder.atrmosphereItemImageIv.setController(controller);
//        holder.atrmosphereItemImageIv.setImageURI();

        holder.atrmosphereItemImageIv.setAspectRatio(1.33f);

    }

    @Override
    public int getItemCount() {
        return 60;
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_atmosphere_view.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    public class AtmHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.atrmosphere_item_title_tv)
        public TextView atrmosphereItemTitleTv;
        @Bind(R.id.atrmosphere_item_image_iv)
        public SimpleDraweeView atrmosphereItemImageIv;
        @Bind(R.id.atrmosphere_item_rb)
        public ProperRatingBar atrmosphereItemRb;
        @Bind(R.id.atrmosphere_item_address_tv)
        public TextView atrmosphereItemAddressTv;
        @Bind(R.id.atrmosphere_item_selected_rl)
        public RelativeLayout atrmosphereItemSelectedRl;
        @Bind(R.id.atrmosphere_item_share_rl)
        public RelativeLayout atrmosphereItemShareRl;
        @Bind(R.id.atrmosphere_item_more_rl)
        public RelativeLayout atrmosphereItemMoreRl;
        @Bind(R.id.root_cv)
        public CardView rootCv;
        private AtmItemClickListener listener;

        AtmHolder(View view, AtmItemClickListener listener) {
            super(view);
            ButterKnife.bind(this, view);
            this.listener = listener;
        }

        @OnClick({R.id.root_cv, R.id.atrmosphere_item_selected_rl, R.id.atrmosphere_item_share_rl, R.id.atrmosphere_item_more_rl})
        @Override
        public void onClick(View v) {
            listener.itemClickListener(v, getAdapterPosition());
        }
    }

    private int colorBurn(int RGBValues) {
//        int alpha = RGBValues >> 24;
        int red = RGBValues >> 16 & 0xFF;
        int green = RGBValues >> 16 & 0xFF;
        int blue = RGBValues >> 16 & 0xFF;
        red = (int) Math.floor(red * (1 - 0.2));
        green = (int) Math.floor(green * (1 - 0.2));
        blue = (int) Math.floor(blue * (1 - 0.2));
        return Color.rgb(red, green, blue);
    }

    public interface AtmItemClickListener {
        void itemClickListener(View v, int position);
    }
}
