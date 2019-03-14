package cn.edu.scu.notifyme;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.edu.scu.notifyme.event.EventID;
import cn.edu.scu.notifyme.event.MessageEvent;
import cn.edu.scu.notifyme.model.Category;
import cn.edu.scu.notifyme.model.RestoreObject;
import cn.edu.scu.notifyme.model.Rule;

/**
 * BackupUtils
 * 备份/还原用户规则的工具类
 */
public class BackupUtils {
    public static void backup() {
        if (!AccountUtils.hasSignedIn()) {
            EventBus.getDefault().post(
                    new MessageEvent(EventID.EVENT_BACKUP_FAILED, null));
            return;
        }

        String dataString = generateDataString();

        OkGo.<String>post("https://caoyu.online/notifyme/user/backup")
                .params("sign", AccountUtils.generateSign())
                .params("account", AccountUtils.getAccount())
                .params("data", dataString)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        RestoreObject ro =
                                new Gson().fromJson(response.body(), RestoreObject.class);
                        if (ro.getStatus() != 200) {
                            EventBus.getDefault().post(
                                    new MessageEvent(EventID.EVENT_RESTORE_FAILED, null));
                            return;
                        }

                        AccountUtils.setToken(ro.getToken());

                        EventBus.getDefault().post(
                                new MessageEvent(EventID.EVENT_BACKUP_SUCCEED, null)
                        );
                    }
                });
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

    public static void restore() {
        if (!AccountUtils.hasSignedIn()) {
            EventBus.getDefault().post(
                    new MessageEvent(EventID.EVENT_RESTORE_FAILED, null));
            return;
        }

        OkGo.<String>post("https://caoyu.online/notifyme/user/restore")
                .params("sign", AccountUtils.generateSign())
                .params("account", AccountUtils.getAccount())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        RestoreObject ro =
                                new Gson().fromJson(response.body(), RestoreObject.class);
                        if (ro.getStatus() != 200) {
                            EventBus.getDefault().post(
                                    new MessageEvent(EventID.EVENT_RESTORE_FAILED, null));
                            return;
                        }

                        AccountUtils.setToken(ro.getToken());

                        DatabaseManager.getInstance().clearAll();
                        for (Category category : ro.getCategories()) {
                            DatabaseManager.getInstance().addCategory(category);
                            for (Rule rule : category.getRules()) {
                                DatabaseManager.getInstance().addRule(category, rule);
                            }
                        }

                        EventBus.getDefault().post(
                                new MessageEvent(EventID.EVENT_RESTORE_SUCCEED, null)
                        );
                    }
                });
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
