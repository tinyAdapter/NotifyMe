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
        list_category = LitePal.findAll(Category.class);
        list_rule = LitePal.findAll(Rule.class);
        list_msg = LitePal.findAll(Message.class);

        for (int i = 0; i < list_category.size(); i++) {
            if (list_category.get(i).getName().equals("Default"))
                return;
        }

        Category category = new Category();
        category.setName("Default");
        addCategory(category);
    }

    public void updataList(){
        list_category = LitePal.findAll(Category.class);
        list_rule = LitePal.findAll(Rule.class);
        list_msg = LitePal.findAll(Message.class);
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
