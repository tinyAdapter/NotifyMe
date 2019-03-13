package com.example.notifyme.controller;

import java.security.NoSuchAlgorithmException;

import com.alibaba.fastjson.JSONObject;
import com.example.notifyme.entity.User;
import com.example.notifyme.service.TokenService;
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

    @Autowired
    private TokenService tokenService;

    @PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
    public String getUserByAccount(@RequestParam(required = true) Long account,
            @RequestParam(required = true) String sign, @RequestParam(required = true) Integer token)
            throws NoSuchAlgorithmException {
        JSONObject result = new JSONObject();
        User checkUser = userService.getUserByAccount(account);
        if (checkUser == null) {
            result.put("status", 401);
            result.put("message", "user does not exist");
        } else if (!userService.isLegalUser(account, token, sign)) {
            result.put("status", 403);
            result.put("message", "wrong password");
        } else {
            result.put("status", 200);
            result.put("message", "OK");
            result.put("token", tokenService.generateNewToken(checkUser.getUserId()));
        }

        return result.toJSONString();
    }

    @PostMapping(value = "/rename", produces = "application/json;charset=UTF-8")
    public String renameUser(@RequestParam(required = true) Long account, @RequestParam(required = true) String sign,
            @RequestParam(required = true) String name) throws NoSuchAlgorithmException {
        JSONObject result = new JSONObject();
        User checkUser = userService.getUserByAccount(account);
        if (checkUser == null) {
            result.put("status", 401);
            result.put("message", "user does not exist");
        } else {
            Integer token = tokenService.getTokenByUserId(checkUser.getUserId());
            if (token == null || !userService.isLegalUser(account, token, sign)) {
                result.put("status", 403);
                result.put("message", "user unauthorized");
            } else if (!userService.updateUserName(account, name)) {
                result.put("status", 402);
                result.put("message", "duplicate username");
                result.put("token", tokenService.generateNewToken(checkUser.getUserId()));
            } else {
                result.put("status", 200);
                result.put("message", "OK");
                result.put("token", tokenService.generateNewToken(checkUser.getUserId()));
            }
        }

        return result.toJSONString();
    }
}
