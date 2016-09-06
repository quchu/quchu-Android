package co.quchu.quchu.view.fragment;

import android.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Nico on 16/8/30.
 */
public class DialogHangoutUserInfo extends DialogFragment {

  @Bind(R.id.sdv) SimpleDraweeView sdv;
  @Bind(R.id.tvUsername) TextView tvUsername;
  @Bind(R.id.tvTag) TextView tvTag;
  @Bind(R.id.tvLabel) TextView tvLabel;
  @Bind(R.id.tvSimilar) TextView tvSimilar;
  @Bind(R.id.ivConfirm) ImageView ivConfirm;
  private int mPid;


  private int mUserId;
  private String mBackground;
  private String mTagName;
  private int mSimilarity;
  private String mUserName;
  OnUserInvitedListener mListener;


  public static String BUNDLE_KEY_PID = "BUNDLE_KEY_PID";
  public static String BUNDLE_KEY_UID = "BUNDLE_KEY_UID";
  public static String BUNDLE_KEY_SIMILARITY = "BUNDLE_KEY_SIMILARITY";
  public static String BUNDLE_KEY_BACKGROUND = "BUNDLE_KEY_BACKGROUND";
  public static String BUNDLE_KEY_TAG_NAME = "BUNDLE_KEY_TAG_NAME";
  public static String BUNDLE_KEY_USER_NAME = "BUNDLE_KEY_USER_NAME";


  public static DialogHangoutUserInfo getInstance(int pid, int userId, String background, String tagName,
      int similarity,String userName) {

    DialogHangoutUserInfo d = new DialogHangoutUserInfo();
    Bundle bundle = new Bundle();
    bundle.putInt(BUNDLE_KEY_PID,pid);
    bundle.putInt(BUNDLE_KEY_UID,userId);
    bundle.putInt(BUNDLE_KEY_SIMILARITY,similarity);
    bundle.putString(BUNDLE_KEY_BACKGROUND,background);
    bundle.putString(BUNDLE_KEY_TAG_NAME,tagName);
    bundle.putString(BUNDLE_KEY_USER_NAME,userName);
    d.setArguments(bundle);
    return d;
  }


  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    mPid = getArguments().getInt(BUNDLE_KEY_PID);
    mUserId = getArguments().getInt(BUNDLE_KEY_UID);
    mSimilarity = getArguments().getInt(BUNDLE_KEY_SIMILARITY);
    mTagName = getArguments().getString(BUNDLE_KEY_TAG_NAME);
    mBackground = getArguments().getString(BUNDLE_KEY_BACKGROUND);
    mUserName = getArguments().getString(BUNDLE_KEY_USER_NAME);

    View view = inflater.inflate(R.layout.dialog_hangout_user_info, container, false);
    ButterKnife.bind(this, view);

    sdv.setImageURI(Uri.parse(mBackground));
    tvUsername.setText(mUserName);
    tvSimilar.setText(mSimilarity+"%");
    tvTag.setText(mTagName);
    ivConfirm.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (null!=mListener){
          mListener.onInvite(mUserId,mUserName);
          dismiss();
        }
      }
    });
    return view;
  }


  public void setOnConfirmListener(OnUserInvitedListener pListener){
    mListener=  pListener;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  public interface OnUserInvitedListener{
    void onInvite(int uid,String userName);
  }
}
