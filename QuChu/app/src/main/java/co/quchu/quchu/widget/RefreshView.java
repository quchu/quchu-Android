package co.quchu.quchu.widget;

import android.view.View;
import android.widget.TextView;

import co.quchu.quchu.R;
import co.quchu.quchu.utils.LogUtils;

/**
 * Created by no21 on 2016/4/26.
 * email:437943145@qq.com
 * desc :
 */
public class RefreshView {

    private TextView textView;
    boolean refreshing;

    int getHeadViewId() {
        return R.layout.layout_headview;
    }

    void headViewInflateFinish(View headView) {
        textView = (TextView) headView.findViewById(R.id.textView);
    }


    /**
     * 下拉刷新的高度比例 0-1
     */
    float getHeadPullRatio() {
        return .5f;
    }

    /**
     * @param progress 下拉的偏移量
     */
    void headViewProgress(int progress) {
        LogUtils.e("刷新view progress:" + progress);
        if (refreshing) {
            textView.setText("刷新中!!");
        } else if (progress >= 50 && !refreshing) {
            textView.setText("释放刷新~~");
        } else if (progress < 50 && !refreshing) {
            textView.setText("下拉刷新");
        }
    }

    void onStartRefresh() {
        textView.setText("刷新中!!");
        refreshing = true;
    }

    void onStopRefresh() {
        textView.setText("刷新中!!");
        refreshing = false;
    }
}
