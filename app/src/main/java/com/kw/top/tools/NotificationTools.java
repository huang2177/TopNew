package com.kw.top.tools;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.kw.top.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * author  ： zy
 * date    ： 2018/7/14
 * des     ：
 */

public class NotificationTools {

//    private static NotificationManager manager;
//    private static Notification friendNotification;
//    private static Notification groupNotification;
//    private static NotificationManager groupManager;
//
//
//    public static void showFriendApplyNotification(Activity activity) {
//        if (friendNotification == null || null == manager) {
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(activity);
//            Intent intent = new Intent(activity, FriendApplyActivity.class);//点击之后进入MainActivity
//            PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            friendNotification = builder.setSmallIcon(R.mipmap.ic_launcher)//设置小图标
//                    .setContentTitle("通知")
//                    .setWhen(System.currentTimeMillis())//通知的时间
//                    .setAutoCancel(true)//点击后消失
//                    .setContentIntent(pendingIntent)//设置意图
//                    .setContentText("您有一条新的好友请求")
//                    .build();//创建通知对象完成
//            manager = (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);
//        }
//        manager.notify(4692, friendNotification);//显示
//    }

//    public static void showGroupNotification(Activity activity,String groupId){
//
//        if (groupNotification == null || null == manager) {
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(activity,CHANNEL_ID);
//            Intent intent = new Intent(activity, VipApplyActivity.class);//点击之后进入MainActivity
//            intent.putExtra("ID",groupId);
//            PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            groupNotification = builder.setSmallIcon(R.mipmap.ic_launcher)//设置小图标
//                    .setContentTitle("通知")
//                    .setWhen(System.currentTimeMillis())//通知的时间
//                    .setAutoCancel(true)//点击后消失
//                    .setContentIntent(pendingIntent)//设置意图
//                    .setContentText("您有一条新的群验证消息")
//                    .build();//创建通知对象完成
//            groupManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
//        }
//        groupManager.notify(1, groupNotification);//显示
//    }

    public static final String name = "channel_name_group";
    private static NotificationManager manager;
    public static final String GROUP_CHANNEL_ID = "1";
    public static final int GROUP_ID = 3;
    public static final String FRIEND_CHANNEL_ID = "2";
    public static final int FRIEND_ID = 2;
    public static final String NEWS_CHANNEL_ID = "3";
    public static final int NEWS_ID = 1;


    public static void showNotification(Context context, PendingIntent pendingIntent, String content, int id, String channel_id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createGroupNotificationChannel(context, channel_id);
            Notification notification = new Notification.Builder(context, channel_id)
                    .setContentTitle("通知")
                    .setContentText(content)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();
            getManager(context).notify(id, notification);
        } else {
            Notification notification = new NotificationCompat.Builder(context)
                    .setContentTitle("通知")
                    .setContentText(content)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();
            getManager(context).notify(id, notification);
        }
    }

    private static void createGroupNotificationChannel(Context activity, String channel_id) {
        NotificationChannel channel = new NotificationChannel(channel_id, name, NotificationManager.IMPORTANCE_HIGH);
        getManager(activity).createNotificationChannel(channel);
    }

    private static NotificationManager getManager(Context activity) {
        if (manager == null) {
            manager = (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public static void cancleNotification(int id) {
        if (manager != null)
            manager.cancel(id);
    }

}
