package com.example.notifyme.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface TokenMapper {

    @Select("select token from token where user_id = #{user_id}")
    Integer getTokenByUserId(@Param("user_id") Long userId);

    @Select("select exists(select 1 from token where user_id = #{user_id})")
    boolean isTokenExists(@Param("user_id") Long userId);

    @Update("update token set token = #{token} where user_id = #{user_id}")
    void updateTokenByUserId(@Param("user_id") Long userId, @Param("token") Integer token);

    @Insert("insert into token(user_id, token) values(#{user_id}, #{token})")
    void insert(@Param("user_id") Long userId, @Param("token") Integer token);

    @Delete("delete from token where user_id = #{user_id}")
    void deleteByUserId(@Param("user_id") Long userId);
}
