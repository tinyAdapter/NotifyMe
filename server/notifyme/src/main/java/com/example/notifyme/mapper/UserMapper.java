package com.example.notifyme.mapper;

import com.example.notifyme.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("select * from user where account = #{account}")
    @Results(id="userMap", value = { 
        @Result(property = "userId", column = "user_id"), 
        @Result(property = "account", column = "account"),
        @Result(property = "password", column = "password"), 
        @Result(property = "userName", column = "user_name") 
    })
    User getUserByAccount(@Param("account") Long account);

    @Select("select * from user where user_name = #{name}")
    @ResultMap("userMap")
    User getUserByName(@Param("name") String name);

    @Update("update user set user_name  = #{name} where account = #{account}")
    void updateUserName(@Param("account") Long account, @Param("name") String name);

    @Update("update user set password = #{password} where account = #{account}")
    void updateUserPassword(@Param("account") Long account, @Param("password") String password);

    @Insert("insert into user(account, password, user_name) values(#{account}, #{password}, #{name})")
    void insertNewUser(@Param("account") Long account, @Param("password") String password, @Param("name") String name);
}
