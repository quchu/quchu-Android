package co.quchu.quchu.view.activity;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

/**
 * Created by Nico on 16/10/27.
 */

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.MyViewHolder> {
  private List<String> mDatas;
  private LayoutInflater mInflater;

  public SimpleAdapter(List<String> mDatas, Context context) {
    this.mDatas = mDatas;
    this.mInflater = LayoutInflater.from(context);
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    /**
     * 此处inflate的时候 ,如果是mInflater.inflate(R.layout.listview_item, parent);会报如下错误
     *
     * The specified child already has a parent. You must call removeView() on the child's parent first
     *
     * 它说这个特定的child已经有一个parent了，你必须在这个parent中首先调用removeView()方法，才能继续你的内容。
     *
     * 这里很明显这个child是一个View，一个子（child）View必须依赖于父（parent）View，如果你要使用这个child，
     *
     * 则必须通过parent，而你如果就是硬想使用这个child，那么就得让这个child与parent脱离父子关系（即removeView（））
     */

    View convertView = mInflater.inflate(android.R.layout.simple_list_item_1, null);
    //如果想让item match_parent 必须setLayoutParams  在布局中match_parent不起作用,暂时不知道为什么,(有可能是mInflater.inflate(R.layout.listview_item, null);的原因)
    convertView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 72));//加了这一句话,可以match_parent了
    MyViewHolder viewHolder = new MyViewHolder(convertView);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    holder.tv.setText(mDatas.get(position));
    holder.tv.setTextColor(Color.BLACK);
  }

  @Override
  public int getItemCount() {
    return mDatas.size();
  }

  /**
   * RecyclerView的ViewHolder
   */
  public static class MyViewHolder extends RecyclerView.ViewHolder {
    TextView tv;

    public MyViewHolder(View itemView) {
      super(itemView);
      tv = (TextView) itemView.findViewById(android.R.id.text1);
    }

  }
}