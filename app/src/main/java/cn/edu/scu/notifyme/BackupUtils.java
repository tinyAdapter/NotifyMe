package cn.edu.scu.notifyme;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import cn.edu.scu.notifyme.model.Category;
import cn.edu.scu.notifyme.model.Rule;

/**
 * BackupUtils
 * 备份/还原用户规则的工具类
 */
public class BackupUtils {
    public static void backup() {
        String dataString = getDataString();
        LogUtils.d(dataString);
    }

    private static Gson categoriesGson = new GsonBuilder().
            setExclusionStrategies(new CategoriesExclusionStrategy())
            .create();

    private static String getDataString() {
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
