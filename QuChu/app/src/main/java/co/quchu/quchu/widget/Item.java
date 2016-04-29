package co.quchu.quchu.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by no21 on 2016/4/29.
 * email:437943145@qq.com
 * desc :
 */
public class Item extends RecyclerView.ItemDecoration {
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        Paint p = new Paint();

        p.setColor(Color.RED);
        Rect rect = new Rect();

        rect.set(0, 0, 800, 800);
        c.drawRect(rect, p);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        Paint p = new Paint();

        p.setColor(Color.RED);
        Rect rect = new Rect();

        rect.set(0, 0, 800, 800);
        c.drawRect(rect, p);
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        Paint p = new Paint();

        p.setColor(Color.RED);
        Rect rect = new Rect();

        rect.set(0, 0, 800, 800);

        super.getItemOffsets(outRect, view, parent, state);
    }
}
