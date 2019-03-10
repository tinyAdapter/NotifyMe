package cn.edu.scu.notifyme;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

import com.blankj.utilcode.util.SPUtils;

import java.util.Locale;

import androidx.annotation.NonNull;

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

    public static void setLocale(String locale) {
        Locale myLocale;
        if (locale.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(locale);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        baseContext.getResources().updateConfiguration(config,
                baseContext.getResources().getDisplayMetrics());

        SPUtils.getInstance().put("locale", locale);
    }

    public static void initLocale(Context baseContext) {
        setBaseContext(baseContext);
        setLocale(getLocale());
    }

    @NonNull
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static String getString(int id) {
        Configuration configuration = new Configuration(baseContext.getResources().getConfiguration());
        configuration.setLocale(new Locale(getLocale()));
        return baseContext.createConfigurationContext(configuration).getResources().getString(id);
    }
}
