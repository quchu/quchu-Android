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
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.utils.KeyboardUtils;

/**
 * FriendsAdatper
 * User: Chenhs
 * Date: 2015-11-09
 */
public class FriendsAdatper extends RecyclerView.Adapter<FriendsAdatper.FriendsViewHolder> {

    private Context mContext;
    private FriendsItemClickListener clickListener;

    public FriendsAdatper(Context mContext) {
        this.mContext = mContext;
    }

    public FriendsAdatper(Context mContext, FriendsItemClickListener clickListener) {
        this.mContext = mContext;
        this.clickListener = clickListener;
    }

    @Override
    public FriendsAdatper.FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FriendsViewHolder friendsViewHolder = new FriendsViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_friends, parent, false), clickListener);
        return friendsViewHolder;
    }

    @Override
    public void onBindViewHolder(FriendsViewHolder holder, int position) {
        holder.itemFirendsIconSdv.setImageURI(Uri.parse("http://imgdn.paimeilv.com/1444721523235"));
        holder.itemFriendsNameTv.setText("User Name" + position);
        holder.itemFirendsAddressTv.setText(String.format("%1s , %2s","女","福建-厦门"));
    }

    @Override
    public int getItemCount() {
        return 22;
    }

    class FriendsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.item_firends_icon_sdv)
        SimpleDraweeView itemFirendsIconSdv;
        @Bind(R.id.item_friends_name_tv)
        TextView itemFriendsNameTv;
        @Bind(R.id.item_firends_address_tv)
        TextView itemFirendsAddressTv;
        @Bind(R.id.item_friends_root_rl)
        RelativeLayout itemFriendsRootRl;
        private FriendsItemClickListener clickListener;

        public FriendsViewHolder(View itemView, FriendsItemClickListener clickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.clickListener = clickListener;
        }

        @OnClick(R.id.item_friends_root_rl)
        @Override
        public void onClick(View v) {
            if (KeyboardUtils.isFastDoubleClick())
                return;
            if (clickListener != null)
                clickListener.itemClick(v, getAdapterPosition());
        }
    }

    interface FriendsItemClickListener {
        void itemClick(View view, int position);
    }

}
