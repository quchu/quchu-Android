package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.MessageModel;
import co.quchu.quchu.utils.DateUtils;
import co.quchu.quchu.utils.StringUtils;

/**
 * 消息中心
 */
public class MessageCenterAdapter extends AdapterBase<MessageModel.ResultBean, RecyclerView.ViewHolder> {

    public static final int ITEM_TYPE_NOTHING = 0;
    public static final int ITEM_TYPE_FOOTPRING = 1;
    public static final int ITEM_TYPE_FOLLOW = 2;

    public static final int CLICK_TYPE_FOLLOW = 1;
    //点击头像
    public static final int CLICK_TYPE_USER_INFO = 2;
    public static final int CLICK_TYPE_FOOTPRINT_COVER = 3;


    private Context mContext;

    public MessageCenterAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onBindView(final RecyclerView.ViewHolder holder, final int position) {
        final MessageModel.ResultBean model = data.get(position);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.itemClick(holder, model, CLICK_TYPE_USER_INFO, position);
            }
        };

        switch (getItemType(position)) {
            case ITEM_TYPE_FOLLOW://普通类型
                final CommomHolder commomHolder = (CommomHolder) holder;
                commomHolder.itemMessageFromAvator.setImageURI(Uri.parse(model.getFormPhoto()));

                commomHolder.itemMessageAddTimeTv.setText(DateUtils.getTimeRange(model.getTime(), mContext));
                commomHolder.itemMessageUserNameTv.setText(model.getForm() + model.getContent());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.itemClick(holder, model, 0, position);
                    }
                });

                switch (model.getType()) {
                    case "follow":
                        commomHolder.itemMessageFollowTv.setVisibility(View.VISIBLE);
                        StringUtils.setTextHighlighting(commomHolder.itemMessageUserNameTv, 0, model.getForm().length());
                        if (model.isInteraction()) {
                            commomHolder.itemMessageFollowTv.setTextColor(ContextCompat.getColor(mContext, android.R.color.white));
                            commomHolder.itemMessageFollowTv.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_message_follow_full_bg));
                            commomHolder.itemMessageFollowTv.setText("互相关注");
                        } else if ("yes".equals(model.getCome())) {
                            commomHolder.itemMessageFollowTv.setTextColor(ContextCompat.getColor(mContext, android.R.color.white));
                            commomHolder.itemMessageFollowTv.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_message_follow_full_bg));
                            commomHolder.itemMessageFollowTv.setText("已关注");
                        } else {
                            commomHolder.itemMessageFollowTv.setTextColor(ContextCompat.getColor(mContext, R.color.standard_color_yellow));
                            commomHolder.itemMessageFollowTv.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_message_follow_bg));
                            commomHolder.itemMessageFollowTv.setText("关注");
                        }

                        break;
                    case "favorite":
                        commomHolder.itemMessageFollowTv.setVisibility(View.GONE);
                        break;
                    case "praise":
                        commomHolder.itemMessageFollowTv.setVisibility(View.GONE);
                        break;
                    case "share":
                        commomHolder.itemMessageFollowTv.setVisibility(View.GONE);
                        break;
                    case "feedback":
                        commomHolder.itemMessageFollowTv.setVisibility(View.GONE);
                        break;
                }
                if (itemClickListener != null) {

                    commomHolder.itemMessageFollowTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            itemClickListener.itemClick(commomHolder, model, CLICK_TYPE_FOLLOW, 0);
                        }
                    });
                    commomHolder.itemMessageFromAvator.setOnClickListener(clickListener);
                }

                break;
            case ITEM_TYPE_FOOTPRING://脚印点赞
                FootprintViewHolder footprintViewHolder = (FootprintViewHolder) holder;

                footprintViewHolder.itemMessageTitle.setText(model.getForm() + model.getContent());

                StringUtils.setTextHighlighting(footprintViewHolder.itemMessageTitle, 0, model.getForm().length());

                footprintViewHolder.itemMessageImage.setImageURI(Uri.parse(model.getFormPhoto()));
                footprintViewHolder.itemMessageTime.setText(DateUtils.getTimeRange(model.getTime(), mContext));


                footprintViewHolder.itemMessageCover.setAspectRatio(model.getWidth() / (float) model.getHeight());
                footprintViewHolder.itemMessageCover.setImageURI(Uri.parse(model.getTargetImageUrl() + ""));

                footprintViewHolder.itemMessageImage.setOnClickListener(clickListener);
                footprintViewHolder.itemMessageCover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.itemClick(holder, model, CLICK_TYPE_FOOTPRINT_COVER, position);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemType(int position) {
        MessageModel.ResultBean bean = data.get(position);
        if (bean.getTargetType() == null) {
            return ITEM_TYPE_FOLLOW;
        }
        switch (bean.getTargetType()) {
            case "10"://普通类型
                return ITEM_TYPE_FOLLOW;
            case "11"://脚印点赞
                return ITEM_TYPE_FOOTPRING;
        }
        return ITEM_TYPE_NOTHING;//无
    }

    @Override
    public RecyclerView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_FOLLOW://普通类型
                return new CommomHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_center_follow, parent, false));
            case ITEM_TYPE_FOOTPRING://脚印点赞
                return new FootprintViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_center_footprint, parent, false));
        }
        return null;

    }

    class CommomHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_message_from_avator)
        SimpleDraweeView itemMessageFromAvator;
        @Bind(R.id.item_message_follow_tv)
        TextView itemMessageFollowTv;
        @Bind(R.id.item_message_user_name_tv)
        TextView itemMessageUserNameTv;
        @Bind(R.id.item_message_add_time_tv)
        TextView itemMessageAddTimeTv;

        public CommomHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class FootprintViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_message_image)
        SimpleDraweeView itemMessageImage;
        @Bind(R.id.item_message_title)
        TextView itemMessageTitle;
        @Bind(R.id.item_message_time)
        TextView itemMessageTime;
        @Bind(R.id.item_message_cover)
        SimpleDraweeView itemMessageCover;

        public FootprintViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
