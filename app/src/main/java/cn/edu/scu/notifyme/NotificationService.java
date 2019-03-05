package cn.edu.scu.notifyme;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.blankj.utilcode.util.LogUtils;

import androidx.annotation.Nullable;
import cn.edu.scu.notifyme.model.Message;

/**
 * NotificationService
 * 实现消息通知，消除通知即视为已读
 * 静态函数newMessage为简化Service调用的函数
 */
public class NotificationService extends Service {

    private static final int NOTIFICATION_ID = 20001;

    private NotificationManager notificationManager;

    private static int unreadNotificationCount;
    private PendingIntent dismissIntent;

    public static class NotificationDismissedBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            unreadNotificationCount = 0;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new NotificationServiceBinder();
    }

    private class NotificationServiceBinder extends Binder {
        public NotificationService getService() {
            return NotificationService.this;
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        this.notificationManager = (NotificationManager) getSystemService(
                this.NOTIFICATION_SERVICE);
        this.dismissIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(),
                0,
                new Intent(this,
                        NotificationDismissedBroadcastReceiver.class),
                0);
        this.unreadNotificationCount = 0;

        LogUtils.d("started");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("destroyed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Send notification every time startService() is called
        Message msg = intent.getParcelableExtra("message");

        unreadNotificationCount++;
        if (this.unreadNotificationCount <= 1) {
            pushNotificationOnOneMessage(msg);
        } else {
            pushNotificationOnMultipleMessages(this.unreadNotificationCount);
        }

        return START_NOT_STICKY;
    }

    private void pushNotificationOnOneMessage(Message msg) {
        //TODO: 引导至正确的网页链接
        PendingIntent contentIntent = PendingIntent.getActivity(
                this,
                0,
                new Intent(this, RulesActivity.class),
                0);

        Notification notification =
                new Notification.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(msg.getTitle())
                        .setContentText(msg.getContent())
                        .setContentIntent(contentIntent)
                        .setDeleteIntent(this.dismissIntent)
                        .build();

        this.notificationManager.notify(this.NOTIFICATION_ID, notification);
    }

    private void pushNotificationOnMultipleMessages(int messageCount) {
        //TODO: 引导至正确的Activity
        PendingIntent contentIntent = PendingIntent.getActivity(
                this,
                0,
                new Intent(this, RulesActivity.class),
                0);

        Notification notification =
                new Notification.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(String.format("您有%d条新通知", messageCount))
                        .setContentText("点击查看")
                        .setContentIntent(contentIntent)
                        .setDeleteIntent(this.dismissIntent)
                        .build();

        this.notificationManager.notify(this.NOTIFICATION_ID, notification);
    }

    public static void newMessage(Context context, Message msg) {
        Intent intent = new Intent(context, NotificationService.class);
        intent.putExtra("message", msg);
        context.startService(intent);
    }
}
