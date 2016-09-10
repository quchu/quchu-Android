package co.quchu.quchu.view.adapter;

import android.animation.ObjectAnimator;
import android.net.Uri;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.model.FavoriteBean;
import co.quchu.quchu.utils.AppKey;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.widget.SwipeDeleteLayout;
import co.quchu.quchu.widget.TagCloudView;

/**
 * Created by no21 on 2016/4/5.
 * email:437943145@qq.com
 * desc :
 */
public class FavoriteQuchuAdapter extends AdapterBase<FavoriteBean.ResultBean, FavoriteQuchuAdapter.ViewHold> {

    private boolean animationed;
    private boolean isMe;

    public FavoriteQuchuAdapter(boolean isMe) {
        animationed = SPUtils.getBooleanFromSPMap(AppContext.mContext, AppKey.SPF_KEY_SWIPE_DELETE_PROMPT_FAVORITE_QUCHU, false);
        this.isMe = isMe;
    }

    @Override
    public void onBindView(final ViewHold holder, final int position) {

        if (position == 0 && !animationed) {
            animationed = true;
            SPUtils.putBooleanToSPMap(AppContext.mContext, AppKey.SPF_KEY_SWIPE_DELETE_PROMPT_FAVORITE_QUCHU, true);
            holder.itemView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    holder.itemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    ObjectAnimator animator = ObjectAnimator.ofInt(holder.swipeDeleteItem, "ScrollX", 0, holder.swipeDeleteAction.getWidth(), 0);
                    animator.setDuration(800);
                    animator.setStartDelay(500);
                    animator.setInterpolator(new FastOutLinearInInterpolator());
                    animator.start();
                }
            });
        }

        final FavoriteBean.ResultBean bean = data.get(position);

        if (isMe) {
            holder.swipeDeleteItem.hideAction(false);
            holder.swipeDeleteItem.setScrollX(0);
            holder.swipeDeleteAction.setVisibility(View.VISIBLE);
        } else {
            holder.swipeDeleteItem.hideAction(true);
            holder.swipeDeleteAction.setVisibility(View.GONE);
        }
        holder.name.setText(bean.getName());
        holder.simpleDraweeView.setImageURI(Uri.parse(bean.getCover()));
        holder.tag.setTags(bean.getTagsString());
        holder.address.setText(bean.getAddress());
        holder.address.setSelected(true);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (itemClickListener != null) {
//                    itemClickListener.itemClick(holder, bean, v.getId(), position);
//                }
//            }
//        });
        if (itemClickListener != null) {

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.swipe_delete_content && holder.swipeDeleteItem.getScrollX() > 0) {
                        holder.swipeDeleteItem.animation(-holder.swipeDeleteItem.getScrollX());
                        return;
                    }
                    itemClickListener.itemClick(holder, bean, v.getId(), position);
                }
            };

            holder.swipeDeleteContent.setOnClickListener(onClickListener);
            holder.swipeDeleteAction.setOnClickListener(onClickListener);
        }
    }

    @Override
    public ViewHold onCreateView(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_quchu, parent, false);
        return new ViewHold(view);
    }

    class ViewHold extends RecyclerView.ViewHolder {

        @Bind(R.id.simpleDraweeView)
        SimpleDraweeView simpleDraweeView;
        @Bind(R.id.desc)
        TextView name;
        @Bind(R.id.tag)
        TagCloudView tag;
        @Bind(R.id.address)
        TextView address;
        @Bind(R.id.cvRoot)
        CardView cvRoot;
        @Bind(R.id.swipe_delete_content)
        RelativeLayout swipeDeleteContent;
        @Bind(R.id.swipe_delete_action)
        FrameLayout swipeDeleteAction;
        @Bind(R.id.swipe_delete_item)
        SwipeDeleteLayout swipeDeleteItem;

        public ViewHold(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            address.setVisibility(View.VISIBLE);
        }
    }


}
