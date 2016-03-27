package co.quchu.quchu.view.adapter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.dialog.ShareDialogFg;
import co.quchu.quchu.model.RecommendModel;
import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;
import co.quchu.quchu.utils.FlyMeUtils;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.activity.QuchuDetailsActivity;
import co.quchu.quchu.widget.HorizontalNumProgressBar;
import co.quchu.quchu.widget.TagCloudView;
import co.quchu.quchu.widget.ratingbar.ProperRatingBar;

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
        holder = new RecommendHolder(LayoutInflater.from(mContext).inflate(R.layout.item_search_cardview, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecommendHolder holder, int position) {
        this.holder = holder;
        model = arrayList.get(position);
        if (!StringUtils.isEmpty(model.getRgb())) {
            holder.rootCv.setCardBackgroundColor(Color.parseColor("#" + model.getRgb()));
        } else {
            holder.rootCv.setCardBackgroundColor(Color.parseColor("#808080"));
        }
        holder.itemRecommendCardPhotoSdv.setImageURI(Uri.parse(model.getCover()));
        if (isFlyme) {
            holder.itemRecommendCardPhotoSdv.setAspectRatio(1.45f);
        } else {
            holder.itemRecommendCardPhotoSdv.setAspectRatio(1.33f);
        }
        holder.itemRecommendCardAddressTv.setText(model.getAddress());
        holder.itemRecommendCardCityTv.setText(model.getDescribe());
        holder.itemRecommendCardNameTv.setText(model.getName());
        holder.itemRecommendCardPrb.setRating((int) (model.getSuggest() + 0.5) >= 5 ? 5 : ((int) (model.getSuggest())));

        holder.itemRecommendCardProgressOne.setProgress(model.getGenes().get(0).getValue());
        holder.itemRecommendCardProgressOne.setProgressName(model.getGenes().get(0).getKey());
        holder.itemRecommendCardProgressTwo.setProgress(model.getGenes().get(1).getValue());
        holder.itemRecommendCardProgressTwo.setProgressName(model.getGenes().get(1).getKey());
        holder.itemRecommendCardProgressThree.setProgress(model.getGenes().get(2).getValue());
        holder.itemRecommendCardProgressThree.setProgressName(model.getGenes().get(2).getKey());

        if (model.isIsf()) {
            holder.itemRecommendCardCollectIv.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_detail_collect));
        } else {
            holder.itemRecommendCardCollectIv.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_detail_uncollect));
        }


        if (null != model.getTags() && model.getTags().size() > 0) {
            ArrayList<String> tags = new ArrayList<String>();
            for (int i = 0; i < model.getTags().size(); i++) {
                tags.add(model.getTags().get(i).getZh());
            }
            holder.detailStoreTagcloundTcv.setVisibility(View.VISIBLE);
            holder.detailStoreTagcloundTcv.setTags(tags);
        } else {
            holder.detailStoreTagcloundTcv.setVisibility(View.INVISIBLE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PackageManager.PERMISSION_DENIED == mContext.
                    checkSelfPermission(Manifest.permission_group.LOCATION)) {

                holder.item_recommend_card_distance_tv.setVisibility(View.GONE);
            } else {
                int distance = (int) AMapUtils.calculateLineDistance(new LatLng(model.getLatitude(), model.getLongitude()),
                        new LatLng(SPUtils.getLatitude(), SPUtils.getLongitude()));
                //            StringUtils.formatDouble(Double.parseDouble(model.getDistance())) + "km";
                holder.item_recommend_card_distance_tv.setText("距您:" + distance / 1000 + "km");
            }
        } else {
            int distance = (int) AMapUtils.calculateLineDistance(new LatLng(model.getLatitude(), model.getLongitude()),
                    new LatLng(SPUtils.getLatitude(), SPUtils.getLongitude()));
            //            StringUtils.formatDouble(Double.parseDouble(model.getDistance())) + "km";
            holder.item_recommend_card_distance_tv.setText("距您:" + distance / 1000 + "km");
            //            StringUtils.alterBoldTextColor(holder.item_recommend_card_distance_tv, 2, 2
            //                    + distance.length(), R.color.white);
        }

    }


    private void setFavorite(final int position) {
        String favoUrl = "";
        if (arrayList.get(position).isIsf()) {
            favoUrl = String.format(NetApi.userDelFavorite, arrayList.get(position).getPid(), NetApi.FavTypePlace);
        } else {
            favoUrl = String.format(NetApi.userFavorite, arrayList.get(position).getPid(), NetApi.FavTypePlace);
        }
        NetService.get(mContext, favoUrl, new IRequestListener() {
            @Override
            public void onSuccess(JSONObject response) {
                if (arrayList.get(position).isIsf()) {
                    Toast.makeText(mContext, "取消收藏!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "收藏成功!", Toast.LENGTH_SHORT).show();
                }
                arrayList.get(position).setIsf(!arrayList.get(position).isIsf());
                notifyDataSetChanged();
            }

            @Override
            public boolean onError(String error) {

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (arrayList == null)
            return 0;
        else
            return arrayList.size();
    }

    class RecommendHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_recommend_card_collect_iv)
        ImageView itemRecommendCardCollectIv;
        @Bind(R.id.item_recommend_card_name_tv)
        TextView itemRecommendCardNameTv;
        @Bind(R.id.item_recommend_card_city_tv)
        TextView itemRecommendCardCityTv;
        @Bind(R.id.item_recommend_card_photo_sdv)
        SimpleDraweeView itemRecommendCardPhotoSdv;
        @Bind(R.id.item_recommend_card_prb)
        ProperRatingBar itemRecommendCardPrb;
        @Bind(R.id.item_recommend_card_address_tv)
        TextView itemRecommendCardAddressTv;
//        @Bind(R.id.item_recommend_card_collect_rl)
//        RelativeLayout itemRecommendCardCollectRl;
//        @Bind(R.id.item_recommend_card_interest_rl)
//        RelativeLayout itemRecommendCardInterestRl;
//        @Bind(R.id.item_recommend_card_reply_rl)
//        RelativeLayout itemRecommendCardReplyRl;

        @Bind(R.id.item_recommend_card_progress_one)
        HorizontalNumProgressBar itemRecommendCardProgressOne;
        @Bind(R.id.item_recommend_card_progress_two)
        HorizontalNumProgressBar itemRecommendCardProgressTwo;
        @Bind(R.id.item_recommend_card_progress_three)
        HorizontalNumProgressBar itemRecommendCardProgressThree;
        @Bind(R.id.root_cv)
        CardView rootCv;
        @Bind(R.id.detail_store_tagclound_tcv)
        TagCloudView detailStoreTagcloundTcv;
        @Bind(R.id.item_recommend_card_distance_tv)
        TextView item_recommend_card_distance_tv;

        public RecommendHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.root_cv, R.id.item_recommend_card_collect_rl, R.id.item_recommend_card_reply_rl, R.id.item_recommend_card_interest_rl})
        public void searchClick(View v) {
            if (KeyboardUtils.isFastDoubleClick())
                return;
            switch (v.getId()) {
                case R.id.root_cv:
                case R.id.item_recommend_card_reply_rl:
                    mContext.startActivity(new Intent(mContext, QuchuDetailsActivity.class).putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, arrayList.get(getPosition()).getPid()));
                    break;
                case R.id.item_recommend_card_collect_rl:
                    setFavorite(getPosition());
                    break;
                case R.id.item_recommend_card_interest_rl:
                    ShareDialogFg shareDialogFg = ShareDialogFg.newInstance(arrayList.get(getPosition()).getPid(), arrayList.get(getPosition()).getName(), true);
                    shareDialogFg.show(mContext.getFragmentManager(), "share_dialog");
                    break;
            }
        }
    }
}
