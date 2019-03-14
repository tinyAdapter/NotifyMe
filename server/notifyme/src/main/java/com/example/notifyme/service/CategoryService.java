package com.example.notifyme.service;

import java.util.List;

import com.example.notifyme.entity.Category;
import com.example.notifyme.mapper.CategoryMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;


    /**
     * 获取指定账户下的所有分类对象
     * 
     * @param account
     * @return List<Category>
     */
    public List<Category> getCategoriesByUserAccount(Long account) {
        return categoryMapper.getCategoriesByUserAccount(account);
    }

    /**
     * 根据用户ID和分类名获取分类对象
     * 
     * @param userId
     * @param categoryName
     * @return Category
     */
    public Category getCategoryByUserIdAndCategoryName(Long userId, String categoryName) {
        return categoryMapper.getCategoryByUserIdAndCategoryName(userId, categoryName);
    }

    /**
     * 插入新的分类信息
     * 
     * @param userId
     * @param category
     */
    public void insertNewCategory(Long userId, String categoryName) {
        categoryMapper.insert(userId, categoryName);
    }

    /**
     * 清空指定用户下所有的分类和规则信息
     * 
     * @param userID
     */
    public void deleteAllByUserId(Long userId) {
        categoryMapper.deleteAllByUserId(userId);
    }
}