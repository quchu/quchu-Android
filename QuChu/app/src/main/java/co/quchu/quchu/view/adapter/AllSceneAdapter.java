package co.quchu.quchu.view.adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.lang.reflect.Field;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.SceneModel;
import co.quchu.quchu.view.fragment.RecommendFragment;

/**
 * AllSceneAdapter
 * User: Chenhs
 * Date: 2015-12-08
 * 趣处推荐 适配器 adapter
 */
public class AllSceneAdapter extends PagerAdapter {


    private Activity mContext;
    private List<SceneModel> dataSet;
    private CardClickListener listener;
    private RecommendFragment fragment;

    public AllSceneAdapter(RecommendFragment fragment, List<SceneModel> arrayList, CardClickListener listener) {
        this.mContext = fragment.getActivity();
        this.fragment = fragment;
        dataSet = arrayList;
        this.listener = listener;
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        RecommendHolder holder;
        if (container.getTag() == null) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_recommend_cardview_new_miui, container, false);

            holder = new RecommendHolder(view);
        } else {
            holder = (RecommendHolder) container.getTag();
            container.setTag(null);
        }
        if (position != 0 && position != fragment.getViewpager().getCurrentItem()) {
            holder.itemView.setScaleY(RecommendFragment.MIN_SCALE);
        } else {
            holder.itemView.setScaleY(1);
        }

        SceneModel model = dataSet.get(position);
        if (!TextUtils.isEmpty(model.getSceneCover()))
            holder.itemRecommendCardPhotoSdv.setImageURI(Uri.parse(model.getSceneCover()));

        holder.item_recommend_card_name_tv.setText(model.getSceneName());

        if (null != model.getSceneTitle() && model.getSceneTitle().length > 0) {
            for (int i = 0; i < model.getSceneTitle().length; i++) {
                switch (i) {
                    case 0:
                        holder.tag1.setText(model.getSceneTitle()[i]);
                        holder.tag1.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        holder.tag2.setText(model.getSceneTitle()[i]);
                        holder.tag2.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        holder.tag3.setText(model.getSceneTitle()[i]);
                        holder.tag3.setVisibility(View.VISIBLE);
                        break;
                }
            }
        } else {
            holder.tag1.setVisibility(View.GONE);
            holder.tag2.setVisibility(View.GONE);
            holder.tag3.setVisibility(View.GONE);
        }
//        if (0 == SPUtils.getLatitude() || 0 == SPUtils.getLongitude()) {
//            holder.item_recommend_card_distance_tv.setVisibility(View.GONE);
//        } else {
//            String distance = StringUtils.getDistance(model.getLatitude(), model.getLongitude(), SPUtils.getLatitude(), SPUtils.getLongitude());
//            String text = "距您当前位置" + distance;
//
//            SpannableStringBuilder builder = new SpannableStringBuilder();
//            builder.append(text);
//            ForegroundColorSpan span_1 = new ForegroundColorSpan(mContext.getResources().getColor(R.color.standard_color_red));
//            builder.setSpan(span_1, 6, text.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//            holder.item_recommend_card_distance_tv.setText(builder);
//
//        }
        container.addView(holder.itemView);

        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCardLick(v, position);

                }
            });
        }

        return holder;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(((RecommendHolder) object).itemView);
        container.setTag(object);
    }

    @Override
    public int getCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    @Override
    public int getItemPosition(Object object) {

        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RecommendHolder) object).itemView;
    }

    class RecommendHolder {
        @Bind(R.id.photo)
        SimpleDraweeView itemRecommendCardPhotoSdv;
        //        @Bind(R.id.item_recommend_card_address_tv)
//        TextView itemRecommendCardAddressTv;
        @Bind(R.id.activity)
        SimpleDraweeView item_place_event_tv;
        @Bind(R.id.desc)
        TextView item_recommend_card_name_tv;
        @Bind(R.id.recommend_tag1)
        TextView tag1;
        @Bind(R.id.recommend_tag2)
        TextView tag2;
        @Bind(R.id.recommend_tag3)
        TextView tag3;
//        @Bind(R.id.rbRating)
//        RatingBar rbRating;

        //        @Bind(R.id.distance)
//        TextView item_recommend_card_distance_tv;
        View itemView;

        public RecommendHolder(View itemView) {
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;
            tag1.setVisibility(View.GONE);
            tag2.setVisibility(View.GONE);
            tag3.setVisibility(View.GONE);
        }
    }

    private static long lastClickTime = 0L;

    /**
     * 防止重复点击
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public interface CardClickListener {
        void onCardLick(View view, int position);
    }

    @Override
    public void notifyDataSetChanged() {
        try {
            Field field = fragment.getViewpager().getClass().getDeclaredField("mPageTransformer");
            field.setAccessible(true);
            field.set(fragment.getViewpager(), null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("release version error");
        }
        super.notifyDataSetChanged();
        fragment.getViewpager().setPageTransformer(false, fragment);
    }

}
