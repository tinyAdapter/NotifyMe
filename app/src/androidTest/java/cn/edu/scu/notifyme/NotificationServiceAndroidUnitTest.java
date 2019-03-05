package cn.edu.scu.notifyme;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Handler;

import junit.framework.TestCase;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.filters.SmallTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ServiceTestRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.Direction;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.Until;

@RunWith(AndroidJUnit4ClassRunner.class)
@SmallTest
public class NotificationServiceAndroidUnitTest extends TestCase {

    @Rule
    public ServiceTestRule serviceRule = new ServiceTestRule();

    @Test
    public void testNotifyOneMessage() throws TimeoutException, InterruptedException {
        int TIMEOUT = 1000;
        String NOTIFICATION_TITLE = "BILIBILI";
        String NOTIFICATION_TEXT = "BILIBILI.COM";

        Intent serviceIntent =
                new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                        NotificationService.class);

        Message msg = new Message();
        msg.setTitle(NOTIFICATION_TITLE);
        msg.setContent(NOTIFICATION_TEXT);
        msg.setTargetUrl("https://m.bilibili.com");
        serviceIntent.putExtra("message", msg);
        serviceRule.startService(serviceIntent);

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.openNotification();
        device.wait(Until.hasObject(By.text(NOTIFICATION_TITLE)), TIMEOUT);
        UiObject2 title = device.findObject(By.text(NOTIFICATION_TITLE));
        UiObject2 text = device.findObject(By.text(NOTIFICATION_TEXT));
        assertEquals(NOTIFICATION_TITLE, title.getText());
        assertEquals(NOTIFICATION_TEXT, text.getText());

        title.swipe(Direction.LEFT, 0.5f);
        Thread.sleep(2000);
    }

    @Test
    public void testNotifyMultipleMessages() throws TimeoutException, InterruptedException {
        int TIMEOUT = 1000;

        Intent serviceIntent =
                new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                        NotificationService.class);

        Message msg1 = new Message();
        msg1.setTitle("BILIBILI1");
        msg1.setContent("BILIBILI1.COM");
        msg1.setTargetUrl("https://m.bilibili.com");
        serviceIntent.putExtra("message", msg1);
        serviceRule.startService(serviceIntent);

        Message msg2 = new Message();
        msg2.setTitle("BILIBILI2");
        msg2.setContent("BILIBILI2.COM");
        msg2.setTargetUrl("https://m.bilibili.com");
        serviceIntent.putExtra("message", msg2);
        serviceRule.startService(serviceIntent);

        Message msg3 = new Message();
        msg3.setTitle("BILIBILI3");
        msg3.setContent("BILIBILI3.COM");
        msg3.setTargetUrl("https://m.bilibili.com");
        serviceIntent.putExtra("message", msg3);
        serviceRule.startService(serviceIntent);

        String NOTIFICATION_TITLE = "您有3条新通知";
        String NOTIFICATION_TEXT = "点击查看";

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.openNotification();
        device.wait(Until.hasObject(By.text(NOTIFICATION_TITLE)), TIMEOUT);
        UiObject2 title = device.findObject(By.text(NOTIFICATION_TITLE));
        UiObject2 text = device.findObject(By.text(NOTIFICATION_TEXT));
        assertEquals(NOTIFICATION_TITLE, title.getText());
        assertEquals(NOTIFICATION_TEXT, text.getText());

        title.swipe(Direction.LEFT, 0.5f);
        Thread.sleep(2000);
    }

    @Test
    public void testNewMessageFunction() throws TimeoutException, InterruptedException {
        int TIMEOUT = 1000;
        String NOTIFICATION_TITLE = "BILIBILI4";
        String NOTIFICATION_TEXT = "BILIBILI4.COM";

        Message msg = new Message();
        msg.setTitle(NOTIFICATION_TITLE);
        msg.setContent(NOTIFICATION_TEXT);
        msg.setTargetUrl("https://m.bilibili.com");
        NotificationService.newMessage(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                msg);

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.openNotification();
        device.wait(Until.hasObject(By.text(NOTIFICATION_TITLE)), TIMEOUT);
        UiObject2 title = device.findObject(By.text(NOTIFICATION_TITLE));
        UiObject2 text = device.findObject(By.text(NOTIFICATION_TEXT));
        assertEquals(NOTIFICATION_TITLE, title.getText());
        assertEquals(NOTIFICATION_TEXT, text.getText());

        title.swipe(Direction.LEFT, 0.5f);
        Thread.sleep(2000);
    }
}
