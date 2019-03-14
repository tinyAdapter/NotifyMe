package com.example.notifyme.mapper;

import com.example.notifyme.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {

    @Select("select category_id, category_name, user_id " + 
            "from category natural join user " + 
            "where account = #{account}")
    @Results(id = "categoryMap", value = { 
        @Result(property = "categoryId", column = "category_id"),
        @Result(property = "categoryName", column = "category_name"),
        @Result(property = "userId", column = "user_id") 
    })
    List<Category> getCategoriesByUserAccount(@Param("account") Long account);

    @Insert("insert into category(category_name, user_id) values(#{category_name}, #{user_id})")
    void insert(@Param("user_id") Long userId, @Param("category_name") String categoryName);

    @Delete("delete from category " +
            "where user_id = #{user_id}")
    void deleteAllByUserId(@Param("user_id") Long userId);
}
