package co.quchu.quchu.view.adapter;

import android.view.View;

/**
 * ItemClickListener
 * User: Chenhs
 * Date: 2015-11-11
 * RecyclerView item click listener
 *  列表条目点击事件
 */
public interface ItemClickListener {
    /**
     * 点击事件回调
     * @param v  view
     * @param position
     */
    void itemClick(View v ,int position);
}
