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

    public List<Category> getCategoriesByUserId(Long account) {
        return categoryMapper.getCategoriesByUserId(userId)
    }
}