package cn.edu.scu.notifyme;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;

import org.litepal.LitePal;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.scu.notifyme.model.Category;
import cn.edu.scu.notifyme.model.Message;
import cn.edu.scu.notifyme.model.Rule;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        LitePal.initialize(this);
        DatabaseManager.getInstance().initial();
    }

    public static void init(Context context) {
        Map<Rule, Message> map_latestMsg = new HashMap<>();
        for (Rule rule : DatabaseManager.getInstance().getList_rule()) {
            if (rule.getMsg().isEmpty())
                continue;
            Message latestMsg = rule.getMsg().get(0);
            for (int i = 1; i < rule.getMsg().size(); i++)
                if (latestMsg.getUpdateTime().getTime() <
                        rule.getMsg().get(i).getUpdateTime().getTime())
                    latestMsg = rule.getMsg().get(i);
            map_latestMsg.put(rule, latestMsg);
        }


        //测试构造
        Rule theRule = new Rule();
        theRule.setName(new Date().toString());
        theRule.setDuration(10);
        theRule.setToLoadUrl("https://m.bilibili.com");
        theRule.setScript("(function() {\n" +
                "  return { results: document.getElementsByTagName('body')[0].innerHTML };\n" +
                "})();\n");

        DatabaseManager.getInstance().addRule(theRule);

        BackgroundWorker.getInstance().setRule(theRule);

        Message theMsg = new Message();

        map_latestMsg.put(theRule, theMsg);

        MessageFilter msgfilter = new MessageFilter(map_latestMsg, DatabaseManager.getInstance());
        msgfilter.bind(context);
    }
}