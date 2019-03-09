package cn.edu.scu.notifyme;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.NotificationTarget;

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
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(msg.getTargetUrl()));
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

    private void notifyWithIcon(PendingIntent contentIntent, Message msg) {
        FutureTarget futureTarget =
                Glide.with(getApplicationContext()).asBitmap()
                        .load(msg.getRule().getIconUrl()).submit();

        LoadImageTask task = new LoadImageTask(icon -> {
            Notification notification =
                    new Notification.Builder(this)
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setContentTitle(msg.getTitle())
                            .setContentText(msg.getContent())
                            .setContentIntent(contentIntent)
                            .setLargeIcon(icon)
                            .setDeleteIntent(this.dismissIntent)
                            .build();

            this.notificationManager.notify(this.NOTIFICATION_ID, notification);
        });
        task.execute(futureTarget);
    }

    private void pushNotificationOnMultipleMessages(int messageCount) {
        //TODO: 引导至正确的Activity
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(MainActivity.NAVIGATE_TO_NOTIFICATION_FRAGMENT, true);
        PendingIntent contentIntent = PendingIntent.getActivity(
                this, 0, intent, 0);

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
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
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
