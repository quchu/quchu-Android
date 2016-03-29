package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.quchu.quchu.R;
import co.quchu.quchu.model.FollowUserModel;
import co.quchu.quchu.utils.KeyboardUtils;
import co.quchu.quchu.view.activity.UserCenterActivity;

/**
 * FriendsAdatper
 * User: Chenhs
 * Date: 2015-11-09
 */
public class FriendsAdatper extends RecyclerView.Adapter<FriendsAdatper.FriendsViewHolder> {

    private Context mContext;
    private FriendsItemClickListener clickListener;
    private boolean isInnerClick = true;
    private ArrayList<FollowUserModel> userList;

    public FriendsAdatper(Context mContext) {
        this.mContext = mContext;
    }

    public FriendsAdatper(Context mContext, FriendsItemClickListener clickListener) {
        this.mContext = mContext;
        this.clickListener = clickListener;
    }

    public FriendsAdatper(Context mContext, FriendsItemClickListener clickListener, ArrayList<FollowUserModel> userList) {
        this.mContext = mContext;
        this.clickListener = clickListener;
        this.userList = userList;
    }
    public FriendsAdatper(Context mContext, ArrayList<FollowUserModel> userList) {
        this.mContext = mContext;
        this.userList = userList;
    }

    @Override
    public FriendsAdatper.FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FriendsViewHolder friendsViewHolder = new FriendsViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_friends, parent, false), clickListener);
        return friendsViewHolder;
    }

    @Override
    public void onBindViewHolder(FriendsViewHolder holder, int position) {
        FollowUserModel model = userList.get(position);
        holder.itemFirendsIconSdv.setImageURI(Uri.parse(model.getPhoto()));
        holder.itemFriendsNameTv.setText(model.getName());
    }

    @Override
    public int getItemCount() {
        if (userList == null)
            return 0;
        else
            return userList.size();
    }

    class FriendsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.item_firends_icon_sdv)
        SimpleDraweeView itemFirendsIconSdv;
        @Bind(R.id.item_friends_name_tv)
        TextView itemFriendsNameTv;

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
            if (isInnerClick) {
                mContext.startActivity(new Intent(mContext, UserCenterActivity.class).putExtra("USERID", userList.get(getPosition()).getUserId()));
            } else {
                if (clickListener != null)
                    clickListener.itemClick(v, getAdapterPosition());
            }
        }
    }

    public void setIsInnerClick(boolean isInnerClick) {
        this.isInnerClick = isInnerClick;
    }

    interface FriendsItemClickListener {
        void itemClick(View view, int position);
    }

}
