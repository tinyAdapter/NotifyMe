package com.example.notifyme.controller;

import java.security.NoSuchAlgorithmException;

import com.example.notifyme.entity.User;
import com.example.notifyme.service.TokenService;
import com.example.notifyme.service.UserService;
import com.example.notifyme.util.ResultMaker;

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
        ResultMaker resultMaker = new ResultMaker(tokenService);
        User checkUser = userService.getUserByAccount(account);
        if (checkUser == null) {
            resultMaker.makeResult(401, "user does not exist");
        } else if (!userService.isLegalUser(account, token, sign)) {
            resultMaker.makeResult(403, "wrong password");
        } else {
            resultMaker.setUser(checkUser);
            resultMaker.makeOKResult();
        }

        return resultMaker.get();
    }

    @PostMapping(value = "/rename", produces = "application/json;charset=UTF-8")
    public String renameUser(@RequestParam(required = true) Long account, @RequestParam(required = true) String sign,
            @RequestParam(required = true) String name) throws NoSuchAlgorithmException {
        ResultMaker resultMaker = new ResultMaker(tokenService);
        User checkUser = userService.getUserByAccount(account);
        if (checkUser == null) {
            resultMaker.makeResult(401, "user does not exist");
        } else {
            resultMaker.setUser(checkUser);
            Integer token = tokenService.getTokenByUserId(checkUser.getUserId());
            if (token == null || !userService.isLegalUser(account, token, sign)) {
                resultMaker.makeResult(403, "user unauthorized");
            } else if (!userService.updateUserName(account, name)) {
                resultMaker.makeResultWithNewToken(402, "duplicate username");
            } else {
                resultMaker.makeOKResult();
            }
        }

        return resultMaker.get();
    }
}
