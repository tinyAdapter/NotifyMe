package cn.edu.scu.notifyme;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
//@RunWith(MockitoJUnitRunner.class)
//public class TaskManagerUnitTest {
//    @Test
//    public void testCreateTimerTasks() throws InterruptedException {
//        BackgroundWorker cttBackgroundWorker = mock(BackgroundWorker.class);
//        doNothing().when(cttBackgroundWorker).newTask(any());
//        doNothing().when(cttBackgroundWorker).start();
//        doNothing().when(cttBackgroundWorker).stop();
//
//        ArrayList<Rule> rules = new ArrayList<>();
//        cn.edu.scu.notifyme.model.Rule rule1 = new cn.edu.scu.notifyme.model.Rule();
//        rule1.setName("BAIDU");
//        rule1.setDuration(10);
//        rule1.setScript("");
//        rule1.setActive(true);
//        rule1.setToLoadUrl("https://www.baidu.com");
//        cn.edu.scu.notifyme.model.Rule rule2 = new cn.edu.scu.notifyme.model.Rule();
//        rule2.setName("SINA CSJ");
//        rule2.setDuration(15);
//        rule2.setScript("");
//        rule2.setActive(true);
//        rule2.setToLoadUrl("https://tech.sina.cn/csj");
//        cn.edu.scu.notifyme.model.Rule rule3 = new cn.edu.scu.notifyme.model.Rule();
//        rule3.setName("BILIBILI");
//        rule3.setDuration(20);
//        rule3.setScript("");
//        rule3.setActive(true);
//        rule3.setToLoadUrl("https://m.bilibili.com");
//        rules.add(rule1);
//        rules.add(rule2);
//        rules.add(rule3);
//        TaskManager taskManager = new TaskManager(rules, cttBackgroundWorker);
//        taskManager.start();
//        Thread.sleep(100);
//
//        verify(cttBackgroundWorker, times(3)).newTask(any());
//
//        taskManager.destroy();
//    }
//
//    @Test
//    @PrepareForTest({BackgroundWorker.class})
//    public void testFilterActiveRules() throws InterruptedException {
//        BackgroundWorker farBackgroundWorker = mock(BackgroundWorker.class);
//        doNothing().when(farBackgroundWorker).newTask(any());
//        doNothing().when(farBackgroundWorker).start();
//        doNothing().when(farBackgroundWorker).stop();
//
//        ArrayList<Rule> rules = new ArrayList<>();
//        cn.edu.scu.notifyme.model.Rule rule1 = new cn.edu.scu.notifyme.model.Rule();
//        rule1.setName("BAIDU");
//        rule1.setDuration(10);
//        rule1.setScript("");
//        rule1.setActive(true);
//        rule1.setToLoadUrl("https://www.baidu.com");
//        cn.edu.scu.notifyme.model.Rule rule2 = new cn.edu.scu.notifyme.model.Rule();
//        rule2.setName("SINA CSJ");
//        rule2.setDuration(15);
//        rule2.setScript("");
//        rule2.setActive(true);
//        rule2.setToLoadUrl("https://tech.sina.cn/csj");
//        cn.edu.scu.notifyme.model.Rule rule3 = new cn.edu.scu.notifyme.model.Rule();
//        rule3.setName("BILIBILI");
//        rule3.setDuration(20);
//        rule3.setScript("");
//        rule3.setActive(false);
//        rule3.setToLoadUrl("https://m.bilibili.com");
//        rules.add(rule1);
//        rules.add(rule2);
//        rules.add(rule3);
//        TaskManager taskManager = new TaskManager(rules, farBackgroundWorker);
//        taskManager.start();
//        Thread.sleep(100);
//
//        verify(farBackgroundWorker, times(2)).newTask(any());
//
//        taskManager.destroy();
//    }
//}