package cn.edu.scu.notifyme;

import android.content.Context;

import com.blankj.utilcode.util.SPUtils;

import java.util.Locale;

public class LocaleUtils {
    public static final String EN = "en";
    public static final String ZH_CN = "zh-CN";

    public static String getLocale() {
        return SPUtils.getInstance().getString("locale", ZH_CN);
    }

    public static void setLocale(Context baseContext, String locale) {
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
        setLocale(baseContext, getLocale());
    }
}
