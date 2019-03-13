package com.example.notifyme.mapper;

import com.example.notifyme.entity.Category;
import com.example.notifyme.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("select * from user where account = #{account}")
    // @Results({
	// 	@Result(property = "userId",  column = "user_id"),
    //     @Result(property = "account", column = "account"),
    //     @Result(property = "password", column = "password"),
    //     @Result(property = "userName", column = "user_name")
	// })
    User getUserByAccount(@Param("account")long account);

    @Select("select * from user where user_name = #{name}")
    User getUserByName(@Param("name")String name);

    @Select("select category_id, category_name, user_id " +
            "from user natural join category " +
            "where account = #{account}")
    Category getUserCategoryByAccount(@Param("account")long account);

    @Update("update user set user_name  = #{name} where account = #{account}")
    void updateUserName(@Param("account")long account, @Param("name")String name);

    @Update("update user set password = #{password} where account = #{account}")
    void updateUserPassword(@Param("account")long account, @Param("password")String password);

    @Insert("insert into user(account, password, user_name) values(#{account}, #{password}, #{name})")
    void insertNewUser(@Param("account")long account, @Param("password")String password, @Param("name")String name);
}
