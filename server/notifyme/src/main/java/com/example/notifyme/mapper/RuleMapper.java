package com.example.notifyme.mapper;

import com.example.notifyme.entity.Rule;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RuleMapper {

    @Select("select * from rule where category_id = #{category_id}")
    @Results(id = "ruleMap", value = {
        @Result(property="ruleId", column="rule_id"),
        @Result(property="ruleName", column="rule_name"),
        @Result(property="isActive", column="isActive"),
        @Result(property="duration", column="duration"),
        @Result(property="iconUrl", column="icon_url"),
        @Result(property="toLoadUrl", column="toLoad_url"),
        @Result(property="script", column="script"),
        @Result(property="categoryId", column="category_id")
    })
    List<Rule> getRulesByCategoryId(@Param("category_id") Long categoryId);

    @Insert("insert into rule" +
                "(rule_name, isActive, duration, icon_url, toLoad_url, script, category_id) " + 
                "values(#{rule_name}, #{isActive}, #{duration}, #{icon_url}, #{toLoad_url}, " + 
                "#{script}, #{category_id})")
    void insert(@Param("rule_name") String ruleName, @Param("isActive") Boolean isActive, 
                @Param("duration") Integer duration, @Param("icon_url") String iconUrl, 
                @Param("toLoad_url") String toLoadUrl, @Param("script") String script,
                @Param("category_id") Long categoryId);

    @Delete("delete from rule where category_id = #{category_id}")
    void deleteByCategoryId(@Param("category_id") Long categoryId);
}
