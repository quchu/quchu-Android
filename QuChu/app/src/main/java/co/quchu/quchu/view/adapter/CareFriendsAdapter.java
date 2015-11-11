package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.widget.RoundProgressBar;

/**
 * CareFriendsAdapter
 * User: Chenhs
 * Date: 2015-11-11
 */
public class CareFriendsAdapter extends RecyclerView.Adapter<CareFriendsAdapter.CareFriendsHolder> {

    private Context mContext;

    public CareFriendsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public CareFriendsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CareFriendsHolder careFriendsHolder = new CareFriendsHolder(LayoutInflater.from(mContext).inflate(R.layout.item_care_friends, parent, false));
        return careFriendsHolder;
    }

    @Override
    public void onBindViewHolder(CareFriendsHolder holder, int position) {
        if (position==0){
            holder.itemCareFriendsBodyRl.setVisibility(View.GONE);
            holder.itemCareFriendsSubtitleRl.setVisibility(View.VISIBLE);
            holder.careAboutSubtitleTv.setText(mContext.getResources().getString(R.string.subtitle_word_care_about_friends_introduce));
            StringUtils.setTextHighlighting( holder.careAboutSubtitleTv, 5, 8);
        }else {
            holder.itemCareFriendsBodyRl.setVisibility(View.VISIBLE);
            holder.itemCareFriendsNameTv.setText("name" + position);
            holder.itemCareFriendsAddressTv.setText("address" + position);
            holder.itemCareFriendsRpb.setProgressText("0" + (position ));
            holder.itemCareFriendsIconSdv.setImageURI(Uri.parse("http://e.hiphotos.baidu.com/image/pic/item/dcc451da81cb39db026e7657d2160924ab183000.jpg"));
            holder.itemCareFriendsRpb.setProgress(90 - (position * 8));
        }

    }

    @Override
    public int getItemCount() {
        return 9;
    }

    class CareFriendsHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.care_about_subtitle_tv)
        TextView careAboutSubtitleTv;
        @Bind(R.id.item_care_friends_subtitle_rl)
        RelativeLayout itemCareFriendsSubtitleRl;
        @Bind(R.id.item_care_friends_icon_sdv)
        SimpleDraweeView itemCareFriendsIconSdv;
        @Bind(R.id.item_care_friends_name_tv)
        TextView itemCareFriendsNameTv;
        @Bind(R.id.item_care_friends_address_tv)
        TextView itemCareFriendsAddressTv;
        @Bind(R.id.item_care_friends_rpb)
        RoundProgressBar itemCareFriendsRpb;
        @Bind(R.id.item_care_friends_body_rl)
        RelativeLayout itemCareFriendsBodyRl;
        @Bind(R.id.item_friends_root_rl)
        RelativeLayout itemFriendsRootRl;

        public CareFriendsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }



}
