package cn.edu.scu.notifyme;

import android.os.Bundle;

import junit.framework.TestCase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.filters.SmallTest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;
import cn.edu.scu.notifyme.event.EventID;
import cn.edu.scu.notifyme.event.MessageEvent;

@RunWith(AndroidJUnit4ClassRunner.class)
@SmallTest
public class BackgroundWorkerAndroidUnitTest extends TestCase {

    static MessageEvent result;
    static Semaphore waitSemaphore;
    static Semaphore testSemaphore;

    @Rule
    public ActivityTestRule activityRule = new ActivityTestRule<>(TestActivity.class);

    @Before
    public void startBackgroundWorker() {
        BackgroundWorker.getInstance().start();
        waitSemaphore = new Semaphore(1);
        testSemaphore = new Semaphore(1);
    }

    @After
    public void stopBackgroundWorker() {
        BackgroundWorker.getInstance().stop();
        waitSemaphore = null;
        testSemaphore = null;
    }

    @Test
    public void testNewTaskBackgroundWorker() throws InterruptedException {
        cn.edu.scu.notifyme.Rule rule = new cn.edu.scu.notifyme.Rule();
        rule.setName("BAIDU");
        rule.setDuration(15);
        rule.setScript("(function() {\n" +
                "  return { results: document.getElementsByTagName('body')[0].innerHTML };\n" +
                "})();\n");
        rule.setToLoadUrl("https://www.baidu.com");
        BackgroundWorker.getInstance().newTask(rule);
        testSemaphore.acquire();
        testSemaphore.acquire();
        assertEquals(result.getMessage().getTitle(), "BAIDU");
        assertFalse(result.getMessage().getContent().equals(""));
        assertTrue(result.getMessage().getUpdateTime().getTime()
                - new Date().getTime() < 1000);
    }

    @Test
    public void testInsertTaskBackgroundWorker() throws InterruptedException {
        cn.edu.scu.notifyme.Rule rule = new cn.edu.scu.notifyme.Rule();
        rule.setName("BAIDU");
        rule.setDuration(15);
        rule.setScript("(function() {\n" +
                "  return { results: document.getElementsByTagName('body')[0].innerHTML };\n" +
                "})();\n");
        rule.setToLoadUrl("https://www.baidu.com");
        BackgroundWorker.getInstance().insertTask(rule);
        testSemaphore.acquire();
        testSemaphore.acquire();
        assertEquals(result.getMessage().getTitle(), "BAIDU");
        assertFalse(result.getMessage().getContent().equals(""));
        assertTrue(result.getMessage().getUpdateTime().getTime()
                - new Date().getTime() < 1000);
    }

    @Test
    public void testInsertTasksBackgroundWorker() throws InterruptedException {
        cn.edu.scu.notifyme.Rule rule1 = new cn.edu.scu.notifyme.Rule();
        rule1.setName("BAIDU");
        rule1.setDuration(15);
        rule1.setScript("(function() {\n" +
                "  return { results: document.getElementsByTagName('body')[0].innerHTML };\n" +
                "})();\n");
        rule1.setToLoadUrl("https://www.baidu.com");
        cn.edu.scu.notifyme.Rule rule2 = new cn.edu.scu.notifyme.Rule();
        rule2.setName("SINA CSJ");
        rule2.setDuration(15);
        rule2.setScript("(function() {\n" +
                "  return { results: document.getElementsByTagName('body')[0].innerHTML };\n" +
                "})();\n");
        rule2.setToLoadUrl("https://tech.sina.cn/csj");
        cn.edu.scu.notifyme.Rule rule3 = new cn.edu.scu.notifyme.Rule();
        rule3.setName("BILIBILI");
        rule3.setDuration(15);
        rule3.setScript("(function() {\n" +
                "  return { results: document.getElementsByTagName('body')[0].innerHTML };\n" +
                "})();\n");
        rule3.setToLoadUrl("https://m.bilibili.com");

        BackgroundWorker.getInstance().newTask(rule1);
        BackgroundWorker.getInstance().newTask(rule2);
        BackgroundWorker.getInstance().insertTask(rule3);

        testSemaphore.acquire();
        testSemaphore.acquire();
        assertEquals(result.getMessage().getTitle(), "BILIBILI");
        assertFalse(result.getMessage().getContent().equals(""));
        assertTrue(result.getMessage().getUpdateTime().getTime()
                - new Date().getTime() < 1000);
        waitSemaphore.release();
        testSemaphore.acquire();
        assertEquals(result.getMessage().getTitle(), "BAIDU");
        assertFalse(result.getMessage().getContent().equals(""));
        assertTrue(result.getMessage().getUpdateTime().getTime()
                - new Date().getTime() < 1000);
        waitSemaphore.release();
        testSemaphore.acquire();
        assertEquals(result.getMessage().getTitle(), "SINA CSJ");
        assertFalse(result.getMessage().getContent().equals(""));
        assertTrue(result.getMessage().getUpdateTime().getTime()
                - new Date().getTime() < 1000);
    }

    public static class TestActivity extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            EventBus.getDefault().register(this);
            BackgroundWorker.getInstance().bind(this);
            BackgroundWorker.getInstance().start();
        }

        @Subscribe(threadMode = ThreadMode.POSTING)
        public void onMessageEvent(MessageEvent event) throws InterruptedException {
            waitSemaphore.acquire();
            switch (event.getId()) {
                case EventID.EVENT_HAS_FETCHED_RESULT:
                    result = event;
                    break;
                case EventID.EVENT_FETCH_TIMEOUT:
                    result = event;
                    break;
            }
            testSemaphore.release();
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            EventBus.getDefault().unregister(this);
        }
    }
}
