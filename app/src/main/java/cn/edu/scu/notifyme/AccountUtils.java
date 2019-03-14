package cn.edu.scu.notifyme;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.util.Random;

import cn.edu.scu.notifyme.event.EventID;
import cn.edu.scu.notifyme.event.MessageEvent;
import cn.edu.scu.notifyme.model.RestoreObject;

public class AccountUtils {

    public static void signUp(Long account, String passwordMd5) {
        OkGo.<String>post("https://caoyu.online/notifyme/user/register")
                .params("account", account)
                .params("password", passwordMd5)
                .params("name", account)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        RestoreObject ro = new Gson().fromJson(response.body(), RestoreObject.class);
                        if (ro.getStatus() != 200) {
                            EventBus.getDefault().post(
                                    new MessageEvent(EventID.EVENT_REGISTER_FAILED, null));
                            return;
                        }

                        EventBus.getDefault().post(
                                new MessageEvent(EventID.EVENT_REGISTER_SUCCEED, null));
                    }
                });
    }

    public static void login(Long account, String passwordMd5) {
        int token = new Random().nextInt();
        String sign = EncryptUtils.encryptMD5ToString(passwordMd5 + token).toLowerCase();

        OkGo.<String>post("https://caoyu.online/notifyme/user/login")
                .params("account", account)
                .params("token", token)
                .params("sign", sign)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        RestoreObject ro = new Gson().fromJson(response.body(), RestoreObject.class);
                        if (ro.getStatus() != 200) {
                            EventBus.getDefault().post(
                                    new MessageEvent(EventID.EVENT_LOGIN_FAILED, null));
                            return;
                        }

                        setToken(ro.getToken());
                        setAccount(account);
                        setPassword(passwordMd5);

                        EventBus.getDefault().post(
                                new MessageEvent(EventID.EVENT_LOGIN_SUCCEED, null));
                    }
                });
    }

    public static void logout() {
        setToken(0);
        setAccount(-1L);
        setPassword(null);
    }

    public static void setToken(int token) {
        SPUtils.getInstance().put("token", token);
    }

    public static Integer getToken() {
        return SPUtils.getInstance().getInt("token", 0);
    }

    public static String generateSign() {
        return EncryptUtils.encryptMD5ToString(
                SPUtils.getInstance().getString("password") + AccountUtils.getToken())
                .toLowerCase();
    }

    public static Long getAccount() {
        return SPUtils.getInstance().getLong("account", -1);
    }

    public static void setAccount(Long account) {
        SPUtils.getInstance().put("account", account);
    }

    public static String getPassword() {
        return SPUtils.getInstance().getString("password");
    }

    public static void setPassword(String password) {
        SPUtils.getInstance().put("password", password);
    }

    public static boolean hasSignedIn() {
        return getAccount() != -1;
    }
}
