package co.quchu.quchu.view.adapter;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.dialog.BottomListDialog;
import co.quchu.quchu.model.CommentModel;
import co.quchu.quchu.model.DetailModel;
import co.quchu.quchu.model.ImageModel;
import co.quchu.quchu.model.SimpleQuchuDetailAnalysisModel;
import co.quchu.quchu.model.TagsModel;
import co.quchu.quchu.model.VisitedInfoModel;
import co.quchu.quchu.model.VisitedUsersModel;
import co.quchu.quchu.utils.StringUtils;
import co.quchu.quchu.view.activity.QuchuDetailsActivity;
import co.quchu.quchu.view.activity.QuchuListSpecifyTagActivity;
import co.quchu.quchu.widget.SimpleIndicatorView;
import co.quchu.quchu.widget.TagCloudView;

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
    protected static final int LAYOUT_TYPE_LABEL = 0x0013;
    protected static final int LAYOUT_TYPE_COMMENT = 0x0014;
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
    private VisitedInfoModel mVisitedInfoModel;
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

    //public static final int[] RANDOM_AVATAR = {R.mipmap.ic_random_user_avatar_a, R.mipmap.ic_random_user_avatar_b,
//            R.mipmap.ic_random_user_avatar_c, R.mipmap.ic_random_user_avatar_d};

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

    public void updateRatingInfo(VisitedInfoModel pVisitedInfoModel){
        mVisitedInfoModel = pVisitedInfoModel;
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
        int imgSize = null != mData.getImglist() ? mData.getImglist().size() : 0;
        int nearbySize = null != mData.getNearPlace() ? mData.getNearPlace().size() : 0;
        int commentsSize = null != mData.getReviewList() ? mData.getReviewList().size() : 0;
        if (position <= (BLOCK_INDEX + 1)) {
            return null != mData && mData.isIsActivity() ? VIEW_TYPES_PARTY[position] : VIEW_TYPES[position];
        } else if (position >= (BLOCK_INDEX + 1) && position < (imgSize + (BLOCK_INDEX + 1))) {
            return LAYOUT_TYPE_IMAGE;
        } else if (position >= (imgSize + (BLOCK_INDEX + 1)) && position < (imgSize + commentsSize + (BLOCK_INDEX + 1))) {
            return LAYOUT_TYPE_COMMENT;
        } else if (position >= (imgSize + commentsSize + (BLOCK_INDEX + 1)) && position < (imgSize + commentsSize + (BLOCK_INDEX + 2))) {
            return LAYOUT_TYPE_LABEL;
        } else if (position >= (imgSize + commentsSize + (BLOCK_INDEX + 2)) && position < (imgSize + commentsSize + BLOCK_INDEX + 2 + nearbySize)) {
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
//            case LAYOUT_TYPE_OPENING_INFO:
//                return new OpeningInfoViewHolder(mLayoutInflater.inflate(R.layout.item_quchu_detail_opening_info, parent, false));
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
//            case LAYOUT_TYPE_IMAGE:
//                return new ImageViewHolder(mLayoutInflater.inflate(R.layout.item_card_image, parent, false));
            case LAYOUT_TYPE_LABEL:
                return new LabelViewHolder(mLayoutInflater.inflate(R.layout.item_quchu_detail_simple_label, parent, false));
            case LAYOUT_TYPE_NEARBY:
                return new NearbyViewHolder(mLayoutInflater.inflate(R.layout.item_nearby_quchu_detail, parent, false));
            case LAYOUT_TYPE_COMMENT:
                return new CommentViewHolder(mLayoutInflater.inflate(R.layout.item_quchu_detail_comment, parent, false));
            case LAYOUT_TYPE_LOAD_MORE:
                return new LoadMoreViewHolder(mLayoutInflater.inflate(R.layout.cp_loadmore_horizontal, parent, false));
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

            if (null != mData.getImglist() && mData.getImglist().size() > 0) {
                List<ImageModel> imageSet = new ArrayList<>();
                for (int i = 0; i < mData.getImglist().size(); i++) {
                    imageSet.add(mData.getImglist().get(i).convert2ImageModel());
                }
                ((IntroImageViewHolder) holder).vpGallery.setAdapter(new GalleryAdapter(imageSet));
                ((IntroImageViewHolder) holder).siv.setIndicators(imageSet.size());
                ((IntroImageViewHolder) holder).siv.setVisibility(View.VISIBLE);
                ((IntroImageViewHolder) holder).vpGallery.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        ((IntroImageViewHolder) holder).siv.setCurrentIndex(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            } else {
                ((IntroImageViewHolder) holder).siv.setVisibility(View.GONE);
            }

            ((IntroImageViewHolder) holder).detail_store_name_tv.setText(null != mData.getName() ? mData.getName().trim() : "");


            //TODO For some reason ,TagCloudView can cause laggy in this activity ,Consider using recyclerview for instead
            if (null != mData.getTags() && mData.getTags().size() > 0) {
                for (int i = 0; i < mData.getTags().size(); i++) {
                    switch (i) {
                        case 0:
                            ((IntroImageViewHolder) holder).tag1.setText(mData.getTags().get(i).getZh());
                            ((IntroImageViewHolder) holder).tag1.setVisibility(View.VISIBLE);
                            ((IntroImageViewHolder) holder).tag1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(mAnchorActivity, QuchuListSpecifyTagActivity.class);
                                    intent.putExtra(QuchuListSpecifyTagActivity.BUNDLE_KEY_TAG_ID, mData.getTags().get(0).getId());
                                    intent.putExtra(QuchuListSpecifyTagActivity.BUNDLE_KEY_TAG_NAME, mData.getTags().get(0).getZh());
                                    mAnchorActivity.startActivity(intent);
                                }
                            });
                            break;
                        case 1:
                            ((IntroImageViewHolder) holder).tag2.setText(mData.getTags().get(i).getZh());
                            ((IntroImageViewHolder) holder).tag2.setVisibility(View.VISIBLE);
                            ((IntroImageViewHolder) holder).tag2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(mAnchorActivity, QuchuListSpecifyTagActivity.class);
                                    intent.putExtra(QuchuListSpecifyTagActivity.BUNDLE_KEY_TAG_ID, mData.getTags().get(1).getId());
                                    intent.putExtra(QuchuListSpecifyTagActivity.BUNDLE_KEY_TAG_NAME, mData.getTags().get(1).getZh());
                                    mAnchorActivity.startActivity(intent);
                                }
                            });
                            break;
                        case 2:
                            ((IntroImageViewHolder) holder).tag3.setText(mData.getTags().get(i).getZh());
                            ((IntroImageViewHolder) holder).tag3.setVisibility(View.VISIBLE);
                            ((IntroImageViewHolder) holder).tag3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(mAnchorActivity, QuchuListSpecifyTagActivity.class);
                                    intent.putExtra(QuchuListSpecifyTagActivity.BUNDLE_KEY_TAG_ID, mData.getTags().get(2).getId());
                                    intent.putExtra(QuchuListSpecifyTagActivity.BUNDLE_KEY_TAG_NAME, mData.getTags().get(2).getZh());
                                    mAnchorActivity.startActivity(intent);
                                }
                            });
                            break;
                    }
                }
            } else {
                ((IntroImageViewHolder) holder).tag1.setVisibility(View.GONE);
                ((IntroImageViewHolder) holder).tag2.setVisibility(View.GONE);
                ((IntroImageViewHolder) holder).tag3.setVisibility(View.GONE);
            }


            if (!mData.isIsActivity() && null != mData.getIcons() && mData.getIcons().size() > 0) {
                ((IntroImageViewHolder) holder).rvInfoGrid.setVisibility(View.VISIBLE);
                ((IntroImageViewHolder) holder).rvInfoGrid.setLayoutManager(new LinearLayoutManager(mAnchorActivity, LinearLayoutManager.HORIZONTAL, false));
                ((IntroImageViewHolder) holder).rvInfoGrid.setAdapter(new AdditionalInfoAdapter(mData.getIcons()));

                if (null == ((IntroImageViewHolder) holder).rvInfoGrid.getTag() || !((boolean) ((IntroImageViewHolder) holder).rvInfoGrid.getTag())) {

                    ((IntroImageViewHolder) holder).rvInfoGrid.setTag(true);
                }
            } else {
                ((IntroImageViewHolder) holder).rvInfoGrid.setVisibility(View.GONE);
            }
        } else if (holder instanceof ActionViewHolder) {
//
//            ((ActionViewHolder) holder).llVisitedUsers.removeAllViews();
//
//            if (null != mVisitedUsers) {
//                if (mVisitedUsers.getUserOutCount() == 0) {
//                    ((ActionViewHolder) holder).tvVisitorCount.setText(R.string.no_visitor);
//                } else {
//                    ((ActionViewHolder) holder).tvVisitorCount.setText(StringUtils.getColorSpan(((ActionViewHolder) holder).llVisitedUsers.getContext(), R.color.standard_color_red, "有", String.valueOf(mVisitedUsers.getUserOutCount()), "人去过这里"));
//                }
//                LinearLayout.LayoutParams lpVisitedUsersAvatar;
//                //ic_care_friends_avatar
//
//                ((ActionViewHolder) holder).llVisitedUsers.removeAllViews();
//                for (int i = 0; i < 9; i++) {
//                    if (mVisitedUsers.getResult().size() > i) {
//                        SimpleDraweeView sdv = new SimpleDraweeView(mAnchorActivity);
//                        sdv.setImageURI(Uri.parse(mVisitedUsers.getResult().get(i).getUserPhoneUrl()));
//                        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
//                        roundingParams.setRoundAsCircle(true);
//                        sdv.getHierarchy().setRoundingParams(roundingParams);
//                        sdv.getHierarchy().setPlaceholderImage(R.mipmap.head_place_hold);
//                        sdv.getHierarchy().setFailureImage(ContextCompat.getDrawable(mAnchorActivity, R.mipmap.head_place_hold));
//                        if (i > 0) {
//                            lpVisitedUsersAvatar = new LinearLayout.LayoutParams(mVisitedUsersAvatarSize, mVisitedUsersAvatarSize);
//                            lpVisitedUsersAvatar.setMargins(mVisitedUsersAvatarMargin, 0, 0, 0);
//                        } else {
//                            lpVisitedUsersAvatar = new LinearLayout.LayoutParams(mVisitedUsersAvatarSize, mVisitedUsersAvatarSize);
//                            lpVisitedUsersAvatar.setMargins(0, 0, 0, 0);
//                        }
//                        //跳转用户中心
//
////                        final int finalI = i;
////                        sdv.setOnClickListener(new View.OnClickListener() {
////                            @Override
////                            public void onClick(View v) {
////                                Intent intent = new Intent(v.getContext(), UserCenterActivity.class);
////                                intent.putExtra(UserCenterActivity.REQUEST_KEY_USER_ID, mVisitedUsers.getResult().get(finalI).getUserId());
////                                v.getContext().startActivity(intent);
////                            }
////                        });
//
//
//                        ((ActionViewHolder) holder).llVisitedUsers.addView(sdv, lpVisitedUsersAvatar);
//                        sdv.requestLayout();
//                    }
////                    else if (mVisitedUsers.getResult().size() > 0) {
////                    } else {
////                        SimpleDraweeView sdv = new SimpleDraweeView(mAnchorActivity);
////                        Random random = new Random();
////                        int randomNumber = random.nextInt(3);
////                        sdv.setImageResource(RANDOM_AVATAR[randomNumber]);
////                        if (i > 0) {
////                            lpVisitedUsersAvatar = new LinearLayout.LayoutParams(mVisitedUsersAvatarSize, mVisitedUsersAvatarSize);
////                            lpVisitedUsersAvatar.setMargins(mVisitedUsersAvatarMargin, 0, 0, 0);
////                        } else {
////                            lpVisitedUsersAvatar = new LinearLayout.LayoutParams(mVisitedUsersAvatarSize, mVisitedUsersAvatarSize);
////                            lpVisitedUsersAvatar.setMargins(0, 0, 0, 0);
////                        }
////                        ((ActionViewHolder) holder).llVisitedUsers.addView(sdv, lpVisitedUsersAvatar);
////                        sdv.requestLayout();
////                    }
//
//                }
//                ((ActionViewHolder) holder).llVisitedUsers.invalidate();
//                ((ActionViewHolder) holder).llVisitedUsers.requestLayout();
//            }


        } else if (holder instanceof ContactInfoViewHolder) {
            mEnableLoadMore = true;
            if (null == mData.getTraffic() || StringUtils.isEmpty(mData.getTraffic())) {
                ((ContactInfoViewHolder) holder).detail_store_address_tv.setText(mData.getAddress());
                ((ContactInfoViewHolder) holder).detail_store_address_tv.setSelected(true);
            } else if (!StringUtils.isEmpty(mData.getTraffic())) {
                ((ContactInfoViewHolder) holder).detail_store_address_tv.setText(String.format(mAnchorActivity.getResources().getString(R.string.detail_address_hint_text), mData.getAddress(), mData.getTraffic()));
            } else {
                ((ContactInfoViewHolder) holder).detail_store_address_tv.setVisibility(View.GONE);
            }
            ((ContactInfoViewHolder) holder).detail_store_address_ll.setOnClickListener(mOnItemClickListener);
            if (null != mData && !StringUtils.isEmpty(mData.getTel())) {
                ((ContactInfoViewHolder) holder).detail_store_phone_tv.setVisibility(View.VISIBLE);
                ((ContactInfoViewHolder) holder).detail_store_phone_tv.setText(mData.getTel());
                ((ContactInfoViewHolder) holder).detail_store_phone_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BottomListDialog b = new BottomListDialog(mAnchorActivity, mData.getTel().split(" "));
                        b.show();
                    }
                });
                ((ContactInfoViewHolder) holder).detail_store_phone_ll.setVisibility(View.VISIBLE);
                ((ContactInfoViewHolder) holder).vDividerPhone.setVisibility(View.VISIBLE);
            } else {
//                ((ContactInfoViewHolder) holder).detail_store_phone_tv.setText("电话：-");
                ((ContactInfoViewHolder) holder).detail_store_phone_tv.setVisibility(View.GONE);
                ((ContactInfoViewHolder) holder).detail_store_phone_ll.setVisibility(View.GONE);
                ((ContactInfoViewHolder) holder).vDividerPhone.setVisibility(View.GONE);


            }

            if (mData.isIsActivity() && null != mData.getBusinessHours() && null != mData.getRestDay()) {
                ((ContactInfoViewHolder) holder).detail_store_business_hours_key_tv.setText(mData.getBusinessHours());
                ((ContactInfoViewHolder) holder).detail_store_business_hours_key_tv.setVisibility(View.VISIBLE);
                ((ContactInfoViewHolder) holder).vDividerOpeningTime.setVisibility(View.VISIBLE);
            } else {
                ((ContactInfoViewHolder) holder).detail_store_business_hours_key_tv.setVisibility(View.GONE);
                ((ContactInfoViewHolder) holder).vDividerOpeningTime.setVisibility(View.GONE);
            }

            if (null != mData && !StringUtils.isEmpty(mData.getPrice()) && !"0".equals(mData.getPrice())) {
                ((ContactInfoViewHolder) holder).detail_avg_price_tv.setText(mData.getPrice() + "元/人");
                //((ContactInfoViewHolder) holder).detail_avg_price_tv.setText(StringUtils.getColorSpan(((ContactInfoViewHolder) holder).detail_avg_price_tv.getContext(), R.color.standard_color_red, "人均消费", "¥" + mData.getPrice(), ""));
                ((ContactInfoViewHolder) holder).detail_avg_price_tv.setVisibility(View.VISIBLE);
            } else {
                ((ContactInfoViewHolder) holder).detail_avg_price_tv.setVisibility(View.GONE);
            }

        } else if (holder instanceof RatingInfoViewHolder) {
            if (null != mVisitedInfoModel ) {
                ((RatingInfoViewHolder) holder).rbRating.setProgress(mVisitedInfoModel.getScore());
            }
            if (null!=mAnalysisModel){
                ((RatingInfoViewHolder) holder).tvRatingCount.setText(mAnalysisModel.getUserOutCount()+"人评价");

                List<String> tags=  new ArrayList<>();
                if (null!=mAnalysisModel.getResult()){
                    for (int i = 0; i < mAnalysisModel.getResult().size() ; i++) {
                        TagsModel objTag = mAnalysisModel.getResult().get(i);
                        tags.add(objTag.getZh() +" "+objTag.getCount());
                    }
                    ((RatingInfoViewHolder) holder).tagCloudView.setTags(tags);
                }
            }
        } else if (holder instanceof AdditionalInfoViewHolder) {

        } else if (holder instanceof OpeningInfoViewHolder) {

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
//            int imgIndex = position - BLOCK_INDEX;
//            if (null != mData.getImglist() && mData.getImglist().size() > imgIndex) {
//
//                if (null != mData.getImglist().get(imgIndex).getImgpath()) {
//                    String strUri = mData.getImglist().get(imgIndex).getImgpath();
//                    ((ImageViewHolder) holder).item_card_image_sdv.setImageURI(Uri.parse(strUri));
//
//                    if (0 == mData.getImglist().get(imgIndex).getWidth() || 0 == mData.getImglist().get(imgIndex).getHeight()) {
//                        ((ImageViewHolder) holder).item_card_image_sdv.setAspectRatio(1.2f);
//                    } else {
//                        ((ImageViewHolder) holder).item_card_image_sdv.setAspectRatio((float) mData.getImglist().get(imgIndex).getWidth() / (float) mData.getImglist().get(imgIndex).getHeight());
//                    }
//                }
//            } else {
//                ((ImageViewHolder) holder).item_card_image_sdv.setAspectRatio(1.2f);
//            }

        } else if (holder instanceof CommentViewHolder) {
            if (null != mData.getReviewList()) {

                int commentIndex = position - BLOCK_INDEX;
                if (null != mData.getImglist()) {
                    commentIndex -= mData.getImglist().size();
                }
                commentIndex -= 1;
                if (null == mData.getReviewList().get(commentIndex) || null == mData.getReviewList().get(commentIndex)) {
                    return;
                }
                CommentModel commentModel = mData.getReviewList().get(commentIndex);
                ((CommentViewHolder) holder).tvUsername.setText(commentModel.getUserName());
                ((CommentViewHolder) holder).tvDate.setText(commentModel.getCreateDate());
                ((CommentViewHolder) holder).tvFrom.setText(commentModel.getSourceContent());
                ((CommentViewHolder) holder).tvUserComment.setText(commentModel.getContent());
                ((CommentViewHolder) holder).sdvAvatar.setImageURI(Uri.parse(commentModel.getUserPhoneUrl()));


            }
        } else if (holder instanceof NearbyViewHolder) {
            if (null != mData.getNearPlace()) {
                int imgIndex = position - BLOCK_INDEX;
                if (null != mData.getImglist()) {
                    imgIndex -= mData.getImglist().size();
                }
                if (null != mData.getReviewList()) {
                    imgIndex -= mData.getReviewList().size();
                }
                imgIndex -= 1;
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
                ((NearbyViewHolder) holder).cardView.setBackgroundColor(Color.RED);

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

            if (mEnableLoadMore) {
                ((LoadMoreViewHolder) holder).ivLoadMore.clearAnimation();
                ((LoadMoreViewHolder) holder).ivLoadMore.setVisibility(View.GONE);
                ((LoadMoreViewHolder) holder).textView.setVisibility(View.VISIBLE);
                ((LoadMoreViewHolder) holder).textView.setText(R.string.click_to_load_more);
                ((LoadMoreViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((LoadMoreViewHolder) holder).ivLoadMore.setVisibility(View.VISIBLE);
                        ((LoadMoreViewHolder) holder).textView.setText(R.string.loading_dialog_text);
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
                });
            } else {
                ((LoadMoreViewHolder) holder).ivLoadMore.setVisibility(View.GONE);
                ((LoadMoreViewHolder) holder).textView.setVisibility(View.VISIBLE);
                ((LoadMoreViewHolder) holder).textView.setText(R.string.click_to_load_more);
                ((LoadMoreViewHolder) holder).ivLoadMore.clearAnimation();
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
        if (null != mData && null != mData.getReviewList()) {
            basicCount += mData.getReviewList().size();
        }
        basicCount += 2;
        return basicCount;
    }

    public static class LoadMoreViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ivIndicator)
        ImageView ivLoadMore;
        @Bind(R.id.textView)
        TextView textView;

        LoadMoreViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public static class LabelViewHolder extends RecyclerView.ViewHolder {
        LabelViewHolder(View view) {
            super(view);
        }
    }

    public static class IntroImageViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.detail_store_name_tv)
        TextView detail_store_name_tv;

        @Bind(R.id.vpGallery)
        ViewPager vpGallery;

        @Bind(R.id.siv)
        SimpleIndicatorView siv;


        @Bind(R.id.rvAdditionalInfo)
        RecyclerView rvInfoGrid;

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
        @Bind(R.id.detail_store_business_hours_key_tv)
        TextView detail_store_business_hours_key_tv;
        @Bind(R.id.vDividerOpeningTime)
        View vDividerOpeningTime;
        @Bind(R.id.vDividerPhone)
        View vDividerPhone;

        @Bind(R.id.detail_avg_price_tv)
        TextView detail_avg_price_tv;

        ContactInfoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class RatingInfoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.rbRating)
        RatingBar rbRating;
        @Bind(R.id.tvRatingCount)
        TextView tvRatingCount;
        @Bind(R.id.tcvTags)
        TagCloudView tagCloudView;

        RatingInfoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class AdditionalInfoViewHolder extends RecyclerView.ViewHolder {
//
//        @Bind(R.id.rvAdditionalInfo)
//        RecyclerView rvInfoGrid;

        AdditionalInfoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class OpeningInfoViewHolder extends RecyclerView.ViewHolder {
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
//        @Bind(R.id.item_card_image_sdv)
//        SimpleDraweeView item_card_image_sdv;

        ImageViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.sdvAvatar)
        SimpleDraweeView sdvAvatar;
        @Bind(R.id.tvUsername)
        TextView tvUsername;
        @Bind(R.id.tvDate)
        TextView tvDate;
        @Bind(R.id.rlUserInfo)
        RelativeLayout rlUserInfo;
        @Bind(R.id.tvUserComment)
        TextView tvUserComment;
        @Bind(R.id.ivFrom)
        SimpleDraweeView ivFrom;
        @Bind(R.id.tvFrom)
        TextView tvFrom;

        CommentViewHolder(View view) {
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
        @Bind(R.id.desc)
        TextView tvName;
        @Bind(R.id.tag)
        TagCloudView tcvTag;
        @Bind(R.id.simpleDraweeView)
        SimpleDraweeView sdvImage;
        @Bind(R.id.cvRoot)
        CardView cardView;

        NearbyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public class AdditionalInfoAdapter extends RecyclerView.Adapter<AdditionalInfoAdapter.ViewHolder> {

        List<DetailModel.IconsEntity> mIconSet;

        public AdditionalInfoAdapter(List<DetailModel.IconsEntity> dataSet) {
            this.mIconSet = dataSet;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_additional_info, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            int id = mIconSet.get(position).getId();
            switch (id) {
                case 18:
                    holder.ivIcon.setImageResource(R.mipmap.ic_cash);
                    holder.tvItemName.setText(R.string.cash);
                    break;
                case 19:
                    holder.ivIcon.setImageResource(R.mipmap.ic_visacard);
                    holder.tvItemName.setText(R.string.visacard);
                    break;
                case 21:
                    holder.ivIcon.setImageResource(R.mipmap.ic_parking_slot);
                    holder.tvItemName.setText(R.string.parking_slot);
                    break;
                case 31:
                    holder.ivIcon.setImageResource(R.mipmap.ic_private_room);
                    holder.tvItemName.setText(R.string.private_room);
                    break;
                case 61:
                    holder.ivIcon.setImageResource(R.mipmap.ic_wechatpay);
                    holder.tvItemName.setText(R.string.wechatpay);
                    break;
                case 63:
                    holder.ivIcon.setImageResource(R.mipmap.ic_alipay);
                    holder.tvItemName.setText(R.string.alipay);
                    break;
                case 64:
                    holder.ivIcon.setImageResource(R.mipmap.ic_no_seat);
                    holder.tvItemName.setText(R.string.no_seat);
                    break;
                case 91:
                    holder.ivIcon.setImageResource(R.mipmap.ic_deliver);
                    holder.tvItemName.setText(R.string.deliver);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return null != mIconSet ? mIconSet.size() : 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.ivIcon)
            ImageView ivIcon;
            @Bind(R.id.tvItemName)
            TextView tvItemName;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }


}
