package cn.edu.scu.notifyme;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import com.blankj.utilcode.util.SPUtils;

import java.util.Locale;

public class LocaleUtils {
    public static final String EN = "en";
    public static final String ZH_CN = "zh-CN";

    public static Context getBaseContext() {
        return baseContext;
    }

    public static void setBaseContext(Context baseContext) {
        LocaleUtils.baseContext = baseContext;
    }

    private static Context baseContext;


    public static String getLocale() {
        return SPUtils.getInstance().getString("locale", ZH_CN);
    }

    public static Context setLocale(Context context, String locale) {
        Locale newLocale;
        if (locale.equals("system")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                newLocale = Resources.getSystem().getConfiguration().getLocales().get(0);
            } else {
                //noinspection deprecation
                newLocale = Resources.getSystem().getConfiguration().locale;
            }
        } else {
            newLocale = new Locale(locale);
        }
        Locale.setDefault(newLocale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, newLocale);
        } else {
            return updateResourcesLegacy(context, newLocale);
        }
    }

    public static Context onAttach(Context context) {
        return setLocale(context, getLocale());
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, Locale locale) {
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, Locale locale) {
        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale);
        }

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }

    public static String getString(int id) {
        Configuration configuration = new Configuration(baseContext.getResources().getConfiguration());
        configuration.setLocale(new Locale(getLocale()));
        return baseContext.createConfigurationContext(configuration).getResources().getString(id);
    }

    public static void store(String s) {
        SPUtils.getInstance().put("locale", s);
    }
}
