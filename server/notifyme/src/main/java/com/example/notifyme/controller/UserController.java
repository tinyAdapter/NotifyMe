package com.example.notifyme.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.notifyme.entity.Category;
import com.example.notifyme.entity.User;
import com.example.notifyme.mapper.CategoryMapper;
import com.example.notifyme.mapper.RuleMapper;
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

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private RuleMapper ruleMapper;

    @PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
    public String getUserByAccount(@RequestParam(required = true) Long account,
            @RequestParam(required = true) String sign, @RequestParam(required = true) Integer token) {
        ResultMaker resultMaker = new ResultMaker(tokenService);
        User checkUser = userService.getUserByAccount(account);
        if (checkUser == null) {
            resultMaker.makeResult(401, "user does not exist");
        } else if (!userService.isLegalUser(account, token, sign)) {
            resultMaker.makeResult(403, "wrong password");
        } else {
            resultMaker.setUser(checkUser);
            resultMaker.makeOKResultWithNewToken();
        }

        return resultMaker.get();
    }

    @PostMapping(value = "/rename", produces = "application/json;charset=UTF-8")
    public String renameUser(@RequestParam(required = true) Long account, @RequestParam(required = true) String sign,
            @RequestParam(required = true) String name) {
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
                resultMaker.makeOKResultWithNewToken();
            }
        }

        return resultMaker.get();
    }

    @PostMapping(value = "/register", produces = "application/json;charset=UTF-8")
    public String register(@RequestParam(required = true) Long account, @RequestParam(required = true) String name,
            @RequestParam(required = true) String password) {
        ResultMaker resultMaker = new ResultMaker(tokenService);
        if (account < 10000000000L || account > 19999999999L) {
            resultMaker.makeResult(403, "account must be valid phone number");
        }
        int result = userService.insertNewUser(account, password, name);
        switch (result) {
        case 1:
            resultMaker.makeResult(401, "duplicate account");
            break;
        case 2:
            resultMaker.makeResult(402, "duplicate username");
            break;
        case 0:
            resultMaker.makeOKResult();
            break;
        }

        return resultMaker.get();
    }

    @PostMapping(value = "/backup", produces = "application/json;charset=UTF-8")
    public String backup(@RequestParam(required = true) Long account, @RequestParam(required = true) String sign,
            @RequestParam(required = true) String data) {
        ResultMaker resultMaker = new ResultMaker(tokenService);
        User checkUser = userService.getUserByAccount(account);
        if (checkUser == null) {
            resultMaker.makeResult(401, "user does not exist");
        } else {
            resultMaker.setUser(checkUser);
            Integer token = tokenService.getTokenByUserId(checkUser.getUserId());
            if (token == null || !userService.isLegalUser(account, token, sign)) {
                resultMaker.makeResult(403, "user unauthorized");
            } else {
                JSONObject dataObject = JSONObject.parseObject(data);
                JSONArray categories = dataObject.getJSONArray("categories");
                for (Object co : categories) {
                    JSONObject categoryObject = (JSONObject) co;
                    String categoryName = categoryObject.getString("name");
                    categoryMapper.insert(checkUser.getUserId(), categoryName);
                    Category category = categoryMapper.getCategoryByUserIdAndCategoryName(
                        checkUser.getUserId(), categoryName);
                    long categoryId = category.getCategoryId();
                    JSONArray rules = categoryObject.getJSONArray("rules");
                    for (Object ro : rules) {
                        JSONObject ruleObject = (JSONObject) ro;
                        ruleMapper.insert(
                            ruleObject.getString("name"), 
                            Boolean.parseBoolean(ruleObject.getString("isActive")), 
                            Integer.parseInt(ruleObject.getString("duration")), 
                            ruleObject.getString("iconUrl"), 
                            ruleObject.getString("toLoadUrl"), 
                            ruleObject.getString("script"), 
                            categoryId);
                    }
                }

                resultMaker.makeOKResultWithNewToken();
            }
        }

        return resultMaker.get();
    }
}
