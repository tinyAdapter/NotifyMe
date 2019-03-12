package cn.edu.scu.notifyme;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
//@RunWith(PowerMockRunner.class)
//public class MessageFilterUnitTest {
//
//    DatabaseManager databaseManager = mock(DatabaseManager.class);
//    Map<Rule, Message> map = mock(HashMap.class);
//
//    @Test
//    @PrepareForTest({MessageFilter.class})
//    public void testIsEquals(){
//
//        doNothing().when(databaseManager).addMessage(any());
//        MessageFilter filter = new MessageFilter(map, databaseManager);
//        Message msg1 = mock(Message.class);
//        Message msg2 = mock(Message.class);
//        when(msg1.getTitle()).thenReturn("0");
//        when(msg2.getTitle()).thenReturn("0");
//        when(msg1.getContent()).thenReturn("0");
//        when(msg2.getContent()).thenReturn("0");
//
//        Assert.assertEquals(filter.isEquals(msg1, msg2), true);
//    }
//
//    @Test
//    @PrepareForTest({MessageFilter.class, NotificationService.class})
//    public void testPushOnGetNewEvent() throws Exception {
//        Message msg1 = new Message();
//        Message msg2 = new Message();
//        Map<Rule, Message> map1 = new HashMap<>();
//        Rule rule = new Rule();
//
//        rule.setName("");
//        rule.setScript("");
//        map1.put(rule, msg1);
//        msg2.setRule(rule);
//
//        MessageEvent messageEvent = new MessageEvent(EventID.EVENT_HAS_FETCHED_RESULT, msg2);
//
//        mockStatic(NotificationService.class);
//        doNothing().when(
//                NotificationService.class, "newMessage", any(), any());
//
//        MessageFilter filter = spy(new MessageFilter(map1, databaseManager));
//        doReturn(false).when(filter).isEquals(any(), any());
//
//        filter.onMessageEvent(messageEvent);
//
//        verifyPrivate(NotificationService.class).invoke(
//                "newMessage", any(), any());
//    }
//
//    @Test
//    @PrepareForTest({MessageFilter.class, NotificationService.class})
//    public void testNotPushOnGetEquivalentEvent() throws Exception {
//        Message msg1 = new Message();
//        Message msg2 = new Message();
//        Map<Rule, Message> map1 = new HashMap<>();
//        Rule rule = new Rule();
//
//        rule.setName("");
//        rule.setScript("");
//        map1.put(rule, msg1);
//        msg2.setRule(rule);
//
//        MessageEvent messageEvent = new MessageEvent(EventID.EVENT_HAS_FETCHED_RESULT, msg2);
//
//        mockStatic(NotificationService.class);
//        doNothing().when(
//                NotificationService.class, "newMessage", any(), any());
//
//        MessageFilter filter = spy(new MessageFilter(map1, databaseManager));
//        doReturn(true).when(filter).isEquals(any(), any());
//
//        filter.onMessageEvent(messageEvent);
//
//        verifyPrivate(NotificationService.class, times(0)).invoke(
//                "newMessage", any(), any());
//    }
//}