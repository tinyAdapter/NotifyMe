package com.example.notifyme.util;

import com.alibaba.fastjson.JSONObject;
import com.example.notifyme.entity.User;
import com.example.notifyme.service.TokenService;

public class ResultMaker {

    private JSONObject jsonObject;
    private TokenService tokenService;
    private User user;

    public ResultMaker(TokenService tokenService) {
        this.jsonObject = new JSONObject();
        this.tokenService = tokenService;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void makeOKResult() {
        makeResult(200, "OK");
    }

    public void makeOKResultWithNewToken() {
        makeResultWithNewToken(200, "OK");
    }

    public void makeResult(int statusCode, String message) {
        jsonObject.put("status", statusCode);
        jsonObject.put("message", message);
    }

    public void makeResultWithNewToken(int statusCode, String message) {
        makeResult(statusCode, message);
        jsonObject.put("token", tokenService.generateNewToken(user.getUserId()));
    }

    public String get() {
        return jsonObject.toJSONString();
    }

    public void put(String key, Object value) {
        jsonObject.put(key, value);
    }

    public JSONObject getJsonObject(){
        return this.jsonObject;
    }
}