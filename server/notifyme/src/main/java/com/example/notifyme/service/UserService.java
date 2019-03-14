package com.example.notifyme.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;

import com.example.notifyme.entity.User;
import com.example.notifyme.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 判断账户、token和sign三者是否满足加密规则
     * 
     * @param account
     * @param token
     * @param sign
     * @return boolean true:满足加密规则 false:不满足加密规则
     */
    public boolean isLegalUser(Long account, int token, String sign) throws NoSuchAlgorithmException {
        String password = userMapper.getUserByAccount(account).getPassword();
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update((password + String.valueOf(token)).getBytes());
        byte[] digest = md.digest();
        String mySign = DatatypeConverter.printHexBinary(digest).toLowerCase();

        if (mySign.equals(sign))
            return true;
        else
            return false;
    }

    /**
     * 根据账户获取用户对象
     *
     * @param account
     * @return User
     */
    public User getUserByAccount(Long account) {
        return userMapper.getUserByAccount(account);
    }

    /**
     * 根据昵称获取用户对象
     *
     * @param name
     * @return User
     */
    public User getUserByName(String name) {
        return userMapper.getUserByName(name);
    }

    /**
     * 更新用户昵称
     *
     * @param account
     * @param name
     * @return boolean true:更新成功 false：更新失败，昵称重复
     */
    public boolean updateUserName(Long account, String name) {
        if (getUserByName(name) == null) {
            userMapper.updateUserName(account, name);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 更新用户密码
     *
     * @param account
     * @param password
     */
    public void updateUserPassword(Long account, String password) {
        userMapper.updateUserPassword(account, password);
    }

    /**
     * 注册新用户
     *
     * @param account
     * @param password
     * @param name
     * @return int 0：注册成功 1：注册失败，账号重复 2：注册失败，昵称重复
     */
    public int insertNewUser(Long account, String password, String name) {
        if (getUserByAccount(account) != null)
            return 1;
        else if (getUserByName(name) != null)
            return 2;
        else {
            userMapper.insertNewUser(account, password, name);
            return 0;
        }
    }

}
