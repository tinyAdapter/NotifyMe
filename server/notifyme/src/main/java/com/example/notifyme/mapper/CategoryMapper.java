package com.example.notifyme.mapper;


import com.example.notifyme.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    List<Category> getlist(int id);
}
