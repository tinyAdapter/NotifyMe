package cn.edu.scu.notifyme;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;

import java.util.concurrent.ExecutionException;

import androidx.annotation.Nullable;
import cn.edu.scu.notifyme.model.Message;
import cn.edu.scu.notifyme.model.Rule;

/**
 * NotificationService
 * 实现消息通知，消除通知即视为已读
 * 静态函数newMessage为简化Service调用的函数
 */
public class NotificationService extends Service {

    private static final int NOTIFICATION_ID = 20001;
    private static final String CHANNEL_ID = "notifyme_channel_01";

    private NotificationManager notificationManager;

    private static int unreadNotificationCount;
    private PendingIntent dismissIntent;

    public static void clearUnreadNotificationCount() {
        unreadNotificationCount = 0;
    }

    public static class NotificationDismissedBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            clearUnreadNotificationCount();
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
                NOTIFICATION_SERVICE);
        this.dismissIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(),
                0,
                new Intent(this,
                        NotificationDismissedBroadcastReceiver.class),
                0);
        clearUnreadNotificationCount();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            this.notificationManager.createNotificationChannel(mChannel);
        }

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
        if (unreadNotificationCount <= 1) {
            pushNotificationOnOneMessage(msg);
        } else {
            pushNotificationOnMultipleMessages(unreadNotificationCount);
        }

        return START_NOT_STICKY;
    }

    private void pushNotificationOnOneMessage(Message msg) {
//        // 点击直接跳转到对应网站
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse(msg.getTargetUrl()));
//        PendingIntent contentIntent = PendingIntent.getActivity(
//                this, 0, intent, 0);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(MainActivity.NAVIGATE_TO_NOTIFICATION_FRAGMENT, true);
        PendingIntent contentIntent = PendingIntent.getActivity(
                this, 0, intent, 0);

        Rule rule = DatabaseManager.getInstance().getRuleByMessageId(msg.getId());
        msg.setRule(rule);

        if (msg.getRule().getIconUrl() == null) {
            notifyWithNoIcon(contentIntent, msg);
        } else {
            notifyWithIcon(contentIntent, msg);
        }
    }

    private void notifyWithNoIcon(PendingIntent contentIntent, Message msg) {
        Notification.Builder notificationBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder = new Notification.Builder(this, CHANNEL_ID);
        } else {
            notificationBuilder = new Notification.Builder(this);
        }
        Notification notification = notificationBuilder
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true)
                .setContentTitle(msg.getTitle())
                .setContentText(msg.getContent())
                .setContentIntent(contentIntent)
                .setDeleteIntent(this.dismissIntent)
                .build();


        this.notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private void notifyWithIcon(PendingIntent contentIntent, Message msg) {
        FutureTarget futureTarget =
                Glide.with(getApplicationContext()).asBitmap()
                        .load(msg.getRule().getIconUrl()).submit();

        LoadImageTask task = new LoadImageTask(icon -> {
            Notification.Builder notificationBuilder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationBuilder = new Notification.Builder(this, CHANNEL_ID);
            } else {
                notificationBuilder = new Notification.Builder(this);
            }
            Notification notification = notificationBuilder
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setAutoCancel(true)
                    .setContentTitle(msg.getTitle())
                    .setContentText(msg.getContent())
                    .setContentIntent(contentIntent)
                    .setLargeIcon(icon)
                    .setDeleteIntent(this.dismissIntent)
                    .build();

            this.notificationManager.notify(NOTIFICATION_ID, notification);
        });
        task.execute(futureTarget);
    }

    private void pushNotificationOnMultipleMessages(int messageCount) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(MainActivity.NAVIGATE_TO_NOTIFICATION_FRAGMENT, true);
        PendingIntent contentIntent = PendingIntent.getActivity(
                this, 0, intent, 0);

        Notification.Builder notificationBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder = new Notification.Builder(this, CHANNEL_ID);
        } else {
            notificationBuilder = new Notification.Builder(this);
        }
        Notification notification = notificationBuilder
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true)
                .setContentTitle(String.format("您有%d条新通知", messageCount))
                .setContentText("点击查看")
                .setContentIntent(contentIntent)
                .setDeleteIntent(this.dismissIntent)
                .build();

        this.notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public static void newMessage(Context context, Message msg) {
        Intent intent = new Intent(context, NotificationService.class);
        intent.putExtra("message", msg);
        context.startService(intent);
    }

    private static class LoadImageTask extends AsyncTask<FutureTarget<Bitmap>, Void, Bitmap> {
        private OnSuccess onSuccess;

        interface OnSuccess {
            void onSuccess(Bitmap bitmap);
        }

        LoadImageTask(OnSuccess onSuccess) {
            this.onSuccess = onSuccess;
        }

        @SafeVarargs
        @Override
        protected final Bitmap doInBackground(FutureTarget<Bitmap>... futureTargets) {
            try {
                return futureTargets[0].get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null)
                onSuccess.onSuccess(bitmap);
        }
    }
}
