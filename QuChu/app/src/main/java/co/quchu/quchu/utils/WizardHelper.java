package co.quchu.quchu.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;

import java.util.HashMap;

import co.quchu.quchu.R;
import co.quchu.quchu.base.AppContext;

/**
 * 引导页辅助类
 * <p/>
 * Created by mwb on 16/9/21.
 */
public class WizardHelper {

  public enum Wizard {
    //我的场景
    my_scene,

    //场景详情
    scene_detail
  }

  // 保存的SharedPreferences名称
  public static final String SETTING_WIZARDS = "setting_wizards";

  public static final String TAG_WIZARD_MY_SCENE = "tag_wizard_my_scene";
  public static final String TAG_WIZARD_SCENE_DETAIL = "tag_wizard_scene_detail";

  public static final int BG_COLOR = 0xa5000000;

  private static HashMap<Wizard, WizardEntity> mWizardViewMap = new HashMap<Wizard, WizardEntity>(Wizard.values().length);

  public static void showWizard(final Activity activity, final Wizard wizard, final OnWizardListener listener) {
    if (activity != null && !isWizardFinished(wizard)) {
      // 设置WizardFlag（以后不再显示）
      WizardHelper.finishWizardFlag(wizard);

      activity.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          new Handler() {

            @Override
            public void handleMessage(Message msg) {
              executeWizardRunnable(activity, wizard, listener);
            }
          }.sendEmptyMessageDelayed(0, 200);
        }
      });
    }
  }

  /**
   * 隐藏引导内容
   */
  public static void hideWizard(final Wizard wizard) {
    if (wizard != null && Thread.currentThread() == Looper.getMainLooper().getThread()) {
      final WizardEntity entiry = mWizardViewMap.get(wizard);
      if (entiry != null && !entiry.isEmpty()) {
        entiry.activity.runOnUiThread(new Runnable() {

          @Override
          public void run() {
            if (entiry != null && !entiry.isEmpty()) {
              try {
                entiry.wondow.dismiss();
              } catch (Exception e) {
                // do nothing
              }

              if (entiry.hasListener()) {
                entiry.listener.onWizardHided(wizard);
              }
            }
          }
        });
      }
    }
  }

  /**
   * 相关事件绑定
   */
  private static void setWizardViewListener(final Wizard wizard, View wizardView) {
    if (wizardView != null && wizard != null) {
      wizardView.setFocusable(true);
      wizardView.setFocusableInTouchMode(true);
      wizardView.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
          hideWizard(wizard);
        }
      });

      wizardView.setOnKeyListener(new View.OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
          if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
            hideWizard(wizard);
            return true;
          }
          return false;
        }
      });
    }
  }

  private static void executeWizardRunnable(Activity activity, Wizard wizard, OnWizardListener listener) {
    View wizardView = null;
    switch (wizard) {
      case my_scene:
        wizardView = createWizardMySceneView(activity);
        break;

      case scene_detail:
        wizardView = createWizardSceneDetailView(activity);
        break;
    }

    if (wizardView != null) {
      // 绑定监听
      setWizardViewListener(wizard, wizardView);
      // 显示引导页
      mWizardViewMap.put(wizard, new WizardEntity(activity, wizard, createPopupWindow(activity, wizardView, wizard), listener));
    }
  }

  /**
   * 我的场景
   */
  private static View createWizardMySceneView(Activity activity) {
    FrameLayout frameLayout = new FrameLayout(activity);
    frameLayout.setBackgroundColor(BG_COLOR);
    frameLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    ImageView imageView = new ImageView(activity);
    imageView.setImageResource(R.mipmap.ic_wizard_changan);
    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.RIGHT | Gravity.CENTER_VERTICAL);
    layoutParams.topMargin = dip2px(activity, 40);
    layoutParams.rightMargin = dip2px(activity, 40);

    frameLayout.addView(imageView, layoutParams);

    return frameLayout;
  }

  /**
   * 场景详情
   */
  private static View createWizardSceneDetailView(Activity activity) {
    View view = null;

    View likeBtn = activity.findViewById(R.id.likeFab);
    if (likeBtn != null) {

      Point point = getShape(R.mipmap.ic_wizard_favorite);

      FrameLayout frameLayout = new FrameLayout(activity);
      frameLayout.setBackgroundColor(BG_COLOR);
      frameLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

      ImageView imageView = new ImageView(activity);
      imageView.setImageResource(R.mipmap.ic_wizard_favorite);

      FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.LEFT | Gravity.TOP);
      layoutParams.leftMargin = likeBtn.getLeft() - point.x + dip2px(activity, 10);
      layoutParams.topMargin = likeBtn.getTop() + (likeBtn.getMeasuredHeight() / 2 - point.y / 2);
      imageView.setLayoutParams(layoutParams);

      frameLayout.addView(imageView);

      view = frameLayout;
    }

    return view;
  }

  /**
   * 创建和显示PopupWindow对话框
   */
  private static PopupWindow createPopupWindow(Activity activity, View contentView, final Wizard wizard) {
    PopupWindow popWin = null;
    if (contentView != null && activity != null && activity.getWindow().getDecorView().getWindowToken() != null) {
      popWin = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
      popWin.setBackgroundDrawable(new ColorDrawable(0));
      popWin.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
      popWin.setOnDismissListener(new PopupWindow.OnDismissListener() {

        @Override
        public void onDismiss() {
          if (wizard != null && Thread.currentThread() == Looper.getMainLooper().getThread()) {
            WizardEntity entiry = mWizardViewMap.remove(wizard);
            if (entiry != null && !entiry.isEmpty()) {
              try {
                // 可以做些释放
              } catch (Exception e) {
                // do nothing
              }
              entiry = null;
            }
          }
        }
      });
    }

    return popWin;
  }

  /**
   * 从SharedPreferences中获取是否显示引导内容
   */
  public static boolean isWizardFinished(Wizard wizard) {
    boolean isFinished = false;

    if (wizard != null) {
      SharedPreferences prefs = AppContext.mContext.getSharedPreferences(SETTING_WIZARDS, Context.MODE_PRIVATE);
      switch (wizard) {
        case my_scene:
          isFinished = prefs.getBoolean(TAG_WIZARD_MY_SCENE, false);
          break;

        case scene_detail:
          isFinished = prefs.getBoolean(TAG_WIZARD_SCENE_DETAIL, false);
          break;

      }
    }

    return isFinished;
  }

  /**
   * 引导内容显示后就将SharedPreferences中的状态更改
   */
  public static void finishWizardFlag(Wizard wizard) {
    SharedPreferences.Editor editor = AppContext.mContext.getSharedPreferences(SETTING_WIZARDS, Context.MODE_PRIVATE).edit();
    switch (wizard) {
      case my_scene:
        editor.putBoolean(TAG_WIZARD_MY_SCENE, true);
        break;

      case scene_detail:
        editor.putBoolean(TAG_WIZARD_SCENE_DETAIL, true);
        break;
    }
    editor.commit();
  }

  /**
   * 模型实体
   */
  public static class WizardEntity {
    public Activity activity;
    public Wizard wizard;
    public PopupWindow wondow;
    public OnWizardListener listener;

    public WizardEntity(Activity activity, Wizard wizard, PopupWindow wondow, OnWizardListener listener) {
      this.activity = activity;
      this.wizard = wizard;
      this.wondow = wondow;
      this.listener = listener;
    }

    public boolean isEmpty() {
      return activity == null || wizard == null || wondow == null;
    }

    public boolean hasListener() {
      return listener != null;
    }
  }

  /**
   * 引导内容隐藏回调
   */
  public interface OnWizardListener {
    void onWizardHided(Wizard w);
  }

  /**
   * dp转换成px
   */
  public static int dip2px(Context context, float dpValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }

  /**
   * 获取资源图片的宽高
   */
  public static Point getShape(int resId) {
    Point point = new Point();
    Drawable drawable = AppContext.mContext.getResources().getDrawable(resId);
    point.x = drawable.getIntrinsicWidth();
    point.y = drawable.getIntrinsicHeight();
    return point;
  }

  /**
   * 获取状态栏的高度
   */
  public static int getStatusBarHeight(Activity activity) {
    // 状态栏的高度
    Rect frame = new Rect();
    activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
    return frame.top;
  }

  /**
   * 获取ActionBar的高度
   */
  public static int getActionBarHeight(Activity activity) {
    return dip2px(activity, 48);
  }
}
