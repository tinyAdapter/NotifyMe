package cn.edu.scu.notifyme;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.edu.scu.notifyme.model.Category;
import cn.edu.scu.notifyme.model.Rule;

/**
 * BackupUtils
 * 备份/还原用户规则的工具类
 */
public class BackupUtils {
    public static void backup() {
        String dataString = generateDataString();
        LogUtils.d(dataString);
        //TODO: 发送数据至服务器
    }

    private static Gson categoriesGson = new GsonBuilder().
            setExclusionStrategies(new CategoriesExclusionStrategy())
            .create();

    private static String generateDataString() {
        List<Category> categories = new ArrayList<>(DatabaseManager.getInstance().getCategories());
        for (Category category : categories) {
            category.setId(null);
            List<Rule> rules = new ArrayList<>(category.getRules());
            for (Rule rule : rules) {
                rule.setId(null);
                rule.setMsg(null);
            }
        }
        String categoriesString = categoriesGson.toJson(categories, ArrayList.class);
        return String.format("{\"categories\":%s}", categoriesString);
    }

    private static boolean hasSignedIn() {
        return SPUtils.getInstance().getString("token") != null;
    }

    public static void restore() {
        String dataString = getDataStringFromServer();
        List<Category> categories = generateCategories(dataString);

        DatabaseManager.getInstance().clearAll();
        for (Category category : categories) {
            DatabaseManager.getInstance().addCategory(category);
            for (Rule rule : category.getRules()) {
                DatabaseManager.getInstance().addRule(category, rule);
            }
        }
    }

    private static List<Category> generateCategories(String data) {
        data = data.replace("{\"categories\":", "");
        data = data.substring(0, data.length() - 1);
        return new ArrayList<>(Arrays.asList(new Gson().fromJson(data, Category[].class)));
    }

    private static String getDataStringFromServer() {
        //TODO: 从服务器获取数据
        return "{\"categories\":[{\"name\":\"Default\",\"rules\":[{\"duration\":18,\"iconUrl\":\"https://hitokoto.cn/favicon.ico\",\"isActive\":true,\"name\":\"Hitokoto - 一言\",\"script\":\"var taskResult \\u003d {};\\ntaskResult.messages \\u003d [];\\nfetch(\\\"https://v1.hitokoto.cn\\\")\\n  .then(response \\u003d\\u003e {\\n    return response.json();\\n  })\\n  .then(json \\u003d\\u003e {\\n    taskResult.iconUrl \\u003d \\\"https://hitokoto.cn/favicon.ico\\\";\\n    return {\\n      title: \\\"Hitokoto\\\",\\n      imgUrl:\\n        \\\"https://piccdn.freejishu.com/images/2016/09/25/930f5212c99ccc71accd4615cb03e255.jpg\\\",\\n      content: `${json.hitokoto} - ${json.from}`,\\n      targetUrl: \\\"https://hitokoto.cn\\\"\\n    };\\n  })\\n  .then(result \\u003d\\u003e {\\n    taskResult.messages.push(result);\\n  })\\n  .then(() \\u003d\\u003e {\\n    return fetch(\\\"https://v1.hitokoto.cn\\\");\\n  })\\n  .then(response \\u003d\\u003e {\\n    return response.json();\\n  })\\n  .then(json \\u003d\\u003e {\\n    taskResult.iconUrl \\u003d \\\"https://hitokoto.cn/favicon.ico\\\";\\n    return {\\n      title: \\\"Hitokoto\\\",\\n      imgUrl:\\n        \\\"https://piccdn.freejishu.com/images/2016/09/25/930f5212c99ccc71accd4615cb03e255.jpg\\\",\\n      content: `${json.hitokoto} - ${json.from}`,\\n      targetUrl: \\\"https://hitokoto.cn\\\"\\n    };\\n  })\\n  .then(result \\u003d\\u003e {\\n    taskResult.messages.push(result);\\n  })\\n  .then(() \\u003d\\u003e {\\n    App.Return(JSON.stringify(taskResult));\\n  });\",\"toLoadUrl\":\"https://hitokoto.cn\"},{\"duration\":10,\"iconUrl\":\"https://alioss.gcores.com/assets/store-logo-white-9b591191580455f5508d8c3be70070e541e53e080b02db926927fbd6c5eb9818.png\",\"isActive\":true,\"name\":\"机核网 - 最新资讯\",\"script\":\"var taskResult \\u003d {};\\ntaskResult.iconUrl \\u003d document.querySelector(\\\".navbar_brand-affix_white\\\").src;\\ntaskResult.messages \\u003d [];\\nvar trIndex \\u003d 0;\\ndocument.querySelectorAll(\\\".showcase-article\\\").forEach(articleDiv \\u003d\\u003e {\\n  if (trIndex \\u003c 5) {\\n    taskResult.messages.push({\\n      title: articleDiv.querySelector(\\\"h4 a\\\").innerHTML.trim(),\\n      content: articleDiv.querySelector(\\\".showcase_info\\\").innerHTML.trim(),\\n      imgUrl: articleDiv.querySelector(\\\".showcase_img a img\\\").src,\\n      targetUrl: articleDiv.querySelector(\\\"h4 a\\\").href\\n    });\\n    trIndex++;\\n  }\\n});\\nApp.Return(JSON.stringify(taskResult));\",\"toLoadUrl\":\"https://www.gcores.com\"}]},{\"name\":\"生活\",\"rules\":[{\"duration\":20,\"isActive\":true,\"name\":\"什么值得买 - 发现\",\"script\":\"var taskResult \\u003d {};\\ntaskResult.iconUrl \\u003d \\\"https://www.smzdm.com/favicon.ico\\\";\\ntaskResult.messages \\u003d [];\\nvar trIndex \\u003d 0;\\ndocument.querySelectorAll(\\\"li.card-group-list\\\").forEach(div \\u003d\\u003e {\\n  if (trIndex \\u003c 4) {\\n    taskResult.messages.push({\\n      title: div.querySelector(\\\"img\\\").alt,\\n      content: div.querySelector(\\\".zm-card-price\\\").innerHTML.trim(),\\n      imgUrl: div.querySelector(\\\"img\\\").src,\\n      targetUrl: div.querySelector(\\\"a\\\").href\\n    });\\n    trIndex++;\\n  }\\n});\\nApp.Return(JSON.stringify(taskResult));\",\"toLoadUrl\":\"https://faxian.m.smzdm.com\"}]}]}";
    }

    private static class CategoriesExclusionStrategy implements ExclusionStrategy {
        public boolean shouldSkipClass(Class<?> arg0) {
            return false;
        }

        public boolean shouldSkipField(FieldAttributes f) {
            return (f.getName().equals("id")) || (f.getName().equals("baseObjId") ||
                    (f.getName().equals("msg")));
        }
    }
}
