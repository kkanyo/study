package tobyspring.user.dao;

import java.util.List;

import tobyspring.user.domain.User;

public interface UserDao {
    public void add(User user);
    public User get(String id);
    public List<User> getAll();
    public void deleteAll();
    public int getCount();
    public void update(User user);
}
