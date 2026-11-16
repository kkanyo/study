package tobyspring.user.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import tobyspring.user.domain.User;

public interface UserService {
    void add(User user);
    void deleteAll();
    void update(User user);
    void upgradeLevels();
    
    @Transactional(readOnly = true)
    User get(String id);
    
    @Transactional(readOnly = true)
    List<User> getAll();
}
