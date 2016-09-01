package co.quchu.quchu.view.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.HangoutUserModel;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.List;

/**
 * Created by Nico on 16/8/29.
 */
public class InviteHangoutUsersAdapter
    extends RecyclerView.Adapter<InviteHangoutUsersAdapter.ViewHolder> {


  private CommonItemClickListener mListener;
  private List<HangoutUserModel> mUsers;


  public void setOnItemClickListener(CommonItemClickListener pListener){
    mListener = pListener;
  }

  public InviteHangoutUsersAdapter(List<HangoutUserModel> pUsers) {
    this.mUsers = pUsers;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invite_user, parent, false));
  }

  @Override public void onBindViewHolder(ViewHolder holder, final int position) {
    Uri uri = Uri.parse(mUsers.get(position).getPhoto());
    holder.tvUserName.setText(mUsers.get(position).getName());
    holder.sdvAvatar.setImageURI(uri);
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (null!=mListener){
          mListener.onItemClick(view,position);
        }
      }
    });
    holder.ivGender.setImageResource(mUsers.get(position).getGender()==0?R.mipmap.ic_women:R.mipmap.ic_man);
  }

  @Override public int getItemCount() {
    return null != mUsers ? mUsers.size() : 0;
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.sdvAvatar) SimpleDraweeView sdvAvatar;
    @Bind(R.id.ivGender) ImageView ivGender;
    @Bind(R.id.tvUserName) TextView tvUserName;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}