package com.kw.top.app;

import android.app.Activity;

import java.util.Stack;

/**
 * author: 正义
 * date  : 2018/4/13
 * desc  : activity栈
 */

public class AppManager {

    private static Stack<Activity> activityStack;
    private volatile static AppManager instance;

    public AppManager(){}

    public static AppManager getAppManager(){
        if (instance == null){
            synchronized (AppManager.class){
                if (instance == null){
                    instance = new AppManager();
                }
            }
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     * @param activity
     */
    public void addActivity(Activity activity){
        if (activityStack == null){
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前 Activity(堆栈中最后一个加入的)
     * @return
     */
    public Activity getCurrentActivity(){
        if (activityStack.size() == 0){
            return null;
        }
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束制定 Activity
     * @param activity
     */
    public void finishActivity(Activity activity){
        if (null != activity && !activity.isFinishing()){
            activityStack.remove(activity);
            activity.finish();;
            activity = null;
        }
    }

    /**
     * 移除指定 Activity
     * @param activity
     */
    public void removeActivity(Activity activity){
        if (null != activity){
            activityStack.remove(activity);
        }
    }

    /**
     * 结束指定类名的Activity
     * @param cls
     */
    public void finishActivity(Class<?> cls){
        for (Activity activity : activityStack){
            if (activity.getClass().equals(cls)){
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有的Activity
     */
    public void finishAllActivity(){
        for (int i=0, size = activityStack.size(); i < size; i++){
            if (null != activityStack.get(i)){
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    public void AppExit(){
        try {
            finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }


}
