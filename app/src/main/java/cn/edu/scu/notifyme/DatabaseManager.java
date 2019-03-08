package cn.edu.scu.notifyme;

import org.litepal.LitePal;

import java.util.List;

import cn.edu.scu.notifyme.model.Category;
import cn.edu.scu.notifyme.model.Message;
import cn.edu.scu.notifyme.model.Rule;

public class DatabaseManager {

    private static DatabaseManager instence = new DatabaseManager();

    private List<Category> list_category;
    private List<Rule> list_rule;
    private List<Message> list_msg;

    private DatabaseManager() {
    }

    public static DatabaseManager getInstance() {
        return instence;
    }

    public void initial() {
        this.updataList();

        for (int i = 0; i < list_category.size(); i++) {
            if (list_category.get(i).getName().equals("Default"))
                return;
        }

        Category defaultCategory = new Category();
        defaultCategory.setName("Default");
        addCategory(defaultCategory);
        Category codeCategory = new Category();
        codeCategory.setName("主页代码");
        addCategory(codeCategory);

        Rule rule;
        rule = new Rule();
        rule.setName("bilibili主页代码");
        rule.setScript("(function() { return { results: document.getElementsByTagName('html')[0].innerHTML.substring(0, 100) }; })();");
        rule.setToLoadUrl("https://m.bilibili.com");
        rule.setActive(true);
        rule.setDuration(15);
        rule.setCategory(defaultCategory);
        addRule(rule);

        rule = new Rule();
        rule.setName("Baidu主页代码");
        rule.setScript("(function() { return { results: document.getElementsByTagName('html')[0].innerHTML.substring(0, 100) }; })();");
        rule.setToLoadUrl("https://www.baidu.com");
        rule.setCategory(codeCategory);
        rule.setActive(true);
        rule.setDuration(20);
        addRule(rule);

        rule = new Rule();
        rule.setName("Sina CSJ代码");
        rule.setScript("(function() { return { results: document.getElementsByTagName('html')[0].innerHTML.substring(0, 100) }; })();");
        rule.setToLoadUrl("https://tech.sina.cn/csj");
        rule.setCategory(codeCategory);
        rule.setActive(true);
        rule.setDuration(12);
        addRule(rule);

        rule = new Rule();
        rule.setName("Bing主页代码");
        rule.setScript("(function() { return { results: document.getElementsByTagName('html')[0].innerHTML.substring(0, 100) }; })();");
        rule.setToLoadUrl("https://cn.bing.com");
        rule.setCategory(defaultCategory);
        rule.setActive(false);
        rule.setDuration(10);
        addRule(rule);
    }

    public void updataList(){
        list_category = LitePal.findAll(Category.class, true);
        list_rule = LitePal.findAll(Rule.class, true);
        list_msg = LitePal.findAll(Message.class,true);
    }

    public List<Category> getList_category() {
        return list_category;
    }

    public List<Rule> getList_rule() {
        return list_rule;
    }

    public List<Message> getList_msg() {
        return list_msg;
    }

    public void addCategory(Category category) {
        list_category.add(category);
        category.save();
    }

    public void deleteCategory(String name) {
        if (name.equals("Default"))
            return;
        for (int i = 0; i < list_category.size(); i++) {
            if (list_category.get(i).getName().equals(name)) {
                Category category =
                        LitePal.where("name = ?", "Default").find(Category.class).get(0);
                for (int j = 0; j < list_category.get(i).getRule().size(); j++)
                    list_category.get(i).getRule().get(j).setCategory(category);
                LitePal.delete(Category.class, list_category.get(i).getId());
                list_category.remove(i);
                break;
            }
        }
    }

    public void updataCategory(Category category) {
        category.save();
    }

    public void addRule(Rule rule) {
        list_rule.add(rule);
        rule.save();
    }

    public void deleteRule(String name) {
        for (int i = 0; i < list_rule.size(); i++) {
            if (list_rule.get(i).getName().equals(name)) {
                LitePal.delete(Rule.class, list_rule.get(i).getId());
                list_rule.remove(i);
                break;
            }
        }
    }

    public void updataRule(Rule rule) {
        rule.save();
    }

    public void addMessage(Message message) {
        list_msg.add(message);
        message.save();
    }

    public void deleteMessage(long id) {
        for (int i = 0; i < list_msg.size(); i++) {
            if (list_msg.get(i).getId() == id) {
                LitePal.delete(Message.class, id);
                list_msg.remove(i);
                break;
            }
        }
    }
}
