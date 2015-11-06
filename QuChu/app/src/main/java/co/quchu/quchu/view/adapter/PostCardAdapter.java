package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;

/**
 * PostCardAdapter
 * User: Chenhs
 * Date: 2015-11-03
 */
public class PostCardAdapter extends RecyclerView.Adapter<PostCardAdapter.SimpleViewHolder> {
    private static final int COUNT = 10;

    private final Context mContext;
    //    private final RecyclerView mRecyclerView;
    private final List<Integer> mItems;

    private int mCurrentItemId = 0;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.my_image_view)
        SimpleDraweeView myImageView;
        @Bind(R.id.root_cv)
        CardView rootCv;
        public SimpleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public PostCardAdapter(Context context) {
        mContext = context;
        mItems = new ArrayList<>(COUNT);
        for (int i = 0; i < COUNT; i++) {
            addItem(i);
        }

    }

    public void addItem(int position) {
        final int id = mCurrentItemId++;
        mItems.add(position, id);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        final View view =;
        SimpleViewHolder simpleViewHolder = new SimpleViewHolder( LayoutInflater.from(mContext).inflate(R.layout.item_postcard, parent, false));
        return simpleViewHolder;
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {

        final View itemView = holder.itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
            }
        });

        DraweeController d = holder.myImageView.getController();

        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse("http://g.hiphotos.baidu.com/image/pic/item/f636afc379310a55ed3771f5b54543a98226109b.jpg"))
                .setProgressiveRenderingEnabled(true)
                .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(d)
                .build();
        holder.myImageView.setAspectRatio(1.33f);
        holder.myImageView.setController(controller);

//        holder.myImageView.setImageURI();
        final int itemId = mItems.get(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
