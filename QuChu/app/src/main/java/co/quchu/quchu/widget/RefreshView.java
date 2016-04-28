package co.quchu.quchu.widget;

import co.quchu.quchu.R;
import co.quchu.quchu.utils.LogUtils;

/**
 * Created by no21 on 2016/4/26.
 * email:437943145@qq.com
 * desc :
 */
public class RefreshView {

    int getHeadViewId() {
        return R.layout.layout_headview;
    }

    /**
     * 下拉刷新的高度比例 0-1
     */
    float getHeadPullRatio() {
        return .5f;
    }

    void headViewProgress(int progress) {
        LogUtils.e("刷新view progress:" + progress);

    }
}
