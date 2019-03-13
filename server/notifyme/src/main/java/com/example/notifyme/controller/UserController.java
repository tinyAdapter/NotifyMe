package com.example.notifyme.controller;

import java.security.NoSuchAlgorithmException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.notifyme.entity.User;
import com.example.notifyme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@EnableAutoConfiguration
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/getuser", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public boolean getUserByAccount(@RequestBody JSONObject jsonObject) throws NoSuchAlgorithmException {
        JSONObject jsonObjectReturn = new JSONObject();
        long account = Long.parseLong(jsonObject.get("account").toString());
        String sign = jsonObject.get("sign").toString();
        int token = Integer.parseInt(jsonObject.get("token").toString()); 
//        int status = 200;
//        String message = "hello world.";
    //    long account = Long.parseLong(jsonObject.get("account").toString());
        
//        jsonObjectReturn.put("status", status);
//        jsonObjectReturn.put("message", message);
//        jsonObjectReturn.put("account", account);
//        return jsonObjectReturn.toJSONString();
        // jsonObjectReturn.put("code", userService.insertNewUser(13909075176L, "34567", "born"));
        return userService.login(account, token, sign);
    }

    @RequestMapping(value = "/rename", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String renameUser(@RequestBody JSONObject jsonObject) {
        int status = 200;
        String message = "";
        long account = Long.parseLong(jsonObject.get("account").toString());
        JSONObject jsonObjectReturn = new JSONObject();
        String newName = jsonObject.get("name").toString();
        if (userService.getUserByAccount(account) == null) {
            status = 401;
            message = "Can not find the user.";
        }
        if (!userService.updateUserName(account, newName)) {
            status = 402;
            message = "The name is duplicated.";
        }
        jsonObjectReturn.put("status", status);
        jsonObjectReturn.put("message", message);
        return jsonObjectReturn.toJSONString();
    }

}
