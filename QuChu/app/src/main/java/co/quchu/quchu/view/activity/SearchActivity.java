package co.quchu.quchu.view.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.widget.VerTextView;

/**
 * SearchActivity
 * User: Chenhs
 * Date: 2015-12-04
 */
public class SearchActivity extends BaseActivity {
    @Bind(R.id.search_input_et)
    EditText searchInputEt;
    @Bind(R.id.search_button_rl)
    RelativeLayout searchButtonRl;
    @Bind(R.id.search_title_bar_ll)
    LinearLayout searchTitleBarLl;
    @Bind(R.id.search_history_rv)
    RecyclerView searchHistoryRv;
    @Bind(R.id.search_history_clear_rl)
    RelativeLayout searchHistoryClearRl;
    @Bind(R.id.search_history_fl)
    FrameLayout searchHistoryFl;
    @Bind(R.id.search_result_rv)
    RecyclerView searchResultRv;
    @Bind(R.id.search_result_fl)
    FrameLayout searchResultFl;
    @Bind(R.id.search_result_isnull_vtv)
    VerTextView searchResultIsnullVtv;
    @Bind(R.id.search_result_isnull_fl)
    FrameLayout searchResultIsnullFl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        showNoneResultFrame();
    }

    private void showNoneResultFrame() {
        searchResultIsnullFl.setVisibility(View.VISIBLE);
        searchResultIsnullVtv.setFontSize(60);             // 设定字体尺寸
        searchResultIsnullVtv.setIsOpenUnderLine(false);     // 设定开启下划线
        /*searchResultIsnullVtv.setUnderLineColor(Color.RED); // 设定下划线颜色
        searchResultIsnullVtv.setUnderLineWidth(3);         // 设定下划线宽度*/
        searchResultIsnullVtv.setUnderLineSpacing(30);      // 设定下划线到字的间距
        searchResultIsnullVtv.setTextStartAlign(VerTextView.RIGHT); // 从右侧或左侧开始排版
        searchResultIsnullVtv.setTextColor(getResources().getColor(R.color.load_progress_gray));           // 设定字体颜色
        searchResultIsnullVtv.setText("啦啦：啦啊 \n呼呼 \n哈");
    }
}
