package cn.edu.scu.notifyme;

import org.junit.Before;
import org.junit.Test;

import cn.edu.scu.notifyme.interfaces.ILogListener;
import cn.edu.scu.notifyme.model.Log;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LogSystemUnitTest {
    @Before
    public void restoreLogSystem() {
        LogSystem.getInstance().removeAllListeners();
        LogSystem.getInstance().clear();
        receivedCount = 0;
    }

    private int receivedCount;

    @Test
    public void testRegister() {
        LogSystem.getInstance().register(new TestLogListener());
        LogSystem.getInstance().d("test");
        LogSystem.getInstance().d("test");
        assertThat(receivedCount, is(2));
    }

    @Test
    public void testUnregister() {
        ILogListener listener = new TestLogListener();
        LogSystem.getInstance().register(listener);
        LogSystem.getInstance().d("test");
        LogSystem.getInstance().unregister(listener);
        LogSystem.getInstance().d("test");
        assertThat(receivedCount, is(1));
    }

    @Test
    public void testGetLogs() {
        LogSystem.getInstance().d("hello");
        LogSystem.getInstance().d("log system");
        LogSystem.getInstance().d("from junit");
        assertThat(LogSystem.getInstance().getLogs().size(), is(3));
        assertThat(LogSystem.getInstance().getLogs().get(0).getMessage(), is("hello"));
    }

    private class TestLogListener implements ILogListener {
        @Override
        public void onLog(Log log) {
            if (log.getMessage().equals("test")) {
                receivedCount++;
            }
        }
    }
}