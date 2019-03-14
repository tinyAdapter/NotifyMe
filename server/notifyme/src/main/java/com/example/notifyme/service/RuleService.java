package com.example.notifyme.service;

import java.util.List;

import com.example.notifyme.entity.Rule;
import com.example.notifyme.mapper.RuleMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RuleService {

    @Autowired
    private RuleMapper ruleMapper;

    /**
     * 根据分类id获取规则列表
     * 
     * @param categoryId
     * @return List<Rule>
     */
    public List<Rule> getRulesByCategoryId(Long categoryId) {
        return ruleMapper.getRulesByCategoryId(categoryId);
    }

    /**
     * 插入规则任务
     * 
     * @param ruleName
     * @param isActive
     * @param duration
     * @param iconUrl
     * @param toLoadUrl
     * @param script
     * @param categoryId
     */
    public void insertRule(String ruleName, boolean isActive, Integer duration, String iconUrl, String toLoadUrl,
            String script, Long categoryId) {
        ruleMapper.insert(ruleName, isActive, duration, iconUrl, toLoadUrl, script, categoryId);
    }

    /**
     * 删除指定分类Id下的所有任务
     * 
     * @param categoryId
     */
    public void deleteByCategoryId(Long categoryId) {
        ruleMapper.deleteByCategoryId(categoryId);
    }
}