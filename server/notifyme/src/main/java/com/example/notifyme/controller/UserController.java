package com.example.notifyme.controller;

import java.security.NoSuchAlgorithmException;

import com.alibaba.fastjson.JSONObject;
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

    @PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
    public String getUserByAccount(@RequestParam(required = true) Long account,
            @RequestParam(required = true) String sign, @RequestParam(required = true) Integer token)
            throws NoSuchAlgorithmException {
        JSONObject result = new JSONObject();
        if (userService.isLegalUser(account, token, sign)) {
            result.put("status", 200);
            result.put("message", "OK");
        } else {
            result.put("status", 403);
            result.put("message", "wrong username or password");
        }
        return result.toJSONString();
    }

    @PostMapping(value = "/rename", produces = "application/json;charset=UTF-8")
    public String renameUser(@RequestParam(required = true) Long account, @RequestParam(required = true) String sign,
            @RequestParam(required = true) String name) throws NoSuchAlgorithmException {
        JSONObject result = new JSONObject();
        int status = 200;
        String message = "OK";
        if (!userService.isLegalUser(account, 1/* TODO: add real token read from database */, sign)) {
            status = 403;
            message = "user unauthorized";
        }
        if (userService.getUserByAccount(account) == null) {
            status = 401;
            message = "user does not exist";
        }
        if (!userService.updateUserName(account, name)) {
            status = 402;
            message = "duplicate username";
        }
        result.put("status", status);
        result.put("message", message);
        return result.toJSONString();
    }
}
