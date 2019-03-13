package com.example.notifyme.service;

import java.util.concurrent.ThreadLocalRandom;
import com.example.notifyme.mapper.TokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Autowired
    private TokenMapper tokenMapper;

    /**
     * 获取用户对应的Token
     *
     * @param userId
     * @return Token, 不存在则为null
     */
    public Integer getTokenByUserId(Long userId) {
        if (!tokenMapper.isTokenExists(userId))
            return null;

        return tokenMapper.getTokenByUserId(userId);
    }

    /**
     * 生成新的Token并放入数据库中
     *
     * @param userId
     * @return Integer 生成的新Token
     */
    public Integer generateNewToken(Long userId) {
        Integer newToken = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
        putToken(userId, newToken);
        return newToken;
    }

    /**
     * 将指定的Token放入数据库
     *
     * @param account
     */
    public void putToken(Long userId, Integer token) {
        if (!tokenMapper.isTokenExists(userId)) {
            tokenMapper.insert(userId, token);
        } else {
            tokenMapper.updateTokenByUserId(userId, token);
        }
    }
}
