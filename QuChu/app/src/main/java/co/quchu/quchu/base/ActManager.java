package co.quchu.quchu.base;

import android.app.Activity;

import java.util.LinkedList;


public class ActManager {

    private static final String TAG = "ActManager";

    private static LinkedList<Activity> activityStack;
    private static ActManager instance;

    private ActManager() {
    }

    public static ActManager getAppManager() {
        if (instance == null) {
            instance = new ActManager();
        }
        return instance;
    }

    /**
     * 压栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new LinkedList<>();
        }
        activityStack.add(activity);
    }

    /**
     * 当前Activity
     */
    public Activity currentActivity() {
        Activity activity = null;
        if (activityStack == null) {
            return null;
        }
        if (activityStack.size() > 0) {
            activity = activityStack.getLast();
        }
        return activity;
    }


    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * 依据类名退出
     *
     * @param cls Activity的类名
     */
    public void finishActivity(Class<? extends BaseActivity> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 结束所有Activity，但保留最后一个
     */
    public void finishActivitiesAndKeepLastOne() {
        for (int i = 1, size = activityStack.size(); i < size; i++) {
            activityStack.get(1).finish();
            activityStack.remove(1);
        }
    }

    public void finishLastOne() {
        if (activityStack.size() > 0) {
            activityStack.get(0).finish();
            activityStack.remove(0);
        }
    }


    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            //finish
            finishAllActivity();
            //    System.exit(0);
            //取消消息
//			NotificationManager mNotificationManager = (NotificationManager)AppContext.mContext.getSystemService(Context.NOTIFICATION_SERVICE) ;
//			mNotificationManager.cancelAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean hasActRunning() {
        return activityStack != null && activityStack.size() > 0;
    }

//    /**
//     * 清楚 menusactivity 之后入栈
//     */
//    public void Back2MenusAct() {
//        boolean isMenu = false;
//        for (Activity activity : activityStack) {
//            if (isMenu) {
//                activity.finish();
//            }
//            if (activity instanceof MenusActivity) {
//                isMenu = true;
//            }
//        }
//
//        if (!isMenu) {
//            currentActivity().startActivity(new Intent(currentActivity(), MenusActivity.class));
//        }
//    }

}
