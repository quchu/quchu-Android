package co.quchu.quchu.widget;

import android.graphics.Canvas;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import co.quchu.quchu.R;
import co.quchu.quchu.view.adapter.AdapterBase;

/**
 * Created by no21 on 2016/5/19.
 * email:437943145@qq.com
 * desc :
 */
public class ItemTouchCallback extends ItemTouchHelper.Callback {

    private AdapterBase recyclerView;
    private View clickDeleteView;
    private int position;

    public ItemTouchCallback(AdapterBase recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getItemViewType() != AdapterBase.ITEM_VIEW_TYPE_FOOTER)
            return makeMovementFlags(0, ItemTouchHelper.START);
        else
            return makeMovementFlags(0, 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        recyclerView.removeItem(viewHolder.getAdapterPosition());

    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (clickDeleteView != null && viewHolder != null && position != viewHolder.getAdapterPosition()) {
            clickDeleteView.setVisibility(View.GONE);
        }
        if (viewHolder != null) {
            position = viewHolder.getAdapterPosition();
            clickDeleteView = viewHolder.itemView.findViewById(R.id.delete);
            clickDeleteView.setTranslationX(viewHolder.itemView.getWidth());
            clickDeleteView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//        if (isCurrentlyActive && dX < 0) {
//            int width = viewHolder.itemView.getWidth();
//            ViewGroup.LayoutParams layoutParams = viewHolder.itemView.getLayoutParams();
//
//            layoutParams.width = (int) (-dX + width);
//            viewHolder.itemView.setLayoutParams(layoutParams);

        if (clickDeleteView != null) {
//            final float dir = Math.signum(dX);
//            if (dir == 0) {
//                clickDeleteView.setTranslationX(-clickDeleteView.getWidth());
//            } else if (dX < 0) {
//                final float overlayOffset = dX - dir * viewHolder.itemView.getWidth();
//                clickDeleteView.setTranslationX(overlayOffset);
//            }
            if (dX < 0) {
                ViewCompat.setTranslationX(clickDeleteView, dX + clickDeleteView.getWidth());
            }
        }
//        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//        }
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();
    }
}
