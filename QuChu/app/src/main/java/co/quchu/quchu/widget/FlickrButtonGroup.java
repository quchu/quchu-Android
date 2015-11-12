package co.quchu.quchu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.Bind;
import co.quchu.quchu.R;

/**
 * FlickrButtonGroup
 * User: Chenhs
 * Date: 2015-11-12
 * 控件为三个button 左中右
 * 点击选中 左右两个button时 View动画移动到选中位置
 * 例如：当前选中左边，当点击右边时 view移动到右边，右边button的background切换为选中是的资源
 * 点击选中 中间按钮 则中间imageview 由大变小动画 切换选中图片而后由小变大
 * 动画效果：位移动画 、伸缩动画
 */
public class FlickrButtonGroup extends RelativeLayout {
    Context context;
    @Bind(R.id.widget_switch_selected_view)
    View widgetSwitchSelectedView;
    @Bind(R.id.widget_switch_hot_btn)
    ImageButton widgetSwitchHotBtn;
    @Bind(R.id.widget_switch_new_btn)
    ImageButton widgetSwitchNewBtn;
    @Bind(R.id.widget_switch_center_iv)
    ImageView widgetSwitchCenterIv;
    /**
     * 左边按钮选中状态
     * true==选中  false=未选中
     */
    private boolean isHotSelected = true;
    /**
     * 右边按钮选中状态
     */
    private boolean isNewSelected = false;
    private int isCenterSelected = 0x00;

    public FlickrButtonGroup(Context context) {
        this(context, null);
    }

    public FlickrButtonGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlickrButtonGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.widget_switch_buttons, this, true);
        widgetSwitchSelectedView = findViewById(R.id.widget_switch_selected_view);
        widgetSwitchHotBtn = (ImageButton) findViewById(R.id.widget_switch_hot_btn);
        widgetSwitchNewBtn = (ImageButton) findViewById(R.id.widget_switch_new_btn);
        widgetSwitchCenterIv = (ImageView) findViewById(R.id.widget_switch_center_iv);

    }


}
