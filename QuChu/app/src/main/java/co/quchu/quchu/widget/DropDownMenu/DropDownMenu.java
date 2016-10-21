package co.quchu.quchu.widget.DropDownMenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import co.quchu.quchu.R;

/**
 * Created by dongjunkun on 2015/6/17.
 */
public class DropDownMenu extends LinearLayout {

  //顶部菜单布局
  private LinearLayout tabMenuView;
  //底部容器，包含popupMenuViews，maskView
  private FrameLayout containerView;
  //弹出菜单父布局
  private FrameLayout popupMenuViews;
  //遮罩半透明View，点击可关闭DropDownMenu
  private View maskView;
  //tabMenuView里面选中的tab位置，-1表示未选中
  private int curClickTabPosition = -1;

  //分割线颜色
  private int dividerColor = 0xffcccccc;
  //tab选中颜色
  private int textSelectedColor = 0xff890c85;
  //tab未选中颜色
  private int textUnselectedColor = 0xff111111;
  //遮罩颜色
  private int maskColor = 0x88888888;
  //tab字体大小
  private int menuTextSize = 14;

  //tab选中图标
  private int menuSelectedIcon;
  //tab未选中图标
  private int menuUnselectedIcon;
  private DropContentView mDropContentView;


  public DropDownMenu(Context context) {
    super(context, null);
  }

  public DropDownMenu(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public DropDownMenu(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    setOrientation(VERTICAL);

    //为DropDownMenu添加自定义属性
    int menuBackgroundColor = 0xffffffff;
    int underlineColor = 0xffcccccc;
    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DropDownMenu);
    underlineColor = a.getColor(R.styleable.DropDownMenu_ddunderlineColor, underlineColor);
    dividerColor = a.getColor(R.styleable.DropDownMenu_dddividerColor, dividerColor);
    textSelectedColor = a.getColor(R.styleable.DropDownMenu_ddtextSelectedColor, textSelectedColor);
    textUnselectedColor = a.getColor(R.styleable.DropDownMenu_ddtextUnselectedColor, textUnselectedColor);
    menuBackgroundColor = a.getColor(R.styleable.DropDownMenu_ddmenuBackgroundColor, menuBackgroundColor);
    maskColor = a.getColor(R.styleable.DropDownMenu_ddmaskColor, maskColor);
    menuTextSize = a.getDimensionPixelSize(R.styleable.DropDownMenu_ddmenuTextSize, menuTextSize);
    menuSelectedIcon = a.getResourceId(R.styleable.DropDownMenu_ddmenuSelectedIcon, menuSelectedIcon);
    menuUnselectedIcon = a.getResourceId(R.styleable.DropDownMenu_ddmenuUnselectedIcon, menuUnselectedIcon);
    a.recycle();

    //初始化tabMenuView并添加到tabMenuView
    tabMenuView = new LinearLayout(context);
    LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    tabMenuView.setOrientation(HORIZONTAL);
    tabMenuView.setBackgroundColor(menuBackgroundColor);
    tabMenuView.setLayoutParams(params);
    addView(tabMenuView, 0);

    //为tabMenuView添加下划线
    View underLine = new View(getContext());
    underLine.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(0.5f)));
    underLine.setBackgroundColor(underlineColor);
    addView(underLine, 1);

    //初始化containerView并将其添加到DropDownMenu
    containerView = new FrameLayout(context);
    containerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    addView(containerView, 2);

    //添加内容
    mDropContentView = new DropContentView(getContext());
    containerView.addView(mDropContentView);
    mDropContentView.setVisibility(GONE);
  }

  /**
   * 添加 tab 内容
   */
  public void addTab(@NonNull List<String> tabs) {
    for (int i = 0; i < tabs.size(); i++) {
      addTab(tabs, i);
    }
  }

  private void addTab(List<String> tabTexts, int i) {
    final DropTabView tabView = new DropTabView(getContext());
    tabView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
    ((TextView) tabView.getChildAt(0)).setText(tabTexts.get(i));
    tabView.setPadding(dpToPx(5), dpToPx(12), dpToPx(5), dpToPx(12));

    //添加点击事件
    tabView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        switchMenu(tabView);
      }
    });
    tabMenuView.addView(tabView);

    //添加分割线
    if (i < tabTexts.size() - 1) {
      View view = new View(getContext());
      view.setLayoutParams(new LayoutParams(dpToPx(0.5f), ViewGroup.LayoutParams.MATCH_PARENT));
      view.setBackgroundColor(dividerColor);
      tabMenuView.addView(view);
    }
  }

  /**
   * 改变 tab 文字
   */
  public void setTabText(String text) {
    if (curClickTabPosition != -1) {
      ((TextView) tabMenuView.getChildAt(curClickTabPosition)).setText(text);
    }
  }

  /**
   * 是否处于可见状态
   */
  public boolean isShowing() {
    return curClickTabPosition != -1;
  }

  /**
   * 切换菜单
   */
  private void switchMenu(DropTabView target) {
    for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
      if (target == tabMenuView.getChildAt(i)) {
        if (curClickTabPosition == i) {
          closeMenu();

        } else {
          showMenu(i);

          changeTabStatus(i, true);
        }

      } else {
        changeTabStatus(i, false);
      }
    }
  }

  /**
   * 关闭菜单
   */
  private void closeMenu() {
    if (curClickTabPosition != -1) {
      changeTabStatus(curClickTabPosition, false);

      mDropContentView.setVisibility(GONE);
      mDropContentView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_out));
      curClickTabPosition = -1;
    }
  }

  /**
   * 显示菜单
   */
  private void showMenu(int position) {
    mDropContentView.setVisibility(View.VISIBLE);
    mDropContentView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_in));
    curClickTabPosition = position;
  }

  /**
   * 改变 Tab 选中状态
   * <p>
   * true-选中,false-未选中
   */
  private void changeTabStatus(int position, boolean isSelected) {
    DropTabView tabView = (DropTabView) tabMenuView.getChildAt(position);
    TextView textView = ((TextView) tabView.getChildAt(0));
    ImageView imageView = ((ImageView) tabView.getChildAt(1));
    if (isSelected) {
      textView.setTextColor(textSelectedColor);
      imageView.setImageResource(R.mipmap.ic_down);

    } else {
      textView.setTextColor(textUnselectedColor);
      imageView.setImageResource(R.mipmap.ic_down);
    }
  }

  private int dpToPx(float value) {
    DisplayMetrics dm = getResources().getDisplayMetrics();
    return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
  }
}
