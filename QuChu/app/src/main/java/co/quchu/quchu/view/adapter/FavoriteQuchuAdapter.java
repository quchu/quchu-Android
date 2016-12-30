package co.quchu.quchu.view.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.FavoriteBean;
import co.quchu.quchu.widget.TagCloudView;

/**
 * Created by no21 on 2016/4/5.
 * email:437943145@qq.com
 * desc :
 */
public class FavoriteQuchuAdapter extends AdapterBase<FavoriteBean.ResultBean, FavoriteQuchuAdapter.ViewHold> {

  @Override
  protected String getNullDataHint() {
    return "你还没有收藏趣处";
  }

  @Override
  public void onBindView(final ViewHold holder, final int position) {
    final FavoriteBean.ResultBean bean = data.get(position);
    holder.name.setText(bean.getName());
    holder.simpleDraweeView.setImageURI(Uri.parse(bean.getCover()));
    holder.tag.setTags(bean.getTagsString());
    String info = "";
    if (null != bean.getDescribe() && bean.getDescribe().indexOf(",") != -1) {
      info = bean.getDescribe().replace(",", "-");
    } else {
      info = bean.getDescribe();
    }

    String distance = "";

    holder.address.setText(info + distance);
    holder.address.setSelected(true);
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mListener != null) {
          mListener.onItemClick(holder, bean, v.getId(), position);
        }
      }
    });

    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        if (mListener != null) {
          mListener.onItemLongClick(holder, bean, v.getId(), position);
        }
        return true;
      }
    });
  }

  @Override
  public ViewHold onCreateView(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_favorite_quchu, parent, false);
    return new ViewHold(view);
  }

  class ViewHold extends RecyclerView.ViewHolder {

    @Bind(R.id.simpleDraweeView) SimpleDraweeView simpleDraweeView;
    @Bind(R.id.desc) TextView name;
    @Bind(R.id.tag) TagCloudView tag;
    @Bind(R.id.address) TextView address;

    public ViewHold(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      address.setVisibility(View.VISIBLE);
    }
  }

  private OnItemClickListener mListener;

  public void setOnItemClickListener(OnItemClickListener listener) {
    mListener = listener;
  }

  public interface OnItemClickListener {
    void onItemClick(RecyclerView.ViewHolder holder, FavoriteBean.ResultBean item, int type, @Deprecated int position);

    void onItemLongClick(RecyclerView.ViewHolder holder, FavoriteBean.ResultBean item, int type, @Deprecated int position);
  }
}
