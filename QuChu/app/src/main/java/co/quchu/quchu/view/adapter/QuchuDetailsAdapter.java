package co.quchu.quchu.view.adapter;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.DetailModel;
import co.quchu.quchu.model.SimpleQuchuDetailAnalysisModel;
import co.quchu.quchu.model.TagsModel;
import co.quchu.quchu.model.VisitedUsersModel;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.activity.QuchuDetailsActivity;
import co.quchu.quchu.view.activity.UserCenterActivity;
import co.quchu.quchu.widget.RoundProgressView;
import co.quchu.quchu.widget.RoundProgressViewNew;
import co.quchu.quchu.widget.TagCloudView;
import co.quchu.quchu.widget.ratingbar.ProperRatingBar;

/**
 * Created by admin on 2016/3/7.
 */
public class QuchuDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected static final int LAYOUT_TYPE_INTRO_IMAGE = 0x0001;
    protected static final int LAYOUT_TYPE_ACTIONBAR = 0x0002;
    //protected static final int LAYOUT_TYPE_SIMPLE_INFO = 0x0003;
    protected static final int LAYOUT_TYPE_CONTACT_INFO = 0x0004;
    protected static final int LAYOUT_TYPE_RATING_INFO = 0x0005;
    protected static final int LAYOUT_TYPE_ADDITIONAL_INFO = 0x0006;
    protected static final int LAYOUT_TYPE_OPENING_INFO = 0x0007;
    protected static final int LAYOUT_TYPE_PARTY_STARTER_INFO = 0x0008;
    protected static final int LAYOUT_TYPE_PARTY_INFO = 0x0009;
    protected static final int LAYOUT_TYPE_IMAGE = 0x0010;
    protected static final int LAYOUT_TYPE_NEARBY = 0x0011;
    protected static final int LAYOUT_TYPE_BLANK = 0x0012;
    protected static final int LAYOUT_TYPE_LOAD_MORE = 0x1001;

    private LayoutInflater mLayoutInflater;
    private Activity mAnchorActivity;
    private DetailModel mData;
    private View.OnClickListener mOnItemClickListener;
    public static final int BLOCK_INDEX = 7;
    private boolean mEnableLoadMore = true;
    private VisitedUsersModel mVisitedUsers;
    private int mVisitedUsersAvatarSize = -1;
    private int mVisitedUsersAvatarMargin;
    private SimpleQuchuDetailAnalysisModel mAnalysisModel;


    protected static final int VIEW_TYPES[] = new int[]{
            LAYOUT_TYPE_INTRO_IMAGE,
            //LAYOUT_TYPE_SIMPLE_INFO,
            LAYOUT_TYPE_CONTACT_INFO,
            LAYOUT_TYPE_OPENING_INFO,
            LAYOUT_TYPE_RATING_INFO,
            LAYOUT_TYPE_ACTIONBAR,
            LAYOUT_TYPE_ADDITIONAL_INFO,
            LAYOUT_TYPE_BLANK,
            LAYOUT_TYPE_BLANK,
            LAYOUT_TYPE_IMAGE,
            LAYOUT_TYPE_NEARBY,
            LAYOUT_TYPE_LOAD_MORE
    };

    protected static final int VIEW_TYPES_PARTY[] = new int[]{
            LAYOUT_TYPE_INTRO_IMAGE,
            //LAYOUT_TYPE_SIMPLE_INFO,
            LAYOUT_TYPE_CONTACT_INFO,
            LAYOUT_TYPE_OPENING_INFO,
            LAYOUT_TYPE_RATING_INFO,
            LAYOUT_TYPE_ACTIONBAR,
            LAYOUT_TYPE_BLANK,
            LAYOUT_TYPE_PARTY_STARTER_INFO,
            LAYOUT_TYPE_PARTY_INFO,
            LAYOUT_TYPE_IMAGE,
            LAYOUT_TYPE_NEARBY,
            LAYOUT_TYPE_LOAD_MORE
    };

    public static final int[] RANDOM_AVATAR = {R.mipmap.ic_random_user_avatar_a, R.mipmap.ic_random_user_avatar_b, R.mipmap.ic_random_user_avatar_c, R.mipmap.ic_random_user_avatar_d};

    public QuchuDetailsAdapter(Activity activity, DetailModel dModel, View.OnClickListener onClickListener) {
        if (null == onClickListener) {
            throw new IllegalArgumentException("OnClickListener Cannot be null");
        }
        mAnchorActivity = activity;
        mData = dModel;
        mLayoutInflater = LayoutInflater.from(activity);
        mOnItemClickListener = onClickListener;
        mVisitedUsersAvatarSize = mAnchorActivity.getResources().getDimensionPixelSize(R.dimen.visited_users_avatar_size);
        mVisitedUsersAvatarMargin = mAnchorActivity.getResources().getDimensionPixelOffset(R.dimen.base_margin) / 2;
    }

    public void updateVisitedUsers(VisitedUsersModel pUsers) {
        mVisitedUsers = pUsers;
        notifyDataSetChanged();
    }


    public void updateVisitorAnalysis(SimpleQuchuDetailAnalysisModel response) {
        mAnalysisModel = response;
        notifyDataSetChanged();
    }

    public void setLoadMoreListener(OnLoadMoreListener pListener) {
        mLoadMoreListener = pListener;
    }

    private OnLoadMoreListener mLoadMoreListener;

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    @Override
    public int getItemViewType(int position) {
        if (position <= (BLOCK_INDEX + 1)) {
            return null != mData && mData.isIsActivity() ? VIEW_TYPES_PARTY[position] : VIEW_TYPES[position];
        } else if (position >= (BLOCK_INDEX + 1) && position < (mData.getImglist().size() + (BLOCK_INDEX + 1))) {
            return LAYOUT_TYPE_IMAGE;
        } else if (position >= (mData.getImglist().size() + (BLOCK_INDEX + 1)) && position < (mData.getImglist().size() + BLOCK_INDEX + 1 + mData.getNearPlace().size())) {
            return LAYOUT_TYPE_NEARBY;
        } else {
            return LAYOUT_TYPE_LOAD_MORE;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case LAYOUT_TYPE_INTRO_IMAGE:
                return new IntroImageViewHolder(mLayoutInflater.inflate(R.layout.item_quchu_detail_image, parent, false));
            case LAYOUT_TYPE_ACTIONBAR:
                return new ActionViewHolder(mLayoutInflater.inflate(R.layout.item_quchu_detail_actionbar, parent, false));
//            case  LAYOUT_TYPE_SIMPLE_INFO:
//                return new SimpleInfoViewHolder(mLayoutInflater.inflate(R.layout.item_quchu_detail_simple_info, parent, false));
            case LAYOUT_TYPE_CONTACT_INFO:
                return new ContactInfoViewHolder(mLayoutInflater.inflate(R.layout.item_quchu_detail_contact_info, parent, false));
            case LAYOUT_TYPE_RATING_INFO:
                return new RatingInfoViewHolder(mLayoutInflater.inflate(R.layout.item_quchu_detail_rating_info, parent, false));
            case LAYOUT_TYPE_ADDITIONAL_INFO:
                if (null == mData || mData.isIsActivity()) {
                    return new BlankViewHolder(mLayoutInflater.inflate(R.layout.item_quchu_detail_blank, parent, false));
                } else {
                    return new AdditionalInfoViewHolder(mLayoutInflater.inflate(R.layout.item_quchu_detail_additional_info, parent, false));
                }
            case LAYOUT_TYPE_OPENING_INFO:
                return new OpeningInfoViewHolder(mLayoutInflater.inflate(R.layout.item_quchu_detail_opening_info, parent, false));
            case LAYOUT_TYPE_PARTY_STARTER_INFO:
                if (null != mData && mData.isIsActivity()) {
                    return new StarterInfoViewHolder(mLayoutInflater.inflate(R.layout.item_quchu_detail_party_starter_info, parent, false));
                } else {
                    return new BlankViewHolder(mLayoutInflater.inflate(R.layout.item_quchu_detail_blank, parent, false));
                }
            case LAYOUT_TYPE_PARTY_INFO:
                if (null != mData && mData.isIsActivity()) {
                    return new PartyInfoViewHolder(mLayoutInflater.inflate(R.layout.item_quchu_detail_party_info, parent, false));
                } else {
                    return new BlankViewHolder(mLayoutInflater.inflate(R.layout.item_quchu_detail_blank, parent, false));
                }
            case LAYOUT_TYPE_IMAGE:
                return new ImageViewHolder(mLayoutInflater.inflate(R.layout.item_card_image, parent, false));
            case LAYOUT_TYPE_NEARBY:
                return new NearbyViewHolder(mLayoutInflater.inflate(R.layout.item_bearby_quchu, parent, false));
            case LAYOUT_TYPE_LOAD_MORE:
                return new LoadMoreViewHolder(mLayoutInflater.inflate(R.layout.cp_loadmore, parent, false));
            default:
                return new BlankViewHolder(mLayoutInflater.inflate(R.layout.item_quchu_detail_blank, parent, false));
        }

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (null == mData) {
            return;
        }
        if (holder instanceof IntroImageViewHolder) {

            ((IntroImageViewHolder) holder).detail_store_name_tv.setText(null != mData.getName() ? mData.getName() : "");

            ((IntroImageViewHolder) holder).linearLayout.removeAllViews();
            for (int i = 0; i < (int)mData.getSuggest(); i++) {
                ImageView imageView = new ImageView(((IntroImageViewHolder) holder).linearLayout.getContext());
                imageView.setImageResource(R.mipmap.ic_ratingbar_heart);
                ((IntroImageViewHolder) holder).linearLayout.addView(imageView);
            }

            if (null != mData && !StringUtils.isEmpty(mData.getPrice()) && !"0".equals(mData.getPrice())) {
                ((IntroImageViewHolder) holder).detail_avg_price_tv.setText(StringUtils.getColorSpan(((IntroImageViewHolder) holder).detail_avg_price_tv.getContext(),R.color.standard_color_red,"人均消费",mData.getPrice(),""));
                ((IntroImageViewHolder) holder).detail_avg_price_tv.setVisibility(View.VISIBLE);
            } else {
                ((IntroImageViewHolder) holder).detail_avg_price_tv.setVisibility(View.GONE);
            }

            //TODO For some reason ,TagCloudView can cause laggy in this activity ,Consider using recyclerview for instead
            if (null != mData.getTags() && mData.getTags().size() > 0) {
                for (int i = 0; i < mData.getTags().size(); i++) {
                    switch (i) {
                        case 0:
                            ((IntroImageViewHolder) holder).tag1.setText(mData.getTags().get(i).getZh());
                            ((IntroImageViewHolder) holder).tag1.setVisibility(View.VISIBLE);
                            break;
                        case 1:
                            ((IntroImageViewHolder) holder).tag2.setText(mData.getTags().get(i).getZh());
                            ((IntroImageViewHolder) holder).tag2.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            ((IntroImageViewHolder) holder).tag3.setText(mData.getTags().get(i).getZh());
                            ((IntroImageViewHolder) holder).tag3.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            } else {
                ((IntroImageViewHolder) holder).tag1.setVisibility(View.INVISIBLE);
                ((IntroImageViewHolder) holder).tag2.setVisibility(View.INVISIBLE);
                ((IntroImageViewHolder) holder).tag3.setVisibility(View.INVISIBLE);
            }
        } else if (holder instanceof ActionViewHolder) {

            ((ActionViewHolder) holder).llVisitedUsers.removeAllViews();

            if (null != mVisitedUsers) {
                if (mVisitedUsers.getUserOutCount() == 0) {
                    ((ActionViewHolder) holder).tvVisitorCount.setText(R.string.no_visitor);
                } else {
                    ((ActionViewHolder) holder).tvVisitorCount.setText(StringUtils.getColorSpan(((ActionViewHolder) holder).llVisitedUsers.getContext(),R.color.standard_color_red,"有",String.valueOf(mVisitedUsers.getUserOutCount()),"人去过这里"));
                }
                LinearLayout.LayoutParams lpVisitedUsersAvatar;
                //ic_care_friends_avatar

                ((ActionViewHolder) holder).llVisitedUsers.removeAllViews();
                for (int i = 0; i < 9; i++) {
                    if (mVisitedUsers.getResult().size() > i) {
                        SimpleDraweeView sdv = new SimpleDraweeView(mAnchorActivity);
                        sdv.setImageURI(Uri.parse(mVisitedUsers.getResult().get(i).getUserPhoneUrl()));
                        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
                        roundingParams.setRoundAsCircle(true);
                        sdv.getHierarchy().setRoundingParams(roundingParams);
                        if (i > 0) {
                            lpVisitedUsersAvatar = new LinearLayout.LayoutParams(mVisitedUsersAvatarSize, mVisitedUsersAvatarSize);
                            lpVisitedUsersAvatar.setMargins(mVisitedUsersAvatarMargin, 0, 0, 0);
                        } else {
                            lpVisitedUsersAvatar = new LinearLayout.LayoutParams(mVisitedUsersAvatarSize, mVisitedUsersAvatarSize);
                            lpVisitedUsersAvatar.setMargins(0, 0, 0, 0);
                        }
                        //跳转用户中心
                        final int finalI = i;
                        sdv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(v.getContext(), UserCenterActivity.class);
                                intent.putExtra(UserCenterActivity.REQUEST_KEY_USER_ID, mVisitedUsers.getResult().get(finalI).getUserId());
                                v.getContext().startActivity(intent);
                            }
                        });


                        ((ActionViewHolder) holder).llVisitedUsers.addView(sdv, lpVisitedUsersAvatar);
                        sdv.requestLayout();
                    } else if (mVisitedUsers.getResult().size() > 0) {
                    } else {
                        SimpleDraweeView sdv = new SimpleDraweeView(mAnchorActivity);
                        Random random = new Random();
                        int randomNumber = random.nextInt(3);
                        sdv.setImageResource(RANDOM_AVATAR[randomNumber]);
                        if (i > 0) {
                            lpVisitedUsersAvatar = new LinearLayout.LayoutParams(mVisitedUsersAvatarSize, mVisitedUsersAvatarSize);
                            lpVisitedUsersAvatar.setMargins(mVisitedUsersAvatarMargin, 0, 0, 0);
                        } else {
                            lpVisitedUsersAvatar = new LinearLayout.LayoutParams(mVisitedUsersAvatarSize, mVisitedUsersAvatarSize);
                            lpVisitedUsersAvatar.setMargins(0, 0, 0, 0);
                        }
                        ((ActionViewHolder) holder).llVisitedUsers.addView(sdv, lpVisitedUsersAvatar);
                        sdv.requestLayout();
                    }

                }
                ((ActionViewHolder) holder).llVisitedUsers.invalidate();
            }


        } else if (holder instanceof ContactInfoViewHolder) {
            mEnableLoadMore = true;
            if (null == mData.getTraffic() || StringUtils.isEmpty(mData.getTraffic())) {
                ((ContactInfoViewHolder) holder).detail_store_address_tv.setText("地址：" + mData.getAddress());
                ((ContactInfoViewHolder) holder).detail_store_address_tv.setSelected(true);
            } else if (!StringUtils.isEmpty(mData.getTraffic())) {
                ((ContactInfoViewHolder) holder).detail_store_address_tv.setText("地址：" + String.format(mAnchorActivity.getResources().getString(R.string.detail_address_hint_text), mData.getAddress(), mData.getTraffic()));
            }
            ((ContactInfoViewHolder) holder).detail_store_address_ll.setOnClickListener(mOnItemClickListener);
            if (null != mData && !StringUtils.isEmpty(mData.getTel())) {
                StringTokenizer token = new StringTokenizer(mData.getTel(), " ");
                StringBuffer phoneHtml = new StringBuffer();
                while (token.hasMoreTokens()) {
                    String phoneNum = token.nextToken();
                    phoneHtml.append("<font color=#"+Integer.toHexString(((ContactInfoViewHolder) holder).detail_store_address_tv.getResources().getColor(R.color.standard_color_h2_dark)& 0x00ffffff)+"><a href='tel:").append(phoneNum).append("'>").append(phoneNum).append("</a> </font>  ");
                }
                ((ContactInfoViewHolder) holder).detail_store_phone_tv.setText("电话：");
                ((ContactInfoViewHolder) holder).detail_store_phone_tv.append(Html.fromHtml(phoneHtml.toString()));
                ((ContactInfoViewHolder) holder).detail_store_phone_tv.setMovementMethod(LinkMovementMethod.getInstance());
            } else {
                ((ContactInfoViewHolder) holder).detail_store_phone_tv.clearComposingText();
            }
        } else if (holder instanceof RatingInfoViewHolder) {
            if (null != mAnalysisModel && null != mAnalysisModel.getResult() && mAnalysisModel.getResult().size() > 0) {
                try {

                    ((RatingInfoViewHolder) holder).rpvItemLeft.setProgress(mAnalysisModel.getResult().get(0).getCount() / mAnalysisModel.getUserOutCount() * 100);
                    ((RatingInfoViewHolder) holder).rpvItemMiddle.setProgress(mAnalysisModel.getResult().get(1).getCount() / mAnalysisModel.getUserOutCount() * 100);
                    ((RatingInfoViewHolder) holder).rpvItemRight.setProgress(mAnalysisModel.getResult().get(2).getCount() / mAnalysisModel.getUserOutCount() * 100);
                    ((RatingInfoViewHolder) holder).tvRatingLeft.setText(mAnalysisModel.getResult().get(0).getZh());
                    ((RatingInfoViewHolder) holder).tvRatingMiddle.setText(mAnalysisModel.getResult().get(1).getZh());
                    ((RatingInfoViewHolder) holder).tvRatingRight.setText(mAnalysisModel.getResult().get(2).getZh());
                } catch (IndexOutOfBoundsException ex) {
                    //ex.printStackTrace();
                }
            }
        } else if (holder instanceof AdditionalInfoViewHolder) {

            ((AdditionalInfoViewHolder) holder).rvInfoGrid.setLayoutManager(new NestedLinearLayoutManager(((AdditionalInfoViewHolder) holder).rvInfoGrid.getContext(),4));
            ((AdditionalInfoViewHolder) holder).rvInfoGrid.setAdapter(null);
            if (!mData.isIsActivity()) {
                ((AdditionalInfoViewHolder) holder).rvInfoGrid.setVisibility(View.VISIBLE);

            } else {
                ((AdditionalInfoViewHolder) holder).rvInfoGrid.setVisibility(View.GONE);
            }


        } else if (holder instanceof OpeningInfoViewHolder) {
            if (mData.isIsActivity() && null != mData.getBusinessHours() && null != mData.getRestDay()) {
                ((OpeningInfoViewHolder) holder).detail_store_business_hours_key_tv.setText("报名时间：" + mData.getBusinessHours() + " " + mData.getRestDay());
            } else {
                ((OpeningInfoViewHolder) holder).detail_store_business_hours_key_tv.setText("营业时间：" + mData.getBusinessHours() + " " + mData.getRestDay());
            }
        } else if (holder instanceof StarterInfoViewHolder) {

            ((StarterInfoViewHolder) holder).detail_activity_initiator_ll.setVisibility(mData.isIsActivity() ? View.VISIBLE : View.GONE);
            ((StarterInfoViewHolder) holder).detail_activity_initiator_name_tv.setText(null != mData.getAutor() ? mData.getAutor() : "");
            if (null != mData.getAutorPhoto()) {
                ((StarterInfoViewHolder) holder).detail_activity_initiator_avator_sdv.setImageURI(Uri.parse(mData.getAutorPhoto()));
            }

        } else if (holder instanceof PartyInfoViewHolder) {

            ((PartyInfoViewHolder) holder).detail_activity_info_ll.setVisibility(mData.isIsActivity() ? View.VISIBLE : View.GONE);
            ((PartyInfoViewHolder) holder).detail_activity_info_tv.setText(mData.isIsActivity() && null != mData.getActivityInfo() ? mData.getActivityInfo() : "");
        } else if (holder instanceof ImageViewHolder) {
            int imgIndex = position - BLOCK_INDEX;
            if (null != mData.getImglist() && mData.getImglist().size() > imgIndex) {

                if (null != mData.getImglist().get(imgIndex).getImgpath()) {
                    String strUri = mData.getImglist().get(imgIndex).getImgpath();
                    ((ImageViewHolder) holder).item_card_image_sdv.setImageURI(Uri.parse(strUri));

                    if (0 == mData.getImglist().get(imgIndex).getWidth() || 0 == mData.getImglist().get(imgIndex).getHeight()) {
                        ((ImageViewHolder) holder).item_card_image_sdv.setAspectRatio(1.2f);
                    } else {
                        ((ImageViewHolder) holder).item_card_image_sdv.setAspectRatio((float) mData.getImglist().get(imgIndex).getWidth() / (float) mData.getImglist().get(imgIndex).getHeight());
                    }
                }
            } else {
                ((ImageViewHolder) holder).item_card_image_sdv.setAspectRatio(1.2f);
            }

        } else if (holder instanceof NearbyViewHolder) {
            if (null != mData.getNearPlace()) {
                int imgIndex = position - BLOCK_INDEX;
                if (null != mData.getImglist()) {
                    imgIndex -= mData.getImglist().size();
                }
                if (null == mData.getNearPlace().get(imgIndex - 1) || null == mData.getNearPlace().get(imgIndex - 1)) {
                    return;
                }
                ((NearbyViewHolder) holder).tvName.setText(mData.getNearPlace().get(imgIndex - 1).getName());
                List<String> strTags = new ArrayList<>();
                List<TagsModel> tags = mData.getNearPlace().get(imgIndex - 1).getTags();
                if (null != tags && tags.size() > 0) {
                    for (int i = 0; i < tags.size(); i++) {
                        strTags.add(tags.get(i).getZh());
                    }
                }

                ((NearbyViewHolder) holder).tcvTag.setTags(strTags);
//                ((NearbyViewHolder) holder).tvAddress.setText(mData.getNearPlace().get(imgIndex - 1).getAddress());
                ((NearbyViewHolder) holder).sdvImage.setImageURI(Uri.parse(mData.getNearPlace().get(imgIndex - 1).getCover()));
                final int pid = mData.getNearPlace().get(imgIndex - 1).getPlaceId();
                ((NearbyViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(mAnchorActivity, QuchuDetailsActivity.class);
                        intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_FROM, QuchuDetailsActivity.FROM_TYPE_RECOM);
                        intent.putExtra(QuchuDetailsActivity.REQUEST_KEY_PID, pid);
                        mAnchorActivity.startActivity(intent);
                    }
                });

            }
        } else if (holder instanceof LoadMoreViewHolder) {
            if (!mEnableLoadMore) {
                ((LoadMoreViewHolder) holder).ivLoadMore.clearAnimation();
                ((LoadMoreViewHolder) holder).itemView.setVisibility(View.INVISIBLE);
            } else {
                ((LoadMoreViewHolder) holder).itemView.setVisibility(View.VISIBLE);
                ObjectAnimator rotation = ObjectAnimator.ofFloat(((LoadMoreViewHolder) holder).ivLoadMore, "rotation", 0, 360);
                rotation.setInterpolator(new LinearInterpolator());
                rotation.setRepeatMode(ValueAnimator.RESTART);
                rotation.setRepeatCount(ValueAnimator.INFINITE);
                rotation.setDuration(1500);
                rotation.start();

                if (null != mLoadMoreListener) {
                    mLoadMoreListener.onLoadMore();
                }
            }


        }
    }


    public void finishLoadMore() {
        mEnableLoadMore = false;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        int basicCount = (BLOCK_INDEX + 1);
        if (null != mData && null != mData.getImglist()) {
            basicCount += mData.getImglist().size();
        }
        if (null != mData && null != mData.getNearPlace()) {
            basicCount += mData.getNearPlace().size();
        }
        basicCount += 1;
        return basicCount;
    }

    public static class LoadMoreViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ivIndicator)
        ImageView ivLoadMore;

        LoadMoreViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class IntroImageViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.detail_store_name_tv)
        TextView detail_store_name_tv;
        @Bind(R.id.llRating)
        LinearLayout linearLayout;
        @Bind(R.id.detail_avg_price_tv)
        TextView detail_avg_price_tv;

        @Bind(R.id.recommend_tag1)
        TextView tag1;
        @Bind(R.id.recommend_tag2)
        TextView tag2;
        @Bind(R.id.recommend_tag3)
        TextView tag3;

        IntroImageViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class ActionViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.llVisitedUsers)
        LinearLayout llVisitedUsers;
        @Bind(R.id.tvVisitorCount)
        TextView tvVisitorCount;

        ActionViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public static class ContactInfoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.detail_store_address_ll)
        LinearLayout detail_store_address_ll;
        @Bind(R.id.detail_store_address_tv)
        TextView detail_store_address_tv;
        @Bind(R.id.detail_store_phone_ll)
        LinearLayout detail_store_phone_ll;
        @Bind(R.id.detail_store_phone_tv)
        TextView detail_store_phone_tv;

        ContactInfoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class RatingInfoViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.rpvItemLeft)
        RoundProgressViewNew rpvItemLeft;
        @Bind(R.id.rpvItemMiddle)
        RoundProgressViewNew rpvItemMiddle;
        @Bind(R.id.rpvItemRight)
        RoundProgressViewNew rpvItemRight;

        @Bind(R.id.tvRatingLeft)
        TextView tvRatingLeft;
        @Bind(R.id.tvRatingRight)
        TextView tvRatingRight;
        @Bind(R.id.tvRatingMiddle)
        TextView tvRatingMiddle;

        RatingInfoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class AdditionalInfoViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.rvAdditionalInfo)
        RecyclerView rvInfoGrid;

        AdditionalInfoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class OpeningInfoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.detail_store_business_hours_key_tv)
        TextView detail_store_business_hours_key_tv;

        OpeningInfoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class StarterInfoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.detail_activity_initiator_ll)
        LinearLayout detail_activity_initiator_ll;
        @Bind(R.id.detail_activity_initiator_title_tv)
        TextView detail_activity_initiator_title_tv;
        @Bind(R.id.detail_activity_initiator_avator_sdv)
        SimpleDraweeView detail_activity_initiator_avator_sdv;
        @Bind(R.id.detail_activity_initiator_name_tv)
        TextView detail_activity_initiator_name_tv;

        StarterInfoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class PartyInfoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.detail_activity_info_ll)
        LinearLayout detail_activity_info_ll;
        @Bind(R.id.detail_activity_title_tv)
        TextView detail_activity_title_tv;
        @Bind(R.id.detail_activity_info_tv)
        TextView detail_activity_info_tv;

        PartyInfoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_card_image_sdv)
        SimpleDraweeView item_card_image_sdv;

        ImageViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public static class BlankViewHolder extends RecyclerView.ViewHolder {
        BlankViewHolder(View view) {
            super(view);
        }
    }

    public static class NearbyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name)
        TextView tvName;
        @Bind(R.id.tag)
        TagCloudView tcvTag;
        @Bind(R.id.simpleDraweeView)
        SimpleDraweeView sdvImage;

        NearbyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
