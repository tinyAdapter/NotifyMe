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

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;
import cn.edu.scu.notifyme.event.EventID;
import cn.edu.scu.notifyme.event.MessageEvent;

@RunWith(AndroidJUnit4ClassRunner.class)
@SmallTest
public class BackgroundWorkerAndroidUnitTest extends TestCase {

    static CountDownLatch mutex;
    static MessageEvent result;

    @Rule
    public ActivityTestRule activityRule = new ActivityTestRule<>(TestActivity.class);

    @Before
    public void startBackgroundWorker() {
        BackgroundWorker.getInstance().start();
        mutex = new CountDownLatch(1);
    }

    @After
    public void stopBackgroundWorker() {
        BackgroundWorker.getInstance().stop();
    }

    @Test
    public void testExecuteBackgroundWorker() throws InterruptedException {
        cn.edu.scu.notifyme.Rule rule = new cn.edu.scu.notifyme.Rule();
        rule.setName("BAIDU");
        rule.setDuration(15);
        rule.setScript("(function() {\n" +
                "  return { results: document.getElementsByTagName('body')[0].innerHTML };\n" +
                "})();\n");
        rule.setToLoadUrl("https://www.baidu.com");
        BackgroundWorker.getInstance().newTask(rule);
        mutex.await();
        assertEquals(result.getMessage().getTitle(), "BAIDU");
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
        public void onMessageEvent(MessageEvent event) {
            switch (event.getId()) {
                case EventID.EVENT_HAS_FETCHED_RESULT:
                    result = event;
                    break;
                case EventID.EVENT_FETCH_TIMEOUT:
                    break;
            }
            mutex.countDown();
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            EventBus.getDefault().unregister(this);
        }
    }
}
