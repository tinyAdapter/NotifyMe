package cn.edu.scu.notifyme;

import org.greenrobot.eventbus.EventBus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import cn.edu.scu.notifyme.event.EventID;
import cn.edu.scu.notifyme.event.MessageEvent;
import cn.edu.scu.notifyme.model.Message;
import cn.edu.scu.notifyme.model.Rule;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(PowerMockRunner.class)
public class FilterTest {

    DatabaseManager databaseManager = mock(DatabaseManager.class);
    Map<Rule, Message> map = mock(HashMap.class);

    @Test
    public void testIsEquals(){

        doNothing().when(databaseManager).addMessage(any());
        MessageFilter filter = new MessageFilter(map, databaseManager);
        Message msg1 = mock(Message.class);
        Message msg2 = mock(Message.class);
        PowerMockito.when(msg1.getTitle()).thenReturn("0");
        PowerMockito.when(msg2.getTitle()).thenReturn("0");
        PowerMockito.when(msg1.getContent()).thenReturn("0");
        PowerMockito.when(msg2.getContent()).thenReturn("0");

        Assert.assertEquals(filter.isEquals(msg1, msg2), true);
    }

    @Test
    @PrepareForTest({MessageFilter.class})
    public void testGetEvent() throws Exception {

        MessageEvent messageEvent = mock(MessageEvent.class);
        Message msg1 = new Message();
        Message msg2 = new Message();
        Map<Rule, Message> map1 = new HashMap<>();
        Rule rule = new Rule();

        rule.setName("");
        rule.setScript("");
        msg2.setRule(rule);
        map1.put(rule, msg1);


        PowerMockito.when(messageEvent.getId()).thenReturn(EventID.EVENT_HAS_FETCHED_RESULT);
        PowerMockito.when(messageEvent.getMessage()).thenReturn(msg2);
//        PowerMockito.when(message.getRule()).thenReturn(rule);
//        PowerMockito.when(map.get(rule)).thenReturn(msg1);
//        PowerMockito.when(message.getContent()).thenReturn("0");
//        PowerMockito.when(message.getTitle()).thenReturn("0");
//        PowerMockito.when(msg1.getTitle()).thenReturn("0");
//        PowerMockito.when(msg1.getContent()).thenReturn("0");

        MessageFilter filter = PowerMockito.spy(new MessageFilter(map1, databaseManager));
//        PowerMockito.when(filter.isEquals(any(), any())).thenReturn(false);
        PowerMockito.doReturn(false).when(filter).isEquals(any(), any());

        PowerMockito.doNothing().when(filter, "pushNotification", any());

        EventBus.getDefault().post(messageEvent);

        PowerMockito.verifyPrivate(filter).invoke("pushNotification", any());
    }
}