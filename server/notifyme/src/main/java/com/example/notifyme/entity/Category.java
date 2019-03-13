package com.example.notifyme.entity;

import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan
public class Category {

    private long categoryId;

    private String categoryName;

    private long userId;

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
