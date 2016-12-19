package co.quchu.quchu.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;

/**
 * Created by mwb on 2016/12/19.
 */
public class DrawerItemAdapter extends RecyclerView.Adapter<DrawerItemAdapter.DrawerItemViewHolder> {

  private int[] imgResids = {R.drawable.ic_wodeshoucang, R.drawable.ic_gerenzhongxin,
      R.drawable.ic_xiaoxizhongxin, R.drawable.ic_yijianbangzhu,
      R.drawable.ic_wodeshezhi, R.drawable.ic_fenxiangapp};

  @Bind(R.id.drawerItemImg) ImageView mDrawerItemImg;
  @Bind(R.id.drawerTitleTv) TextView mDrawerTitleTv;
  @Bind(R.id.drawerUnReadTv) TextView mDrawerUnReadTv;

  @Override
  public DrawerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new DrawerItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drawer, parent, false));
  }

  @Override
  public void onBindViewHolder(DrawerItemViewHolder holder, int position) {

  }

  @Override
  public int getItemCount() {
    return 0;
  }

  public class DrawerItemViewHolder extends RecyclerView.ViewHolder {

    public DrawerItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
