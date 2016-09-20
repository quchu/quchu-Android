package co.quchu.quchu.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.model.XiaoQModel;

/**
 * Created by mwb on 16/9/20.
 */
public class XiaoQAdapter extends AdapterBase<XiaoQModel,XiaoQAdapter.XiaoQViewHolder > {

  private final Context mContext;

  public XiaoQAdapter(Context context) {
    mContext = context;
  }

  @Override
  public void onBindView(XiaoQViewHolder holder, int position) {

  }

  @Override
  public RecyclerView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
    return new XiaoQViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_xiaoq, parent, false));
  }

  public class XiaoQViewHolder extends RecyclerView.ViewHolder {

    public XiaoQViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
