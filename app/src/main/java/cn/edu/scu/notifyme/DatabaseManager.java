package cn.edu.scu.notifyme;

import org.litepal.LitePal;

import java.util.List;

import cn.edu.scu.notifyme.model.Category;
import cn.edu.scu.notifyme.model.Message;
import cn.edu.scu.notifyme.model.Rule;

public class DatabaseManager {

    private static DatabaseManager instence = new DatabaseManager();

    private List<Category> categories;
    private List<Rule> rules;
    private List<Message> messages;

    private DatabaseManager() {
    }

    public static DatabaseManager getInstance() {
        return instence;
    }

    public void initial() {
        this.updateList();

        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getName().equals("Default"))
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
        addRule(defaultCategory, rule);

        rule = new Rule();
        rule.setName("Baidu主页代码");
        rule.setScript("(function() { return { results: document.getElementsByTagName('html')[0].innerHTML.substring(0, 100) }; })();");
        rule.setToLoadUrl("https://www.baidu.com");
        rule.setActive(true);
        rule.setDuration(20);
        addRule(codeCategory, rule);

        rule = new Rule();
        rule.setName("Sina CSJ代码");
        rule.setScript("(function() { return { results: document.getElementsByTagName('html')[0].innerHTML.substring(0, 100) }; })();");
        rule.setToLoadUrl("https://tech.sina.cn/csj");
        rule.setActive(true);
        rule.setDuration(12);
        addRule(codeCategory, rule);

        rule = new Rule();
        rule.setName("Bing主页代码");
        rule.setScript("(function() { return { results: document.getElementsByTagName('html')[0].innerHTML.substring(0, 100) }; })();");
        rule.setToLoadUrl("https://cn.bing.com");
        rule.setCategory(defaultCategory);
        rule.setActive(false);
        rule.setDuration(10);
        addRule(defaultCategory, rule);
    }

    public void updateList(){
        categories = LitePal.findAll(Category.class, true);
        rules = LitePal.findAll(Rule.class, true);
        messages = LitePal.findAll(Message.class,true);
    }

    public List<Category> getCategories() {
        this.updateList();
        return this.categories;
    }

    public List<Rule> getRules() {
        this.updateList();
        return this.rules;
    }

    public List<Message> getMessages() {
        this.updateList();
        return this.messages;
    }

    public void addCategory(Category category) {
        categories.add(category);
        category.save();
    }

    public Category getCategoryById(long id) {
        List<Category> result = LitePal.where("id = ?", String.valueOf(id)).find(
                Category.class, true);
        if (result.size() <= 0) return null;
        else return result.get(0);
    }

    public Category getCategoryByName(String name){
        List<Category> result = LitePal.where("name = ?", name).find(
                Category.class, false);
        if(result.size() == 0) return null;
        else return result.get(0);
    }

    public void deleteCategory(String name) {
        if (name.equals("Default"))
            return;
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getName().equals(name)) {
                Category category =
                        LitePal.where("name = ?", "Default").find(
                                Category.class, true).get(0);
                for (int j = 0; j < categories.get(i).getRule().size(); j++)
                    categories.get(i).getRule().get(j).setCategory(category);
                LitePal.delete(Category.class, categories.get(i).getId());
                categories.remove(i);
                break;
            }
        }
    }

    public void updateCategory(Category category) {
        category.save();
    }

    public void addRule(Category category, Rule rule) {
        rule.setCategory(category);
        rules.add(rule);
        rule.save();
    }

    public Rule getRuleById(long id) {
        List<Rule> result = LitePal.where("id = ?", String.valueOf(id)).find(
                Rule.class, true);
        if (result.size() <= 0) return null;
        else return result.get(0);
    }

    public void deleteRule(long id) {
        for (int i = 0; i < rules.size(); i++) {
            if (rules.get(i).getId() == id) {
                LitePal.delete(Rule.class, rules.get(i).getId());
                rules.remove(i);
                break;
            }
        }
    }

    public void updateRule(Category category, Rule rule) {
        // 必需：激进查询只能递归一层
        rule.setCategory(category);
        rule.save();
    }

    public void addMessage(Rule rule, Message message) {
        message.setRule(rule);
        messages.add(message);
        message.save();
    }

    public void deleteMessage(long id) {
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getId() == id) {
                LitePal.delete(Message.class, id);
                messages.remove(i);
                break;
            }
        }
    }
}
